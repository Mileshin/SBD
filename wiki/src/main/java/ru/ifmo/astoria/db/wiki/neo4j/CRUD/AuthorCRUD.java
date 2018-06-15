package ru.ifmo.astoria.db.wiki.neo4j.CRUD;

import ru.ifmo.astoria.db.wiki.neo4j.entity.Author;

import java.util.HashMap;
import java.util.Map;

public class AuthorCRUD extends GenericService<Author> {
    @Override
    public Long getIdByName(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("param", name);
        Iterable<Author> iterable = session.query(getEntityType(),
                "MATCH (n: " + getEntityType().getSimpleName() +" ) WHERE n.login = {param} RETURN n", map);
        if (iterable.iterator().hasNext()) {
            return iterable.iterator().next().getId();
        }
        return null;
    }

    @Override
    public Author createOrUpdate(Author entity) {
        return super.createOrUpdate(entity);
    }

    @Override
    public Author find(Long id) {
        return super.find(id);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    Class<Author> getEntityType() {
        return Author.class;
    }
}
