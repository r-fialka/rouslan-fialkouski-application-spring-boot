package com.openclassrooms.safetynet.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.repository.DataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalRecordCRUDController.class)
class MedicalRecordCRUDControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataRepository dataRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("Test");
        medicalRecord.setLastName("User");
        medicalRecord.setBirthdate("01/01/2000");
        medicalRecord.setMedications(List.of("med1:100mg"));
        medicalRecord.setAllergies(List.of("allergy1"));

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateMedicalRecord_Success() throws Exception {
        MedicalRecord updatedRecord = new MedicalRecord();
        updatedRecord.setFirstName("John");
        updatedRecord.setLastName("Boyd");
        updatedRecord.setBirthdate("03/06/1984");
        updatedRecord.setMedications(List.of("newMed:500mg"));
        updatedRecord.setAllergies(List.of("newAllergy"));

        when(dataRepository.updateMedicalRecord(anyString(), anyString(), any(MedicalRecord.class)))
                .thenReturn(true);

        mockMvc.perform(put("/medicalRecord")
                        .param("firstName", "John")
                        .param("lastName", "Boyd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRecord)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateMedicalRecord_NotFound() throws Exception {
        MedicalRecord updatedRecord = new MedicalRecord();

        when(dataRepository.updateMedicalRecord(anyString(), anyString(), any(MedicalRecord.class)))
                .thenReturn(false);

        mockMvc.perform(put("/medicalRecord")
                        .param("firstName", "Unknown")
                        .param("lastName", "Person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRecord)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMedicalRecord_Success() throws Exception {
        when(dataRepository.deleteMedicalRecord(anyString(), anyString()))
                .thenReturn(true);

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "John")
                        .param("lastName", "Boyd"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteMedicalRecord_NotFound() throws Exception {
        when(dataRepository.deleteMedicalRecord(anyString(), anyString()))
                .thenReturn(false);

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "Unknown")
                        .param("lastName", "Person"))
                .andExpect(status().isNotFound());
    }
}