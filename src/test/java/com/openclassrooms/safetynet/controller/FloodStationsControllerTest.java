package com.openclassrooms.safetynet.controller;


import com.openclassrooms.safetynet.dto.FloodHomeDTO;
import com.openclassrooms.safetynet.dto.FloodResidentDTO;
import com.openclassrooms.safetynet.dto.FloodStationsDTO;
import com.openclassrooms.safetynet.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FloodStationsController.class)
class FloodStationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    void testFloodStations_Success() throws Exception {
        FloodResidentDTO resident = new FloodResidentDTO("John", "Boyd", "841-874-6512", 42, List.of(), List.of());
        FloodHomeDTO home = new FloodHomeDTO("1509 Culver St", "3", List.of(resident));
        when(personService.getFloodByStations(anyList()))
                .thenReturn(new FloodStationsDTO(List.of(home)));

        mockMvc.perform(get("/flood/stations").param("stations", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.homes[0].address").value("1509 Culver St"))
                .andExpect(jsonPath("$.homes[0].stationNumber").value("3"))
                .andExpect(jsonPath("$.homes[0].residents[0].firstName").value("John"));
    }

    @Test
    void testFloodStations_Empty() throws Exception {
        when(personService.getFloodByStations(anyList()))
                .thenReturn(new FloodStationsDTO(List.of()));

        mockMvc.perform(get("/flood/stations").param("stations", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.homes").isEmpty());
    }
}
