package tg.univlome.epl.ajee.city.skyline.utils;

/**
 * Constantes du jeu EnergyTycoon.
 */
public final class Constants {

    private Constants() {
        // Classe utilitaire, pas d'instanciation
    }

    // === Ressources initiales ===
    public static final int INITIAL_MONEY = 10000;
    public static final int INITIAL_HAPPINESS = 75;

    // === Seuils ===
    public static final int MIN_HAPPINESS_THRESHOLD = 20; // Game Over si en dessous
    public static final int MAX_HAPPINESS = 100;

    // === Coûts de base des centrales ===
    public static final int COAL_PLANT_BASE_COST = 1000;
    public static final int SOLAR_PLANT_BASE_COST = 1500;
    public static final int WIND_PLANT_BASE_COST = 1200;
    public static final int NUCLEAR_PLANT_BASE_COST = 5000;
    public static final int HYDRO_PLANT_BASE_COST = 3000;

    // === Production de base des centrales (kWh/cycle) ===
    public static final int COAL_PLANT_BASE_PRODUCTION = 500;
    public static final int SOLAR_PLANT_BASE_PRODUCTION = 200;
    public static final int WIND_PLANT_BASE_PRODUCTION = 250;
    public static final int NUCLEAR_PLANT_BASE_PRODUCTION = 1000;
    public static final int HYDRO_PLANT_BASE_PRODUCTION = 400;

    // === Coûts de maintenance (par cycle) ===
    public static final int COAL_PLANT_MAINTENANCE = 100;
    public static final int SOLAR_PLANT_MAINTENANCE = 20;
    public static final int WIND_PLANT_MAINTENANCE = 30;
    public static final int NUCLEAR_PLANT_MAINTENANCE = 200;
    public static final int HYDRO_PLANT_MAINTENANCE = 50;

    // === Coût d'amélioration (multiplicateur du coût de base) ===
    public static final double UPGRADE_COST_MULTIPLIER = 0.6;

    // === Économie ===
    public static final double BASE_ELECTRICITY_PRICE = 0.15; // Prix de base par kWh
    public static final double PRICE_FLUCTUATION = 0.05; // Variation de prix

    // === Temps ===
    public static final int GAME_TICK_MS = 1000; // Durée d'un tick en millisecondes
    public static final int DAYS_PER_MONTH = 30;
    public static final int MONTHS_PER_YEAR = 12;

    // === Interface ===
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final String GAME_TITLE = "EnergyTycoon - Gestionnaire d'Énergie";

    // === Événements aléatoires ===
    public static final double EVENT_PROBABILITY_PER_CYCLE = 0.1; // 10% de chance par cycle
}
