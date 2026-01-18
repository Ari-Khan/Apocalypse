import java.awt.*;
import java.util.*;

public class Gun {

    int length = 30;

    int magSize;
    int ammo;

    int fireDelay;
    int fireTimer;

    boolean reloading;
    int reloadTime;
    int reloadTimer;

    ArrayList<Bullet> bullets = new ArrayList<>();

    void update() {
        if (fireTimer > 0) fireTimer--;

        if (reloading) {
            reloadTimer--;
            if (reloadTimer <= 0) {
                ammo = magSize;
                reloading = false;
            }
        }

        bullets.removeIf(b -> !b.update());
    }

    void shoot(int x, int y, double dx, double dy) {
        if (fireTimer > 0 || reloading || ammo <= 0) return;

        ammo--;
        fireTimer = fireDelay;

        double angle = Math.atan2(dy, dx);

        int spawnX = (int)(x + Math.cos(angle) * length);
        int spawnY = (int)(y + Math.sin(angle) * length);

        bullets.add(new Bullet(spawnX, spawnY, dx, dy));
    }

    void reload() {
        if (reloading || ammo == magSize) return;
        reloading = true;
        reloadTimer = reloadTime;
    }

    ArrayList<Bullet> getBullets() {
        return bullets;
    }

    void draw(Graphics2D g2, int px, int py) {
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        Point screen = Game.getFrameLocation();

        int mx = mouse.x - screen.x;
        int my = mouse.y - screen.y;

        double angle = Math.atan2(my - py, mx - px);

        int x2 = px + (int)(Math.cos(angle) * length);
        int y2 = py + (int)(Math.sin(angle) * length);

        g2.setStroke(new BasicStroke(6));
        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(px, py, x2, y2);
    }
}
