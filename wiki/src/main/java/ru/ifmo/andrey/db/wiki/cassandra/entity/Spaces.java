package ru.ifmo.andrey.db.wiki.cassandra.entity;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;


/**
 * Created by Andrey on 06.04.2018.
 */
@Table(keyspace = "wiki", name = "spaces",
        readConsistency = "ONE",
        writeConsistency = "ONE",
        caseSensitiveKeyspace = false,
        caseSensitiveTable = false)
public class Spaces {
    @PartitionKey(0)
    private String name;

    @PartitionKey(1)
    private Date modificationTime;

    private String content;
    private String author;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public Date getModificationTime(){
        return modificationTime;
    }
    public void setModificationTime(Date modificationTime){
        this.modificationTime = modificationTime;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Spaces() {};

    public Spaces(String name, Date modificationTime, String content, String author){
        this.name = name;
        this.modificationTime = modificationTime;
        this.content = content;
        this.author = author;
    }
}
