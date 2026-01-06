package tg.univlome.epl.ajee.city.skyline.model.map;

import tg.univlome.epl.ajee.city.skyline.model.entities.Residence;
import tg.univlome.epl.ajee.city.skyline.model.energy.PowerPlant;

/**
 * Représente une cellule de la carte de la ville.
 */
public class MapCell {

    /**
     * Types de cellules possibles.
     */
    public enum CellType {
        EMPTY("Vide", "🌿"),
        RESIDENCE("Résidence", "🏠"),
        POWER_PLANT("Centrale", "⚡");

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
    private CellType type;
    private Residence residence;
    private PowerPlant powerPlant;
    private boolean powered; // Si la cellule est alimentée en électricité

    public MapCell(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = CellType.EMPTY;
        this.powered = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

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
     */
    public void setResidence(Residence residence) {
        this.residence = residence;
        this.powerPlant = null;
        this.type = CellType.RESIDENCE;
    }

    /**
     * Place une centrale sur cette cellule.
     */
    public void setPowerPlant(PowerPlant powerPlant) {
        this.powerPlant = powerPlant;
        this.residence = null;
        this.type = CellType.POWER_PLANT;
    }

    /**
     * Vide la cellule.
     */
    public void clear() {
        this.residence = null;
        this.powerPlant = null;
        this.type = CellType.EMPTY;
        this.powered = false;
    }

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
     * Calcule la distance à une autre cellule.
     */
    public double distanceTo(MapCell other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calcule la distance à une position.
     */
    public double distanceTo(int otherX, int otherY) {
        int dx = this.x - otherX;
        int dy = this.y - otherY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format("Cell[%d,%d] %s", x, y, type.getDisplayName());
    }
}
