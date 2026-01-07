package tg.univlome.epl.ajee.city.skyline.model.entities;

/**
 * Niveaux des résidences avec leurs caractéristiques.
 * Chaque niveau définit des besoins en énergie et un pouvoir d'achat
 * différents.
 */
public enum ResidenceLevel {

    BASIC("Basique", 50, 100, 10, 20, 5, 2),
    STANDARD("Standard", 100, 200, 20, 40, 10, 5),
    LUXURY("Luxueux", 200, 400, 40, 80, 20, 10),
    PREMIUM("Premium", 400, 800, 80, 150, 30, 20);

    private final String displayName;
    private final int minEnergyNeed; // Consommation minimale d'énergie par cycle
    private final int maxEnergyNeed; // Consommation maximale d'énergie par cycle
    private final int minPurchasePower; // Pouvoir d'achat minimum (prix max accepté)
    private final int maxPurchasePower; // Pouvoir d'achat maximum
    private final int maxInhabitants; // Nombre maximum d'habitants
    private final int taxPerInhabitant; // Taxe par habitant par cycle (en €)

    ResidenceLevel(String displayName, int minEnergyNeed, int maxEnergyNeed,
            int minPurchasePower, int maxPurchasePower, int maxInhabitants, int taxPerInhabitant) {
        this.displayName = displayName;
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

    /**
     * Retourne la taxe par habitant par cycle (en €).
     * Les habitants ne paient la taxe que s'ils sont alimentés en électricité.
     */
    public int getTaxPerInhabitant() {
        return taxPerInhabitant;
    }
}
