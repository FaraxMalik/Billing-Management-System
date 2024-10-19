// File: lesco/Customer_Menu.java
package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.JTableHeader;
import lesco.Customer_Management;

public class Customer_Menu extends JFrame {
    private Customer_Management customerManagement;
    
    public Customer_Menu(JFrame parent) {
        super("Customer Menu");
        this.customerManagement = new Customer_Management();

        UIUtils.initializeLookAndFeel();
        UIUtils.applyFont(this, new Font("Segoe UI", Font.PLAIN, 16)); // Apply font to all components
        setLayout(new BorderLayout());
        setSize(1000, 700); // Increased size
        setLocationRelativeTo(parent); // Center the window

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel headerLabel = new JLabel("Customer Menu", SwingConstants.CENTER);
        UIUtils.styleLabel(headerLabel, new Font("Segoe UI", Font.BOLD, 24), Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(headerPanel, BorderLayout.NORTH);

        // Button Panel with GridBagLayout for centering buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create buttons for each option
        JButton btnViewBill = createStyledButton("View Bill");
        JButton btnUpdateCNIC = createStyledButton("Update CNIC Expiry Date");
        JButton btnGoBack = createStyledButton("Go Back");

        // Add action listeners to buttons
        btnViewBill.addActionListener(e -> {
            dispose(); // Close current window
            openViewBillWindow(); // Open View Bill Window
        });
        btnUpdateCNIC.addActionListener(e -> {
            dispose(); // Close current window
            openUpdateCNICWindow(); // Open Update CNIC Window
        });
        btnGoBack.addActionListener(e -> dispose()); // Simply close the window

        // GridBagConstraints for centering buttons
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment

        // Add buttons to the button panel
        buttonPanel.add(btnViewBill, gbc);
        gbc.gridy = 1; // Move to next row
        buttonPanel.add(btnUpdateCNIC, gbc);
        gbc.gridy = 2; // Move to next row
        buttonPanel.add(btnGoBack, gbc);

        // Center button panel in the main frame
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Placeholder for other components or future enhancements
        JLabel infoLabel = new JLabel("Select an option from the menu.", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        add(infoLabel, BorderLayout.SOUTH);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18)); 
        button.setPreferredSize(new Dimension(300, 80)); 
        button.setBackground(new Color(0, 153, 204)); 
        button.setForeground(Color.WHITE); 
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2)); 
        button.setFocusPainted(false); 
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 123, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 153, 204));
            }
        });
        return button;
    }
    
