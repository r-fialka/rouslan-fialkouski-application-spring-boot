package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.PhoneAlertDTO;
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
public class PhoneAlertController {

    private final PersonService personService;

    // GET: Returns phone numbers of residents served by the fire station
    @GetMapping("/phoneAlert")
    public ResponseEntity<PhoneAlertDTO> getPhoneAlert(@RequestParam("firestation") String stationNumber) {
        log.info("GET /phoneAlert?firestation={}", stationNumber);

        PhoneAlertDTO result = personService.getPhonesByStation(stationNumber);

        log.info("Answer: {} phones found", result.getPhones().size());

        return ResponseEntity.ok(result);
    }
}