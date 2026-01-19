/**
 * Pointer.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: On-screen navigation arrow that points toward the helicopter.
 */

// Import statement
import java.awt.*;

// Pointer class
public class Pointer {
    // Constants
    static final Color COLOR_FILL = new Color(0, 200, 0);
    static final Color COLOR_BORDER = new Color(0, 150, 0);

    // Draw navigation pointer arrow
    void draw(Graphics g, int playerX, int playerY, int heliX, int heliY, int screenW, int screenH) {
        // Calculate direction to helicopter
        int dx = heliX - playerX;
        int dy = heliY - playerY;
        
        double angle = Math.atan2(dy, dx);
        
        // Calculate pointer position on screen edge
        int centerX = screenW / 2;
        int centerY = screenH / 2;

        int pointerDist = Math.min(screenW / 2, screenH / 2) - 60;
        
        int pointerX = centerX + (int)(Math.cos(angle) * pointerDist);
        int pointerY = centerY + (int)(Math.sin(angle) * pointerDist);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        
        // Calculate arrow points
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        
        xPoints[0] = pointerX + (int)(Math.cos(angle) * 20);
        yPoints[0] = pointerY + (int)(Math.sin(angle) * 20);

        xPoints[1] = pointerX + (int)(Math.cos(angle + 2.5) * 20 * 0.6);
        yPoints[1] = pointerY + (int)(Math.sin(angle + 2.5) * 20 * 0.6);

        xPoints[2] = pointerX + (int)(Math.cos(angle - 2.5) * 20 * 0.6);
        yPoints[2] = pointerY + (int)(Math.sin(angle - 2.5) * 20 * 0.6);
        
        // Draw arrow
        g2.setColor(COLOR_FILL);
        g2.fillPolygon(xPoints, yPoints, 3);
        
        g2.setColor(COLOR_BORDER);
        g2.drawPolygon(xPoints, yPoints, 3);
        
        g2.setStroke(new BasicStroke(1));
    } // End method
} // End class
