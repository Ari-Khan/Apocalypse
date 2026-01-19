/**
 * GunSpawner.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: Crate locations that spawn AK47 rifles for player collection.
 */

import java.awt.*;

// Gun spawner class
public class GunSpawner {
    // Position and size
    int x, y;
    int size = 30;

    // Constructor
    public GunSpawner(int x, int y) {
        this.x = x;
        this.y = y;
    } // End constructor

    // Draw gun spawner
    void draw(Graphics g, int camX, int camY) {
        g.setColor(Color.ORANGE);
        g.fillRect(
            x - size / 2 - camX,
            y - size / 2 - camY,
            size,
            size
        );
    } // End method

    // Draw interaction hint
    void drawHint(Graphics g, int px, int py, int camX, int camY) {
        if (!isNear(px, py)) return;

        g.setColor(Color.WHITE);
        g.drawString(
            "Press E",
            x - camX - 18,
            y - camY - size
        );
    } // End method

    // Check if player is near spawner
    boolean isNear(int px, int py) {
        return checkDistance(px, py, 50);
    } // End method

    // Check distance between two points
    private boolean checkDistance(int px, int py, int range) {
        int dx = px - x;
        int dy = py - y;
        return dx * dx + dy * dy < range * range;
    } // End method

    // Spawn gun at this location
    Gun spawnGun() {
        return new AK47();
    } // End method
} // End class
