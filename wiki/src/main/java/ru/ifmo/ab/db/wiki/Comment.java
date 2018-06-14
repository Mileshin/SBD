package ru.ifmo.ab.db.wiki;

import lombok.*;

import java.util.Date;

@Data
@ToString
public class Comment {
    private String author;
    private Date creationTime;
    private String content;
}
