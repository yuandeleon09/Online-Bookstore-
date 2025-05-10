package FINALNAAYOKONA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class adminDashboard extends JFrame {

    private JPanel topPanel, centerPanel, homePanel;
    private CardLayout cardLayout;
    private JLabel helloLabel;
    private JButton dashboardButton, profileButton, userViewButton, bookDetailsButton, manageOrdersButton; // Added manageOrdersButton
    private ProfilePanel profilePanel;
    private Map<String, JPanel> panelMap = new HashMap<>();
    private String[][] navItems = {
            {"MANAGE ORDERS", "orders"},
            {"MANAGE INVENTORY", "inventory"},
            {"VIEW SALES REPORTS", "reports"},
            {"UPDATE BOOK DETAILS", "book_details"},
            {"MANAGE USERS", "users"}
    };

    private String loggedInAdminUsername;

    public adminDashboard(String username) {
        this.loggedInAdminUsername = username;
        setTitle("Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel setup
        topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        helloLabel = new JLabel("HELLO(" + loggedInAdminUsername.toUpperCase() + ")");
        dashboardButton = new JButton("DASHBOARD");
        profileButton = new JButton("PROFILE");
        userViewButton = new JButton("USER VIEW");
        bookDetailsButton = new JButton("UPDATE BOOK DETAILS");
        manageOrdersButton = new JButton("MANAGE ORDERS"); // Initialize the button
        topPanel.add(helloLabel);
        topPanel.add(dashboardButton);
        topPanel.add(profileButton);
        topPanel.add(userViewButton);

        add(topPanel, BorderLayout.NORTH);

        // Center panel with card layout
        centerPanel = new JPanel();
        cardLayout = new CardLayout();
        centerPanel.setLayout(cardLayout);
        add(centerPanel, BorderLayout.CENTER);

        // Home panel with dynamic buttons
        homePanel = new JPanel();
        homePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        centerPanel.add(homePanel, "home");

        for (int i = 0; i < navItems.length; i++) {
            final int index = i;
            JButton navButton = new JButton(navItems[i][0]);
            homePanel.add(navButton);

            // Add listener using anonymous inner class
            navButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showPanel(navItems[index][1]);
                    if ("reports".equals(navItems[index][1])) {
                        ((SalesReport) panelMap.get("reports")).updateReport();
                    }
                }
            });
        }

        // Initialize and register the profile panel.  Pass the username!
        profilePanel = new ProfilePanel(loggedInAdminUsername);
        registerPanel("profile", profilePanel);

        // Register each panel with its name
        registerPanel("orders", new ManageOrders()); // Register the ManageOrders panel
        registerPanel("inventory", new ManageInventoryPanel());
        registerPanel("reports", new SalesReport());
        registerPanel("book_details", new EditBookDetailsPanel());
        registerPanel("users", new ManageUsers());

        // Dashboard button (home)
        dashboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPanel("home");
            }
        });

        // Profile button.  Show the profile panel when clicked!
        profileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPanel("profile");
            }
        });

        // User View button
        userViewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new userDashboard(username).setVisible(true);
                dispose();
            }
        });

        // Book Details button action listener
        bookDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPanel("book_details");
            }
        });

        // Manage Orders button action listener
        manageOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel("orders"); // Show the ManageOrders panel
            }
        });

        setVisible(true);
        showPanel("home");
    }

    // Method to add panel to center layout and map
    private void registerPanel(String name, JPanel panel) {
        centerPanel.add(panel, name);
        panelMap.put(name, panel);
    }

    // Central method to switch cards
    private void showPanel(String name) {
        cardLayout.show(centerPanel, name);
    }

    // A basic placeholder panel with just a label
    class JLabelPanel extends JPanel {
        public JLabelPanel(String text) {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            add(new JLabel(text));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String adminUsername = "defaultadmin";
                new adminDashboard(adminUsername);
            }
        });
    }
}

