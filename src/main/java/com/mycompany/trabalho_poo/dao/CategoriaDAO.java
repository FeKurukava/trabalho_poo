package com.mycompany.trabalho_poo.dao;

import com.mycompany.trabalho_poo.dto.usuario.Categoria;
import java.util.List;

public interface CategoriaDAO extends GenericDAO<Categoria, Long> {

    Categoria findByNome(String nome);

    List<Categoria> findByUsuario(Long usuarioId);
}