package com.mycompany.trabalho_poo.panels.lancamentos;

import com.mycompany.trabalho_poo.SistemaFinanceiro_POO;
import com.mycompany.trabalho_poo.dto.usuario.*;
import com.mycompany.trabalho_poo.panels.financeiro.SistemaFinanceiroPanel;
import com.mycompany.trabalho_poo.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static com.mycompany.trabalho_poo.SistemaFinanceiro_POO.usuarioLogado;

public class LancamentosPanel extends JPanel {
    private JTextField valorField;
    private JTextField descricaoField;
    private JComboBox<Categoria> categoriaBox;
    private JComboBox<TipoTransacaoEnum> tipoBox;
    private JSpinner dataSpinner;
    private JButton salvarButton, voltarButton;

    public LancamentosPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel valorLabel = new JLabel("Valor:");
        JLabel descricaoLabel = new JLabel("Descrição:");
        JLabel categoriaLabel = new JLabel("Categoria:");
        JLabel tipoLabel = new JLabel("Tipo:");
        JLabel dataLabel = new JLabel("Data:");

        valorField = new JTextField(15);
        valorField.setHorizontalAlignment(JTextField.LEFT);
        valorField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formatarValor();
            }
        });

        descricaoField = new JTextField(15);
        categoriaBox = new JComboBox<>(usuarioLogado.getCategoria().toArray(new Categoria[0]));
        tipoBox = new JComboBox<>(TipoTransacaoEnum.values());

        dataSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dataSpinner, "dd/MM/yyyy");
        dataSpinner.setEditor(editor);

        salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(e -> salvarTransacao());

        voltarButton = new JButton("Voltar");
        voltarButton.addActionListener(e -> voltarParaSistemaFinanceiro());

        gbc.gridx = 0; gbc.gridy = 0; add(valorLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; add(valorField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(descricaoLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; add(descricaoField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(categoriaLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; add(categoriaBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(tipoLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; add(tipoBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4; add(dataLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; add(dataSpinner, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(salvarButton);
        buttonPanel.add(voltarButton);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; add(buttonPanel, gbc);
    }

    private void formatarValor() {
        String text = valorField.getText().replaceAll("[^0-9]", "");
        if (text.isEmpty()) {
            valorField.setText("R$ 0,00");
            return;
        }
        double value = Double.parseDouble(text) / 100;
        valorField.setText("R$ " + new DecimalFormat("#,##0.00").format(value));
    }

    private void salvarTransacao() {
        try {
            String textValue = valorField.getText().replace("R$", "").replace(".", "").replace(",", ".").trim();
            BigDecimal valor = new BigDecimal(textValue);

            String descricao = descricaoField.getText();
            Categoria categoria = (Categoria) categoriaBox.getSelectedItem();
            TipoTransacaoEnum tipo = (TipoTransacaoEnum) tipoBox.getSelectedItem();
            Date selectedDate = (Date) dataSpinner.getValue();
            LocalDate dataCadastro = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (descricao.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Descrição não pode ser vazia!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Transacao novaTransacao = new Transacao(valor, categoria, tipo, dataCadastro, descricao);
            SistemaFinanceiro_POO.saveTransacao(novaTransacao, usuarioLogado);
            JOptionPane.showMessageDialog(this, "Transação cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.getWindowAncestor(this).dispose();
            SwingUtils.abrirNovoFrame("Sistema Financeiro", new SistemaFinanceiroPanel());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltarParaSistemaFinanceiro() {
        SwingUtilities.getWindowAncestor(this).dispose();
        SwingUtils.abrirNovoFrame("Sistema Financeiro", new SistemaFinanceiroPanel());
    }
}
