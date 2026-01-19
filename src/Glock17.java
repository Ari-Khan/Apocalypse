public class Glock17 extends Gun {

    @Override
    protected void initStats() {
        length = 30;
        magSize = 17;
        fireDelay = 8;
        reloadTime = 60;
        automatic = false;
    }
}
