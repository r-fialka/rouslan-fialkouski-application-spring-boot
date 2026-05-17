package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.PersonInfoDTO;
import com.openclassrooms.safetynet.dto.PersonInfoResponseDTO;
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

@WebMvcTest(PersonInfoController.class)
class PersonInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    void testPersonInfo_Success() throws Exception {
        PersonInfoDTO person = new PersonInfoDTO("John", "Boyd", "1509 Culver St", 42, "john@email.com", List.of("aznol:350mg"), List.of("nillacilan"));
        when(personService.getPersonInfoByLastName(anyString()))
                .thenReturn(new PersonInfoResponseDTO(List.of(person)));

        mockMvc.perform(get("/personInfo").param("lastName", "Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons[0].firstName").value("John"))
                .andExpect(jsonPath("$.persons[0].lastName").value("Boyd"))
                .andExpect(jsonPath("$.persons[0].age").value(42))
                .andExpect(jsonPath("$.persons[0].email").value("john@email.com"));
    }

    @Test
    void testPersonInfo_NotFound() throws Exception {
        when(personService.getPersonInfoByLastName(anyString()))
                .thenReturn(new PersonInfoResponseDTO(List.of()));

        mockMvc.perform(get("/personInfo").param("lastName", "Unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons").isEmpty());
    }
}
