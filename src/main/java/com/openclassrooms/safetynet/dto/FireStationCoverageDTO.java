package com.openclassrooms.safetynet.dto;

import lombok.Data;
import java.util.List;

@Data
public class FireStationCoverageDTO {
    private List<PersonCoverageInfo> persons;
    private int adultsCount;
    private int childrenCount;

    // Default constructor
    public FireStationCoverageDTO() {
    }

    // Constructor with parameters
    public FireStationCoverageDTO(List<PersonCoverageInfo> persons, int adultsCount, int childrenCount) {
        this.persons = persons;
        this.adultsCount = adultsCount;
        this.childrenCount = childrenCount;
    }
}