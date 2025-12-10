package it.overzoom.genealogia.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Person")
public class Person {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;
    private String lastName;
    private String gender; // MALE / FEMALE / OTHER
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String occupation;
    private String birthCity;
    private String currentCity;
    private String photoUrl;

    // Metadata per il social network
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean verified; // Se i dati sono stati verificati da più utenti
    private int verificationCount; // Numero di utenti che hanno confermato questi dati

    // ----------- RELATIONSHIPS -----------

    // Chi ha creato questa persona
    @Relationship(type = "CREATED_BY", direction = Relationship.Direction.OUTGOING)
    private User createdBy;

    // A person can have multiple children (outgoing relationship)
    @Relationship(type = "PARENT_OF", direction = Relationship.Direction.OUTGOING)
    private List<Person> children;

    // A person can have 0–2 parents (incoming relationship)
    @Relationship(type = "PARENT_OF", direction = Relationship.Direction.INCOMING)
    private List<Person> parents;

    // Optional: adopted parents
    @Relationship(type = "ADOPTED_BY", direction = Relationship.Direction.OUTGOING)
    private List<Person> adoptedBy;

    @Relationship(type = "SPOUSE_OF", direction = Relationship.Direction.OUTGOING)
    private List<Person> spouses;

    @Relationship(type = "SIBLING_OF", direction = Relationship.Direction.OUTGOING)
    private List<Person> siblings;

    public Person() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.verified = false;
        this.verificationCount = 0;
    }

    public Person(String firstName, String lastName, String gender,
            LocalDate birthDate, LocalDate deathDate, String occupation,
            String birthCity, String currentCity, String photoUrl) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.occupation = occupation;
        this.birthCity = birthCity;
        this.currentCity = currentCity;
        this.photoUrl = photoUrl;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public int getVerificationCount() {
        return verificationCount;
    }

    public void setVerificationCount(int verificationCount) {
        this.verificationCount = verificationCount;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<Person> getChildren() {
        return children;
    }

    public void setChildren(List<Person> children) {
        this.children = children;
    }

    public List<Person> getParents() {
        return parents;
    }

    public void setParents(List<Person> parents) {
        this.parents = parents;
    }

    public List<Person> getSpouses() {
        return spouses;
    }

    public void setSpouses(List<Person> spouses) {
        this.spouses = spouses;
    }

    public List<Person> getAdoptedBy() {
        return adoptedBy;
    }

    public void setAdoptedBy(List<Person> adoptedBy) {
        this.adoptedBy = adoptedBy;
    }

    public List<Person> getSiblings() {
        return siblings;
    }

    public void setSiblings(List<Person> siblings) {
        this.siblings = siblings;
    }
}