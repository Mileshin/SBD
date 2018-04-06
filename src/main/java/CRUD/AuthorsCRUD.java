package CRUD;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import entity.Authors;
import entity.Spaces;
import interfaces.AuthorsAccessor;
import interfaces.SpacesAccessor;

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


    public List<Authors> getAll(){
        Result<Authors> res = authorsAccessor.getAll();
        return res.all();
    }

  public List<Authors> getAllByLogin(String login){
        Result<Authors> res = authorsAccessor.getAllByLogin(login);
        return res.all();
    }

    public  List<Authors> getLastRowsByName(String login, int count){
        Result<Authors> res = authorsAccessor.getLastRowsByLogin(login, count);
        return res.all();
    }

    public List<Authors> getAllBetweenTime(String login, Date time1, Date time2){
        Result<Authors> res = authorsAccessor.getAllBetweenTime(login, time1, time2);
        return res.all();
    }

    public void insertNow(String login, String tableName, String action){
        authorsAccessor.insertNow(login,tableName,action);
    }
}
