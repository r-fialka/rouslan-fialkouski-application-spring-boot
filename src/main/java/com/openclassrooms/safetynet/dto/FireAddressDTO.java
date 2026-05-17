package com.openclassrooms.safetynet.dto;

import lombok.Data;
import java.util.List;

@Data
public class FireAddressDTO {
    private String stationNumber;
    private List<FireResidentDTO> residents;

    // Default constructor
    public FireAddressDTO() {
    }

    // Constructor with parameters
    public FireAddressDTO(String stationNumber, List<FireResidentDTO> residents) {
        this.stationNumber = stationNumber;
        this.residents = residents;
    }
}