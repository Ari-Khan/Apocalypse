/**
 * Helicopter.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: Extraction point for player escape and game completion.
 */

import java.awt.*;

public class Helicopter {
    
    int x, y;
    int size = 240;
    
    public Helicopter(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    void draw(Graphics g, int camX, int camY) {
        int screenX = x - camX;
        int screenY = y - camY;
        
        g.setColor(new Color(50, 100, 50));
        g.fillRect(screenX - 20, screenY + 60, 40, 100);
        
        g.setColor(new Color(30, 80, 30));
        g.fillOval(screenX - 25, screenY + 150, 50, 40);
        
        g.setColor(new Color(60, 120, 60));
        g.fillOval(screenX - 70, screenY - 50, 140, 140);
        
        g.setColor(new Color(40, 90, 40));
        g.fillOval(screenX - 55, screenY - 30, 110, 110);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(8));
        g2.setColor(new Color(80, 80, 80));
        
        g2.drawLine(screenX - 110, screenY, screenX + 110, screenY);
        g2.drawLine(screenX, screenY - 110, screenX, screenY + 110);
        
        g2.setStroke(new BasicStroke(6));
        g2.setColor(new Color(60, 60, 60));
        g2.drawLine(screenX - 20, screenY + 160, screenX + 20, screenY + 160);
        
        g2.setStroke(new BasicStroke(1));
    }
    
    void drawHint(Graphics g, int px, int py, int camX, int camY) {
        if (!isNear(px, py)) {
            return;
        }
        
        g.setColor(Color.WHITE);
        g.drawString("Press K", x - camX - 20, y - camY - size);
    }
    
    boolean isNear(int px, int py) {
        int dx = px - x;
        int dy = py - y;
        return dx * dx + dy * dy < 80 * 80;
    }
}
