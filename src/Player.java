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
        int newX = x;
        int newY = y;

        if (up) {
            newY -= speed;
        }
        if (down) {
            newY += speed;
        }
        if (left) {
            newX -= speed;
        }
        if (right) {
            newX += speed;
        }

        newX = Math.max(size / 2, Math.min(newX, Game.WORLD_W - size / 2));
        newY = Math.max(size / 2, Math.min(newY, Game.WORLD_H - size / 2));
        
        x = newX;
        y = newY;
    }

    void resolveCollision(java.util.ArrayList<Building> buildings, int oldX, int oldY) {
        if (!checkBuildingCollision(buildings)) {
            return;
        }
        
        x = oldX;
        if (!checkBuildingCollision(buildings)) {
            return;
        }
        
        x = oldX;
        y = oldY;
    }

    boolean checkBuildingCollision(java.util.ArrayList<Building> buildings) {
        for (Building b : buildings) {
            if (b.intersects(x, y, size)) {
                return true;
            }
        }
        return false;
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
