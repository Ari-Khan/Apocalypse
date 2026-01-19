/**
 * Bullet.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: Class for projectiles fired by the player's gun.
 */

// Import statement
import java.awt.*;

// Bullet class
public class Bullet {
    // Position
    double x, y;
    // Velocity components
    double vx, vy;

    // Speed and lifespan
    int speed = 20;
    int life = 120;

    // Constructor
    public Bullet(int x, int y, double dx, double dy) {
        this.x = x;
        this.y = y;

        // Normalize direction and apply speed
        double len = Math.sqrt(dx * dx + dy * dy);
        vx = (dx / len) * speed;
        vy = (dy / len) * speed;
    } // End constructor

    // Update bullet position and lifespan
    boolean update() {
        // Move bullet
        x += vx;
        y += vy;
        // Decrease lifespan
        life--;
        return life > 0;
    } // End method

    // Draw bullet
    void draw(Graphics g, int camX, int camY) {
        g.setColor(Color.YELLOW);
        g.fillOval((int)x - camX - 3, (int)y - camY - 3, 6, 6);
    } // End method
} // End class
