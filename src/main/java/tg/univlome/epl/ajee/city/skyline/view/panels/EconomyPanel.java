package tg.univlome.epl.ajee.city.skyline.view.panels;

import tg.univlome.epl.ajee.city.skyline.model.economy.Market;
import tg.univlome.epl.ajee.city.skyline.model.economy.Transaction;
import tg.univlome.epl.ajee.city.skyline.model.entities.City;
import tg.univlome.epl.ajee.city.skyline.model.entities.Player;
import tg.univlome.epl.ajee.city.skyline.model.simulation.GameEngine;
import tg.univlome.epl.ajee.city.skyline.view.styles.Colors;
import tg.univlome.epl.ajee.city.skyline.view.styles.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panneau Économie affichant les finances et transactions.
 */
public class EconomyPanel extends JPanel {

    private final GameEngine gameEngine;

    // Labels financiers
    private final JLabel moneyLabel;
    private final JLabel revenueLabel;
    private final JLabel taxLabel;
    private final JLabel maintenanceLabel;
    private final JLabel netIncomeLabel;

    // Table des transactions
    private final DefaultTableModel transactionModel;
    private final JTable transactionTable;

    // Indicateur de tendance
    private final JLabel trendLabel;

    // Variables de suivi
    private int lastMoney = 0;

