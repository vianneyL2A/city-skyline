package tg.univlome.epl.ajee.city.skyline.model.map;

import tg.univlome.epl.ajee.city.skyline.model.entities.Residence;
import tg.univlome.epl.ajee.city.skyline.model.energy.PowerPlant;

/**
 * Représente une cellule de la carte de la ville.
 */
public class MapCell {

    /**
     * Types de cellules possibles (bâtiments).
     */
    public enum CellType {
        EMPTY("Vide", "🌿"),
        RESIDENCE("Résidence", "🏠"),
        POWER_PLANT("Centrale", "⚡"),
        POWER_LINE("Ligne électrique", "─");

        private final String displayName;
        private final String icon;

        CellType(String displayName, String icon) {
            this.displayName = displayName;
            this.icon = icon;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getIcon() {
            return icon;
        }
    }

    private final int x;
    private final int y;
    private TerrainType terrainType; // Type de terrain (constructible ou cours d'eau)
    private CellType type; // Type de bâtiment
    private Residence residence;
    private PowerPlant powerPlant;
    private boolean powered; // Si la cellule est alimentée en électricité
    private int powerLevel; // Niveau d'alimentation (-1 = non alimenté, 0 = direct, 1+ = propagation)
    private boolean hasPowerLine; // Si une ligne électrique passe par cette cellule

    public MapCell(int x, int y) {
        this(x, y, TerrainType.CONSTRUCTIBLE);
    }

    public MapCell(int x, int y, TerrainType terrainType) {
        this.x = x;
        this.y = y;
        this.terrainType = terrainType;
        this.type = CellType.EMPTY;
        this.powered = false;
        this.powerLevel = -1;
        this.hasPowerLine = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // ===== Terrain =====

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public void setTerrainType(TerrainType terrainType) {
        this.terrainType = terrainType;
    }

    /**
     * Vérifie si le terrain est constructible (code 0).
     */
    public boolean isConstructible() {
        return terrainType.isConstructible() && !terrainType.blocksElectricity();
    }

    /**
     * Vérifie si le terrain est un cours d'eau (code 1).
     */
    public boolean isWater() {
        return terrainType == TerrainType.WATER;
    }

    // ===== Type de bâtiment =====

    public CellType getType() {
        return type;
    }

    public boolean isEmpty() {
        return type == CellType.EMPTY;
    }

    public boolean isResidence() {
        return type == CellType.RESIDENCE;
    }

    public boolean isPowerPlant() {
        return type == CellType.POWER_PLANT;
    }

    public Residence getResidence() {
        return residence;
    }

    public PowerPlant getPowerPlant() {
        return powerPlant;
    }

    /**
     * Place une résidence sur cette cellule.
     * 
     * @return true si la résidence a été placée, false si le terrain n'est pas
     *         constructible
     */
    public boolean setResidence(Residence residence) {
        if (!isConstructible()) {
            return false;
        }
        this.residence = residence;
        this.powerPlant = null;
        this.type = CellType.RESIDENCE;
        return true;
    }

    /**
     * Place une centrale sur cette cellule.
     * Les centrales hydrauliques peuvent être placées sur l'eau.
     * Les autres centrales ne peuvent être placées que sur terrain constructible.
     * 
     * @return true si la centrale a été placée, false sinon
     */
    public boolean setPowerPlant(PowerPlant powerPlant) {
        boolean isHydro = powerPlant.getEnergyType() == tg.univlome.epl.ajee.city.skyline.model.energy.EnergyType.HYDRO;

        if (isHydro) {
            // Les centrales hydrauliques ne peuvent être placées que sur l'eau
            if (!isWater()) {
                return false;
            }
        } else {
            // Les autres centrales ne peuvent être placées que sur terrain constructible
            if (!isConstructible()) {
                return false;
            }
        }

        this.powerPlant = powerPlant;
        this.residence = null;
        this.type = CellType.POWER_PLANT;
        return true;
    }

    /**
     * Vide la cellule (supprime le bâtiment mais garde le terrain).
     */
    public void clear() {
        this.residence = null;
        this.powerPlant = null;
        this.type = CellType.EMPTY;
        this.powered = false;
        this.powerLevel = -1;
        this.hasPowerLine = false;
    }

    // ===== Alimentation électrique =====

    public boolean isPowered() {
        return powered;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
        if (residence != null) {
            residence.setEnergySupplied(powered);
        }
    }

    /**
     * Retourne le niveau d'alimentation.
     * -1 = non alimenté
     * 0 = raccordement direct à une centrale
     * 1+ = alimenté par propagation (le nombre indique le niveau de propagation)
     */
    public int getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }

    /**
     * Vérifie si cette cellule est alimentée directement par une centrale.
     */
    public boolean isDirectlyPowered() {
        return powered && powerLevel == 0;
    }

    /**
     * Vérifie si cette cellule est alimentée par propagation.
     */
    public boolean isPoweredByPropagation() {
        return powered && powerLevel > 0;
    }

    // ===== Lignes électriques =====

    public boolean hasPowerLine() {
        return hasPowerLine;
    }

    public void setHasPowerLine(boolean hasPowerLine) {
        this.hasPowerLine = hasPowerLine;
    }

    // ===== Utilitaires =====

    /**
     * Calcule la distance à une autre cellule (Euclidienne).
     */
    public double distanceTo(MapCell other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calcule la distance à une position (Euclidienne).
     */
    public double distanceTo(int otherX, int otherY) {
        int dx = this.x - otherX;
        int dy = this.y - otherY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calcule la distance de Manhattan à une autre cellule.
     */
    public int manhattanDistanceTo(MapCell other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    /**
     * Calcule la distance de Manhattan à une position.
     */
    public int manhattanDistanceTo(int otherX, int otherY) {
        return Math.abs(this.x - otherX) + Math.abs(this.y - otherY);
    }

    /**
     * Retourne l'icône appropriée pour l'affichage.
     */
    public String getDisplayIcon() {
        if (isWater()) {
            return terrainType.getIcon(); // 🌊
        }
        if (hasPowerLine && isEmpty()) {
            return CellType.POWER_LINE.getIcon(); // ─
        }
        return type.getIcon();
    }

    @Override
    public String toString() {
        String powerInfo = powered ? (powerLevel == 0 ? "⚡Direct" : "⚡Prop" + powerLevel) : "❌";
        return String.format("Cell[%d,%d] %s (%s) %s",
                x, y, type.getDisplayName(), terrainType.getDisplayName(), powerInfo);
    }
}
