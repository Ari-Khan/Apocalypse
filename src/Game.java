import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel
        implements ActionListener, KeyListener, MouseListener {

    static final int SCREEN_W = 800;
    static final int SCREEN_H = 600;

    static final int WORLD_W = 10000;
    static final int WORLD_H = 10000;

    static final int TILE = 80;

    Timer timer = new Timer(16, this);

    Player player = new Player(
        WORLD_W / 16,
        WORLD_H - (WORLD_H / 16)
    );

    int camX, camY;
    static JFrame frame;

    public Game() {
        frame = new JFrame("Top-Down Wasteland");

        setPreferredSize(new Dimension(SCREEN_W, SCREEN_H));
        setFocusable(true);

        addKeyListener(this);
        addMouseListener(this);

        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        requestFocus();
        timer.start();
    }

    public static Point getFrameLocation() {
        return frame.getLocationOnScreen();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.update();
        player.gun.update();

        camX = player.x - SCREEN_W / 2;
        camY = player.y - SCREEN_H / 2;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ===== OUTER VOID =====
        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, SCREEN_W, SCREEN_H);

        // ===== INNER WORLD =====
        g.setColor(new Color(139, 125, 96));
        g.fillRect(-camX, -camY, WORLD_W, WORLD_H);

        // ===== GRID =====
        g.setColor(new Color(120, 110, 85));
        for (int x = 0; x <= WORLD_W; x += TILE) {
            g.drawLine(x - camX, -camY, x - camX, WORLD_H - camY);
        }
        for (int y = 0; y <= WORLD_H; y += TILE) {
            g.drawLine(-camX, y - camY, WORLD_W - camX, y - camY);
        }

        // ===== BULLETS =====
        for (Bullet b : player.gun.getBullets()) {
            b.draw(g, camX, camY);
        }

        // ===== WORLD BORDER =====
        g.setColor(Color.BLACK);
        g.drawRect(-camX, -camY, WORLD_W, WORLD_H);

        // ===== PLAYER =====
        player.draw(g);

        // ===== AMMO COUNTER =====
        g.setFont(new Font("Monospaced", Font.BOLD, 24));
        g.setColor(Color.WHITE);
        String ammoText = player.gun.ammo + " / " + player.gun.magSize;
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(ammoText);
        int textX = SCREEN_W - textWidth - 20;
        int textY = SCREEN_H - 20;
        
        // Draw shadow for better visibility
        g.setColor(Color.BLACK);
        g.drawString(ammoText, textX + 2, textY + 2);
        
        // Draw actual text
        g.setColor(player.gun.reloading ? Color.YELLOW : Color.WHITE);
        g.drawString(ammoText, textX, textY);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX() + camX;
        int my = e.getY() + camY;

        double dx = mx - player.x;
        double dy = my - player.y;

        player.gun.shoot(player.x, player.y, dx, dy);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);

        if (e.getKeyCode() == KeyEvent.VK_R) {
            player.gun.reload();
        }
    }

    @Override public void keyReleased(KeyEvent e) { player.keyReleased(e); }
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        new Game();
    }
}
