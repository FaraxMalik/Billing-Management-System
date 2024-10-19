package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import lesco.Employees_Management;

public class Employee_Login extends JDialog {
    private Employees_Management employeesManagement;

    public Employee_Login(JFrame parent) {
        super(parent, "Employee Login", true);
        UIUtils.applyFont(this, new Font("Segoe UI", Font.PLAIN, 16)); 
        setSize(500, 350); 
        setLocationRelativeTo(parent);
        setResizable(false);

        employeesManagement = new Employees_Management();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245)); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20); 

        JLabel lblUsername = new JLabel("Username:");
        UIUtils.styleLabel(lblUsername, new Font("Segoe UI", Font.BOLD, 18), new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblUsername, gbc);

        JTextField txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(200, 30)); 
        UIUtils.styleTextField(txtUsername, new Font("Segoe UI", Font.PLAIN, 18));
        txtUsername.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        txtUsername.setBackground(Color.WHITE); 
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Password:");
        UIUtils.styleLabel(lblPassword, new Font("Segoe UI", Font.BOLD, 18), new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblPassword, gbc);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(200, 30)); 
        UIUtils.stylePasswordField(txtPassword, new Font("Segoe UI", Font.PLAIN, 18));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2)); 
        txtPassword.setBackground(Color.WHITE); 
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        // Buttons Panel
        JPanel buttons = new JPanel();
        buttons.setBackground(new Color(245, 245, 245)); 
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Login Button
        JButton btnLogin = new JButton("Login");
        UIUtils.styleButton(btnLogin);
        btnLogin.setPreferredSize(new Dimension(120, 40));
        btnLogin.setBackground(new Color(0, 102, 204)); 
        btnLogin.setForeground(Color.WHITE); 
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        buttons.add(btnLogin);

        // Cancel Button
        JButton btnCancel = new JButton("Cancel");
        UIUtils.styleButton(btnCancel);
        btnCancel.setPreferredSize(new Dimension(120, 40));
        btnCancel.setBackground(new Color(200, 0, 0)); // Red color for cancel button
        btnCancel.setForeground(Color.WHITE); // Text color
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Font style
        buttons.add(btnCancel);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(buttons, gbc);

        // Add panel to dialog
        add(panel);

        // Action Listeners
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(Employee_Login.this,
                            "Please enter both username and password.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (employeesManagement.validateEmployee(username, password)) {
                    dispose();
                    new Employee_Menu(employeesManagement, username, password); // Open Employee Options
                } else {
                    JOptionPane.showMessageDialog(Employee_Login.this,
                            "Incorrect Username or Password", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }
}
