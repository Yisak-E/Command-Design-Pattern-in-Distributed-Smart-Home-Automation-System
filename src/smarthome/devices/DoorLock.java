package smarthome.devices;

public class DoorLock {
    private boolean locked = true;

    public void lock() {
        locked = true;
        System.out.println("Door is Locked");
    }

    public void unlock() {
        locked = false;
        System.out.println("Door is Unlocked");
    }

    public boolean isLocked() {
        return locked;
    }
}
