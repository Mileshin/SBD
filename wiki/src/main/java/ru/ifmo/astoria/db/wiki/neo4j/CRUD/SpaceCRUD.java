package ru.ifmo.astoria.db.wiki.neo4j.CRUD;

import ru.ifmo.astoria.db.wiki.neo4j.entity.Space;

public class SpaceCRUD extends GenericService<Space> {
    @Override
    public Long getIdByName(String name) {
        return super.getIdByName(name);
    }

    @Override
    public Space createOrUpdate(Space entity) {
        return super.createOrUpdate(entity);
    }

    @Override
    public Space find(Long id) {
        return super.find(id);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    Class<Space> getEntityType() {
        return Space.class;
    }
}
