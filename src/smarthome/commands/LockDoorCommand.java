package smarthome.commands;

import smarthome.receiver.DoorLock;

// Concrete command: secures the door lock.
public class LockDoorCommand implements Command {
    private final DoorLock doorLock;

    // Creates a command bound to the provided lock.
    public LockDoorCommand(DoorLock doorLock) {
        this.doorLock = doorLock;
    }

    // Executes the lock action on the receiver.
    @Override
    public void execute() {
        doorLock.lock();
    }

    // Reverts the action by unlocking the door.
    @Override
    public void undo() {
        doorLock.unlock();
    }
}