private void openViewBillWindow() {
    JFrame viewBillFrame = new JFrame("View Bill Information");
    viewBillFrame.setSize(1200, 800); 
    viewBillFrame.setLayout(new BorderLayout());
    viewBillFrame.setLocationRelativeTo(this); 
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(new Color(0, 102, 204));
    JLabel headerLabel = new JLabel("View Bill Information", SwingConstants.CENTER);
    UIUtils.styleLabel(headerLabel, new Font("Segoe UI", Font.BOLD, 22), Color.WHITE);
    headerPanel.add(headerLabel, BorderLayout.CENTER);
    headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    viewBillFrame.add(headerPanel, BorderLayout.NORTH);

    // Input Panel for ID, CNIC, Month, Year
    JPanel inputPanel = new JPanel(new GridBagLayout());
    inputPanel.setBackground(new Color(245, 245, 245));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 20, 10, 20);

    // Labels and Text Fields
    String[] labels = {"Enter ID: ", "Enter CNIC: ", "Enter Bill Month: ", "Enter Year: "};
    JTextField[] textFields = new JTextField[4];

    for (int i = 0; i < labels.length; i++) {
        JLabel lbl = new JLabel(labels[i]);
        UIUtils.styleLabel(lbl, new Font("Segoe UI", Font.BOLD, 18), new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = i;
        inputPanel.add(lbl, gbc);

        textFields[i] = new JTextField(15); // Set shorter width for text fields
        UIUtils.styleTextField(textFields[i], new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 1;
        inputPanel.add(textFields[i], gbc);
    }

    // Submit and Back Buttons
    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
    buttons.setBackground(new Color(245, 245, 245));

    JButton btnSubmit = createStyledButton("Submit");
    JButton btnBack = createStyledButton("Back");

    buttons.add(btnBack);
    buttons.add(btnSubmit);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    inputPanel.add(buttons, gbc);

    viewBillFrame.add(inputPanel, BorderLayout.WEST);

    // Table for Bill Information
    String[] columnNames = {"Field", "Value"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    JTable billTable = new JTable(tableModel);
    billTable.setRowHeight(30);
    billTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    billTable.setBackground(Color.WHITE);
    billTable.setGridColor(new Color(0, 102, 204));
    billTable.setFillsViewportHeight(true);

    // Custom header style
    JTableHeader header = billTable.getTableHeader();
    header.setBackground(new Color(0, 102, 204));
    header.setForeground(Color.WHITE);
    header.setFont(new Font("Segoe UI", Font.BOLD, 18));

    JScrollPane scrollPane = new JScrollPane(billTable);
    viewBillFrame.add(scrollPane, BorderLayout.CENTER);

    // Action listener for Submit button
    btnSubmit.addActionListener(e -> {
        String id = textFields[0].getText().trim();
        String cnic = textFields[1].getText().trim();
        String month = textFields[2].getText().trim();
        String year = textFields[3].getText().trim();
        int intYear;

        // Validate the inputs
        if (id.isEmpty() || cnic.isEmpty() || month.isEmpty() || year.isEmpty()) {
            JOptionPane.showMessageDialog(viewBillFrame, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate year as integer
        try {
            intYear = Integer.parseInt(year);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(viewBillFrame, "Year must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Validate customer info
          Customer_Management CustomerBill =new Customer_Management();
//        if (!CustomerBill.validateCustomer(id, cnic, month, intYear)) {
//            JOptionPane.showMessageDialog(viewBillFrame, "Invalid customer ID or CNIC!", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
        // Fetch bill data
      
        String [][] billData = CustomerBill.fetchBillData(id, cnic, month, intYear);
        if (billData == null || billData.length == 0) {
            JOptionPane.showMessageDialog(viewBillFrame, "No bill found for the given details.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Clear the table and populate with new data
        tableModel.setRowCount(0); // Clear existing data
        for (Object[] row : billData) {
            tableModel.addRow(row);
        }
    });

    // Action listener for Back button
    btnBack.addActionListener(e -> {
        viewBillFrame.dispose(); // Close the view bill window
        new Customer_Menu(this); // Open Customer Menu again
    });

    viewBillFrame.setVisible(true);
    viewBillFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
}

private void openUpdateCNICWindow() {
    JFrame updateCNICFrame = new JFrame("Update CNIC Expiry Date");
    updateCNICFrame.setSize(800, 600);
    updateCNICFrame.setLayout(new BorderLayout());
    updateCNICFrame.setLocationRelativeTo(this); 
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(new Color(0, 153, 255)); 
    JLabel headerLabel = new JLabel("Update CNIC Expiry Date", SwingConstants.CENTER);
    UIUtils.styleLabel(headerLabel, new Font("Segoe UI", Font.BOLD, 24), Color.WHITE);
    headerPanel.add(headerLabel, BorderLayout.CENTER);
    headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    updateCNICFrame.add(headerPanel, BorderLayout.NORTH);
    JPanel inputPanel = new JPanel(new GridBagLayout());
    inputPanel.setBackground(new Color(240, 240, 240)); 
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(15, 20, 15, 20);
    JLabel lblID = new JLabel("Enter ID: ");
    UIUtils.styleLabel(lblID, new Font("Segoe UI", Font.BOLD, 18), new Color(0, 102, 204));
    gbc.gridx = 0;
    gbc.gridy = 0;
    inputPanel.add(lblID, gbc);
    JTextField txtID = new JTextField(20);
    UIUtils.styleTextField(txtID, new Font("Segoe UI", Font.PLAIN, 18));
    gbc.gridx = 1;
    inputPanel.add(txtID, gbc);
    JLabel lblCNIC = new JLabel("Enter CNIC: ");
    UIUtils.styleLabel(lblCNIC, new Font("Segoe UI", Font.BOLD, 18), new Color(0, 102, 204));
    gbc.gridx = 0;
    gbc.gridy = 1;
    inputPanel.add(lblCNIC, gbc);
    JTextField txtCNIC = new JTextField(20);
    UIUtils.styleTextField(txtCNIC, new Font("Segoe UI", Font.PLAIN, 18));
    gbc.gridx = 1;
    inputPanel.add(txtCNIC, gbc);
    JLabel lblExpiryDate = new JLabel("Enter Expiry Date: ");
    UIUtils.styleLabel(lblExpiryDate, new Font("Segoe UI", Font.BOLD, 18), new Color(0, 102, 204));
    gbc.gridx = 0;
    gbc.gridy = 2;
    inputPanel.add(lblExpiryDate, gbc);
    JTextField txtExpiryDate = new JTextField(20);
    UIUtils.styleTextField(txtExpiryDate, new Font("Segoe UI", Font.PLAIN, 18));
    gbc.gridx = 1;
    inputPanel.add(txtExpiryDate, gbc);
    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
    buttons.setBackground(new Color(240, 240, 240)); // Same color as input panel
    JButton btnSubmit = createStyledButton("Submit");
    JButton btnBack = createStyledButton("Back");
    buttons.add(btnBack);
    buttons.add(btnSubmit);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    inputPanel.add(buttons, gbc);

    updateCNICFrame.add(inputPanel, BorderLayout.CENTER); 

    btnSubmit.addActionListener(e -> {
        String id = txtID.getText().trim();
        String cnic = txtCNIC.getText().trim();
        String expiryDate = txtExpiryDate.getText().trim();
        if (id.isEmpty() || cnic.isEmpty() || expiryDate.isEmpty()) {
            JOptionPane.showMessageDialog(updateCNICFrame, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = customerManagement.updateCNIC(id, cnic, expiryDate);
        if (success) {
            JOptionPane.showMessageDialog(updateCNICFrame, "CNIC expiry date updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(updateCNICFrame, "Failed to update CNIC expiry date.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    btnBack.addActionListener(e -> {
        updateCNICFrame.dispose();
        new Customer_Menu(this); 
    });

    updateCNICFrame.setVisible(true);
    updateCNICFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Customer_Menu(null));
    }
}
