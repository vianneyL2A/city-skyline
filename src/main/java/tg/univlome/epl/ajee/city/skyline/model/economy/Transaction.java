package tg.univlome.epl.ajee.city.skyline.model.economy;

import java.time.LocalDateTime;

/**
 * Représente une transaction financière.
 */
public class Transaction {

    public enum Type {
        INCOME("Revenu"),
        EXPENSE("Dépense");

        private final String displayName;

        Type(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private final Type type;
    private final int amount;
    private final String description;
    private final LocalDateTime timestamp;
    private final int dayInGame;

    public Transaction(Type type, int amount, String description, int dayInGame) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
        this.dayInGame = dayInGame;
    }

    public Type getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getDayInGame() {
        return dayInGame;
    }

    public boolean isIncome() {
        return type == Type.INCOME;
    }

    public boolean isExpense() {
        return type == Type.EXPENSE;
    }

    /**
     * Retourne le montant signé (+/- selon le type).
     */
    public int getSignedAmount() {
        return isIncome() ? amount : -amount;
    }

    @Override
    public String toString() {
        String sign = isIncome() ? "+" : "-";
        return String.format("[Jour %d] %s%d€ - %s", dayInGame, sign, amount, description);
    }
}
