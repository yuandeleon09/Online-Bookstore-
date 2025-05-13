package FINALNAAYOKONA;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BookCard extends JPanel {
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

    // Inner border class
    private static class RoundBorder extends AbstractBorder {
        private int radius;
        private int shadowSize;

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
