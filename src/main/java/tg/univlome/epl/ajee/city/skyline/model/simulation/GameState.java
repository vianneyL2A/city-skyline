package tg.univlome.epl.ajee.city.skyline.model.simulation;

/**
 * États possibles du jeu.
 */
public enum GameState {

    NOT_STARTED("Non démarré"),
    RUNNING("En cours"),
    PAUSED("En pause"),
    GAME_OVER("Terminé");

    private final String displayName;

    GameState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isPlayable() {
        return this == RUNNING || this == PAUSED;
    }
}
