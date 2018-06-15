package ru.ifmo.astoria.db.wiki.neo4j.entity.relationships;

import org.neo4j.ogm.annotation.*;
import ru.ifmo.astoria.db.wiki.neo4j.entity.Attachment;
import ru.ifmo.astoria.db.wiki.neo4j.entity.Page;

@RelationshipEntity(type = "INCLUDES")
public class IncludesRelation {
    @Id
    @GeneratedValue
    private Long relationshipId;

    @StartNode Page page;
    @EndNode Attachment attachment;

    public IncludesRelation() {}

    public IncludesRelation(Page page, Attachment attachment){
        this.page = page;
        this.attachment = attachment;
    }

    public String getAttachmentName() { return attachment.getName(); }
}
