package com.mycompany.trabalho_poo.panels.categoria;

import com.mycompany.trabalho_poo.SistemaFinanceiro_POO;
import com.mycompany.trabalho_poo.dto.usuario.Categoria;
import com.mycompany.trabalho_poo.dto.usuario.Transacao;
import com.mycompany.trabalho_poo.panels.financeiro.SistemaFinanceiroPanel;
import com.mycompany.trabalho_poo.utils.SwingUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static com.mycompany.trabalho_poo.SistemaFinanceiro_POO.usuarioLogado;

public class GerenciarCategoriasPanel extends JPanel {
    private JTable categoriaTable;
    private DefaultTableModel tableModel;
    private JTextField nomeCategoriaField;
    private JButton adicionarButton, editarButton, excluirButton, voltarButton;

    public GerenciarCategoriasPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        voltarButton = new JButton("< Voltar");
        voltarButton.addActionListener(e -> voltarParaSistemaFinanceiro());
        topPanel.add(voltarButton);
        add(topPanel, BorderLayout.NORTH);

        String[] colunas = {"Nome da Categoria"};
        tableModel = new DefaultTableModel(colunas, 0);
        categoriaTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(categoriaTable);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Categoria:"));
        nomeCategoriaField = new JTextField(20);
        inputPanel.add(nomeCategoriaField);

        adicionarButton = new JButton("Adicionar");
        adicionarButton.addActionListener(e -> adicionarCategoria());
        inputPanel.add(adicionarButton);

        editarButton = new JButton("Editar");
        editarButton.addActionListener(e -> editarCategoria());
        inputPanel.add(editarButton);

        excluirButton = new JButton("Excluir");
        excluirButton.addActionListener(e -> excluirCategoria());
        inputPanel.add(excluirButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        carregarCategorias();
    }

    private void carregarCategorias() {
        tableModel.setRowCount(0);
        List<Categoria> categorias = usuarioLogado.getCategoria();
        for (Categoria categoria : categorias) {
            tableModel.addRow(new Object[]{categoria.getNome()});
        }
    }

    private void adicionarCategoria() {
        String nome = nomeCategoriaField.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome da categoria não pode estar vazio!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Categoria> categorias = usuarioLogado.getCategoria();
        if (categorias.stream().anyMatch(c -> c.getNome().equalsIgnoreCase(nome))) {
            JOptionPane.showMessageDialog(this, "Categoria já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Create a new category
            Categoria novaCategoria = new Categoria(nome);

            // Save the category to the database and get the managed entity back
            Categoria savedCategoria = SistemaFinanceiro_POO.saveCategoria(novaCategoria, usuarioLogado);

            // Make sure we're using the managed entity with the correct ID
            if (savedCategoria != null) {
                // Refresh the categories list
                carregarCategorias();
                nomeCategoriaField.setText("");

                JOptionPane.showMessageDialog(this, "Categoria adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar categoria: categoria não foi salva corretamente", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar categoria: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarCategoria() {
        int selectedRow = categoriaTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para editar!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nomeAtual = (String) tableModel.getValueAt(selectedRow, 0);
        String novoNome = JOptionPane.showInputDialog(this, "Novo nome da categoria:", nomeAtual);

        if (novoNome == null) {
            return;
        }

        novoNome = novoNome.trim();
        if (novoNome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Get the category to update
            List<Categoria> categorias = usuarioLogado.getCategoria();
            Categoria categoria = categorias.get(selectedRow);

            // Update the category name
            categoria.setNome(novoNome);

            // Save the changes to the database
            SistemaFinanceiro_POO.updateCategoria(categoria);

            // Refresh the categories list
            carregarCategorias();

            JOptionPane.showMessageDialog(this, "Categoria atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar categoria: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCategoria() {
        int selectedRow = categoriaTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para excluir!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Categoria> categorias = usuarioLogado.getCategoria();
        if (categorias.size() == 1) {
            JOptionPane.showMessageDialog(this, "Não é possível excluir todas as categorias!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nomeCategoria = (String) tableModel.getValueAt(selectedRow, 0);
        Categoria categoriaParaExcluir = categorias.get(selectedRow);

        // Store the ID for later reference
        Long categoriaId = categoriaParaExcluir.getId();

        if (categoriaId == null) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir categoria: categoria não possui ID válido", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir a categoria '" + nomeCategoria + "'?\nIsso também excluirá todas as transações associadas!",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // First, delete all transactions associated with this category
                List<Transacao> transacoesParaRemover = usuarioLogado.getTransacao().stream()
                        .filter(transacao -> transacao.getCategoria().getId().equals(categoriaId))
                        .collect(java.util.stream.Collectors.toList());

                for (Transacao transacao : transacoesParaRemover) {
                    SistemaFinanceiro_POO.deleteTransacao(transacao);
                    usuarioLogado.getTransacao().remove(transacao);
                }

                // Remove the category from the user's list
                categorias.remove(selectedRow);

                // Update the user in the database to remove the association in usuario_categoria table
                // Make sure the user was updated successfully
                if (SistemaFinanceiro_POO.updateUsuario(usuarioLogado) != null) {
                    // Then, delete the category from the database
                    try {
                        SistemaFinanceiro_POO.deleteCategoria(categoriaParaExcluir);
                    } catch (Exception e) {
                        // If we can't delete the category, log the error but don't show it to the user
                        // since the category has already been removed from the user's list
                        System.err.println("Erro ao excluir categoria do banco de dados: " + e.getMessage());
                    }

                    // Refresh the categories list
                    carregarCategorias();

                    JOptionPane.showMessageDialog(this, "Categoria excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar usuário no banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir categoria: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void voltarParaSistemaFinanceiro() {
        SwingUtilities.getWindowAncestor(this).dispose();
        SwingUtils.abrirNovoFrame("Sistema Financeiro", new SistemaFinanceiroPanel());
    }
}
