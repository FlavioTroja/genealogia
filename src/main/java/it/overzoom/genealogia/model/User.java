package it.overzoom.genealogia.model;

import org.springframework.data.neo4j.core.schema.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Set;

@Node("User")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String username;
    private String email;
    private String password; // Hashed
    private String firstName;
    private String lastName;
    private String profilePhotoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private boolean enabled;

    // Relazione con la propria persona nell'albero
    @Relationship(type = "IS_PROFILE_OF", direction = Relationship.Direction.OUTGOING)
    private Person profilePerson;

    // Persone create/modificate da questo utente
    @Relationship(type = "CREATED_BY", direction = Relationship.Direction.INCOMING)
    private Set<Person> createdPersons;

    // Ruoli (per autorizzazioni future)
    private Set<String> roles; // USER, ADMIN, MODERATOR

    public User() {
        this.createdAt = LocalDateTime.now();
        this.enabled = true;
    }

    public User(String username, String email, String password) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Person getProfilePerson() {
        return profilePerson;
    }

    public void setProfilePerson(Person profilePerson) {
        this.profilePerson = profilePerson;
    }

    public Set<Person> getCreatedPersons() {
        return createdPersons;
    }

    public void setCreatedPersons(Set<Person> createdPersons) {
        this.createdPersons = createdPersons;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}