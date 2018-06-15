package ru.ifmo.astoria.db.wiki.neo4j.entity.relationships;

import org.neo4j.ogm.annotation.*;
import ru.ifmo.astoria.db.wiki.neo4j.entity.Page;
import ru.ifmo.astoria.db.wiki.neo4j.entity.Space;

@RelationshipEntity(type = "INCLUDED_IN")
public class IncludedInRelations {
    @Id
    @GeneratedValue
    private Long relationshipId;

    @StartNode Page page;
    @EndNode Space space;

    public IncludedInRelations() {}

    public IncludedInRelations(Page page, Space space){
        this.page = page;
        this.space = space;
    }

    public String getSpaceName() { return space.getName(); }
}
