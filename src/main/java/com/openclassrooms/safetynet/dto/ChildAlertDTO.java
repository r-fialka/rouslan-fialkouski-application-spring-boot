package com.openclassrooms.safetynet.dto;

import lombok.Data;
import java.util.List;

@Data
public class ChildAlertDTO {
    private List<ChildInfo> children;

    // Default constructor
    public ChildAlertDTO() {
    }

    // Constructor with parameters
    public ChildAlertDTO(List<ChildInfo> children) {
        this.children = children;
    }
}