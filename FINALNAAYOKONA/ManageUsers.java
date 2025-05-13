package FINALNAAYOKONA;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ManageUsers extends JPanel {

    private JTable userTable;
    private DefaultTableModel tableModel;

    public ManageUsers() {
        setLayout(new BorderLayout());
        initializeTable();
        loadUsers();
    }

    private void initializeTable() {
        // Updated column names to match the database
        String[] columns = {"name", "email", "username", "role", "add book", "remove book", "edit books", "manage users", "view sales report"};
        tableModel = new DefaultTableModel(columns, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadUsers() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Updated query to select the correct columns
            String query = "SELECT name, email, username, role FROM users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                tableModel.setRowCount(0);

                while (resultSet.next()) {
                    Vector<String> row = new Vector<>();
                    row.add(resultSet.getString("name"));
                    row.add(resultSet.getString("email"));
                    row.add(resultSet.getString("username"));
                    row.add(resultSet.getString("role")); //Added role
                    String role = resultSet.getString("role");
                    if ("admin".equals(role)) {
                        row.add("yes");
                        row.add("yes");
                        row.add("yes");
                        row.add("yes");
                        row.add("yes");
                    } else {
                        row.add("no");
                        row.add("no");
                        row.add("no");
                        row.add("no");
                        row.add("no");
                    }
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
