package tg.univlome.epl.ajee.city.skyline.model.entities;

/**
 * Niveaux des résidences avec leurs caractéristiques.
 * - BASIC: Petite maison (5 habitants max)
 * - MEDIUM: Immeuble 4 étages (20 habitants max)
 * - HIGH: Tour 50 étages (100 habitants max)
 */
public enum ResidenceLevel {

    BASIC("Maison", "🏠", "residence_basic.png", 50, 100, 10, 20, 5, 4),
    MEDIUM("Immeuble", "🏢", "residence_medium.png", 150, 300, 30, 60, 20, 8),
    HIGH("Tour", "🏙️", "residence_high.png", 500, 1000, 80, 150, 100, 16);

    private final String displayName;
    private final String icon;
    private final String imageName;
    private final int minEnergyNeed;
    private final int maxEnergyNeed;
    private final int minPurchasePower;
    private final int maxPurchasePower;
    private final int maxInhabitants;
    private final int taxPerInhabitant;

    ResidenceLevel(String displayName, String icon, String imageName,
            int minEnergyNeed, int maxEnergyNeed,
            int minPurchasePower, int maxPurchasePower,
            int maxInhabitants, int taxPerInhabitant) {
        this.displayName = displayName;
        this.icon = icon;
        this.imageName = imageName;
        this.minEnergyNeed = minEnergyNeed;
        this.maxEnergyNeed = maxEnergyNeed;
        this.minPurchasePower = minPurchasePower;
        this.maxPurchasePower = maxPurchasePower;
        this.maxInhabitants = maxInhabitants;
        this.taxPerInhabitant = taxPerInhabitant;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public String getImageName() {
        return imageName;
    }

    public int getMinEnergyNeed() {
        return minEnergyNeed;
    }

    public int getMaxEnergyNeed() {
        return maxEnergyNeed;
    }

    public int getMinPurchasePower() {
        return minPurchasePower;
    }

    public int getMaxPurchasePower() {
        return maxPurchasePower;
    }

    public int getMaxInhabitants() {
        return maxInhabitants;
    }

    public int getTaxPerInhabitant() {
        return taxPerInhabitant;
    }

    /**
     * Vérifie si ce niveau peut être amélioré.
     */
    public boolean canUpgrade() {
        return this != HIGH;
    }

    /**
     * Retourne le niveau suivant (ou null si niveau max).
     */
    public ResidenceLevel getNextLevel() {
        return switch (this) {
            case BASIC -> MEDIUM;
            case MEDIUM -> HIGH;
            case HIGH -> null;
        };
    }

    /**
     * Retourne le coût d'amélioration vers le niveau suivant.
     */
    public int getUpgradeCost() {
        return switch (this) {
            case BASIC -> 500;
            case MEDIUM -> 2000;
            case HIGH -> 0;
        };
    }
}
