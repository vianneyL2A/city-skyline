package tg.univlome.epl.ajee.city.skyline;

import tg.univlome.epl.ajee.city.skyline.model.simulation.GameEngine;
import tg.univlome.epl.ajee.city.skyline.view.MainWindow;
import tg.univlome.epl.ajee.city.skyline.view.styles.Theme;

import javax.swing.*;

/**
 * Point d'entrée principal de l'application EnergyTycoon.
 * Jeu de gestion d'énergie - Projet INF2328
 */
public class Main {

    public static void main(String[] args) {
        // Appliquer le look and feel système
        Theme.applySystemLookAndFeel();

        // Lancer l'interface sur le thread Swing
        SwingUtilities.invokeLater(() -> {
            // Créer le moteur de jeu
            GameEngine gameEngine = new GameEngine();

            // Créer et afficher la fenêtre principale
            MainWindow mainWindow = new MainWindow(gameEngine);
            mainWindow.setVisible(true);

            // Message de bienvenue
            JOptionPane.showMessageDialog(mainWindow,
                    "Bienvenue dans EnergyTycoon!\n\n" +
                            "Vous êtes le gestionnaire de l'énergie de cette ville.\n" +
                            "Construisez des centrales pour alimenter les habitants.\n" +
                            "Maintenez le niveau de bonheur au-dessus de 20%.\n\n" +
                            "Cliquez sur 'Jouer' pour commencer!",
                    "🎮 EnergyTycoon",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
