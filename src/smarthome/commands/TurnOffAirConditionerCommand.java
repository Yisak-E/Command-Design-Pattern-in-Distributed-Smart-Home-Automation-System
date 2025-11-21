package smarthome.commands;

import smarthome.receiver.AirConditioner;

// Concrete command: powers down the air conditioner.
public class TurnOffAirConditionerCommand implements Command {
    private final AirConditioner airConditioner;

    // Creates a command bound to the provided air conditioner.
    public TurnOffAirConditionerCommand(AirConditioner airConditioner) {
        this.airConditioner = airConditioner;
    }

    // Executes the off action on the receiver.
    @Override
    public void execute() {
        airConditioner.turnOff();
    }

    // Reverts the action by switching the unit back on.
    @Override
    public void undo() {
        airConditioner.turnOn();
    }
}
