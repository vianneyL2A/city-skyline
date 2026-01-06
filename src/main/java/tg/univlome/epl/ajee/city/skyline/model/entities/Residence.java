package tg.univlome.epl.ajee.city.skyline.model.entities;

import tg.univlome.epl.ajee.city.skyline.model.config.RandomRange;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une résidence dans la ville.
 * Une résidence a un niveau et contient des habitants.
 */
public class Residence {

    private static int idCounter = 0;

    private final int id;
    private String name;
    private ResidenceLevel level;
    private final List<Inhabitant> inhabitants;
    private int energyNeed; // Consommation d'énergie (calculée aléatoirement)
    private int purchasePower; // Pouvoir d'achat (calculé aléatoirement)
    private boolean energySupplied; // Si la résidence est alimentée en énergie

    public Residence(String name, ResidenceLevel level) {
        this.id = ++idCounter;
        this.name = name;
        this.level = level;
        this.inhabitants = new ArrayList<>();
        this.energySupplied = false;

        // Calcul des valeurs aléatoires basées sur le niveau
        recalculateRandomValues();
    }

    public Residence(ResidenceLevel level) {
        this("Résidence_" + (idCounter + 1), level);
    }

    /**
     * Recalcule les valeurs aléatoires (appeler après un changement de niveau).
     */
    public void recalculateRandomValues() {
        this.energyNeed = new RandomRange(level.getMinEnergyNeed(), level.getMaxEnergyNeed()).getValue();
        this.purchasePower = new RandomRange(level.getMinPurchasePower(), level.getMaxPurchasePower()).getValue();
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

    public ResidenceLevel getLevel() {
        return level;
    }

    /**
     * Améliore le niveau de la résidence.
     */
    public boolean upgrade() {
        ResidenceLevel[] levels = ResidenceLevel.values();
        int currentIndex = level.ordinal();
        if (currentIndex < levels.length - 1) {
            this.level = levels[currentIndex + 1];
            recalculateRandomValues();
            return true;
        }
        return false;
    }

    public List<Inhabitant> getInhabitants() {
        return new ArrayList<>(inhabitants);
    }

    public int getInhabitantCount() {
        return inhabitants.size();
    }

    /**
     * Ajoute un habitant si la résidence n'est pas pleine.
     */
    public boolean addInhabitant(Inhabitant inhabitant) {
        if (inhabitants.size() < level.getMaxInhabitants()) {
            inhabitants.add(inhabitant);
            return true;
        }
        return false;
    }

    /**
     * Ajoute un nouvel habitant généré automatiquement.
     */
    public Inhabitant addNewInhabitant() {
        if (inhabitants.size() < level.getMaxInhabitants()) {
            Inhabitant inhabitant = new Inhabitant(this);
            inhabitants.add(inhabitant);
            return inhabitant;
        }
        return null;
    }

    public boolean removeInhabitant(Inhabitant inhabitant) {
        return inhabitants.remove(inhabitant);
    }

    public int getEnergyNeed() {
        return energyNeed;
    }

    public int getPurchasePower() {
        return purchasePower;
    }

    public boolean isEnergySupplied() {
        return energySupplied;
    }

    public void setEnergySupplied(boolean energySupplied) {
        this.energySupplied = energySupplied;
    }

    public boolean isFull() {
        return inhabitants.size() >= level.getMaxInhabitants();
    }

    /**
     * Calcule la satisfaction moyenne des habitants.
     */
    public double getAverageSatisfaction() {
        if (inhabitants.isEmpty()) {
            return 0;
        }
        return inhabitants.stream()
                .mapToInt(Inhabitant::getSatisfaction)
                .average()
                .orElse(0);
    }

    /**
     * Calcule le montant que la résidence peut payer pour l'énergie.
     */
    public int calculateEnergyPayment(double pricePerUnit) {
        if (!energySupplied) {
            return 0;
        }
        double payment = energyNeed * pricePerUnit;
        return (int) Math.min(payment, purchasePower);
    }

    @Override
    public String toString() {
        return String.format("Résidence[%d] %s - %s (%d/%d habitants, Besoin: %d kWh)",
                id, name, level.getDisplayName(), inhabitants.size(),
                level.getMaxInhabitants(), energyNeed);
    }
}
