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
    boolean dead = false;

    long startTime = System.currentTimeMillis();

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

        spawnAKs(20);
        spawnEnemySpawners(750);

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

    void spawnEnemySpawners(int amount) {
        Random r = new Random();
        for (int i = 0; i < amount; i++)
            enemySpawners.add(new EnemySpawner(
                r.nextInt(WORLD_W),
                r.nextInt(WORLD_H)
            ));
    }

    public static Point getFrameLocation() {
        return frame.getLocationOnScreen();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (player.health <= 0) {
            dead = true;
            timer.stop();
            repaint();
            return;
        }

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

        // BULLET → ENEMY DAMAGE
        ArrayList<Bullet> bullets = player.gun.getBullets();

        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);

            for (int j = enemies.size() - 1; j >= 0; j--) {
                Enemy e2 = enemies.get(j);

                double dx = b.x - e2.x;
                double dy = b.y - e2.y;
                double r = e2.size / 2.0;

                if (dx * dx + dy * dy < r * r) {
                    e2.health -= 1;
                    bullets.remove(i);

                    if (e2.health <= 0)
                        enemies.remove(j);

                    break;
                }
            }
        }

        // ENEMY → PLAYER DAMAGE (enemy dies on contact)
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy e2 = enemies.get(i);

            int dx = e2.x - player.x;
            int dy = e2.y - player.y;
            int r = (e2.size / 2) + (player.size / 2);

            if (dx * dx + dy * dy < r * r) {
                player.health -= 10;
                enemies.remove(i);
                break;
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

        for (GunSpawner s : akSpawners) {
            s.draw(g, camX, camY);
            s.drawHint(g, player.x, player.y, camX, camY);
        }

        for (EnemySpawner es : enemySpawners)
            es.draw(g, camX, camY);

        for (Enemy e2 : enemies)
            e2.draw(g, camX, camY);

        player.draw(g);

        // HEALTH BAR
        int barW = 120;
        int barH = 12;
        int hx = 20;
        int hy = SCREEN_H - 40;

        g.setColor(Color.BLACK);
        g.drawRect(hx, hy, barW, barH);

        g.setColor(Color.GREEN);
        int hpW = (int) (barW * (player.health / (double) player.maxHealth));
        g.fillRect(hx, hy, hpW, barH);

        // AMMO
        String ammo = player.gun.ammo + " / " + player.gun.magSize;
        int ax = SCREEN_W - g.getFontMetrics().stringWidth(ammo) - 20;
        int ay = SCREEN_H - 20;

        g.setColor(Color.BLACK);
        g.drawString(ammo, ax + 2, ay + 2);

        g.setColor(player.gun.reloading ? Color.YELLOW : Color.WHITE);
        g.drawString(ammo, ax, ay);

        // TIMER
        long elapsed = System.currentTimeMillis() - startTime;
        long ms = elapsed % 1000;
        long sec = (elapsed / 1000) % 60;
        long min = elapsed / 60000;

        String time = String.format("%02d:%02d.%03d", min, sec, ms);
        int tx = 20;
        int ty = 30;

        g.setColor(Color.BLACK);
        g.drawString(time, tx + 2, ty + 2);
        g.setColor(Color.WHITE);
        g.drawString(time, tx, ty);

        // YOU DIED
        if (dead) {
            g.setFont(new Font("Monospaced", Font.BOLD, 48));
            String msg = "YOU DIED";

            int x = (SCREEN_W - g.getFontMetrics().stringWidth(msg)) / 2;
            int y = SCREEN_H / 2;

            g.setColor(Color.BLACK);
            g.drawString(msg, x + 3, y + 3);
            g.setColor(Color.RED);
            g.drawString(msg, x, y);
        }
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

    @Override public void mouseReleased(MouseEvent e) { mouseDown = false; }

    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);

        if (e.getKeyCode() == KeyEvent.VK_R)
            player.gun.reload();

        if (e.getKeyCode() == KeyEvent.VK_E)
            for (GunSpawner s : akSpawners)
                if (s.isNear(player.x, player.y)) {
                    player.gun = s.spawnGun();
                    break;
                }
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
