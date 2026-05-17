package com.openclassrooms.safetynet.model;

import lombok.Data;
import java.util.List;

@Data
public class RootData {
    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;
}