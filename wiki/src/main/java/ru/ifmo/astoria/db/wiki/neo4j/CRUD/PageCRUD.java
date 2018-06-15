package ru.ifmo.astoria.db.wiki.neo4j.CRUD;

import ru.ifmo.astoria.db.wiki.neo4j.entity.Page;

public class PageCRUD extends GenericService<Page>{
    @Override
    Class<Page> getEntityType() {
        return Page.class;
    }
}
