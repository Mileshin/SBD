package ru.ifmo.andrey.db.wiki.cassandra.CRUD;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import ru.ifmo.andrey.db.wiki.cassandra.entity.Pages;
import ru.ifmo.andrey.db.wiki.cassandra.entity.Spaces;
import ru.ifmo.andrey.db.wiki.cassandra.interfaces.PagesAccessor;

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

    public Result<Pages> getAll(){
        return pagesAccessor.getAll();
    }

    public Result<Pages> getAllByName(String name){
        return pagesAccessor.getAllByName(name);
    }

    public Result<Pages> getAllBetweenTime(String name, Date time1, Date time2){
        return pagesAccessor.getAllBetweenTime(name, time1, time2);
    }

    public  Result<Pages> getLastRowsByName(String name, int count){
        return pagesAccessor.getLastRowsByName(name, count);
    }

    public void insertNow(String name, String content, String author){
        pagesAccessor.insertNow(name, content, author);
    }
}
