package com.openclassrooms.safetynet.dto;

import lombok.Data;
import java.util.List;

@Data
public class FloodHomeDTO {
    private String address;
    private String stationNumber;
    private List<FloodResidentDTO> residents;

    // Default constructor
    public FloodHomeDTO() {
    }

    // Constructor with parameters
    public FloodHomeDTO(String address, String stationNumber, List<FloodResidentDTO> residents) {
        this.address = address;
        this.stationNumber = stationNumber;
        this.residents = residents;
    }
}