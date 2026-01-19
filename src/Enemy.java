import java.awt.*;

public class Enemy {

    int x, y;
    int size = 30;

    int speed = 4;

    int maxHealth = 2;
    int health = maxHealth;
    int generation = 0;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Enemy(int x, int y, int generation) {
        this.x = x;
        this.y = y;
        this.generation = generation;
        this.size = 30 - (generation * 5);
        this.maxHealth = Math.max(1, 2 - generation);
        this.health = maxHealth;
        this.speed = 3 + generation;
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

    void hit() {
        health--;
    }

    boolean isDead() {
        return health <= 0;
    }

    boolean checkCollision(int otherX, int otherY, int otherSize) {
        int dx = otherX - x;
        int dy = otherY - y;
        int totalRadius = (size / 2) + (otherSize / 2);
        return dx * dx + dy * dy < totalRadius * totalRadius;
    }

    void spawnChildren(java.util.ArrayList<Enemy> enemies, int depth) {
        if (depth <= 0 || generation >= 3) {
            return;
        }

        int count = Math.max(1, 4 - generation);
        spawnChildrenRecursive(enemies, count, 0, generation + 1);
    }

    private void spawnChildrenRecursive(java.util.ArrayList<Enemy> enemies, int totalCount, int index, int childGen) {
        if (index >= totalCount) {
            return;
        }

        double angleStep = 2 * Math.PI / totalCount;
        double angle = angleStep * index;
        int spawnDist = size;
        int spawnX = x + (int)(Math.cos(angle) * spawnDist);
        int spawnY = y + (int)(Math.sin(angle) * spawnDist);
        
        enemies.add(new Enemy(spawnX, spawnY, childGen));
        
        spawnChildrenRecursive(enemies, totalCount, index + 1, childGen);
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
