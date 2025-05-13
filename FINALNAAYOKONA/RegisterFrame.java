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

    // Define colors
    private Color primaryBlue = new Color(173, 216, 230);
    private Color complementaryOrange = new Color(255, 165, 0);
    private Color accentOrange = new Color(255, 192, 103);
    private Color textColor = Color.BLACK;

    public RegisterFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        setTitle("Register");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load background image
        ImageIcon bgIcon = new ImageIcon("C:\\Users\\yuan deleon\\eclipse-workspace\\OBSS\\src\\icons\\Bgpo.jpg"); // Adjust path to your image
        Image bgImage = bgIcon.getImage();

        // Custom background panel
        JPanel backgroundPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout()); // Keep form centered
        setContentPane(backgroundPanel); // Set as main content

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Register"));
        formPanel.setBackground(primaryBlue);
        formPanel.setOpaque(true); // ensure background is painted

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(textColor);
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);

        nameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(textColor);
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(emailLabel, gbc);

        emailField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(textColor);
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(textColor);
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        registerButton = new JButton("Register");
        registerButton.setBackground(accentOrange);
        registerButton.setForeground(textColor);
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(registerButton, gbc);

        backButton = new JButton("Back");
        backButton.setBackground(new Color(220, 220, 220));
        backButton.setForeground(textColor);
        gbc.gridx = 1;
        formPanel.add(backButton, gbc);

        backgroundPanel.add(formPanel); // Add form to center of background

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
