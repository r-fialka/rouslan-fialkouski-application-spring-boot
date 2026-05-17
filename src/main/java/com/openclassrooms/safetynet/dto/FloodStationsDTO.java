package com.openclassrooms.safetynet.dto;

import lombok.Data;
import java.util.List;

@Data
public class FloodStationsDTO {
    private List<FloodHomeDTO> homes;

    // Default constructor
    public FloodStationsDTO() {
    }

    // Constructor with parameters
    public FloodStationsDTO(List<FloodHomeDTO> homes) {
        this.homes = homes;
    }
}