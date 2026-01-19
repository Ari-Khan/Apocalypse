/**
 * Helicopter.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: Extraction point for player escape and game completion.
 */

// Import statement
import java.awt.*;

// Helicopter class
public class Helicopter {
    // Constants
    static final int DEFAULT_SIZE = 240;
    static final int MAIN_ROTOR_STROKE = 8;
    static final int TAIL_ROTOR_STROKE = 6;
    static final int HINT_OFFSET_X = -20;
    static final Color COLOR_FUSELAGE = new Color(50, 100, 50);
    static final Color COLOR_TAIL = new Color(30, 80, 30);
    static final Color COLOR_ROTOR_OUTER = new Color(60, 120, 60);
    static final Color COLOR_ROTOR_INNER = new Color(40, 90, 40);
    static final Color COLOR_MAIN_BLADE = new Color(80, 80, 80);
    static final Color COLOR_TAIL_BLADE = new Color(60, 60, 60);
    static final String HINT_TEXT = "Press K";
    static final int NEAR_RADIUS = 80;
    // Position and size
    int x, y;
    int size = DEFAULT_SIZE;
    
    // Constructor
    public Helicopter(int x, int y) {
        this.x = x;
        this.y = y;
    } // End constructor
    
    // Draw helicopter sprite
    void draw(Graphics g, int camX, int camY) {
        int screenX = x - camX;
        int screenY = y - camY;
        
        // Draw fuselage
        g.setColor(COLOR_FUSELAGE);
        g.fillRect(screenX - 20, screenY + 60, 40, 100);
        
        // Draw tail rotor
        g.setColor(COLOR_TAIL);
        g.fillOval(screenX - 25, screenY + 150, 50, 40);
        
        // Draw main rotor base
        g.setColor(COLOR_ROTOR_OUTER);
        g.fillOval(screenX - 70, screenY - 50, 140, 140);
        
        g.setColor(COLOR_ROTOR_INNER);
        g.fillOval(screenX - 55, screenY - 30, 110, 110);
        
        // Draw rotor blades
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(MAIN_ROTOR_STROKE));
        g2.setColor(COLOR_MAIN_BLADE);
        
        g2.drawLine(screenX - 110, screenY, screenX + 110, screenY);
        g2.drawLine(screenX, screenY - 110, screenX, screenY + 110);
        
        // Draw tail rotor blades
        g2.setStroke(new BasicStroke(TAIL_ROTOR_STROKE));
        g2.setColor(COLOR_TAIL_BLADE);
        g2.drawLine(screenX - 20, screenY + 160, screenX + 20, screenY + 160);
        
        g2.setStroke(new BasicStroke(1));
    } // End method
    
    // Draw interaction hint
    void drawHint(Graphics g, int px, int py, int camX, int camY) {
        if (!isNear(px, py)) {
            return;
        }
        
        g.setColor(Color.WHITE);
        g.drawString(HINT_TEXT, x - camX + HINT_OFFSET_X, y - camY - size);
    } // End method
    
    // Check if player is near helicopter
    boolean isNear(int px, int py) {
        int dx = px - x;
        int dy = py - y;
        return dx * dx + dy * dy < NEAR_RADIUS * NEAR_RADIUS;
    } // End method
} // End class
