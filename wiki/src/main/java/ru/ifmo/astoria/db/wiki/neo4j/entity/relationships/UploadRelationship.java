package ru.ifmo.astoria.db.wiki.neo4j.entity.relationships;

import org.neo4j.ogm.annotation.*;
import ru.ifmo.astoria.db.wiki.neo4j.entity.Attachment;
import ru.ifmo.astoria.db.wiki.neo4j.entity.Author;

@RelationshipEntity(type = "UPLOADED")
public class UploadRelationship {
    @Id
    @GeneratedValue
    private Long relationshipId;

    @StartNode Author author;
    @EndNode Attachment attachment;

    public UploadRelationship() {}

    public UploadRelationship(Author author, Attachment attachment) {
        this.author = author;
        this.attachment = attachment;
    }

    public String getAttachmentName() {
        return attachment.getName();
    }
}
