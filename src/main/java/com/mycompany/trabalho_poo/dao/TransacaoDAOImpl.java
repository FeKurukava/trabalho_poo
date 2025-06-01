package com.mycompany.trabalho_poo.dao;

import com.mycompany.trabalho_poo.dto.usuario.TipoTransacaoEnum;
import com.mycompany.trabalho_poo.dto.usuario.Transacao;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Implementation of the TransacaoDAO interface.
 */
public class TransacaoDAOImpl extends GenericDAOImpl<Transacao, Long> implements TransacaoDAO {
    
    @Override
    public List<Transacao> findByUsuario(Long usuarioId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Transacao> query = em.createQuery(
                    "SELECT t FROM Transacao t WHERE t.usuario.id = :usuarioId", Transacao.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Transacao> findByUsuarioAndPeriodo(Long usuarioId, LocalDate dataInicio, LocalDate dataFim) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Transacao> query = em.createQuery(
                    "SELECT t FROM Transacao t WHERE t.usuario.id = :usuarioId " +
                    "AND t.dataCadastro BETWEEN :dataInicio AND :dataFim", Transacao.class);
            query.setParameter("usuarioId", usuarioId);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Transacao> findByUsuarioAndTipo(Long usuarioId, TipoTransacaoEnum tipoTransacao) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Transacao> query = em.createQuery(
                    "SELECT t FROM Transacao t WHERE t.usuario.id = :usuarioId " +
                    "AND t.tipoTransacao = :tipoTransacao", Transacao.class);
            query.setParameter("usuarioId", usuarioId);
            query.setParameter("tipoTransacao", tipoTransacao);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Transacao> findByUsuarioAndCategoria(Long usuarioId, Long categoriaId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Transacao> query = em.createQuery(
                    "SELECT t FROM Transacao t WHERE t.usuario.id = :usuarioId " +
                    "AND t.categoria.id = :categoriaId", Transacao.class);
            query.setParameter("usuarioId", usuarioId);
            query.setParameter("categoriaId", categoriaId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Transacao> findByUsuarioWithFilters(Long usuarioId, LocalDate dataInicio, LocalDate dataFim, 
            TipoTransacaoEnum tipoTransacao, Long categoriaId) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Transacao> cq = cb.createQuery(Transacao.class);
            Root<Transacao> root = cq.from(Transacao.class);
            
            List<Predicate> predicates = new ArrayList<>();
            
            // Always filter by user
            predicates.add(cb.equal(root.get("usuario").get("id"), usuarioId));
            
            // Add optional filters
            if (dataInicio != null && dataFim != null) {
                predicates.add(cb.between(root.get("dataCadastro"), dataInicio, dataFim));
            } else if (dataInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataCadastro"), dataInicio));
            } else if (dataFim != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataCadastro"), dataFim));
            }
            
            if (tipoTransacao != null) {
                predicates.add(cb.equal(root.get("tipoTransacao"), tipoTransacao));
            }
            
            if (categoriaId != null) {
                predicates.add(cb.equal(root.get("categoria").get("id"), categoriaId));
            }
            
            cq.where(predicates.toArray(new Predicate[0]));
            
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }
}