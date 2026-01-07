package tg.univlome.epl.ajee.city.skyline.view;

import tg.univlome.epl.ajee.city.skyline.model.simulation.GameEngine;
import tg.univlome.epl.ajee.city.skyline.observer.GameEventType;
import tg.univlome.epl.ajee.city.skyline.observer.GameObserver;
import tg.univlome.epl.ajee.city.skyline.utils.Constants;
import tg.univlome.epl.ajee.city.skyline.view.components.ResourceBar;
import tg.univlome.epl.ajee.city.skyline.view.components.TimeControlBar;
import tg.univlome.epl.ajee.city.skyline.view.panels.CityMapPanel;
import tg.univlome.epl.ajee.city.skyline.view.panels.DashboardPanel;
import tg.univlome.epl.ajee.city.skyline.view.panels.EconomyPanel;
import tg.univlome.epl.ajee.city.skyline.view.panels.PowerPlantPanel;
import tg.univlome.epl.ajee.city.skyline.view.styles.Colors;
import tg.univlome.epl.ajee.city.skyline.view.styles.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre principale du jeu EnergyTycoon.
 */
public class MainWindow extends JFrame implements GameObserver {

    private final GameEngine gameEngine;
    private final ResourceBar resourceBar;
    private final TimeControlBar timeControlBar;
    private final JTabbedPane tabbedPane;
    private final DashboardPanel dashboardPanel;
    private final PowerPlantPanel powerPlantPanel;
    private final CityMapPanel cityMapPanel;
    private final EconomyPanel economyPanel;
    private Timer gameTimer;

    public MainWindow(GameEngine gameEngine) {
        super(Constants.GAME_TITLE);
        this.gameEngine = gameEngine;

        // Configuration de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setMinimumSize(new Dimension(1024, 600));
        setLocationRelativeTo(null);

        // Layout principal
        setLayout(new BorderLayout());

        // Barre de ressources (haut)
        resourceBar = new ResourceBar();
        add(resourceBar, BorderLayout.NORTH);

        // Panneau central avec onglets
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Theme.FONT_BODY);

        // Carte de la ville (premier onglet)
        cityMapPanel = new CityMapPanel(gameEngine);
        tabbedPane.addTab("🗺️ Carte", cityMapPanel);

        dashboardPanel = new DashboardPanel(gameEngine);
        tabbedPane.addTab("📊 Tableau de bord", dashboardPanel);

        powerPlantPanel = new PowerPlantPanel(gameEngine);
        tabbedPane.addTab("⚡ Centrales", powerPlantPanel);

        // Panneau Économie
        economyPanel = new EconomyPanel(gameEngine);
        tabbedPane.addTab("💰 Économie", economyPanel);

        // Placeholder pour d'autres onglets
        JPanel residencesPanel = createPlaceholderPanel("🏠 Gestion des résidences - À venir");
        tabbedPane.addTab("🏠 Résidences", residencesPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Barre de contrôle du temps (bas)
        timeControlBar = new TimeControlBar();
        timeControlBar.setPlayPauseAction(e -> toggleGame());
        add(timeControlBar, BorderLayout.SOUTH);

        // S'enregistrer comme observateur
        gameEngine.addObserver(this);

        // Timer pour la boucle de jeu
        setupGameTimer();

        // Mise à jour initiale
        refreshAll();
    }

    private JPanel createPlaceholderPanel(String message) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Colors.BACKGROUND);
        JLabel label = Theme.createSubtitleLabel(message);
        label.setForeground(Colors.TEXT_SECONDARY);
        panel.add(label);
        return panel;
    }

    private void setupGameTimer() {
        gameTimer = new Timer(timeControlBar.getTickDelay(), e -> {
            if (gameEngine.isRunning()) {
                gameEngine.runCycle();
                refreshAll();
            }
        });
    }

    private void toggleGame() {
        if (gameEngine.isPaused() || gameEngine.getState().name().equals("NOT_STARTED")) {
            if (gameEngine.getState().name().equals("NOT_STARTED")) {
                gameEngine.initializeGame();
            }
            gameEngine.resume();
            gameTimer.setDelay(timeControlBar.getTickDelay());
            gameTimer.start();
            timeControlBar.setPaused(false);
        } else {
            gameEngine.pause();
            gameTimer.stop();
            timeControlBar.setPaused(true);
        }
    }

    /**
     * Rafraîchit toute l'interface.
     */
    public void refreshAll() {
        // Mettre à jour la barre de ressources
        resourceBar.setMoney(gameEngine.getPlayer().getMoney());
        resourceBar.setDate(gameEngine.getTimeManager().formatDate());
        resourceBar.updateBalance(
                gameEngine.getCity().calculateTotalProduction(),
                gameEngine.getCity().calculateTotalDemand());
        resourceBar.setHappiness(gameEngine.getCity().getGlobalHappiness());
        resourceBar.setInhabitants(gameEngine.getCity().getTotalInhabitants());

        // Mettre à jour l'onglet actif
        Component activeTab = tabbedPane.getSelectedComponent();
        if (activeTab == cityMapPanel) {
            cityMapPanel.refresh();
        } else if (activeTab == dashboardPanel) {
            dashboardPanel.refresh();
        } else if (activeTab == powerPlantPanel) {
            powerPlantPanel.refresh();
        } else if (activeTab == economyPanel) {
            economyPanel.refresh();
        }

        // Mettre à jour la vitesse du timer
        if (gameTimer.getDelay() != timeControlBar.getTickDelay()) {
            gameTimer.setDelay(timeControlBar.getTickDelay());
        }
    }

    @Override
    public void onGameEvent(GameEventType eventType, Object data) {
        SwingUtilities.invokeLater(() -> {
            switch (eventType) {
                case GAME_OVER -> showGameOverDialog((String) data);
                case RANDOM_EVENT -> showEventNotification(data);
                case HAPPINESS_CRITICAL -> showWarningNotification(
                        "⚠️ Attention!", "Le niveau de bonheur est critique! Les habitants sont mécontents.");
                default -> refreshAll();
            }
        });
    }

    private void showGameOverDialog(String message) {
        gameTimer.stop();
        JOptionPane.showMessageDialog(this,
                message + "\n\nJours survécus: " + gameEngine.getPlayer().getDaysSurvived(),
                "Game Over",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showEventNotification(Object data) {
        if (data != null) {
            JOptionPane.showMessageDialog(this,
                    data.toString(),
                    "📢 Événement",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showWarningNotification(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }
}
