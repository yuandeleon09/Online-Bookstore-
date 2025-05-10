package FINALNAAYOKONA;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class BookCard extends JPanel {
    private JLabel titleLabel;
    private JLabel authorLabel;
    private JLabel priceLabel;
    private JLabel imageLabel;
    private int bookId;
    private userDashboard parent;
    private static final int IMAGE_WIDTH = 150;
    private static final int IMAGE_HEIGHT = 200;

    public BookCard(int bookId, String title, String author, double price, byte[] imageData, userDashboard parent) {
        this.bookId = bookId;
        this.parent = parent;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, 5),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setBackground(Color.WHITE);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));

        if (imageData != null) {
            ImageIcon imageIcon = new ImageIcon(imageData);
            Image scaledImage = imageIcon.getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(scaledImage);
            imageLabel.setIcon(imageIcon);
        } else {
            imageLabel.setText("No Image");
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        }
        add(imageLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        titleLabel = new JLabel("Title: " + title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(51, 51, 51));
        authorLabel = new JLabel("Author: " + author);
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        authorLabel.setForeground(new Color(102, 102, 102));
        priceLabel = new JLabel("Price: $" + String.format("%.2f", price));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(new Color(0, 122, 255));

        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);
        infoPanel.add(priceLabel);
        infoPanel.setBackground(Color.WHITE);
        add(infoPanel, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                parent.showBookDetails(bookId);
            }
        });
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(250, 350));
    }

    private static class RoundBorder extends AbstractBorder {
        private int radius;
        private int shadowSize = 5;

        public RoundBorder(int radius, int shadowSize) {
            this.radius = radius;
            this.shadowSize = shadowSize;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color shadowColor = new Color(0, 0, 0, 50);
            g2d.setColor(shadowColor);
            for (int i = 0; i < shadowSize; i++) {
                g2d.fillRoundRect(x + shadowSize - i, y + shadowSize - i, width - 2 * shadowSize + i, height - 2 * shadowSize + i, radius, radius);
            }

            g2d.setColor(c.getBackground());
            g2d.fillRoundRect(x, y, width - 2 * shadowSize, height - 2 * shadowSize, radius, radius);

            g2d.setColor(new Color(220, 220, 220));
            g2d.drawRoundRect(x, y, width - 2 * shadowSize, height - 2 * shadowSize, radius, radius);

            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = this.shadowSize;
            insets.left = this.shadowSize;
            insets.bottom = this.shadowSize;
            insets.right = this.shadowSize;
            return insets;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return getBorderInsets(c, new Insets(this.shadowSize, this.shadowSize, this.shadowSize, this.shadowSize));
        }
    }
}

public class userDashboard extends JFrame {

    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel bookListPanel;
    private JScrollPane scrollPane;
    private bookInfo bookInfoPanel;
    private CardLayout cardLayout;
    private JButton dashboardButton;
    private JTextField searchField;
    private JButton searchButton;
    private JButton cartButton;
    private JButton favoritesButton;
    private Favorites favoritesPanel;
    private Cart cartPanel;
    private List<Book> allBooks;
    private static final int COLUMNS_PER_ROW = 3;
    private String loggedInUsername;
    private JButton profileButton;
    private JLabel logoLabel;
    private static final String LOGO_FILE_PATH = "C:\\Users\\yuan deleon\\Downloads\\pexels-pixabay-87651.jpg"; // Updated file path

    public userDashboard(String username) {
        this.loggedInUsername = username;
        setTitle("User Dashboard");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());

