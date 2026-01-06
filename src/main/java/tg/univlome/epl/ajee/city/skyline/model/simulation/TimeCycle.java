package tg.univlome.epl.ajee.city.skyline.model.simulation;

/**
 * Cycles de temps dans le jeu.
 */
public enum TimeCycle {

    DAY("Jour", 1),
    MONTH("Mois", 30),
    YEAR("Année", 365);

    private final String displayName;
    private final int daysEquivalent;

    TimeCycle(String displayName, int daysEquivalent) {
        this.displayName = displayName;
        this.daysEquivalent = daysEquivalent;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getDaysEquivalent() {
        return daysEquivalent;
    }
}
