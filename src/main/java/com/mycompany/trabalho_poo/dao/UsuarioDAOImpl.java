package com.mycompany.trabalho_poo.dao;

import com.mycompany.trabalho_poo.dto.usuario.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Implementation of the UsuarioDAO interface.
 */
public class UsuarioDAOImpl extends GenericDAOImpl<Usuario, Long> implements UsuarioDAO {
    
    @Override
    public Usuario findByLogin(String login) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.login = :login", Usuario.class);
            query.setParameter("login", login);
            try {
                return query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        } finally {
            em.close();
        }
    }
    
    @Override
    public Usuario authenticate(String login, String senha) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha", Usuario.class);
            query.setParameter("login", login);
            query.setParameter("senha", senha);
            try {
                return query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        } finally {
            em.close();
        }
    }
    
    /**
     * Saves a user and initializes default categories if it's a new user.
     * 
     * @param usuario The user to save
     * @return The saved user
     */
    @Override
    public Usuario save(Usuario usuario) {
        boolean isNew = usuario.getId() == null;
        Usuario savedUsuario = super.save(usuario);
        
        if (isNew) {
            // Initialize default categories for new users
            savedUsuario.initializeDefaultCategories();
            savedUsuario = super.update(savedUsuario);
        }
        
        return savedUsuario;
    }
}