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
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.categoria WHERE u.login = :login", Usuario.class);
            query.setParameter("login", login);

            Usuario usuario;
            try {
                usuario = query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }

            if (usuario != null) {
                TypedQuery<Usuario> transacaoQuery = em.createQuery(
                        "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.transacao WHERE u.id = :id", Usuario.class);
                transacaoQuery.setParameter("id", usuario.getId());
                try {
                    return transacaoQuery.getSingleResult();
                } catch (NoResultException e) {
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
            if (usuario != null) {
                TypedQuery<Usuario> transacaoQuery = em.createQuery(
                        "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.transacao WHERE u.id = :id", Usuario.class);
                transacaoQuery.setParameter("id", usuario.getId());
                try {
                    return transacaoQuery.getSingleResult();
                } catch (NoResultException e) {
                    return usuario;
                }
            }

            return usuario;
        } finally {
            em.close();
        }
    }

    @Override
    public Usuario save(Usuario usuario) {
        boolean isNew = usuario.getId() == null;
        Usuario savedUsuario = super.save(usuario);

        if (isNew) {
            savedUsuario.initializeDefaultCategories();
            savedUsuario = super.update(savedUsuario);
        }

        return savedUsuario;
    }

    @Override
    public Usuario findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.categoria WHERE u.id = :id", Usuario.class);
            query.setParameter("id", id);

            Usuario usuario;
            try {
                usuario = query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }

            if (usuario != null) {
                TypedQuery<Usuario> transacaoQuery = em.createQuery(
                        "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.transacao WHERE u.id = :id", Usuario.class);
                transacaoQuery.setParameter("id", id);
                try {
                    return transacaoQuery.getSingleResult();
                } catch (NoResultException e) {
                    return usuario;
                }
            }

            return usuario;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Usuario> findAll() {
        EntityManager em = getEntityManager();
        try {
            List<Usuario> usuarios = em.createQuery(
                    "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.categoria", Usuario.class)
                    .getResultList();
            if (!usuarios.isEmpty()) {
                List<Long> userIds = usuarios.stream()
                        .map(Usuario::getId)
                        .collect(java.util.stream.Collectors.toList());

                List<Usuario> usuariosWithTransacoes = em.createQuery(
                        "SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.transacao WHERE u.id IN :ids", Usuario.class)
                        .setParameter("ids", userIds)
                        .getResultList();

                return usuariosWithTransacoes;
            }

            return usuarios;
        } finally {
            em.close();
        }
    }
}
