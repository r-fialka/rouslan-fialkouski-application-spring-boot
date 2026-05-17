package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.PhoneAlertDTO;
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

@WebMvcTest(PhoneAlertController.class)
class PhoneAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    void testGetPhoneAlert() throws Exception {
        PhoneAlertDTO dto = new PhoneAlertDTO(List.of("841-874-6512", "841-874-6513"));
        when(personService.getPhonesByStation(anyString())).thenReturn(dto);

        mockMvc.perform(get("/phoneAlert").param("firestation", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phones[0]").value("841-874-6512"))
                .andExpect(jsonPath("$.phones[1]").value("841-874-6513"));
    }

    @Test
    void testGetPhoneAlert_Empty() throws Exception {
        when(personService.getPhonesByStation(anyString())).thenReturn(new PhoneAlertDTO(List.of()));

        mockMvc.perform(get("/phoneAlert").param("firestation", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phones").isEmpty());
    }

    @Test
    void testPhoneAlert_Success() throws Exception {
        PhoneAlertDTO dto = new PhoneAlertDTO(List.of("841-874-6512", "841-874-6513"));
        when(personService.getPhonesByStation(anyString())).thenReturn(dto);

        mockMvc.perform(get("/phoneAlert").param("firestation", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phones[0]").value("841-874-6512"))
                .andExpect(jsonPath("$.phones[1]").value("841-874-6513"));
    }

    @Test
    void testPhoneAlert_Empty() throws Exception {
        when(personService.getPhonesByStation(anyString()))
                .thenReturn(new PhoneAlertDTO(List.of()));

        mockMvc.perform(get("/phoneAlert").param("firestation", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phones").isEmpty());
    }
}
