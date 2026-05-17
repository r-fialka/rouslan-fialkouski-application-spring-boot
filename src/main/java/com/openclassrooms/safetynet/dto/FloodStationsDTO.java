package com.openclassrooms.safetynet.dto;

import lombok.Data;
import java.util.List;

@Data
public class FloodStationsDTO {
    private List<FloodHomeDTO> homes;

    public FloodStationsDTO() {
    }

    public FloodStationsDTO(List<FloodHomeDTO> homes) {
        this.homes = homes;
    }
}