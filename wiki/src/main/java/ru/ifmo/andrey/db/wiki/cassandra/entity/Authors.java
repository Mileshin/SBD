package ru.ifmo.andrey.db.wiki.cassandra.entity;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;

/**
 * Created by Andrey on 06.04.2018.
 */
@Table(keyspace = "wiki", name = "authors",
        readConsistency = "ONE",
        writeConsistency = "ONE",
        caseSensitiveKeyspace = false,
        caseSensitiveTable = false)
public class Authors {
    @PartitionKey(0)
    private String login;

    @PartitionKey(1)
    private Date modificationTime;

    private String tableName;
    private String action;

    public String getLogin(){
        return login;
    }
    public void setLogin(String login){
        this.login = login;
    }

    public String getTableName(){
        return tableName;
    }
    public void setTableName(String tableName){
        this.tableName = tableName;
    }

    public Date getModificationTime(){
        return modificationTime;
    }
    public void setModificationTime(Date modificationTime){
        this.modificationTime = modificationTime;
    }

    public String getAction(){
        return action;
    }
    public void setAction(String action){
        this.action = action;
    }
}
