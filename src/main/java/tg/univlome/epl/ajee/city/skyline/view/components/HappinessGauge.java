package tg.univlome.epl.ajee.city.skyline.view.components;

import tg.univlome.epl.ajee.city.skyline.view.styles.Colors;
import tg.univlome.epl.ajee.city.skyline.view.styles.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * Composant jauge de bonheur des habitants.
 */
public class HappinessGauge extends JPanel {

    private int value;
    private final int maxValue;

    public HappinessGauge() {
        this(75, 100);
    }

    public HappinessGauge(int value, int maxValue) {
        this.value = value;
        this.maxValue = maxValue;
        setPreferredSize(new Dimension(200, 60));
        setOpaque(false);
    }

    public void setValue(int value) {
        this.value = Math.max(0, Math.min(maxValue, value));
        repaint();
    }

    public int getValue() {
        return value;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth() - 20;
        int height = 25;
        int x = 10;
        int y = 25;

        // Label
        g2d.setFont(Theme.FONT_SMALL);
        g2d.setColor(Colors.TEXT_PRIMARY);
        g2d.drawString("Bonheur des habitants", x, 15);

        // Fond de la jauge
        g2d.setColor(Colors.BORDER);
        g2d.fillRoundRect(x, y, width, height, 10, 10);

        // Remplissage
        float ratio = (float) value / maxValue;
        int fillWidth = (int) (width * ratio);
        g2d.setColor(Colors.getGaugeColor(value));
        g2d.fillRoundRect(x, y, fillWidth, height, 10, 10);

        // Valeur
        g2d.setColor(Colors.TEXT_ON_PRIMARY);
        g2d.setFont(Theme.FONT_HEADER);
        String text = value + "%";
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (width - fm.stringWidth(text)) / 2;
        int textY = y + ((height - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(text, textX, textY);

        // Indicateur de seuil critique
        int thresholdX = x + (int) (width * 0.2);
        g2d.setColor(Colors.ERROR);
        g2d.drawLine(thresholdX, y - 2, thresholdX, y + height + 2);

        g2d.dispose();
    }
}