        // Header Panel
        headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        headerPanel.setBackground(new Color(255, 255, 255));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        dashboardButton = new JButton("Dashboard");
        styleButton(dashboardButton);
        logoLabel = new JLabel();
        try {
            // Load the image
            File logoFile = new File(LOGO_FILE_PATH);
            if (logoFile.exists()) { // Check if the file exists
                Image logoImage = ImageIO.read(logoFile);
                // Scale the image
                Image scaledLogo = logoImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                ImageIcon logoIcon = new ImageIcon(scaledLogo);
                logoLabel.setIcon(logoIcon);
            } else {
                System.err.println("Logo file not found at: " + LOGO_FILE_PATH); //handle error
                logoLabel.setText("Logo");
                logoLabel.setPreferredSize(new Dimension(80, 80));
                logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                logoLabel.setVerticalAlignment(SwingConstants.CENTER);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logoLabel.setText("Logo");
            logoLabel.setPreferredSize(new Dimension(80, 80));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            logoLabel.setVerticalAlignment(SwingConstants.CENTER);
        }
        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchButton = new JButton("Search");
        styleButton(searchButton);
        cartButton = new JButton("Cart (0)");
        styleButton(cartButton);
        favoritesButton = new JButton("Favorites");
        styleButton(favoritesButton);
        profileButton = new JButton("My Profile");
        styleButton(profileButton);

        headerPanel.add(dashboardButton);
        headerPanel.add(logoLabel);
        headerPanel.add(new JLabel("Search:"));
        headerPanel.add(searchField);
        headerPanel.add(searchButton);
        headerPanel.add(cartButton);
        headerPanel.add(favoritesButton);
        headerPanel.add(profileButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content Panel with CardLayout
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        // Book List Panel
        bookListPanel = new JPanel();
        bookListPanel.setLayout(new GridLayout(0, COLUMNS_PER_ROW, 20, 30));
        scrollPane = new JScrollPane(bookListPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Initialize other panels
        bookInfoPanel = new bookInfo(this);
        favoritesPanel = new Favorites();
        cartPanel = new Cart();
        cartPanel.setParent(this);

        contentPanel.add(scrollPane, "bookList");
        contentPanel.add(bookInfoPanel, "bookInfo");
        contentPanel.add(favoritesPanel, "favorites");
        contentPanel.add(cartPanel, "cart");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        allBooks = new ArrayList<>();
        loadBooks();
        updateCartButtonText();
        add(mainPanel);
        cardLayout.show(contentPanel, "bookList");
        setVisible(true);

        // Action Listeners
        dashboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "bookList");
                loadBooks();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        searchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        cartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "cart");
            }
        });

        favoritesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "favorites");
            }
        });

        profileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openProfile();
            }
        });
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(240, 240, 240));
        button.setForeground(new Color(51, 51, 51));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(240, 240, 240));
            }
        });
    }

    private void loadBooks() {
        bookListPanel.removeAll();
        allBooks.clear();
        List<Book> books = getBooksFromDatabase();
        allBooks.addAll(books);
        bookListPanel.setLayout(new GridLayout(0, COLUMNS_PER_ROW, 20, 30));
        for (Book book : books) {
            BookCard card = new BookCard(book.getBookId(), book.getName(), book.getAuthor(), book.getPrice(), book.getImageData(), this);
            bookListPanel.add(card);
        }
        bookListPanel.revalidate();
        bookListPanel.repaint();
    }

    private List<Book> getBooksFromDatabase() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT book_id, name, author, price, book_image FROM books";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String name = rs.getString("name");
                String author = rs.getString("author");
                double price = rs.getDouble("price");
                byte[] imageData = rs.getBytes("book_image");
                books.add(new Book(bookId, name, author, price, imageData));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return books;
    }

    public void showBookDetails(int bookId) {
        String query = "SELECT book_id, name, author, isbn, published_date, publisher, stock, price, description, book_image FROM books WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int fetchedBookId = rs.getInt("book_id");
                String name = rs.getString("name");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                java.sql.Date publishedDate = rs.getDate("published_date");
                String publisher = rs.getString("publisher");
                int stock = rs.getInt("stock");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                byte[] imageData = rs.getBytes("book_image");

                String publishedDateString = (publishedDate != null) ? publishedDate.toString() : "N/A";
                bookInfoPanel.setBookDetails(name, author, isbn, publishedDateString, publisher, stock, price, description, imageData, fetchedBookId);
                cardLayout.show(contentPanel, "bookInfo");

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JButton addToCartButton = new JButton("Add to Cart");
                styleButton(addToCartButton);
                addToCartButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addToCart(fetchedBookId, name, price);
                    }
                });
                buttonPanel.add(addToCartButton);

                JButton addToFavoritesButton = new JButton("Add to Favorites");
                styleButton(addToFavoritesButton);
                addToFavoritesButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addToFavorites(fetchedBookId, name, price);
                    }
                });
                buttonPanel.add(addToFavoritesButton);

                bookInfoPanel.setButtonsPanel(buttonPanel);

            } else {
                bookInfoPanel.clearBookDetails();
                JOptionPane.showMessageDialog(this, "Book details not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performSearch() {
        String searchText = searchField.getText().toLowerCase();
        bookListPanel.removeAll();
        List<Book> searchResults = new ArrayList<>();
        for (Book book : allBooks) {
            if (book.getName().toLowerCase().contains(searchText) ||
                    book.getAuthor().toLowerCase().contains(searchText)) {
                searchResults.add(book);
            }
        }
        if (searchResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No matching books found.");
        }
        bookListPanel.setLayout(new GridLayout(0, COLUMNS_PER_ROW, 20, 30));
        for (Book book : searchResults) {
            BookCard card = new BookCard(book.getBookId(), book.getName(), book.getAuthor(), book.getPrice(), book.getImageData(), this);
            bookListPanel.add(card);
        }
        bookListPanel.revalidate();
        bookListPanel.repaint();
    }

    public void updateCartButtonText() {
        int totalItems = 0;
        for (CartItem item : cartPanel.getCartItems()) {
            totalItems += item.getQuantity();
        }
        cartButton.setText("Cart (" + totalItems + ")");
    }

    public void addToCart(int bookId, String title, double price) {
        Book book = new Book(bookId, title, price);
        cartPanel.addBook(book, 1);
        updateCartButtonText();
        JOptionPane.showMessageDialog(this, "Book added to cart!");
    }

    public void addToFavorites(int bookId, String title, double price) {
        Book book = new Book(bookId, title, price);
        favoritesPanel.addBook(book);
        JOptionPane.showMessageDialog(this, "Book added to favorites!");
    }

    public void performCheckout(List<CartItem> cartItems, String paymentMethod) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Store the transaction
            String transactionQuery = "INSERT INTO transactions (book_id, book_name, price, payment_method, checkout_date, quantity) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(transactionQuery);
            for (CartItem item : cartItems) {
                Book book = item.getBook();
                int quantity = item.getQuantity();
                pstmt.setInt(1, book.getBookId());
                pstmt.setString(2, book.getName());
                pstmt.setDouble(3, book.getPrice());
                pstmt.setString(4, paymentMethod);
                pstmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setInt(6, quantity);
                pstmt.executeUpdate();
                pstmt.close();
            }

            // 2. Decrease book stock
            for (CartItem item : cartItems) {
                Book book = item.getBook();
                int quantity = item.getQuantity();
                String updateStockQuery = "UPDATE books SET stock = stock - ? WHERE book_id = ?";
                pstmt = conn.prepareStatement(updateStockQuery);
                pstmt.setInt(1, quantity);
                pstmt.setInt(2, book.getBookId());
                pstmt.executeUpdate();
                pstmt.close();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Transaction completed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            // 3. Remove ONLY the checked out items from the cart.
            List<Book> booksToRemove = new ArrayList<>();
            for (CartItem item : cartItems) {
                booksToRemove.add(item.getBook());
            }
            cartPanel.removeBooks(booksToRemove);
            updateCartButtonText();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during checkout: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void openProfile() {
        ProfileFrame profileFrame = new ProfileFrame(loggedInUsername);
        profileFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new userDashboard("testuser").setVisible(true);
            }
        });
    }
}

