package tg.univlome.epl.ajee.city.skyline.view.components;

import tg.univlome.epl.ajee.city.skyline.view.styles.Colors;
import tg.univlome.epl.ajee.city.skyline.view.styles.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Barre de contrôle du temps (pause, play, vitesse).
 */
public class TimeControlBar extends JPanel {

    private final JButton playPauseButton;
    private final JButton speedUpButton;
    private final JButton speedDownButton;
    private final JLabel speedLabel;
    private boolean isPaused;
    private int speed; // 1 = normal, 2 = rapide, 3 = très rapide

    public TimeControlBar() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 8));
        setBackground(Colors.SURFACE);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Colors.BORDER));
        setPreferredSize(new Dimension(0, 55));

        this.isPaused = true;
        this.speed = 1;

        // Bouton ralentir
        speedDownButton = new JButton("<<");
        speedDownButton.setFont(Theme.FONT_BODY);
        speedDownButton.setPreferredSize(new Dimension(60, 35));
        speedDownButton.setToolTipText("Ralentir");
        add(speedDownButton);

        // Bouton play/pause - PLUS VISIBLE
        playPauseButton = new JButton("JOUER");
        playPauseButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        playPauseButton.setPreferredSize(new Dimension(120, 40));
        playPauseButton.setBackground(Colors.PRIMARY);
        playPauseButton.setForeground(Color.BLACK);
        playPauseButton.setFocusPainted(false);
        playPauseButton.setBorderPainted(true);
        playPauseButton.setOpaque(true);
        playPauseButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(playPauseButton);

        // Bouton accélérer
        speedUpButton = new JButton(">>");
        speedUpButton.setFont(Theme.FONT_BODY);
        speedUpButton.setPreferredSize(new Dimension(60, 35));
        speedUpButton.setToolTipText("Accélérer");
        add(speedUpButton);

        // Séparateur
        add(Box.createHorizontalStrut(20));

        // Label vitesse
        speedLabel = new JLabel("Vitesse: x1");
        speedLabel.setFont(Theme.FONT_HEADER);
        speedLabel.setForeground(Colors.TEXT_PRIMARY);
        add(speedLabel);

        // Actions
        speedUpButton.addActionListener(e -> increaseSpeed());
        speedDownButton.addActionListener(e -> decreaseSpeed());
    }

    public void setPlayPauseAction(ActionListener listener) {
        playPauseButton.addActionListener(listener);
    }

    public void togglePlayPause() {
        isPaused = !isPaused;
        updatePlayPauseButton();
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
        updatePlayPauseButton();
    }

    private void updatePlayPauseButton() {
        if (isPaused) {
            playPauseButton.setText("JOUER");
            playPauseButton.setBackground(Colors.PRIMARY);
        } else {
            playPauseButton.setText("PAUSE");
            playPauseButton.setBackground(Colors.SECONDARY);
        }
        playPauseButton.repaint();
    }

    private void increaseSpeed() {
        if (speed < 3) {
            speed++;
            updateSpeedLabel();
        }
    }

    private void decreaseSpeed() {
        if (speed > 1) {
            speed--;
            updateSpeedLabel();
        }
    }

    private void updateSpeedLabel() {
        speedLabel.setText("Vitesse: x" + speed);
    }

    public boolean isPaused() {
        return isPaused;
    }

    public int getSpeed() {
        return speed;
    }

    /**
     * Retourne le délai en ms entre chaque tick selon la vitesse.
     */
    public int getTickDelay() {
        return switch (speed) {
            case 1 -> 1000;
            case 2 -> 500;
            case 3 -> 200;
            default -> 1000;
        };
    }
}
