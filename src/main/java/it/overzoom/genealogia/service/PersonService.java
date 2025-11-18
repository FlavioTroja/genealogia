package it.overzoom.genealogia.service;

import org.springframework.stereotype.Service;

import it.overzoom.genealogia.model.Person;
import it.overzoom.genealogia.repository.PersonRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class PersonService {

    private final PersonRepository repo;

    public PersonService(PersonRepository repo) {
        this.repo = repo;
    }

    public Person create(Person person) {
        return repo.save(person);
    }

    public Optional<Person> getById(UUID id) {
        return repo.findById(id);
    }

    public Person update(Person person) {
        return repo.save(person);
    }

    public void createMarriage(UUID id1, UUID id2) {
        repo.createMarriage(id1, id2);
    }

    public void addParentRelation(UUID parentId, UUID childId) {
        repo.addParentRelation(parentId, childId);
    }
}

