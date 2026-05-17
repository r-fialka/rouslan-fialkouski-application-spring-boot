package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.FireAddressDTO;
import com.openclassrooms.safetynet.dto.FireResidentDTO;
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

@WebMvcTest(FireController.class)
class FireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    void testFire_Success() throws Exception {
        FireResidentDTO resident = new FireResidentDTO("John", "Boyd", "841-874-6512", 42, List.of("aznol:350mg"), List.of("nillacilan"));
        when(personService.getFireByAddress(anyString()))
                .thenReturn(new FireAddressDTO("3", List.of(resident)));

        mockMvc.perform(get("/fire").param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNumber").value("3"))
                .andExpect(jsonPath("$.residents[0].firstName").value("John"))
                .andExpect(jsonPath("$.residents[0].age").value(42));
    }

    @Test
    void testFire_Empty() throws Exception {
        when(personService.getFireByAddress(anyString()))
                .thenReturn(new FireAddressDTO(null, List.of()));

        mockMvc.perform(get("/fire").param("address", "Unknown St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.residents").isEmpty());
    }
}