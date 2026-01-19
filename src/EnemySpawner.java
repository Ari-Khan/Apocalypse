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
