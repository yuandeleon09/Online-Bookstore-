package FINALNAAYOKONA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;

public class Checkout extends JFrame {
    private JPanel mainPanel;
    private JTextArea bookInfoTextArea;
    private JComboBox<String> paymentMethodComboBox;
    private JButton checkoutButton;
    private JButton cancelButton;
    private List<CartItem> cartItems;
    private Cart cartPanel; //reference to the cart panel.
    private userDashboard parentDashboard; // Reference to userDashboard

    public Checkout(List<CartItem> cartItems, Cart cartPanel, userDashboard parentDashboard) {
        this.cartItems = cartItems;
        this.cartPanel = cartPanel;
        this.parentDashboard = parentDashboard; // Initialize the reference

        setTitle("Checkout");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Checkout");
        topPanel.add(titleLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        bookInfoTextArea = new JTextArea();
        bookInfoTextArea.setEditable(false);
        JScrollPane bookInfoScrollPane = new JScrollPane(bookInfoTextArea);
        mainPanel.add(bookInfoScrollPane, BorderLayout.CENTER);

        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel paymentLabel = new JLabel("Payment Method:");
        paymentMethodComboBox = new JComboBox<>(new String[]{"Cash", "Credit Card", "Debit Card"});
        paymentPanel.add(paymentLabel);
        paymentPanel.add(paymentMethodComboBox);
        mainPanel.add(paymentPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        checkoutButton = new JButton("Checkout");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(checkoutButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.EAST);

        add(mainPanel);
        displayBookInfo();
        addListener();
    }

    private void displayBookInfo() {
        StringBuilder sb = new StringBuilder();
        double total = 0;
        for (CartItem item : cartItems) {
            Book book = item.getBook();
            int quantity = item.getQuantity();
            sb.append(book.getName()).append(" x").append(quantity).append("  Price: $").append(String.format("%.2f", book.getPrice() * quantity)).append("\n");
            total += book.getPrice() * quantity;
        }
        sb.append("--------------------\n");
        sb.append("Total: $").append(String.format("%.2f", total));
        bookInfoTextArea.setText(sb.toString());
    }

    private void addListener() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();
                // Call the performCheckout method in userDashboard
                parentDashboard.performCheckout(cartItems, paymentMethod);
                dispose(); // Close the checkout window after attempting checkout
            }
        });
    }

    // We no longer need this method here as the logic is in userDashboard
    // private boolean performCheckout(String paymentMethod) { ... }
}