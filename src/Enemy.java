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
    // Constants
    static final int BASE_SIZE = 30;
    static final int BASE_SPEED = 4;
    static final int BASE_MAX_HEALTH = 2;
    static final int GENERATION_LIMIT = 3;
    static final int SIZE_DECREMENT_PER_GEN = 5;
    static final int SPEED_INCREMENT_PER_GEN = 1;
    static final int SPAWN_CHILD_COUNT_BASE = 4;
    static final int HEALTH_BAR_HEIGHT = 5;
    static final int HEALTH_BAR_OFFSET_Y = 10;
    static final Color COLOR_BODY = Color.RED;
    static final Color COLOR_HEALTH_BG = Color.BLACK;
    static final Color COLOR_HEALTH_FILL = Color.GREEN;

    // Position
    int x, y;
    int size = BASE_SIZE;

    // Movement
    int speed = BASE_SPEED;

    // Health
    int maxHealth = BASE_MAX_HEALTH;
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
        this.size = BASE_SIZE - (generation * SIZE_DECREMENT_PER_GEN);
        this.maxHealth = Math.max(1, BASE_MAX_HEALTH - generation);
        this.health = maxHealth;
        this.speed = (BASE_SPEED - 1) + (generation * SPEED_INCREMENT_PER_GEN);
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
        if (depth <= 0 || generation >= GENERATION_LIMIT) {
            return;
        }

        int count = Math.max(1, SPAWN_CHILD_COUNT_BASE - generation);
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
        g.setColor(COLOR_BODY);
        g.fillOval(
            x - size / 2 - camX,
            y - size / 2 - camY,
            size,
            size
        );

        // Draw health bar background
        g.setColor(COLOR_HEALTH_BG);
        g.drawRect(
            x - size / 2 - camX,
            y - size / 2 - HEALTH_BAR_OFFSET_Y - camY,
            size,
            HEALTH_BAR_HEIGHT
        );

        // Draw health bar
        g.setColor(COLOR_HEALTH_FILL);
        int hpWidth = (int) (size * (health / (double) maxHealth));
        g.fillRect(
            x - size / 2 - camX,
            y - size / 2 - HEALTH_BAR_OFFSET_Y - camY,
            hpWidth,
            HEALTH_BAR_HEIGHT
        );
    } // End method

    // Check if enemy is stuck inside a building
    boolean isStuckInBuilding(java.util.ArrayList<Building> buildings) {
        // Check if enemy is overlapping with a building
        for (Building b : buildings) {
            if (x + size / 2 > b.x && x - size / 2 < b.x + b.width &&
                y + size / 2 > b.y && y - size / 2 < b.y + b.height) {
                return true;
            }
        } // End for loop
        return false;
    } // End method
} // End class
