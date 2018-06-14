package ru.ifmo.andrey.db.wiki.cassandra.CRUD;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import ru.ifmo.andrey.db.wiki.cassandra.entity.Authors;
import ru.ifmo.andrey.db.wiki.cassandra.entity.Spaces;
import ru.ifmo.andrey.db.wiki.cassandra.interfaces.AuthorsAccessor;

import java.util.Date;
import java.util.List;


/**
 * Created by Andrey on 06.04.2018.
 */
public class AuthorsCRUD {
    private MappingManager manager;
    private Mapper<Spaces> mapper;
    private AuthorsAccessor authorsAccessor;

    public AuthorsCRUD(Session session){
        manager = new MappingManager(session);
        mapper = manager.mapper(Spaces.class);
        authorsAccessor = manager.createAccessor(AuthorsAccessor.class);
    }

    public void insertSpaces(Spaces space){
        mapper.save(space);
    }
    public void updateSpaces(Spaces space){
        mapper.save(space);
    }
    public Spaces findSpaces(String login, Date modified){
        return mapper.get(login,modified);
    }
    public void deleteSpaces(String login, Date modified){
        mapper.delete(login,modified);
    }


    public Result<Authors> getAll(){
        return authorsAccessor.getAll();
    }

  public Result<Authors> getAllByLogin(String login){
        return authorsAccessor.getAllByLogin(login);
    }

    public  Result<Authors> getLastRowsByName(String login, int count){
        return authorsAccessor.getLastRowsByLogin(login, count);
    }

    public Result<Authors> getAllBetweenTime(String login, Date time1, Date time2){
        return authorsAccessor.getAllBetweenTime(login, time1, time2);
    }

    public void insertNow(String login, String tableName, String action){
        authorsAccessor.insertNow(login, tableName, action);
    }
}
