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
    // Position and spawn parameters
    int x, y;
    int radius = 400;
    int cooldown = 120;
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

    // Update spawner with building collision checking
    void update(int px, int py, java.util.ArrayList<Enemy> enemies, java.util.ArrayList<Building> buildings) {
        // Check if player is in range
        if (!isPlayerInRange(px, py)) {
            return;
        }

        // Wait for cooldown
        if (timer > 0) {
            timer--;
            return;
        }

        // Spawn enemy if not blocked by building
        if (!checkBuildingCollision(buildings)) {
            enemies.add(new Enemy(x, y));
        }
        timer = cooldown;
    } // End method

    // Check collision with buildings
    private boolean checkBuildingCollision(java.util.ArrayList<Building> buildings) {
        for (Building b : buildings) {
            if (x > b.x && x < b.x + b.width && y > b.y && y < b.y + b.height) {
                return true;
            }
        } // End for loop
        return false;
    } // End method

    // Check if player is in spawn range
    private boolean isPlayerInRange(int px, int py) {
        int dx = px - x;
        int dy = py - y;
        return dx * dx + dy * dy <= radius * radius;
    } // End method

    // Draw spawner
    void draw(Graphics g, int camX, int camY) {
        g.setColor(Color.RED);
        g.fillOval(x - 10 - camX, y - 10 - camY, 20, 20);
    } // End method
} // End class
