package com.mycompany.trabalho_poo.panels.cadastro;

import com.mycompany.trabalho_poo.SistemaFinanceiro_POO;
import com.mycompany.trabalho_poo.dto.usuario.Usuario;
import com.mycompany.trabalho_poo.panels.login.LoginPanel;

import javax.swing.*;
import java.awt.*;

import static com.mycompany.trabalho_poo.utils.SwingUtils.abrirNovoFrame;

public class CadastrarPanel extends JPanel {

    private JTextField nomeField;
    private JTextField loginField;
    private JPasswordField senhaField;
    private JButton cadastrarButton;
    private JButton voltarButton;

    public CadastrarPanel() {
        initComponents();
    }

    private void initComponents() {
        JLabel jLabel1 = new JLabel("Nome:");
        JLabel jLabel2 = new JLabel("Login:");
        JLabel jLabel3 = new JLabel("Senha:");
        nomeField = new JTextField(20);
        loginField = new JTextField(20);
        senhaField = new JPasswordField(20);
        cadastrarButton = new JButton("Cadastrar");
        voltarButton = new JButton("< Voltar");

        cadastrarButton.addActionListener(e -> cadastrarUsuario());
        voltarButton.addActionListener(e -> voltarParaLogin());

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(voltarButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(jLabel1, gbc);
        gbc.gridx = 1;
        add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(jLabel2, gbc);
        gbc.gridx = 1;
        add(loginField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(jLabel3, gbc);
        gbc.gridx = 1;
        add(senhaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(cadastrarButton, gbc);
    }

    private void cadastrarUsuario() {
        String nome = nomeField.getText();
        String login = loginField.getText();
        String senha = new String(senhaField.getPassword());

        if (nome.isEmpty() || login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario existingUsuario = SistemaFinanceiro_POO.findUsuarioByLogin(login);
        if (existingUsuario != null) {
            JOptionPane.showMessageDialog(this, "Usuário já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario novoUsuario = new Usuario(login, nome, senha);
        SistemaFinanceiro_POO.saveUsuario(novoUsuario);

        JOptionPane.showMessageDialog(this, "Cadastro realizado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        SwingUtilities.getWindowAncestor(this).dispose();
        abrirNovoFrame("Login", new LoginPanel());
    }

    private void voltarParaLogin() {
        SwingUtilities.getWindowAncestor(this).dispose();
        abrirNovoFrame("Login", new LoginPanel());
    }
}
