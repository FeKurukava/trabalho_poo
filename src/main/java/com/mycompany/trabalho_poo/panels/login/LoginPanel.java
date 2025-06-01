package com.mycompany.trabalho_poo.panels.login;

import com.mycompany.trabalho_poo.SistemaFinanceiro_POO;
import com.mycompany.trabalho_poo.dto.usuario.Usuario;
import com.mycompany.trabalho_poo.panels.financeiro.SistemaFinanceiroPanel;
import com.mycompany.trabalho_poo.panels.cadastro.CadastrarPanel;
import com.mycompany.trabalho_poo.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.mycompany.trabalho_poo.SistemaFinanceiro_POO.usuarioLogado;

public class LoginPanel extends JPanel {

    private JTextField loginField;
    private JPasswordField senhaField;
    private JButton loginButton;
    private JButton cadastrarButton;

    public LoginPanel() {
        initComponents();
    }

    private void initComponents() {
        JLabel jLabel1 = new JLabel("Login:");
        JLabel jLabel2 = new JLabel("Senha:");
        loginField = new JTextField(20);
        senhaField = new JPasswordField(20);
        loginButton = new JButton("Entrar");
        cadastrarButton = new JButton("Cadastrar");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarLogin();
            }
        });

        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirCadastrarPanel();
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(jLabel1, gbc);

        gbc.gridx = 1;
        add(loginField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(jLabel2, gbc);

        gbc.gridx = 1;
        add(senhaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        gbc.gridy = 3;
        add(cadastrarButton, gbc);
    }

    private void validarLogin() {
        String user = loginField.getText();
        String pass = new String(senhaField.getPassword());

        Usuario usuario = SistemaFinanceiro_POO.authenticateUsuario(user, pass);

        if (usuario != null) {
            usuarioLogado = usuario;
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.getWindowAncestor(this).dispose();
            SwingUtils.abrirNovoFrame("Sistema Financeiro", new SistemaFinanceiroPanel());
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirCadastrarPanel() {
        SwingUtilities.getWindowAncestor(this).dispose();

        JFrame cadastroFrame = new JFrame("Cadastrar Usuário");
        cadastroFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cadastroFrame.setSize(400, 300);
        cadastroFrame.setLocationRelativeTo(null);
        cadastroFrame.setContentPane(new CadastrarPanel());
        cadastroFrame.setVisible(true);
    }
}
