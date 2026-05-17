package com.openclassrooms.safetynet.controller;


import com.openclassrooms.safetynet.dto.CommunityEmailDTO;
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

@WebMvcTest(CommunityEmailController.class)
class CommunityEmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    void testGetCommunityEmail() throws Exception {
        CommunityEmailDTO dto = new CommunityEmailDTO(List.of("test@email.com", "user@email.com"));
        when(personService.getEmailsByCity(anyString())).thenReturn(dto);

        mockMvc.perform(get("/communityEmail").param("city", "Culver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emails[0]").value("test@email.com"))
                .andExpect(jsonPath("$.emails[1]").value("user@email.com"));
    }

    @Test
    void testCommunityEmail_Success() throws Exception {
        CommunityEmailDTO dto = new CommunityEmailDTO(List.of("john@email.com", "jane@email.com"));
        when(personService.getEmailsByCity(anyString())).thenReturn(dto);

        mockMvc.perform(get("/communityEmail").param("city", "Culver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emails[0]").value("john@email.com"))
                .andExpect(jsonPath("$.emails[1]").value("jane@email.com"));
    }

    @Test
    void testCommunityEmail_Empty() throws Exception {
        when(personService.getEmailsByCity(anyString()))
                .thenReturn(new CommunityEmailDTO(List.of()));

        mockMvc.perform(get("/communityEmail").param("city", "Unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emails").isEmpty());
    }
}