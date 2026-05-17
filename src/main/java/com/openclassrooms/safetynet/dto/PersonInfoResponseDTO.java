package com.openclassrooms.safetynet.dto;

import lombok.Data;
import java.util.List;

@Data
public class PersonInfoResponseDTO {
    private List<PersonInfoDTO> persons;

    public PersonInfoResponseDTO() {
    }

    public PersonInfoResponseDTO(List<PersonInfoDTO> persons) {
        this.persons = persons;
    }
}