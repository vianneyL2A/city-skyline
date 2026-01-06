package tg.univlome.epl.ajee.city.skyline.model.energy;

import tg.univlome.epl.ajee.city.skyline.utils.Constants;

/**
 * Centrale solaire.
 * Écologique mais production variable (dépend du soleil).
 */
public class SolarPlant extends PowerPlant {

    public SolarPlant(String name) {
        super(name, EnergyType.SOLAR,
                Constants.SOLAR_PLANT_BASE_COST,
                Constants.SOLAR_PLANT_BASE_PRODUCTION,
                Constants.SOLAR_PLANT_MAINTENANCE);
    }

    public SolarPlant() {
        this("Centrale Solaire");
    }
}
