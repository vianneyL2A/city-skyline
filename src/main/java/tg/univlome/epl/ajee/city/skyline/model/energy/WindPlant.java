package tg.univlome.epl.ajee.city.skyline.model.energy;

import tg.univlome.epl.ajee.city.skyline.utils.Constants;

/**
 * Éolienne.
 * Écologique mais production très variable (dépend du vent).
 */
public class WindPlant extends PowerPlant {

    public WindPlant(String name) {
        super(name, EnergyType.WIND,
                Constants.WIND_PLANT_BASE_COST,
                Constants.WIND_PLANT_BASE_PRODUCTION,
                Constants.WIND_PLANT_MAINTENANCE);
    }

    public WindPlant() {
        this("Éolienne");
    }
}
