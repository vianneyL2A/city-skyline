package tg.univlome.epl.ajee.city.skyline.model.map;

import tg.univlome.epl.ajee.city.skyline.model.entities.Residence;
import tg.univlome.epl.ajee.city.skyline.model.energy.PowerPlant;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente la carte de la ville sous forme de grille 2D.
 */
public class CityMap {

    public static final int DEFAULT_WIDTH = 20;
    public static final int DEFAULT_HEIGHT = 15;
    public static final int POWER_PLANT_COVERAGE_RADIUS = 5; // Rayon de couverture des centrales

    private final int width;
    private final int height;
    private final MapCell[][] cells;

    public CityMap() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public CityMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new MapCell[width][height];

        // Initialiser toutes les cellules
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new MapCell(x, y);
            }
        }
    }

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

    /**
     * Place une résidence à la position donnée.
     */
    public boolean placeResidence(int x, int y, Residence residence) {
        MapCell cell = getCell(x, y);
        if (cell != null && cell.isEmpty()) {
            cell.setResidence(residence);
            updatePowerGrid();
            return true;
        }
        return false;
    }

    /**
     * Place une centrale à la position donnée.
     */
    public boolean placePowerPlant(int x, int y, PowerPlant powerPlant) {
        MapCell cell = getCell(x, y);
        if (cell != null && cell.isEmpty()) {
            cell.setPowerPlant(powerPlant);
            updatePowerGrid();
            return true;
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

    /**
     * Met à jour le réseau électrique (quelles résidences sont alimentées).
     */
    public void updatePowerGrid() {
        // Réinitialiser l'alimentation
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y].setPowered(cells[x][y].isPowerPlant());
            }
        }

        // Pour chaque centrale, alimenter les résidences à proximité
        List<MapCell> powerPlants = getPowerPlantCells();
        for (MapCell plantCell : powerPlants) {
            PowerPlant plant = plantCell.getPowerPlant();
            if (plant != null && plant.isOperational()) {
                // Alimenter les cellules dans le rayon
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        MapCell cell = cells[x][y];
                        if (cell.isResidence()) {
                            double distance = plantCell.distanceTo(x, y);
                            if (distance <= POWER_PLANT_COVERAGE_RADIUS) {
                                cell.setPowered(true);
                            }
                        }
                    }
                }
            }
        }
    }

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
}
