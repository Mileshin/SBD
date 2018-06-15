package ru.ifmo.astoria.db.wiki.neo4j.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import ru.ifmo.astoria.db.wiki.neo4j.interfaces.Entity;

@NodeEntity
public class Space implements Entity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Space() {}

    public Space(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() { return name; }
}
