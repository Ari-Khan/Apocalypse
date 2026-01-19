/**
 * Spawner.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: An abstract class for reused spawner logic.
 */

// Spawner abstract class
public abstract class Spawner {
    // Position
    protected int x;
    protected int y;

    // Constructor
    protected Spawner(int x, int y) {
        this.x = x;
        this.y = y;
    } // End constructor

    // Check if a point is within a certain range of the spawner
    protected boolean withinRange(int px, int py, int range) {
        int dx = px - x;
        int dy = py - y;
        return dx * dx + dy * dy <= range * range;
    } // End method
} // End class
