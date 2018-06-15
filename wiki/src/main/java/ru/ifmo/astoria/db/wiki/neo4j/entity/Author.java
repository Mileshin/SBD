package ru.ifmo.astoria.db.wiki.neo4j.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import ru.ifmo.astoria.db.wiki.neo4j.entity.relationships.CommentedRelationship;
import ru.ifmo.astoria.db.wiki.neo4j.entity.relationships.CreationRelationship;
import ru.ifmo.astoria.db.wiki.neo4j.entity.relationships.UploadRelationship;
import ru.ifmo.astoria.db.wiki.neo4j.interfaces.Entity;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

@NodeEntity
public class Author implements Entity {
    @Id
    @GeneratedValue
    private Long id;

    private String login;

    @Relationship(type = "CREATED")
    private Set<CreationRelationship> pageCreated = new HashSet<>();

    @Relationship(type = "COMMENTED")
    private Set<CommentedRelationship> pageCommented = new HashSet<>();

    @Relationship(type = "UPLOADED")
    private Set<UploadRelationship> attachments = new HashSet<>();

    public Author() {}

    public Author(String login) {
        this.login = login;
    }

    public void setPageCreated(Page page) {
        pageCreated.add(new CreationRelationship(this, page));
    }

    public void setPageCommented(Page page) {
        pageCommented.add(new CommentedRelationship(this, page));
    }

    public void setAttachment(Attachment attachment) {
        attachments.add(new UploadRelationship(this, attachment));
    }

    public void removePageCreated(Page page){
        Predicate<CreationRelationship> predicate =  p -> p.getPageName().equals(page.getName());
        pageCreated.removeIf(predicate);
    }

    public void removePageAttachment(Attachment attachment){
        Predicate<UploadRelationship> predicate =  p -> p.getAttachmentName().equals(attachment.getName());
        attachments.removeIf(predicate);
    }

    public void removePageCommented(Page page){
        Predicate<CommentedRelationship> predicate =  p -> p.getPageName().equals(page.getName());
        pageCommented.removeIf(predicate);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.login;
    }
}