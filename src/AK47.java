/**
 * AK47.java
 * Author: Ari Khan
 * Version: 1.0.0
 * Date: 2025-01-18
 * Description: Automatic rifle weapon with 30 round magazine.
 */

// AK47 class
public class AK47 extends Gun {

    // Initialize AK47 weapon stats
    @Override
    protected void initStats() {
        length = 40;
        magSize = 30;
        fireDelay = 10;
        reloadTime = 90;
        automatic = true;
    } // End method
} // End class
