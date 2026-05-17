package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.PersonInfoResponseDTO;
import com.openclassrooms.safetynet.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PersonInfoController {

    private final PersonService personService;

    @GetMapping("/personInfo")
    public ResponseEntity<PersonInfoResponseDTO> getPersonInfo(
            @RequestParam("lastName") String lastName) {

        log.info("GET /personInfo?lastName={}", lastName);

        PersonInfoResponseDTO result = personService.getPersonInfoByLastName(lastName);

        log.info("Answer: {} people found", result.getPersons().size());

        return ResponseEntity.ok(result);
    }
}