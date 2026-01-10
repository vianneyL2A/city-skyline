package tg.univlome.epl.ajee.city.skyline.model.energy;

/**
 * Niveaux des centrales électriques.
 * Chaque niveau augmente la production et les coûts.
 */
public enum PlantLevel {

    LEVEL_1("1", 1.0, 1.0),
    LEVEL_2("2", 1.5, 1.3),
    LEVEL_3("3", 2.2, 1.6),
    LEVEL_4("4", 3.0, 2.0);

    private final String displayName;
    private final double productionMultiplier; // Multiplicateur de production
    private final double maintenanceMultiplier; // Multiplicateur de coûts de maintenance

    PlantLevel(String displayName, double productionMultiplier, double maintenanceMultiplier) {
        this.displayName = displayName;
        this.productionMultiplier = productionMultiplier;
        this.maintenanceMultiplier = maintenanceMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getProductionMultiplier() {
        return productionMultiplier;
    }

    public double getMaintenanceMultiplier() {
        return maintenanceMultiplier;
    }

    /**
     * Retourne le niveau suivant (pour upgrade).
     * 
     * @return Le niveau suivant ou null si déjà au max
     */
    public PlantLevel getNextLevel() {
        PlantLevel[] levels = values();
        int currentIndex = this.ordinal();
        if (currentIndex < levels.length - 1) {
            return levels[currentIndex + 1];
        }
        return null;
    }

    /**
     * Vérifie si ce niveau peut être amélioré.
     */
    public boolean canUpgrade() {
        return getNextLevel() != null;
    }
}
