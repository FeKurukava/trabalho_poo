package com.mycompany.trabalho_poo.dto.usuario;

public enum TipoTransacaoEnum {
    RECEITA(1, "Receita"),
    DESPESA(2, "Despesa");

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    private Integer id;
    private String descricao;

    TipoTransacaoEnum(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
