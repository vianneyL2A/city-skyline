package tg.univlome.epl.ajee.city.skyline.model.config;

import java.util.Random;

/**
 * Utilitaire pour gérer des intervalles de valeurs aléatoires.
 * Permet d'éviter un jeu déterministe en assignant des valeurs
 * dans un intervalle plutôt que des valeurs fixes.
 */
public class RandomRange {

    private static final Random RANDOM = new Random();

    private final int min;
    private final int max;

    public RandomRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min doit être inférieur ou égal à max");
        }
        this.min = min;
        this.max = max;
    }

    /**
     * Génère une valeur aléatoire dans l'intervalle [min, max].
     */
    public int getValue() {
        if (min == max) {
            return min;
        }
        return RANDOM.nextInt(max - min + 1) + min;
    }

    /**
     * Génère une valeur double aléatoire dans l'intervalle.
     */
    public double getDoubleValue() {
        return min + RANDOM.nextDouble() * (max - min);
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    /**
     * Retourne la valeur moyenne de l'intervalle.
     */
    public double getAverage() {
        return (min + max) / 2.0;
    }

    @Override
    public String toString() {
        return String.format("[%d - %d]", min, max);
    }
}
