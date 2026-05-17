package com.openclassrooms.safetynet.dto;

import lombok.Data;
import java.util.List;

@Data
public class FloodHomeDTO {
    private String address;
    private String stationNumber;
    private List<FloodResidentDTO> residents;

    public FloodHomeDTO() {
    }

    public FloodHomeDTO(String address, String stationNumber, List<FloodResidentDTO> residents) {
        this.address = address;
        this.stationNumber = stationNumber;
        this.residents = residents;
    }
}