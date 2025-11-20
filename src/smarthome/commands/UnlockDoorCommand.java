package smarthome.commands;

import smarthome.devices.DoorLock;

// Concrete command: releases the door lock.
public class UnlockDoorCommand implements Command {
    private final DoorLock doorLock;

    // Creates a command bound to the provided lock.
    public UnlockDoorCommand(DoorLock doorLock) {
        this.doorLock = doorLock;
    }

    // Executes the unlock action on the receiver.
    @Override
    public void execute() {
        doorLock.unlock();
    }

    // Reverts the action by locking the door.
    @Override
    public void undo() {
        doorLock.lock();
    }
}
