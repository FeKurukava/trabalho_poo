package com.mycompany.trabalho_poo.panels.financeiro;

import com.mycompany.trabalho_poo.dto.usuario.Categoria;
import com.mycompany.trabalho_poo.dto.usuario.TipoTransacaoEnum;
import com.mycompany.trabalho_poo.dto.usuario.Transacao;
import com.mycompany.trabalho_poo.panels.ResumoFinanceiroPanel;
import com.mycompany.trabalho_poo.panels.categoria.GerenciarCategoriasPanel;
import com.mycompany.trabalho_poo.panels.lancamentos.LancamentosPanel;
import com.mycompany.trabalho_poo.panels.login.LoginPanel;
import com.mycompany.trabalho_poo.utils.SwingUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.mycompany.trabalho_poo.SistemaFinanceiro_POO.usuarioLogado;

public class SistemaFinanceiroPanel extends JPanel {
    private JMenuBar menuBar;
    private JMenu menuOperacoes, menuHistorico;
    private JMenuItem menuLancamentos, menuCadastrarCategoria, menuResumoFinanceiro;
    private JTable transacoesTable;
    private DefaultTableModel tableModel;
    private JTextField dataField;
    private JComboBox<TipoTransacaoEnum> tipoBox;
    private JComboBox<Categoria> categoriaBox;

    public SistemaFinanceiroPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        initMenus();

        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        this.add(mainPanel, BorderLayout.CENTER);

        JPanel boasVindasPanel = initBoasVindasPanel();
        mainPanel.add(boasVindasPanel, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        mainPanel.add(filterPanel, BorderLayout.CENTER);
        initFiltrosPanel(filterPanel);

        JLabel tableTitle = new JLabel("Histórico de Transações", JLabel.CENTER);
        tableTitle.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(tableTitle, BorderLayout.SOUTH);

        JScrollPane jScrollPane = initTabelaTransacoes();
        this.add(jScrollPane, BorderLayout.SOUTH);

        revalidate();
        repaint();

        carregarTransacoes();
    }


    private void carregarTransacoes() {
        tableModel.setRowCount(0);
        List<Transacao> transacoes = usuarioLogado.getTransacao();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String dataFiltro = dataField.getText().trim();
        TipoTransacaoEnum tipoFiltro = (TipoTransacaoEnum) tipoBox.getSelectedItem();
        Categoria categoriaFiltro = (Categoria) categoriaBox.getSelectedItem();

        List<Transacao> transacoesFiltradas = transacoes.stream()
                .filter(t -> {
                    if (dataFiltro.isEmpty()) return true;

                    if (t.getDataCadastro() == null) return false;

                    Date dataConvertida = Date.from(t.getDataCadastro().atStartOfDay(ZoneId.systemDefault()).toInstant());

                    String dataTransacao = dateFormat.format(dataConvertida);

                    return dataTransacao.equals(dataFiltro);
                })
                .filter(t -> tipoFiltro == null || t.getTipoTransacao().equals(tipoFiltro))
                .filter(t -> categoriaFiltro == null || t.getCategoria().equals(categoriaFiltro))
                .collect(Collectors.toList());

        for (Transacao transacao : transacoesFiltradas) {
            Object[] row = {
                    dateFormat.format(Date.from(transacao.getDataCadastro().atStartOfDay(ZoneId.systemDefault()).toInstant())),
                    transacao.getDescricao(),
                    transacao.getCategoria().getNome(),
                    transacao.getTipoTransacao().toString(),
                    currencyFormat.format(transacao.getValor())
            };
            tableModel.addRow(row);
        }
    }

    private void abrirLancamentosPanel() {
        SwingUtilities.getWindowAncestor(this).dispose();
        SwingUtils.abrirNovoFrame("Lançamentos", new LancamentosPanel());
    }

    private void abrirCadastrarCategoriaPanel() {
        SwingUtilities.getWindowAncestor(this).dispose();
        SwingUtils.abrirNovoFrame("Gerenciar Categorias", new GerenciarCategoriasPanel());
    }

    private void abrirResumoFinanceiroPanel() {
        SwingUtilities.getWindowAncestor(this).dispose();
        SwingUtils.abrirNovoFrame("Resumo Financeiro", new ResumoFinanceiroPanel());
    }

    private void initMenus() {
        menuBar = new JMenuBar();
        menuOperacoes = new JMenu("Operações");
        menuHistorico = new JMenu("Resumo Financeiro");
        JMenu menuConta = new JMenu("Conta");

        menuLancamentos = new JMenuItem("Lançamentos");
        menuCadastrarCategoria = new JMenuItem("Gerenciar Categorias");
        menuResumoFinanceiro = new JMenuItem("Ver resumo");
        JMenuItem menuItemSair = new JMenuItem("Sair");

        menuLancamentos.addActionListener(e -> abrirLancamentosPanel());
        menuCadastrarCategoria.addActionListener(e -> abrirCadastrarCategoriaPanel());
        menuResumoFinanceiro.addActionListener(e -> abrirResumoFinanceiroPanel());

        menuItemSair.addActionListener(e -> {
            usuarioLogado = null;
            SwingUtilities.getWindowAncestor(this).dispose();
            SwingUtils.abrirNovoFrame("Login", new LoginPanel());
        });

        menuOperacoes.add(menuLancamentos);
        menuOperacoes.add(menuCadastrarCategoria);
        menuHistorico.add(menuResumoFinanceiro);
        menuConta.add(menuItemSair);

        menuBar.add(menuOperacoes);
        menuBar.add(menuHistorico);
        menuBar.add(menuConta);

        this.add(menuBar, BorderLayout.NORTH);
    }


    private JPanel initBoasVindasPanel() {
        JPanel boasVindasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("<html><b>Bem-vindo, " + usuarioLogado.getNome() + "! Aqui estão seus últimos lançamentos.</b></html>");
        label.setFont(new Font("Arial", Font.BOLD, 12));
        boasVindasPanel.add(label);

        return boasVindasPanel;
    }

    private void initFiltrosPanel(JPanel filterPanel) {
        filterPanel.add(new JLabel("Data:"));
        dataField = new JTextField(7);
        filterPanel.add(dataField);

        filterPanel.add(new JLabel("Tipo:"));
        tipoBox = new JComboBox<>();
        tipoBox.addItem(null);
        for (TipoTransacaoEnum tipo : TipoTransacaoEnum.values()) {
            tipoBox.addItem(tipo);
        }
        filterPanel.add(tipoBox);

        filterPanel.add(new JLabel("Categoria:"));
        categoriaBox = new JComboBox<>();
        categoriaBox.addItem(null);
        for (Categoria categoria : usuarioLogado.getCategoria()) {
            categoriaBox.addItem(categoria);
        }
        filterPanel.add(categoriaBox);

        JButton filterButton = new JButton("Pesquisar");
        filterButton.addActionListener(e -> carregarTransacoes());
        filterPanel.add(filterButton);
    }

    private JScrollPane initTabelaTransacoes() {
        String[] colunas = {"Data", "Descrição", "Categoria", "Tipo", "Valor"};
        tableModel = new DefaultTableModel(colunas, 0);
        transacoesTable = new JTable(tableModel);
        transacoesTable.setEnabled(false);

        return new JScrollPane(transacoesTable);
    }
}
