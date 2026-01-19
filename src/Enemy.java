import java.awt.*;

public class Enemy {

    int x, y;
    int size = 30;

    int speed = 3;

    int maxHealth = 2;
    int health = maxHealth;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // chase player
    void update(int px, int py) {
        int dx = px - x;
        int dy = py - y;

        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) return;

        x += (int) (dx / dist * speed);
        y += (int) (dy / dist * speed);
    }

    void hit() {
        health--;
    }

    boolean isDead() {
        return health <= 0;
    }

    void draw(Graphics g, int camX, int camY) {
        g.setColor(Color.RED);
        g.fillOval(
            x - size / 2 - camX,
            y - size / 2 - camY,
            size,
            size
        );

        // health bar
        g.setColor(Color.BLACK);
        g.drawRect(
            x - size / 2 - camX,
            y - size / 2 - 10 - camY,
            size,
            5
        );

        g.setColor(Color.GREEN);
        int hpWidth = (int) (size * (health / (double) maxHealth));
        g.fillRect(
            x - size / 2 - camX,
            y - size / 2 - 10 - camY,
            hpWidth,
            5
        );
    }
}
