package smarthome.commands;

import smarthome.devices.Light;

// Concrete command: switches a light on via its receiver.
public class TurnOnLightCommand implements Command {
    private final Light light;

    // Creates a command bound to the provided light.
    public TurnOnLightCommand(Light light) {
        this.light = light;
    }

    // Executes the on action on the receiver.
    @Override
    public void execute() {
        light.turnOn();
    }

    // Reverts the action by turning the light off.
    @Override
    public void undo() {
        light.turnOff();
    }
}
