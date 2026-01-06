package tg.univlome.epl.ajee.city.skyline.view.panels;

import tg.univlome.epl.ajee.city.skyline.model.entities.Residence;
import tg.univlome.epl.ajee.city.skyline.model.entities.ResidenceLevel;
import tg.univlome.epl.ajee.city.skyline.model.energy.*;
import tg.univlome.epl.ajee.city.skyline.model.map.CityMap;
import tg.univlome.epl.ajee.city.skyline.model.map.MapCell;
import tg.univlome.epl.ajee.city.skyline.model.simulation.GameEngine;
import tg.univlome.epl.ajee.city.skyline.view.styles.Colors;
import tg.univlome.epl.ajee.city.skyline.view.styles.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Panneau affichant la carte de la ville en grille 2D.
 */
public class CityMapPanel extends JPanel {

    private static final int CELL_SIZE = 40;
    private static final int GRID_LINE_WIDTH = 1;

    private final GameEngine gameEngine;
    private final CityMap cityMap;
    private MapCell selectedCell;
    private BuildTool currentTool;

    // Panneau de détails
    private final JPanel detailsPanel;
    private final JLabel detailsTitle;
    private final JLabel detailsInfo;
    private final JButton actionButton;

    /**
     * Outils de construction disponibles.
     */
    public enum BuildTool {
        SELECT("Sélectionner", "🔍", null),
        RESIDENCE("Résidence", "🏠", null),
        COAL("Charbon", "🏭", EnergyType.COAL),
        SOLAR("Solaire", "☀️", EnergyType.SOLAR),
        WIND("Éolien", "🌬️", EnergyType.WIND),
        NUCLEAR("Nucléaire", "⚛️", EnergyType.NUCLEAR),
        HYDRO("Hydraulique", "💧", EnergyType.HYDRO),
        DEMOLISH("Démolir", "🗑️", null);

        private final String name;
        private final String icon;
        private final EnergyType energyType;

        BuildTool(String name, String icon, EnergyType energyType) {
            this.name = name;
            this.icon = icon;
            this.energyType = energyType;
        }

        public String getDisplayName() {
            return icon + " " + name;
        }

        public EnergyType getEnergyType() {
            return energyType;
        }
    }

