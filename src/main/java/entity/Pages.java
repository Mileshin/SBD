package entity;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;


/**
 * Created by Andrey on 06.04.2018.
 */
@Table(keyspace = "testkeyspace", name = "pages",
        readConsistency = "ONE",
        writeConsistency = "ONE",
        caseSensitiveKeyspace = false,
        caseSensitiveTable = false)
public class Pages {
    @PartitionKey(0)
    private String name;
    @PartitionKey(1)
    private int revision;
    private String content;
    private Date modified;
    @Column(name = "diffs_json")
    private String diffsJson;

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

    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
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



    public Pages(){};

    public Pages(int revision, String name, String content,
                 Date modified, String diffsJson){
        this.revision = revision;
        this.name = name;
        this.content = content;
        this.modified = modified;
        this.diffsJson = diffsJson;
    }

}
