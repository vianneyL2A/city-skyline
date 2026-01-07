package tg.univlome.epl.ajee.city.skyline.model.map;

/**
 * Types de terrain possibles sur la carte.
 * 
 * Code 0 = Terrain constructible
 * Code 1 = Cours d'eau (non constructible, bloque électricité)
 */
public enum TerrainType {
    CONSTRUCTIBLE(0, "Constructible", "🌿"),
    WATER(1, "Cours d'eau", "🌊");

    private final int code;
    private final String displayName;
    private final String icon;

    TerrainType(int code, String displayName, String icon) {
        this.code = code;
        this.displayName = displayName;
        this.icon = icon;
    }

    public int getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    /**
     * Retourne le type de terrain à partir de son code.
     */
    public static TerrainType fromCode(int code) {
        for (TerrainType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return CONSTRUCTIBLE; // Par défaut
    }

    /**
     * Vérifie si ce terrain permet la construction.
     */
    public boolean isConstructible() {
        return this == CONSTRUCTIBLE;
    }

    /**
     * Vérifie si ce terrain bloque le passage de l'électricité.
     */
    public boolean blocksElectricity() {
        return this == WATER;
    }
}
