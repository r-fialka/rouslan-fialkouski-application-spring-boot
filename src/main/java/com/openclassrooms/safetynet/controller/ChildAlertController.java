package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.ChildAlertDTO;
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
public class ChildAlertController {

    private final PersonService personService;

    // GET: Returns list of children (age ≤ 18) at an address with their household members
    @GetMapping("/childAlert")
    public ResponseEntity<ChildAlertDTO> getChildAlert(@RequestParam("address") String address) {
        log.info("GET /childAlert?address={}", address);

        ChildAlertDTO result = personService.getChildrenByAddress(address);

        log.info("Answer: {} children found", result.getChildren().size());

        return ResponseEntity.ok(result);
    }
}