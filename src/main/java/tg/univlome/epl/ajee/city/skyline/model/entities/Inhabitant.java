package tg.univlome.epl.ajee.city.skyline.model.entities;

import tg.univlome.epl.ajee.city.skyline.model.config.RandomRange;

/**
 * Représente un habitant de la ville.
 * Chaque habitant a un niveau de satisfaction individuel.
 */
public class Inhabitant {

    private static int idCounter = 0;

    private final int id;
    private String name;
    private int satisfaction; // 0 à 100
    private final Residence residence;

    public Inhabitant(String name, Residence residence) {
        this.id = ++idCounter;
        this.name = name;
        this.residence = residence;
        // Satisfaction initiale aléatoire entre 50 et 80
        this.satisfaction = new RandomRange(50, 80).getValue();
    }

    public Inhabitant(Residence residence) {
        this("Habitant_" + (idCounter + 1), residence);
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

    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = Math.max(0, Math.min(100, satisfaction));
    }

    /**
     * Modifie la satisfaction de l'habitant.
     * 
     * @param delta Variation (+/-)
     */
    public void adjustSatisfaction(int delta) {
        setSatisfaction(this.satisfaction + delta);
    }

    public Residence getResidence() {
        return residence;
    }

    public boolean isHappy() {
        return satisfaction >= 50;
    }

    public boolean isVeryUnhappy() {
        return satisfaction < 20;
    }

    @Override
    public String toString() {
        return String.format("Habitant[%d] %s (Satisfaction: %d%%)", id, name, satisfaction);
    }
}
