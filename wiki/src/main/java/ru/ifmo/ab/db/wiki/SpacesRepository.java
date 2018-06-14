package ru.ifmo.ab.db.wiki;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SpacesRepository extends MongoRepository<Space, String> {
    Optional<Space> findByName(String name);
}
