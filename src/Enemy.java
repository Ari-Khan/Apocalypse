/**
 * Enemy.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: Represents enemy entities with recursive spawning mechanics and child enemies.
 */

// Import statement
import java.awt.*;

// Enemy class
public class Enemy {
    // Position
    int x, y;
    int size = 30;

    // Movement
    int speed = 4;

    // Health
    int maxHealth = 2;
    int health = maxHealth;
    int generation = 0;

    // Default constructor
    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    } // End constructor

    // Constructor with generation for spawned children
    public Enemy(int x, int y, int generation) {
        this.x = x;
        this.y = y;
        this.generation = generation;
        this.size = 30 - (generation * 5);
        this.maxHealth = Math.max(1, 2 - generation);
        this.health = maxHealth;
        this.speed = 3 + generation;
    } // End constructor

    // Update enemy movement toward player
    void update(int px, int py) {
        int dx = px - x;
        int dy = py - y;

        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) return;

        // Move toward player
        x += (int) (dx / dist * speed);
        y += (int) (dy / dist * speed);
    } // End method

    // Check collision with buildings
    boolean checkBuildingCollision(java.util.ArrayList<Building> buildings) {
        for (Building b : buildings) {
            int halfSize = size / 2;
            if (x + halfSize > b.x && x - halfSize < b.x + b.width &&
                y + halfSize > b.y && y - halfSize < b.y + b.height) {
                return true;
            }
        } // End for loop
        return false;
    } // End method

    // Undo last movement
    void undoMove(int oldX, int oldY) {
        x = oldX;
        y = oldY;
    } // End method

    // Take damage
    void hit() {
        health--;
    } // End method

    // Check if enemy is dead
    boolean isDead() {
        return health <= 0;
    } // End method

    // Check circle collision with another entity
    boolean checkCollision(int otherX, int otherY, int otherSize) {
        int dx = otherX - x;
        int dy = otherY - y;
        int totalRadius = (size / 2) + (otherSize / 2);
        return dx * dx + dy * dy < totalRadius * totalRadius;
    } // End method

    // Spawn child enemies around this enemy
    void spawnChildren(java.util.ArrayList<Enemy> enemies, int depth) {
        // Check recursion limit
        if (depth <= 0 || generation >= 3) {
            return;
        }

        int count = Math.max(1, 4 - generation);
        spawnChildrenRecursive(enemies, count, 0, generation + 1);
    } // End method

    // Recursively spawn child enemies
    private void spawnChildrenRecursive(java.util.ArrayList<Enemy> enemies, int totalCount, int index, int childGen) {
        // Base case
        if (index >= totalCount) {
            return;
        }

        // Calculate spawn position around parent
        double angleStep = 2 * Math.PI / totalCount;
        double angle = angleStep * index;
        int spawnDist = size;
        int spawnX = x + (int)(Math.cos(angle) * spawnDist);
        int spawnY = y + (int)(Math.sin(angle) * spawnDist);
        
        // Add child to list
        enemies.add(new Enemy(spawnX, spawnY, childGen));
        
        // Recurse for next child
        spawnChildrenRecursive(enemies, totalCount, index + 1, childGen);
    } // End method

    // Draw enemy
    void draw(Graphics g, int camX, int camY) {
        // Draw enemy body
        g.setColor(Color.RED);
        g.fillOval(
            x - size / 2 - camX,
            y - size / 2 - camY,
            size,
            size
        );

        // Draw health bar background
        g.setColor(Color.BLACK);
        g.drawRect(
            x - size / 2 - camX,
            y - size / 2 - 10 - camY,
            size,
            5
        );

        // Draw health bar
        g.setColor(Color.GREEN);
        int hpWidth = (int) (size * (health / (double) maxHealth));
        g.fillRect(
            x - size / 2 - camX,
            y - size / 2 - 10 - camY,
            hpWidth,
            5
        );
    } // End method
} // End class
