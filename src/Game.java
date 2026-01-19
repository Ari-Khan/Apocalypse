/** 
 * Game.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: Main game class for the Apocalypse game.
 * Identity: I've been playing survival games for years, and I wanted to make my own to demonstrate my proficiency in Java programming and development concepts. This project showcases my ability to create a complex, interactive application with graphics, user input handling, and game mechanics.
 * Purpose: The purpose of this project is to create an engaging 2D survival game where the player must navigate a post-apocalyptic world, avoid enemies, collect weapons, and reach a helicopter to escape. It demonstrates my skills in object-oriented programming, game design, and user interface development.
 * Input: Keyboard and mouse inputs for player movement, shooting, reloading, and interactions.
 * Process: The game processes player input, updates game elements, checks for collisions, and renders the game world each frame.
 * Output: A graphical window displaying the game world, player status, score, and time elapsed, as well as game sound effects.
 */

// Import statements
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

// Main Game class
public class Game extends JPanel
        implements ActionListener, KeyListener, MouseListener {

    // Define constants
    static final String FRAME_TITLE = "Apocalypse";
    static final int SCREEN_W = 800;
    static final int SCREEN_H = 600;
    static final int WORLD_W = 10000;
    static final int WORLD_H = 10000;
    static final int TILE = 80;
    static final int TIMER_DELAY_MS = 16;
    static final int BUILDING_COUNT = 200;
    static final int AK_SPAWNER_COUNT = 20;
    static final int ENEMY_SPAWNER_COUNT = 500;
    static final int SPAWNER_HALF_SIZE = 30;
    static final int HELI_SPACING = 300;
    static final int PLAYER_SIZE = 40;
    static final int PLAYER_SPACING = 200;
    static final int PLAYER_START_DIVISOR = 16;
    static final int HELI_START_MULTIPLIER = 15;
    static final int HELI_START_DIVISOR = 16;
    static final int BUILDING_SIZE_MIN_MULT = 2;
    static final int BUILDING_SIZE_RANGE = 3;

    // Define variables
    int camX, camY;
    int score = 0;
    boolean mouseDown;
    boolean dead = false;
    boolean escaped = false;
    long startTime = System.currentTimeMillis();

    // Initialize objects
    Timer timer = new Timer(TIMER_DELAY_MS, this);

    Player player = new Player(
        WORLD_W / PLAYER_START_DIVISOR,
        WORLD_H - (WORLD_H / PLAYER_START_DIVISOR)
    );

    Helicopter helicopter = new Helicopter(
        WORLD_W * HELI_START_MULTIPLIER / HELI_START_DIVISOR,
        WORLD_H / HELI_START_DIVISOR
    );

    Pointer pointer = new Pointer();

    // Initialize lists
    ArrayList<GunSpawner> akSpawners = new ArrayList<>();
    ArrayList<EnemySpawner> enemySpawners = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Building> buildings = new ArrayList<>();

    // JFrame reference
    static JFrame frame;

    // Game constructor
    public Game() {
        // Configure JFrame
        frame = new JFrame(FRAME_TITLE);

        setPreferredSize(new Dimension(SCREEN_W, SCREEN_H));
        setFocusable(true);

        addKeyListener(this);
        addMouseListener(this);

        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Spawn elements
        spawnBuildings(BUILDING_COUNT);
        spawnAKs(AK_SPAWNER_COUNT);
        spawnEnemySpawners(ENEMY_SPAWNER_COUNT);

        // Set focus and start timer
        requestFocus();
        timer.start();
    } // End constructor

    // Spawn AK47 spawners
    void spawnAKs(int amount) {
        Random r = new Random();
        // Loop to create spawners
        for (int i = 0; i < amount; i++) {
            int x = r.nextInt(WORLD_W);
            int y = r.nextInt(WORLD_H);
            akSpawners.add(new GunSpawner(x, y));
        } // End for loop
        
        // Remove any spawners touching buildings
        akSpawners.removeIf(s -> isSpawnerTouchingBuilding(s.x, s.y));
    } // End method

    // Spawn enemy spawners
    void spawnEnemySpawners(int amount) {
        Random r = new Random();
        // Loop to create spawners
        for (int i = 0; i < amount; i++) {
            int x = r.nextInt(WORLD_W);
            int y = r.nextInt(WORLD_H);
            enemySpawners.add(new EnemySpawner(x, y));
        } // End for loop
        
        // Remove spawners touching buildings or near helicopter
        enemySpawners.removeIf(es -> isSpawnerTouchingBuilding(es.x, es.y) || isNearHelicopter(es.x, es.y));
    } // End method

    // Check if spawner is touching a building
    private boolean isSpawnerTouchingBuilding(int x, int y) {
        for (Building b : buildings) {
            if (x + SPAWNER_HALF_SIZE > b.x && x - SPAWNER_HALF_SIZE < b.x + b.width &&
                y + SPAWNER_HALF_SIZE > b.y && y - SPAWNER_HALF_SIZE < b.y + b.height) {
                return true;
            }
        } // End for loop
        return false;
    } // End method

    // Check if spawner is near helicopter
    private boolean isNearHelicopter(int x, int y) {
        int heliX = helicopter.x;
        int heliY = helicopter.y;
        int heliSize = helicopter.size;
        return x + SPAWNER_HALF_SIZE > heliX - HELI_SPACING && x - SPAWNER_HALF_SIZE < heliX + heliSize + HELI_SPACING &&
               y + SPAWNER_HALF_SIZE > heliY - HELI_SPACING && y - SPAWNER_HALF_SIZE < heliY + heliSize + HELI_SPACING;
    } // End method

    // Spawn random buildings on map
    void spawnBuildings(int amount) {
        Random r = new Random();
        // Loop to create buildings
        for (int i = 0; i < amount; i++) {
            int size = (r.nextInt(BUILDING_SIZE_RANGE) + BUILDING_SIZE_MIN_MULT) * TILE;
            int x, y;
            boolean validSpawn;
            do {
                x = r.nextInt(WORLD_W - size);
                y = r.nextInt(WORLD_H - size);
                // Check if building would spawn near player spawn location
                int playerSpawnX = WORLD_W / PLAYER_START_DIVISOR;
                int playerSpawnY = WORLD_H - (WORLD_H / PLAYER_START_DIVISOR);
                int playerSize = PLAYER_SIZE;
                int playerSpacing = PLAYER_SPACING; // Safe spacing around player spawn

                // Avoid overlapping the player spawn area
                validSpawn = !(x + size > playerSpawnX - playerSpacing && x < playerSpawnX + playerSize + playerSpacing &&
                               y + size > playerSpawnY - playerSpacing && y < playerSpawnY + playerSize + playerSpacing);

                // Avoid overlapping the helicopter extraction area
                if (validSpawn) {
                    int heliX = helicopter.x;
                    int heliY = helicopter.y;
                    int heliSize = helicopter.size;
                    validSpawn = !(x + size > heliX - HELI_SPACING && x < heliX + heliSize + HELI_SPACING &&
                                   y + size > heliY - HELI_SPACING && y < heliY + heliSize + HELI_SPACING);
                }
            } while (!validSpawn);
            
            buildings.add(new Building(x, y, size, size));
        } // End for loop
    } // End method

    // Get frame location on screen
    public static Point getFrameLocation() {
        return frame.getLocationOnScreen();
    } // End method

    // Draw text with black shadow effect
    private void drawTextWithShadow(Graphics g, String text, int x, int y, Color textColor) {
        g.setColor(Color.BLACK);
        g.drawString(text, x + 2, y + 2);
        g.setColor(textColor);
        g.drawString(text, x, y);
    } // End method

    // Check collision between two circles
    private boolean checkCircleCollision(double x1, double y1, double r1, double x2, double y2, double r2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double totalRadius = r1 + r2;
        return dx * dx + dy * dy < totalRadius * totalRadius;
    } // End method

    // Convert mouse screen coordinates to world coordinates
    private Point getMouseWorldCoordinates() {
        Point m = MouseInfo.getPointerInfo().getLocation();
        Point f = getFrameLocation();
        return new Point(m.x - f.x + camX, m.y - f.y + camY);
    } // End method

    // Update game logic each frame
    @Override
    public void actionPerformed(ActionEvent e) {
        // Check for player death
        if (player.health <= 0) {
            dead = true;
            timer.stop();
            repaint();
            return;
        }

        // Check for player escape
        if (escaped) {
            timer.stop();
            repaint();
            return;
        }

        // Update player
        int oldPlayerX = player.x;
        int oldPlayerY = player.y;
        player.update();
        
        // Check building collisions
        if (player.checkBuildingCollision(buildings)) {
            player.undoMove(oldPlayerX, oldPlayerY);
        }
        
        // Update gun
        player.gun.update();

        // Update enemy spawners
        for (EnemySpawner es : enemySpawners) {
            es.update(player.x, player.y, enemies);
        } // End for loop

        // Remove any spawners touching buildings
        akSpawners.removeIf(s -> isSpawnerTouchingBuilding(s.x, s.y));
        enemySpawners.removeIf(es -> isSpawnerTouchingBuilding(es.x, es.y));

        // Update enemies
        for (Enemy e2 : enemies) {
            int oldEnemyX = e2.x;
            int oldEnemyY = e2.y;
            e2.update(player.x, player.y);
            
            // Check building collisions
            if (e2.checkBuildingCollision(buildings)) {
                e2.undoMove(oldEnemyX, oldEnemyY);
            }
        } // End for loop

        // Remove boxed-in enemies
        enemies.removeIf(enemy -> enemy.isStuckInBuilding(buildings));

        // Handle automatic firing
        if (mouseDown) {
            Point worldMouse = getMouseWorldCoordinates();
            player.gun.tryAutoFire(
                player.x,
                player.y,
                worldMouse.x - player.x,
                worldMouse.y - player.y
            );
        }

        // Update camera position
        camX = player.x - SCREEN_W / 2;
        camY = player.y - SCREEN_H / 2;

        // Get bullets
        ArrayList<Bullet> bullets = player.gun.getBullets();

        // Check bullet collisions with buildings
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);

            // Check collision with buildings
            for (Building building : buildings) {
                if (building.intersectsPoint(b.x, b.y)) {
                    bullets.remove(i);
                    break;
                }
            } // End for loop
        } // End for loop

        // Check bullet collisions with enemies
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);

            // Check collision with enemies
            for (int j = enemies.size() - 1; j >= 0; j--) {
                Enemy e2 = enemies.get(j);

                // Check circle collision
                if (checkCircleCollision(b.x, b.y, 3, e2.x, e2.y, e2.size / 2.0)) {
                    e2.health -= 1;
                    bullets.remove(i);

                    // Handle enemy death and spawning children
                    if (e2.health <= 0) {
                        e2.spawnChildren(enemies, 3);
                        enemies.remove(j);
                        score += 10;
                    }
                    break;
                }
            } // End for loop
        } // End for loop

        // Check enemy collisions with player
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy e2 = enemies.get(i);

            if (checkCircleCollision(e2.x, e2.y, e2.size / 2.0, player.x, player.y, player.size / 2.0)) {
                player.health -= 10;
                enemies.remove(i);
                break;
            }
        } // End for loop

        repaint();
    } // End method

    // Render game graphics
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Monospaced", Font.BOLD, 24));

        // Paint background
        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, SCREEN_W, SCREEN_H);

        // Paint world terrain
        g.setColor(new Color(139, 125, 96));
        g.fillRect(-camX, -camY, WORLD_W, WORLD_H);

        // Draw grid
        g.setColor(new Color(120, 110, 85));
        for (int x = 0; x <= WORLD_W; x += TILE) {
            g.drawLine(x - camX, -camY, x - camX, WORLD_H - camY);
        } // End for loop

        // Draw horizontal grid lines
        for (int y = 0; y <= WORLD_H; y += TILE) {
            g.drawLine(-camX, y - camY, WORLD_W - camX, y - camY);
        } // End for loop

        // Draw world entities
        for (Building building : buildings) {
            building.draw(g, camX, camY);
        } // End for loop

        // Draw bullets
        for (Bullet b : player.gun.getBullets()) {
            b.draw(g, camX, camY);
        } // End for loop

        // Draw AK spawners
        for (GunSpawner s : akSpawners) {
            s.draw(g, camX, camY);
            s.drawHint(g, player.x, player.y, camX, camY);
        } // End for loop

        // Draw enemy spawners
        for (EnemySpawner es : enemySpawners) {
            es.draw(g, camX, camY);
        } // End for loop

        // Draw enemies
        for (Enemy e2 : enemies) {
            e2.draw(g, camX, camY);
        } // End for loop

        // Draw helicopter
        helicopter.draw(g, camX, camY);
        helicopter.drawHint(g, player.x, player.y, camX, camY);

        // Draw player
        player.draw(g);

        // Draw health bar
        int barW = 120;
        int barH = 12;
        int hx = 20;
        int hy = SCREEN_H - 40;

        // Draw health bar background
        g.setColor(Color.BLACK);
        g.drawRect(hx, hy, barW, barH);

        // Draw current health
        g.setColor(Color.GREEN);
        int hpW = (int) (barW * (player.health / (double) player.maxHealth));
        g.fillRect(hx, hy, hpW, barH);

        // Draw ammo counter
        String ammo = player.gun.ammo + " / " + player.gun.magSize;
        int ax = SCREEN_W - g.getFontMetrics().stringWidth(ammo) - 20;
        int ay = SCREEN_H - 20;

        drawTextWithShadow(g, ammo, ax, ay, player.gun.reloading ? Color.YELLOW : Color.WHITE);

        // Show reload prompt when ammo is empty
        if (player.gun.ammo <= 0 && !player.gun.reloading) {
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            String reloadText = "Press R to Reload";
            int rx = SCREEN_W - g.getFontMetrics().stringWidth(reloadText) - 20;
            drawTextWithShadow(g, reloadText, rx, ay - 30, Color.RED);
            g.setFont(new Font("Monospaced", Font.BOLD, 24));
        }

        // Calculate and draw elapsed time
        long elapsed = System.currentTimeMillis() - startTime;
        long ms = elapsed % 1000;
        long sec = (elapsed / 1000) % 60;
        long min = elapsed / 60000;

        // Draw time and score
        String time = String.format("%02d:%02d.%03d", min, sec, ms);
        drawTextWithShadow(g, time, 20, 30, Color.WHITE);
        drawTextWithShadow(g, "Score: " + score, 20, 60, Color.WHITE);

        // Draw navigation pointer to helicopter
        if (!escaped) {
            pointer.draw(g, player.x, player.y, helicopter.x, helicopter.y, SCREEN_W, SCREEN_H);
        }

        // Draw game over screens
        if (dead) {
            g.setFont(new Font("Monospaced", Font.BOLD, 48));
            String msg = "YOU DIED";
            int x = (SCREEN_W - g.getFontMetrics().stringWidth(msg)) / 2;
            drawTextWithShadow(g, msg, x, SCREEN_H / 2, Color.RED);
            
            g.setFont(new Font("Monospaced", Font.BOLD, 24));
            String replay = "Press Y to Replay";
            int rx = (SCREEN_W - g.getFontMetrics().stringWidth(replay)) / 2;
            drawTextWithShadow(g, replay, rx, SCREEN_H / 2 + 80, Color.WHITE);
        }

        // Draw escape success screen
        if (escaped) {
            g.setFont(new Font("Monospaced", Font.BOLD, 48));
            String msg = "ESCAPED!";
            int x = (SCREEN_W - g.getFontMetrics().stringWidth(msg)) / 2;
            drawTextWithShadow(g, msg, x, SCREEN_H / 2, Color.GREEN);
            
            g.setFont(new Font("Monospaced", Font.BOLD, 24));
            String replay = "Press Y to Replay";
            int rx = (SCREEN_W - g.getFontMetrics().stringWidth(replay)) / 2;
            drawTextWithShadow(g, replay, rx, SCREEN_H / 2 + 80, Color.WHITE);
        }
    } // End method

    // Handle mouse click to fire gun
    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = true;

        Point worldMouse = getMouseWorldCoordinates();
        player.gun.shoot(
            player.x,
            player.y,
            worldMouse.x - player.x,
            worldMouse.y - player.y
        );
    } // End method

    // Stop firing on mouse release
    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
    } // End method

    // Reset game and start new session
    private void restart() {
        // Reset player and game state
        player = new Player(WORLD_W / 16, WORLD_H - (WORLD_H / 16));
        helicopter = new Helicopter(WORLD_W * 15 / 16, WORLD_H / 16);
        pointer = new Pointer();
        akSpawners.clear();
        enemySpawners.clear();
        enemies.clear();
        buildings.clear();
        
        score = 0;
        mouseDown = false;
        dead = false;
        escaped = false;
        startTime = System.currentTimeMillis();
        
        // Spawn elements
        spawnAKs(20);
        spawnEnemySpawners(750);
        spawnBuildings(100);
        
        timer.start();
    } // End method

    // Handle keyboard input
    @Override
    public void keyPressed(KeyEvent e) {
        // Restart game on Y key
        if (e.getKeyCode() == KeyEvent.VK_Y) {
            if (dead || escaped) {
                restart();
                return;
            }
        }

        player.keyPressed(e);

        // Handle reload input
        if (e.getKeyCode() == KeyEvent.VK_R) {
            player.gun.reload();
        }

        // Handle escape input
        if (e.getKeyCode() == KeyEvent.VK_K) {
            if (helicopter.isNear(player.x, player.y)) {
                escaped = true;
            }
        }

        // Handle AK47 pickup input
        if (e.getKeyCode() == KeyEvent.VK_E) {
            for (GunSpawner s : akSpawners) {
                if (s.isNear(player.x, player.y)) {
                    player.gun = s.spawnGun();
                    break;
                }
            }
        }
    } // End method

    // Handle key release
    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    } // End method

    // Unused interface methods
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // Main method to start the game
    public static void main(String[] args) {
        new Game();
    } // End method
} // End class
