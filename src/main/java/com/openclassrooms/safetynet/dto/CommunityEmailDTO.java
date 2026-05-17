package com.openclassrooms.safetynet.dto;

import lombok.Data;
import java.util.List;

@Data
public class CommunityEmailDTO {
    private List<String> emails;

    // Default constructor
    public CommunityEmailDTO() {
    }

    // Constructor with parameters
    public CommunityEmailDTO(List<String> emails) {
        this.emails = emails;
    }
}