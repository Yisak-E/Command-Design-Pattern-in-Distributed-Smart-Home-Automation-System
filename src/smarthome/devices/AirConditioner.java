package smarthome.devices;

// Receiver: controls the air conditioner unit.
public class AirConditioner {
    private boolean on;


    // Powers the unit on and echoes the current temperature.
    public void turnOn() {
        on = true;
        System.out.println("AC is ON ");
    }

    // Powers the unit off and announces shutdown.
    public void turnOff() {
        on = false;
        System.out.println("AC is OFF");
    }

    // Indicates whether the unit is currently running.
    public boolean isOn() {
        return on;
    }


}
