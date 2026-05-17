package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataRepositoryTest {

    @Autowired
    private DataRepository dataRepository;

    // Test data
    private Person testPerson;
    private FireStation testFirestation;
    private MedicalRecord testMedicalRecord;

    /**
     * Set up test data before each test
     */
    @BeforeEach
    void setUp() {
        // Create test person
        testPerson = new Person();
        testPerson.setFirstName("Test");
        testPerson.setLastName("Person");
        testPerson.setAddress("Test Address");
        testPerson.setPhone("000-000-0000");
        testPerson.setEmail("test@email.com");

        // Create test firestation mapping
        testFirestation = new FireStation();
        testFirestation.setAddress("Test Address");
        testFirestation.setStation("99");

        // Create test medical record
        testMedicalRecord = new MedicalRecord();
        testMedicalRecord.setFirstName("Test");
        testMedicalRecord.setLastName("Person");
        testMedicalRecord.setBirthdate("01/01/2000");
        testMedicalRecord.setMedications(List.of());
        testMedicalRecord.setAllergies(List.of());
    }

    // ==================== DATA LOADING TESTS ====================

    /**
     * Test that data.json loads successfully and all data collections are not null
     */
    @Test
    void testDataRepositoryLoadsSuccessfully() {
        assertNotNull(dataRepository.getRootData());
        assertNotNull(dataRepository.getRootData().getPersons());
        assertNotNull(dataRepository.getRootData().getFirestations());
        assertNotNull(dataRepository.getRootData().getMedicalrecords());
    }

    // ==================== PERSON CRUD TESTS ====================

    /**
     * Test adding a new person increases the list size by 1
     */
    @Test
    void testAddPerson() {
        int initialSize = dataRepository.getRootData().getPersons().size();

        dataRepository.addPerson(testPerson);

        int newSize = dataRepository.getRootData().getPersons().size();
        assertEquals(initialSize + 1, newSize);
    }

    /**
     * Test updating an existing person successfully
     */
    @Test
    void testUpdatePerson_Success() {
        // First add the person
        dataRepository.addPerson(testPerson);

        Person updatedPerson = new Person();
        updatedPerson.setFirstName("Test");
        updatedPerson.setLastName("Person");
        updatedPerson.setAddress("Updated Address");
        updatedPerson.setPhone("111-111-1111");

        boolean result = dataRepository.updatePerson("Test", "Person", updatedPerson);

        assertTrue(result);
    }

    /**
     * Test updating a non-existent person returns false
     */
    @Test
    void testUpdatePerson_NotFound() {
        Person updatedPerson = new Person();
        boolean result = dataRepository.updatePerson("Nonexistent", "Person", updatedPerson);

        assertFalse(result);
    }

    /**
     * Test deleting an existing person successfully
     */
    @Test
    void testDeletePerson_Success() {
        dataRepository.addPerson(testPerson);

        boolean result = dataRepository.deletePerson("Test", "Person");

        assertTrue(result);
    }

    /**
     * Test deleting a non-existent person returns false
     */
    @Test
    void testDeletePerson_NotFound() {
        boolean result = dataRepository.deletePerson("Nonexistent", "Person");

        assertFalse(result);
    }

    // ==================== FIRESTATION CRUD TESTS ====================

    /**
     * Test adding a new firestation mapping increases the list size by 1
     */
    @Test
    void testAddFirestation() {
        int initialSize = dataRepository.getRootData().getFirestations().size();

        dataRepository.addFirestation(testFirestation);

        int newSize = dataRepository.getRootData().getFirestations().size();
        assertEquals(initialSize + 1, newSize);
    }

    /**
     * Test updating an existing firestation mapping successfully
     */
    @Test
    void testUpdateFirestation_Success() {
        dataRepository.addFirestation(testFirestation);

        boolean result = dataRepository.updateFirestation("Test Address", "100");

        assertTrue(result);
    }

    /**
     * Test updating a non-existent firestation mapping returns false
     */
    @Test
    void testUpdateFirestation_NotFound() {
        boolean result = dataRepository.updateFirestation("Unknown Address", "100");

        assertFalse(result);
    }

    /**
     * Test deleting a firestation mapping by address successfully
     */
    @Test
    void testDeleteFirestationByAddress_Success() {
        dataRepository.addFirestation(testFirestation);

        boolean result = dataRepository.deleteFirestationByAddress("Test Address");

        assertTrue(result);
    }

    /**
     * Test deleting a non-existent firestation mapping by address returns false
     */
    @Test
    void testDeleteFirestationByAddress_NotFound() {
        boolean result = dataRepository.deleteFirestationByAddress("Unknown Address");

        assertFalse(result);
    }

    /**
     * Test deleting firestation mappings by station number successfully
     */
    @Test
    void testDeleteFirestationByStation_Success() {
        dataRepository.addFirestation(testFirestation);

        boolean result = dataRepository.deleteFirestationByStation("99");

        assertTrue(result);
    }

    /**
     * Test deleting non-existent station number returns false
     */
    @Test
    void testDeleteFirestationByStation_NotFound() {
        boolean result = dataRepository.deleteFirestationByStation("999");

        assertFalse(result);
    }

    // ==================== MEDICAL RECORD CRUD TESTS ====================

    /**
     * Test adding a new medical record increases the list size by 1
     */
    @Test
    void testAddMedicalRecord() {
        int initialSize = dataRepository.getRootData().getMedicalrecords().size();

        dataRepository.addMedicalRecord(testMedicalRecord);

        int newSize = dataRepository.getRootData().getMedicalrecords().size();
        assertEquals(initialSize + 1, newSize);
    }

    /**
     * Test updating an existing medical record successfully
     */
    @Test
    void testUpdateMedicalRecord_Success() {
        dataRepository.addMedicalRecord(testMedicalRecord);

        MedicalRecord updated = new MedicalRecord();
        updated.setFirstName("Test");
        updated.setLastName("Person");
        updated.setBirthdate("01/01/2000");
        updated.setMedications(List.of("newMed:100mg"));

        boolean result = dataRepository.updateMedicalRecord("Test", "Person", updated);

        assertTrue(result);
    }

    /**
     * Test updating a non-existent medical record returns false
     */
    @Test
    void testUpdateMedicalRecord_NotFound() {
        MedicalRecord updated = new MedicalRecord();
        boolean result = dataRepository.updateMedicalRecord("Nonexistent", "Person", updated);

        assertFalse(result);
    }

    /**
     * Test deleting an existing medical record successfully
     */
    @Test
    void testDeleteMedicalRecord_Success() {
        dataRepository.addMedicalRecord(testMedicalRecord);

        boolean result = dataRepository.deleteMedicalRecord("Test", "Person");

        assertTrue(result);
    }

    /**
     * Test deleting a non-existent medical record returns false
     */
    @Test
    void testDeleteMedicalRecord_NotFound() {
        boolean result = dataRepository.deleteMedicalRecord("Nonexistent", "Person");

        assertFalse(result);
    }
}