/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.trabalho_poo;

import com.mycompany.trabalho_poo.dto.usuario.Usuario;
import com.mycompany.trabalho_poo.panels.login.LoginPanel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

import static com.mycompany.trabalho_poo.utils.SwingUtils.abrirNovoFrame;


public class SistemaFinanceiro_POO {
    
    public static List<Usuario> usuariosCadastrados = new ArrayList();
    public static Usuario usuarioLogado = null;

    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        abrirNovoFrame("Login", new LoginPanel());
    });
}

}
