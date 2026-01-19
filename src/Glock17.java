/**
 * Glock17.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: Semi-automatic pistol weapon with 17 round magazine.
 */

// Glock17 class
public class Glock17 extends Gun {

    // Initialize Glock17 weapon stats
    @Override
    protected void initStats() {
        length = 30;
        magSize = 17;
        fireDelay = 8;
        reloadTime = 60;
        automatic = false;
    } // End method
} // End class
