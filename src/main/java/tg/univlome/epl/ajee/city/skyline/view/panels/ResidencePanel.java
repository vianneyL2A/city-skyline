package tg.univlome.epl.ajee.city.skyline.view.panels;

import tg.univlome.epl.ajee.city.skyline.model.entities.Residence;
import tg.univlome.epl.ajee.city.skyline.model.map.MapCell;
import tg.univlome.epl.ajee.city.skyline.model.simulation.GameEngine;
import tg.univlome.epl.ajee.city.skyline.view.styles.Colors;
import tg.univlome.epl.ajee.city.skyline.view.styles.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panneau de gestion des résidences.
 */
public class ResidencePanel extends JPanel {

    private final GameEngine gameEngine;

    // Statistiques - labels stockés directement
    private JLabel totalResidencesLabel;
    private JLabel populationLabel;
    private JLabel occupancyLabel;
    private JLabel poweredLabel;

    // Tableau
    private final JTable residenceTable;
    private final ResidenceTableModel tableModel;

    public ResidencePanel(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        setLayout(new BorderLayout(10, 10));
        setBackground(Colors.BACKGROUND);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // === Statistiques en haut ===
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.NORTH);

        // === Tableau des résidences au centre ===
        tableModel = new ResidenceTableModel();
        residenceTable = new JTable(tableModel);
        configureTable();

        JScrollPane scrollPane = new JScrollPane(residenceTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.BORDER));
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Créer les labels et les stocker
        totalResidencesLabel = new JLabel("0");
        populationLabel = new JLabel("0 / 0");
        occupancyLabel = new JLabel("0%");
        poweredLabel = new JLabel("0 / 0");

        panel.add(createStatCard("🏠 Résidences", totalResidencesLabel, Colors.PRIMARY));
        panel.add(createStatCard("👥 Population", populationLabel, Colors.INFO));
        panel.add(createStatCard("📊 Occupation", occupancyLabel, Colors.SECONDARY));
        panel.add(createStatCard("⚡ Alimentées", poweredLabel, Colors.SUCCESS));

        return panel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Colors.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 2),
                new EmptyBorder(12, 15, 12, 15)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_SMALL);
        titleLabel.setForeground(Colors.TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(valueLabel);

        return card;
    }

    private void configureTable() {
        residenceTable.setFont(Theme.FONT_BODY);
        residenceTable.setRowHeight(35);
        residenceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        residenceTable.getTableHeader().setFont(Theme.FONT_HEADER);
        residenceTable.getTableHeader().setBackground(Colors.PRIMARY);
        residenceTable.getTableHeader().setForeground(Colors.TEXT_ON_PRIMARY);
        residenceTable.setGridColor(Colors.BORDER);

        // Renderer centré
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < residenceTable.getColumnCount(); i++) {
            residenceTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Colonne action avec bouton
        residenceTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        residenceTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Largeurs
        residenceTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        residenceTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        residenceTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        residenceTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        residenceTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        residenceTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        residenceTable.getColumnModel().getColumn(6).setPreferredWidth(100);
    }

    public void refresh() {
        List<MapCell> residenceCells = new ArrayList<>();
        if (gameEngine.getCityMap() != null) {
            residenceCells = gameEngine.getCityMap().getResidenceCells();
        }

        // Statistiques
        int total = residenceCells.size();
        int population = 0;
        int capacity = 0;
        int powered = 0;

        for (MapCell cell : residenceCells) {
            Residence r = cell.getResidence();
            if (r != null) {
                population += r.getInhabitantCount();
                capacity += r.getLevel().getMaxInhabitants();
                if (r.isEnergySupplied()) {
                    powered++;
                }
            }
        }

        totalResidencesLabel.setText(String.valueOf(total));
        populationLabel.setText(population + " / " + capacity);
        occupancyLabel.setText(capacity > 0 ? (population * 100 / capacity) + "%" : "0%");
        poweredLabel.setText(powered + " / " + total);

        // Mettre à jour le tableau
        tableModel.setResidences(residenceCells);
    }

    // === Table Model ===
    private class ResidenceTableModel extends AbstractTableModel {
        private final String[] columns = { "Nom", "Niveau", "Habitants", "Énergie", "Taxe", "Statut", "Action" };
        private List<MapCell> residences = new ArrayList<>();

        public void setResidences(List<MapCell> residences) {
            this.residences = residences;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return residences.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            MapCell cell = residences.get(rowIndex);
            Residence r = cell.getResidence();
            if (r == null)
                return "";

            return switch (columnIndex) {
                case 0 -> r.getName();
                case 1 -> r.getLevel().getDisplayName();
                case 2 -> r.getInhabitantCount() + "/" + r.getLevel().getMaxInhabitants();
                case 3 -> r.getEnergyNeed() + " kWh";
                case 4 -> r.calculateTax() + "€";
                case 5 -> r.isEnergySupplied() ? "⚡" : "❌";
                case 6 -> r.getLevel().canUpgrade() ? "Améliorer" : "Max";
                default -> "";
            };
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 6;
        }

        public Residence getResidenceAt(int row) {
            if (row >= 0 && row < residences.size()) {
                return residences.get(row).getResidence();
            }
            return null;
        }
    }

    // === Button Renderer (utilise JLabel avec paintComponent pour fiabilité) ===
    private class ButtonRenderer extends JLabel implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(Theme.FONT_BODY);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            String text = value != null ? value.toString() : "";
            setText(text);

            if ("Max".equals(text)) {
                setBackground(new Color(200, 200, 200));
                setForeground(Color.DARK_GRAY);
            } else {
                setBackground(new Color(34, 139, 34)); // Vert forêt
                setForeground(Color.WHITE);
            }
            return this;
        }
    }

    // === Button Editor ===
    private class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                Residence residence = tableModel.getResidenceAt(currentRow);
                if (residence != null && residence.getLevel().canUpgrade()) {
                    if (gameEngine.upgradeResidence(residence)) {
                        JOptionPane.showMessageDialog(ResidencePanel.this,
                                "✅ " + residence.getName() + " améliorée en " + residence.getLevel().getDisplayName(),
                                "Amélioration réussie",
                                JOptionPane.INFORMATION_MESSAGE);
                        refresh();
                    } else {
                        JOptionPane.showMessageDialog(ResidencePanel.this,
                                "❌ Fonds insuffisants pour améliorer cette résidence.",
                                "Amélioration impossible",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            String text = value != null ? value.toString() : "";
            button.setText(text);
            button.setBackground(Colors.PRIMARY);
            button.setForeground(Colors.TEXT_ON_PRIMARY);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }
}
