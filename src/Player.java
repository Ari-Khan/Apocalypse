/**
 * Player.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: Represents the player character with movement and health.
 */

// Import statements
import java.awt.*;
import java.awt.event.*;

// Player class
public class Player {
    // Player properties
    int x, y;
    int size = 40;
    int speed = 6;
    int maxHealth = 100;
    int health = maxHealth;

    // Input state
    boolean up, down, left, right;

    // Weapon
    Gun gun;

    // Constructor
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        gun = new Glock17();
    } // End constructor

    // Update player position and movement
    void update() {
        // Apply movement based on input state
        if (up) {
            y -= speed;
        }
        if (down) {
            y += speed;
        }
        if (left) {
            x -= speed;
        }
        if (right) {
            x += speed;
        }

        // Clamp position to world bounds
        x = Math.max(size / 2, Math.min(x, Game.WORLD_W - size / 2));
        y = Math.max(size / 2, Math.min(y, Game.WORLD_H - size / 2));
    } // End method

    // Check collision with buildings
    boolean checkBuildingCollision(java.util.ArrayList<Building> buildings) {
        // Check collision with each building
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

    // Draw player and gun
    void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int sx = Game.SCREEN_W / 2;
        int sy = Game.SCREEN_H / 2;

        // Draw gun
        gun.draw(g2, sx, sy);

        // Draw player sprite
        g2.setColor(new Color(70, 100, 160));
        g2.fillOval(sx - size / 2, sy - size / 2, size, size);
    } // End method

    // Handle key press input
    void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> up = true;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> down = true;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> left = true;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> right = true;
        }
    } // End method

    // Handle key release input
    void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> up = false;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> down = false;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> left = false;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> right = false;
        }
    } // End method
} // End class
