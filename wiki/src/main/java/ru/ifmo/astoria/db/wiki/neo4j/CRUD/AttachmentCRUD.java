package ru.ifmo.astoria.db.wiki.neo4j.CRUD;

import ru.ifmo.astoria.db.wiki.neo4j.entity.Attachment;

public class AttachmentCRUD extends GenericService<Attachment> {
    @Override
    public Long getIdByName(String name) {
        return super.getIdByName(name);
    }

    @Override
    public Attachment createOrUpdate(Attachment entity) {
        return super.createOrUpdate(entity);
    }

    @Override
    public Attachment find(Long id) {
        return super.find(id);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    Class<Attachment> getEntityType() {
        return Attachment.class;
    }
}
