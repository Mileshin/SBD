package ru.ifmo.astoria.db.wiki.neo4j.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import ru.ifmo.astoria.db.wiki.neo4j.entity.relationships.IncludedInRelations;
import ru.ifmo.astoria.db.wiki.neo4j.entity.relationships.IncludesRelation;
import ru.ifmo.astoria.db.wiki.neo4j.entity.relationships.LinksRelations;
import ru.ifmo.astoria.db.wiki.neo4j.interfaces.Entity;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

@NodeEntity
public class Page implements Entity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Relationship(type = "LINKED")
    private Set<LinksRelations> pages = new HashSet<>();

    @Relationship(type = "INCLUDES")
    private Set<IncludesRelation> attachments = new HashSet<>();

    @Relationship(type = "INCLUDED_IN")
    private Set<IncludedInRelations> spaces = new HashSet<>();

    public Page() {}

    public Page(String name) {
        this.name = name;
    }

    public void addSpace(Space space){ spaces.add(new IncludedInRelations(this, space)); }

    public void addAttachment(Attachment attachment){
        attachments.add(new IncludesRelation(this, attachment));
    }

    public void addPage(Page page) {
        pages.add(new LinksRelations(this, page));
    }

    public void removeSpace(Space space){
        Predicate<IncludedInRelations> predicate = p -> p.getSpaceName().equals(space.getName());
        spaces.removeIf(predicate);
    }

    public void removeAttachment(Attachment attachment){
        Predicate<IncludesRelation> predicate = p -> p.getAttachmentName().equals(attachment.getName());
        attachments.removeIf(predicate);
    }

    public void removePage(Page page){
        Predicate<LinksRelations> predicate = p -> p.getPageName().equals(page.getName());
        pages.removeIf(predicate);
    }

    public String getName() { return name; }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
