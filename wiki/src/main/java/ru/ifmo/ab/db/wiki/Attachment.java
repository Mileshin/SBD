package ru.ifmo.ab.db.wiki;

import astoria.dummymaker.annotation.string.GenFileType;
import astoria.dummymaker.annotation.string.GenPhrase;
import astoria.dummymaker.annotation.string.GenString;
import astoria.dummymaker.annotation.time.GenTime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "Attachments")
public class Attachment {
    @Id
    private String id;

    private String filename;

    @GenFileType
    private String type;

    @GenPhrase
    private String description;

    private String uploader;

    @GenTime
    private Date uploadTime;
    private List<AccessRights> access;

    @GenString
    private String URI;
}
