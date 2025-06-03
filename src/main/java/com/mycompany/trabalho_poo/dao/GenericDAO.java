package com.mycompany.trabalho_poo.dao;

import java.util.List;

public interface GenericDAO<T, ID> {

    T save(T entity);

    T update(T entity);

    void delete(T entity);

    T findById(ID id);

    List<T> findAll();
}