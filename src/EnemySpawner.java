/**
 * EnemySpawner.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: Enemy generation points that spawn hostile entities when player is in range.
 */

// Import statement
import java.awt.*;

// Enemy spawner class
public class EnemySpawner {
    // Constants
    static final int DEFAULT_RADIUS = 400;
    static final int DEFAULT_COOLDOWN = 120;
    static final int DRAW_RADIUS = 10;
    static final int DRAW_DIAMETER = DRAW_RADIUS * 2;
    static final Color COLOR_SPAWNER = Color.RED;

    // Position and spawn parameters
    int x, y;
    int radius = DEFAULT_RADIUS;
    int cooldown = DEFAULT_COOLDOWN;
    int timer = 0;

    // Constructor
    public EnemySpawner(int x, int y) {
        this.x = x;
        this.y = y;
    } // End constructor

    // Update spawner and create enemies
    void update(int px, int py, java.util.ArrayList<Enemy> enemies) {
        // Check if player is in range
        if (!isPlayerInRange(px, py)) {
            return;
        }

        // Wait for cooldown
        if (timer > 0) {
            timer--;
            return;
        }

        // Spawn enemy
        enemies.add(new Enemy(x, y));
        timer = cooldown;
    } // End method

    // Check if player is in spawn range
    private boolean isPlayerInRange(int px, int py) {
        int dx = px - x;
        int dy = py - y;
        return dx * dx + dy * dy <= radius * radius;
    } // End method

    // Draw spawner
    void draw(Graphics g, int camX, int camY) {
        g.setColor(COLOR_SPAWNER);
        g.fillOval(x - DRAW_RADIUS - camX, y - DRAW_RADIUS - camY, DRAW_DIAMETER, DRAW_DIAMETER);
    } // End method
} // End class
