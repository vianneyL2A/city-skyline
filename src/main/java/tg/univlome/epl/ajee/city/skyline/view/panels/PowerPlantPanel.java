package tg.univlome.epl.ajee.city.skyline.view.panels;

import tg.univlome.epl.ajee.city.skyline.model.energy.*;
import tg.univlome.epl.ajee.city.skyline.model.simulation.GameEngine;
import tg.univlome.epl.ajee.city.skyline.view.styles.Colors;
import tg.univlome.epl.ajee.city.skyline.view.styles.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panneau de gestion des centrales électriques.
 */
public class PowerPlantPanel extends JPanel {

    private final GameEngine gameEngine;
    private final JPanel plantListPanel;
    private final JLabel totalProductionLabel;

    public PowerPlantPanel(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        setLayout(new BorderLayout(10, 10));
        setBackground(Colors.BACKGROUND);
        setBorder(Theme.BORDER_PADDING_LARGE);

        // En-tête
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = Theme.createTitleLabel("⚡ Centrales Électriques");
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Boutons de construction
        JPanel buildButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buildButtonsPanel.setOpaque(false);

        totalProductionLabel = Theme.createHeaderLabel("Production totale: 0 kWh");
        buildButtonsPanel.add(totalProductionLabel);

        JButton buildButton = Theme.createPrimaryButton("+ Construire");
        buildButton.addActionListener(e -> showBuildDialog());
        buildButtonsPanel.add(buildButton);

        headerPanel.add(buildButtonsPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Liste des centrales
        plantListPanel = new JPanel();
        plantListPanel.setLayout(new BoxLayout(plantListPanel, BoxLayout.Y_AXIS));
        plantListPanel.setBackground(Colors.BACKGROUND);

        JScrollPane scrollPane = new JScrollPane(plantListPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Colors.BACKGROUND);
        scrollPane.getViewport().setBackground(Colors.BACKGROUND);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        refresh();
    }

    /**
     * Affiche le dialogue de construction de centrale.
     */
    private void showBuildDialog() {
        String[] options = {
                "🏭 Charbon (1000€ - 500 kWh)",
                "☀️ Solaire (1500€ - 200 kWh)",
                "🌬️ Éolien (1200€ - 250 kWh)",
                "⚛️ Nucléaire (5000€ - 1000 kWh)",
                "💧 Hydraulique (3000€ - 400 kWh)"
        };

        String choice = (String) JOptionPane.showInputDialog(
                this,
                "Choisissez le type de centrale à construire:",
                "Construire une centrale",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (choice != null) {
            PowerPlant plant = null;
            if (choice.startsWith("🏭")) {
                plant = new CoalPlant();
            } else if (choice.startsWith("☀️")) {
                plant = new SolarPlant();
            } else if (choice.startsWith("🌬️")) {
                plant = new WindPlant();
            } else if (choice.startsWith("⚛️")) {
                plant = new NuclearPlant();
            } else if (choice.startsWith("💧")) {
                plant = new HydroPlant();
            }

            if (plant != null) {
                if (gameEngine.buildPowerPlant(plant)) {
                    JOptionPane.showMessageDialog(this,
                            "Centrale construite avec succès!",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                    refresh();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Pas assez d'argent pour construire cette centrale.",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Crée une carte pour une centrale.
     */
    private JPanel createPlantCard(PowerPlant plant) {
        JPanel card = Theme.createCardPanel();
        card.setLayout(new BorderLayout(10, 5));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Info principale
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setOpaque(false);

        JLabel nameLabel = Theme.createHeaderLabel(plant.getDescription());
        infoPanel.add(nameLabel);

        JLabel productionLabel = Theme.createBodyLabel(
                String.format("Production: ~%d kWh | Maintenance: %d€",
                        plant.calculateProduction(), plant.calculateMaintenance()));
        infoPanel.add(productionLabel);

        JLabel levelLabel = Theme.createBodyLabel("Niveau: " + plant.getLevel().getDisplayName());
        if (plant.getLevel().canUpgrade()) {
            levelLabel.setText(levelLabel.getText() +
                    String.format(" (Amélioration: %d€)", plant.calculateUpgradeCost()));
        }
        infoPanel.add(levelLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // Bouton améliorer
        if (plant.getLevel().canUpgrade()) {
            JButton upgradeButton = Theme.createSecondaryButton("⬆️ Améliorer");
            upgradeButton.addActionListener(e -> {
                if (gameEngine.upgradePowerPlant(plant)) {
                    refresh();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Pas assez d'argent!", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });
            card.add(upgradeButton, BorderLayout.EAST);
        }

        // Couleur selon le type
        Color typeColor = switch (plant.getEnergyType()) {
            case COAL -> Colors.ENERGY_COAL;
            case SOLAR -> Colors.ENERGY_SOLAR;
            case WIND -> Colors.ENERGY_WIND;
            case NUCLEAR -> Colors.ENERGY_NUCLEAR;
            case HYDRO -> Colors.ENERGY_HYDRO;
        };
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, typeColor),
                Theme.BORDER_PADDING_MEDIUM));

        return card;
    }

    /**
     * Rafraîchit la liste des centrales.
     */
    public void refresh() {
        plantListPanel.removeAll();

        // Récupérer les centrales depuis la carte
        java.util.List<PowerPlant> plants = new java.util.ArrayList<>();
        if (gameEngine.getCityMap() != null) {
            for (var cell : gameEngine.getCityMap().getPowerPlantCells()) {
                if (cell.getPowerPlant() != null) {
                    plants.add(cell.getPowerPlant());
                }
            }
        }

        if (plants.isEmpty()) {
            JLabel emptyLabel = Theme
                    .createBodyLabel("Aucune centrale construite. Cliquez sur 'Construire' pour commencer.");
            emptyLabel.setForeground(Colors.TEXT_SECONDARY);
            plantListPanel.add(emptyLabel);
        } else {
            int totalProduction = 0;
            for (PowerPlant plant : plants) {
                plantListPanel.add(createPlantCard(plant));
                plantListPanel.add(Box.createVerticalStrut(10));
                totalProduction += plant.calculateProduction();
            }
            totalProductionLabel.setText(String.format("Production totale: %,d kWh", totalProduction));
        }

        plantListPanel.revalidate();
        plantListPanel.repaint();
    }
}
