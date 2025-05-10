package FINALNAAYOKONA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegisterFrame extends JFrame {
    private JTextField nameField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton registerButton, backButton;
    private LoginFrame loginFrame;

    public RegisterFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        setTitle("Register");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Register"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);

        nameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(emailLabel, gbc);

        emailField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        registerButton = new JButton("Register");
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(registerButton, gbc);

        backButton = new JButton("Back");
        gbc.gridx = 1;
        formPanel.add(backButton, gbc);

        add(formPanel, gbc);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginFrame.setVisible(true);
                dispose();
            }
        });
    }

    private void registerUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (name, email, username, password) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, email); // Email as username
            pst.setString(4, password);

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loginFrame.setVisible(true);
                dispose();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error during registration!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
