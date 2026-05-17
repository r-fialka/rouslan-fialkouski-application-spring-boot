package com.openclassrooms.safetynet.dto;

import lombok.Data;
import java.util.List;

@Data
public class ChildInfo {
    private String firstName;
    private String lastName;
    private int age;
    private List<String> householdMembers;

    // Default constructor (required for Jackson)
    public ChildInfo() {
    }

    // Constructor with all fields
    public ChildInfo(String firstName, String lastName, int age, List<String> householdMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.householdMembers = householdMembers;
    }
}