package com.simpleproject.demo.dao;

import com.simpleproject.demo.model.Task;

import java.util.List;

public interface DAO<T, ID> {
    void save(T entity);

    List<T> listAll();

    void delete(ID id);

    T findByID(ID id);

    void update(ID id, T entity);
}
