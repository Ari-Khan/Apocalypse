import java.awt.*;

public class Pointer {
    
    void draw(Graphics g, int playerX, int playerY, int heliX, int heliY, int screenW, int screenH) {
        int dx = heliX - playerX;
        int dy = heliY - playerY;
        
        double angle = Math.atan2(dy, dx);
        
        int margin = 60;
        int centerX = screenW / 2;
        int centerY = screenH / 2;
        
        int pointerDist = Math.min(screenW / 2, screenH / 2) - margin;
        
        int pointerX = centerX + (int)(Math.cos(angle) * pointerDist);
        int pointerY = centerY + (int)(Math.sin(angle) * pointerDist);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        
        int arrowSize = 20;
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        
        xPoints[0] = pointerX + (int)(Math.cos(angle) * arrowSize);
        yPoints[0] = pointerY + (int)(Math.sin(angle) * arrowSize);
        
        xPoints[1] = pointerX + (int)(Math.cos(angle + 2.5) * arrowSize * 0.6);
        yPoints[1] = pointerY + (int)(Math.sin(angle + 2.5) * arrowSize * 0.6);
        
        xPoints[2] = pointerX + (int)(Math.cos(angle - 2.5) * arrowSize * 0.6);
        yPoints[2] = pointerY + (int)(Math.sin(angle - 2.5) * arrowSize * 0.6);
        
        g2.setColor(new Color(0, 200, 0));
        g2.fillPolygon(xPoints, yPoints, 3);
        
        g2.setColor(new Color(0, 150, 0));
        g2.drawPolygon(xPoints, yPoints, 3);
        
        g2.setStroke(new BasicStroke(1));
    }
}
