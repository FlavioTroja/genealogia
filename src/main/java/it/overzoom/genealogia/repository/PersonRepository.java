package it.overzoom.genealogia.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import it.overzoom.genealogia.model.Person;

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

        // Trova tutti gli antenati (genitori, nonni, bisnonni, ecc.)
        @Query("""
                        MATCH (p:Person {id: $personId})-[:PARENT_OF*]->(ancestor:Person)
                        RETURN DISTINCT ancestor
                        """)
        List<Person> findAncestors(UUID personId);

        // Trova tutti i discendenti (figli, nipoti, pronipoti, ecc.)
        @Query("""
                        MATCH (p:Person {id: $personId})<-[:PARENT_OF*]-(descendant:Person)
                        RETURN DISTINCT descendant
                        """)
        List<Person> findDescendants(UUID personId);

        // Trova i fratelli/sorelle (persone con almeno un genitore in comune)
        @Query("""
                        MATCH (p:Person {id: $personId})-[:PARENT_OF]->(parent:Person)
                              -[:PARENT_OF]->(sibling:Person)
                        WHERE sibling.id <> $personId
                        RETURN DISTINCT sibling
                        """)
        List<Person> findSiblings(UUID personId);

        // Trova i cugini
        @Query("""
                        MATCH (p:Person {id: $personId})-[:PARENT_OF]->(parent:Person)
                              -[:PARENT_OF]->(grandparent:Person)
                              -[:PARENT_OF]->(uncle:Person)
                              -[:PARENT_OF]->(cousin:Person)
                        WHERE uncle.id <> parent.id AND cousin.id <> $personId
                        RETURN DISTINCT cousin
                        """)
        List<Person> findCousins(UUID personId);

        // Cerca persone per nome (per collegare alberi genealogici)
        @Query("""
                        MATCH (p:Person)
                        WHERE toLower(p.firstName) CONTAINS toLower($searchTerm)
                           OR toLower(p.lastName) CONTAINS toLower($searchTerm)
                        RETURN p
                        LIMIT 50
                        """)
        List<Person> searchByName(String searchTerm);

        // Cerca persone con nome, cognome e città di nascita simili
        @Query("""
                        MATCH (p:Person)
                        WHERE toLower(p.firstName) = toLower($firstName)
                          AND toLower(p.lastName) = toLower($lastName)
                          AND (p.birthCity IS NULL OR toLower(p.birthCity) CONTAINS toLower($birthCity))
                        RETURN p
                        """)
        List<Person> findPotentialMatches(String firstName, String lastName, String birthCity);

        // Trova persone create da un utente specifico
        @Query("""
                        MATCH (u:User {id: $userId})<-[:CREATED_BY]-(p:Person)
                        RETURN p
                        ORDER BY p.createdAt DESC
                        """)
        List<Person> findByCreatedBy(UUID userId);

        // Trova connessioni tra due persone (percorso genealogico)
        @Query("""
                        MATCH path = shortestPath(
                          (p1:Person {id: $person1Id})-[*]-(p2:Person {id: $person2Id})
                        )
                        WHERE length(path) <= $maxDepth
                        RETURN nodes(path) as persons, relationships(path) as relations
                        """)
        List<Object> findConnectionBetween(UUID person1Id, UUID person2Id, int maxDepth);

        // Statistiche: persone più collegate
        @Query("""
                        MATCH (p:Person)
                        RETURN p,
                               size((p)-[:PARENT_OF]->()) +
                               size((p)<-[:PARENT_OF]-()) +
                               size((p)-[:SPOUSE_OF]->()) as connectionCount
                        ORDER BY connectionCount DESC
                        LIMIT 10
                        """)
        List<Person> findMostConnectedPersons();

        // Trova "orfani" - persone senza collegamenti
        @Query("""
                        MATCH (p:Person)
                        WHERE NOT (p)-[:PARENT_OF]-()
                          AND NOT (p)<-[:PARENT_OF]-()
                          AND NOT (p)-[:SPOUSE_OF]-()
                        RETURN p
                        ORDER BY p.createdAt DESC
                        """)
        List<Person> findOrphans();
}