package ru.ifmo.astoria.db.wiki.neo4j.CRUD;

import org.neo4j.ogm.session.Session;
import ru.ifmo.astoria.db.wiki.neo4j.Neo4jSessionFactory;
import ru.ifmo.astoria.db.wiki.neo4j.interfaces.*;

import java.util.HashMap;
import java.util.Map;

abstract class GenericService<T> implements CRUDService<T> {
    private static final int DEPTH_LIST = 0;
    private static final int DEPTH_ENTITY = 3;

    protected Session session = Neo4jSessionFactory.getInstance().getNeo4jSession();

    private T object;

    @Override
    public Long getIdByName(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("param", name);
        Iterable<T> iterable = session.query(getEntityType(),
                "MATCH (n: " + getEntityType().getSimpleName() +" ) WHERE n.name = {param} RETURN n", map);
        if (iterable.iterator().hasNext()) {
            object = iterable.iterator().next();
            if (object instanceof Entity)
                return ((Entity) object).getId();
        }
        return null;
    }

    @Override
    public Iterable<T> findAll() {
        return session.loadAll(getEntityType(), DEPTH_LIST);
    }

    @Override
    public T find(Long id) {
        return session.load(getEntityType(), id, DEPTH_ENTITY);
    }

    @Override
    public void delete(Long id) {
        session.delete(session.load(getEntityType(), id));
    }

    @Override
    public T createOrUpdate(T entity) {
        if (entity instanceof Entity) {
            Entity e = (Entity)entity;
            session.save(entity, DEPTH_ENTITY);
            return find(e.getId());
        }
        return null;
    }

    abstract Class<T> getEntityType();
}