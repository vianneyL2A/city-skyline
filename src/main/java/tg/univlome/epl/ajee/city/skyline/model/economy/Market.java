package tg.univlome.epl.ajee.city.skyline.model.economy;

import tg.univlome.epl.ajee.city.skyline.model.config.RandomRange;
import tg.univlome.epl.ajee.city.skyline.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Marché de l'électricité.
 * Gère le prix de vente et l'historique des transactions.
 */
public class Market {

    private double currentPrice; // Prix actuel par kWh
    private final List<Transaction> transactionHistory;
    private int totalEnergySold;
    private int totalRevenue;

    public Market() {
        this.currentPrice = Constants.BASE_ELECTRICITY_PRICE;
        this.transactionHistory = new ArrayList<>();
        this.totalEnergySold = 0;
        this.totalRevenue = 0;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * Met à jour le prix en fonction de l'offre et la demande.
     * 
     * @param supply Production totale
     * @param demand Demande totale
     */
    public void updatePrice(int supply, int demand) {
        double ratio = demand > 0 ? (double) supply / demand : 1.0;

        // Plus la demande dépasse l'offre, plus le prix monte
        if (ratio < 0.5) {
            // Pénurie grave : prix monte beaucoup
            currentPrice = Constants.BASE_ELECTRICITY_PRICE * 1.5;
        } else if (ratio < 0.8) {
            // Léger déficit : prix monte un peu
            currentPrice = Constants.BASE_ELECTRICITY_PRICE * 1.2;
        } else if (ratio > 1.5) {
            // Gros surplus : prix baisse
            currentPrice = Constants.BASE_ELECTRICITY_PRICE * 0.8;
        } else if (ratio > 1.2) {
            // Léger surplus : prix baisse un peu
            currentPrice = Constants.BASE_ELECTRICITY_PRICE * 0.9;
        } else {
            // Équilibre : prix normal avec petite fluctuation
            double fluctuation = new RandomRange(-5, 5).getValue() / 100.0;
            currentPrice = Constants.BASE_ELECTRICITY_PRICE * (1 + fluctuation);
        }
    }

    /**
     * Enregistre une vente d'électricité.
     * 
     * @param energySold Quantité d'énergie vendue en kWh
     * @param revenue    Montant du revenu en €
     * @param dayInGame  Jour en jeu
     */
    public int sellEnergy(int energySold, int revenue, int dayInGame) {
        totalEnergySold += energySold;
        totalRevenue += revenue;

        transactionHistory.add(new Transaction(
                Transaction.Type.INCOME,
                revenue,
                String.format("Vente de %d kWh", energySold),
                dayInGame));

        return revenue;
    }

    /**
     * Enregistre une dépense.
     */
    public void recordExpense(int amount, String description, int dayInGame) {
        transactionHistory.add(new Transaction(
                Transaction.Type.EXPENSE,
                amount,
                description,
                dayInGame));
    }

    /**
     * Enregistre un revenu (taxes, etc.).
     */
    public void recordIncome(int amount, String description, int dayInGame) {
        transactionHistory.add(new Transaction(
                Transaction.Type.INCOME,
                amount,
                description,
                dayInGame));
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public List<Transaction> getRecentTransactions(int count) {
        int size = transactionHistory.size();
        int from = Math.max(0, size - count);
        return new ArrayList<>(transactionHistory.subList(from, size));
    }

    public int getTotalEnergySold() {
        return totalEnergySold;
    }

    public int getTotalRevenue() {
        return totalRevenue;
    }

    public String formatPrice() {
        return String.format("%.2f€/kWh", currentPrice);
    }

    /**
     * Réinitialise le marché pour une nouvelle partie.
     */
    public void reset() {
        this.currentPrice = Constants.BASE_ELECTRICITY_PRICE;
        this.transactionHistory.clear();
        this.totalEnergySold = 0;
        this.totalRevenue = 0;
    }

    @Override
    public String toString() {
        return String.format("Marché | Prix: %s | Total vendu: %d kWh | Revenus: %d€",
                formatPrice(), totalEnergySold, totalRevenue);
    }
}
