package com.mycompany.trabalho_poo.panels;

import com.mycompany.trabalho_poo.dto.usuario.TipoTransacaoEnum;
import com.mycompany.trabalho_poo.dto.usuario.Transacao;
import com.mycompany.trabalho_poo.panels.financeiro.SistemaFinanceiroPanel;
import com.mycompany.trabalho_poo.utils.SwingUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.mycompany.trabalho_poo.SistemaFinanceiro_POO.usuarioLogado;

public class ResumoFinanceiroPanel extends JPanel {
    private JSpinner dataInicioSpinner, dataFimSpinner;
    private JButton filtrarButton, voltarButton;
    private JTable resumoTable;
    private DefaultTableModel tableModel;

    public ResumoFinanceiroPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        voltarButton = new JButton("< Voltar");
        voltarButton.addActionListener(e -> voltarParaSistemaFinanceiro());
        topPanel.add(voltarButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("Data Início:"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dataInicioSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorInicio = new JSpinner.DateEditor(dataInicioSpinner, "dd/MM/yyyy");
        dataInicioSpinner.setEditor(editorInicio);
        filterPanel.add(dataInicioSpinner);

        filterPanel.add(new JLabel("Data Fim:"));
        dataFimSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorFim = new JSpinner.DateEditor(dataFimSpinner, "dd/MM/yyyy");
        dataFimSpinner.setEditor(editorFim);
        filterPanel.add(dataFimSpinner);

        filtrarButton = new JButton("Filtrar");
        filtrarButton.addActionListener(e -> atualizarResumoFinanceiro());
        filterPanel.add(filtrarButton);
        add(filterPanel, BorderLayout.CENTER);

        String[] colunas = {"Descrição", "Qtd Itens", "Valor"};
        tableModel = new DefaultTableModel(colunas, 0);
        resumoTable = new JTable(tableModel);
        resumoTable.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(resumoTable);
        add(scrollPane, BorderLayout.SOUTH);

        atualizarResumoFinanceiro();
    }

    private void atualizarResumoFinanceiro() {
        LocalDate inicio = converterParaLocalDate((Date) dataInicioSpinner.getValue());
        LocalDate fim = converterParaLocalDate((Date) dataFimSpinner.getValue());

        if (inicio.isAfter(fim)) {
            JOptionPane.showMessageDialog(this, "A data inicial não pode ser maior que a data final!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Transacao> transacoesFiltradas = usuarioLogado.getTransacao().stream()
                .filter(t -> !t.getDataCadastro().isBefore(inicio) && !t.getDataCadastro().isAfter(fim))
                .collect(Collectors.toList());

        int qtdItensReceita = (int) transacoesFiltradas.stream().filter(t -> t.getTipoTransacao() == TipoTransacaoEnum.RECEITA).count();
        int qtdItensDespesa = (int) transacoesFiltradas.stream().filter(t -> t.getTipoTransacao() == TipoTransacaoEnum.DESPESA).count();

        BigDecimal totalReceitas = transacoesFiltradas.stream()
                .filter(t -> t.getTipoTransacao() == TipoTransacaoEnum.RECEITA)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDespesas = transacoesFiltradas.stream()
                .filter(t -> t.getTipoTransacao() == TipoTransacaoEnum.DESPESA)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldoTotal = totalReceitas.subtract(totalDespesas);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{"Total Receitas", qtdItensReceita, currencyFormat.format(totalReceitas)});
        tableModel.addRow(new Object[]{"Total Despesas", qtdItensDespesa, currencyFormat.format(totalDespesas)});
        tableModel.addRow(new Object[]{"Saldo Total", qtdItensReceita + qtdItensDespesa, currencyFormat.format(saldoTotal)});
    }


    private LocalDate converterParaLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void voltarParaSistemaFinanceiro() {
        SwingUtilities.getWindowAncestor(this).dispose();
        SwingUtils.abrirNovoFrame("Sistema Financeiro", new SistemaFinanceiroPanel());
    }
}
