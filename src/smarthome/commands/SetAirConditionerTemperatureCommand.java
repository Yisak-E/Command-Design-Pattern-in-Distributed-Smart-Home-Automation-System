package smarthome.commands;

import smarthome.devices.AirConditioner;

public class SetAirConditionerTemperatureCommand implements Command {
    private final AirConditioner airConditioner;
    private final int newTemperature;
    private int previousTemperature;

    public SetAirConditionerTemperatureCommand(AirConditioner airConditioner, int newTemperature) {
        this.airConditioner = airConditioner;
        this.newTemperature = newTemperature;
    }

    @Override
    public void execute() {
        previousTemperature = airConditioner.getTemperature();
        airConditioner.setTemperature(newTemperature);
    }

    @Override
    public void undo() {
        airConditioner.setTemperature(previousTemperature);
    }
}
