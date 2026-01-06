package tg.univlome.epl.ajee.city.skyline.model.energy;

import tg.univlome.epl.ajee.city.skyline.utils.Constants;

/**
 * Centrale hydraulique.
 * Écologique et assez fiable, dépend des cours d'eau.
 */
public class HydroPlant extends PowerPlant {

    public HydroPlant(String name) {
        super(name, EnergyType.HYDRO,
                Constants.HYDRO_PLANT_BASE_COST,
                Constants.HYDRO_PLANT_BASE_PRODUCTION,
                Constants.HYDRO_PLANT_MAINTENANCE);
    }

    public HydroPlant() {
        this("Centrale Hydraulique");
    }
}
