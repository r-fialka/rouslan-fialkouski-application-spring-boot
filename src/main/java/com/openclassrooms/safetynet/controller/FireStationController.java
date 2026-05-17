package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.FireStationCoverageDTO;
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
public class FireStationController {

    private final PersonService personService;

    // GET: Returns list of residents served by a station with adult and child counts
    @GetMapping("/firestation")
    public ResponseEntity<FireStationCoverageDTO> getFirestation(
            @RequestParam("stationNumber") String stationNumber) {

        log.info("GET /firestation?stationNumber={}", stationNumber);

        FireStationCoverageDTO result = personService.getPersonsByStation(stationNumber);

        log.info("Answer: {} people, adults: {}, children: {}",
                result.getPersons().size(), result.getAdultsCount(), result.getChildrenCount());

        return ResponseEntity.ok(result);
    }
}