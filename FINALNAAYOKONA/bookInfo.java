package FINALNAAYOKONA;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class bookInfo extends JPanel {

    private JLabel titleLabel;
    private JLabel authorLabel;
    private JLabel isbnLabel;
    private JLabel publishedDateLabel;
    private JLabel publisherLabel;
    private JLabel stockLabel;
    private JLabel priceLabel;
    private JTextArea descriptionTextArea;
    private JPanel actionsPanel;
    private JLabel imageLabel;
    private static final int IMAGE_WIDTH = 120;
    private static final int IMAGE_HEIGHT = 180;
    private JButton addToCartButton;
    private JButton addToFavoritesButton;
    private userDashboard parent; // Reference to the parent frame
    private Book currentBook; // Store the currently displayed book

    public bookInfo(userDashboard parent) {
        this.parent = parent;
        setLayout(new BorderLayout(15, 15)); // Use BorderLayout for overall structure with gaps
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding around the panel
        setBackground(new Color(245, 245, 245)); // Light background

        // Top Section for Image and Basic Info (FlowLayout for responsiveness)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 20, 10)); // Image to the left, info to the right
        topPanel.setOpaque(false); // Make it transparent if the parent has a background

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204))); // Subtle border for image
        topPanel.add(imageLabel);

        JPanel infoPanel = new JPanel(new GridLayout(7, 2, 5, 5)); // Grid for labels and values
        infoPanel.setOpaque(false);

        Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Title
        JLabel titleHeaderLabel = new JLabel("Title:");
        titleHeaderLabel.setFont(headerFont);
        infoPanel.add(titleHeaderLabel);
        titleLabel = new JLabel();
        titleLabel.setFont(valueFont);
        infoPanel.add(titleLabel);

        // Author
        JLabel authorHeaderLabel = new JLabel("Author:");
        authorHeaderLabel.setFont(headerFont);
        infoPanel.add(authorHeaderLabel);
        authorLabel = new JLabel();
        authorLabel.setFont(valueFont);
        infoPanel.add(authorLabel);

        // ISBN
        JLabel isbnHeaderLabel = new JLabel("ISBN:");
        isbnHeaderLabel.setFont(headerFont);
        infoPanel.add(isbnHeaderLabel);
        isbnLabel = new JLabel();
        isbnLabel.setFont(valueFont);
        infoPanel.add(isbnLabel);

        // Published Date
        JLabel publishedDateHeaderLabel = new JLabel("Published:");
        publishedDateHeaderLabel.setFont(headerFont);
        infoPanel.add(publishedDateHeaderLabel);
        publishedDateLabel = new JLabel();
        publishedDateLabel.setFont(valueFont);
        infoPanel.add(publishedDateLabel);

        // Publisher
        JLabel publisherHeaderLabel = new JLabel("Publisher:");
        publisherHeaderLabel.setFont(headerFont);
        infoPanel.add(publisherHeaderLabel);
        publisherLabel = new JLabel();
        publisherLabel.setFont(valueFont);
        infoPanel.add(publisherLabel);

        // Stock
        JLabel stockHeaderLabel = new JLabel("Stock:");
        stockHeaderLabel.setFont(headerFont);
        infoPanel.add(stockHeaderLabel);
        stockLabel = new JLabel();
        stockLabel.setFont(valueFont);
        infoPanel.add(stockLabel);

        // Price
        JLabel priceHeaderLabel = new JLabel("Price:");
        priceHeaderLabel.setFont(headerFont);
        infoPanel.add(priceHeaderLabel);
        priceLabel = new JLabel();
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setForeground(new Color(0, 128, 0)); // Green for price
        infoPanel.add(priceLabel);

        topPanel.add(infoPanel);
        add(topPanel, BorderLayout.NORTH);

        // Center Section for Description (JScrollPane for scrollability)
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setOpaque(false);
        JLabel descriptionHeaderLabel = new JLabel("Description");
        descriptionHeaderLabel.setFont(headerFont);
        descriptionPanel.add(descriptionHeaderLabel, BorderLayout.NORTH);
        descriptionTextArea = new JTextArea();
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setFont(valueFont);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea);
        descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);
        add(descriptionPanel, BorderLayout.CENTER);

        // Bottom Section for Action Buttons (FlowLayout for centering)
        actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionsPanel.setOpaque(false);

        addToCartButton = new JButton("Add to Cart");
        styleButton(addToCartButton, new Color(0, 102, 204), Color.WHITE); // Blue button
        actionsPanel.add(addToCartButton);

        addToFavoritesButton = new JButton("Add to Favorites");
        styleButton(addToFavoritesButton, new Color(255, 165, 0), Color.WHITE); // Orange button
        actionsPanel.add(addToFavoritesButton);

        add(actionsPanel, BorderLayout.SOUTH);

        // Action Listeners for buttons
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentBook != null) {
                    parent.addToCart(currentBook.getBookId(), currentBook.getName(), currentBook.getPrice());
                } else {
                    JOptionPane.showMessageDialog(bookInfo.this, "No book details to add to cart.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        addToFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentBook != null) {
                    parent.addToFavorites(currentBook);
                } else {
                    JOptionPane.showMessageDialog(bookInfo.this, "No book details to add to favorites.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private void styleButton(JButton button, Color backgroundColor, Color foregroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    }

    public void setBookDetails(String title, String author, String isbn, String publishedDate, String publisher, int stock, double price, String description, byte[] imageData, int bookId) {
        this.titleLabel.setText(title);
        this.authorLabel.setText(author);
        this.isbnLabel.setText(isbn);
        this.publishedDateLabel.setText(publishedDate);
        this.publisherLabel.setText(publisher);
        this.stockLabel.setText(String.valueOf(stock));
        this.priceLabel.setText("$" + String.format("%.2f", price));
        this.descriptionTextArea.setText(description);

        if (imageData != null) {
            ImageIcon imageIcon = new ImageIcon(imageData);
            Image scaledImage = imageIcon.getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            this.imageLabel.setIcon(new ImageIcon(scaledImage));
            this.imageLabel.setText("");
        } else {
            this.imageLabel.setIcon(null);
            this.imageLabel.setText("No Image");
            this.imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        }

        // Create a Book object and store it
        this.currentBook = new Book(bookId, title, author, price, imageData);
    }

    public void setButtonsPanel(JPanel newButtonPanel) {
        if (actionsPanel != null) {
            remove(actionsPanel);
        }
        actionsPanel = newButtonPanel;
        add(actionsPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    public void clearBookDetails() {
        titleLabel.setText("");
        authorLabel.setText("");
        isbnLabel.setText("");
        publishedDateLabel.setText("");
        publisherLabel.setText("");
        stockLabel.setText("");
        priceLabel.setText("");
        descriptionTextArea.setText("");
        imageLabel.setIcon(null);
        imageLabel.setText("");
        currentBook = null; // Clear the current book

        if (actionsPanel != null) {
            remove(actionsPanel);
            actionsPanel = null;
        }
        revalidate();
        repaint();
    }
}