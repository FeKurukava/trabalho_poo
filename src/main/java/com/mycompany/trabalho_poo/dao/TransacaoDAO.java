package com.mycompany.trabalho_poo.dao;

import com.mycompany.trabalho_poo.dto.usuario.Categoria;
import com.mycompany.trabalho_poo.dto.usuario.TipoTransacaoEnum;
import com.mycompany.trabalho_poo.dto.usuario.Transacao;
import java.time.LocalDate;
import java.util.List;

public interface TransacaoDAO extends GenericDAO<Transacao, Long> {

    List<Transacao> findByUsuario(Long usuarioId);

    List<Transacao> findByUsuarioAndPeriodo(Long usuarioId, LocalDate dataInicio, LocalDate dataFim);

    List<Transacao> findByUsuarioAndTipo(Long usuarioId, TipoTransacaoEnum tipoTransacao);

    List<Transacao> findByUsuarioAndCategoria(Long usuarioId, Long categoriaId);

    List<Transacao> findByUsuarioWithFilters(Long usuarioId, LocalDate dataInicio, LocalDate dataFim, 
            TipoTransacaoEnum tipoTransacao, Long categoriaId);
}