package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonCRUDController {

    private final DataRepository dataRepository;

    // POST: add a new person
    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        log.info("POST /person - add: {} {}", person.getFirstName(), person.getLastName());

        dataRepository.addPerson(person);

        return ResponseEntity.status(HttpStatus.CREATED).body(person);
    }

    // PUT: update an existing person
    @PutMapping
    public ResponseEntity<Person> updatePerson(@RequestParam String firstName,
                                               @RequestParam String lastName,
                                               @RequestBody Person updatedPerson) {
        log.info("PUT /person - update: {} {}", firstName, lastName);

        boolean updated = dataRepository.updatePerson(firstName, lastName, updatedPerson);

        if (updated) {
            return ResponseEntity.ok(updatedPerson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: delete a person
    @DeleteMapping
    public ResponseEntity<Void> deletePerson(@RequestParam String firstName,
                                             @RequestParam String lastName) {
        log.info("DELETE /person - deletion: {} {}", firstName, lastName);

        boolean deleted = dataRepository.deletePerson(firstName, lastName);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}