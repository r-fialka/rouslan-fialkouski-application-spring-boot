package com.openclassrooms.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynet.model.Person;
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

@WebMvcTest(PersonCRUDController.class)
class PersonCRUDControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataRepository dataRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddPerson() throws Exception {
        Person person = new Person();
        person.setFirstName("Test");
        person.setLastName("User");
        person.setAddress("123 Test St");
        person.setPhone("123-456-7890");

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdatePerson_Success() throws Exception {
        Person updatedPerson = new Person();
        updatedPerson.setFirstName("John");
        updatedPerson.setLastName("Boyd");
        updatedPerson.setAddress("New Address");

        when(dataRepository.updatePerson(anyString(), anyString(), any(Person.class))).thenReturn(true);

        mockMvc.perform(put("/person")
                        .param("firstName", "John")
                        .param("lastName", "Boyd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPerson)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePerson_NotFound() throws Exception {
        Person updatedPerson = new Person();
        when(dataRepository.updatePerson(anyString(), anyString(), any(Person.class))).thenReturn(false);

        mockMvc.perform(put("/person")
                        .param("firstName", "Unknown")
                        .param("lastName", "Person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPerson)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePerson_Success() throws Exception {
        when(dataRepository.deletePerson(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(delete("/person")
                        .param("firstName", "John")
                        .param("lastName", "Boyd"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeletePerson_NotFound() throws Exception {
        when(dataRepository.deletePerson(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(delete("/person")
                        .param("firstName", "Unknown")
                        .param("lastName", "Person"))
                .andExpect(status().isNotFound());
    }
}