package tg.univlome.epl.ajee.city.skyline.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface pour les objets observables du jeu.
 * Permet d'enregistrer des observateurs et de les notifier.
 */
public interface GameObservable {

    /**
     * Liste des observateurs (à implémenter dans les classes concrètes).
     */
    List<GameObserver> getObservers();

    /**
     * Ajoute un observateur.
     */
    default void addObserver(GameObserver observer) {
        if (!getObservers().contains(observer)) {
            getObservers().add(observer);
        }
    }

    /**
     * Retire un observateur.
     */
    default void removeObserver(GameObserver observer) {
        getObservers().remove(observer);
    }

    /**
     * Notifie tous les observateurs d'un événement.
     */
    default void notifyObservers(GameEventType eventType, Object data) {
        for (GameObserver observer : new ArrayList<>(getObservers())) {
            observer.onGameEvent(eventType, data);
        }
    }

    /**
     * Notifie tous les observateurs d'un événement sans données.
     */
    default void notifyObservers(GameEventType eventType) {
        notifyObservers(eventType, null);
    }
}
