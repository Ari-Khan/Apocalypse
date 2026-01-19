public class AK47 extends Gun {
    @Override
    protected void initStats() {
        length = 40;
        magSize = 30;
        fireDelay = 10;
        reloadTime = 90;
        automatic = true;
    }
}
