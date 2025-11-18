package it.overzoom.genealogia.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import it.overzoom.genealogia.model.Person;

import java.util.UUID;

public interface PersonRepository extends Neo4jRepository<Person, UUID> {

    @Query("""
        MATCH (a:Person {id: $id1}), (b:Person {id: $id2})
        MERGE (a)-[:SPOUSE_OF]->(b)
        MERGE (b)-[:SPOUSE_OF]->(a)
        """)
    void createMarriage(UUID id1, UUID id2);

    @Query("""
        MATCH (parent:Person {id: $parentId}), (child:Person {id: $childId})
        MERGE (parent)-[:PARENT_OF]->(child)
        """)
    void addParentRelation(UUID parentId, UUID childId);
}
