/**
 * EnemySpawner.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: Enemy generation points that spawn hostile entities when player is in range.
 */

import java.awt.*;

public class EnemySpawner {

    int x, y;
    int radius = 400;
    int cooldown = 120;
    int timer = 0;

    public EnemySpawner(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void update(int px, int py, java.util.ArrayList<Enemy> enemies) {
        if (!isPlayerInRange(px, py)) {
            return;
        }

        if (timer > 0) {
            timer--;
            return;
        }

        enemies.add(new Enemy(x, y));
        timer = cooldown;
    }

    void update(int px, int py, java.util.ArrayList<Enemy> enemies, java.util.ArrayList<Building> buildings) {
        if (!isPlayerInRange(px, py)) {
            return;
        }

        if (timer > 0) {
            timer--;
            return;
        }

        if (!checkBuildingCollision(buildings)) {
            enemies.add(new Enemy(x, y));
        }
        timer = cooldown;
    }

    private boolean checkBuildingCollision(java.util.ArrayList<Building> buildings) {
        for (Building b : buildings) {
            if (x > b.x && x < b.x + b.width && y > b.y && y < b.y + b.height) {
                return true;
            }
        }
        return false;
    }

    private boolean isPlayerInRange(int px, int py) {
        int dx = px - x;
        int dy = py - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    void draw(Graphics g, int camX, int camY) {
        g.setColor(Color.RED);
        g.fillOval(x - 10 - camX, y - 10 - camY, 20, 20);
    }
}
