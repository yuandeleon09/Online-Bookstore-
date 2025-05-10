package FINALNAAYOKONA;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageOrders extends JPanel {

    private JTable orderTable;
    private DefaultTableModel tableModel;

    public ManageOrders() {
        setLayout(new BorderLayout());
        initializeTable();
        loadOrders();
    }

    private void initializeTable() {
        // Define the columns based on the transaction table structure
        String[] columns = {"transaction_id", "book_id", "book_name", "price", "payment_method", "checkout_date", "quantity"};
        tableModel = new DefaultTableModel(columns, 0);
        orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadOrders() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // SQL query to fetch data from the transactions table
            String query = "SELECT transaction_id, book_id, book_name, price, payment_method, checkout_date, quantity, status FROM transactions";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                tableModel.setRowCount(0); // Clear the table before loading data

                // Iterate through the result set and add rows to the table model
                while (resultSet.next()) {
                    Object[] row = {
                            resultSet.getInt("transaction_id"),
                            resultSet.getInt("book_id"),
                            resultSet.getString("book_name"),
                            resultSet.getDouble("price"),
                            resultSet.getString("payment_method"),
                            resultSet.getDate("checkout_date"),
                            resultSet.getInt("quantity"),
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading order data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // It's good practice to print the stack trace for debugging
        }
    }
}

