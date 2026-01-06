package tg.univlome.epl.ajee.city.skyline.observer;

/**
 * Interface pour les observateurs du jeu.
 * Implémente le pattern Observer pour mettre à jour les vues.
 */
public interface GameObserver {

    /**
     * Appelée quand un événement se produit dans le jeu.
     * 
     * @param eventType Type de l'événement
     * @param data      Données associées à l'événement (peut être null)
     */
    void onGameEvent(GameEventType eventType, Object data);
}
