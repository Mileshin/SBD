package ru.ifmo.astoria.db.wiki.neo4j.entity.relationships;

import org.neo4j.ogm.annotation.*;
import ru.ifmo.astoria.db.wiki.neo4j.entity.Page;

@RelationshipEntity(type = "LINKED")
public class LinksRelations {
    @Id
    @GeneratedValue
    private Long relationshipId;

    @StartNode Page page;
    @EndNode Page pages;

    public LinksRelations() {}

    public LinksRelations(Page one, Page two){
        page = one;
        pages = two;
    }

    public String getPageName(){
        return pages.getName();
    }
}
