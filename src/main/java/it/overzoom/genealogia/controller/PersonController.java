package it.overzoom.genealogia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.overzoom.genealogia.model.Person;
import it.overzoom.genealogia.service.PersonService;

import java.util.UUID;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    // CREATE person
    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        return ResponseEntity.ok(service.create(person));
    }

    // GET person
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE person
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(
            @PathVariable UUID id,
            @RequestBody Person updated
    ) {
        return service.getById(id)
                .map(existing -> {
                    updated.setId(id);
                    return ResponseEntity.ok(service.update(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE MARRIAGE (bidirectional SPOUSE_OF)
    @PostMapping("/{id1}/marry/{id2}")
    public ResponseEntity<Void> createMarriage(
            @PathVariable UUID id1,
            @PathVariable UUID id2
    ) {
        service.createMarriage(id1, id2);
        return ResponseEntity.ok().build();
    }

    // ADD PARENT RELATION
    @PostMapping("/{parentId}/add-child/{childId}")
    public ResponseEntity<Void> addParentRelation(
            @PathVariable UUID parentId,
            @PathVariable UUID childId
    ) {
        service.addParentRelation(parentId, childId);
        return ResponseEntity.ok().build();
    }
}
