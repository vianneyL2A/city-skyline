package tg.univlome.epl.ajee.city.skyline.model.simulation;

import tg.univlome.epl.ajee.city.skyline.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Gestionnaire des événements aléatoires du jeu.
 */
public class EventManager {

    private static final Random RANDOM = new Random();

    private final List<GameEvent> activeEvents;
    private final List<GameEvent> eventHistory;

    public EventManager() {
        this.activeEvents = new ArrayList<>();
        this.eventHistory = new ArrayList<>();
    }

    /**
     * Vérifie et génère potentiellement un événement aléatoire.
     * 
     * @return L'événement généré ou null
     */
    public GameEvent tryGenerateEvent() {
        if (RANDOM.nextDouble() < Constants.EVENT_PROBABILITY_PER_CYCLE) {
            GameEvent event = generateRandomEvent();
            addEvent(event);
            return event;
        }
        return null;
    }

    /**
     * Génère un événement aléatoire.
     */
    private GameEvent generateRandomEvent() {
        int eventType = RANDOM.nextInt(5);
        return switch (eventType) {
            case 0 -> GameEvent.createStorm();
            case 1 -> GameEvent.createHeatWave();
            case 2 -> GameEvent.createPlantBreakdown();
            case 3 -> GameEvent.createEconomicBoom();
            default -> GameEvent.createDemandSurge();
        };
    }

    /**
     * Ajoute un événement actif.
     */
    public void addEvent(GameEvent event) {
        activeEvents.add(event);
        eventHistory.add(event);
    }

    /**
     * Met à jour tous les événements actifs (avance d'un cycle).
     * 
     * @return Liste des événements qui viennent de se terminer
     */
    public List<GameEvent> updateEvents() {
        List<GameEvent> finishedEvents = new ArrayList<>();

        activeEvents.removeIf(event -> {
            if (event.tick()) {
                finishedEvents.add(event);
                return true;
            }
            return false;
        });

        return finishedEvents;
    }

    /**
     * Calcule le modificateur de production total des événements actifs.
     */
    public double calculateProductionModifier() {
        return activeEvents.stream()
                .mapToDouble(GameEvent::getProductionModifier)
                .reduce(1.0, (a, b) -> a * b);
    }

    /**
     * Calcule l'impact total sur le bonheur des événements actifs.
     */
    public int calculateHappinessImpact() {
        return activeEvents.stream()
                .mapToInt(GameEvent::getHappinessImpact)
                .sum();
    }

    public List<GameEvent> getActiveEvents() {
        return new ArrayList<>(activeEvents);
    }

    public List<GameEvent> getEventHistory() {
        return new ArrayList<>(eventHistory);
    }

    public boolean hasActiveEvents() {
        return !activeEvents.isEmpty();
    }

    public int getActiveEventCount() {
        return activeEvents.size();
    }

    /**
     * Réinitialise les événements pour une nouvelle partie.
     */
    public void reset() {
        this.activeEvents.clear();
        this.eventHistory.clear();
    }
}
