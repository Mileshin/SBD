package ru.ifmo.astoria.db.wiki.neo4j.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import ru.ifmo.astoria.db.wiki.neo4j.interfaces.Entity;

@NodeEntity
public class Attachment implements Entity {
    @Override
    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Attachment() {}

    public Attachment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
