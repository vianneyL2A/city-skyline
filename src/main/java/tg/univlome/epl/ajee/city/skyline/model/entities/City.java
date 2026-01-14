package tg.univlome.epl.ajee.city.skyline.model.entities;

import tg.univlome.epl.ajee.city.skyline.model.energy.PowerPlant;
import tg.univlome.epl.ajee.city.skyline.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente la ville que le joueur doit alimenter en énergie.
 * Conteneur principal pour les résidences et centrales.
 */
public class City {

    private String name;
    private final List<Residence> residences;
    private final List<PowerPlant> powerPlants;
    private int globalHappiness;

    public City(String name) {
        this.name = name;
        this.residences = new ArrayList<>();
        this.powerPlants = new ArrayList<>();
        this.globalHappiness = Constants.INITIAL_HAPPINESS;
    }

    public City() {
        this("Ma Ville");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // === Gestion des résidences ===

    public void addResidence(Residence residence) {
        residences.add(residence);
    }

    public boolean removeResidence(Residence residence) {
        return residences.remove(residence);
    }

    public List<Residence> getResidences() {
        return new ArrayList<>(residences);
    }

    public int getResidenceCount() {
        return residences.size();
    }

    // === Gestion des centrales ===

    public void addPowerPlant(PowerPlant plant) {
        powerPlants.add(plant);
    }

    public boolean removePowerPlant(PowerPlant plant) {
        return powerPlants.remove(plant);
    }

    public List<PowerPlant> getPowerPlants() {
        return new ArrayList<>(powerPlants);
    }

    public int getPowerPlantCount() {
        return powerPlants.size();
    }

    // === Calculs ===

    /**
     * Calcule la production totale d'énergie de toutes les centrales.
     */
    public int calculateTotalProduction() {
        return powerPlants.stream()
                .filter(PowerPlant::isOperational)
                .mapToInt(PowerPlant::calculateProduction)
                .sum();
    }

    /**
     * Calcule la demande totale d'énergie de toutes les résidences.
     */
    public int calculateTotalDemand() {
        return residences.stream()
                .mapToInt(Residence::getEnergyNeed)
                .sum();
    }

    /**
     * Calcule le surplus ou déficit d'énergie.
     * Positif = surplus, Négatif = déficit
     */
    public int calculateEnergyBalance() {
        return calculateTotalProduction() - calculateTotalDemand();
    }

    /**
     * Calcule le coût total de maintenance des centrales.
     */
    public int calculateTotalMaintenance() {
        return powerPlants.stream()
                .filter(PowerPlant::isOperational)
                .mapToInt(PowerPlant::calculateMaintenance)
                .sum();
    }

    /**
     * Compte le nombre total d'habitants.
     */
    public int getTotalInhabitants() {
        return residences.stream()
                .mapToInt(Residence::getInhabitantCount)
                .sum();
    }

    /**
     * Calcule le total des taxes à collecter.
     * Les habitants ne paient la taxe que si leur résidence est alimentée en
     * électricité.
     */
    public int calculateTotalTax() {
        return residences.stream()
                .mapToInt(Residence::calculateTax)
                .sum();
    }

    /**
     * Calcule la satisfaction moyenne de tous les habitants.
     */
    public double calculateAverageHappiness() {
        int totalInhabitants = getTotalInhabitants();
        if (totalInhabitants == 0) {
            return globalHappiness;
        }

        double totalSatisfaction = residences.stream()
                .flatMap(r -> r.getInhabitants().stream())
                .mapToInt(Inhabitant::getSatisfaction)
                .sum();

        return totalSatisfaction / totalInhabitants;
    }

    public int getGlobalHappiness() {
        return globalHappiness;
    }

    public void setGlobalHappiness(int happiness) {
        this.globalHappiness = Math.max(0, Math.min(100, happiness));
    }

    public void adjustGlobalHappiness(int delta) {
        setGlobalHappiness(globalHappiness + delta);
    }

    /**
     * Vérifie si le niveau de bonheur est critique (Game Over imminent).
     */
    public boolean isHappinessCritical() {
        return globalHappiness < Constants.MIN_HAPPINESS_THRESHOLD;
    }

    /**
     * Vérifie si la ville est correctement alimentée.
     */
    public boolean isFullyPowered() {
        return calculateEnergyBalance() >= 0;
    }

    /**
     * Réinitialise la ville pour une nouvelle partie.
     */
    public void reset() {
        this.residences.clear();
        this.powerPlants.clear();
        this.globalHappiness = Constants.INITIAL_HAPPINESS;
    }

    @Override
    public String toString() {
        return String.format("Ville: %s | %d résidences | %d centrales | %d habitants | Bonheur: %d%%",
                name, residences.size(), powerPlants.size(), getTotalInhabitants(), globalHappiness);
    }
}
