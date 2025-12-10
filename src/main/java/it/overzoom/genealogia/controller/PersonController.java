package it.overzoom.genealogia.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.overzoom.genealogia.model.Person;
import it.overzoom.genealogia.service.PersonService;

@RestController
@RequestMapping("/api/persons")
@CrossOrigin(origins = "*")
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    // CREATE person (richiede userId)
    @PostMapping
    public ResponseEntity<Person> createPerson(
            @RequestBody Person person,
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(service.create(person, userId));
    }

    // GET ALL persons
    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET person by ID
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
            @RequestBody Person updated) {
        return service.getById(id)
                .map(existing -> {
                    updated.setId(id);
                    return ResponseEntity.ok(service.update(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE person
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID id) {
        if (service.getById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // CREATE MARRIAGE
    @PostMapping("/{id1}/marry/{id2}")
    public ResponseEntity<Void> createMarriage(
            @PathVariable UUID id1,
            @PathVariable UUID id2) {
        service.createMarriage(id1, id2);
        return ResponseEntity.ok().build();
    }

    // ADD PARENT RELATION
    @PostMapping("/{parentId}/add-child/{childId}")
    public ResponseEntity<Void> addParentRelation(
            @PathVariable UUID parentId,
            @PathVariable UUID childId) {
        service.addParentRelation(parentId, childId);
        return ResponseEntity.ok().build();
    }

    // GET ANCESTORS
    @GetMapping("/{id}/ancestors")
    public ResponseEntity<List<Person>> getAncestors(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getAncestors(id));
    }

    // GET DESCENDANTS
    @GetMapping("/{id}/descendants")
    public ResponseEntity<List<Person>> getDescendants(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getDescendants(id));
    }

    // GET SIBLINGS
    @GetMapping("/{id}/siblings")
    public ResponseEntity<List<Person>> getSiblings(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getSiblings(id));
    }

    // GET COUSINS
    @GetMapping("/{id}/cousins")
    public ResponseEntity<List<Person>> getCousins(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getCousins(id));
    }

    // GET FAMILY TREE (person with all relationships loaded)
    @GetMapping("/{id}/family-tree")
    public ResponseEntity<Person> getFamilyTree(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // SEARCH persons by name
    @GetMapping("/search")
    public ResponseEntity<List<Person>> searchPersons(@RequestParam String q) {
        return ResponseEntity.ok(service.searchByName(q));
    }

    // FIND potential matches/duplicates
    @GetMapping("/matches")
    public ResponseEntity<List<Person>> findMatches(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(required = false) String birthCity) {
        return ResponseEntity.ok(
                service.findPotentialMatches(firstName, lastName, birthCity != null ? birthCity : ""));
    }

    // GET persons created by a user
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Person>> getPersonsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.getPersonsByUser(userId));
    }

    // FIND connection between two persons
    @GetMapping("/connection")
    public ResponseEntity<List<Object>> findConnection(
            @RequestParam UUID person1,
            @RequestParam UUID person2,
            @RequestParam(defaultValue = "10") int maxDepth) {
        return ResponseEntity.ok(
                service.findConnection(person1, person2, maxDepth));
    }

    // GET most connected persons (statistics)
    @GetMapping("/stats/most-connected")
    public ResponseEntity<List<Person>> getMostConnected() {
        return ResponseEntity.ok(service.getMostConnectedPersons());
    }

    // GET orphan persons (no connections)
    @GetMapping("/stats/orphans")
    public ResponseEntity<List<Person>> getOrphans() {
        return ResponseEntity.ok(service.getOrphans());
    }

    // VERIFY a person (crowdsourced verification)
    @PostMapping("/{id}/verify")
    public ResponseEntity<Person> verifyPerson(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(service.verifyPerson(id, userId));
    }
}