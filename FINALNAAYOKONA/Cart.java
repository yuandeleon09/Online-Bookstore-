package FINALNAAYOKONA;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart extends JPanel {
    private JPanel itemsPanel;
    private JScrollPane scrollPane;
    private JLabel totalLabel;
    private double total;
    private Map<Book, Integer> cartItems;
    private List<CartItem> cartItemList;
    private userDashboard parent;
    private static final int IMAGE_WIDTH = 100;
    private static final int IMAGE_HEIGHT = 150;

    public Cart() {
        setLayout(new BorderLayout());

        itemsPanel = new JPanel();
        itemsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        scrollPane = new JScrollPane(itemsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(new Color(0, 122, 255));
        totalLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        cartItems = new HashMap<>();
        cartItemList = new ArrayList<>();
        total = 0.0;

        add(scrollPane, BorderLayout.CENTER);
        add(totalLabel, BorderLayout.SOUTH);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        checkoutButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        checkoutButton.setBackground(new Color(0, 122, 255));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setBorderPainted(false);
        checkoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<CartItem> selectedCartItems = getSelectedCartItems();
                if (selectedCartItems.isEmpty()) {
                    JOptionPane.showMessageDialog(Cart.this, "Please select items to checkout!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                Checkout checkoutWindow = new Checkout(selectedCartItems, Cart.this, parent);
                checkoutWindow.setVisible(true);
            }
        });
        JPanel checkoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        checkoutPanel.add(checkoutButton);
        checkoutPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(checkoutPanel, BorderLayout.NORTH);

        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        updateTotal();
    }

    public void setParent(userDashboard parent) {
        this.parent = parent;
    }

    public List<CartItem> getCartItems() {
        return cartItemList;
    }

    public void addBook(Book book, int quantity) {
        // Fetch image data from the database
        byte[] imageData = getImageDataFromDatabase(book.getBookId());
        if (cartItems.containsKey(book)) {
            cartItems.put(book, cartItems.get(book) + quantity);
            for (CartItem item : cartItemList) {
                if (item.getBook().equals(book)) {
                    item.setQuantity(cartItems.get(book));
                    item.getBook().setImageData(imageData); //update image
                    break;
                }
            }
        } else {
            book.setImageData(imageData); //set image
            cartItems.put(book, quantity);
            cartItemList.add(new CartItem(book, quantity));
        }
        updateItemsPanel();
        updateTotal();
        if (parent != null) {
            parent.updateCartButtonText();
        }
    }

    private byte[] getImageDataFromDatabase(int bookId) {
        String query = "SELECT book_image FROM books WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("book_image");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public void removeBook(Book book) {
        cartItems.remove(book);
        cartItemList.removeIf(item -> item.getBook().equals(book));
        updateItemsPanel();
        updateTotal();
        if (parent != null) {
            parent.updateCartButtonText();
        }
    }

    public void removeBooks(List<Book> books) {
        for (Book bookToRemove : books) {
            cartItems.remove(bookToRemove);
        }
        cartItemList.clear();
        for (Map.Entry<Book, Integer> entry : cartItems.entrySet()) {
            cartItemList.add(new CartItem(entry.getKey(), entry.getValue()));
        }
        updateItemsPanel();
        updateTotal();
        if (parent != null) {
            parent.updateCartButtonText();
        }
    }

    public void clearCart() {
        cartItems.clear();
        cartItemList.clear();
        updateItemsPanel();
        updateTotal();
        if (parent != null) {
            parent.updateCartButtonText();
        }
    }

    private List<CartItem> getSelectedCartItems() {
        List<CartItem> selectedItems = new ArrayList<>();
        for (Component comp : itemsPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel itemCard = (JPanel) comp;
                CartItem cartItem = (CartItem) itemCard.getClientProperty("cartItem");
                for (Component innerComp : itemCard.getComponents()) {
                    if (innerComp instanceof JCheckBox) {
                        JCheckBox checkBox = (JCheckBox) innerComp;
                        if (checkBox.isSelected() && cartItem != null) {
                            selectedItems.add(cartItem);
                        }
                        break;
                    }
                }
            }
        }
        return selectedItems;
    }

    private void updateItemsPanel() {
        itemsPanel.removeAll();
        for (CartItem item : cartItemList) {
            Book book = item.getBook();
            JPanel itemCard = new JPanel();
            itemCard.setLayout(new BoxLayout(itemCard, BoxLayout.Y_AXIS));
            Border lineBorder = BorderFactory.createLineBorder(new Color(220, 220, 220));
            Border paddingBorder = BorderFactory.createEmptyBorder(15, 15, 15, 15);
            itemCard.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));
            itemCard.setPreferredSize(new Dimension(250, 350));
            itemCard.putClientProperty("cartItem", item);
            itemCard.setBackground(Color.WHITE);

            JLabel imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));

            if (book.getImageData() != null) {
                try {
                    ImageIcon imageIcon = new ImageIcon(book.getImageData());
                    Image scaledImage = imageIcon.getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(scaledImage);
                    imageLabel.setIcon(imageIcon);
                } catch (Exception e) {
                    e.printStackTrace();
                    imageLabel.setText("Error");
                    imageLabel.setVerticalAlignment(SwingConstants.CENTER);
                }
            } else {
                imageLabel.setText("No Image");
                imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            }
            itemCard.add(imageLabel);

            JLabel titleLabel = new JLabel("Title: " + book.getName());
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setForeground(new Color(51, 51, 51));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel priceLabel = new JLabel("Price: $" + String.format("%.2f", book.getPrice()));
            priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            priceLabel.setForeground(new Color(0, 122, 255));
            priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel subtotalLabel = new JLabel("Subtotal: $" + String.format("%.2f", book.getPrice() * item.getQuantity()));
            subtotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            subtotalLabel.setForeground(new Color(51, 51, 51));
            subtotalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel quantityLabelText = new JLabel("Quantity:");
            quantityLabelText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            quantityLabelText.setForeground(new Color(51, 51, 51));
            quantityLabelText.setAlignmentX(Component.LEFT_ALIGNMENT);

            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(item.getQuantity(), 1, book.getStock(), 1));
            quantitySpinner.setPreferredSize(new Dimension(80, 30));
            quantitySpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            quantitySpinner.setAlignmentX(Component.LEFT_ALIGNMENT);

            quantitySpinner.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    int newQuantity = (int) quantitySpinner.getValue();
                    item.setQuantity(newQuantity);
                    cartItems.put(book, newQuantity);
                    subtotalLabel.setText("Subtotal: $" + String.format("%.2f", book.getPrice() * newQuantity));
                    updateTotal();
                    if (parent != null) {
                        parent.updateCartButtonText();
                    }
                }
            });

            JButton removeButton = new JButton("Remove");
            removeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            removeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            removeButton.setBackground(new Color(220, 0, 0));
            removeButton.setForeground(Color.WHITE);
            removeButton.setBorderPainted(false);
            removeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeBook(book);
                }
            });

            JCheckBox selectCheckbox = new JCheckBox("Select");
            selectCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
            selectCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            selectCheckbox.setForeground(new Color(51, 51, 51));

            itemCard.add(selectCheckbox);
            itemCard.add(titleLabel);
            itemCard.add(priceLabel);
            itemCard.add(quantityLabelText);
            itemCard.add(quantitySpinner);
            itemCard.add(subtotalLabel);
            itemCard.add(Box.createVerticalGlue());
            itemCard.add(removeButton);

            itemsPanel.add(itemCard);
        }
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    private void updateTotal() {
        total = 0;
        for (CartItem item : cartItemList) {
            total += item.getBook().getPrice() * item.getQuantity();
        }
        totalLabel.setText("Total: $" + String.format("%.2f", total));
    }
}

