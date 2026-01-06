package tg.univlome.epl.ajee.city.skyline.model.energy;

import tg.univlome.epl.ajee.city.skyline.utils.Constants;

/**
 * Centrale nucléaire.
 * Production massive et très fiable mais coût élevé.
 */
public class NuclearPlant extends PowerPlant {

    public NuclearPlant(String name) {
        super(name, EnergyType.NUCLEAR,
                Constants.NUCLEAR_PLANT_BASE_COST,
                Constants.NUCLEAR_PLANT_BASE_PRODUCTION,
                Constants.NUCLEAR_PLANT_MAINTENANCE);
    }

    public NuclearPlant() {
        this("Centrale Nucléaire");
    }
}
