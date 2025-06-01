package com.mycompany.trabalho_poo.dao;

import com.mycompany.trabalho_poo.dto.usuario.Categoria;
import com.mycompany.trabalho_poo.dto.usuario.TipoTransacaoEnum;
import com.mycompany.trabalho_poo.dto.usuario.Transacao;
import java.time.LocalDate;
import java.util.List;

/**
 * DAO interface for Transacao entity.
 */
public interface TransacaoDAO extends GenericDAO<Transacao, Long> {
    
    /**
     * Finds all transactions for a specific user.
     * 
     * @param usuarioId The ID of the user
     * @return A list of transactions for the user
     */
    List<Transacao> findByUsuario(Long usuarioId);
    
    /**
     * Finds transactions for a user filtered by date range.
     * 
     * @param usuarioId The ID of the user
     * @param dataInicio The start date
     * @param dataFim The end date
     * @return A list of filtered transactions
     */
    List<Transacao> findByUsuarioAndPeriodo(Long usuarioId, LocalDate dataInicio, LocalDate dataFim);
    
    /**
     * Finds transactions for a user filtered by transaction type.
     * 
     * @param usuarioId The ID of the user
     * @param tipoTransacao The transaction type
     * @return A list of filtered transactions
     */
    List<Transacao> findByUsuarioAndTipo(Long usuarioId, TipoTransacaoEnum tipoTransacao);
    
    /**
     * Finds transactions for a user filtered by category.
     * 
     * @param usuarioId The ID of the user
     * @param categoriaId The ID of the category
     * @return A list of filtered transactions
     */
    List<Transacao> findByUsuarioAndCategoria(Long usuarioId, Long categoriaId);
    
    /**
     * Finds transactions for a user with multiple filters.
     * 
     * @param usuarioId The ID of the user
     * @param dataInicio The start date (optional)
     * @param dataFim The end date (optional)
     * @param tipoTransacao The transaction type (optional)
     * @param categoriaId The ID of the category (optional)
     * @return A list of filtered transactions
     */
    List<Transacao> findByUsuarioWithFilters(Long usuarioId, LocalDate dataInicio, LocalDate dataFim, 
            TipoTransacaoEnum tipoTransacao, Long categoriaId);
}