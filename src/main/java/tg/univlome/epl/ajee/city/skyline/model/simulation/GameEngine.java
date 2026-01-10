package tg.univlome.epl.ajee.city.skyline.model.simulation;

import tg.univlome.epl.ajee.city.skyline.model.economy.Market;
import tg.univlome.epl.ajee.city.skyline.model.entities.*;
import tg.univlome.epl.ajee.city.skyline.model.energy.PowerPlant;
import tg.univlome.epl.ajee.city.skyline.observer.GameEventType;
import tg.univlome.epl.ajee.city.skyline.observer.GameObservable;
import tg.univlome.epl.ajee.city.skyline.observer.GameObserver;
import tg.univlome.epl.ajee.city.skyline.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Moteur principal du jeu.
 * Gère la boucle de jeu et coordonne tous les systèmes.
 */
public class GameEngine implements GameObservable {

    private final List<GameObserver> observers;
    private final City city;
    private final Player player;
    private final Market market;
    private final TimeManager timeManager;
    private final EventManager eventManager;
    private GameState state;
    private tg.univlome.epl.ajee.city.skyline.model.map.CityMap cityMap;

    public GameEngine() {
        this.observers = new ArrayList<>();
        this.city = new City();
        this.player = new Player();
        this.market = new Market();
        this.timeManager = new TimeManager();
        this.eventManager = new EventManager();
        this.state = GameState.NOT_STARTED;
        this.cityMap = null;
    }

    public void setCityMap(tg.univlome.epl.ajee.city.skyline.model.map.CityMap cityMap) {
        this.cityMap = cityMap;
    }

    public tg.univlome.epl.ajee.city.skyline.model.map.CityMap getCityMap() {
        return cityMap;
    }

    @Override
    public List<GameObserver> getObservers() {
        return observers;
    }

    /**
     * Initialise une nouvelle partie avec configuration par défaut.
     */
    public void initializeGame() {
        // Le joueur commence sans résidence - il doit les construire sur la carte
        // Les résidences seront créées via CityMapPanel.buildResidence()

        state = GameState.RUNNING;
        notifyObservers(GameEventType.GAME_STARTED);
    }

    /**
     * Exécute un cycle de jeu complet.
     */
    public void runCycle() {
        if (state != GameState.RUNNING) {
            return;
        }

        // 1. Avancer le temps
        TimeCycle completedCycle = timeManager.advanceTime();

        // Incrémenter les jours survécus seulement quand un jour complet passe
        if (completedCycle == TimeCycle.DAY || completedCycle == TimeCycle.MONTH || completedCycle == TimeCycle.YEAR) {
            player.incrementDaysSurvived();
        }

        // Notifier selon le cycle complété
        switch (completedCycle) {
            case YEAR -> notifyObservers(GameEventType.YEAR_PASSED);
            case MONTH -> notifyObservers(GameEventType.MONTH_PASSED);
            case DAY -> notifyObservers(GameEventType.DAY_PASSED);
            default -> {
            } // Pas de notification pour les heures/minutes
        }

        // 2. Calculer production et demande
        int production = city.calculateTotalProduction();
        int demand = city.calculateTotalDemand();

        // Appliquer modificateurs d'événements
        double eventModifier = eventManager.calculateProductionModifier();
        production = (int) (production * eventModifier);

        // 3. Mettre à jour le marché
        market.updatePrice(production, demand);

        // 4. Distribuer l'énergie aux résidences
        distributeEnergy(production, demand);

        // 5. Collecter les revenus de vente d'électricité
        int revenue = collectRevenue();
        player.earn(revenue);

        // 6. Collecter les taxes des habitants (seulement si alimentés)
        int taxes = calculateTotalTaxFromMap();
        player.earn(taxes);
        if (taxes > 0) {
            // Enregistrer comme REVENU (pas comme dépense!)
            market.getTransactionHistory().add(new tg.univlome.epl.ajee.city.skyline.model.economy.Transaction(
                    tg.univlome.epl.ajee.city.skyline.model.economy.Transaction.Type.INCOME,
                    taxes,
                    "💰 Taxes collectées",
                    timeManager.getTotalDays()));
        }

        notifyObservers(GameEventType.MONEY_CHANGED, player.getMoney());

        // 7. Payer la maintenance
        int maintenance = city.calculateTotalMaintenance();
        if (!player.spend(maintenance)) {
            // Pas assez d'argent pour la maintenance
            city.adjustGlobalHappiness(-5);
        }
        market.recordExpense(maintenance, "Maintenance des centrales", timeManager.getTotalDays());

        // 8. Mettre à jour le bonheur
        updateHappiness(production, demand);

        // 9. Croissance de la population (les résidences se remplissent au fil du
        // temps)
        if (completedCycle == TimeCycle.DAY || completedCycle == TimeCycle.MONTH || completedCycle == TimeCycle.YEAR) {
            growPopulation();
        }

        // 10. Gérer les événements aléatoires
        GameEvent newEvent = eventManager.tryGenerateEvent();
        if (newEvent != null) {
            notifyObservers(GameEventType.RANDOM_EVENT, newEvent);
        }
        eventManager.updateEvents();

        // 11. Vérifier condition de fin
        checkGameOver();
    }

