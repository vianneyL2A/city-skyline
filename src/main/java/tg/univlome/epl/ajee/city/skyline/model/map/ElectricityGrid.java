package tg.univlome.epl.ajee.city.skyline.model.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Gère le réseau électrique de la ville.
 * Inclut les lignes de raccordement et la propagation entre maisons.
 */
public class ElectricityGrid {

    /**
     * Distance maximale de propagation entre maisons (en cases).
     */
    public static final int MAX_PROPAGATION_DISTANCE = 3;

    private final CityMap map;
    private final List<PowerLine> powerLines;

    public ElectricityGrid(CityMap map) {
        this.map = map;
        this.powerLines = new ArrayList<>();
    }

    /**
     * Ajoute une ligne électrique au réseau.
     * 
     * @param line La ligne à ajouter
     * @return true si la ligne est valide et a été ajoutée
     */
    public boolean addPowerLine(PowerLine line) {
        if (line.isValid(map)) {
            powerLines.add(line);
            updateGrid();
            return true;
        }
        return false;
    }

    /**
     * Supprime une ligne électrique du réseau.
     */
    public void removePowerLine(PowerLine line) {
        powerLines.remove(line);
        updateGrid();
    }

    /**
     * Supprime une ligne électrique par son ID.
     */
    public boolean removePowerLineById(int lineId) {
        for (int i = 0; i < powerLines.size(); i++) {
            if (powerLines.get(i).getId() == lineId) {
                powerLines.remove(i);
                updateGrid();
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne toutes les lignes électriques.
     */
    public List<PowerLine> getPowerLines() {
        return new ArrayList<>(powerLines);
    }

    /**
     * Met à jour tout le réseau électrique.
     * 1. Réinitialise l'alimentation de toutes les cellules
     * 2. Alimente les maisons dans le rayon de couverture des centrales
     * (directement raccordées)
     * 3. Alimente les maisons via les lignes électriques (directement raccordées)
     * 4. Propage l'électricité aux maisons voisines
     */
    public void updateGrid() {
        // 1. Réinitialiser l'alimentation de toutes les cellules
        resetPower();

        // 2. Identifier et alimenter les maisons directement raccordées
        Set<MapCell> directlyPowered = new HashSet<>();

        // 2a. Alimenter les résidences dans le rayon de couverture des centrales
        List<MapCell> powerPlants = map.getPowerPlantCells();
        for (MapCell plantCell : powerPlants) {
            if (plantCell.getPowerPlant() != null && plantCell.getPowerPlant().isOperational()) {
                // Trouver toutes les résidences dans le rayon de couverture
                List<MapCell> residencesInRange = findResidencesInCoverageRadius(
                        plantCell.getX(), plantCell.getY(), CityMap.POWER_PLANT_COVERAGE_RADIUS);

                for (MapCell residence : residencesInRange) {
                    if (!residence.isPowered()) {
                        residence.setPowered(true);
                        residence.setPowerLevel(0); // Niveau 0 = raccordement direct
                        directlyPowered.add(residence);
                    }
                }
            }
        }

        // 2b. Alimenter les résidences via les lignes électriques
        for (PowerLine line : powerLines) {
            if (!line.isValid(map)) {
                continue; // Ignorer les lignes invalides
            }

            // Vérifier si la ligne part d'une centrale active
            if (!isConnectedToPowerPlant(line)) {
                continue;
            }

            // Alimenter toutes les résidences adjacentes au bout de la ligne
            Point end = line.getEnd();
            List<MapCell> adjacentResidences = getAdjacentResidences(end.x, end.y);

            for (MapCell residence : adjacentResidences) {
                if (!residence.isPowered()) {
                    residence.setPowered(true);
                    residence.setPowerLevel(0); // Niveau 0 = raccordement direct
                    directlyPowered.add(residence);
                }
            }
        }

        // 3. Propager l'électricité aux maisons voisines (BFS)
        propagatePower(directlyPowered);
    }

    /**
     * Trouve les résidences dans le rayon de couverture d'une centrale.
     */
    private List<MapCell> findResidencesInCoverageRadius(int centerX, int centerY, int radius) {
        List<MapCell> residences = new ArrayList<>();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (dx == 0 && dy == 0)
                    continue;

                // Distance euclidienne
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance > radius)
                    continue;

                MapCell cell = map.getCell(centerX + dx, centerY + dy);
                if (cell != null && cell.isResidence()) {
                    residences.add(cell);
                }
            }
        }
        return residences;
    }

