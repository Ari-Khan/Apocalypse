import java.awt.*;

public class Building {
    
    int x, y;
    int width, height;
    
    public Building(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    boolean intersects(int px, int py, int size) {
        int halfSize = size / 2;
        return px + halfSize > x && px - halfSize < x + width &&
               py + halfSize > y && py - halfSize < y + height;
    }
    
    boolean intersectsPoint(double px, double py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }
    
    void draw(Graphics g, int camX, int camY) {
        g.setColor(new Color(80, 80, 80));
        g.fillRect(x - camX, y - camY, width, height);
        
        g.setColor(new Color(60, 60, 60));
        g.drawRect(x - camX, y - camY, width, height);
    }
}
