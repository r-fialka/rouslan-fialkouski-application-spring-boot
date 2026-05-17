package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.FloodStationsDTO;
import com.openclassrooms.safetynet.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FloodStationsController {

    private final PersonService personService;

    @GetMapping("/flood/stations")
    public ResponseEntity<FloodStationsDTO> getFloodStations(
            @RequestParam("stations") String stations) {

        log.info("GET /flood/stations?stations={}", stations);

        // Convert the string “1,2,3” into the list [“1”, ‘2’, “3”]
        List<String> stationList = Arrays.asList(stations.split(","));

        FloodStationsDTO result = personService.getFloodByStations(stationList);

        log.info("Answer: {} homes found", result.getHomes().size());

        return ResponseEntity.ok(result);
    }
}