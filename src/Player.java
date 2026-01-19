import java.awt.*;
import java.awt.event.*;

public class Player {

    int x, y;
    int size = 40;
    int speed = 6;
    int maxHealth = 100;
    int health = maxHealth;

    boolean up, down, left, right;

    Gun gun;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        gun = new Glock17();
    }

    void update() {
        if (up) {
            y -= speed;
        }
        if (down) {
            y += speed;
        }
        if (left) {
            x -= speed;
        }
        if (right) {
            x += speed;
        }

        x = Math.max(size / 2, Math.min(x, Game.WORLD_W - size / 2));
        y = Math.max(size / 2, Math.min(y, Game.WORLD_H - size / 2));
    }

    boolean checkBuildingCollision(java.util.ArrayList<Building> buildings) {
        for (Building b : buildings) {
            int halfSize = size / 2;
            if (x + halfSize > b.x && x - halfSize < b.x + b.width &&
                y + halfSize > b.y && y - halfSize < b.y + b.height) {
                return true;
            }
        }
        return false;
    }

    void undoMove(int oldX, int oldY) {
        x = oldX;
        y = oldY;
    }

    void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int sx = Game.SCREEN_W / 2;
        int sy = Game.SCREEN_H / 2;

        gun.draw(g2, sx, sy);

        g2.setColor(new Color(70, 100, 160));
        g2.fillOval(sx - size / 2, sy - size / 2, size, size);
    }

    void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> up = true;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> down = true;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> left = true;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> right = true;
        }
    }

    void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> up = false;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> down = false;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> left = false;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> right = false;
        }
    }
}
