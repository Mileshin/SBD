package entity;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;


/**
 * Created by Andrey on 06.04.2018.
 */
@Table(keyspace = "testkeyspace", name = "spaces",
        readConsistency = "ONE",
        writeConsistency = "ONE",
        caseSensitiveKeyspace = false,
        caseSensitiveTable = false)
public class Spaces {
    @PartitionKey(0)
    private String name;
    @PartitionKey(1)
    private int revision;
    private String description;
    private Date modified;
    @Column(name = "diffs_json")
    private String diffsJson;
    private String parent;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public int getRevision(){
        return revision;
    }
    public void setRevision(int revision){
        this.revision = revision;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public Date getModified(){
        return modified;
    }
    public void setModified(Date modified){
        this.modified = modified;
    }

    public String getDiffsJson(){
        return diffsJson;
    }
    public void setDiffsJson(String diffsJson){
        this.diffsJson = diffsJson;
    }

    public String getParent(){
        return parent;
    }
    public void setParent(String parent){
        this.parent = parent;
    }


    public  Spaces(){};

    public Spaces(int revision, String name, String description,
                  Date modified, String diffsJson,String parent){
        this.revision = revision;
        this.name = name;
        this.description = description;
        this.modified = modified;
        this.diffsJson = diffsJson;
        this.parent = parent;
    }

}
