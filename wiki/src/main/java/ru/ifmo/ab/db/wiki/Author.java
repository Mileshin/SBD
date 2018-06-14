package ru.ifmo.ab.db.wiki;

import astoria.dummymaker.annotation.string.GenName;
import astoria.dummymaker.annotation.string.GenString;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = "Authors")
public class Author {
    @Id
    private String id;

    private String login;

    @GenName
    private String name;

    @GenString
    private String group;
}
