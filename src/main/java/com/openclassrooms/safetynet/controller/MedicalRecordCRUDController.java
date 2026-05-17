package com.openclassrooms.safetynet.controller;


import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/medicalRecord")
@RequiredArgsConstructor
public class MedicalRecordCRUDController {

    private final DataRepository dataRepository;

    // POST: add a new medical record
    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        log.info("POST /medicalRecord - add to: {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());

        dataRepository.addMedicalRecord(medicalRecord);

        return ResponseEntity.status(HttpStatus.CREATED).body(medicalRecord);
    }

    // PUT: update an existing medical record
    @PutMapping
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestParam String firstName,
                                                             @RequestParam String lastName,
                                                             @RequestBody MedicalRecord updatedRecord) {
        log.info("PUT /medicalRecord - update for: {} {}", firstName, lastName);

        boolean updated = dataRepository.updateMedicalRecord(firstName, lastName, updatedRecord);

        if (updated) {
            return ResponseEntity.ok(updatedRecord);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: delete medical record
    @DeleteMapping
    public ResponseEntity<Void> deleteMedicalRecord(@RequestParam String firstName,
                                                    @RequestParam String lastName) {
        log.info("DELETE /medicalRecord - delete for: {} {}", firstName, lastName);

        boolean deleted = dataRepository.deleteMedicalRecord(firstName, lastName);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}