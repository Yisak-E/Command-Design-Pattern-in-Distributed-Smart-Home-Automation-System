package smarthome.commands;

import smarthome.devices.AirConditioner;

// Concrete command: adjusts the target temperature.
public class SetAirConditionerTemperatureCommand implements Command {
    private final AirConditioner airConditioner;
    private final int newTemperature;
    private int previousTemperature;

    // Creates a command with the desired temperature.
    public SetAirConditionerTemperatureCommand(AirConditioner airConditioner, int newTemperature) {
        this.airConditioner = airConditioner;
        this.newTemperature = newTemperature;
    }

    // Stores prior state and applies the new temperature.
    @Override
    public void execute() {
        previousTemperature = airConditioner.getTemperature();
        airConditioner.setTemperature(newTemperature);
    }

    // Restores the previous temperature value.
    @Override
    public void undo() {
        airConditioner.setTemperature(previousTemperature);
    }
}
