package UI;
import javax.swing.*;
import java.awt.*;
public class Main_Menu extends JFrame {

    public Main_Menu() {
        setTitle("LESCO Management System - Main Menu");
        setSize(800, 600); // Increased size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Main Panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // Light Grey

        // Title Label
        JLabel titleLabel = new JLabel("LESCO Management System", SwingConstants.CENTER);
        UIUtils.styleLabel(titleLabel, new Font("Segoe UI", Font.BOLD, 28), new Color(0, 102, 204));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Buttons Panel with GridLayout
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 50, 50)); // 2 rows, 1 column, gaps of 50
        buttonPanel.setBackground(new Color(245, 245, 245));

        // Employee Menu Button
        JButton btnEmployee = new JButton("Employee Menu");
        UIUtils.styleButton(btnEmployee);
        btnEmployee.setPreferredSize(new Dimension(200, 60));
        btnEmployee.addActionListener(e -> openEmployeeLogin());

        // Customer Menu Button
        JButton btnCustomer = new JButton("Customer Menu");
        UIUtils.styleButton(btnCustomer);
        btnCustomer.setPreferredSize(new Dimension(200, 60));
        btnCustomer.addActionListener(e -> openCustomerMenu());

        // Add buttons to the panel
        buttonPanel.add(btnEmployee);
        buttonPanel.add(btnCustomer);

        // Add some padding around the buttons
        JPanel paddedButtonPanel = new JPanel(new GridBagLayout());
        paddedButtonPanel.setBackground(new Color(245, 245, 245));
        paddedButtonPanel.add(buttonPanel);

        mainPanel.add(paddedButtonPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }
    private void openEmployeeLogin() {
        new Employee_Login(this);
    }
    private void openCustomerMenu() {
        new Customer_Menu(this);
    }
}
