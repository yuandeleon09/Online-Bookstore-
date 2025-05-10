package FINALNAAYOKONA;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SalesReport extends JPanel {

    private JPanel mainPanel;
    private JLabel totalRevenueLabel;
    private JLabel totalBooksSoldLabel;
    private JLabel totalStockLeftLabel;
    private JTable transactionTable;
    private JTable stockTable;
    private JScrollPane transactionScrollPane;
    private JScrollPane stockScrollPane;
    private DefaultTableModel transactionTableModel;
    private DefaultTableModel stockTableModel;

    public SalesReport() {
        setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 1));

        // Labels for totals
        totalRevenueLabel = new JLabel("Total Revenue: ");
        totalBooksSoldLabel = new JLabel("Total Books Sold: ");
        totalStockLeftLabel = new JLabel("Total Stock Left: ");
        mainPanel.add(totalRevenueLabel);
        mainPanel.add(totalBooksSoldLabel);
        mainPanel.add(totalStockLeftLabel);

        // Table for transactions
        String[] transactionColumnNames = {"Transaction ID", "Book Name", "Quantity", "Price", "Payment Method", "Checkout Date"};
        transactionTableModel = new DefaultTableModel(transactionColumnNames, 0);
        transactionTable = new JTable(transactionTableModel);
        transactionScrollPane = new JScrollPane(transactionTable);
        mainPanel.add(transactionScrollPane);

        // Table for stock and genre
        String[] stockColumnNames = {"Genre", "Total Stock"};
        stockTableModel = new DefaultTableModel(stockColumnNames, 0);
        stockTable = new JTable(stockTableModel);
        stockScrollPane = new JScrollPane(stockTable);
        mainPanel.add(stockScrollPane);

        add(mainPanel, BorderLayout.CENTER);

        updateReport(); // Initial update of the report
    }

    public void updateReport() {
        // Clear current data
        transactionTableModel.setRowCount(0);
        stockTableModel.setRowCount(0);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction for data consistency

            // 1. Calculate and display total revenue and books sold from transactions table
            String transactionQuery = "SELECT SUM(price * quantity), SUM(quantity) FROM transactions WHERE checkout_date IS NOT NULL";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(transactionQuery);
            double totalRevenue = 0;
            int totalBooksSold = 0;
            if (rs.next()) {
                totalRevenue = rs.getDouble(1);
                totalBooksSold = rs.getInt(2);
            }
            totalRevenueLabel.setText("Total Revenue: $" + String.format("%.2f", totalRevenue));
            totalBooksSoldLabel.setText("Total Books Sold: " + totalBooksSold);
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();

            // 2. Calculate and display total stock left from books table
            String stockQuery = "SELECT SUM(stock) FROM books";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(stockQuery);
            int totalStockLeft = 0;
            if (rs.next()) {
                totalStockLeft = rs.getInt(1);
            }
            totalStockLeftLabel.setText("Total Stock Left: " + totalStockLeft);
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();

            // 3. Populate the transaction table
            String transactionDetailsQuery = "SELECT transaction_id, book_name, quantity, price, payment_method, checkout_date FROM transactions WHERE checkout_date IS NOT NULL";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(transactionDetailsQuery);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (rs.next()) {
                int transactionId = rs.getInt("transaction_id");
                String bookName = rs.getString("book_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String paymentMethod = rs.getString("payment_method");
                java.sql.Date sqlDate = rs.getDate("checkout_date");
                String formattedDate = sqlDate != null ? sqlDate.toLocalDate().format(formatter) : "";
                transactionTableModel.addRow(new Object[]{transactionId, bookName, quantity, price, paymentMethod, formattedDate});
            }
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();

            // 4. Populate the stock table with genre and stock information
            String genreStockQuery = "SELECT genre, SUM(stock) AS total_stock FROM books GROUP BY genre";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(genreStockQuery);
            while (rs.next()) {
                String genre = rs.getString("genre");
                int totalStock = rs.getInt("total_stock");
                stockTableModel.addRow(new Object[]{genre, totalStock});
            }
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();

            conn.commit(); // Commit the transaction
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}