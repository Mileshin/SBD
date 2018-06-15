package ru.ifmo.astoria.db.wiki.neo4j.interfaces;

public interface CRUDService<T> {
    Iterable<T> findAll();

    Long getIdByName(String name);

    T find(Long id);

    void delete(Long id);

    T createOrUpdate(T object);
}