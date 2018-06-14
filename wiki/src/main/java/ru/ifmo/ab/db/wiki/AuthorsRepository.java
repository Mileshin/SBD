package ru.ifmo.ab.db.wiki;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthorsRepository extends MongoRepository<Author, String> {
    Optional<Author> findByLogin(String login);
}
