package tg.univlome.epl.ajee.city.skyline.view.panels;

import tg.univlome.epl.ajee.city.skyline.model.entities.Residence;
import tg.univlome.epl.ajee.city.skyline.model.entities.ResidenceLevel;
import tg.univlome.epl.ajee.city.skyline.model.energy.*;
import tg.univlome.epl.ajee.city.skyline.model.map.CityMap;
import tg.univlome.epl.ajee.city.skyline.model.map.MapCell;
import tg.univlome.epl.ajee.city.skyline.model.simulation.GameEngine;
import tg.univlome.epl.ajee.city.skyline.view.styles.Colors;
import tg.univlome.epl.ajee.city.skyline.view.styles.Theme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Panneau affichant la carte de la ville en grille 2D.
 */
public class CityMapPanel extends JPanel {

    private static final int CELL_SIZE = 40;
    private static final int GRID_LINE_WIDTH = 1;

    // Tuiles de terrain
    private BufferedImage groundTile;
    private BufferedImage waterTile;

    private final GameEngine gameEngine;
    private final CityMap cityMap;
    private MapCell selectedCell;
    private BuildTool currentTool;

    // Pour le mode raccordement électrique
    private MapCell powerLineStart;

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
        POWER_LINE("Raccordement", "🔌", null),
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

        // Charger les tuiles de terrain
        loadTiles();

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

