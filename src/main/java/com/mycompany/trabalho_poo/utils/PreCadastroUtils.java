package com.mycompany.trabalho_poo.utils;

import com.mycompany.trabalho_poo.dto.usuario.Categoria;

import java.util.Arrays;
import java.util.List;

public class PreCadastroUtils {

    public static List<Categoria> buildCategoriasDefault() {
        return Arrays.asList(
                new Categoria("Alimentação"),
                new Categoria("Conta de água"),
                new Categoria("Transporte")
        );
    }
}
