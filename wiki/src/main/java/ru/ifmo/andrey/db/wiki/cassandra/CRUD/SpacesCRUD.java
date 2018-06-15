package ru.ifmo.andrey.db.wiki.cassandra.CRUD;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import ru.ifmo.andrey.db.wiki.cassandra.entity.Spaces;
import ru.ifmo.andrey.db.wiki.cassandra.interfaces.SpacesAccessor;

import java.util.Date;
import java.util.List;


/**
 * Created by Andrey on 06.04.2018.
 */
public class SpacesCRUD {
    private MappingManager manager;
    private Mapper<Spaces> mapper;
    private SpacesAccessor spacesAccessor;

    public SpacesCRUD(Session session){
        manager = new MappingManager(session);
        mapper = manager.mapper(Spaces.class);
        spacesAccessor = manager.createAccessor(SpacesAccessor.class);
    }

    public void insertSpaces(Spaces space){
        mapper.save(space);
    }
    public void updateSpaces(Spaces space){
        mapper.save(space);
    }
    public Spaces findSpaces(String name, Date modified){
        return mapper.get(name, modified);
    }
    public void deleteSpaces(String name, Date modified){
        mapper.delete(name, modified);
    }


    public Result<Spaces> getAll(){
        return spacesAccessor.getAll();
    }

    public Result<Spaces> getAllByName(String name){
        return spacesAccessor.getAllByName(name);
    }

    public Result<Spaces> getAllBetweenTime(String name, Date time1, Date time2){
        return spacesAccessor.getAllBetweenTime(name, time1, time2);
    }

    public Result<Spaces> getLastRowsByName(String name, int count){
        return spacesAccessor.getLastRowsByName(name, count);
    }

    public void insertNow(String name, String content, String author){
        spacesAccessor.insertNow(name, content, author);
    }

}
