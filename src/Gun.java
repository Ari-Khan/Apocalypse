/**
 * Gun.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: Abstract base class for all weapon types with fire rate, ammo, and reload mechanics.
 */

// Import statements
import java.awt.*;
import java.util.*;
import javax.sound.sampled.*;
import java.io.*;

// Gun class
public abstract class Gun {
    // Constants
    static final int GUN_STROKE_WIDTH = 6;
    static final int SOUND_SAMPLE_RATE = 44100;
    static final int SOUND_DURATION_SAMPLES = SOUND_SAMPLE_RATE / 10;
    static final int SOUND_MAX_AMPLITUDE = 32767;
    // Gun length for aiming
    int length;

    // Magazine and ammo
    int magSize;
    int ammo;

    // Fire rate control
    int fireDelay;
    int fireTimer;

    // Fire mode
    boolean automatic;

    // Reload mechanics
    boolean reloading;
    int reloadTime;
    int reloadTimer;

    // Active bullets
    ArrayList<Bullet> bullets = new ArrayList<>();

    // Constructor
    public Gun() {
        initStats();
        ammo = magSize;
    } // End constructor

    // Initialize gun statistics
    protected abstract void initStats();

    // Update gun state
    void update() {
        // Update fire timer
        if (fireTimer > 0) {
            fireTimer--;
        }

        // Update reload timer
        if (reloading) {
            reloadTimer--;
            if (reloadTimer <= 0) {
                ammo = magSize;
                reloading = false;
            }
        }

        // Update bullets
        bullets.removeIf(b -> !b.update());
    } // End method

    // Fire gun if able
    void shoot(int x, int y, double dx, double dy) {
        // Check fire conditions
        if (fireTimer > 0 || reloading || ammo <= 0) {
            return;
        }

        // Consume ammo and set fire delay
        ammo--;
        fireTimer = fireDelay;

        // Calculate spawn position
        double angle = Math.atan2(dy, dx);

        int spawnX = (int)(x + Math.cos(angle) * length);
        int spawnY = (int)(y + Math.sin(angle) * length);

        // Add bullet to list
        bullets.add(new Bullet(spawnX, spawnY, dx, dy));
        
        // Play gunshot sound
        playGunshotSound();
    } // End method

    // Auto fire for automatic weapons
    void tryAutoFire(int x, int y, double dx, double dy) {
        // Only auto fire if weapon supports it
        if (!automatic) {
            return;
        }

        shoot(x, y, dx, dy);
    } // End method

    // Begin reload process
    void reload() {
        // Check reload conditions
        if (reloading || ammo == magSize) {
            return;
        }

        // Start reload
        reloading = true;
        reloadTimer = reloadTime;
    } // End method

    // Get active bullets
    ArrayList<Bullet> getBullets() {
        return bullets;
    } // End method

    // Draw gun on screen
    void draw(Graphics2D g2, int px, int py) {
        // Get mouse position
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        Point screen = Game.getFrameLocation();

        int mx = mouse.x - screen.x;
        int my = mouse.y - screen.y;

        // Calculate angle to mouse
        double angle = Math.atan2(my - py, mx - px);

        // Calculate gun end position
        int x2 = px + (int)(Math.cos(angle) * length);
        int y2 = py + (int)(Math.sin(angle) * length);

        // Draw gun barrel
        g2.setStroke(new BasicStroke(GUN_STROKE_WIDTH));
        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(px, py, x2, y2);
    } // End method

    // Play gunshot sound
    private void playGunshotSound() {
        new Thread(() -> {
            try {
                // Generate a simple beep sound as gunshot
                Clip clip = AudioSystem.getClip();
                AudioFormat format = new AudioFormat(SOUND_SAMPLE_RATE, 16, 1, true, false);
                byte[] data = new byte[SOUND_DURATION_SAMPLES];
                
                // Generate simple gunshot-like sound (white noise pulse)
                for (int i = 0; i < data.length; i++) {
                    int sample = (int)(Math.random() * SOUND_MAX_AMPLITUDE * (1.0 - i / (double)data.length));
                    data[i] = (byte)sample;
                }
                
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                AudioInputStream ais = new AudioInputStream(bais, format, data.length);
                clip.open(ais);
                clip.start();
            } catch (Exception e) {
                // Silently fail if sound cannot be played
            }
        }).start();
    } // End method
}
