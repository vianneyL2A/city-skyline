package tg.univlome.epl.ajee.city.skyline.view.styles;

import java.awt.Color;

/**
 * Palette de couleurs pour l'interface du jeu.
 */
public final class Colors {

    private Colors() {
        // Classe utilitaire
    }

    // === Couleurs principales ===
    public static final Color PRIMARY = new Color(46, 125, 50); // Vert énergie
    public static final Color PRIMARY_DARK = new Color(27, 94, 32);
    public static final Color PRIMARY_LIGHT = new Color(129, 199, 132);

    public static final Color SECONDARY = new Color(255, 152, 0); // Orange
    public static final Color SECONDARY_DARK = new Color(245, 124, 0);
    public static final Color SECONDARY_LIGHT = new Color(255, 183, 77);

    // === Couleurs d'arrière-plan ===
    public static final Color BACKGROUND = new Color(245, 245, 245);
    public static final Color BACKGROUND_DARK = new Color(33, 33, 33);
    public static final Color SURFACE = Color.WHITE;
    public static final Color SURFACE_DARK = new Color(50, 50, 50);

    // === Couleurs de texte ===
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117);
    public static final Color TEXT_ON_PRIMARY = Color.WHITE;
    public static final Color TEXT_ON_DARK = Color.WHITE;

    // === Couleurs d'état ===
    public static final Color SUCCESS = new Color(76, 175, 80);
    public static final Color WARNING = new Color(255, 193, 7);
    public static final Color ERROR = new Color(244, 67, 54);
    public static final Color INFO = new Color(33, 150, 243);

    // === Couleurs des types d'énergie ===
    public static final Color ENERGY_COAL = new Color(97, 97, 97);
    public static final Color ENERGY_SOLAR = new Color(255, 235, 59);
    public static final Color ENERGY_WIND = new Color(129, 212, 250);
    public static final Color ENERGY_NUCLEAR = new Color(156, 39, 176);
    public static final Color ENERGY_HYDRO = new Color(3, 169, 244);

    // === Couleurs des jauges ===
    public static final Color GAUGE_LOW = ERROR;
    public static final Color GAUGE_MEDIUM = WARNING;
    public static final Color GAUGE_HIGH = SUCCESS;

    // === Bordures ===
    public static final Color BORDER = new Color(224, 224, 224);
    public static final Color BORDER_DARK = new Color(66, 66, 66);

    /**
     * Retourne une couleur interpolée entre deux couleurs.
     */
    public static Color interpolate(Color c1, Color c2, float ratio) {
        ratio = Math.max(0, Math.min(1, ratio));
        int r = (int) (c1.getRed() + (c2.getRed() - c1.getRed()) * ratio);
        int g = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * ratio);
        int b = (int) (c1.getBlue() + (c2.getBlue() - c1.getBlue()) * ratio);
        return new Color(r, g, b);
    }

    /**
     * Retourne une couleur de jauge selon la valeur (0-100).
     */
    public static Color getGaugeColor(int value) {
        if (value < 30) {
            return GAUGE_LOW;
        } else if (value < 60) {
            return GAUGE_MEDIUM;
        } else {
            return GAUGE_HIGH;
        }
    }
}
