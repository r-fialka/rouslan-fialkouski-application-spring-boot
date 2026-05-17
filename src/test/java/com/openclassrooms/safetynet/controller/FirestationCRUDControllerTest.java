package com.openclassrooms.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.repository.DataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FirestationCRUDController.class)
class FirestationCRUDControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataRepository dataRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddFirestation() throws Exception {
        FireStation fs = new FireStation();
        fs.setAddress("123 Test St");
        fs.setStation("5");

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fs)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateFirestation_Success() throws Exception {
        when(dataRepository.updateFirestation(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(put("/firestation")
                        .param("address", "1509 Culver St")
                        .param("stationNumber", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateFirestation_NotFound() throws Exception {
        when(dataRepository.updateFirestation(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(put("/firestation")
                        .param("address", "Unknown Address")
                        .param("stationNumber", "10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteFirestationByAddress_Success() throws Exception {
        when(dataRepository.deleteFirestationByAddress(anyString())).thenReturn(true);

        mockMvc.perform(delete("/firestation/address")
                        .param("address", "1509 Culver St"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteFirestationByAddress_NotFound() throws Exception {
        when(dataRepository.deleteFirestationByAddress(anyString())).thenReturn(false);

        mockMvc.perform(delete("/firestation/address")
                        .param("address", "Unknown Address"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteFirestationByStation_Success() throws Exception {
        when(dataRepository.deleteFirestationByStation(anyString())).thenReturn(true);

        mockMvc.perform(delete("/firestation/station")
                        .param("stationNumber", "3"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteFirestationByStation_NotFound() throws Exception {
        when(dataRepository.deleteFirestationByStation(anyString())).thenReturn(false);

        mockMvc.perform(delete("/firestation/station")
                        .param("stationNumber", "99"))
                .andExpect(status().isNotFound());
    }
}