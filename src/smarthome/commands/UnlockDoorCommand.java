package smarthome.commands;

import smarthome.devices.DoorLock;

public class UnlockDoorCommand implements Command {
    private final DoorLock doorLock;

    public UnlockDoorCommand(DoorLock doorLock) {
        this.doorLock = doorLock;
    }

    @Override
    public void execute() {
        doorLock.unlock();
    }

    @Override
    public void undo() {
        doorLock.lock();
    }
}
