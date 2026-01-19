import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel
        implements ActionListener, KeyListener, MouseListener {

    static final int SCREEN_W = 800;
    static final int SCREEN_H = 600;

    static final int WORLD_W = 10000;
    static final int WORLD_H = 10000;

    static final int TILE = 80;

    boolean mouseDown;

    Timer timer = new Timer(16, this);

    Player player = new Player(
        WORLD_W / 16,
        WORLD_H - (WORLD_H / 16)
    );

    ArrayList<GunSpawner> akSpawners = new ArrayList<>();
    ArrayList<EnemySpawner> enemySpawners = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();

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

        spawnAKs(25);
        spawnEnemySpawners(1000);

        requestFocus();
        timer.start();
    }

    void spawnAKs(int amount) {
        Random r = new Random();
        for (int i = 0; i < amount; i++)
            akSpawners.add(new GunSpawner(
                r.nextInt(WORLD_W),
                r.nextInt(WORLD_H)
            ));
    }

    public static Point getFrameLocation() {
        return frame.getLocationOnScreen();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.update();
        player.gun.update();

        for (EnemySpawner es : enemySpawners)
            es.update(player.x, player.y, enemies);

        for (Enemy e2 : enemies)
            e2.update(player.x, player.y);

        if (mouseDown) {
            Point m = MouseInfo.getPointerInfo().getLocation();
            Point f = getFrameLocation();

            player.gun.tryAutoFire(
                player.x,
                player.y,
                m.x - f.x + camX - player.x,
                m.y - f.y + camY - player.y
            );
        }

        camX = player.x - SCREEN_W / 2;
        camY = player.y - SCREEN_H / 2;

        ArrayList<Bullet> bullets = player.gun.getBullets();

        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);

            for (int j = enemies.size() - 1; j >= 0; j--) {
                Enemy e2 = enemies.get(j);

                double dx = b.x - e2.x;
                double dy = b.y - e2.y;
                double r = e2.size / 2.0;

                if (dx * dx + dy * dy < r * r) {
                    e2.health -= 1; // 2 shots = dead
                    bullets.remove(i);

                    if (e2.health <= 0)
                        enemies.remove(j);

                    break;
                }
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Monospaced", Font.BOLD, 24));

        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, SCREEN_W, SCREEN_H);

        g.setColor(new Color(139, 125, 96));
        g.fillRect(-camX, -camY, WORLD_W, WORLD_H);

        g.setColor(new Color(120, 110, 85));
        for (int x = 0; x <= WORLD_W; x += TILE)
            g.drawLine(x - camX, -camY, x - camX, WORLD_H - camY);

        for (int y = 0; y <= WORLD_H; y += TILE)
            g.drawLine(-camX, y - camY, WORLD_W - camX, y - camY);

        for (Bullet b : player.gun.getBullets())
            b.draw(g, camX, camY);

        g.setColor(Color.BLACK);
        g.drawRect(-camX, -camY, WORLD_W, WORLD_H);

        for (GunSpawner s : akSpawners) {
            s.draw(g, camX, camY);
            s.drawHint(g, player.x, player.y, camX, camY);
        }

        for (EnemySpawner es : enemySpawners)
            es.draw(g, camX, camY);

        for (Enemy e2 : enemies)
            e2.draw(g, camX, camY);

        player.draw(g);

        String ammo = player.gun.ammo + " / " + player.gun.magSize;

        int x = SCREEN_W - g.getFontMetrics().stringWidth(ammo) - 20;
        int y = SCREEN_H - 20;

        g.setColor(Color.BLACK);
        g.drawString(ammo, x + 2, y + 2);

        g.setColor(player.gun.reloading ? Color.YELLOW : Color.WHITE);
        g.drawString(ammo, x, y);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = true;

        Point m = MouseInfo.getPointerInfo().getLocation();
        Point f = getFrameLocation();

        player.gun.shoot(
            player.x,
            player.y,
            m.x - f.x + camX - player.x,
            m.y - f.y + camY - player.y
        );
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);

        if (e.getKeyCode() == KeyEvent.VK_R)
            player.gun.reload();

        if (e.getKeyCode() == KeyEvent.VK_K)
            for (GunSpawner s : akSpawners)
                if (s.isNear(player.x, player.y)) {
                    player.gun = s.spawnGun();
                    break;
                }
    }

    void spawnEnemySpawners(int amount) {
        Random r = new Random();
        for (int i = 0; i < amount; i++)
            enemySpawners.add(new EnemySpawner(
                r.nextInt(WORLD_W),
                r.nextInt(WORLD_H)
            ));
    }

    @Override public void keyReleased(KeyEvent e) { player.keyReleased(e); }
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        new Game();
    }
}
