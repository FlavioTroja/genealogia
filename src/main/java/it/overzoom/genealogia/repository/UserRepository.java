package it.overzoom.genealogia.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import it.overzoom.genealogia.model.User;

public interface UserRepository extends Neo4jRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("""
            MATCH (u:User {id: $userId})-[:IS_PROFILE_OF]->(p:Person)
            RETURN p
            """)
    Optional<User> findUserWithProfilePerson(UUID userId);
}