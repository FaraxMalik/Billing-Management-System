package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import lesco.Billing_Management;
import lesco.Customer_Management;
import lesco.Employees_Management;
import lesco.Tax_Management;

public class Employee_Menu extends JFrame {
    private Employees_Management employeesManagement;
    private String username;
    private String password;
    private JPanel displayPanel;

public Employee_Menu(Employees_Management employeesManagement, String username, String password) {
        super("Employee Options");
        this.employeesManagement = employeesManagement;
        this.username = username;
        this.password = password;

        UIUtils.initializeLookAndFeel();
        UIUtils.applyFont(this, new Font("SansSerif", Font.PLAIN, 14)); // Softer, smoother font
        setLayout(new BorderLayout());
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel headerLabel = new JLabel("Employee Options", SwingConstants.CENTER);
        UIUtils.styleLabel(headerLabel, new Font("SansSerif", Font.BOLD, 22), Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0)); // Softer padding
        add(headerPanel, BorderLayout.NORTH);

        // Main Panel for buttons and display area
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Button Panel using GridBagLayout for full-width buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // GridBagConstraints for making buttons occupy full width
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 0, 5, 0); // Reduced gap between buttons

        // Create buttons for each option
        JButton btnAddCustomer = createStyledButton("Add Customer");
        JButton btnUpdatePassword = createStyledButton("Update Password");
        JButton btnupdateCustomer = createStyledButton("Update Customers");
        JButton btnAddBill = createStyledButton("Add Bill");
        JButton btnUpdateBillStatus = createStyledButton("Update Bill Paid Status");
        JButton btnChangeTaxes = createStyledButton("Change Taxes");
        JButton btnViewBill = createStyledButton("View Bill");
        JButton btnViewExpireCnic = createStyledButton("View Expiring CNIC's");

        // Go Back Button
        JButton btnGoBack = createStyledButton("Go Back");

        // Add action listeners to buttons
        btnAddCustomer.addActionListener(e -> openAddCustomerWindow());
        btnUpdatePassword.addActionListener(e -> openUpdatePasswordWindow());
        btnupdateCustomer.addActionListener(e -> openUpdateCustomerWindow());
        btnAddBill.addActionListener(e -> openAddBillWindow());
        btnUpdateBillStatus.addActionListener(e -> openUpdateBillStatusWindow());
         btnChangeTaxes.addActionListener(e -> openChangeTaxesWindow());
 btnViewBill.addActionListener(e -> openViewBillWindow());
         
        // Add buttons to the button panel using GridBagConstraints for full width
        buttonPanel.add(btnAddCustomer, gbc);
        buttonPanel.add(btnUpdatePassword, gbc);
        buttonPanel.add(btnupdateCustomer, gbc);
        buttonPanel.add(btnAddBill, gbc);
        buttonPanel.add(btnUpdateBillStatus, gbc);
        buttonPanel.add(btnChangeTaxes, gbc);
        buttonPanel.add(btnViewBill, gbc);
        buttonPanel.add(btnViewExpireCnic, gbc);
        buttonPanel.add(btnGoBack, gbc);

        // Add the button panel to the main panel (left side)
        mainPanel.add(buttonPanel, BorderLayout.WEST);

        // Right Side Panel for display area (table)
        displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBackground(new Color(230, 230, 250));
        displayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(displayPanel, BorderLayout.CENTER);

        // Add main panel to the frame
        add(mainPanel, BorderLayout.CENTER);

        // Ensure visibility
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    // Method to create styled buttons
private JTextField createStyledTextField() {
    JTextField textField = new JTextField(20);
    textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
    textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204), 2), // Border color
            BorderFactory.createEmptyBorder(5, 10, 5, 10))); // Padding
    textField.setBackground(displayPanel.getBackground()); // Set the same background color
    return textField; // Remove background color to keep default
}
// Helper method to create a styled JButton
private JButton createStyledButton(String text) {
    JButton button = new JButton(text);
    button.setBackground(new Color(0, 102, 204));
    button.setForeground(Color.WHITE);
    button.setFont(new Font("SansSerif", Font.BOLD, 14)); // Softer font for buttons
    button.setFocusPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Softer padding
    button.setOpaque(true);
    button.setContentAreaFilled(true); // Ensure filled background
    button.setBorderPainted(false);
    
    // Add hover effect
    button.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            button.setBackground(new Color(30, 144, 255)); // Dodger Blue
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            button.setBackground(new Color(0, 102, 204)); // Original Color
        }
    });

    return button;
}

