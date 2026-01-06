package tg.univlome.epl.ajee.city.skyline.model.entities;

import tg.univlome.epl.ajee.city.skyline.utils.Constants;

/**
 * Représente le joueur/gestionnaire de l'énergie.
 * Gère les ressources financières et les statistiques.
 */
public class Player {

    private String name;
    private int money;
    private int totalEarnings;
    private int totalSpending;
    private int daysSurvived;

    public Player(String name) {
        this.name = name;
        this.money = Constants.INITIAL_MONEY;
        this.totalEarnings = 0;
        this.totalSpending = 0;
        this.daysSurvived = 0;
    }

    public Player() {
        this("Gestionnaire");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    /**
     * Vérifie si le joueur peut se permettre une dépense.
     */
    public boolean canAfford(int amount) {
        return money >= amount;
    }

    /**
     * Dépense de l'argent.
     * 
     * @return true si la transaction a réussi
     */
    public boolean spend(int amount) {
        if (canAfford(amount)) {
            money -= amount;
            totalSpending += amount;
            return true;
        }
        return false;
    }

    /**
     * Gagne de l'argent.
     */
    public void earn(int amount) {
        money += amount;
        totalEarnings += amount;
    }

    public int getTotalEarnings() {
        return totalEarnings;
    }

    public int getTotalSpending() {
        return totalSpending;
    }

    public int getNetProfit() {
        return totalEarnings - totalSpending;
    }

    public int getDaysSurvived() {
        return daysSurvived;
    }

    public void incrementDaysSurvived() {
        this.daysSurvived++;
    }

    public void addDaysSurvived(int days) {
        this.daysSurvived += days;
    }

    @Override
    public String toString() {
        return String.format("Joueur: %s | Argent: %d€ | Jours: %d", name, money, daysSurvived);
    }
}
