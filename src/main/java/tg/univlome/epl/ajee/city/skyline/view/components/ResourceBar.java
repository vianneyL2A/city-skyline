package tg.univlome.epl.ajee.city.skyline.view.components;

import tg.univlome.epl.ajee.city.skyline.view.styles.Colors;
import tg.univlome.epl.ajee.city.skyline.view.styles.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * Barre affichant les ressources du joueur.
 */
public class ResourceBar extends JPanel {

    private final JLabel moneyLabel;
    private final JLabel dateLabel;
    private final JLabel productionLabel;
    private final JLabel demandLabel;
    private final JLabel happinessLabel;
    private final JLabel inhabitantsLabel;

    public ResourceBar() {
        setLayout(new BorderLayout());
        setBackground(Colors.PRIMARY_DARK);
        setBorder(Theme.BORDER_PADDING_SMALL);

        // Panel gauche avec les infos principales
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        leftPanel.setOpaque(false);

        // Argent
        JPanel moneyPanel = createInfoPanel("Argent:", "10 000€");
        moneyLabel = (JLabel) moneyPanel.getComponent(1);
        leftPanel.add(moneyPanel);

        // Production
        JPanel productionPanel = createInfoPanel("Production:", "0 kWh");
        productionLabel = (JLabel) productionPanel.getComponent(1);
        leftPanel.add(productionPanel);

        // Demande
        JPanel demandPanel = createInfoPanel("Demande:", "0 kWh");
        demandLabel = (JLabel) demandPanel.getComponent(1);
        leftPanel.add(demandPanel);

        add(leftPanel, BorderLayout.WEST);

        // Panel droit avec date, habitants et satisfaction
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        rightPanel.setOpaque(false);

        // Date avec heure
        JPanel datePanel = createInfoPanel("", "Jour 1, Mois 1, Année 1 - 08:00:00");
        dateLabel = (JLabel) datePanel.getComponent(0);
        rightPanel.add(datePanel);

        // Séparateur visuel
        JLabel separator1 = new JLabel("|");
        separator1.setForeground(Colors.TEXT_ON_DARK);
        rightPanel.add(separator1);

        // Habitants
        JPanel inhabitantsPanel = createInfoPanel("Habitants:", "0");
        inhabitantsLabel = (JLabel) inhabitantsPanel.getComponent(1);
        rightPanel.add(inhabitantsPanel);

        // Séparateur visuel
        JLabel separator2 = new JLabel("|");
        separator2.setForeground(Colors.TEXT_ON_DARK);
        rightPanel.add(separator2);

        // Satisfaction des habitants
        JPanel happinessPanel = createHappinessPanel();
        happinessLabel = (JLabel) happinessPanel.getComponent(1);
        rightPanel.add(happinessPanel);

        add(rightPanel, BorderLayout.EAST);
    }

    private JPanel createInfoPanel(String label, String value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);

        if (!label.isEmpty()) {
            JLabel labelComp = new JLabel(label);
            labelComp.setFont(Theme.FONT_SMALL);
            labelComp.setForeground(Colors.TEXT_ON_DARK);
            panel.add(labelComp);
        }

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(Theme.FONT_HEADER);
        valueComp.setForeground(Colors.TEXT_ON_PRIMARY);
        panel.add(valueComp);

        return panel;
    }

    private JPanel createHappinessPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panel.setOpaque(false);

        JLabel iconLabel = new JLabel("Satisfaction:");
        iconLabel.setFont(Theme.FONT_SMALL);
        iconLabel.setForeground(Colors.TEXT_ON_DARK);
        panel.add(iconLabel);

        JLabel valueLabel = new JLabel("75%");
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLabel.setForeground(Colors.SUCCESS);
        panel.add(valueLabel);

        return panel;
    }

    public void setMoney(int money) {
        moneyLabel.setText(String.format("%,d€", money));
    }

    public void setDate(String date) {
        dateLabel.setText(date);
    }

    public void setProduction(int production) {
        productionLabel.setText(String.format("%,d kWh", production));
    }

    public void setDemand(int demand) {
        demandLabel.setText(String.format("%,d kWh", demand));
    }

    public void setInhabitants(int count) {
        inhabitantsLabel.setText(String.format("%,d", count));
    }

    public void setHappiness(int happiness) {
        happinessLabel.setText(happiness + "%");

        // Colorer selon le niveau de bonheur
        if (happiness >= 60) {
            happinessLabel.setForeground(Colors.SUCCESS);
        } else if (happiness >= 30) {
            happinessLabel.setForeground(Colors.WARNING);
        } else {
            happinessLabel.setForeground(Colors.ERROR);
        }
    }

    public void updateBalance(int production, int demand) {
        setProduction(production);
        setDemand(demand);

        // Colorer selon le surplus/déficit
        if (production >= demand) {
            productionLabel.setForeground(Colors.SUCCESS);
        } else {
            productionLabel.setForeground(Colors.ERROR);
        }
    }
}
