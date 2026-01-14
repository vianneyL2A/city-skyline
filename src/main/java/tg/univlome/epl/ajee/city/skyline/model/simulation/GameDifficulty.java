package tg.univlome.epl.ajee.city.skyline.model.simulation;

import tg.univlome.epl.ajee.city.skyline.utils.Constants;

/**
 * Niveaux de difficulté du jeu.
 */
public enum GameDifficulty {

    EASY("Facile", 15000, 85, 3),
    NORMAL("Normal", Constants.INITIAL_MONEY, Constants.INITIAL_HAPPINESS, Constants.MIN_HAPPINESS_THRESHOLD),
    HARD("Difficile", 5000, 60, 10);

    private final String displayName;
    private final int initialMoney;
    private final int initialHappiness;
    private final int gameOverThreshold;

    GameDifficulty(String displayName, int initialMoney, int initialHappiness, int gameOverThreshold) {
        this.displayName = displayName;
        this.initialMoney = initialMoney;
        this.initialHappiness = initialHappiness;
        this.gameOverThreshold = gameOverThreshold;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getInitialMoney() {
        return initialMoney;
    }

    public int getInitialHappiness() {
        return initialHappiness;
    }

    public int getGameOverThreshold() {
        return gameOverThreshold;
    }
}
