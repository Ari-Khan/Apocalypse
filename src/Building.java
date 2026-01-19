/**
 * Building.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: Rectangular obstacles that block player, enemy, and bullet movement.
 */

// Import statement
import java.awt.*;

// Building class
public class Building {
    // Constants
    static final Color COLOR_FILL = new Color(80, 80, 80);
    static final Color COLOR_BORDER = new Color(60, 60, 60);
    
    // Position and dimensions
    int x, y;
    int width, height;
    
    // Constructor
    public Building(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    } // End constructor
    
    // Check circle collision with building
    boolean intersects(int px, int py, int size) {
        int halfSize = size / 2;
        return px + halfSize > x && px - halfSize < x + width &&
               py + halfSize > y && py - halfSize < y + height;
    } // End method
    
    // Check point collision with building
    boolean intersectsPoint(double px, double py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    } // End method
    
    // Draw building
    void draw(Graphics g, int camX, int camY) {
        // Draw building fill
        g.setColor(COLOR_FILL);
        g.fillRect(x - camX, y - camY, width, height);
        
        // Draw building border
        g.setColor(COLOR_BORDER);
        g.drawRect(x - camX, y - camY, width, height);
    } // End method
} // End class
