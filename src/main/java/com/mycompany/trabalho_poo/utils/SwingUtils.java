package com.mycompany.trabalho_poo.utils;

import javax.swing.*;

public class SwingUtils {

    public static void abrirNovoFrame(String titulo, JPanel painel) {
        JFrame frame = new JFrame(titulo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setResizable(false);

        JScrollPane scrollPane = new JScrollPane(painel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        frame.setContentPane(scrollPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
