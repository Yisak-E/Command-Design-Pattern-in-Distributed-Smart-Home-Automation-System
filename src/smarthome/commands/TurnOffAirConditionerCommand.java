package smarthome.commands;

import smarthome.devices.AirConditioner;

public class TurnOffAirConditionerCommand implements Command {
    private final AirConditioner airConditioner;

    public TurnOffAirConditionerCommand(AirConditioner airConditioner) {
        this.airConditioner = airConditioner;
    }

    @Override
    public void execute() {
        airConditioner.turnOff();
    }

    @Override
    public void undo() {
        airConditioner.turnOn();
    }
}
