package tg.univlome.epl.ajee.city.skyline;

import tg.univlome.epl.ajee.city.skyline.model.simulation.GameDifficulty;
import tg.univlome.epl.ajee.city.skyline.model.simulation.GameEngine;
import tg.univlome.epl.ajee.city.skyline.view.panels.GamePanel;
import tg.univlome.epl.ajee.city.skyline.view.panels.MenuPanel;
import tg.univlome.epl.ajee.city.skyline.view.styles.Theme;
import tg.univlome.epl.ajee.city.skyline.utils.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * Point d'entrée principal de l'application CitySkyline.
 * Gère la navigation entre le menu et le jeu.
 */
public class Main {

    private static JFrame mainFrame;
    private static JPanel containerPanel;
    private static CardLayout cardLayout;
    private static MenuPanel menuPanel;
    private static GamePanel gamePanel;
    private static GameEngine gameEngine;

    public static void main(String[] args) {
        // Appliquer le look and feel système
        Theme.applySystemLookAndFeel();

        // Lancer l'interface sur le thread Swing
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // Créer la fenêtre principale
        mainFrame = new JFrame(Constants.GAME_TITLE);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        mainFrame.setMinimumSize(new Dimension(1024, 600));
        mainFrame.setLocationRelativeTo(null);

        // Layout avec CardLayout pour basculer entre menu et jeu
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        // Créer le panel de menu
        menuPanel = new MenuPanel();
        menuPanel.setOnStartGame(e -> startGame());
        containerPanel.add(menuPanel, "MENU");

        // Créer le moteur de jeu
        gameEngine = new GameEngine();

        // Ajouter le container à la fenêtre
        mainFrame.add(containerPanel);

        // Afficher le menu
        cardLayout.show(containerPanel, "MENU");
        mainFrame.setVisible(true);
    }

    /**
     * Démarre le jeu avec le niveau sélectionné.
     */
    private static void startGame() {
        // Récupérer la difficulté sélectionnée
        GameDifficulty difficulty = menuPanel.getSelectedDifficulty();

        gameEngine.setDifficulty(difficulty);
        gameEngine.reset();

        // Supprimer l'ancien panneau de jeu si existant
        if (gamePanel != null) {
            gamePanel.stopTimer();
            containerPanel.remove(gamePanel);
        }

        // Créer le nouveau panneau de jeu
        gamePanel = new GamePanel(gameEngine, Main::showMenu);
        containerPanel.add(gamePanel, "GAME");

        // Basculer vers le jeu
        cardLayout.show(containerPanel, "GAME");
    }

    /**
     * Retourne au menu principal.
     */
    public static void showMenu() {
        // Arrêter le jeu en cours
        if (gamePanel != null) {
            gamePanel.stopTimer();
        }
        if (gameEngine != null) {
            gameEngine.pause();
        }

        // Afficher le menu
        cardLayout.show(containerPanel, "MENU");
    }
}
