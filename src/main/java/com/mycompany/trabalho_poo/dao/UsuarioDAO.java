package com.mycompany.trabalho_poo.dao;

import com.mycompany.trabalho_poo.dto.usuario.Usuario;
import java.util.List;

/**
 * DAO interface for Usuario entity.
 */
public interface UsuarioDAO extends GenericDAO<Usuario, Long> {
    
    /**
     * Finds a user by login.
     * 
     * @param login The login to search for
     * @return The found user, or null if not found
     */
    Usuario findByLogin(String login);
    
    /**
     * Authenticates a user with login and password.
     * 
     * @param login The user's login
     * @param senha The user's password
     * @return The authenticated user, or null if authentication fails
     */
    Usuario authenticate(String login, String senha);
}