package com.openclassrooms.safetynet.dto;

import lombok.Data;
import java.util.List;

@Data
public class FireResidentDTO {
    private String firstName;
    private String lastName;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;

    public FireResidentDTO() {
    }

    public FireResidentDTO(String firstName, String lastName, String phone,
                           int age, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.age = age;
        this.medications = medications;
        this.allergies = allergies;
    }
}