
package UI;

import javax.swing.*;
import java.awt.*;

public class UIUtils {
    // Initialize Nimbus Look and Feel
    public static void initializeLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus Look and Feel not available, using default.");
        }
    }

    // Apply consistent font to components
    public static void applyFont(Component component, Font font) {
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyFont(child, font);
            }
        }
    }

    // Style buttons consistently
    public static void styleButton(JButton button) {
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 144, 255)); // Dodger Blue
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204)); // Original Color
            }
        });
    }

    // Style labels consistently
    public static void styleLabel(JLabel label, Font font, Color color) {
        label.setFont(font);
        label.setForeground(color);
    }

    // Style text fields consistently
    public static void styleTextField(JTextField textField, Font font) {
        textField.setFont(font);
        textField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)));
    }

    // Style password fields consistently
    public static void stylePasswordField(JPasswordField passwordField, Font font) {
        passwordField.setFont(font);
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)));
    }
}
