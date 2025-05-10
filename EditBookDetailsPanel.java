package FINALNAAYOKONA;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditBookDetailsPanel extends JPanel {

    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JButton editButton;
    private JDialog editDialog;
    private JLabel bookIdLabel, nameLabel, authorLabel, genreLabel, isbnLabel, publishedDateLabel, publisherLabel, stockLabel, priceLabel, descriptionLabel;
    private JTextField bookIdField, nameField, authorField, isbnField, genreField, publishedDateField, publisherField, stockField, priceField;
    private JTextArea descriptionTextArea;
    private JButton saveButton, cancelButton;
    private String selectedISBN;

    public EditBookDetailsPanel() {
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel();
        tableModel.addColumn("book_id");
        tableModel.addColumn("name");
        tableModel.addColumn("author");
        tableModel.addColumn("genre");
        tableModel.addColumn("isbn");
        tableModel.addColumn("published_date");
        tableModel.addColumn("publisher");
        tableModel.addColumn("stock");
        tableModel.addColumn("price");
        tableModel.addColumn("description");
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        editButton = new JButton("Edit Book");
        buttonPanel.add(editButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Edit dialog setup (initially invisible)
        editDialog = new JDialog();
        editDialog.setTitle("Edit Book Details");
        editDialog.setSize(600, 400);
        editDialog.setLocationRelativeTo(this);
        editDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize labels and fields for the dialog
        bookIdLabel = new JLabel("book_id:");
        bookIdField = new JTextField(20);
        bookIdField.setEditable(false);
        nameLabel = new JLabel("name:");
        nameField = new JTextField(20);
        authorLabel = new JLabel("author:");
        authorField = new JTextField(20);
        isbnLabel = new JLabel("isbn:");
        isbnField = new JTextField(20);
        isbnField.setEditable(false);
        genreLabel = new JLabel("genre:");
        genreField = new JTextField(20);
        publisherLabel = new JLabel("publisher:");
        publisherField = new JTextField(20);
        publishedDateLabel = new JLabel("published_date:");
        publishedDateField = new JTextField(20);
        descriptionLabel = new JLabel("description:");
        descriptionTextArea = new JTextArea(5, 20);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea);
        priceLabel = new JLabel("price:");
        priceField = new JTextField(20);
        stockLabel = new JLabel("stock:");
        stockField = new JTextField(20);
        saveButton = new JButton("Save Changes");
        cancelButton = new JButton("Cancel");

        // Add components to the dialog's GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0; editDialog.add(bookIdLabel, gbc);
        gbc.gridx = 1; editDialog.add(bookIdField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; editDialog.add(nameLabel, gbc);
        gbc.gridx = 1; editDialog.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; editDialog.add(authorLabel, gbc);
        gbc.gridx = 1; editDialog.add(authorField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; editDialog.add(isbnLabel, gbc);
        gbc.gridx = 1; editDialog.add(isbnField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; editDialog.add(genreLabel, gbc);
        gbc.gridx = 1; editDialog.add(genreField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; editDialog.add(publisherLabel, gbc);
        gbc.gridx = 1; editDialog.add(publisherField, gbc);
        gbc.gridx = 0; gbc.gridy = 6; editDialog.add(publishedDateLabel, gbc);
        gbc.gridx = 1; editDialog.add(publishedDateField, gbc);
        gbc.gridx = 0; gbc.gridy = 7; editDialog.add(descriptionLabel, gbc);
        gbc.gridx = 1; editDialog.add(descriptionScrollPane, gbc);
        gbc.gridx = 0; gbc.gridy = 8; editDialog.add(priceLabel, gbc);
        gbc.gridx = 1; editDialog.add(priceField, gbc);
        gbc.gridx = 0; gbc.gridy = 9; editDialog.add(stockLabel, gbc);
        gbc.gridx = 1; editDialog.add(stockField, gbc);
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        JPanel dialogButtonPanel = new JPanel();
        dialogButtonPanel.add(saveButton);
        dialogButtonPanel.add(cancelButton);
        editDialog.add(dialogButtonPanel, gbc);
        gbc.gridwidth = 1;

        loadBookData();
        addListener();
    }

    private void loadBookData() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT book_id, name, author, genre, isbn, published_date, publisher, stock, price, description FROM books")) {
            ResultSet rs = ps.executeQuery();
            tableModel.setRowCount(0); // Clear the table
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("book_id"),
                        rs.getString("name"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getString("isbn"),
                        rs.getDate("published_date"),
                        rs.getString("publisher"),
                        rs.getInt("stock"),
                        rs.getDouble("price"),
                        rs.getString("description")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading book data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadBookDetailsForEdit(String isbn) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT book_id, name, author, isbn, genre, publisher, published_date, description, price, stock FROM books WHERE isbn = ?")) {
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bookIdField.setText(String.valueOf(rs.getInt("book_id")));
                nameField.setText(rs.getString("name"));
                authorField.setText(rs.getString("author"));
                isbnField.setText(rs.getString("isbn"));
                genreField.setText(rs.getString("genre"));
                publisherField.setText(rs.getString("publisher"));
                publishedDateField.setText(rs.getDate("published_date").toString());
                descriptionTextArea.setText(rs.getString("description"));
                priceField.setText(String.valueOf(rs.getDouble("price")));
                stockField.setText(String.valueOf(rs.getInt("stock")));
            } else {
                JOptionPane.showMessageDialog(this, "Book not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading book details: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void saveChanges() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE books SET name = ?, author = ?, genre = ?, publisher = ?, published_date = ?, description = ?, price = ?, stock = ? WHERE isbn = ?")) {
            ps.setString(1, nameField.getText());
            ps.setString(2, authorField.getText());
            ps.setString(3, genreField.getText());
            ps.setString(4, publisherField.getText());
            ps.setString(5, publishedDateField.getText());
            ps.setString(6, descriptionTextArea.getText());
            ps.setDouble(7, Double.parseDouble(priceField.getText()));
            ps.setInt(8, Integer.parseInt(stockField.getText()));
            ps.setString(9, isbnField.getText());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Book details updated successfully");
                loadBookData(); // Refresh the table
                editDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update book details", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating book details: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void addListener() {
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedISBN = (String) bookTable.getValueAt(selectedRow, 4); // Get ISBN
                    loadBookDetailsForEdit(selectedISBN);
                    editDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(EditBookDetailsPanel.this, "Please select a book to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChanges();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDialog.dispose();
            }
        });
    }
}

