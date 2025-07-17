package vista;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {

    public SplashScreen() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 76, 153));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Chargement du logo
        ImageIcon logo = new ImageIcon("src/resources/logo.png");
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("InvenTrack Pro");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel slogan = new JLabel("Control total de tu inventario");
        slogan.setFont(new Font("SansSerif", Font.PLAIN, 16));
        slogan.setForeground(Color.WHITE);
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(logoLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(slogan);
        panel.add(Box.createVerticalGlue());

        setContentPane(panel);
        setSize(400, 500);
        setLocationRelativeTo(null);
    }

    public static void mostrar() {
        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);

        // Timer Swing : ne bloque pas le thread UI
        Timer timer = new Timer(3500, e -> {
            splash.setVisible(false);
            splash.dispose();
            new LoginFrame().setVisible(true);
        });
        timer.setRepeats(false);
        timer.start();
    }
}