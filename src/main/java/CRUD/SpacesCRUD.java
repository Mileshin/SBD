package CRUD;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import entity.Spaces;
import interfaces.SpacesAccessor;
import org.omg.CORBA.DATA_CONVERSION;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Andrey on 06.04.2018.
 */
public class SpacesCRUD {
    private MappingManager manager;
    private Mapper<Spaces> mapper;
    SpacesAccessor spacesAccessor;

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
    public Spaces findSpaces(String name, int revision){
        return mapper.get(name,revision);
    }
    public void deleteSpaces(String name, int revision){
        mapper.delete(name,revision);
    }


    public List<Spaces> getAll(){
        Result<Spaces> res = spacesAccessor.getAll();
        return res.all();
    }

    public List<Spaces> getAllByName(String name){
        Result<Spaces> res = spacesAccessor.getAllByName(name);
        return res.all();
    }

    public List<Spaces> getAllBetweenRevision(String name, int rev1, int rev2){
        Result<Spaces> res = spacesAccessor.getAllBetweenRevision(name, rev1, rev2);
        return res.all();
    }

    public  List<Spaces> getLastRevisionsByName(String name, int count){
        Result<Spaces> res = spacesAccessor.getLastRevisionsByName(name, count);
        return res.all();
    }

    /*public List<Spaces> getAllBetweenTime(String name, Date time1, Date time2){
        Result<Spaces> res = spacesAccessor.getAllBetweenTime(name, time1, time2);
        return res.all();
    }*/

}
