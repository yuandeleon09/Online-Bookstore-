package FINALNAAYOKONA;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ManageInventoryPanel extends JPanel {

    private JButton addBookButton;
    private JButton removeBookButton;
    private JLabel imagePreviewLabel;
    private File selectedImageFile = null;

    public ManageInventoryPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));

        addBookButton = new JButton("Add Book");
        removeBookButton = new JButton("Remove Book");

        add(addBookButton);
        add(removeBookButton);

        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddBookDialog();
            }
        });

        removeBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRemoveBookDialog();
            }
        });
    }

    private void showAddBookDialog() {
        JDialog addBookDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add New Book", true);
        addBookDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST; // Align labels to the left
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make text fields fill horizontal space

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);
        JLabel authorLabel = new JLabel("Author:");
        JTextField authorField = new JTextField(20);
        JLabel genreLabel = new JLabel("Genre:");
        JTextField genreField = new JTextField(20);
        JLabel isbnLabel = new JLabel("ISBN:");
        JTextField isbnField = new JTextField(15);
        JLabel publishedDateLabel = new JLabel("Published Date (YYYY-MM-DD):");
        JTextField publishedDateField = new JTextField(10);
        JLabel publisherLabel = new JLabel("Publisher:");
        JTextField publisherField = new JTextField(20);
        JLabel stockLeftLabel = new JLabel("Stock Left:");
        JSpinner stockLeftSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(10);
        JLabel descriptionLabel = new JLabel("Description:");
        JTextArea descriptionTextArea = new JTextArea(5, 20);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea);
        JLabel imageLabel = new JLabel("Book Image:");
        JButton selectImageButton = new JButton("Select Image");
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setPreferredSize(new Dimension(100, 150));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);
        buttonPanel.add(addButton);

        int row = 0;

        // Name
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1; // Label occupies one column
        addBookDialog.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0; // Allocate space for the text field
        addBookDialog.add(nameField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.weightx = 0.0; // Reset weight

        // Author
        gbc.gridx = 0;
        gbc.gridy = row;
        addBookDialog.add(authorLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        addBookDialog.add(authorField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;

        // Genre
        gbc.gridx = 0;
        gbc.gridy = row;
        addBookDialog.add(genreLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        addBookDialog.add(genreField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;

        // ISBN
        gbc.gridx = 0;
        gbc.gridy = row;
        addBookDialog.add(isbnLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        addBookDialog.add(isbnField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;

        // Published Date
        gbc.gridx = 0;
        gbc.gridy = row;
        addBookDialog.add(publishedDateLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        addBookDialog.add(publishedDateField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;

        // Publisher
        gbc.gridx = 0;
        gbc.gridy = row;
        addBookDialog.add(publisherLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        addBookDialog.add(publisherField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;

        // Stock Left
        gbc.gridx = 0;
        gbc.gridy = row;
        addBookDialog.add(stockLeftLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        addBookDialog.add(stockLeftSpinner, gbc);

        row++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;

        // Price
        gbc.gridx = 0;
        gbc.gridy = row;
        addBookDialog.add(priceLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        addBookDialog.add(priceField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST; // Align description label to top-left

        // Description
        gbc.gridx = 0;
        gbc.gridy = row;
        addBookDialog.add(descriptionLabel, gbc);
        gbc.gridx = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        addBookDialog.add(descriptionScrollPane, gbc);

        row++;
        gbc.gridx = 0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Book Image
        gbc.gridx = 0;
        gbc.gridy = row;
        addBookDialog.add(imageLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        addBookDialog.add(selectImageButton, gbc);

        row++;
        gbc.gridx = 1;
        addBookDialog.add(imagePreviewLabel, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2; // Buttons span both columns
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST; // Align buttons to the right
        addBookDialog.add(buttonPanel, gbc);

        selectImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(addBookDialog);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedImageFile = fileChooser.getSelectedFile();
                    ImageIcon icon = new ImageIcon(new ImageIcon(selectedImageFile.getAbsolutePath())
                            .getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH));
                    imagePreviewLabel.setIcon(icon);
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String author = authorField.getText();
                String genre = genreField.getText();
                String isbn = isbnField.getText();
                String publishedDateStr = publishedDateField.getText();
                String publisher = publisherField.getText();
                int stockLeft = (int) stockLeftSpinner.getValue();
                String priceStr = priceField.getText();
                String description = descriptionTextArea.getText();

                if (name.isEmpty() || author.isEmpty() || isbn.isEmpty() || priceStr.isEmpty()) {
                    JOptionPane.showMessageDialog(addBookDialog, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double price = Double.parseDouble(priceStr);
                    java.sql.Date sqlPublishedDate = null;
                    if (!publishedDateStr.isEmpty()) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date parsedDate = dateFormat.parse(publishedDateStr);
                        sqlPublishedDate = new java.sql.Date(parsedDate.getTime());
                    }

                    if (storeBookInDatabase(name, author, genre, isbn, sqlPublishedDate, publisher, stockLeft, price, description, selectedImageFile)) {
                        JOptionPane.showMessageDialog(addBookDialog, "Book added successfully!");
                        addBookDialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(addBookDialog, "Failed to add book.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addBookDialog, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(addBookDialog, "Invalid date format (YYYY-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBookDialog.dispose();
            }
        });

        addBookDialog.pack();
        addBookDialog.setLocationRelativeTo(this);
        addBookDialog.setVisible(true);
    }

    private void showRemoveBookDialog() {
        JDialog removeBookDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Remove Book", true);
        removeBookDialog.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Book ID", "Name", "Author", "ISBN"}, 0);
        JTable bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT book_id, name, author, isbn FROM books")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("book_id"),
                        rs.getString("name"),
                        rs.getString("author"),
                        rs.getString("isbn")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(removeBookDialog, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton removeButton = new JButton("Remove");
        JButton cancelButton = new JButton("Cancel");

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int bookIdToRemove = (int) tableModel.getValueAt(selectedRow, 0);
                    if (removeBookFromDatabase(bookIdToRemove)) {
                        JOptionPane.showMessageDialog(removeBookDialog, "Book removed successfully!");
                        tableModel.removeRow(selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(removeBookDialog, "Failed to remove book.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(removeBookDialog, "Please select a book to remove.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeBookDialog.dispose();
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(removeButton);

        removeBookDialog.add(scrollPane, BorderLayout.CENTER);
        removeBookDialog.add(buttonPanel, BorderLayout.SOUTH);

        removeBookDialog.setSize(500, 350);
        removeBookDialog.setLocationRelativeTo(this);
        removeBookDialog.setVisible(true);
    }

    private boolean storeBookInDatabase(String name, String author, String genre, String isbn, java.sql.Date publishedDate, String publisher, int stockLeft, double price, String description, File imageFile) {
        String query = "INSERT INTO books (name, author, genre, isbn, published_date, publisher, stock, price, description, book_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, name);
            pst.setString(2, author);
            pst.setString(3, genre);
            pst.setString(4, isbn);
            pst.setDate(5, publishedDate);
            pst.setString(6, publisher);
            pst.setInt(7, stockLeft);
            pst.setDouble(8, price);
            pst.setString(9, description); // Set the description

            if (imageFile != null) {
                FileInputStream fis = new FileInputStream(imageFile);
                pst.setBinaryStream(10, fis, (int) imageFile.length());
            } else {
                pst.setNull(10, Types.BLOB);
            }

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean removeBookFromDatabase(int bookId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement("DELETE FROM books WHERE book_id = ?")) {
            pst.setInt(1, bookId);
            pst.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getErrorCode() == 1451) {
                JOptionPane.showMessageDialog(this, "Cannot delete book because it has associated transactions. Delete the transactions first.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
    }

    private void addComponent(Container container, GridBagConstraints gbc, Component component, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        container.add(component, gbc);
    }
}