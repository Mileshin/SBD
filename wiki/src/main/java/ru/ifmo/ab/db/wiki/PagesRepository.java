package ru.ifmo.ab.db.wiki;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface PagesRepository extends MongoRepository<Page, String> {
    Optional<Page> findByName(String name);

    @Query("{ '$text': { '$search': ?0 } }")
    org.springframework.data.domain.Page<Page> textSearch(String text, Pageable pg);
}
