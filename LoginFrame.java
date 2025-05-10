package FINALNAAYOKONA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginFrame() {
        setTitle("Login");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        // Create Main Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("User Login"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components fill horizontal space
        gbc.weightx = 1.0; // Distribute horizontal space

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1; // Login button occupies one column
        gbc.fill = GridBagConstraints.NONE; // Don't fill extra space
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        formPanel.add(loginButton, gbc);

        registerButton = new JButton("Register");
        gbc.gridx = 1;
        gbc.gridwidth = 1; // Register button occupies one column
        gbc.fill = GridBagConstraints.NONE; // Don't fill extra space
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        formPanel.add(registerButton, gbc);

        // Add the form panel to the center
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0; // Distribute vertical space
        gbc.fill = GridBagConstraints.CENTER; // Center the form panel
        add(formPanel, gbc);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame(LoginFrame.this).setVisible(true);
                setVisible(false); // Hide login frame instead of disposing
            }
        });

        setVisible(true);
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role"); // Assuming "role" column in the users table
                if ("admin".equals(role)) {
                    // Open the admin dashboard with the username
                    JOptionPane.showMessageDialog(this, "Admin Login Successful!");
                    new adminDashboard(username).setVisible(true); // Pass the username
                    dispose();
                } else {
                    // Open the user dashboard, pass the username
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    new userDashboard(username).setVisible(true); // Pass the username here
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // Very important: print the stack trace for debugging
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

