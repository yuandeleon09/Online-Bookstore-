package FINALNAAYOKONA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class UI extends JPanel {

    private BufferedImage backgroundImage;
    private String imagePath; // Store the path for re-loading
    private int originalWidth;  // Store original image dimensions
    private int originalHeight;

    public UI(String imagePath) {
        this.imagePath = imagePath;
        try {
            loadImage();
        } catch (IOException e) {
            // Handle the exception in loadImage() to ensure image is loaded or error is shown
            // Don't throw exception in constructor.
        }
        // Make the panel resizable
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repaint(); // Trigger repaint when the panel is resized
            }
        });
        setOpaque(false); // Make the panel transparent, so the background image is visible
    }

    private void loadImage() throws IOException {
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            try {
                backgroundImage = ImageIO.read(imageFile);
                originalWidth = backgroundImage.getWidth();
                originalHeight = backgroundImage.getHeight();
            } catch (IOException e) {
                System.err.println("Error reading image: " + imagePath);
                e.printStackTrace();
                throw e; // re-throw the exception so caller knows
            }
        } else {
            System.err.println("Image file does not exist: " + imagePath);
            throw new IOException("Image file does not exist: " + imagePath);
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Get the current size of the panel
            int currentWidth = getWidth();
            int currentHeight = getHeight();

            // Calculate the scaled width and height, maintaining aspect ratio
            int scaledWidth = currentWidth;
            int scaledHeight = currentHeight;

            // Maintain aspect ratio.  There are several strategies.  This one
            // scales to fill the entire component, potentially clipping.
             float widthRatio = (float) currentWidth / originalWidth;
             float heightRatio = (float) currentHeight / originalHeight;
             float scaleFactor = Math.max(widthRatio, heightRatio); // Or Math.min to fit within bounds

             scaledWidth = (int) (originalWidth * scaleFactor);
             scaledHeight = (int) (originalHeight * scaleFactor);


            // Draw the image scaled to the panel size
            g.drawImage(backgroundImage, 0, 0, scaledWidth, scaledHeight, this);
        }
    }

    // Example usage:
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Resizable Background Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Replace with the actual path to your image file
            String imagePath = "\"C:\\Users\\yuan deleon\\eclipse-workspace\\OBSS\\src\\icons\\bgobss.jpg\"";
            UI backgroundPanel = new UI(imagePath);

            // Add a label or other components to the background panel
            JLabel label = new JLabel("This is a resizable background!");
            label.setFont(new Font("Arial", Font.BOLD, 20));
            label.setForeground(Color.WHITE);
            label.setOpaque(false); // Make the label's background transparent
            backgroundPanel.add(label);

            frame.setContentPane(backgroundPanel); // Set our custom panel as content pane

            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

