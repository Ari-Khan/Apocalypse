import java.awt.*;

public class Bullet {

    double x, y;
    double vx, vy;

    int speed = 20;
    int life = 120;

    public Bullet(int x, int y, double dx, double dy) {
        this.x = x;
        this.y = y;

        double len = Math.sqrt(dx * dx + dy * dy);
        vx = (dx / len) * speed;
        vy = (dy / len) * speed;
    }

    boolean update() {
        x += vx;
        y += vy;
        life--;
        return life > 0;
    }

    void draw(Graphics g, int camX, int camY) {
        g.setColor(Color.YELLOW);
        g.fillOval((int)x - camX - 3, (int)y - camY - 3, 6, 6);
    }
}