    /**
     * Distribue l'énergie aux résidences.
     */
    private void distributeEnergy(int production, int demand) {
        // Utiliser les résidences de la carte
        java.util.List<Residence> residences = getResidencesFromMap();

        if (production >= demand) {
            // Assez d'énergie pour tout le monde
            for (Residence residence : residences) {
                residence.setEnergySupplied(true);
            }
        } else {
            // Pas assez d'énergie - distribution proportionnelle
            double ratio = (double) production / demand;
            for (Residence residence : residences) {
                // Les résidences de niveau supérieur ont priorité
                double priority = (residence.getLevel().ordinal() + 1) / 4.0;
                residence.setEnergySupplied(Math.random() < (ratio + priority * 0.2));
            }
            notifyObservers(GameEventType.ENERGY_SHORTAGE, demand - production);
        }
    }

    /**
     * Collecte les revenus de vente d'électricité.
     */
    private int collectRevenue() {
        int totalRevenue = 0;
        int totalEnergySold = 0;

        // Utiliser les résidences de la carte
        java.util.List<Residence> residences = getResidencesFromMap();

        for (Residence residence : residences) {
            if (residence.isEnergySupplied()) {
                int payment = residence.calculateEnergyPayment(market.getCurrentPrice());
                totalRevenue += payment;
                totalEnergySold += residence.getEnergyNeed();
            }
        }
        return market.sellEnergy(totalEnergySold, totalRevenue, timeManager.getTotalDays());
    }

    /**
     * Retourne les résidences de la carte.
     */
    private java.util.List<Residence> getResidencesFromMap() {
        if (cityMap != null) {
            java.util.List<Residence> residences = new java.util.ArrayList<>();
            for (var cell : cityMap.getResidenceCells()) {
                if (cell.getResidence() != null) {
                    residences.add(cell.getResidence());
                }
            }
            return residences;
        }
        return city.getResidences();
    }

    /**
     * Calcule les taxes totales depuis la carte.
     */
    private int calculateTotalTaxFromMap() {
        return getResidencesFromMap().stream()
                .mapToInt(Residence::calculateTax)
                .sum();
    }

    /**
     * Met à jour le niveau de bonheur global.
     */
    private void updateHappiness(int production, int demand) {
        int previousHappiness = city.getGlobalHappiness();

        // Impact de l'approvisionnement énergétique
        if (production >= demand) {
            // Bien alimenté : bonus de satisfaction (plus significatif)
            city.adjustGlobalHappiness(2);
        } else {
            // Déficit : pénalité proportionnelle au manque
            double deficit = 1 - (double) production / demand;
            int penalty = (int) (-deficit * 15);
            city.adjustGlobalHappiness(penalty);
        }

        // Impact des événements (seulement si des événements sont actifs)
        if (eventManager.hasActiveEvents()) {
            int eventImpact = eventManager.calculateHappinessImpact();
            // Limiter l'impact négatif des événements
            int adjustedImpact = Math.max(eventImpact / 20, -2);
            city.adjustGlobalHappiness(adjustedImpact);
        }

        if (city.getGlobalHappiness() != previousHappiness) {
            notifyObservers(GameEventType.HAPPINESS_CHANGED, city.getGlobalHappiness());
        }

        if (city.isHappinessCritical()) {
            notifyObservers(GameEventType.HAPPINESS_CRITICAL, city.getGlobalHappiness());
        }
    }

