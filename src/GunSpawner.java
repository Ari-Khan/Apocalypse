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
            "Press K",
            x - camX - 18,
            y - camY - size
        );
    }

    boolean isNear(int px, int py) {
        int dx = px - x;
        int dy = py - y;
        return dx * dx + dy * dy < 50 * 50;
    }

    Gun spawnGun() {
        return new AK47();
    }
}
