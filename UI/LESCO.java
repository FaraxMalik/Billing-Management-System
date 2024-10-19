// File: lesco/LESCO.java
package UI;

import javax.swing.*;
import java.awt.*;

public class LESCO {
    public LESCO() {
        UIUtils.initializeLookAndFeel();
        showSplashScreen();
    }

    private void showSplashScreen() {
        JWindow splash = new JWindow();
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(0, 102, 204)); // Dark Blue

        // Welcome Label
        JLabel label = new JLabel("Welcome TO LESCO Management System", SwingConstants.CENTER);
        UIUtils.styleLabel(label, new Font("Segoe UI", Font.BOLD, 22), Color.WHITE);
        content.add(label, BorderLayout.CENTER);

        // Progress Bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(30, 144, 255)); // Dodger Blue
        progressBar.setBackground(Color.WHITE);
        progressBar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        content.add(progressBar, BorderLayout.SOUTH);

        splash.setContentPane(content);
        splash.setSize(600, 400); // Increased size
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);

        // Loading Simulation
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                int progress = 0;
                while (progress <= 100) {
                    Thread.sleep(4); // Faster loading for demonstration
                    publish(progress);
                    progress++;
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                int value = chunks.get(chunks.size() - 1);
                progressBar.setValue(value);
                progressBar.setString("Loading " + value + "%");
            }

            @Override
            protected void done() {
                splash.dispose(); // Close splash screen
                SwingUtilities.invokeLater(() -> new Main_Menu());
            }
        };

        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LESCO());
    }
}
