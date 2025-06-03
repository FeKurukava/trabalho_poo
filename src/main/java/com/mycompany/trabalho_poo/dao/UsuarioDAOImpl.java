package com.mycompany.trabalho_poo.dao;

import com.mycompany.trabalho_poo.dto.usuario.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class UsuarioDAOImpl extends GenericDAOImpl<Usuario, Long> implements UsuarioDAO {

    @Override
    public Usuario findByLogin(String login) {
        EntityManager em = getEntityManager();
        try {
            // First fetch user with categoria collection
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.categoria WHERE u.login = :login", Usuario.class);
            query.setParameter("login", login);

            Usuario usuario;
            try {
                usuario = query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }

            // Then fetch the same user with transacao collection
            if (usuario != null) {
                TypedQuery<Usuario> transacaoQuery = em.createQuery(
                        "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.transacao WHERE u.id = :id", Usuario.class);
                transacaoQuery.setParameter("id", usuario.getId());
                try {
                    return transacaoQuery.getSingleResult();
                } catch (NoResultException e) {
                    // If for some reason the second query fails, return the user from the first query
                    return usuario;
                }
            }

            return usuario;
        } finally {
            em.close();
        }
    }

    @Override
    public Usuario authenticate(String login, String senha) {
        EntityManager em = getEntityManager();
        try {
            // First fetch user with categoria collection
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.categoria WHERE u.login = :login AND u.senha = :senha", Usuario.class);
            query.setParameter("login", login);
            query.setParameter("senha", senha);

            Usuario usuario;
            try {
                usuario = query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }

            // Then fetch the same user with transacao collection
            if (usuario != null) {
                TypedQuery<Usuario> transacaoQuery = em.createQuery(
                        "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.transacao WHERE u.id = :id", Usuario.class);
                transacaoQuery.setParameter("id", usuario.getId());
                try {
                    return transacaoQuery.getSingleResult();
                } catch (NoResultException e) {
                    // If for some reason the second query fails, return the user from the first query
                    return usuario;
                }
            }

            return usuario;
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

    /**
     * Overrides the findById method to eagerly fetch the categoria and transacao collections.
     * 
     * @param id The ID of the user to find
     * @return The found user, or null if not found
     */
    @Override
    public Usuario findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            // First fetch user with categoria collection
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.categoria WHERE u.id = :id", Usuario.class);
            query.setParameter("id", id);

            Usuario usuario;
            try {
                usuario = query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }

            // Then fetch the same user with transacao collection
            if (usuario != null) {
                TypedQuery<Usuario> transacaoQuery = em.createQuery(
                        "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.transacao WHERE u.id = :id", Usuario.class);
                transacaoQuery.setParameter("id", id);
                try {
                    return transacaoQuery.getSingleResult();
                } catch (NoResultException e) {
                    // If for some reason the second query fails, return the user from the first query
                    return usuario;
                }
            }

            return usuario;
        } finally {
            em.close();
        }
    }

    /**
     * Overrides the findAll method to eagerly fetch the categoria and transacao collections.
     * 
     * @return A list of all users with their categoria and transacao collections initialized
     */
    @Override
    public List<Usuario> findAll() {
        EntityManager em = getEntityManager();
        try {
            // First fetch users with categoria collection
            List<Usuario> usuarios = em.createQuery(
                    "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.categoria", Usuario.class)
                    .getResultList();

            // Then fetch the same users with transacao collection
            // This is done in two steps to avoid cartesian product issues with multiple collections
            if (!usuarios.isEmpty()) {
                // Get all user IDs
                List<Long> userIds = usuarios.stream()
                        .map(Usuario::getId)
                        .collect(java.util.stream.Collectors.toList());

                // Fetch the same users with transacao collection
                List<Usuario> usuariosWithTransacoes = em.createQuery(
                        "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.transacao WHERE u.id IN :ids", Usuario.class)
                        .setParameter("ids", userIds)
                        .getResultList();

                // Replace the original list with the fully initialized users
                return usuariosWithTransacoes;
            }

            return usuarios;
        } finally {
            em.close();
        }
    }
}
