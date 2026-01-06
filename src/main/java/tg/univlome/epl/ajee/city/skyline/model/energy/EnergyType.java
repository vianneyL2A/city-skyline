package tg.univlome.epl.ajee.city.skyline.model.energy;

/**
 * Types d'énergie disponibles pour les centrales.
 * Chaque type a des caractéristiques différentes.
 */
public enum EnergyType {

    COAL("Charbon", "🏭", 0.8, 0.3), // Haute pollution, fiable
    SOLAR("Solaire", "☀️", 0.0, 0.6), // Écologique, variable
    WIND("Éolien", "🌬️", 0.0, 0.5), // Écologique, variable
    NUCLEAR("Nucléaire", "⚛️", 0.1, 0.9), // Quasi-propre, très fiable
    HYDRO("Hydraulique", "💧", 0.0, 0.85); // Écologique, fiable

    private final String displayName;
    private final String icon;
    private final double pollutionFactor; // 0.0 = propre, 1.0 = très polluant
    private final double reliability; // 0.0 = très variable, 1.0 = constant

    EnergyType(String displayName, String icon, double pollutionFactor, double reliability) {
        this.displayName = displayName;
        this.icon = icon;
        this.pollutionFactor = pollutionFactor;
        this.reliability = reliability;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public double getPollutionFactor() {
        return pollutionFactor;
    }

    public double getReliability() {
        return reliability;
    }

    public boolean isRenewable() {
        return this == SOLAR || this == WIND || this == HYDRO;
    }
}
