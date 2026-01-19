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
    // Constants
    static final int DEFAULT_SIZE = 30;
    static final int HINT_OFFSET_X = -18;
    static final int HINT_OFFSET_Y_MULT = -1; // multiplied by size
    static final int NEAR_RANGE = 50;
    static final Color COLOR_SPAWNER = Color.ORANGE;
    static final Color COLOR_HINT = Color.WHITE;
    static final String TEXT_HINT = "Press E";

    // Position and size
    int x, y;
    int size = DEFAULT_SIZE;

    // Constructor
    public GunSpawner(int x, int y) {
        this.x = x;
        this.y = y;
    } // End constructor

    // Draw gun spawner
    void draw(Graphics g, int camX, int camY) {
        g.setColor(COLOR_SPAWNER);
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

        g.setColor(COLOR_HINT);
        g.drawString(
            TEXT_HINT,
            x - camX + HINT_OFFSET_X,
            y - camY + (HINT_OFFSET_Y_MULT * size)
        );
    } // End method

    // Check if player is near spawner
    boolean isNear(int px, int py) {
        return checkDistance(px, py, NEAR_RANGE);
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