    public CityMapPanel(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.cityMap = new CityMap();
        this.currentTool = BuildTool.SELECT;

        setLayout(new BorderLayout(10, 10));
        setBackground(Colors.BACKGROUND);
        setBorder(Theme.BORDER_PADDING_MEDIUM);

        // Panel principal avec carte et détails
        JPanel mainPanel = new JPanel(new BorderLayout(10, 0));
        mainPanel.setOpaque(false);

        // Zone de la carte (scrollable)
        JPanel mapContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMap((Graphics2D) g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(
                        cityMap.getWidth() * CELL_SIZE + 1,
                        cityMap.getHeight() * CELL_SIZE + 1);
            }
        };
        mapContainer.setBackground(Colors.SURFACE);
        mapContainer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMapClick(e.getX(), e.getY());
            }
        });
        mapContainer.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX() / CELL_SIZE;
                int y = e.getY() / CELL_SIZE;
                MapCell cell = cityMap.getCell(x, y);
                if (cell != null) {
                    mapContainer.setToolTipText(getCellTooltip(cell));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(mapContainer);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.BORDER));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panneau de détails (droite)
        detailsPanel = Theme.createCardPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setPreferredSize(new Dimension(200, 0));

        detailsTitle = Theme.createSubtitleLabel("Sélection");
        detailsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(detailsTitle);
        detailsPanel.add(Box.createVerticalStrut(10));

        detailsInfo = Theme.createBodyLabel("Cliquez sur une cellule");
        detailsInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(detailsInfo);
        detailsPanel.add(Box.createVerticalStrut(10));

        actionButton = Theme.createPrimaryButton("Action");
        actionButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionButton.setVisible(false);
        detailsPanel.add(actionButton);

        detailsPanel.add(Box.createVerticalGlue());

        // Statistiques de la carte
        JLabel statsLabel = Theme.createHeaderLabel("📊 Statistiques");
        statsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(statsLabel);
        detailsPanel.add(Box.createVerticalStrut(5));

        mainPanel.add(detailsPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);

        // Barre d'outils
        add(createToolbar(), BorderLayout.SOUTH);
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        toolbar.setBackground(Colors.SURFACE);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Colors.BORDER),
                Theme.BORDER_PADDING_SMALL));

        ButtonGroup toolGroup = new ButtonGroup();

        for (BuildTool tool : BuildTool.values()) {
            JToggleButton btn = new JToggleButton(tool.getDisplayName());
            btn.setFont(Theme.FONT_BODY);
            btn.setFocusPainted(false);
            btn.addActionListener(e -> {
                currentTool = tool;
                updateCursor();
            });

            if (tool == BuildTool.SELECT) {
                btn.setSelected(true);
            }

            toolGroup.add(btn);
            toolbar.add(btn);
        }

        return toolbar;
    }

    private void updateCursor() {
        if (currentTool == BuildTool.DEMOLISH) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else if (currentTool != BuildTool.SELECT) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void drawMap(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Dessiner les cellules
        for (int x = 0; x < cityMap.getWidth(); x++) {
            for (int y = 0; y < cityMap.getHeight(); y++) {
                drawCell(g2d, cityMap.getCell(x, y));
            }
        }

        // Dessiner les zones de couverture des centrales
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
        for (MapCell cell : cityMap.getPowerPlantCells()) {
            int centerX = cell.getX() * CELL_SIZE + CELL_SIZE / 2;
            int centerY = cell.getY() * CELL_SIZE + CELL_SIZE / 2;
            int radius = CityMap.POWER_PLANT_COVERAGE_RADIUS * CELL_SIZE;

            g2d.setColor(Colors.PRIMARY);
            g2d.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        // Dessiner la sélection
        if (selectedCell != null) {
            int px = selectedCell.getX() * CELL_SIZE;
            int py = selectedCell.getY() * CELL_SIZE;
            g2d.setColor(Colors.PRIMARY);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(px + 1, py + 1, CELL_SIZE - 2, CELL_SIZE - 2);
        }

        // Dessiner la grille
        g2d.setColor(Colors.BORDER);
        g2d.setStroke(new BasicStroke(GRID_LINE_WIDTH));
        for (int x = 0; x <= cityMap.getWidth(); x++) {
            g2d.drawLine(x * CELL_SIZE, 0, x * CELL_SIZE, cityMap.getHeight() * CELL_SIZE);
        }
        for (int y = 0; y <= cityMap.getHeight(); y++) {
            g2d.drawLine(0, y * CELL_SIZE, cityMap.getWidth() * CELL_SIZE, y * CELL_SIZE);
        }
    }

    private void drawCell(Graphics2D g2d, MapCell cell) {
        int px = cell.getX() * CELL_SIZE;
        int py = cell.getY() * CELL_SIZE;

        // Couleur de fond
        Color bgColor = switch (cell.getType()) {
            case EMPTY -> new Color(200, 230, 200); // Vert clair
            case RESIDENCE -> cell.isPowered() ? Colors.INFO : Colors.ERROR;
            case POWER_PLANT -> getPlantColor(cell.getPowerPlant());
        };

        g2d.setColor(bgColor);
        g2d.fillRect(px + 1, py + 1, CELL_SIZE - 1, CELL_SIZE - 1);

        // Icône
        if (!cell.isEmpty()) {
            g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            g2d.setColor(Colors.TEXT_ON_PRIMARY);
            String icon = getCellIcon(cell);
            FontMetrics fm = g2d.getFontMetrics();
            int textX = px + (CELL_SIZE - fm.stringWidth(icon)) / 2;
            int textY = py + (CELL_SIZE + fm.getAscent()) / 2 - 2;
            g2d.drawString(icon, textX, textY);
        }
    }

    private Color getPlantColor(PowerPlant plant) {
        if (plant == null)
            return Colors.SURFACE;
        return switch (plant.getEnergyType()) {
            case COAL -> Colors.ENERGY_COAL;
            case SOLAR -> Colors.ENERGY_SOLAR;
            case WIND -> Colors.ENERGY_WIND;
            case NUCLEAR -> Colors.ENERGY_NUCLEAR;
            case HYDRO -> Colors.ENERGY_HYDRO;
        };
    }

    private String getCellIcon(MapCell cell) {
        return switch (cell.getType()) {
            case EMPTY -> "";
            case RESIDENCE -> "🏠";
            case POWER_PLANT -> {
                if (cell.getPowerPlant() != null) {
                    yield cell.getPowerPlant().getEnergyType().getIcon();
                }
                yield "⚡";
            }
        };
    }

    private String getCellTooltip(MapCell cell) {
        return switch (cell.getType()) {
            case EMPTY -> "Terrain vide - Cliquez pour construire";
            case RESIDENCE -> {
                Residence r = cell.getResidence();
                yield String.format("Résidence %s - %s",
                        r != null ? r.getLevel().getDisplayName() : "",
                        cell.isPowered() ? "✅ Alimentée" : "❌ Sans électricité");
            }
            case POWER_PLANT -> {
                PowerPlant p = cell.getPowerPlant();
                yield String.format("Centrale %s - Production: %d kWh",
                        p != null ? p.getEnergyType().getDisplayName() : "",
                        p != null ? p.calculateProduction() : 0);
            }
        };
    }

    private void handleMapClick(int mouseX, int mouseY) {
        int x = mouseX / CELL_SIZE;
        int y = mouseY / CELL_SIZE;
        MapCell cell = cityMap.getCell(x, y);

        if (cell == null)
            return;

        switch (currentTool) {
            case SELECT -> selectCell(cell);
            case RESIDENCE -> buildResidence(cell);
            case DEMOLISH -> demolish(cell);
            default -> {
                if (currentTool.getEnergyType() != null) {
                    buildPowerPlant(cell, currentTool.getEnergyType());
                }
            }
        }

        repaint();
    }

    private void selectCell(MapCell cell) {
        selectedCell = cell;
        updateDetails(cell);
    }

    private void buildResidence(MapCell cell) {
        if (!cell.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cette cellule n'est pas vide!",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Residence residence = new Residence(ResidenceLevel.BASIC);

        // Ajouter des habitants initiaux (entre 1 et 3)
        int initialInhabitants = 1 + (int) (Math.random() * 3);
        for (int i = 0; i < initialInhabitants; i++) {
            residence.addNewInhabitant();
        }

        if (cityMap.placeResidence(cell.getX(), cell.getY(), residence)) {
            gameEngine.getCity().addResidence(residence);
            JOptionPane.showMessageDialog(this,
                    String.format("Résidence construite avec %d habitants!", initialInhabitants),
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buildPowerPlant(MapCell cell, EnergyType type) {
        if (!cell.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cette cellule n'est pas vide!",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PowerPlant plant = switch (type) {
            case COAL -> new CoalPlant();
            case SOLAR -> new SolarPlant();
            case WIND -> new WindPlant();
            case NUCLEAR -> new NuclearPlant();
            case HYDRO -> new HydroPlant();
        };

        if (gameEngine.buildPowerPlant(plant)) {
            cityMap.placePowerPlant(cell.getX(), cell.getY(), plant);
            JOptionPane.showMessageDialog(this,
                    plant.getEnergyType().getIcon() + " Centrale construite!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Pas assez d'argent!",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void demolish(MapCell cell) {
        if (cell.isEmpty())
            return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous démolir ce bâtiment?", "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (cell.isResidence() && cell.getResidence() != null) {
                gameEngine.getCity().getResidences().remove(cell.getResidence());
            } else if (cell.isPowerPlant() && cell.getPowerPlant() != null) {
                gameEngine.getCity().getPowerPlants().remove(cell.getPowerPlant());
            }
            cityMap.clearCell(cell.getX(), cell.getY());
            selectedCell = null;
        }
    }

    private void updateDetails(MapCell cell) {
        if (cell == null) {
            detailsTitle.setText("Sélection");
            detailsInfo.setText("Cliquez sur une cellule");
            actionButton.setVisible(false);
            return;
        }

        switch (cell.getType()) {
            case EMPTY -> {
                detailsTitle.setText("🌿 Terrain vide");
                detailsInfo.setText("<html>Position: " + cell.getX() + ", " + cell.getY() +
                        "<br>Sélectionnez un outil<br>pour construire.</html>");
                actionButton.setVisible(false);
            }
            case RESIDENCE -> {
                Residence r = cell.getResidence();
                detailsTitle.setText("🏠 Résidence");
                detailsInfo.setText(
                        String.format("<html>Niveau: %s<br>Habitants: %d/%d<br>Énergie: %s<br>Besoin: %d kWh</html>",
                                r.getLevel().getDisplayName(),
                                r.getInhabitantCount(), r.getLevel().getMaxInhabitants(),
                                cell.isPowered() ? "✅ Oui" : "❌ Non",
                                r.getEnergyNeed()));
                actionButton.setText("⬆️ Améliorer");
                actionButton.setVisible(true);
            }
            case POWER_PLANT -> {
                PowerPlant p = cell.getPowerPlant();
                detailsTitle.setText(p.getEnergyType().getIcon() + " " + p.getEnergyType().getDisplayName());
                detailsInfo.setText(String.format(
                        "<html>Niveau: %s<br>Production: %d kWh<br>Maintenance: %d€<br>Couverture: %d cases</html>",
                        p.getLevel().getDisplayName(),
                        p.calculateProduction(),
                        p.calculateMaintenance(),
                        CityMap.POWER_PLANT_COVERAGE_RADIUS));
                if (p.getLevel().canUpgrade()) {
                    actionButton.setText("⬆️ Améliorer (" + p.calculateUpgradeCost() + "€)");
                    actionButton.setVisible(true);
                } else {
                    actionButton.setVisible(false);
                }
            }
        }
    }

    public void refresh() {
        cityMap.updatePowerGrid();
        repaint();
    }

    public CityMap getCityMap() {
        return cityMap;
    }
}
