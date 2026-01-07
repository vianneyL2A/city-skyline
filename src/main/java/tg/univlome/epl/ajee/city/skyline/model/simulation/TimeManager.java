package tg.univlome.epl.ajee.city.skyline.model.simulation;

import tg.univlome.epl.ajee.city.skyline.utils.Constants;

/**
 * Gestionnaire du temps dans le jeu.
 * Gère les cycles temporels (heure, jour, mois, année).
 */
public class TimeManager {

    private int currentHour;
    private int currentMinute;
    private int currentSecond;
    private int currentDay;
    private int currentMonth;
    private int currentYear;
    private TimeCycle currentCycleUnit; // Unité de temps pour chaque tick

    public TimeManager() {
        this.currentHour = 8; // Commence à 8h du matin
        this.currentMinute = 0;
        this.currentSecond = 0;
        this.currentDay = 1;
        this.currentMonth = 1;
        this.currentYear = 1;
        this.currentCycleUnit = TimeCycle.DAY;
    }

    /**
     * Avance le temps d'un cycle (1 heure de jeu par tick).
     * 
     * @return Le type de cycle qui vient de se terminer (YEAR, MONTH, ou DAY)
     */
    public TimeCycle advanceTime() {
        TimeCycle completedCycle = TimeCycle.TICK; // Par défaut, pas de jour complet

        // Avancer de 1h30 (90 minutes) par tick
        currentMinute += 90;

        // Gérer le débordement des minutes
        while (currentMinute >= 60) {
            currentMinute -= 60;
            currentHour++;
        }

        // Gérer le débordement des heures
        while (currentHour >= 24) {
            currentHour -= 24;
            currentDay++;
            completedCycle = TimeCycle.DAY; // Un jour complet est passé
        }

        if (currentDay > Constants.DAYS_PER_MONTH) {
            currentDay = 1;
            currentMonth++;
            completedCycle = TimeCycle.MONTH;

            if (currentMonth > Constants.MONTHS_PER_YEAR) {
                currentMonth = 1;
                currentYear++;
                completedCycle = TimeCycle.YEAR;
            }
        }

        return completedCycle;
    }

    /**
     * Avance de plusieurs jours.
     */
    public void advanceDays(int days) {
        for (int i = 0; i < days; i++) {
            advanceTime();
        }
    }

    public int getCurrentHour() {
        return currentHour;
    }

    public int getCurrentMinute() {
        return currentMinute;
    }

    public int getCurrentSecond() {
        return currentSecond;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    /**
     * Retourne le nombre total de jours écoulés.
     */
    public int getTotalDays() {
        return (currentYear - 1) * Constants.MONTHS_PER_YEAR * Constants.DAYS_PER_MONTH
                + (currentMonth - 1) * Constants.DAYS_PER_MONTH
                + currentDay;
    }

    public TimeCycle getCurrentCycleUnit() {
        return currentCycleUnit;
    }

    public void setCurrentCycleUnit(TimeCycle cycleUnit) {
        this.currentCycleUnit = cycleUnit;
    }

    /**
     * Formate la date et l'heure actuelles en chaîne lisible.
     */
    public String formatDate() {
        return String.format("Jour %d, Mois %d, Année %d - %02d:%02d:%02d",
                currentDay, currentMonth, currentYear,
                currentHour, currentMinute, currentSecond);
    }

    /**
     * Format court de la date avec heure.
     */
    public String formatShortDate() {
        return String.format("J%d M%d A%d %02d:%02d",
                currentDay, currentMonth, currentYear,
                currentHour, currentMinute);
    }

    /**
     * Format de l'heure seulement.
     */
    public String formatTime() {
        return String.format("%02d:%02d:%02d", currentHour, currentMinute, currentSecond);
    }

    @Override
    public String toString() {
        return formatDate();
    }
}
