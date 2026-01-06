package tg.univlome.epl.ajee.city.skyline.model.energy;

import tg.univlome.epl.ajee.city.skyline.model.config.RandomRange;

/**
 * Classe abstraite représentant une centrale électrique.
 * Les différents types de centrales héritent de cette classe.
 */
public abstract class PowerPlant {

    private static int idCounter = 0;

    protected final int id;
    protected String name;
    protected PlantLevel level;
    protected final EnergyType energyType;
    protected final int baseCost;
    protected final int baseProduction;
    protected final int baseMaintenance;
    protected boolean operational;
    protected int age; // Âge en cycles

    protected PowerPlant(String name, EnergyType energyType, int baseCost,
            int baseProduction, int baseMaintenance) {
        this.id = ++idCounter;
        this.name = name;
        this.energyType = energyType;
        this.baseCost = baseCost;
        this.baseProduction = baseProduction;
        this.baseMaintenance = baseMaintenance;
        this.level = PlantLevel.LEVEL_1;
        this.operational = true;
        this.age = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlantLevel getLevel() {
        return level;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public int getBaseCost() {
        return baseCost;
    }

    /**
     * Calcule la production actuelle en tenant compte du niveau et de la fiabilité.
     * Les centrales renouvelables ont une production variable.
     */
    public int calculateProduction() {
        if (!operational) {
            return 0;
        }

        double production = baseProduction * level.getProductionMultiplier();

        // Variabilité basée sur la fiabilité
        double reliability = energyType.getReliability();
        if (reliability < 1.0) {
            // Production variable : entre (reliability * 100)% et 100%
            double minFactor = reliability;
            double factor = minFactor + new RandomRange(0, 100).getValue() / 100.0 * (1 - minFactor);
            production *= factor;
        }

        return (int) production;
    }

    /**
     * Calcule le coût de maintenance actuel.
     */
    public int calculateMaintenance() {
        return (int) (baseMaintenance * level.getMaintenanceMultiplier());
    }

    /**
     * Calcule le coût d'amélioration au niveau suivant.
     */
    public int calculateUpgradeCost() {
        if (!level.canUpgrade()) {
            return -1; // Ne peut pas être amélioré
        }
        return (int) (baseCost * level.getNextLevel().getProductionMultiplier() * 0.6);
    }

    /**
     * Améliore la centrale au niveau suivant.
     */
    public boolean upgrade() {
        if (level.canUpgrade()) {
            level = level.getNextLevel();
            return true;
        }
        return false;
    }

    public boolean isOperational() {
        return operational;
    }

    public void setOperational(boolean operational) {
        this.operational = operational;
    }

    public int getAge() {
        return age;
    }

    public void incrementAge() {
        this.age++;
    }

    /**
     * Retourne une description lisible de la centrale.
     */
    public String getDescription() {
        return String.format("%s %s - %s", energyType.getIcon(), name, level.getDisplayName());
    }

    @Override
    public String toString() {
        return String.format("Centrale[%d] %s (%s - %s) | Production: ~%d kWh | Maintenance: %d€",
                id, name, energyType.getDisplayName(), level.getDisplayName(),
                baseProduction, calculateMaintenance());
    }
}
