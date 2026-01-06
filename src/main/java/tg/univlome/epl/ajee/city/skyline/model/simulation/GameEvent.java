package tg.univlome.epl.ajee.city.skyline.model.simulation;

/**
 * Représente un événement aléatoire du jeu.
 */
public class GameEvent {

    public enum EventSeverity {
        INFO("Info"),
        WARNING("Attention"),
        CRITICAL("Critique");

        private final String displayName;

        EventSeverity(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum EventCategory {
        WEATHER("Météo"),
        TECHNICAL("Technique"),
        ECONOMIC("Économique"),
        SOCIAL("Social");

        private final String displayName;

        EventCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private final String title;
    private final String description;
    private final EventCategory category;
    private final EventSeverity severity;
    private final int happinessImpact; // Impact sur le bonheur (+/-)
    private final double productionModifier; // Multiplicateur de production
    private final int duration; // Durée en cycles
    private int remainingDuration;

    public GameEvent(String title, String description, EventCategory category,
            EventSeverity severity, int happinessImpact,
            double productionModifier, int duration) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.severity = severity;
        this.happinessImpact = happinessImpact;
        this.productionModifier = productionModifier;
        this.duration = duration;
        this.remainingDuration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public EventCategory getCategory() {
        return category;
    }

    public EventSeverity getSeverity() {
        return severity;
    }

    public int getHappinessImpact() {
        return happinessImpact;
    }

    public double getProductionModifier() {
        return productionModifier;
    }

    public int getDuration() {
        return duration;
    }

    public int getRemainingDuration() {
        return remainingDuration;
    }

    /**
     * Fait avancer l'événement d'un cycle.
     * 
     * @return true si l'événement est terminé
     */
    public boolean tick() {
        remainingDuration--;
        return remainingDuration <= 0;
    }

    public boolean isActive() {
        return remainingDuration > 0;
    }

    // === Factory methods pour créer des événements prédéfinis ===

    public static GameEvent createStorm() {
        return new GameEvent(
                "Tempête",
                "Une tempête réduit la production des éoliennes et panneaux solaires.",
                EventCategory.WEATHER,
                EventSeverity.WARNING,
                -5,
                0.5,
                3);
    }

    public static GameEvent createHeatWave() {
        return new GameEvent(
                "Canicule",
                "La canicule augmente la demande en énergie.",
                EventCategory.WEATHER,
                EventSeverity.WARNING,
                -10,
                1.0,
                5);
    }

    public static GameEvent createPlantBreakdown() {
        return new GameEvent(
                "Panne technique",
                "Une centrale est en panne et nécessite des réparations.",
                EventCategory.TECHNICAL,
                EventSeverity.CRITICAL,
                -15,
                0.7,
                2);
    }

    public static GameEvent createEconomicBoom() {
        return new GameEvent(
                "Boom économique",
                "L'économie locale est en plein essor. Les revenus augmentent.",
                EventCategory.ECONOMIC,
                EventSeverity.INFO,
                10,
                1.0,
                7);
    }

    public static GameEvent createDemandSurge() {
        return new GameEvent(
                "Pic de demande",
                "La demande en énergie augmente soudainement.",
                EventCategory.SOCIAL,
                EventSeverity.WARNING,
                -5,
                1.0,
                4);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (Durée restante: %d cycles)",
                severity.getDisplayName(), title, description, remainingDuration);
    }
}
