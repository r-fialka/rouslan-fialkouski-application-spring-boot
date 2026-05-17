package com.openclassrooms.safetynet.controller;


import com.openclassrooms.safetynet.dto.FireStationCoverageDTO;
import com.openclassrooms.safetynet.dto.PersonCoverageInfo;
import com.openclassrooms.safetynet.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FireStationController.class)
class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    void testGetFirestation() throws Exception {
        PersonCoverageInfo person = new PersonCoverageInfo("John", "Boyd", "1509 Culver St", "841-874-6512");
        FireStationCoverageDTO dto = new FireStationCoverageDTO(List.of(person), 5, 2);
        when(personService.getPersonsByStation(anyString())).thenReturn(dto);

        mockMvc.perform(get("/firestation").param("stationNumber", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons[0].firstName").value("John"))
                .andExpect(jsonPath("$.adultsCount").value(5))
                .andExpect(jsonPath("$.childrenCount").value(2));
    }

    @Test
    void testFirestation_Success() throws Exception {
        PersonCoverageInfo person = new PersonCoverageInfo("John", "Boyd", "1509 Culver St", "841-874-6512");
        FireStationCoverageDTO dto = new FireStationCoverageDTO(List.of(person), 5, 2);
        when(personService.getPersonsByStation(anyString())).thenReturn(dto);

        mockMvc.perform(get("/firestation").param("stationNumber", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons[0].firstName").value("John"))
                .andExpect(jsonPath("$.persons[0].lastName").value("Boyd"))
                .andExpect(jsonPath("$.adultsCount").value(5))
                .andExpect(jsonPath("$.childrenCount").value(2));
    }

    @Test
    void testFirestation_Empty() throws Exception {
        when(personService.getPersonsByStation(anyString()))
                .thenReturn(new FireStationCoverageDTO(List.of(), 0, 0));

        mockMvc.perform(get("/firestation").param("stationNumber", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons").isEmpty())
                .andExpect(jsonPath("$.adultsCount").value(0));
    }
}