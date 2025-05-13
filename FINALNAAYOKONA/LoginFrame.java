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

    // Define colors
    private Color primaryBlue = new Color(173, 216, 230);
    private Color complementaryOrange = new Color(255, 165, 0);
    private Color accentOrange = new Color(255, 192, 103);
    private Color textColor = Color.BLACK;
    private Color buttonTextColor = Color.BLACK;
    private Color backButtonColor = new Color(220, 220, 220);

    public LoginFrame() {
        setTitle("Login");
        setSize(800, 500); // Larger size to better show background
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load background image
        ImageIcon backgroundImage = new ImageIcon("C:\\Users\\yuan deleon\\eclipse-workspace\\OBSS\\src\\icons\\Bgpo.jpg"); // Change path as needed
        Image img = backgroundImage.getImage();

        // Custom panel that draws the background image
        JPanel backgroundPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout()); // So formPanel stays centered
        setContentPane(backgroundPanel); // Set as main content

        // Login form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("User Login"));
        formPanel.setBackground(primaryBlue);
        formPanel.setOpaque(true); // Make sure background color shows

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        loginButton = new JButton("Login");
        loginButton.setBackground(accentOrange);
        loginButton.setForeground(buttonTextColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(loginButton, gbc);

        registerButton = new JButton("Register");
        registerButton.setBackground(backButtonColor);
        registerButton.setForeground(buttonTextColor);
        gbc.gridx = 1;
        formPanel.add(registerButton, gbc);

        // Add form panel to center of background panel
        backgroundPanel.add(formPanel);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame(LoginFrame.this).setVisible(true);
                setVisible(false);
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
                String role = rs.getString("role");
                if ("admin".equals(role)) {
                    JOptionPane.showMessageDialog(this, "Admin Login Successful!");
                    new adminDashboard(username).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    new userDashboard(username).setVisible(true);
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
