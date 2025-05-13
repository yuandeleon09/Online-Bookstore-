package FINALNAAYOKONA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ProfilePanel extends JPanel {

    private JLabel nameLabel, usernameLabel, emailLabel, imageLabel; // Added imageLabel
    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JButton saveButton, cancelButton, editImageButton; // Added editImageButton
    private String currentUsername;
    private JButton logoutButton;
    private byte[] imageData; // To store the image data
    private static final int IMG_WIDTH = 100;  //Set image width and height
    private static final int IMG_HEIGHT = 100;
    private boolean isEditing = false; // Track edit mode

    public ProfilePanel(String username) {
        this.currentUsername = username;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Image Label
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(imageLabel, gbc);
        gbc.gridwidth = 1;  //reset

        // Edit Image Button
        editImageButton = new JButton("Change Picture");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(editImageButton, gbc);
        gbc.gridwidth = 1;

        // Name
        JLabel nameTitleLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(nameTitleLabel, gbc);
        nameLabel = new JLabel();
        nameField = new JTextField(20);
        nameField.setVisible(false);
        gbc.gridx = 1;
        add(nameLabel, gbc);
        add(nameField, gbc);

        // Username
        JLabel usernameTitleLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(usernameTitleLabel, gbc);
        usernameLabel = new JLabel();
        gbc.gridx = 1;
        add(usernameLabel, gbc);

        // Email
        JLabel emailTitleLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(emailTitleLabel, gbc);
        emailLabel = new JLabel();
        emailField = new JTextField(20);
        emailField.setVisible(false);
        gbc.gridx = 1;
        add(emailLabel, gbc);
        add(emailField, gbc);

        // Password
        JLabel passwordTitleLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(passwordTitleLabel, gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        saveButton = new JButton("Edit");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        logoutButton = new JButton("Logout");
        gbc.gridy = 7;
        add(logoutButton, gbc);
        
        loadUserProfile();
        addListener();
    }

    private void loadUserProfile() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT name, email, username, profile_picture FROM users WHERE username = ?")) {
            pst.setString(1, currentUsername);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                nameLabel.setText(rs.getString("name"));
                usernameLabel.setText(rs.getString("username"));
                emailLabel.setText(rs.getString("email"));
                nameField.setText(rs.getString("name"));
                emailField.setText(rs.getString("email"));
                imageData = rs.getBytes("profile_picture"); // Retrieve image data

                if (imageData != null) {
                    ImageIcon imageIcon = new ImageIcon(imageData);
                    Image scaledImage = imageIcon.getImage().getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(scaledImage);
                    imageLabel.setIcon(imageIcon);
                } else {
                    imageLabel.setText("No Image");
                }
            } else {
                JOptionPane.showMessageDialog(this, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                //  dispose();  //Removed dispose
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            // dispose();  //Removed dispose
        }
    }

    private void saveChanges() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement("UPDATE users SET name = ?, email = ?, profile_picture = ? WHERE username = ?")) {
            pst.setString(1, nameField.getText());
            pst.setString(2, emailField.getText());
            pst.setBytes(3, imageData);  //save image
            pst.setString(4, currentUsername);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully");
                nameLabel.setText(nameField.getText());
                emailLabel.setText(emailField.getText());

                nameField.setVisible(false);
                emailField.setVisible(false);
                nameLabel.setVisible(true);
                emailLabel.setVisible(true);
                saveButton.setText("Edit");
                isEditing = false;
                toggleEditFields(); //update view
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update profile", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void addListener() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (saveButton.getText().equals("Edit")) {
                    isEditing = true;
                    toggleEditFields();
                    saveButton.setText("Save");
                } else {
                    saveChanges();
                }
            }
        });
        
       
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEditing = false;
                toggleEditFields(); // revert to non-edit mode
                saveButton.setText("Edit");
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(ProfilePanel.this,
                        "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Assuming ProfilePanel is inside a JFrame, find the top-level frame
                    Window window = SwingUtilities.getWindowAncestor(ProfilePanel.this);
                    if (window != null) {
                        window.dispose(); // Close the window
                    }
                    new LoginFrame(); // Show the login frame again
                }
            }
        });
        editImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png");
                fileChooser.setFileFilter(filter);
                int returnVal = fileChooser.showOpenDialog(ProfilePanel.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        //read the image file
                        BufferedImage img = ImageIO.read(selectedFile);
                        //scale the image
                        Image scaledImage = img.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH);
                        ImageIcon imageIcon = new ImageIcon(scaledImage);
                        imageLabel.setIcon(imageIcon);  //display image
                        //store the image data
                        imageData = imageToBytes(selectedFile);

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(ProfilePanel.this, "Error reading image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });
        
    }

    // Convert image file to byte array
    private byte[] imageToBytes(File imageFile) throws IOException {
        FileInputStream fis = new FileInputStream(imageFile);
        byte[] buffer = new byte[(int) imageFile.length()];
        fis.read(buffer);
        fis.close();
        return buffer;
    }

    private void toggleEditFields() {
        nameLabel.setVisible(!isEditing);
        nameField.setVisible(isEditing);
        emailLabel.setVisible(!isEditing);
        emailField.setVisible(isEditing);
    }
}