    /**
     * Fait croître la population dans les résidences.
     * Chaque jour, les résidences qui ne sont pas pleines
     * ont une chance de recevoir un nouvel habitant.
     */
    private void growPopulation() {
        int happiness = city.getGlobalHappiness();

        // Utiliser les résidences de la carte si disponible, sinon celles de City
        java.util.List<Residence> residences;
        if (cityMap != null) {
            residences = new java.util.ArrayList<>();
            for (var cell : cityMap.getResidenceCells()) {
                if (cell.getResidence() != null) {
                    residences.add(cell.getResidence());
                }
            }
        } else {
            residences = city.getResidences();
        }

        for (Residence residence : residences) {
            // Si la résidence est pleine, pas de croissance
            if (residence.isFull()) {
                continue;
            }

            // Probabilité de croissance basée sur le bonheur global
            // Plus le bonheur est élevé, plus la probabilité est grande
            double growthChance = 0.3 + (happiness / 100.0) * 0.5; // 30% à 80% selon le bonheur

            // Bonus si la résidence est alimentée en électricité
            if (residence.isEnergySupplied()) {
                growthChance += 0.2;
            }

            // Bonus pour les résidences de niveau supérieur (plus attractives)
            growthChance += residence.getLevel().ordinal() * 0.1;

            // Cap à 100%
            growthChance = Math.min(1.0, growthChance);

            if (Math.random() < growthChance) {
                residence.addNewInhabitant();
            }
        }
    }

    /**
     * Vérifie si le jeu est terminé.
     */
    private void checkGameOver() {
        if (city.getGlobalHappiness() < Constants.MIN_HAPPINESS_THRESHOLD) {
            state = GameState.GAME_OVER;
            notifyObservers(GameEventType.GAME_OVER,
                    "Les habitants sont trop mécontents. Le maire vous retire la gestion!");
        }

        if (player.getMoney() < 0) {
            state = GameState.GAME_OVER;
            notifyObservers(GameEventType.GAME_OVER, "Vous êtes en faillite!");
        }
    }

    // === Actions du joueur ===

    /**
     * Construit une nouvelle centrale.
     */
    public boolean buildPowerPlant(PowerPlant plant) {
        if (player.canAfford(plant.getBaseCost())) {
            player.spend(plant.getBaseCost());
            city.addPowerPlant(plant);
            market.recordExpense(plant.getBaseCost(), "Construction: " + plant.getName(), timeManager.getTotalDays());
            notifyObservers(GameEventType.PLANT_BUILT, plant);
            notifyObservers(GameEventType.MONEY_CHANGED, player.getMoney());
            return true;
        }
        return false;
    }

    /**
     * Améliore une centrale existante.
     */
    public boolean upgradePowerPlant(PowerPlant plant) {
        int cost = plant.calculateUpgradeCost();
        if (cost > 0 && player.canAfford(cost)) {
            player.spend(cost);
            plant.upgrade();
            market.recordExpense(cost, "Amélioration: " + plant.getName(), timeManager.getTotalDays());
            notifyObservers(GameEventType.PLANT_UPGRADED, plant);
            notifyObservers(GameEventType.MONEY_CHANGED, player.getMoney());
            return true;
        }
        return false;
    }

    /**
     * Améliore une résidence existante.
     */
    public boolean upgradeResidence(Residence residence) {
        if (!residence.getLevel().canUpgrade()) {
            return false;
        }
        int cost = residence.getLevel().getUpgradeCost();
        if (cost > 0 && player.canAfford(cost)) {
            player.spend(cost);
            residence.upgrade();
            market.recordExpense(cost, "Amélioration: " + residence.getName(), timeManager.getTotalDays());
            notifyObservers(GameEventType.RESIDENCE_UPGRADED, residence);
            notifyObservers(GameEventType.MONEY_CHANGED, player.getMoney());
            return true;
        }
        return false;
    }

    // === Contrôle du jeu ===

    public void pause() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
            notifyObservers(GameEventType.GAME_PAUSED);
        }
    }

    public void resume() {
        if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
            notifyObservers(GameEventType.GAME_RESUMED);
        }
    }

    public void togglePause() {
        if (state == GameState.RUNNING) {
            pause();
        } else if (state == GameState.PAUSED) {
            resume();
        }
    }

    // === Getters ===

    public City getCity() {
        return city;
    }

    public Player getPlayer() {
        return player;
    }

    public Market getMarket() {
        return market;
    }

    public TimeManager getTimeManager() {
        return timeManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public GameState getState() {
        return state;
    }

    public boolean isRunning() {
        return state == GameState.RUNNING;
    }

    public boolean isPaused() {
        return state == GameState.PAUSED;
    }

    public boolean isGameOver() {
        return state == GameState.GAME_OVER;
    }
}
