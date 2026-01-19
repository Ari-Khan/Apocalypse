/**
 * GunSpawner.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: Crate locations that spawn AK47 rifles for player collection.
 */

import java.awt.*;

public class GunSpawner {

    int x, y;
    int size = 30;

    public GunSpawner(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void draw(Graphics g, int camX, int camY) {
        g.setColor(Color.ORANGE);
        g.fillRect(
            x - size / 2 - camX,
            y - size / 2 - camY,
            size,
            size
        );
    }

    void drawHint(Graphics g, int px, int py, int camX, int camY) {
        if (!isNear(px, py)) return;

        g.setColor(Color.WHITE);
        g.drawString(
            "Press E",
            x - camX - 18,
            y - camY - size
        );
    }

    boolean isNear(int px, int py) {
        return checkDistance(px, py, 50);
    }

    private boolean checkDistance(int px, int py, int range) {
        int dx = px - x;
        int dy = py - y;
        return dx * dx + dy * dy < range * range;
    }

    Gun spawnGun() {
        return new AK47();
    }
}
