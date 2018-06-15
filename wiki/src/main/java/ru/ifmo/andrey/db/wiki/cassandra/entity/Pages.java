package ru.ifmo.andrey.db.wiki.cassandra.entity;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;


/**
 * Created by Andrey on 06.04.2018.
 */
@Table(keyspace = "wiki", name = "pages",
        readConsistency = "ONE",
        writeConsistency = "ONE",
        caseSensitiveKeyspace = false,
        caseSensitiveTable = false)
public class Pages {
    @PartitionKey(0)
    private String name;

    @PartitionKey(1)
    private Date modificationTime;

    private String author;
    private String content;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getAuthor(){
        return author;
    }
    public void setAuthor(String author){
        this.author = author;
    }

    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public Date getModificationTime(){
        return modificationTime;
    }
    public void setModificationTime(Date modificationTime){
        this.modificationTime = modificationTime;
    }

    public Pages() {}

    public Pages(String name, String author, String content, Date modificationTime){
        this.name = name;
        this.content = content;
        this.modificationTime = modificationTime;
        this.author = author;
    }
}
