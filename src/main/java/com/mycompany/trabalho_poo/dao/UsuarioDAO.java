package com.mycompany.trabalho_poo.dao;

import com.mycompany.trabalho_poo.dto.usuario.Usuario;
import java.util.List;

public interface UsuarioDAO extends GenericDAO<Usuario, Long> {

    Usuario findByLogin(String login);

    Usuario authenticate(String login, String senha);
}