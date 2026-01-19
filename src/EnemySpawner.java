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
        int dx = px - x;
        int dy = py - y;

        if (dx * dx + dy * dy > radius * radius)
            return;

        if (timer > 0) {
            timer--;
            return;
        }

        enemies.add(new Enemy(x, y));
        timer = cooldown;
    }

    void draw(Graphics g, int camX, int camY) {
        g.setColor(Color.RED);
        g.fillOval(x - 10 - camX, y - 10 - camY, 20, 20);
    }
}
