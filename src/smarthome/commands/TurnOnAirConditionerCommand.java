package smarthome.commands;

import smarthome.devices.AirConditioner;

public class TurnOnAirConditionerCommand implements Command {
    private final AirConditioner airConditioner;

    public TurnOnAirConditionerCommand(AirConditioner airConditioner) {
        this.airConditioner = airConditioner;
    }

    @Override
    public void execute() {
        airConditioner.turnOn();
    }

    @Override
    public void undo() {
        airConditioner.turnOff();
    }
}
