package ru.ifmo.ab.db.wiki;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AttachmentsRepository extends MongoRepository<Attachment, String> {
    Optional<Attachment> findByFilename(String filename);
}
