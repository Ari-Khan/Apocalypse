/**
 * Glock17.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: Semi-automatic pistol weapon with 17 round magazine.
 */

// Glock17 class
public class Glock17 extends Gun {

    // Constants
    static final int GLOCK_LENGTH = 30;
    static final int GLOCK_MAG_SIZE = 17;
    static final int GLOCK_FIRE_DELAY = 8;
    static final int GLOCK_RELOAD_TIME = 60;

    // Initialize Glock17 weapon stats
    @Override
    protected void initStats() {
        length = GLOCK_LENGTH;
        magSize = GLOCK_MAG_SIZE;
        fireDelay = GLOCK_FIRE_DELAY;
        reloadTime = GLOCK_RELOAD_TIME;
        automatic = false;
    } // End method
} // End class
