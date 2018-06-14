package ru.ifmo.ab.db.wiki;

import astoria.dummymaker.annotation.string.GenPhrase;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@ToString
@Document(collection = "Spaces")
public class Space {
    @Id
    private String id;

    private String name;

    @GenPhrase
    private String description;

    @GenPhrase
    private Date creationTime;

    private Date modificationTime;
    private String creator;
    private List<AccessRights> access;
    private String parent;
    private List<String> children;
}
