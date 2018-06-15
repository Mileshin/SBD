package ru.ifmo.astoria.db.wiki.neo4j.entity.relationships;

import org.neo4j.ogm.annotation.*;
import ru.ifmo.astoria.db.wiki.neo4j.entity.Author;
import ru.ifmo.astoria.db.wiki.neo4j.entity.Page;

@RelationshipEntity(type = "CREATED")
public class CreationRelationship {
    @Id
    @GeneratedValue
    private Long relationshipId;

    @StartNode Author author;
    @EndNode Page page;

    public CreationRelationship() {}

    public CreationRelationship(Author author, Page page){
        this.author = author;
        this.page = page;
    }

    public String getPageName(){
        return page.getName();
    }
}
