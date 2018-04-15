package CRUD;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import entity.Pages;
import entity.Spaces;
import interfaces.PagesAccessor;

import java.util.Date;
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
    public Pages findPages(String name, Date modified){
        return mapper.get(name,modified);
    }
    public void deletePages(String name, Date modified){
        mapper.delete(name,modified);
    }

    public List<Spaces> getAll(){
        Result<Spaces> res = pagesAccessor.getAll();
        return res.all();
    }

    public List<Spaces> getAllByName(String name){
        Result<Spaces> res = pagesAccessor.getAllByName(name);
        return res.all();
    }

    public List<Spaces> getAllBetweenTime(String name, Date time1, Date time2){
        Result<Spaces> res = pagesAccessor.getAllBetweenTime(name, time1, time2);
        return res.all();
    }

    public  List<Spaces> getLastRowsByName(String name, int count){
        Result<Spaces> res = pagesAccessor.getLastRowsByName(name, count);
        return res.all();
    }

    public void insertNow(String name, String content, String diff_json){
        pagesAccessor.insertNow(name,content,diff_json);
    }
}
