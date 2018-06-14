package ru.ifmo.ab.db.wiki;

import astoria.dummymaker.annotation.string.GenPhrase;
import astoria.dummymaker.annotation.time.GenTime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@ToString
@Document(collection = "Pages")
public class Page {
    @Id
    private String id;

    private String name;

    @GenPhrase
    private String content;

    private List<String> refersTo;
    private List<String> attachments;
    private List<String> spaces;

    @GenTime
    private Date creationTime;

    private Date modificationTime;
    private String creator;
    private List<AccessRights> access;
    private List<Comment> comments;
}
