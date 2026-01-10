package tg.univlome.epl.ajee.city.skyline.view.styles;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Thème visuel de l'application.
 */
public final class Theme {

    private Theme() {
        // Classe utilitaire
    }

    // === Polices ===
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 14);

    // === Espacement ===
    public static final int PADDING_SMALL = 5;
    public static final int PADDING_MEDIUM = 10;
    public static final int PADDING_LARGE = 20;

    // === Bordures ===
    public static final Border BORDER_EMPTY = new EmptyBorder(0, 0, 0, 0);
    public static final Border BORDER_PADDING_SMALL = new EmptyBorder(PADDING_SMALL, PADDING_SMALL, PADDING_SMALL,
            PADDING_SMALL);
    public static final Border BORDER_PADDING_MEDIUM = new EmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM,
            PADDING_MEDIUM);
    public static final Border BORDER_PADDING_LARGE = new EmptyBorder(PADDING_LARGE, PADDING_LARGE, PADDING_LARGE,
            PADDING_LARGE);

    // === Dimensions ===
    public static final Dimension BUTTON_SIZE = new Dimension(120, 35);
    public static final Dimension BUTTON_LARGE_SIZE = new Dimension(150, 45);
    public static final int CARD_WIDTH = 200;
    public static final int CARD_HEIGHT = 120;

    /**
     * Applique le look and feel système.
     */
    public static void applySystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Utiliser le look par défaut si échec
        }
    }

    /**
     * Crée un bouton stylisé principal.
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBackground(Colors.PRIMARY);
        button.setForeground(Colors.TEXT_ON_PRIMARY);
        button.setFont(FONT_BODY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(BUTTON_SIZE);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Crée un bouton stylisé secondaire.
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBackground(Colors.SECONDARY);
        button.setForeground(Colors.TEXT_ON_PRIMARY);
        button.setFont(FONT_BODY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(BUTTON_SIZE);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Crée un panel avec style de carte.
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Colors.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER, 1),
                BORDER_PADDING_MEDIUM));
        return panel;
    }

    /**
     * Crée un label de titre.
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(Colors.TEXT_PRIMARY);
        return label;
    }

    /**
     * Crée un label de sous-titre.
     */
    public static JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SUBTITLE);
        label.setForeground(Colors.TEXT_PRIMARY);
        return label;
    }

    /**
     * Crée un label d'en-tête.
     */
    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_HEADER);
        label.setForeground(Colors.TEXT_PRIMARY);
        return label;
    }

    /**
     * Crée un label de corps de texte.
     */
    public static JLabel createBodyLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_BODY);
        label.setForeground(Colors.TEXT_PRIMARY);
        return label;
    }
}
