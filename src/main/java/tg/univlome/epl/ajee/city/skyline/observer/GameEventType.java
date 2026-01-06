package tg.univlome.epl.ajee.city.skyline.observer;

/**
 * Types d'événements observables dans le jeu.
 */
public enum GameEventType {

    // Événements temporels
    DAY_PASSED("Jour passé"),
    MONTH_PASSED("Mois passé"),
    YEAR_PASSED("Année passée"),

    // Événements énergétiques
    ENERGY_PRODUCTION_CHANGED("Production modifiée"),
    ENERGY_SHORTAGE("Pénurie d'énergie"),
    ENERGY_SURPLUS("Surplus d'énergie"),

    // Événements de centrales
    PLANT_BUILT("Centrale construite"),
    PLANT_UPGRADED("Centrale améliorée"),
    PLANT_DESTROYED("Centrale détruite"),
    PLANT_BREAKDOWN("Panne de centrale"),

    // Événements économiques
    MONEY_CHANGED("Argent modifié"),
    TRANSACTION_COMPLETED("Transaction effectuée"),
    PRICE_CHANGED("Prix modifié"),

    // Événements de population
    HAPPINESS_CHANGED("Bonheur modifié"),
    HAPPINESS_CRITICAL("Bonheur critique"),
    INHABITANT_ARRIVED("Nouvel habitant"),
    INHABITANT_LEFT("Habitant parti"),

    // Événements de résidences
    RESIDENCE_ADDED("Résidence ajoutée"),
    RESIDENCE_UPGRADED("Résidence améliorée"),

    // Événements spéciaux
    RANDOM_EVENT("Événement aléatoire"),
    GAME_OVER("Fin de partie"),
    GAME_STARTED("Partie commencée"),
    GAME_PAUSED("Partie en pause"),
    GAME_RESUMED("Partie reprise");

    private final String displayName;

    GameEventType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
