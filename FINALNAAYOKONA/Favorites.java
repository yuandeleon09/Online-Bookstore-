package FINALNAAYOKONA;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Favorites extends JPanel {

    private JTable favoritesTable;
    private DefaultTableModel tableModel;
    private List<Book> favoriteBooks; // Store Book objects

    public Favorites() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Increased border for better spacing

        // Initialize Table Model with non-editable cells
        tableModel = new DefaultTableModel(new Object[]{"Book ID", "Name", "Author", "Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        favoritesTable = new JTable(tableModel);
        favoritesTable.setFocusable(false); // Prevent cell selection highlight

        // UI Enhancements for the Table
        favoritesTable.setRowHeight(30); // Increased row height for better readability
        favoritesTable.setShowGrid(false); // Remove default grid lines
        favoritesTable.setBackground(new Color(245, 245, 245)); // Light gray background
        favoritesTable.setForeground(new Color(51, 51, 51)); // Dark gray text
        favoritesTable.setSelectionBackground(new Color(204, 204, 204)); // Light selection color
        favoritesTable.setSelectionForeground(new Color(51, 51, 51)); // Dark selection text

        // Header UI Enhancements
        JTableHeader tableHeader = favoritesTable.getTableHeader();
        tableHeader.setBackground(new Color(230, 230, 230)); // Light gray header background
        tableHeader.setForeground(new Color(0, 0, 0)); // Black header text
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 14)); // Bold header font
        tableHeader.setResizingAllowed(false); // Prevent column resizing
        tableHeader.setReorderingAllowed(false); // Prevent column reordering

        // Center align cell content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        favoritesTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Book ID
        favoritesTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Price

        JScrollPane scrollPane = new JScrollPane(favoritesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204))); // Light border for scroll pane
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)); // Added spacing to button layout
        buttonPanel.setBackground(new Color(240, 240, 240)); // Light background for button panel

        JButton removeFromFavoritesButton = new JButton("Remove from Favorites");
        removeFromFavoritesButton.setFocusPainted(false); // Remove focus border
        removeFromFavoritesButton.setBackground(new Color(255, 102, 102)); // Light red background for remove button
        removeFromFavoritesButton.setForeground(Color.WHITE); // White text
        removeFromFavoritesButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        removeFromFavoritesButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); // Padding for button
        removeFromFavoritesButton.addActionListener(e -> {
            int selectedRow = favoritesTable.getSelectedRow();
            if (selectedRow >= 0) {
                int bookIdToRemove = (int) tableModel.getValueAt(selectedRow, 0);
                removeBook(bookIdToRemove);
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to remove from favorites.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(removeFromFavoritesButton);
        add(buttonPanel, BorderLayout.SOUTH);

        favoriteBooks = new ArrayList<>();
    }

    public void addBook(Book book) {
        if (!favoriteBooks.contains(book)) { //prevent duplicates.
            favoriteBooks.add(book);
            tableModel.addRow(new Object[]{book.getBookId(), book.getName(), book.getAuthor(), book.getPrice()});
            revalidate();
            repaint();
        }
    }

    public void removeBook(int bookId) {
        for (int i = 0; i < favoriteBooks.size(); i++) {
            if (favoriteBooks.get(i).getBookId() == bookId) {
                favoriteBooks.remove(i);
                return;
            }
        }
    }

    public List<Book> getFavoriteBooks() {
        return favoriteBooks;
    }

    public void clearFavorites() {
        favoriteBooks.clear();
        tableModel.setRowCount(0); // Clear all rows
        revalidate();
        repaint();
    }
}