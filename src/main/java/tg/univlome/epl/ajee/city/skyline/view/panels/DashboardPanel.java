package tg.univlome.epl.ajee.city.skyline.view.panels;

import tg.univlome.epl.ajee.city.skyline.model.entities.City;
import tg.univlome.epl.ajee.city.skyline.model.entities.Player;
import tg.univlome.epl.ajee.city.skyline.model.simulation.GameEngine;
import tg.univlome.epl.ajee.city.skyline.view.components.HappinessGauge;
import tg.univlome.epl.ajee.city.skyline.view.styles.Colors;
import tg.univlome.epl.ajee.city.skyline.view.styles.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * Panneau tableau de bord principal.
 * Affiche un aperçu général de l'état du jeu.
 */
public class DashboardPanel extends JPanel {

    private final GameEngine gameEngine;
    private final HappinessGauge happinessGauge;
    private final JLabel cityNameLabel;
    private final JLabel residenceCountLabel;
    private final JLabel plantCountLabel;
    private final JLabel inhabitantCountLabel;
    private final JLabel productionLabel;
    private final JLabel demandLabel;
    private final JLabel balanceLabel;
    private final JLabel priceLabel;

    public DashboardPanel(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        setLayout(new BorderLayout(10, 10));
        setBackground(Colors.BACKGROUND);
        setBorder(Theme.BORDER_PADDING_LARGE);

        // Titre
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        cityNameLabel = Theme.createTitleLabel("🏙️ Ma Ville");
        headerPanel.add(cityNameLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Contenu principal
        JPanel contentPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        contentPanel.setOpaque(false);

        // Colonne gauche : Stats ville
        JPanel cityStatsPanel = createStatsCard("📊 Statistiques Ville");
        residenceCountLabel = addStatRow(cityStatsPanel, "Résidences:");
        plantCountLabel = addStatRow(cityStatsPanel, "Centrales:");
        inhabitantCountLabel = addStatRow(cityStatsPanel, "Habitants:");
        contentPanel.add(cityStatsPanel);

        // Colonne milieu : Énergie
        JPanel energyPanel = createStatsCard("⚡ Énergie");
        productionLabel = addStatRow(energyPanel, "Production:");
        demandLabel = addStatRow(energyPanel, "Demande:");
        balanceLabel = addStatRow(energyPanel, "Balance:");
        priceLabel = addStatRow(energyPanel, "Prix kWh:");
        contentPanel.add(energyPanel);

        // Colonne droite : Bonheur
        JPanel happinessPanel = createStatsCard("😊 Satisfaction");
        happinessGauge = new HappinessGauge();
        happinessPanel.add(happinessGauge);
        happinessPanel.add(Box.createVerticalGlue());
        contentPanel.add(happinessPanel);

        add(contentPanel, BorderLayout.CENTER);

        // Mise à jour initiale
        refresh();
    }

    private JPanel createStatsCard(String title) {
        JPanel panel = Theme.createCardPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = Theme.createSubtitleLabel(title);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(15));

        return panel;
    }

    private JLabel addStatRow(JPanel panel, String label) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        rowPanel.setOpaque(false);
        rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel labelComp = Theme.createBodyLabel(label);
        rowPanel.add(labelComp);

        JLabel valueComp = Theme.createHeaderLabel("-");
        rowPanel.add(valueComp);

        panel.add(rowPanel);
        return valueComp;
    }

    /**
     * Rafraîchit l'affichage avec les données actuelles.
     */
    public void refresh() {
        City city = gameEngine.getCity();
        Player player = gameEngine.getPlayer();

        cityNameLabel.setText("🏙️ " + city.getName());
        residenceCountLabel.setText(String.valueOf(city.getResidenceCount()));
        plantCountLabel.setText(String.valueOf(city.getPowerPlantCount()));
        inhabitantCountLabel.setText(String.valueOf(city.getTotalInhabitants()));

        int production = city.calculateTotalProduction();
        int demand = city.calculateTotalDemand();
        int balance = production - demand;

        productionLabel.setText(String.format("%,d kWh", production));
        demandLabel.setText(String.format("%,d kWh", demand));
        balanceLabel.setText(String.format("%+,d kWh", balance));
        balanceLabel.setForeground(balance >= 0 ? Colors.SUCCESS : Colors.ERROR);

        priceLabel.setText(gameEngine.getMarket().formatPrice());

        happinessGauge.setValue(city.getGlobalHappiness());
    }
}
