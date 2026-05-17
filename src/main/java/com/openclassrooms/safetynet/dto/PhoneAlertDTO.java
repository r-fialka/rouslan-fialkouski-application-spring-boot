package com.openclassrooms.safetynet.dto;

import lombok.Data;
import java.util.List;

@Data
public class PhoneAlertDTO {
    private List<String> phones;

    // Default constructor
    public PhoneAlertDTO() {
    }

    // Constructor with parameters
    public PhoneAlertDTO(List<String> phones) {
        this.phones = phones;
    }
}