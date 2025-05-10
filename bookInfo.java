package FINALNAAYOKONA;

import javax.swing.*;
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
    private JPanel buttonPanel;
    private JLabel imageLabel;
    private static final int IMAGE_WIDTH = 120;
    private static final int IMAGE_HEIGHT = 180;
    private JButton addToCartButton;
    private JButton addToFavoritesButton;
    private userDashboard parent; // Add a reference to the parent frame

    public bookInfo(userDashboard parent) { // Constructor now takes a userDashboard
        this.parent = parent; // Store the reference
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Image
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(imageLabel, gbc);
        gbc.gridwidth = 1;

        // Title
        JLabel titleHeaderLabel = new JLabel("Title:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(titleHeaderLabel, gbc);
        titleLabel = new JLabel();
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(titleLabel, gbc);
        gbc.weightx = 0;

        // Author
        JLabel authorHeaderLabel = new JLabel("Author:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(authorHeaderLabel, gbc);
        authorLabel = new JLabel();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(authorLabel, gbc);

        // ISBN
        JLabel isbnHeaderLabel = new JLabel("ISBN:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        add(isbnHeaderLabel, gbc);
        isbnLabel = new JLabel();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(isbnLabel, gbc);

        // Published Date
        JLabel publishedDateHeaderLabel = new JLabel("Published Date:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        add(publishedDateHeaderLabel, gbc);
        publishedDateLabel = new JLabel();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(publishedDateLabel, gbc);

        // Publisher
        JLabel publisherHeaderLabel = new JLabel("Publisher:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        add(publisherHeaderLabel, gbc);
        publisherLabel = new JLabel();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(publisherLabel, gbc);

        // Stock
        JLabel stockHeaderLabel = new JLabel("Stock Left:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        add(stockHeaderLabel, gbc);
        stockLabel = new JLabel();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(stockLabel, gbc);

        // Price
        JLabel priceHeaderLabel = new JLabel("Price:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.NONE;
        add(priceHeaderLabel, gbc);
        priceLabel = new JLabel();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(priceLabel, gbc);

        // Description
        JLabel descriptionHeaderLabel = new JLabel("Description:");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(descriptionHeaderLabel, gbc);
        descriptionTextArea = new JTextArea();
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setEditable(false);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea);
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(descriptionScrollPane, gbc);

        // Buttons Panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        addToCartButton = new JButton("Add to Cart");
        addToFavoritesButton = new JButton("Add to Favorites");
        buttonPanel.add(addToCartButton);
        buttonPanel.add(addToFavoritesButton);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(buttonPanel, gbc);

        // Action Listeners for buttons (implementation needed)
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the book details and pass them to the parent's addToCart method
                int bookId = Integer.parseInt(isbnLabel.getText()); // Use ISBN as a unique identifier.  Consider using bookId if available.
                String title = titleLabel.getText();
                double price = Double.parseDouble(priceLabel.getText().substring(1)); // Remove the '$'
                parent.addToCart(bookId, title, price); // Call the parent's method
            }
        });

        addToFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the book details and pass them to the parent's addToFavorites method
                int bookId =  Integer.parseInt(isbnLabel.getText());  // Use ISBN as a unique identifier. Consider using bookId if available.
                String title = titleLabel.getText();
                double price = Double.parseDouble(priceLabel.getText().substring(1));
                parent.addToFavorites(bookId, title, price); // Call the parent's method
            }
        });

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.BLACK)
        ));
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
        // You might want to store bookId in bookInfo if needed later
        // this.currentBookId = bookId;
        if (imageData != null) {
            ImageIcon imageIcon = new ImageIcon(imageData);
            Image scaledImage = imageIcon.getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            this.imageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            this.imageLabel.setText("No Image");
            this.imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        }
    }

    public void setButtonsPanel(JPanel newButtonPanel) {
        if (buttonPanel != null) {
            remove(buttonPanel);
        }
        buttonPanel = newButtonPanel;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 0, 0);

        add(buttonPanel, gbc);
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

        if (buttonPanel != null) {
            remove(buttonPanel);
            buttonPanel = null;
        }
        revalidate();
        repaint();
    }
}
