package FINALNAAYOKONA;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Favorites extends JPanel {

    private JTable favoritesTable;
    private DefaultTableModel tableModel;
    private List<Book> favoriteBooks; // Store Book objects

    public Favorites() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tableModel = new DefaultTableModel(new Object[]{"Book ID", "Name", "Author", "Price"}, 0);
        favoritesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(favoritesTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton removeFromFavoritesButton = new JButton("Remove from Favorites");
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

