package tg.univlome.epl.ajee.city.skyline.model.energy;

/**
 * Types d'énergie disponibles pour les centrales.
 * Chaque type a des caractéristiques différentes.
 */
public enum EnergyType {

    COAL("Charbon", "🏭", "plant_coal.png", 0.8, 0.3),
    SOLAR("Solaire", "☀️", "plant_solar.png", 0.0, 0.6),
    WIND("Éolien", "🌬️", "plant_wind.png", 0.0, 0.5),
    NUCLEAR("Nucléaire", "⚛️", "plant_nuclear.png", 0.1, 0.9),
    HYDRO("Hydraulique", "💧", "plant_hydro.png", 0.0, 0.85);

    private final String displayName;
    private final String icon;
    private final String imageName;
    private final double pollutionFactor;
    private final double reliability;

    EnergyType(String displayName, String icon, String imageName, double pollutionFactor, double reliability) {
        this.displayName = displayName;
        this.icon = icon;
        this.imageName = imageName;
        this.pollutionFactor = pollutionFactor;
        this.reliability = reliability;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public String getImageName() {
        return imageName;
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
