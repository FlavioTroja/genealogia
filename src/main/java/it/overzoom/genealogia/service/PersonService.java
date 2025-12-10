package it.overzoom.genealogia.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.overzoom.genealogia.model.Person;
import it.overzoom.genealogia.model.User;
import it.overzoom.genealogia.repository.PersonRepository;
import it.overzoom.genealogia.repository.UserRepository;

@Service
@Transactional
public class PersonService {

    private final PersonRepository repo;
    private final UserRepository userRepo;

    public PersonService(PersonRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public Person create(Person person, UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        person.setCreatedBy(user);
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());

        return repo.save(person);
    }

    public Optional<Person> getById(UUID id) {
        return repo.findById(id);
    }

    public List<Person> getAll() {
        return repo.findAll();
    }

    public Person update(Person person) {
        person.setUpdatedAt(LocalDateTime.now());
        return repo.save(person);
    }

    public void delete(UUID id) {
        repo.deleteById(id);
    }

    public void createMarriage(UUID id1, UUID id2) {
        repo.createMarriage(id1, id2);
    }

    public void addParentRelation(UUID parentId, UUID childId) {
        repo.addParentRelation(parentId, childId);
    }

    // Metodi genealogici
    public List<Person> getAncestors(UUID personId) {
        return repo.findAncestors(personId);
    }

    public List<Person> getDescendants(UUID personId) {
        return repo.findDescendants(personId);
    }

    public List<Person> getSiblings(UUID personId) {
        return repo.findSiblings(personId);
    }

    public List<Person> getCousins(UUID personId) {
        return repo.findCousins(personId);
    }

    // Cerca persone per nome
    public List<Person> searchByName(String searchTerm) {
        return repo.searchByName(searchTerm);
    }

    // Trova possibili duplicati/match
    public List<Person> findPotentialMatches(String firstName, String lastName, String birthCity) {
        return repo.findPotentialMatches(firstName, lastName, birthCity);
    }

    // Persone create da un utente
    public List<Person> getPersonsByUser(UUID userId) {
        return repo.findByCreatedBy(userId);
    }

    // Trova connessione tra due persone
    public List<Object> findConnection(UUID person1Id, UUID person2Id, int maxDepth) {
        return repo.findConnectionBetween(person1Id, person2Id, maxDepth);
    }

    // Statistiche
    public List<Person> getMostConnectedPersons() {
        return repo.findMostConnectedPersons();
    }

    public List<Person> getOrphans() {
        return repo.findOrphans();
    }

    // Verifica una persona (incrementa verification count)
    public Person verifyPerson(UUID personId, UUID userId) {
        Person person = repo.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Persona non trovata"));

        // TODO: Controllare che l'utente non abbia già verificato questa persona
        person.setVerificationCount(person.getVerificationCount() + 1);

        if (person.getVerificationCount() >= 3) {
            person.setVerified(true);
        }

        return repo.save(person);
    }
}