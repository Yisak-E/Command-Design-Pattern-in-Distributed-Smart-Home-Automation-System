package smarthome.devices;

// Receiver: represents a smart door lock.
public class DoorLock {
    private boolean locked = true;

    // Engages the lock and prints feedback.
    public void lock() {
        locked = true;
        System.out.println("Door is Locked");
    }

    // Releases the lock and prints feedback.
    public void unlock() {
        locked = false;
        System.out.println("Door is Unlocked");
    }

    // Indicates whether the door is currently locked.
    public boolean isLocked() {
        return locked;
    }
}
