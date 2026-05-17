package com.openclassrooms.safetynet.dto;

import lombok.Data;

@Data
public class PersonCoverageInfo {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    public PersonCoverageInfo() {
    }

    public PersonCoverageInfo(String firstName, String lastName, String address, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
    }
}