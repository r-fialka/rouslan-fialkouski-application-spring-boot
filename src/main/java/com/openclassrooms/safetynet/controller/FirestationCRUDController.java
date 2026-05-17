package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.model.FireStation;
import com.openclassrooms.safetynet.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/firestation")
@RequiredArgsConstructor
public class FirestationCRUDController {

    private final DataRepository dataRepository;

    // POST: Add a new address-to-station link
    @PostMapping
    public ResponseEntity<FireStation> addFirestation(@RequestBody FireStation firestation) {
        log.info("POST /firestation - addition: address {} -> station {}",
                firestation.getAddress(), firestation.getStation());

        dataRepository.addFirestation(firestation);

        return ResponseEntity.status(HttpStatus.CREATED).body(firestation);
    }

    // PUT: Update the station number for the address
    @PutMapping
    public ResponseEntity<Void> updateFirestation(@RequestParam String address,
                                                  @RequestParam String stationNumber) {
        log.info("PUT /firestation - Update: Address {} -> Station {}", address, stationNumber);

        boolean updated = dataRepository.updateFirestation(address, stationNumber);

        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: Remove the link at this address
    @DeleteMapping("/address")
    public ResponseEntity<Void> deleteFirestationByAddress(@RequestParam String address) {
        log.info("DELETE /firestation/address - remove at the address: {}", address);

        boolean deleted = dataRepository.deleteFirestationByAddress(address);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: Delete all entries associated with the station number
    @DeleteMapping("/station")
    public ResponseEntity<Void> deleteFirestationByStation(@RequestParam String stationNumber) {
        log.info("DELETE /firestation/station - deletion by station: {}", stationNumber);

        boolean deleted = dataRepository.deleteFirestationByStation(stationNumber);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}