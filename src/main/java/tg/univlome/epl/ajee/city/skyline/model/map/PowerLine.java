package tg.univlome.epl.ajee.city.skyline.model.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une ligne électrique reliant une centrale à une maison.
 * La ligne suit un chemin de cases contigües.
 */
public class PowerLine {

    private static int idCounter = 0;

    private final int id;
    private final Point start; // Case de départ (adjacente à la centrale)
    private final Point end; // Case d'arrivée (adjacente à la maison)
    private final List<Point> path; // Chemin complet de la ligne

    public PowerLine(Point start, Point end) {
        this.id = ++idCounter;
        this.start = start;
        this.end = end;
        this.path = new ArrayList<>();
        this.path.add(start);
    }

    public PowerLine(int startX, int startY, int endX, int endY) {
        this(new Point(startX, startY), new Point(endX, endY));
    }

    public int getId() {
        return id;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public List<Point> getPath() {
        return new ArrayList<>(path);
    }

    /**
     * Ajoute un point au chemin de la ligne.
     */
    public void addPathPoint(Point point) {
        path.add(point);
    }

    /**
     * Ajoute un point au chemin de la ligne.
     */
    public void addPathPoint(int x, int y) {
        path.add(new Point(x, y));
    }

    /**
     * Calcule automatiquement le chemin le plus court entre start et end.
     * Utilise un chemin en ligne droite (Manhattan).
     */
    public void calculateStraightPath() {
        path.clear();

        int x = start.x;
        int y = start.y;

        path.add(new Point(x, y));

        // Avancer horizontalement d'abord
        while (x != end.x) {
            x += (end.x > x) ? 1 : -1;
            path.add(new Point(x, y));
        }

        // Puis verticalement
        while (y != end.y) {
            y += (end.y > y) ? 1 : -1;
            path.add(new Point(x, y));
        }
    }

    /**
     * Vérifie si la ligne est valide (ne traverse pas d'obstacles).
     */
    public boolean isValid(CityMap map) {
        for (Point point : path) {
            MapCell cell = map.getCell(point.x, point.y);
            if (cell == null) {
                return false; // Hors limites
            }
            if (cell.getTerrainType().blocksElectricity()) {
                return false; // Cours d'eau
            }
        }
        return true;
    }

    /**
     * Vérifie si la ligne passe par une cellule donnée.
     */
    public boolean passesThrough(int x, int y) {
        for (Point point : path) {
            if (point.x == x && point.y == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne la longueur de la ligne (nombre de cases).
     */
    public int getLength() {
        return path.size();
    }

    @Override
    public String toString() {
        return String.format("PowerLine[%d] (%d,%d) → (%d,%d) | Longueur: %d",
                id, start.x, start.y, end.x, end.y, getLength());
    }
}
