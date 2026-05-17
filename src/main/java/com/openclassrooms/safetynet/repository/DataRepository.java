package com.openclassrooms.safetynet.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.model.RootData;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Slf4j
@Repository
public class DataRepository {

    private RootData rootData;

    // Loads data from data.json file at application startup
    @PostConstruct
    public void loadData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("data.json");
            rootData = mapper.readValue(resource.getFile(), RootData.class);
            log.info("Data loaded successfully!");
        } catch (IOException e) {
            log.error("Error loading data.json: {}", e.getMessage());
            throw new RuntimeException("Unable to load data.json");
        }
    }

    // Returns the root data object containing all persons, firestations and medical records
    public RootData getRootData() {
        return rootData;
    }

    // ==================== PERSON METHODS ====================

    // Adds a new person to the list
    public void addPerson(Person person) {
        rootData.getPersons().add(person);
        log.info("Added person: {} {}", person.getFirstName(), person.getLastName());
    }

    // Updates an existing person's data (first and last name cannot be changed)
    public boolean updatePerson(String firstName, String lastName, Person updatedPerson) {
        List<Person> persons = rootData.getPersons();
        for (int i = 0; i < persons.size(); i++) {
            Person p = persons.get(i);
            if (p.getFirstName().equalsIgnoreCase(firstName) &&
                    p.getLastName().equalsIgnoreCase(lastName)) {
                updatedPerson.setFirstName(p.getFirstName());
                updatedPerson.setLastName(p.getLastName());
                persons.set(i, updatedPerson);
                log.info("Updated person: {} {}", firstName, lastName);
                return true;
            }
        }
        log.warn("Person {} {} not found for update", firstName, lastName);
        return false;
    }

    // Deletes a person by first and last name
    public boolean deletePerson(String firstName, String lastName) {
        boolean removed = rootData.getPersons().removeIf(p ->
                p.getFirstName().equalsIgnoreCase(firstName) &&
                        p.getLastName().equalsIgnoreCase(lastName));
        if (removed) {
            log.info("Deleted person: {} {}", firstName, lastName);
        } else {
            log.warn("Person {} {} not found for deletion", firstName, lastName);
        }
        return removed;
    }

    // ==================== FIRESTATION METHODS ====================

    // Adds a new address to firestation mapping
    public void addFirestation(FireStation firestation) {
        rootData.getFirestations().add(firestation);
        log.info("Added mapping: address {} -> station {}",
                firestation.getAddress(), firestation.getStation());
    }

    // Updates the station number for a given address
    public boolean updateFirestation(String address, String newStationNumber) {
        List<FireStation> firestations = rootData.getFirestations();
        for (FireStation fs : firestations) {
            if (fs.getAddress().equalsIgnoreCase(address)) {
                fs.setStation(newStationNumber);
                log.info("Updated station for address {}: now {}", address, newStationNumber);
                return true;
            }
        }
        log.warn("Address {} not found for station update", address);
        return false;
    }

    // Deletes a firestation mapping by address
    public boolean deleteFirestationByAddress(String address) {
        boolean removed = rootData.getFirestations().removeIf(fs ->
                fs.getAddress().equalsIgnoreCase(address));
        if (removed) {
            log.info("Deleted mapping for address {}", address);
        } else {
            log.warn("Address {} not found for deletion", address);
        }
        return removed;
    }

    // Deletes all firestation mappings by station number
    public boolean deleteFirestationByStation(String stationNumber) {
        boolean removed = rootData.getFirestations().removeIf(fs ->
                fs.getStation().equals(stationNumber));
        if (removed) {
            log.info("Deleted all mappings for station {}", stationNumber);
        } else {
            log.warn("Station {} not found for deletion", stationNumber);
        }
        return removed;
    }

    // ==================== MEDICAL RECORD METHODS ====================

    // Adds a new medical record
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        rootData.getMedicalrecords().add(medicalRecord);
        log.info("Added medical record for: {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());
    }

    // Updates an existing medical record (first and last name cannot be changed)
    public boolean updateMedicalRecord(String firstName, String lastName, MedicalRecord updatedRecord) {
        List<MedicalRecord> records = rootData.getMedicalrecords();
        for (int i = 0; i < records.size(); i++) {
            MedicalRecord mr = records.get(i);
            if (mr.getFirstName().equalsIgnoreCase(firstName) &&
                    mr.getLastName().equalsIgnoreCase(lastName)) {
                updatedRecord.setFirstName(firstName);
                updatedRecord.setLastName(lastName);
                records.set(i, updatedRecord);
                log.info("Updated medical record for: {} {}", firstName, lastName);
                return true;
            }
        }
        log.warn("Medical record not found for: {} {}", firstName, lastName);
        return false;
    }

    // Deletes a medical record by first and last name
    public boolean deleteMedicalRecord(String firstName, String lastName) {
        boolean removed = rootData.getMedicalrecords().removeIf(mr ->
                mr.getFirstName().equalsIgnoreCase(firstName) &&
                        mr.getLastName().equalsIgnoreCase(lastName));
        if (removed) {
            log.info("Deleted medical record for: {} {}", firstName, lastName);
        } else {
            log.warn("Medical record not found for: {} {}", firstName, lastName);
        }
        return removed;
    }
}