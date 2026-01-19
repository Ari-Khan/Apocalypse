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
    // Constants
    static final int SPEED = 20;
    static final int LIFE_TICKS = 120;
    static final int DRAW_RADIUS = 3;
    static final int DRAW_DIAMETER = DRAW_RADIUS * 2;
    static final Color COLOR_BULLET = Color.YELLOW;

    // Position
    double x, y;
    // Velocity components
    double vx, vy;

    // Speed and lifespan
    int speed = SPEED;
    int life = LIFE_TICKS;

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
        g.setColor(COLOR_BULLET);
        g.fillOval((int)x - camX - DRAW_RADIUS, (int)y - camY - DRAW_RADIUS, DRAW_DIAMETER, DRAW_DIAMETER);
    } // End method
} // End class
