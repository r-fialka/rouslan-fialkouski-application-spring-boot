package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.FireAddressDTO;
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
public class FireController {

    private final PersonService personService;

    @GetMapping("/fire")
    public ResponseEntity<FireAddressDTO> getFire(@RequestParam("address") String address) {
        log.info("GET /fire?address={}", address);

        FireAddressDTO result = personService.getFireByAddress(address);

        log.info("Answer: Station {}, Population: {}",
                result.getStationNumber(), result.getResidents().size());

        return ResponseEntity.ok(result);
    }
}