package smarthome.commands;

import smarthome.receiver.AirConditioner;

// Concrete command: powers on the air conditioner.
public class TurnOnAirConditionerCommand implements Command {
    private final AirConditioner airConditioner;

    // Creates a command bound to the provided air conditioner.
    public TurnOnAirConditionerCommand(AirConditioner airConditioner) {
        this.airConditioner = airConditioner;
    }

    // Executes the on action on the receiver.
    @Override
    public void execute() {
        airConditioner.turnOn();
    }

    // Reverts the action by switching the unit off.
    @Override
    public void undo() {
        airConditioner.turnOff();
    }
}
