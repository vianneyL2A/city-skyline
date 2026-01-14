package tg.univlome.epl.ajee.city.skyline.view.panels;

import tg.univlome.epl.ajee.city.skyline.model.simulation.GameDifficulty;
import tg.univlome.epl.ajee.city.skyline.view.styles.Colors;
import tg.univlome.epl.ajee.city.skyline.view.styles.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Panneau du menu principal du jeu avec design amélioré.
 */
public class MenuPanel extends JPanel {

    private GameDifficulty selectedDifficulty = GameDifficulty.NORMAL;
    private final JButton startButton;
    private final JButton[] difficultyButtons;
    private final JLabel infoLabel;
    private ActionListener onStartGame;

    // Couleurs du thème
    private static final Color BG_DARK = new Color(15, 23, 42);
    private static final Color BG_CARD = new Color(30, 41, 59);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);
    private static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private static final Color ACCENT_RED = new Color(239, 68, 68);
    private static final Color TEXT_WHITE = new Color(248, 250, 252);
    private static final Color TEXT_GRAY = new Color(148, 163, 184);

    public MenuPanel() {
        setLayout(new GridBagLayout());
        setBackground(BG_DARK);

        // Panel central avec carte
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(BG_CARD);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                new EmptyBorder(40, 50, 40, 50)));

        // === Logo / Icône ===
        JLabel iconLabel = new JLabel("🏙️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(iconLabel);
        cardPanel.add(Box.createVerticalStrut(10));

        // === Titre ===
        JLabel titleLabel = new JLabel("CitySkyline");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(titleLabel);

        // === Sous-titre ===
        JLabel subtitleLabel = new JLabel("Gestionnaire d'Énergie");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        subtitleLabel.setForeground(TEXT_GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(subtitleLabel);

        cardPanel.add(Box.createVerticalStrut(35));

        // === Séparateur ===
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(300, 1));
        separator.setForeground(new Color(51, 65, 85));
        cardPanel.add(separator);

        cardPanel.add(Box.createVerticalStrut(25));

        // === Label difficulté ===
        JLabel difficultyLabel = new JLabel("Choisissez votre niveau");
        difficultyLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        difficultyLabel.setForeground(TEXT_WHITE);
        difficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(difficultyLabel);

        cardPanel.add(Box.createVerticalStrut(15));

        // === Boutons de difficulté ===
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        difficultyPanel.setOpaque(false);
        difficultyPanel.setMaximumSize(new Dimension(400, 50));

        difficultyButtons = new JButton[GameDifficulty.values().length];
        Color[] diffColors = { ACCENT_GREEN, ACCENT_BLUE, ACCENT_RED };
        String[] icons = { "🌱", "⚡", "🔥" };

        for (int i = 0; i < GameDifficulty.values().length; i++) {
            GameDifficulty diff = GameDifficulty.values()[i];
            JButton btn = createDifficultyButton(diff, icons[i], diffColors[i]);
            difficultyButtons[i] = btn;
            difficultyPanel.add(btn);
        }
        cardPanel.add(difficultyPanel);

        cardPanel.add(Box.createVerticalStrut(20));

        // === Info niveau sélectionné ===
        infoLabel = new JLabel();
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoLabel.setForeground(TEXT_GRAY);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(infoLabel);

        cardPanel.add(Box.createVerticalStrut(30));

        // === Bouton Start ===
        startButton = createStartButton();
        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        startPanel.setOpaque(false);
        startPanel.add(startButton);
        cardPanel.add(startPanel);

        cardPanel.add(Box.createVerticalStrut(20));

        // === Footer ===
        JLabel footerLabel = new JLabel("Projet INF2328 • 2026");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(100, 116, 139));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(footerLabel);

        add(cardPanel);

        // Mettre à jour l'affichage initial
        updateDifficultySelection();
    }

    private JButton createDifficultyButton(GameDifficulty difficulty, String icon, Color accentColor) {
        JButton btn = new JButton(icon + " " + difficulty.getDisplayName()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (selectedDifficulty == difficulty) {
                    g2.setColor(accentColor);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(Color.WHITE);
                } else {
                    g2.setColor(new Color(51, 65, 85));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(new Color(71, 85, 105));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                    g2.setColor(TEXT_GRAY);
                }

                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(120, 38));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            selectedDifficulty = difficulty;
            updateDifficultySelection();
        });

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.repaint();
            }
        });

        return btn;
    }

    private JButton createStartButton() {
        JButton btn = new JButton("🎮  Démarrer la partie") {
            private boolean hover = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dégradé
                Color startColor = hover ? ACCENT_GREEN.brighter() : ACCENT_GREEN;
                Color endColor = hover ? new Color(22, 163, 74).brighter() : new Color(22, 163, 74);
                GradientPaint gp = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                // Ombre interne légère
                if (hover) {
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() / 2, 14, 14);
                }

                // Texte
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(280, 55));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            if (onStartGame != null) {
                onStartGame.actionPerformed(e);
            }
        });

        return btn;
    }

    private void updateDifficultySelection() {
        // Repaint les boutons
        for (JButton btn : difficultyButtons) {
            btn.repaint();
        }

        // Mettre à jour le label d'info avec des icônes - sur une seule ligne
        infoLabel.setText(String.format(
                "💰 %d€   •   😊 %d%%   •   ⚠️ Game Over à %d%%",
                selectedDifficulty.getInitialMoney(),
                selectedDifficulty.getInitialHappiness(),
                selectedDifficulty.getGameOverThreshold()));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public GameDifficulty getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public void setOnStartGame(ActionListener listener) {
        this.onStartGame = listener;
    }
}
