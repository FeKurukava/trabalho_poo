package com.mycompany.trabalho_poo.dao;

import java.util.List;

/**
 * Generic DAO interface that defines common CRUD operations for all entities.
 * 
 * @param <T> The entity type
 * @param <ID> The type of the entity's ID
 */
public interface GenericDAO<T, ID> {
    
    /**
     * Saves an entity to the database.
     * 
     * @param entity The entity to save
     * @return The saved entity
     */
    T save(T entity);
    
    /**
     * Updates an existing entity in the database.
     * 
     * @param entity The entity to update
     * @return The updated entity
     */
    T update(T entity);
    
    /**
     * Deletes an entity from the database.
     * 
     * @param entity The entity to delete
     */
    void delete(T entity);
    
    /**
     * Finds an entity by its ID.
     * 
     * @param id The ID of the entity to find
     * @return The found entity, or null if not found
     */
    T findById(ID id);
    
    /**
     * Finds all entities of the given type.
     * 
     * @return A list of all entities
     */
    List<T> findAll();
}