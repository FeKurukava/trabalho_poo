package com.mycompany.trabalho_poo.dao;

import com.mycompany.trabalho_poo.dto.usuario.Categoria;
import java.util.List;

/**
 * DAO interface for Categoria entity.
 */
public interface CategoriaDAO extends GenericDAO<Categoria, Long> {
    
    /**
     * Finds a category by name.
     * 
     * @param nome The name to search for
     * @return The found category, or null if not found
     */
    Categoria findByNome(String nome);
    
    /**
     * Finds all categories for a specific user.
     * 
     * @param usuarioId The ID of the user
     * @return A list of categories for the user
     */
    List<Categoria> findByUsuario(Long usuarioId);
}