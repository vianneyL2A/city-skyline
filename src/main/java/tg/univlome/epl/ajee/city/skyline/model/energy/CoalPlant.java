package tg.univlome.epl.ajee.city.skyline.model.energy;

import tg.univlome.epl.ajee.city.skyline.utils.Constants;

/**
 * Centrale à charbon.
 * Production élevée et fiable mais polluante.
 */
public class CoalPlant extends PowerPlant {

    public CoalPlant(String name) {
        super(name, EnergyType.COAL,
                Constants.COAL_PLANT_BASE_COST,
                Constants.COAL_PLANT_BASE_PRODUCTION,
                Constants.COAL_PLANT_MAINTENANCE);
    }

    public CoalPlant() {
        this("Centrale Charbon");
    }
}
