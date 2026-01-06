package tg.univlome.epl.ajee.city.skyline.model.economy;

/**
 * Représente les ressources financières (monnaie du jeu).
 */
public class Resource {

    private int amount;
    private final String name;
    private final String symbol;

    public Resource(String name, String symbol, int initialAmount) {
        this.name = name;
        this.symbol = symbol;
        this.amount = initialAmount;
    }

    public Resource(int initialAmount) {
        this("Crédits", "€", initialAmount);
    }

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean canAfford(int cost) {
        return amount >= cost;
    }

    public boolean spend(int cost) {
        if (canAfford(cost)) {
            amount -= cost;
            return true;
        }
        return false;
    }

    public void add(int value) {
        amount += value;
    }

    public String format() {
        return String.format("%d%s", amount, symbol);
    }

    @Override
    public String toString() {
        return format();
    }
}