    /**
     * Réinitialise l'alimentation de toutes les cellules sauf les centrales.
     */
    private void resetPower() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                MapCell cell = map.getCell(x, y);
                if (cell.isPowerPlant()) {
                    cell.setPowered(true);
                    cell.setPowerLevel(0);
                } else {
                    cell.setPowered(false);
                    cell.setPowerLevel(-1);
                }
            }
        }
    }

    /**
     * Vérifie si une ligne est connectée à une centrale opérationnelle.
     */
    private boolean isConnectedToPowerPlant(PowerLine line) {
        Point start = line.getStart();

        // Vérifier les cellules adjacentes au point de départ
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0)
                    continue;

                MapCell cell = map.getCell(start.x + dx, start.y + dy);
                if (cell != null && cell.isPowerPlant()) {
                    if (cell.getPowerPlant() != null && cell.getPowerPlant().isOperational()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Retourne les résidences adjacentes à une position (rayon de 1 case).
     */
    private List<MapCell> getAdjacentResidences(int x, int y) {
        List<MapCell> residences = new ArrayList<>();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapCell cell = map.getCell(x + dx, y + dy);
                if (cell != null && cell.isResidence()) {
                    residences.add(cell);
                }
            }
        }
        return residences;
    }

    /**
     * Propage l'électricité des maisons alimentées vers leurs voisines.
     * Utilise BFS avec limite de distance de MAX_PROPAGATION_DISTANCE cases.
     */
    private void propagatePower(Set<MapCell> directlyPowered) {
        Queue<MapCell> queue = new LinkedList<>(directlyPowered);
        Map<MapCell, Integer> visited = new HashMap<>();

        for (MapCell cell : directlyPowered) {
            visited.put(cell, 0);
        }

        while (!queue.isEmpty()) {
            MapCell current = queue.poll();
            int currentLevel = visited.get(current);

            // Trouver les maisons voisines dans un rayon de MAX_PROPAGATION_DISTANCE
            List<MapCell> neighbors = findResidencesInRange(current, MAX_PROPAGATION_DISTANCE);

            for (MapCell neighbor : neighbors) {
                if (!visited.containsKey(neighbor)) {
                    // Vérifier qu'il n'y a pas de cours d'eau entre les deux maisons
                    if (hasDirectPath(current, neighbor)) {
                        int newLevel = currentLevel + 1;
                        neighbor.setPowered(true);
                        neighbor.setPowerLevel(newLevel);
                        visited.put(neighbor, newLevel);
                        queue.add(neighbor);
                    }
                }
            }
        }
    }

    /**
     * Trouve toutes les résidences dans un rayon donné autour d'une cellule.
     */
    private List<MapCell> findResidencesInRange(MapCell center, int range) {
        List<MapCell> residences = new ArrayList<>();

        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                if (dx == 0 && dy == 0)
                    continue;

                // Vérifier la distance (Manhattan)
                if (Math.abs(dx) + Math.abs(dy) > range)
                    continue;

                MapCell cell = map.getCell(center.getX() + dx, center.getY() + dy);
                if (cell != null && cell.isResidence()) {
                    residences.add(cell);
                }
            }
        }
        return residences;
    }

    /**
     * Vérifie s'il existe un chemin direct entre deux cellules sans obstacle.
     * Utilise un tracé de ligne simple (Bresenham simplifié).
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

            // Vérifier si la cellule intermédiaire bloque
            MapCell cell = map.getCell(x, y);
            if (cell == null) {
                return false;
            }
            if (cell.getTerrainType().blocksElectricity()) {
                return false; // Cours d'eau bloque le passage
            }
        }

        return true;
    }

    /**
     * Crée automatiquement une ligne entre une centrale et une résidence.
     * 
     * @param plantX     Position X de la centrale
     * @param plantY     Position Y de la centrale
     * @param residenceX Position X de la résidence
     * @param residenceY Position Y de la résidence
     * @return La ligne créée, ou null si impossible
     */
    public PowerLine createAutoLine(int plantX, int plantY, int residenceX, int residenceY) {
        // Trouver un point adjacent à la centrale comme départ
        Point start = findAdjacentEmptyCell(plantX, plantY);
        if (start == null) {
            return null;
        }

        // Trouver un point adjacent à la résidence comme arrivée
        Point end = findAdjacentEmptyCell(residenceX, residenceY);
        if (end == null) {
            return null;
        }

        PowerLine line = new PowerLine(start, end);
        line.calculateStraightPath();

        if (line.isValid(map)) {
            return line;
        }
        return null;
    }

    /**
     * Trouve une cellule vide adjacente à une position.
     */
    private Point findAdjacentEmptyCell(int x, int y) {
        // D'abord les 4 directions cardinales
        int[][] directions = { { 0, -1 }, { 1, 0 }, { 0, 1 }, { -1, 0 } };

        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            MapCell cell = map.getCell(nx, ny);
            if (cell != null && cell.isEmpty() && cell.isConstructible()) {
                return new Point(nx, ny);
            }
        }

        // Puis les diagonales
        int[][] diagonals = { { -1, -1 }, { 1, -1 }, { -1, 1 }, { 1, 1 } };
        for (int[] dir : diagonals) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            MapCell cell = map.getCell(nx, ny);
            if (cell != null && cell.isEmpty() && cell.isConstructible()) {
                return new Point(nx, ny);
            }
        }

        return null;
    }

    /**
     * Retourne le nombre de maisons alimentées directement (niveau 0).
     */
    public int countDirectlyPowered() {
        int count = 0;
        for (MapCell cell : map.getResidenceCells()) {
            if (cell.isPowered() && cell.getPowerLevel() == 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Retourne le nombre de maisons alimentées par propagation (niveau > 0).
     */
    public int countPropagationPowered() {
        int count = 0;
        for (MapCell cell : map.getResidenceCells()) {
            if (cell.isPowered() && cell.getPowerLevel() > 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Retourne le nombre de maisons non alimentées.
     */
    public int countUnpowered() {
        int count = 0;
        for (MapCell cell : map.getResidenceCells()) {
            if (!cell.isPowered()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return String.format("ElectricityGrid | Lignes: %d | Direct: %d | Propagation: %d | Non alimentées: %d",
                powerLines.size(), countDirectlyPowered(), countPropagationPowered(), countUnpowered());
    }
}
