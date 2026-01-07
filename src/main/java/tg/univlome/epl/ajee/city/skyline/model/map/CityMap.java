package tg.univlome.epl.ajee.city.skyline.model.map;

import tg.univlome.epl.ajee.city.skyline.model.entities.Residence;
import tg.univlome.epl.ajee.city.skyline.model.energy.PowerPlant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Représente la carte de la ville sous forme de grille 2D.
 * Gère les terrains (constructible/cours d'eau) et le réseau électrique.
 */
public class CityMap {

    public static final int DEFAULT_WIDTH = 20;
    public static final int DEFAULT_HEIGHT = 15;
    public static final int POWER_PLANT_COVERAGE_RADIUS = 5; // Rayon de couverture directe des centrales

    private final int width;
    private final int height;
    private final MapCell[][] cells;
    private ElectricityGrid electricityGrid;

    public CityMap() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public CityMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new MapCell[width][height];

        // Initialiser toutes les cellules avec terrain constructible par défaut
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new MapCell(x, y, TerrainType.CONSTRUCTIBLE);
            }
        }

        // Générer l'océan par défaut (zone en bas à gauche)
        generateDefaultOcean();

        this.electricityGrid = new ElectricityGrid(this);
    }

    /**
     * Génère une zone océan par défaut en bas à gauche de la carte.
     * Crée une zone triangulaire/organique pour simuler une côte.
     */
    private void generateDefaultOcean() {
        // Zone océan en bas à gauche (environ 5x5 cases avec forme organique)
        int oceanMaxX = 6;
        int oceanStartY = height - 6;

        for (int x = 0; x < oceanMaxX; x++) {
            for (int y = oceanStartY; y < height; y++) {
                // Forme triangulaire : plus on va vers la droite, moins d'eau en haut
                int maxYForThisX = oceanStartY + (oceanMaxX - x);
                if (y >= maxYForThisX || x <= 2) {
                    if (isValidPosition(x, y)) {
                        cells[x][y].setTerrainType(TerrainType.WATER);
                    }
                }
            }
        }

        // Ajouter une petite rivière qui part de l'océan vers le haut
        int riverX = 3;
        for (int y = oceanStartY - 1; y >= oceanStartY - 4 && y >= 0; y--) {
            if (isValidPosition(riverX, y)) {
                cells[riverX][y].setTerrainType(TerrainType.WATER);
            }
            // Légère variation aléatoire
            if (new Random().nextBoolean() && riverX < width - 1) {
                riverX++;
            }
        }
    }

    // ===== Getters de base =====

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public MapCell getCell(int x, int y) {
        if (isValidPosition(x, y)) {
            return cells[x][y];
        }
        return null;
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public ElectricityGrid getElectricityGrid() {
        return electricityGrid;
    }

    // ===== Génération de terrain =====

    /**
     * Génère des cours d'eau aléatoires sur la carte.
     * 
     * @param riverCount  Nombre de rivières à générer
     * @param riverLength Longueur moyenne des rivières
     */
    public void generateRandomWater(int riverCount, int riverLength) {
        Random random = new Random();

        for (int r = 0; r < riverCount; r++) {
            // Point de départ aléatoire sur un bord
            int startX, startY;
            boolean horizontal = random.nextBoolean();

            if (horizontal) {
                startX = 0;
                startY = random.nextInt(height);
            } else {
                startX = random.nextInt(width);
                startY = 0;
            }

            int x = startX;
            int y = startY;

            for (int i = 0; i < riverLength && isValidPosition(x, y); i++) {
                cells[x][y].setTerrainType(TerrainType.WATER);

                // Direction aléatoire avec tendance vers l'avant
                int direction = random.nextInt(10);
                if (horizontal) {
                    x++; // Toujours avancer
                    if (direction < 3 && y > 0)
                        y--;
                    else if (direction < 6 && y < height - 1)
                        y++;
                } else {
                    y++; // Toujours avancer
                    if (direction < 3 && x > 0)
                        x--;
                    else if (direction < 6 && x < width - 1)
                        x++;
                }
            }
        }
    }

    /**
     * Définit le terrain d'une cellule.
     */
    public boolean setTerrain(int x, int y, TerrainType terrainType) {
        MapCell cell = getCell(x, y);
        if (cell != null) {
            // Ne pas changer le terrain si un bâtiment est présent
            if (!cell.isEmpty() && terrainType == TerrainType.WATER) {
                return false;
            }
            cell.setTerrainType(terrainType);
            return true;
        }
        return false;
    }

    /**
     * Retourne toutes les cellules de type cours d'eau.
     */
    public List<MapCell> getWaterCells() {
        List<MapCell> result = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (cells[x][y].isWater()) {
                    result.add(cells[x][y]);
                }
            }
        }
        return result;
    }

    // ===== Placement de bâtiments =====

    /**
     * Place une résidence à la position donnée.
     * Vérifie que le terrain est constructible.
     */
    public boolean placeResidence(int x, int y, Residence residence) {
        MapCell cell = getCell(x, y);
        if (cell != null && cell.isEmpty() && cell.isConstructible()) {
            if (cell.setResidence(residence)) {
                updatePowerGrid();
                return true;
            }
        }
        return false;
    }

    /**
     * Place une centrale à la position donnée.
     * Vérifie que le terrain est constructible.
     * Pour les centrales hydrauliques, vérifie qu'elles sont adjacentes à l'eau.
     */
    public boolean placePowerPlant(int x, int y, PowerPlant powerPlant) {
        MapCell cell = getCell(x, y);
        if (cell == null || !cell.isEmpty() || !cell.isConstructible()) {
            return false;
        }

        // Vérification spéciale pour les centrales hydrauliques
        if (powerPlant.getEnergyType() == tg.univlome.epl.ajee.city.skyline.model.energy.EnergyType.HYDRO) {
            if (!isAdjacentToWater(x, y)) {
                return false; // Doit être à côté de l'eau
            }
        }

        if (cell.setPowerPlant(powerPlant)) {
            updatePowerGrid();
            return true;
        }
        return false;
    }

    /**
     * Vérifie si une cellule est adjacente à un cours d'eau.
     */
    public boolean isAdjacentToWater(int x, int y) {
        // Vérifier les 8 cellules adjacentes
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0)
                    continue;

                MapCell neighbor = getCell(x + dx, y + dy);
                if (neighbor != null && neighbor.isWater()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Supprime le bâtiment à la position donnée.
     */
    public void clearCell(int x, int y) {
        MapCell cell = getCell(x, y);
        if (cell != null) {
            cell.clear();
            updatePowerGrid();
        }
    }

    // ===== Lignes électriques =====

    /**
     * Ajoute une ligne électrique entre une centrale et une résidence.
     */
    public boolean addPowerLine(int plantX, int plantY, int residenceX, int residenceY) {
        PowerLine line = electricityGrid.createAutoLine(plantX, plantY, residenceX, residenceY);
        if (line != null) {
            // Marquer les cellules traversées par la ligne
            for (java.awt.Point point : line.getPath()) {
                MapCell cell = getCell(point.x, point.y);
                if (cell != null && cell.isEmpty()) {
                    cell.setHasPowerLine(true);
                }
            }
            return electricityGrid.addPowerLine(line);
        }
        return false;
    }

    /**
     * Ajoute une ligne électrique personnalisée.
     */
    public boolean addPowerLine(PowerLine line) {
        if (line.isValid(this)) {
            // Marquer les cellules traversées par la ligne
            for (java.awt.Point point : line.getPath()) {
                MapCell cell = getCell(point.x, point.y);
                if (cell != null && cell.isEmpty()) {
                    cell.setHasPowerLine(true);
                }
            }
            return electricityGrid.addPowerLine(line);
        }
        return false;
    }

    /**
     * Retourne toutes les lignes électriques.
     */
    public List<PowerLine> getPowerLines() {
        return electricityGrid.getPowerLines();
    }

    // ===== Réseau électrique =====

    /**
     * Met à jour le réseau électrique.
     * Utilise le nouveau système avec propagation entre maisons.
     */
    public void updatePowerGrid() {
        // Utiliser l'ElectricityGrid pour la mise à jour
        electricityGrid.updateGrid();

        // Fallback : alimenter aussi par rayon direct pour compatibilité
        updatePowerGridByRadius();
    }

    /**
     * Met à jour le réseau électrique par rayon (ancien système).
     * Garde la compatibilité avec le système existant.
     */
    private void updatePowerGridByRadius() {
        // Pour chaque centrale, alimenter les résidences à proximité (rayon direct)
        List<MapCell> powerPlants = getPowerPlantCells();
        for (MapCell plantCell : powerPlants) {
            PowerPlant plant = plantCell.getPowerPlant();
            if (plant != null && plant.isOperational()) {
                // Alimenter les cellules dans le rayon
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        MapCell cell = cells[x][y];
                        if (cell.isResidence() && !cell.isPowered()) {
                            double distance = plantCell.distanceTo(x, y);
                            if (distance <= POWER_PLANT_COVERAGE_RADIUS) {
                                // Vérifier qu'il n'y a pas de cours d'eau entre
                                if (hasDirectPath(plantCell, cell)) {
                                    cell.setPowered(true);
                                    cell.setPowerLevel(0); // Alimentation directe
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Vérifie s'il existe un chemin direct sans obstacle entre deux cellules.
     */
    private boolean hasDirectPath(MapCell from, MapCell to) {
        int x0 = from.getX();
        int y0 = from.getY();
        int x1 = to.getX();
        int y1 = to.getY();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        int x = x0;
        int y = y0;

        while (x != x1 || y != y1) {
            int e2 = 2 * err;

            if (e2 > -dy) {
                err -= dy;
                x += sx;
            }

            if (e2 < dx) {
                err += dx;
                y += sy;
            }

            MapCell cell = getCell(x, y);
            if (cell == null || cell.isWater()) {
                return false;
            }
        }

        return true;
    }

    // ===== Statistiques =====

    /**
     * Retourne toutes les cellules contenant une centrale.
     */
    public List<MapCell> getPowerPlantCells() {
        List<MapCell> result = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (cells[x][y].isPowerPlant()) {
                    result.add(cells[x][y]);
                }
            }
        }
        return result;
    }

    /**
     * Retourne toutes les cellules contenant une résidence.
     */
    public List<MapCell> getResidenceCells() {
        List<MapCell> result = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (cells[x][y].isResidence()) {
                    result.add(cells[x][y]);
                }
            }
        }
        return result;
    }

    /**
     * Compte les résidences alimentées.
     */
    public int countPoweredResidences() {
        return (int) getResidenceCells().stream()
                .filter(MapCell::isPowered)
                .count();
    }

    /**
     * Compte les résidences non alimentées.
     */
    public int countUnpoweredResidences() {
        return (int) getResidenceCells().stream()
                .filter(cell -> !cell.isPowered())
                .count();
    }

    /**
     * Compte les résidences alimentées directement.
     */
    public int countDirectlyPoweredResidences() {
        return (int) getResidenceCells().stream()
                .filter(MapCell::isDirectlyPowered)
                .count();
    }

    /**
     * Compte les résidences alimentées par propagation.
     */
    public int countPropagationPoweredResidences() {
        return (int) getResidenceCells().stream()
                .filter(MapCell::isPoweredByPropagation)
                .count();
    }

    /**
     * Retourne le nombre total de bâtiments.
     */
    public int getTotalBuildings() {
        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!cells[x][y].isEmpty()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Retourne le nombre de cellules avec cours d'eau.
     */
    public int getWaterCellCount() {
        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (cells[x][y].isWater()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Affiche une représentation texte de la carte.
     */
    public String toAsciiMap() {
        StringBuilder sb = new StringBuilder();

        // En-tête avec numéros de colonnes
        sb.append("   ");
        for (int x = 0; x < width; x++) {
            sb.append(String.format("%2d ", x));
        }
        sb.append("\n");

        for (int y = 0; y < height; y++) {
            sb.append(String.format("%2d ", y));
            for (int x = 0; x < width; x++) {
                MapCell cell = cells[x][y];
                sb.append(" ").append(cell.getDisplayIcon()).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
