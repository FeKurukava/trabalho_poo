/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.trabalho_poo.dto.usuario;

import java.math.BigDecimal;
import java.time.LocalDate;


public class Transacao {
    
    private BigDecimal valor;
    private String descricao;
    private LocalDate dataCadastro;
    private TipoTransacaoEnum tipoTransacao;
    private Categoria categoria;

    public Transacao(BigDecimal valor, Categoria categoria, TipoTransacaoEnum tipoTransacao, LocalDate dataCadastro, String descricao) {
        this.valor = valor;
        this.categoria = categoria;
        this.tipoTransacao = tipoTransacao;
        this.dataCadastro = dataCadastro;
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public TipoTransacaoEnum getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(TipoTransacaoEnum tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
