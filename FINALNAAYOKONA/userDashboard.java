package FINALNAAYOKONA;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

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
    private String currentUsername;
    private JButton profileButton;
    private JLabel logoLabel;
    private static final String LOGO_FILE_PATH = "C:\\Users\\yuan deleon\\Downloads\\Logo_po-removebg-preview.png";
    private JButton aboutUsButton;

    public userDashboard(String username) {
        this.currentUsername = username;
        setTitle("User Dashboard");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        Color lightBrown = new Color(210, 180, 140); // Tan
        Color lightBrownHover = new Color(190, 160, 120); // Slightly darker for hover

        headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(lightBrown);
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

     // Create buttons (background set in createIconButton)
        dashboardButton = createIconButton("Home", "C:\\Users\\yuan deleon\\eclipse-workspace\\OBSS\\src\\icons\\home.png", lightBrown, lightBrownHover);
        logoLabel = new JLabel();
        setLogo();

        searchField = new JTextField(10);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchButton = createIconButton("Search", "C:\\Users\\yuan deleon\\eclipse-workspace\\OBSS\\src\\icons\\search.png", lightBrown, lightBrownHover);

        cartButton = createIconButton("Cart (0)", "C:\\Users\\yuan deleon\\eclipse-workspace\\OBSS\\src\\icons\\shopping-cart.png", lightBrown, lightBrownHover);
        favoritesButton = createIconButton("Favorites", "C:\\Users\\yuan deleon\\eclipse-workspace\\OBSS\\src\\icons\\star.png", lightBrown, lightBrownHover);
        profileButton = createIconButton("My Profile", "C:\\Users\\yuan deleon\\eclipse-workspace\\OBSS\\src\\icons\\user.png", lightBrown, lightBrownHover);
        aboutUsButton = createIconButton("About Us", "C:\\Users\\yuan deleon\\eclipse-workspace\\OBSS\\src\\icons\\users-alt.png", lightBrown, lightBrownHover);


        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(lightBrown);
        leftPanel.add(dashboardButton);
        leftPanel.add(logoLabel);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        centerPanel.setBackground(lightBrown);
        centerPanel.add(new JLabel("Search:"));
        centerPanel.add(searchField);
        centerPanel.add(searchButton);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(lightBrown);
        rightPanel.add(cartButton);
        rightPanel.add(favoritesButton);
        rightPanel.add(profileButton);
        rightPanel.add(aboutUsButton);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(centerPanel, BorderLayout.CENTER);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        bookListPanel = new JPanel(new GridLayout(0, COLUMNS_PER_ROW, 20, 30));
        scrollPane = new JScrollPane(bookListPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        bookInfoPanel = new bookInfo(this);
        favoritesPanel = new Favorites();
        cartPanel = new Cart();
        cartPanel.setParent(this);
        AboutUs aboutUsPanel = new AboutUs();

        contentPanel.add(scrollPane, "bookList");
        contentPanel.add(bookInfoPanel, "bookInfo");
        contentPanel.add(favoritesPanel, "favorites");
        contentPanel.add(cartPanel, "cart");
        contentPanel.add(aboutUsPanel, "aboutUs");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        allBooks = new ArrayList<>();
        loadBooks();
        updateCartButtonText();

        add(mainPanel);
        cardLayout.show(contentPanel, "bookList");

        addListeners();
    }

    private void setLogo() {
        try {
            File logoFile = new File(LOGO_FILE_PATH);
            if (logoFile.exists()) {
                Image logoImage = ImageIO.read(logoFile);
                Image scaledLogo = logoImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaledLogo));
            } else {
                logoLabel.setText("Logo");
            }
        } catch (IOException e) {
            logoLabel.setText("Logo");
        }
    }

    private JButton createIconButton(String text, String iconFilePath, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(10);

        try {
            File imageFile = new File(iconFilePath);
            if (imageFile.exists()) {
                BufferedImage image = ImageIO.read(imageFile);
                Image scaled = image.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaled));
            } else {
                System.out.println("Icon file not found: " + iconFilePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    

    private void addListeners() {
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
            @Override
            public void actionPerformed(ActionEvent e) {
                ProfileFrame profile = new ProfileFrame(currentUsername, userDashboard.this); // pass current instance
                profile.setVisible(true);
            }
        });



        aboutUsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "aboutUs");	
            }
        });
    }
    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(new Color(33, 150, 243)); // Blue color
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(30, 136, 229)); // Darker blue
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(33, 150, 243)); // Original blue
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

   

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new userDashboard("testuser").setVisible(true);
            }
        });
    }

	public void addToFavorites(Book currentBook) {
		// TODO Auto-generated method stub
		
	}
}