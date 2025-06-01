package com.mycompany.trabalho_poo.dao;

import com.mycompany.trabalho_poo.dto.usuario.Categoria;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Implementation of the CategoriaDAO interface.
 */
public class CategoriaDAOImpl extends GenericDAOImpl<Categoria, Long> implements CategoriaDAO {

    @Override
    public Categoria findByNome(String nome) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Categoria> query = em.createQuery(
                    "SELECT c FROM Categoria c WHERE c.nome = :nome", Categoria.class);
            query.setParameter("nome", nome);
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
    public List<Categoria> findByUsuario(Long usuarioId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Categoria> query = em.createQuery(
                    "SELECT c FROM Usuario u JOIN u.categoria c WHERE u.id = :usuarioId", Categoria.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