    /**
     * Charge les images de tuiles pour le terrain et l'eau.
     */
    private void loadTiles() {
        try {
            groundTile = ImageIO
                    .read(getClass().getResourceAsStream("/tg/univlome/epl/ajee/city/skyline/view/tuiles/ground.png"));
            waterTile = ImageIO
                    .read(getClass().getResourceAsStream("/tg/univlome/epl/ajee/city/skyline/view/tuiles/water.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Erreur lors du chargement des tuiles: " + e.getMessage());
            // Fallback: créer des images de couleur unie
            groundTile = new BufferedImage(CELL_SIZE, CELL_SIZE, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = groundTile.createGraphics();
            g.setColor(new Color(140, 190, 110));
            g.fillRect(0, 0, CELL_SIZE, CELL_SIZE);
            g.dispose();

            waterTile = new BufferedImage(CELL_SIZE, CELL_SIZE, BufferedImage.TYPE_INT_RGB);
            g = waterTile.createGraphics();
            g.setColor(new Color(65, 135, 215));
            g.fillRect(0, 0, CELL_SIZE, CELL_SIZE);
            g.dispose();
        }
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

        // Dessiner la tuile de fond (terrain ou eau) pour toutes les cellules
        BufferedImage baseTile = cell.isWater() ? waterTile : groundTile;
        if (baseTile != null) {
            g2d.drawImage(baseTile, px, py, CELL_SIZE, CELL_SIZE, null);
        }

        // Pour les cellules non vides, dessiner une superposition semi-transparente
        if (!cell.isEmpty()) {
            Color overlayColor = switch (cell.getType()) {
                case EMPTY -> null;
                case RESIDENCE -> cell.isPowered() ? new Color(33, 150, 243, 180) : new Color(244, 67, 54, 180);
                case POWER_PLANT -> withAlpha(getPlantColor(cell.getPowerPlant()), 200);
                case POWER_LINE -> new Color(255, 200, 100, 180);
            };

            if (overlayColor != null) {
                g2d.setColor(overlayColor);
                g2d.fillRoundRect(px + 3, py + 3, CELL_SIZE - 6, CELL_SIZE - 6, 8, 8);
            }

            // Icône
            g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            g2d.setColor(Colors.TEXT_ON_PRIMARY);
            String icon = getCellIcon(cell);
            FontMetrics fm = g2d.getFontMetrics();
            int textX = px + (CELL_SIZE - fm.stringWidth(icon)) / 2;
            int textY = py + (CELL_SIZE + fm.getAscent()) / 2 - 2;
            g2d.drawString(icon, textX, textY);
        }
    }

    /**
     * Retourne une couleur avec une transparence alpha spécifique.
     */
    private Color withAlpha(Color color, int alpha) {
        if (color == null)
            return new Color(128, 128, 128, alpha);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
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
            case EMPTY -> cell.isWater() ? "🌊" : (cell.hasPowerLine() ? "─" : "");
            case RESIDENCE -> "🏠";
            case POWER_PLANT -> {
                if (cell.getPowerPlant() != null) {
                    yield cell.getPowerPlant().getEnergyType().getIcon();
                }
                yield "⚡";
            }
            case POWER_LINE -> "─";
        };
    }

    private String getCellTooltip(MapCell cell) {
        return switch (cell.getType()) {
            case EMPTY -> cell.isWater() ? "Cours d'eau - Non constructible" : "Terrain vide - Cliquez pour construire";
            case RESIDENCE -> {
                Residence r = cell.getResidence();
                String powerInfo = cell.isPowered()
                        ? (cell.getPowerLevel() == 0 ? "✅ Raccordée directement"
                                : "✅ Alimentée par propagation (niveau " + cell.getPowerLevel() + ")")
                        : "❌ Sans électricité";
                yield String.format("Résidence %s - %s",
                        r != null ? r.getLevel().getDisplayName() : "",
                        powerInfo);
            }
            case POWER_PLANT -> {
                PowerPlant p = cell.getPowerPlant();
                yield String.format("Centrale %s - Production: %d kWh",
                        p != null ? p.getEnergyType().getDisplayName() : "",
                        p != null ? p.calculateProduction() : 0);
            }
            case POWER_LINE -> "Ligne électrique";
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
            case POWER_LINE -> buildPowerLine(cell);
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

        if (cell.isWater()) {
            JOptionPane.showMessageDialog(this, "Impossible de construire sur l'eau!",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérification spéciale pour les centrales hydrauliques
        if (type == EnergyType.HYDRO && !cityMap.isAdjacentToWater(cell.getX(), cell.getY())) {
            JOptionPane.showMessageDialog(this,
                    "💧 Les centrales hydrauliques doivent être construites\nà côté d'un cours d'eau!",
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
            if (cityMap.placePowerPlant(cell.getX(), cell.getY(), plant)) {
                JOptionPane.showMessageDialog(this,
                        plant.getEnergyType().getIcon() + " Centrale construite!",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Le placement a échoué - ne devrait pas arriver si les vérifications sont
                // correctes
                JOptionPane.showMessageDialog(this, "Impossible de placer la centrale ici!",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pas assez d'argent!",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gère le mode de création de ligne électrique.
     * 1er clic : sélectionner une centrale
     * 2ème clic : sélectionner une résidence non alimentée
     */
    private void buildPowerLine(MapCell cell) {
        // Si pas de point de départ sélectionné
        if (powerLineStart == null) {
            // Vérifier que c'est une centrale opérationnelle
            if (!cell.isPowerPlant()) {
                JOptionPane.showMessageDialog(this,
                        "🔌 Étape 1: Cliquez d'abord sur une centrale électrique",
                        "Raccordement", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (cell.getPowerPlant() == null || !cell.getPowerPlant().isOperational()) {
                JOptionPane.showMessageDialog(this,
                        "⚠️ Cette centrale n'est pas opérationnelle!",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Sauvegarder le point de départ
            powerLineStart = cell;
            selectedCell = cell;
            updateDetails(cell);
            JOptionPane.showMessageDialog(this,
                    "✅ Centrale sélectionnée!\n\n🔌 Étape 2: Cliquez maintenant sur une résidence\nnon alimentée pour créer le raccordement.",
                    "Raccordement", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Deuxième clic - doit être une résidence
            if (!cell.isResidence()) {
                JOptionPane.showMessageDialog(this,
                        "❌ Cliquez sur une résidence pour créer le raccordement.\n\n(Clic droit pour annuler)",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Vérifier si la résidence est déjà alimentée
            if (cell.isPowered()) {
                JOptionPane.showMessageDialog(this,
                        "✅ Cette résidence est déjà alimentée!",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
                powerLineStart = null;
                return;
            }

            // Créer la ligne électrique automatiquement
            var electricityGrid = cityMap.getElectricityGrid();
            var powerLine = electricityGrid.createAutoLine(
                    powerLineStart.getX(), powerLineStart.getY(),
                    cell.getX(), cell.getY());

            if (powerLine != null && electricityGrid.addPowerLine(powerLine)) {
                JOptionPane.showMessageDialog(this,
                        "🔌 Raccordement créé avec succès!\n\nLa résidence est maintenant alimentée.",
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "❌ Impossible de créer le raccordement.\n\n" +
                                "Vérifiez qu'il n'y a pas d'obstacles (cours d'eau)\nentre la centrale et la résidence.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }

            // Réinitialiser
            powerLineStart = null;
            selectedCell = null;
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
                if (cell.isWater()) {
                    detailsTitle.setText("🌊 Cours d'eau");
                    detailsInfo.setText("<html>Position: " + cell.getX() + ", " + cell.getY() +
                            "<br>Non constructible.<br>Bloque l'électricité.</html>");
                } else {
                    detailsTitle.setText("🌿 Terrain vide");
                    detailsInfo.setText("<html>Position: " + cell.getX() + ", " + cell.getY() +
                            "<br>Sélectionnez un outil<br>pour construire.</html>");
                }
                actionButton.setVisible(false);
            }
            case RESIDENCE -> {
                Residence r = cell.getResidence();
                detailsTitle.setText("🏠 Résidence");
                String powerStatus = cell.isPowered()
                        ? (cell.getPowerLevel() == 0 ? "✅ Raccordée"
                                : "✅ Propagation (niv." + cell.getPowerLevel() + ")")
                        : "❌ Non";
                detailsInfo.setText(
                        String.format("<html>Niveau: %s<br>Habitants: %d/%d<br>Énergie: %s<br>Besoin: %d kWh</html>",
                                r.getLevel().getDisplayName(),
                                r.getInhabitantCount(), r.getLevel().getMaxInhabitants(),
                                powerStatus,
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
            case POWER_LINE -> {
                detailsTitle.setText("─ Ligne électrique");
                detailsInfo.setText("<html>Position: " + cell.getX() + ", " + cell.getY() +
                        "<br>Transporte l'électricité<br>vers les maisons.</html>");
                actionButton.setVisible(false);
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
