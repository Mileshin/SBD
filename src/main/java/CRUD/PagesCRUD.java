package CRUD;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import entity.Pages;
import entity.Spaces;
import interfaces.PagesAccessor;

import java.util.List;


/**
 * Created by Andrey on 06.04.2018.
 */
public class PagesCRUD {
    private MappingManager manager;
    private Mapper<Pages> mapper;
    private PagesAccessor pagesAccessor;

    public PagesCRUD(Session session){
        manager = new MappingManager(session);
        mapper = manager.mapper(Pages.class);
        pagesAccessor = manager.createAccessor(PagesAccessor.class);
    }



    public void insertPages(Pages pages){
        mapper.save(pages);
    }
    public void updatePages(Pages pages){
        mapper.save(pages);
    }
    public Pages findPages(String name, int revision){
        return mapper.get(name,revision);
    }
    public void deletePages(String name, int revision){
        mapper.delete(name,revision);
    }

    public List<Spaces> getAll(){
        Result<Spaces> res = pagesAccessor.getAll();
        return res.all();
    }

    public List<Spaces> getAllByName(String name){
        Result<Spaces> res = pagesAccessor.getAllByName(name);
        return res.all();
    }

    public List<Spaces> getAllBetweenRevision(String name, int rev1, int rev2){
        Result<Spaces> res = pagesAccessor.getAllBetweenRevision(name, rev1, rev2);
        return res.all();
    }

    public  List<Spaces> getLastRevisionsByName(String name, int count){
        Result<Spaces> res = pagesAccessor.getLastRevisionsByName(name, count);
        return res.all();
    }

}
