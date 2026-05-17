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

    public RootData getRootData() {
        return rootData;
    }


    // ========== METHODS FOR PERSON ==========

    public void addPerson(Person person) {
        rootData.getPersons().add(person);
        log.info("Добавлен человек: {} {}", person.getFirstName(), person.getLastName());
    }

    public boolean updatePerson(String firstName, String lastName, Person updatedPerson) {
        List<Person> persons = rootData.getPersons();
        for (int i = 0; i < persons.size(); i++) {
            Person p = persons.get(i);
            if (p.getFirstName().equalsIgnoreCase(firstName) &&
                    p.getLastName().equalsIgnoreCase(lastName)) {
                // Keep the first and last names (they remain unchanged)
                updatedPerson.setFirstName(p.getFirstName());
                updatedPerson.setLastName(p.getLastName());
                persons.set(i, updatedPerson);
                log.info("The person has been updated: {} {}", firstName, lastName);
                return true;
            }
        }
        log.warn("The person {} {} was not found for the update", firstName, lastName);
        return false;
    }

    public boolean deletePerson(String firstName, String lastName) {
        boolean removed = rootData.getPersons().removeIf(p ->
                p.getFirstName().equalsIgnoreCase(firstName) &&
                        p.getLastName().equalsIgnoreCase(lastName));
        if (removed) {
            log.info("The person has been removed: {} {}", firstName, lastName);
        } else {
            log.warn("The person {} {} was not found for deletion", firstName, lastName);
        }
        return removed;
    }

    // ========== METHODS FOR FIRESTATION ==========

    public void addFirestation(FireStation firestation) {
        rootData.getFirestations().add(firestation);
        log.info("Added connection: address {} -> station {}",
                firestation.getAddress(), firestation.getStation());
    }

    public boolean updateFirestation(String address, String newStationNumber) {
        List<FireStation> firestations = rootData.getFirestations();
        for (FireStation fs : firestations) {
            if (fs.getAddress().equalsIgnoreCase(address)) {
                fs.setStation(newStationNumber);
                log.info("The station for the address {} has been updated: now {}", address, newStationNumber);
                return true;
            }
        }
        log.warn("The address {} could not be found for the station update", address);
        return false;
    }

    public boolean deleteFirestationByAddress(String address) {
        boolean removed = rootData.getFirestations().removeIf(fs ->
                fs.getAddress().equalsIgnoreCase(address));
        if (removed) {
            log.info("The link for the address {} has been removed", address);
        } else {
            log.warn("The address {} could not be found for deletion", address);
        }
        return removed;
    }

    public boolean deleteFirestationByStation(String stationNumber) {
        boolean removed = rootData.getFirestations().removeIf(fs ->
                fs.getStation().equals(stationNumber));
        if (removed) {
            log.info("All connections for station {} have been removed", stationNumber);
        } else {
            log.warn("The station {} was not found for deletion", stationNumber);
        }
        return removed;
    }

    // ========== METHODS FOR MEDICALRECORD ==========

    public void addMedicalRecord(MedicalRecord medicalRecord) {
        rootData.getMedicalrecords().add(medicalRecord);
        log.info("A medical record has been added for: {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());
    }

    public boolean updateMedicalRecord(String firstName, String lastName, MedicalRecord updatedRecord) {
        List<MedicalRecord> records = rootData.getMedicalrecords();
        for (int i = 0; i < records.size(); i++) {
            MedicalRecord mr = records.get(i);
            if (mr.getFirstName().equalsIgnoreCase(firstName) &&
                    mr.getLastName().equalsIgnoreCase(lastName)) {
                updatedRecord.setFirstName(firstName);
                updatedRecord.setLastName(lastName);
                records.set(i, updatedRecord);
                log.info("The medical record has been updated for: {} {}", firstName, lastName);
                return true;
            }
        }
        log.warn("No medical record found for {} {}", firstName, lastName);
        return false;
    }

    public boolean deleteMedicalRecord(String firstName, String lastName) {
        boolean removed = rootData.getMedicalrecords().removeIf(mr ->
                mr.getFirstName().equalsIgnoreCase(firstName) &&
                        mr.getLastName().equalsIgnoreCase(lastName));
        if (removed) {
            log.info("Medical record deleted for: {} {}", firstName, lastName);
        } else {
            log.warn("No medical record found for {} {}", firstName, lastName);
        }
        return removed;
    }
}