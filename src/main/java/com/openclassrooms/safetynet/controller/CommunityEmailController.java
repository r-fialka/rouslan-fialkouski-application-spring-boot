package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.CommunityEmailDTO;
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
public class CommunityEmailController {

    private final PersonService personService;

    @GetMapping("/communityEmail")
    public ResponseEntity<CommunityEmailDTO> getCommunityEmail(@RequestParam("city") String city) {
        log.info("GET /communityEmail?city={}", city);

        CommunityEmailDTO result = personService.getEmailsByCity(city);

        log.info("Answer: {} email found", result.getEmails().size());

        return ResponseEntity.ok(result);
    }
}