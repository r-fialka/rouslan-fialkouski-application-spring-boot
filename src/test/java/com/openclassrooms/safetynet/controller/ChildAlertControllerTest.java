package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.ChildAlertDTO;
import com.openclassrooms.safetynet.dto.ChildInfo;
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

@WebMvcTest(ChildAlertController.class)
class ChildAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    void testGetChildAlert() throws Exception {
        // Preparing test data
        ChildInfo child = new ChildInfo("Tenley", "Boyd", 12, List.of("John Boyd", "Jacob Boyd"));
        ChildAlertDTO dto = new ChildAlertDTO(List.of(child));

        when(personService.getChildrenByAddress(anyString())).thenReturn(dto);

        // Execute the request and check the response
        mockMvc.perform(get("/childAlert").param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children[0].firstName").value("Tenley"))
                .andExpect(jsonPath("$.children[0].lastName").value("Boyd"))
                .andExpect(jsonPath("$.children[0].age").value(12));
    }

    @Test
    void testGetChildAlert_NoChildren() throws Exception {
        // Empty response
        when(personService.getChildrenByAddress(anyString())).thenReturn(new ChildAlertDTO(List.of()));

        mockMvc.perform(get("/childAlert").param("address", "Empty Address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children").isEmpty());
    }

    @Test
    void testChildAlert_Success() throws Exception {
        ChildInfo child = new ChildInfo("Tenley", "Boyd", 12, List.of("John Boyd", "Jacob Boyd"));
        when(personService.getChildrenByAddress(anyString()))
                .thenReturn(new ChildAlertDTO(List.of(child)));

        mockMvc.perform(get("/childAlert").param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children[0].firstName").value("Tenley"))
                .andExpect(jsonPath("$.children[0].lastName").value("Boyd"))
                .andExpect(jsonPath("$.children[0].age").value(12));
    }

    @Test
    void testChildAlert_NoChildren() throws Exception {
        when(personService.getChildrenByAddress(anyString()))
                .thenReturn(new ChildAlertDTO(List.of()));

        mockMvc.perform(get("/childAlert").param("address", "Empty St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children").isEmpty());
    }
}
