package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.*;
import com.openclassrooms.safetynet.model.*;
import com.openclassrooms.safetynet.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private PersonService personService;

    private RootData rootData;

    /**
     * Set up test data before each test
     * Creates persons, firestations and medical records for testing
     */
    @BeforeEach
    void setUp() {
        // Initialize root data container
        rootData = new RootData();

        // ==================== CREATE TEST PERSONS ====================

        // Person 1: John Boyd - lives at 1509 Culver St
        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Boyd");
        person1.setAddress("1509 Culver St");
        person1.setCity("Culver");
        person1.setPhone("841-874-6512");
        person1.setEmail("jaboyd@email.com");

        // Person 2: Jane Doe - lives at 123 Main St
        Person person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("Doe");
        person2.setAddress("123 Main St");
        person2.setCity("Culver");
        person2.setPhone("123-456-7890");
        person2.setEmail("jane@email.com");

        rootData.setPersons(Arrays.asList(person1, person2));

        // ==================== CREATE TEST FIRESTATIONS ====================

        // Firestation 3 serves 1509 Culver St
        FireStation fs1 = new FireStation();
        fs1.setAddress("1509 Culver St");
        fs1.setStation("3");

        // Firestation 1 serves 123 Main St
        FireStation fs2 = new FireStation();
        fs2.setAddress("123 Main St");
        fs2.setStation("1");

        rootData.setFirestations(Arrays.asList(fs1, fs2));

        // ==================== CREATE TEST MEDICAL RECORDS ====================

        // Medical record for John Boyd
        MedicalRecord mr1 = new MedicalRecord();
        mr1.setFirstName("John");
        mr1.setLastName("Boyd");
        mr1.setBirthdate("03/06/1984"); // 42 years old in 2026
        mr1.setMedications(Arrays.asList("aznol:350mg"));
        mr1.setAllergies(Arrays.asList("nillacilan"));

        rootData.setMedicalrecords(Arrays.asList(mr1));

        // Configure mock to return test data
        when(dataRepository.getRootData()).thenReturn(rootData);
    }

    // ==================== FIND PERSONS BY ADDRESS TESTS ====================

    /**
     * Test finding persons by existing address - should return 1 person
     */
    @Test
    void testFindPersonsByAddress() {
        List<Person> persons = personService.findPersonsByAddress("1509 Culver St");

        assertEquals(1, persons.size());
        assertEquals("John", persons.get(0).getFirstName());
    }

    /**
     * Test finding persons by non-existent address - should return empty list
     */
    @Test
    void testFindPersonsByAddress_NotFound() {
        List<Person> persons = personService.findPersonsByAddress("Unknown Address");

        assertEquals(0, persons.size());
    }

    // ==================== AGE CALCULATION TESTS ====================

    /**
     * Test age calculation for a person with valid medical record
     */
    @Test
    void testCalculateAge() {
        Person person = rootData.getPersons().get(0);
        int age = personService.calculateAge(person);

        assertEquals(42, age);
    }

    /**
     * Test age calculation for a person without medical record - should return 0
     */
    @Test
    void testCalculateAge_NoMedicalRecord() {
        Person person = new Person();
        person.setFirstName("NoRecord");
        person.setLastName("Person");

        int age = personService.calculateAge(person);

        assertEquals(0, age);
    }

    // ==================== PERSONS BY STATION TESTS ====================

    /**
     * Test getting persons by existing station number - should return list of residents
     */
    @Test
    void testGetPersonsByStation() {
        FireStationCoverageDTO result = personService.getPersonsByStation("3");

        assertEquals(1, result.getPersons().size());
        assertEquals("John", result.getPersons().get(0).getFirstName());
    }

    /**
     * Test getting persons by non-existent station - should return empty list with zero counts
     */
    @Test
    void testGetPersonsByStation_NotFound() {
        FireStationCoverageDTO result = personService.getPersonsByStation("99");

        assertEquals(0, result.getPersons().size());
        assertEquals(0, result.getAdultsCount());
        assertEquals(0, result.getChildrenCount());
    }

    /**
     * Test getting persons by empty station number - should return empty list
     */
    @Test
    void testGetPersonsByStation_InvalidStation() {
        FireStationCoverageDTO result = personService.getPersonsByStation("");

        assertTrue(result.getPersons().isEmpty());
        assertEquals(0, result.getAdultsCount());
        assertEquals(0, result.getChildrenCount());
    }

    /**
     * Test getting persons by station with both adults and children
     */
    @Test
    void testGetPersonsByStation_WithChildren() {
        // Create a child resident
        Person child = new Person();
        child.setFirstName("Tenley");
        child.setLastName("Boyd");
        child.setAddress("1509 Culver St");
        child.setPhone("841-874-6512");
        child.setEmail("tenley@email.com");
        child.setCity("Culver");

        Person adult = new Person();
        adult.setFirstName("John");
        adult.setLastName("Boyd");
        adult.setAddress("1509 Culver St");
        adult.setPhone("841-874-6512");
        adult.setEmail("john@email.com");
        adult.setCity("Culver");

        rootData.setPersons(Arrays.asList(child, adult));

        // Medical record for child (under 18)
        MedicalRecord childMr = new MedicalRecord();
        childMr.setFirstName("Tenley");
        childMr.setLastName("Boyd");
        childMr.setBirthdate("02/18/2012");  // 14 years old

        // Medical record for adult
        MedicalRecord adultMr = new MedicalRecord();
        adultMr.setFirstName("John");
        adultMr.setLastName("Boyd");
        adultMr.setBirthdate("03/06/1984");  // 42 years old

        rootData.setMedicalrecords(Arrays.asList(childMr, adultMr));

        FireStation fs = new FireStation();
        fs.setAddress("1509 Culver St");
        fs.setStation("3");
        rootData.setFirestations(List.of(fs));

        when(dataRepository.getRootData()).thenReturn(rootData);

        FireStationCoverageDTO result = personService.getPersonsByStation("3");

        assertNotNull(result);
        assertEquals(2, result.getPersons().size());
        assertEquals(1, result.getAdultsCount());
        assertEquals(1, result.getChildrenCount());

        boolean hasChild = result.getPersons().stream()
                .anyMatch(p -> p.getFirstName().equals("Tenley"));
        assertTrue(hasChild);
    }

    // ==================== PHONE ALERT TESTS ====================

    /**
     * Test getting phone numbers by station - should return list of phones
     */
    @Test
    void testGetPhonesByStation() {
        PhoneAlertDTO result = personService.getPhonesByStation("3");

        assertEquals(1, result.getPhones().size());
        assertEquals("841-874-6512", result.getPhones().get(0));
    }

    /**
     * Test getting phone numbers by non-existent station - should return empty list
     */
    @Test
    void testGetPhonesByStation_Empty() {
        PhoneAlertDTO result = personService.getPhonesByStation("99");
        assertNotNull(result);
        assertTrue(result.getPhones().isEmpty());
    }

    // ==================== COMMUNITY EMAIL TESTS ====================

    /**
     * Test getting emails by city - should return all emails in that city
     */
    @Test
    void testGetEmailsByCity() {
        CommunityEmailDTO result = personService.getEmailsByCity("Culver");

        assertEquals(2, result.getEmails().size());
        assertTrue(result.getEmails().contains("jaboyd@email.com"));
        assertTrue(result.getEmails().contains("jane@email.com"));
    }

    /**
     * Test getting emails by non-existent city - should return empty list
     */
    @Test
    void testGetEmailsByCity_Empty() {
        CommunityEmailDTO result = personService.getEmailsByCity("Unknown City");
        assertNotNull(result);
        assertTrue(result.getEmails().isEmpty());
    }

    // ==================== PERSON INFO TESTS ====================

    /**
     * Test getting person information by last name - should return full details
     */
    @Test
    void testGetPersonInfoByLastName() {
        PersonInfoResponseDTO result = personService.getPersonInfoByLastName("Boyd");

        assertEquals(1, result.getPersons().size());
        assertEquals("John", result.getPersons().get(0).getFirstName());
        assertEquals(42, result.getPersons().get(0).getAge());
        assertEquals(1, result.getPersons().get(0).getMedications().size());
        assertEquals(1, result.getPersons().get(0).getAllergies().size());
    }

    /**
     * Test getting person info by non-existent last name - should return empty list
     */
    @Test
    void testGetPersonInfoByLastName_Empty() {
        PersonInfoResponseDTO result = personService.getPersonInfoByLastName("Unknown");
        assertNotNull(result);
        assertTrue(result.getPersons().isEmpty());
    }

    /**
     * Test getting person info for person without medical record
     */
    @Test
    void testGetPersonInfoByLastName_NoMedicalRecord() {
        Person personWithoutMedicalRecord = new Person();
        personWithoutMedicalRecord.setFirstName("NoMed");
        personWithoutMedicalRecord.setLastName("Record");
        personWithoutMedicalRecord.setAddress("123 Test St");
        personWithoutMedicalRecord.setEmail("nomed@email.com");
        personWithoutMedicalRecord.setCity("Culver");

        rootData.setPersons(List.of(personWithoutMedicalRecord));
        rootData.setMedicalrecords(List.of());

        when(dataRepository.getRootData()).thenReturn(rootData);

        PersonInfoResponseDTO result = personService.getPersonInfoByLastName("Record");

        assertNotNull(result);
        assertEquals(1, result.getPersons().size());

        PersonInfoDTO personInfo = result.getPersons().get(0);
        assertEquals("NoMed", personInfo.getFirstName());
        assertNotNull(personInfo.getMedications());
        assertTrue(personInfo.getMedications().isEmpty());
        assertNotNull(personInfo.getAllergies());
        assertTrue(personInfo.getAllergies().isEmpty());
    }

    // ==================== FIRE ADDRESS TESTS ====================

    /**
     * Test getting fire information by address - should return station and residents
     */
    @Test
    void testGetFireByAddress() {
        FireAddressDTO result = personService.getFireByAddress("1509 Culver St");

        assertEquals("3", result.getStationNumber());
        assertEquals(1, result.getResidents().size());
        assertEquals("John", result.getResidents().get(0).getFirstName());
        assertEquals(42, result.getResidents().get(0).getAge());
    }

    /**
     * Test getting fire info by address without station - should return null station
     */
    @Test
    void testGetFireByAddress_NoStation() {
        FireAddressDTO result = personService.getFireByAddress("Unknown Address");
        assertNull(result.getStationNumber());
        assertTrue(result.getResidents().isEmpty());
    }

    /**
     * Test getting fire info by empty address
     */
    @Test
    void testGetFireByAddress_EmptyAddress() {
        FireAddressDTO result = personService.getFireByAddress("");
        assertNull(result.getStationNumber());
        assertTrue(result.getResidents().isEmpty());
    }

    /**
     * Test getting fire info for address that exists but has no residents
     */
    @Test
    void testGetFireByAddress_AddressExistsButNoResidents() {
        FireStation fs = new FireStation();
        fs.setAddress("Empty House Address");
        fs.setStation("5");
        rootData.setFirestations(List.of(fs));
        rootData.setPersons(List.of());

        when(dataRepository.getRootData()).thenReturn(rootData);

        FireAddressDTO result = personService.getFireByAddress("Empty House Address");

        assertNotNull(result);
        assertEquals("5", result.getStationNumber());
        assertTrue(result.getResidents().isEmpty());
    }

    /**
     * Test getting fire info for resident without medical record
     */
    @Test
    void testGetFireByAddress_ResidentWithoutMedicalRecord() {
        FireStation fs = new FireStation();
        fs.setAddress("123 Test St");
        fs.setStation("5");
        rootData.setFirestations(List.of(fs));

        Person personWithoutMedicalRecord = new Person();
        personWithoutMedicalRecord.setFirstName("NoMed");
        personWithoutMedicalRecord.setLastName("Record");
        personWithoutMedicalRecord.setAddress("123 Test St");
        personWithoutMedicalRecord.setPhone("000-000-0000");
        rootData.setPersons(List.of(personWithoutMedicalRecord));
        rootData.setMedicalrecords(List.of());

        when(dataRepository.getRootData()).thenReturn(rootData);

        FireAddressDTO result = personService.getFireByAddress("123 Test St");

        assertNotNull(result);
        assertEquals("5", result.getStationNumber());
        assertEquals(1, result.getResidents().size());

        FireResidentDTO resident = result.getResidents().get(0);
        assertNotNull(resident.getMedications());
        assertTrue(resident.getMedications().isEmpty());
        assertNotNull(resident.getAllergies());
        assertTrue(resident.getAllergies().isEmpty());
    }

    // ==================== FIND STATION BY ADDRESS TESTS ====================

    /**
     * Test finding station number by existing address
     */
    @Test
    void testFindStationByAddress_Success() {
        String station = personService.findStationByAddress("1509 Culver St");
        assertEquals("3", station);
    }

    /**
     * Test finding station number by non-existent address - should return null
     */
    @Test
    void testFindStationByAddress_NotFound() {
        String station = personService.findStationByAddress("Unknown Address");
        assertNull(station);
    }

    // ==================== CHILD ALERT TESTS ====================

    /**
     * Test getting children by address - should return list of children with family members
     */
    @Test
    void testGetChildrenByAddress_Success() {
        Person child1 = new Person();
        child1.setFirstName("Tenley");
        child1.setLastName("Boyd");
        child1.setAddress("1509 Culver St");

        Person child2 = new Person();
        child2.setFirstName("Roger");
        child2.setLastName("Boyd");
        child2.setAddress("1509 Culver St");

        Person adult = new Person();
        adult.setFirstName("John");
        adult.setLastName("Boyd");
        adult.setAddress("1509 Culver St");

        rootData.setPersons(Arrays.asList(child1, child2, adult));

        MedicalRecord mrChild1 = new MedicalRecord();
        mrChild1.setFirstName("Tenley");
        mrChild1.setLastName("Boyd");
        mrChild1.setBirthdate("02/18/2012");  // 14 years

        MedicalRecord mrChild2 = new MedicalRecord();
        mrChild2.setFirstName("Roger");
        mrChild2.setLastName("Boyd");
        mrChild2.setBirthdate("09/06/2017");  // 8 years

        MedicalRecord mrAdult = new MedicalRecord();
        mrAdult.setFirstName("John");
        mrAdult.setLastName("Boyd");
        mrAdult.setBirthdate("03/06/1984");

        rootData.setMedicalrecords(Arrays.asList(mrChild1, mrChild2, mrAdult));

        when(dataRepository.getRootData()).thenReturn(rootData);

        ChildAlertDTO result = personService.getChildrenByAddress("1509 Culver St");

        assertNotNull(result);
        assertEquals(2, result.getChildren().size());

        ChildInfo firstChild = result.getChildren().get(0);
        assertEquals("Tenley", firstChild.getFirstName());
        assertEquals(14, firstChild.getAge());
        assertTrue(firstChild.getHouseholdMembers().contains("John Boyd"));
    }

    /**
     * Test getting children by address with no children - should return empty list
     */
    @Test
    void testGetChildrenByAddress_NoChildren() {
        Person adult = new Person();
        adult.setFirstName("John");
        adult.setLastName("Boyd");
        adult.setAddress("1509 Culver St");
        rootData.setPersons(List.of(adult));

        MedicalRecord mrAdult = new MedicalRecord();
        mrAdult.setFirstName("John");
        mrAdult.setLastName("Boyd");
        mrAdult.setBirthdate("03/06/1984");
        rootData.setMedicalrecords(List.of(mrAdult));

        when(dataRepository.getRootData()).thenReturn(rootData);

        ChildAlertDTO result = personService.getChildrenByAddress("1509 Culver St");

        assertNotNull(result);
        assertTrue(result.getChildren().isEmpty());
    }

    /**
     * Test getting children by empty address - should return empty list
     */
    @Test
    void testGetChildrenByAddress_EmptyAddress() {
        when(dataRepository.getRootData()).thenReturn(rootData);

        ChildAlertDTO result = personService.getChildrenByAddress("Empty Address");

        assertNotNull(result);
        assertTrue(result.getChildren().isEmpty());
    }

    // ==================== FLOOD STATIONS TESTS ====================

    /**
     * Test getting flood information by stations - should return homes with residents
     */
    @Test
    void testGetFloodByStations_Success() {
        FireStation fs1 = new FireStation();
        fs1.setAddress("1509 Culver St");
        fs1.setStation("3");
        rootData.setFirestations(List.of(fs1));

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setPhone("841-874-6512");
        rootData.setPersons(List.of(person));

        MedicalRecord mr = new MedicalRecord();
        mr.setFirstName("John");
        mr.setLastName("Boyd");
        mr.setBirthdate("03/06/1984");
        mr.setMedications(List.of("aznol:350mg"));
        mr.setAllergies(List.of("nillacilan"));
        rootData.setMedicalrecords(List.of(mr));

        when(dataRepository.getRootData()).thenReturn(rootData);

        FloodStationsDTO result = personService.getFloodByStations(List.of("3"));

        assertNotNull(result);
        assertEquals(1, result.getHomes().size());

        FloodHomeDTO home = result.getHomes().get(0);
        assertEquals("1509 Culver St", home.getAddress());
        assertEquals("3", home.getStationNumber());
        assertEquals(1, home.getResidents().size());

        FloodResidentDTO resident = home.getResidents().get(0);
        assertEquals("John", resident.getFirstName());
        assertEquals(42, resident.getAge());
        assertEquals(1, resident.getMedications().size());
        assertEquals(1, resident.getAllergies().size());
    }

    /**
     * Test getting flood info with empty station list - should return empty list
     */
    @Test
    void testGetFloodByStations_NoStations() {
        FloodStationsDTO result = personService.getFloodByStations(List.of());
        assertNotNull(result);
        assertTrue(result.getHomes().isEmpty());
    }

    /**
     * Test getting flood info with non-existent station - should return empty list
     */
    @Test
    void testGetFloodByStations_StationNotFound() {
        rootData.setFirestations(List.of());
        when(dataRepository.getRootData()).thenReturn(rootData);

        FloodStationsDTO result = personService.getFloodByStations(List.of("99"));

        assertNotNull(result);
        assertTrue(result.getHomes().isEmpty());
    }

    /**
     * Test getting flood info for multiple stations
     */
    @Test
    void testGetFloodByStations_MultipleStations() {
        FireStation fs1 = new FireStation();
        fs1.setAddress("1509 Culver St");
        fs1.setStation("3");

        FireStation fs2 = new FireStation();
        fs2.setAddress("123 Main St");
        fs2.setStation("1");

        rootData.setFirestations(Arrays.asList(fs1, fs2));

        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Boyd");
        person1.setAddress("1509 Culver St");
        person1.setPhone("841-874-6512");

        Person person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("Doe");
        person2.setAddress("123 Main St");
        person2.setPhone("841-874-6513");

        rootData.setPersons(Arrays.asList(person1, person2));

        MedicalRecord mr1 = new MedicalRecord();
        mr1.setFirstName("John");
        mr1.setLastName("Boyd");
        mr1.setBirthdate("03/06/1984");

        MedicalRecord mr2 = new MedicalRecord();
        mr2.setFirstName("Jane");
        mr2.setLastName("Doe");
        mr2.setBirthdate("01/01/1990");

        rootData.setMedicalrecords(Arrays.asList(mr1, mr2));

        when(dataRepository.getRootData()).thenReturn(rootData);

        FloodStationsDTO result = personService.getFloodByStations(List.of("3", "1"));

        assertNotNull(result);
        assertEquals(2, result.getHomes().size());
    }

    /**
     * Test getting flood info for resident without medical record
     */
    @Test
    void testGetFloodByStations_ResidentWithoutMedicalRecord() {
        FireStation fs = new FireStation();
        fs.setAddress("123 Test St");
        fs.setStation("5");
        rootData.setFirestations(List.of(fs));

        Person personWithoutMedicalRecord = new Person();
        personWithoutMedicalRecord.setFirstName("NoMed");
        personWithoutMedicalRecord.setLastName("Record");
        personWithoutMedicalRecord.setAddress("123 Test St");
        personWithoutMedicalRecord.setPhone("000-000-0000");
        rootData.setPersons(List.of(personWithoutMedicalRecord));
        rootData.setMedicalrecords(List.of());

        when(dataRepository.getRootData()).thenReturn(rootData);

        FloodStationsDTO result = personService.getFloodByStations(List.of("5"));

        assertNotNull(result);
        assertEquals(1, result.getHomes().size());

        FloodHomeDTO home = result.getHomes().get(0);
        FloodResidentDTO resident = home.getResidents().get(0);

        assertNotNull(resident.getMedications());
        assertTrue(resident.getMedications().isEmpty());
        assertNotNull(resident.getAllergies());
        assertTrue(resident.getAllergies().isEmpty());
    }

    // ==================== MEDICAL RECORD LOOKUP TESTS ====================

    /**
     * Test finding medical record by person - success case
     */
    @Test
    void testFindMedicalRecordByPerson_Success() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");

        MedicalRecord result = personService.findMedicalRecordByPerson(person);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Boyd", result.getLastName());
    }

    /**
     * Test finding medical record by non-existent person - should return null
     */
    @Test
    void testFindMedicalRecordByPerson_NotFound() {
        Person person = new Person();
        person.setFirstName("Unknown");
        person.setLastName("Person");

        MedicalRecord result = personService.findMedicalRecordByPerson(person);

        assertNull(result);
    }

    /**
     * Test case-insensitive matching when finding medical record
     */
    @Test
    void testFindMedicalRecordByPerson_CaseInsensitiveMatch() {
        Person person = new Person();
        person.setFirstName("JOHN");
        person.setLastName("BOYD");

        MedicalRecord result = personService.findMedicalRecordByPerson(person);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Boyd", result.getLastName());
    }

    /**
     * Test finding medical record with wrong first name - should return null
     */
    @Test
    void testFindMedicalRecordByPerson_WrongFirstName() {
        Person person = new Person();
        person.setFirstName("Wrong");
        person.setLastName("Boyd");

        MedicalRecord result = personService.findMedicalRecordByPerson(person);

        assertNull(result);
    }

    /**
     * Test finding medical record with wrong last name - should return null
     */
    @Test
    void testFindMedicalRecordByPerson_WrongLastName() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Wrong");

        MedicalRecord result = personService.findMedicalRecordByPerson(person);

        assertNull(result);
    }
}