// Method to filter table based on search text
private void filterBillTable(JTable billTable, String[][] originalData, String searchText) {
    // Create a new table model with filtered data
    String[][] filteredData = Arrays.stream(originalData)
            .filter(row -> row[0].contains(searchText)) // Assuming first column is Customer ID
            .toArray(String[][]::new);
    billTable.setModel(new DefaultTableModel(filteredData, new String[]{"Customer ID", "Billing Month", "Units Consumed", "Peak Units", 
            "Entry Date", "Cost", "Tax", "Fixed Charges", "Total Amount", "Paid Date", "Bill Status", "Payment Date"}));
}
// Helper method to create a styled JButton
private void openAddCustomerWindow() {
    // Create a panel for adding customer information
    JPanel customerPanel = new JPanel();
    customerPanel.setLayout(new GridBagLayout());
    customerPanel.setBackground(displayPanel.getBackground()); // Set the same background color
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Define a consistent font for labels and text fields
    Font labelFont = new Font("SansSerif", Font.BOLD, 14);
    Font textFont = new Font("SansSerif", Font.PLAIN, 14);

    // Labels and TextFields for each field
    JLabel lblCnic = new JLabel("CNIC:");
    lblCnic.setFont(labelFont);
    JTextField txtCnic = createStyledTextField();
    gbc.gridx = 0;
    gbc.gridy = 0;
    customerPanel.add(lblCnic, gbc);
    gbc.gridx = 1;
    customerPanel.add(txtCnic, gbc);

    JLabel lblName = new JLabel("Name:");
    lblName.setFont(labelFont);
    JTextField txtName = createStyledTextField();
    gbc.gridx = 0;
    gbc.gridy = 1;
    customerPanel.add(lblName, gbc);
    gbc.gridx = 1;
    customerPanel.add(txtName, gbc);

    JLabel lblAddress = new JLabel("Address:");
    lblAddress.setFont(labelFont);
    JTextField txtAddress = createStyledTextField();
    gbc.gridx = 0;
    gbc.gridy = 2;
    customerPanel.add(lblAddress, gbc);
    gbc.gridx = 1;
    customerPanel.add(txtAddress, gbc);

    JLabel lblPhone = new JLabel("Phone:");
    lblPhone.setFont(labelFont);
    JTextField txtPhone = createStyledTextField();
    gbc.gridx = 0;
    gbc.gridy = 3;
    customerPanel.add(lblPhone, gbc);
    gbc.gridx = 1;
    customerPanel.add(txtPhone, gbc);

    JLabel lblCustType = new JLabel("Customer Type (C/D):");
    lblCustType.setFont(labelFont);
    JTextField txtCustType = createStyledTextField();
    gbc.gridx = 0;
    gbc.gridy = 4;
    customerPanel.add(lblCustType, gbc);
    gbc.gridx = 1;
    customerPanel.add(txtCustType, gbc);

    JLabel lblMeterType = new JLabel("Meter Type (S/T):");
    lblMeterType.setFont(labelFont);
    JTextField txtMeterType = createStyledTextField();
    gbc.gridx = 0;
    gbc.gridy = 5;
    customerPanel.add(lblMeterType, gbc);
    gbc.gridx = 1;
    customerPanel.add(txtMeterType, gbc);

    // Confirm Button
    JButton btnConfirm = createStyledButton("Confirm");
    gbc.gridx = 1;
    gbc.gridy = 6;
    customerPanel.add(btnConfirm, gbc);

    // Add the customer panel to the display area on the right side
    displayPanel.removeAll(); // Clear previous content
    displayPanel.add(customerPanel); // Add the new customer panel
    displayPanel.revalidate();
    displayPanel.repaint();

    // Action listener for Confirm button
    btnConfirm.addActionListener(e -> {
        // Get input values
        String cnic = txtCnic.getText().trim();
        String name = txtName.getText().trim();
        String address = txtAddress.getText().trim();
        String phone = txtPhone.getText().trim();
        String custType = txtCustType.getText().trim();
        String meterType = txtMeterType.getText().trim();

        // Call addCustomer method from Customer_Management class
        Customer_Management customerManagement = new Customer_Management();
        boolean success = customerManagement.addCustomer(cnic, name, address, phone, custType, meterType);

        if (success) {
            JOptionPane.showMessageDialog(this, "Customer added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add customer. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
}

private void openUpdatePasswordWindow() {
    // Create a panel for updating password
    JPanel updatePasswordPanel = new JPanel();
    updatePasswordPanel.setLayout(new GridBagLayout());
    updatePasswordPanel.setBackground(displayPanel.getBackground()); // Set the same background color
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.CENTER; // Center the components horizontally

    // Define a consistent font for labels and text fields
    Font labelFont = new Font("SansSerif", Font.BOLD, 14);
    Font textFont = new Font("SansSerif", Font.PLAIN, 14);

    // Shift the whole content down by increasing the starting row index
    gbc.gridy = 1; // Start from row 1 to push everything down by 1 row

    // Labels and TextFields for username and new password
    JLabel lblUsername = new JLabel("Username:");
    lblUsername.setFont(labelFont);
    gbc.gridx = 0; // Column 0
    updatePasswordPanel.add(lblUsername, gbc);
    gbc.gridx = 1; // Column 1
    JTextField txtNewname = new JTextField(20);
    txtNewname.setBackground(Color.WHITE); // Set background color to white
    txtNewname.setFont(textFont); // Set font style
    updatePasswordPanel.add(txtNewname, gbc);

    JLabel lblNewPass = new JLabel("New Password:");
    lblNewPass.setFont(labelFont);
    gbc.gridx = 0; // Column 0
    gbc.gridy = 2; // Move to the next row
    updatePasswordPanel.add(lblNewPass, gbc);
    gbc.gridx = 1; // Column 1
    JPasswordField txtNewPass = new JPasswordField(20);
    txtNewPass.setBackground(Color.WHITE); // Set background color to white
    txtNewPass.setFont(textFont); // Set font style
    updatePasswordPanel.add(txtNewPass, gbc);

    // Confirm Button
    JButton btnUpdate = createStyledButton("Update Password"); // Create a styled button
    gbc.gridx = 1; // Column 1
    gbc.gridy = 3; // Move to the next row for the button
    updatePasswordPanel.add(btnUpdate, gbc);

    // Add the update password panel to the display area on the right side
    displayPanel.removeAll(); // Clear previous content
    displayPanel.add(updatePasswordPanel); // Add the new update password panel
    displayPanel.revalidate();
    displayPanel.repaint();

    // Create an instance of Employees_Management to access updatePass method
    Employees_Management employeeManagement = new Employees_Management();

    // Action listener for Update button
    btnUpdate.addActionListener(e -> {
        // Get input values
        String username = txtNewname.getText().trim(); // Corrected: Use txtUsername instead of lblNewPass
        String newPassword = new String(txtNewPass.getPassword()).trim();

        // Call updatePass method from Employee_Management class
        boolean success = employeeManagement.updatePass(username, newPassword);

        if (success) {
            JOptionPane.showMessageDialog(this, "Password updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
}

private void openUpdateCustomerWindow() {
        // Create a panel for displaying customer data
        JPanel updateCustomerPanel = new JPanel(new BorderLayout());
        updateCustomerPanel.setBackground(displayPanel.getBackground());

        // Fetch customer data from file using arrays
        Customer_Management customerManagement = new Customer_Management();
        String[][] customerData = customerManagement.readCustomersFromFile();

        // Define the column names for the table
        String[] columnNames = {"CNIC", "Name", "Address", "Phone", "Customer Type", "Meter Type"};

        // Create a JTable with the data and column names
        JTable customerTable = new JTable(customerData, columnNames);
        customerTable.setFillsViewportHeight(true);
        customerTable.setShowGrid(false);  // Removes table grid lines for a cleaner look
        customerTable.setRowHeight(30);    // Set row height for better readability

        // Customize the table header
        JTableHeader header = customerTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setBackground(new Color(200, 200, 255)); // Soft background color for header
        header.setForeground(Color.BLACK);
        header.setReorderingAllowed(false);  // Disable column reordering
        header.setBorder(BorderFactory.createEmptyBorder());  // Remove header border

        // Set font for table content
        customerTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        customerTable.setBackground(displayPanel.getBackground());
        customerTable.setForeground(Color.BLACK);

        // Place the table inside a scroll pane
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(null);  // Remove scroll pane border for a clean appearance

        // Add the scroll pane to the panel (CENTER)
        updateCustomerPanel.add(scrollPane, BorderLayout.CENTER);

        // Save button
        JButton btnSave = new JButton("Save Changes");
        btnSave.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnSave.setBackground(new Color(0, 123, 255));  // Button color
        btnSave.setForeground(Color.WHITE);

        // Add Save button to the bottom of the panel
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(displayPanel.getBackground());
        btnPanel.add(btnSave, BorderLayout.SOUTH);

        updateCustomerPanel.add(btnPanel, BorderLayout.SOUTH);

        // Add the customer panel to the display area on the right side
        displayPanel.removeAll(); // Clear previous content
        displayPanel.add(updateCustomerPanel); // Add the new customer panel
        displayPanel.revalidate();
        displayPanel.repaint();

        // Action listener for the save button
        btnSave.addActionListener(e -> {
            // Get updated data and write back to the file
            String[][] updatedData = new String[customerTable.getRowCount()][customerTable.getColumnCount()];
            for (int i = 0; i < customerTable.getRowCount(); i++) {
                for (int j = 0; j < customerTable.getColumnCount(); j++) {
                    updatedData[i][j] = customerTable.getValueAt(i, j).toString();
                }
            }
            customerManagement.writeCustomersToFile(updatedData);
            JOptionPane.showMessageDialog(this, "Changes saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
    }

public void openAddBillWindow() {
    JPanel billPanel = new JPanel(new GridBagLayout());
    billPanel.setBackground(displayPanel.getBackground());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    Font labelFont = new Font("SansSerif", Font.BOLD, 14);
    Font textFont = new Font("SansSerif", Font.PLAIN, 14);

    // Labels and TextFields for each field
    JLabel lblCustID = new JLabel("Customer ID:");
    lblCustID.setFont(labelFont);
    JTextField txtCustID = createStyledTextField();
    gbc.gridx = 0;
    gbc.gridy = 0;
    billPanel.add(lblCustID, gbc);
    gbc.gridx = 1;
    billPanel.add(txtCustID, gbc);

    JLabel lblBillingMonth = new JLabel("Billing Month:");
    lblBillingMonth.setFont(labelFont);
    JTextField txtBillingMonth = createStyledTextField();
    gbc.gridx = 0;
    gbc.gridy = 1;
    billPanel.add(lblBillingMonth, gbc);
    gbc.gridx = 1;
    billPanel.add(txtBillingMonth, gbc);

    JLabel lblMeterReading = new JLabel("Meter Reading:");
    lblMeterReading.setFont(labelFont);
    JTextField txtMeterReading = createStyledTextField();
    gbc.gridx = 0;
    gbc.gridy = 2;
    billPanel.add(lblMeterReading, gbc);
    gbc.gridx = 1;
    billPanel.add(txtMeterReading, gbc);

    JLabel lblMeterReadingPeak = new JLabel("Meter Reading Peak:");
    lblMeterReadingPeak.setFont(labelFont);
    JTextField txtMeterReadingPeak = createStyledTextField();
    gbc.gridx = 0;
    gbc.gridy = 3;
    billPanel.add(lblMeterReadingPeak, gbc);
    gbc.gridx = 1;
    billPanel.add(txtMeterReadingPeak, gbc);

    // Submit Button
    JButton btnSubmit = createStyledButton("Submit");
    gbc.gridx = 1;
    gbc.gridy = 4;
    billPanel.add(btnSubmit, gbc);

    displayPanel.removeAll();
    displayPanel.add(billPanel);
    displayPanel.revalidate();
    displayPanel.repaint();

    btnSubmit.addActionListener(e -> {
        String custID = txtCustID.getText().trim();
        String billingMonth = txtBillingMonth.getText().trim();
        String meterReading = txtMeterReading.getText().trim();
        String meterReadingPeak = txtMeterReadingPeak.getText().trim();

        Billing_Management billingManagement = new Billing_Management();
        boolean success = billingManagement.addNewBill(custID, billingMonth, meterReading, meterReadingPeak);

        if (success) {
            JOptionPane.showMessageDialog(this, "Bill added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add bill. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
}

private void openUpdateBillStatusWindow() {
    // Create a panel with GridBagLayout for better control over component placement
    JPanel updatePanel = new JPanel(new GridBagLayout());
    updatePanel.setBackground(displayPanel.getBackground());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    Font labelFont = new Font("SansSerif", Font.BOLD, 14);
    Font textFont = new Font("SansSerif", Font.PLAIN, 14);

    // Labels and TextFields for each field
    JLabel lblCustID = new JLabel("Customer ID:");
    lblCustID.setFont(labelFont);
    JTextField txtCustID = createStyledTextField();
    gbc.gridx = 0;
    gbc.gridy = 0;
    updatePanel.add(lblCustID, gbc);
    gbc.gridx = 1;
    updatePanel.add(txtCustID, gbc);

    JLabel lblBillingMonth = new JLabel("Billing Month:");
    lblBillingMonth.setFont(labelFont);
    JTextField txtBillingMonth = createStyledTextField();
    gbc.gridx = 0;
    gbc.gridy = 1;
    updatePanel.add(lblBillingMonth, gbc);
    gbc.gridx = 1;
    updatePanel.add(txtBillingMonth, gbc);

    JLabel lblEntryDate = new JLabel("Bill Entry Date (dd/MM/yyyy):");
    lblEntryDate.setFont(labelFont);
    JTextField txtEntryDate = createStyledTextField();
    gbc.gridx = 0;
    gbc.gridy = 2;
    updatePanel.add(lblEntryDate, gbc);
    gbc.gridx = 1;
    updatePanel.add(txtEntryDate, gbc);

    JLabel lblPaidDate = new JLabel("Bill Paid Date (dd/MM/yyyy):");
    lblPaidDate.setFont(labelFont);
    JTextField txtPaidDate = createStyledTextField();
    gbc.gridx = 0;
    gbc.gridy = 3;
    updatePanel.add(lblPaidDate, gbc);
    gbc.gridx = 1;
    updatePanel.add(txtPaidDate, gbc);

    // Create instance of Billing_Management class
    Billing_Management billingManagement = new Billing_Management();

    // Submit Button
    JButton btnSubmit = createStyledButton("Update Bill Status");
    gbc.gridx = 1;
    gbc.gridy = 4;
    updatePanel.add(btnSubmit, gbc);

    // Add cancel button
    JButton btnCancel = createStyledButton("Cancel");
    gbc.gridx = 0;
    gbc.gridy = 4;
    updatePanel.add(btnCancel, gbc);

    // Clear previous content and add the update panel
    displayPanel.removeAll();
    displayPanel.add(updatePanel);
    displayPanel.revalidate();
    displayPanel.repaint();

    // Submit button action
    btnSubmit.addActionListener(e -> {
        String custID = txtCustID.getText().trim();
        String billingMonth = txtBillingMonth.getText().trim();
        String entryDate = txtEntryDate.getText().trim();
        String paidDate = txtPaidDate.getText().trim();

        // Validate the billing month
        if (!billingManagement.validateBillingMonth(billingMonth)) {
            JOptionPane.showMessageDialog(this, "Invalid Billing Month. Please enter a valid month.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Call the changePaidStatus method from Billing_Management
        if (billingManagement.changePaidStatus(custID, billingMonth, entryDate, paidDate)) {
            JOptionPane.showMessageDialog(this, "Bill status updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error updating bill status. Please check the details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    // Cancel button action
    btnCancel.addActionListener(e -> {
        displayPanel.removeAll();
        displayPanel.revalidate();
        displayPanel.repaint();
    });
}

private void openChangeTaxesWindow() {
    // Create a panel for displaying tax data
    JPanel taxPanel = new JPanel(new BorderLayout());
    taxPanel.setBackground(displayPanel.getBackground());

    // Create instance of Tax_Management class
    Tax_Management taxManagement = new Tax_Management();

    // Fetch tax data from file using a 2D array
    String[][] taxData = taxManagement.readTaxesFromFile();

    // Define the column names for the table
    String[] columnNames = {"Meter Type", "Rate", "Peak Rate", "Other Rate", "Fixed Charge"};

    // Create a JTable with the data and column names
    JTable taxTable = new JTable(taxData, columnNames);
    taxTable.setFillsViewportHeight(true);
    taxTable.setShowGrid(false);  // Removes table grid lines for a cleaner look
    taxTable.setRowHeight(30);    // Set row height for better readability

    // Customize the table header
    JTableHeader header = taxTable.getTableHeader();
    header.setFont(new Font("SansSerif", Font.BOLD, 16));
    header.setBackground(new Color(200, 200, 255)); // Soft background color for header
    header.setForeground(Color.BLACK);
    header.setReorderingAllowed(false);  // Disable column reordering
    header.setBorder(BorderFactory.createEmptyBorder());  // Remove header border

    // Set font for table content
    taxTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
    taxTable.setBackground(displayPanel.getBackground());
    taxTable.setForeground(Color.BLACK);

    // Place the table inside a scroll pane
    JScrollPane scrollPane = new JScrollPane(taxTable);
    scrollPane.setBorder(null);  // Remove scroll pane border for a clean appearance

    // Add the scroll pane to the panel (CENTER)
    taxPanel.add(scrollPane, BorderLayout.CENTER);

    // Save button
    JButton btnSave = new JButton("Save Changes");
    btnSave.setFont(new Font("SansSerif", Font.PLAIN, 14));
    btnSave.setBackground(new Color(0, 123, 255));  // Button color
    btnSave.setForeground(Color.WHITE);

    // Add Save button to the bottom of the panel
    JPanel btnPanel = new JPanel();
    btnPanel.setBackground(displayPanel.getBackground());
    btnPanel.add(btnSave, BorderLayout.SOUTH);

    taxPanel.add(btnPanel, BorderLayout.SOUTH);

    // Add the tax panel to the display area
    displayPanel.removeAll(); // Clear previous content
    displayPanel.add(taxPanel); // Add the new tax panel
    displayPanel.revalidate();
    displayPanel.repaint();

    // Action listener for the save button
    btnSave.addActionListener(e -> {
        // Get updated data and write back to the file
        String[][] updatedData = new String[taxTable.getRowCount()][taxTable.getColumnCount()];
        for (int i = 0; i < taxTable.getRowCount(); i++) {
            for (int j = 0; j < taxTable.getColumnCount(); j++) {
                updatedData[i][j] = taxTable.getValueAt(i, j).toString();
            }
        }
        taxManagement.writeTaxesToFile(updatedData);
        JOptionPane.showMessageDialog(this, "Changes saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    });
}

private void openViewBillWindow() {
    // Create a panel for displaying billing data
    JPanel viewBillPanel = new JPanel(new BorderLayout());
    viewBillPanel.setBackground(displayPanel.getBackground());

    // Fetch billing data from the file
    Billing_Management billingManagement = new Billing_Management();
    String[][] billingData = billingManagement.readBillsFromFile();

    // Define the column names for the table
    String[] columnNames = {"Customer ID", "Billing Month", "Units Consumed", "Peak Units", 
                             "Entry Date", "Cost", "Tax", "Fixed Charges", 
                             "Total Amount", "Paid Date", "Bill Status", "Payment Date"};

    // Create a JTable with the data and column names
    JTable billTable = new JTable(billingData, columnNames);
    billTable.setFillsViewportHeight(true);
    billTable.setShowGrid(false);
    billTable.setRowHeight(30);

    // Set preferred widths for columns
    int[] columnWidths = {100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100};
    for (int i = 0; i < columnWidths.length; i++) {
        billTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
    }

    // Customize the table header
    JTableHeader header = billTable.getTableHeader();
    header.setFont(new Font("SansSerif", Font.BOLD, 16));
    header.setBackground(new Color(200, 200, 255));
    header.setForeground(Color.BLACK);
    header.setReorderingAllowed(false);
    header.setBorder(BorderFactory.createEmptyBorder());

    // Set font for table content
    billTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
    billTable.setBackground(displayPanel.getBackground());
    billTable.setForeground(Color.BLACK);

    // Place the table inside a scroll pane
    JScrollPane scrollPane = new JScrollPane(billTable);
    scrollPane.setBorder(null);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Enable horizontal scroll if needed

    // Add the scroll pane to the panel (CENTER)
    viewBillPanel.add(scrollPane, BorderLayout.CENTER);

    // Create a search bar
    JTextField searchField = new JTextField("Search by Customer ID...");
    searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
    searchField.setBackground(new Color(240, 240, 240)); // Light background for search field
    searchField.setForeground(Color.GRAY); // Placeholder color

    // Add action listener for search functionality
    searchField.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String searchText = searchField.getText().trim();
            filterBillTable(billTable, billingData, searchText);
        }
    });

    // Add focus listener to clear placeholder text
    searchField.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            if (searchField.getText().equals("Search by Customer ID...")) {
                searchField.setText("");
                searchField.setForeground(Color.BLACK);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (searchField.getText().isEmpty()) {
                searchField.setForeground(Color.GRAY);
                searchField.setText("Search by Customer ID...");
            }
        }
    });

    // Add search bar to the top of the panel
    JPanel searchPanel = new JPanel(new BorderLayout());
    searchPanel.add(searchField, BorderLayout.CENTER);
    viewBillPanel.add(searchPanel, BorderLayout.NORTH);

    // Save button
    JButton btnSave = new JButton("Save Changes");
    btnSave.setFont(new Font("SansSerif", Font.PLAIN, 14));
    btnSave.setBackground(new Color(0, 123, 255));
    btnSave.setForeground(Color.WHITE);

    // Add Save button to the bottom of the panel
    JPanel btnPanel = new JPanel();
    btnPanel.setBackground(displayPanel.getBackground());
    btnPanel.add(btnSave);

    viewBillPanel.add(btnPanel, BorderLayout.SOUTH);

    // Add the billing panel to the display area
    displayPanel.removeAll(); // Clear previous content
    displayPanel.add(viewBillPanel); // Add the new billing panel
    displayPanel.revalidate();
    displayPanel.repaint();

    // Action listener for the save button
    btnSave.addActionListener(e -> {
        // Get updated data and write back to the file
        String[][] updatedData = new String[billTable.getRowCount()][billTable.getColumnCount()];
        for (int i = 0; i < billTable.getRowCount(); i++) {
            for (int j = 0; j < billTable.getColumnCount(); j++) {
                updatedData[i][j] = billTable.getValueAt(i, j).toString();
            }
        }
        billingManagement.writeBillsToFile(updatedData);
        JOptionPane.showMessageDialog(this, "Changes saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    });
}

    private void openViewExpireCnicWindow() {
        JOptionPane.showMessageDialog(this, "View Expiring CNIC's Window is not implemented yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
