package FINALNAAYOKONA;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AboutUs extends JPanel {

    private static final String[] CREATOR_NAMES = {
            "Leader: Yuan De Leon", "Tester: Ken Ryan Perez", " Developer: Lawrence Dolorito", "Debugger: Angelic Pauleen Costin"
    };
    private static final String[] CREATOR_INFO = {
            "BSIT Student",
            "BSIT Student",
            "BSIT Student",
            "BSIT Student"
    };
    private static final String[] CREATOR_IMAGES = {
            "C:\\Users\\yuan deleon\\Downloads\\id pic.jpg", // Replace with actual image paths
            "C:\\Users\\yuan deleon\\Downloads\\pexels-pixabay-87651.jpg",
            "C:\\Users\\yuan deleon\\Downloads\\user.jpg",
            "C:\\Users\\yuan deleon\\Downloads\\pexels-pixabay-87651.jpg"
    };

    private static final int INFO_PANEL_WIDTH = 300;
    private static final int INFO_PANEL_HEIGHT = 200;
    private static final int IMAGE_WIDTH = 150;
    private static final int IMAGE_HEIGHT = 150;
    private static final int CARD_WIDTH = 350;
    private static final int CARD_HEIGHT = 400;
    private static final int SHADOW_SIZE = 5;

    public AboutUs() {
        setLayout(new GridLayout(0, 2, 20, 20)); // 2 columns, variable rows, with spacing
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding for the whole panel
        setBackground(new Color(240, 240, 240)); // Light background

        for (int i = 0; i < 4; i++) {
            JPanel creatorCard = createCreatorCard(CREATOR_NAMES[i], CREATOR_INFO[i], CREATOR_IMAGES[i]);
            add(creatorCard);
        }
    }

    private JPanel createCreatorCard(String name, String info, String imagePath) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(new RoundBorder(10, SHADOW_SIZE));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));

        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Image creatorImage = ImageIO.read(imageFile);
                Image scaledImage = creatorImage.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(scaledImage);
                imageLabel.setIcon(imageIcon);
            } else {
                System.err.println("Image file not found at: " + imagePath);
                imageLabel.setText("Image Not Found");
                imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            }
        } catch (IOException e) {
            e.printStackTrace();
            imageLabel.setText("Error");
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        }
        cardPanel.add(imageLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(INFO_PANEL_WIDTH, INFO_PANEL_HEIGHT));
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(new Color(51, 51, 51));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea infoTextArea = new JTextArea(info);
        infoTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoTextArea.setForeground(new Color(102, 102, 102));
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setLineWrap(true);
        infoTextArea.setEditable(false);
        infoTextArea.setBackground(Color.WHITE);
        infoTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add some vertical space
        infoPanel.add(infoTextArea);
        infoPanel.setBackground(Color.WHITE);
        cardPanel.add(infoPanel, BorderLayout.CENTER);

        return cardPanel;
    }

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

            // Draw shadow
            Color shadowColor = new Color(0, 0, 0, 50); // Subtle shadow
            g2d.setColor(shadowColor);
            for (int i = 0; i < shadowSize; i++) {
                g2d.fillRoundRect(x + shadowSize - i, y + shadowSize - i,
                        width - 2 * shadowSize + i, height - 2 * shadowSize + i,
                        radius, radius);
            }

            // Draw the rounded rectangle for the card
            g2d.setColor(c.getBackground());
            g2d.fillRoundRect(x, y, width - 2 * shadowSize, height - 2 * shadowSize, radius, radius);

            // Draw the border
            g2d.setColor(new Color(220, 220, 220)); // Light gray border
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("About Us");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new AboutUs());
            frame.setSize(800, 600); // Adjust size as needed
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}