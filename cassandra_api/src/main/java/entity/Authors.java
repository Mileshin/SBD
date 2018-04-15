package entity;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;

/**
 * Created by Andrey on 06.04.2018.
 */
@Table(keyspace = "testkeyspace", name = "authors",
        readConsistency = "ONE",
        writeConsistency = "ONE",
        caseSensitiveKeyspace = false,
        caseSensitiveTable = false)
public class Authors {
    @PartitionKey(0)
    private String login;
    @PartitionKey(1)
    private Date modified;
    @Column(name = "table_name")
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

    public Date getModified(){
        return modified;
    }
    public void setModified(Date modified){
        this.modified = modified;
    }

    public String getAction(){
        return action;
    }

    public void setAction(String action){
        this.action = action;
    }

}
