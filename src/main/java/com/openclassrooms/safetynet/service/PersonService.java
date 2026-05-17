package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.*;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private final DataRepository dataRepository;

    // Finds all persons living at a specific address
    public List<Person> findPersonsByAddress(String address) {
        log.debug("Search for people by address: {}", address);

        List<Person> result = dataRepository.getRootData().getPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());

        log.debug("Found {} people at this address {}", result.size(), address);
        return result;
    }

    // Finds a person's medical record by first and last name
    public MedicalRecord findMedicalRecordByPerson(Person person) {
        return dataRepository.getRootData().getMedicalrecords().stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(person.getFirstName())
                        && mr.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst()
                .orElse(null);
    }

    // Calculates a person's age based on their birthdate in medical records
    public int calculateAge(Person person) {
        MedicalRecord medicalRecord = findMedicalRecordByPerson(person);

        if (medicalRecord == null) {
            log.warn("There is no medical record for {} {}", person.getFirstName(), person.getLastName());
            return 0;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), formatter);
        LocalDate today = LocalDate.now();
        int age = Period.between(birthDate, today).getYears();

        log.debug("{} {} - age: {}", person.getFirstName(), person.getLastName(), age);
        return age;
    }

    // Returns list of children (≤18) at an address with their household members
    public ChildAlertDTO getChildrenByAddress(String address) {
        log.info("Search for children by address: {}", address);

        List<Person> residents = findPersonsByAddress(address);

        if (residents.isEmpty()) {
            log.info("No one lives at {}", address);
            return new ChildAlertDTO(List.of());
        }

        List<String> allHouseholdMembers = residents.stream()
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .collect(Collectors.toList());

        List<ChildInfo> children = residents.stream()
                .filter(person -> calculateAge(person) <= 18)
                .map(person -> {
                    int age = calculateAge(person);
                    String fullName = person.getFirstName() + " " + person.getLastName();

                    List<String> otherMembers = allHouseholdMembers.stream()
                            .filter(name -> !name.equals(fullName))
                            .collect(Collectors.toList());

                    return new ChildInfo(
                            person.getFirstName(),
                            person.getLastName(),
                            age,
                            otherMembers
                    );
                })
                .collect(Collectors.toList());

        log.info("{} children were found at {}", children.size(), address);
        return new ChildAlertDTO(children);
    }

    // Returns phone numbers of all residents served by a fire station
    public PhoneAlertDTO getPhonesByStation(String stationNumber) {
        log.info("Search for phones for the station: {}", stationNumber);

        List<String> addresses = dataRepository.getRootData().getFirestations().stream()
                .filter(fs -> fs.getStation().equals(stationNumber))
                .map(fs -> fs.getAddress())
                .collect(Collectors.toList());

        if (addresses.isEmpty()) {
            log.info("Station {} not found", stationNumber);
            return new PhoneAlertDTO(List.of());
        }

        List<String> phones = dataRepository.getRootData().getPersons().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .map(person -> person.getPhone())
                .distinct()
                .collect(Collectors.toList());

        log.info("Found {} phones for station {}", phones.size(), stationNumber);
        return new PhoneAlertDTO(phones);
    }

    // Returns all email addresses of residents in a specific city
    public CommunityEmailDTO getEmailsByCity(String city) {
        log.info("Search for email addresses in a city: {}", city);

        List<String> emails = dataRepository.getRootData().getPersons().stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(person -> person.getEmail())
                .distinct()
                .collect(Collectors.toList());

        log.info("Found {} email in the city of {}", emails.size(), city);
        return new CommunityEmailDTO(emails);
    }

    // Returns residents served by a station with adult and child counts
    public FireStationCoverageDTO getPersonsByStation(String stationNumber) {
        log.info("Looking for people for the station: {}", stationNumber);

        List<String> addresses = dataRepository.getRootData().getFirestations().stream()
                .filter(fs -> fs.getStation().equals(stationNumber))
                .map(fs -> fs.getAddress())
                .collect(Collectors.toList());

        if (addresses.isEmpty()) {
            log.info("Station {} not found", stationNumber);
            return new FireStationCoverageDTO(List.of(), 0, 0);
        }

        List<Person> personsAtStation = dataRepository.getRootData().getPersons().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .collect(Collectors.toList());

        int adultsCount = 0;
        int childrenCount = 0;

        for (Person person : personsAtStation) {
            int age = calculateAge(person);
            if (age <= 18) {
                childrenCount++;
            } else {
                adultsCount++;
            }
        }

        List<PersonCoverageInfo> personsInfo = personsAtStation.stream()
                .map(person -> new PersonCoverageInfo(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getPhone()
                ))
                .collect(Collectors.toList());

        log.info("{} people were found at {} station (adults: {}, children: {})",
                personsInfo.size(), stationNumber, adultsCount, childrenCount);

        return new FireStationCoverageDTO(personsInfo, adultsCount, childrenCount);
    }

    // Returns person details (age, medications, allergies) by last name
    public PersonInfoResponseDTO getPersonInfoByLastName(String lastName) {
        log.info("Search for people with the last name: {}", lastName);

        List<Person> persons = dataRepository.getRootData().getPersons().stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());

        if (persons.isEmpty()) {
            log.info("No people with the last name {} were found", lastName);
            return new PersonInfoResponseDTO(List.of());
        }

        List<PersonInfoDTO> result = persons.stream()
                .map(person -> {
                    int age = calculateAge(person);
                    MedicalRecord medicalRecord = findMedicalRecordByPerson(person);

                    List<String> medications = (medicalRecord != null)
                            ? medicalRecord.getMedications()
                            : List.of();
                    List<String> allergies = (medicalRecord != null)
                            ? medicalRecord.getAllergies()
                            : List.of();

                    return new PersonInfoDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            person.getAddress(),
                            age,
                            person.getEmail(),
                            medications,
                            allergies
                    );
                })
                .collect(Collectors.toList());

        log.info("Found {} people with the last name {}", result.size(), lastName);
        return new PersonInfoResponseDTO(result);
    }

    // Finds the station number serving a given address
    public String findStationByAddress(String address) {
        return dataRepository.getRootData().getFirestations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .map(fs -> fs.getStation())
                .findFirst()
                .orElse(null);
    }

    // Returns residents at an address with station number and medical records
    public FireAddressDTO getFireByAddress(String address) {
        log.info("Search for residents by address: {}", address);

        String stationNumber = findStationByAddress(address);

        if (stationNumber == null) {
            log.info("The address {} is not served by the station", address);
            return new FireAddressDTO(null, List.of());
        }

        List<Person> residents = findPersonsByAddress(address);

        if (residents.isEmpty()) {
            log.info("No one lives at {}", address);
            return new FireAddressDTO(stationNumber, List.of());
        }

        List<FireResidentDTO> residentInfo = residents.stream()
                .map(person -> {
                    int age = calculateAge(person);
                    MedicalRecord medicalRecord = findMedicalRecordByPerson(person);
                    List<String> medications = (medicalRecord != null)
                            ? medicalRecord.getMedications()
                            : List.of();
                    List<String> allergies = (medicalRecord != null)
                            ? medicalRecord.getAllergies()
                            : List.of();

                    return new FireResidentDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            person.getPhone(),
                            age,
                            medications,
                            allergies
                    );
                })
                .collect(Collectors.toList());

        log.info("Found {} residents at {}, station {}",
                residentInfo.size(), address, stationNumber);

        return new FireAddressDTO(stationNumber, residentInfo);
    }

    // Returns all homes served by a list of stations, grouped by address with residents and medical records
    public FloodStationsDTO getFloodByStations(List<String> stationNumbers) {
        log.info("Search for station buildings: {}", stationNumbers);

        List<String> addresses = dataRepository.getRootData().getFirestations().stream()
                .filter(fs -> stationNumbers.contains(fs.getStation()))
                .map(fs -> fs.getAddress())
                .distinct()
                .collect(Collectors.toList());

        if (addresses.isEmpty()) {
            log.info("No addresses found for stations {}", stationNumbers);
            return new FloodStationsDTO(List.of());
        }

        List<FloodHomeDTO> homes = addresses.stream()
                .map(address -> {
                    String stationNumber = findStationByAddress(address);
                    List<Person> residents = findPersonsByAddress(address);

                    List<FloodResidentDTO> residentInfo = residents.stream()
                            .map(person -> {
                                int age = calculateAge(person);
                                MedicalRecord medicalRecord = findMedicalRecordByPerson(person);
                                List<String> medications = (medicalRecord != null)
                                        ? medicalRecord.getMedications()
                                        : List.of();
                                List<String> allergies = (medicalRecord != null)
                                        ? medicalRecord.getAllergies()
                                        : List.of();

                                return new FloodResidentDTO(
                                        person.getFirstName(),
                                        person.getLastName(),
                                        person.getPhone(),
                                        age,
                                        medications,
                                        allergies
                                );
                            })
                            .collect(Collectors.toList());

                    return new FloodHomeDTO(address, stationNumber, residentInfo);
                })
                .collect(Collectors.toList());

        log.info("Found {} homes for {} stations", homes.size(), stationNumbers);
        return new FloodStationsDTO(homes);
    }
}