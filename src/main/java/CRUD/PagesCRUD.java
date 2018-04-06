package CRUD;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import entity.Pages;



/**
 * Created by Andrey on 06.04.2018.
 */
public class PagesCRUD {
    private MappingManager manager;
    private Mapper<Pages> mapper;

    public PagesCRUD(Session session){
        manager = new MappingManager(session);
        mapper = manager.mapper(Pages.class);
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
}
