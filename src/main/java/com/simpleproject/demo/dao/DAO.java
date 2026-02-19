package com.simpleproject.demo.dao;


import java.util.List;
import java.util.Optional;

//general dao
public interface DAO<T, ID> {
    void save(T entity);

    List<T> listAll();

    void delete(ID id);

    Optional<T> findByID(ID id);

    void update(ID id, T entity);
}