    public EconomyPanel(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        setLayout(new BorderLayout(10, 10));
        setBackground(Colors.BACKGROUND);
        setBorder(Theme.BORDER_PADDING_LARGE);

        // === Titre ===
        JLabel titleLabel = Theme.createTitleLabel("💰 Économie & Finances");
        add(titleLabel, BorderLayout.NORTH);

        // === Contenu principal ===
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setOpaque(false);

        // --- Panneau gauche : Résumé financier ---
        JPanel financePanel = createFinancePanel();

        moneyLabel = createValueLabel(financePanel, "💵 Solde actuel:", "0 €");
        revenueLabel = createValueLabel(financePanel, "⚡ Revenus électricité:", "+0 €");
        taxLabel = createValueLabel(financePanel, "🏛️ Taxes collectées:", "+0 €");
        maintenanceLabel = createValueLabel(financePanel, "🔧 Maintenance:", "-0 €");

        // Séparateur
        financePanel.add(Box.createVerticalStrut(10));
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        financePanel.add(separator);
        financePanel.add(Box.createVerticalStrut(10));

        netIncomeLabel = createValueLabel(financePanel, "📈 Revenu net/cycle:", "+0 €");

        financePanel.add(Box.createVerticalStrut(15));

        // Tendance
        JPanel trendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        trendPanel.setOpaque(false);
        trendPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel trendTitle = Theme.createBodyLabel("Tendance:");
        trendLabel = Theme.createHeaderLabel("📊 Stable");
        trendPanel.add(trendTitle);
        trendPanel.add(trendLabel);
        financePanel.add(trendPanel);

        financePanel.add(Box.createVerticalGlue());

        contentPanel.add(financePanel, BorderLayout.WEST);

        // --- Panneau droit : Transactions récentes ---
        JPanel transactionsPanel = Theme.createCardPanel();
        transactionsPanel.setLayout(new BorderLayout(5, 10));
        transactionsPanel.setPreferredSize(new Dimension(500, 0));

        JLabel transactionsTitle = Theme.createSubtitleLabel("📜 Transactions Récentes");
        transactionsPanel.add(transactionsTitle, BorderLayout.NORTH);

        // Table des transactions
        String[] columns = { "Jour", "Type", "Montant", "Description" };
        transactionModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(transactionModel);
        transactionTable.setFont(Theme.FONT_BODY);
        transactionTable.setRowHeight(28);
        transactionTable.getTableHeader().setFont(Theme.FONT_SMALL);
        transactionTable.setSelectionBackground(Colors.PRIMARY);
        transactionTable.setSelectionForeground(Colors.TEXT_ON_PRIMARY);
        transactionTable.setGridColor(Colors.BORDER);

        // Rendu personnalisé pour les montants
        transactionTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String text = value.toString();
                if (text.startsWith("+")) {
                    c.setForeground(isSelected ? Colors.TEXT_ON_PRIMARY : Colors.SUCCESS);
                } else if (text.startsWith("-")) {
                    c.setForeground(isSelected ? Colors.TEXT_ON_PRIMARY : Colors.ERROR);
                } else {
                    c.setForeground(isSelected ? Colors.TEXT_ON_PRIMARY : Colors.TEXT_PRIMARY);
                }
                setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });

        // Largeur des colonnes
        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(70);
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.BORDER));
        transactionsPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(transactionsPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        // === Panneau inférieur : Statistiques ===
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.SOUTH);

        // Mise à jour initiale
        refresh();
    }

    private JPanel createFinancePanel() {
        JPanel panel = Theme.createCardPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(280, 0));

        JLabel title = Theme.createSubtitleLabel("📊 Bilan Financier");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(15));

        return panel;
    }

    private JLabel createValueLabel(JPanel parent, String labelText, String initialValue) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel label = Theme.createBodyLabel(labelText);
        row.add(label, BorderLayout.WEST);

        JLabel value = Theme.createHeaderLabel(initialValue);
        value.setHorizontalAlignment(SwingConstants.RIGHT);
        row.add(value, BorderLayout.EAST);

        parent.add(row);
        parent.add(Box.createVerticalStrut(8));

        return value;
    }

    private JPanel createStatsPanel() {
        JPanel panel = Theme.createCardPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));

        // Stats rapides
        panel.add(createQuickStat("💵", "Revenu total", gameEngine.getMarket().getTotalRevenue() + " €"));
        panel.add(createQuickStat("⚡", "Énergie vendue", gameEngine.getMarket().getTotalEnergySold() + " kWh"));
        panel.add(createQuickStat("📅", "Jours survécus", gameEngine.getPlayer().getDaysSurvived() + ""));

        return panel;
    }

    private JPanel createQuickStat(String icon, String label, String value) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = Theme.createHeaderLabel(value);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelComp = Theme.createBodyLabel(label);
        labelComp.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(iconLabel);
        panel.add(valueLabel);
        panel.add(labelComp);

        return panel;
    }

    /**
     * Rafraîchit l'affichage avec les données actuelles.
     */
    public void refresh() {
        Player player = gameEngine.getPlayer();
        City city = gameEngine.getCity();
        Market market = gameEngine.getMarket();

        // Mise à jour des labels financiers
        int currentMoney = player.getMoney();
        moneyLabel.setText(String.format("%,d €", currentMoney));
        moneyLabel.setForeground(currentMoney >= 0 ? Colors.SUCCESS : Colors.ERROR);

        // Calculer les revenus du cycle depuis les résidences de la carte
        int energyRevenue = 0;
        int taxes = 0;
        if (gameEngine.getCityMap() != null) {
            for (var cell : gameEngine.getCityMap().getResidenceCells()) {
                var residence = cell.getResidence();
                if (residence != null) {
                    if (residence.isEnergySupplied()) {
                        energyRevenue += residence.calculateEnergyPayment(market.getCurrentPrice());
                    }
                    taxes += residence.calculateTax();
                }
            }
        }
        revenueLabel.setText(String.format("+%,d €", energyRevenue));
        revenueLabel.setForeground(Colors.SUCCESS);

        // Taxes
        taxLabel.setText(String.format("+%,d €", taxes));
        taxLabel.setForeground(taxes > 0 ? Colors.SUCCESS : Colors.TEXT_SECONDARY);

        // Maintenance
        int maintenance = city.calculateTotalMaintenance();
        maintenanceLabel.setText(String.format("-%,d €", maintenance));
        maintenanceLabel.setForeground(Colors.ERROR);

        // Revenu net
        int netIncome = energyRevenue + taxes - maintenance;
        netIncomeLabel.setText(String.format("%+,d €", netIncome));
        netIncomeLabel.setForeground(netIncome >= 0 ? Colors.SUCCESS : Colors.ERROR);

        // Tendance
        updateTrend(currentMoney);
        lastMoney = currentMoney;

        // Mise à jour des transactions
        updateTransactionTable();
    }

    private void updateTrend(int currentMoney) {
        int diff = currentMoney - lastMoney;
        if (diff > 100) {
            trendLabel.setText("📈 En hausse (+)");
            trendLabel.setForeground(Colors.SUCCESS);
        } else if (diff < -100) {
            trendLabel.setText("📉 En baisse (-)");
            trendLabel.setForeground(Colors.ERROR);
        } else {
            trendLabel.setText("📊 Stable");
            trendLabel.setForeground(Colors.TEXT_SECONDARY);
        }
    }

    private void updateTransactionTable() {
        // Vider la table
        transactionModel.setRowCount(0);

        // Ajouter les transactions récentes (max 15)
        List<Transaction> transactions = gameEngine.getMarket().getRecentTransactions(15);

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            String type = t.getType() == Transaction.Type.INCOME ? "💵 Revenu" : "💸 Dépense";
            String amount = t.getType() == Transaction.Type.INCOME
                    ? String.format("+%,d €", t.getAmount())
                    : String.format("-%,d €", t.getAmount());

            transactionModel.addRow(new Object[] {
                    "Jour " + t.getDayInGame(),
                    type,
                    amount,
                    t.getDescription()
            });
        }
    }
}
