package smarthome.commands;

import smarthome.devices.Light;

// Concrete command: switches a light off via its receiver.
public class TurnOffLightCommand implements Command {
    private final Light light;

    // Creates a command bound to the provided light.
    public TurnOffLightCommand(Light light) {
        this.light = light;
    }

    // Executes the off action on the receiver.
    @Override
    public void execute() {
        light.turnOff();
    }

    // Reverts the action by turning the light back on.
    @Override
    public void undo() {
        light.turnOn();
    }
}
