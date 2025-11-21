package smarthome.receiver;

// Receiver: controls the air conditioner unit.
public class AirConditioner {
    private boolean on;
    private int temperature = 24;

    // Powers the unit on and echoes the current temperature.
    public void turnOn() {
        on = true;
        System.out.println("AC is ON at " + temperature + "°C");
    }

    // Powers the unit off and announces shutdown.
    public void turnOff() {
        on = false;
        System.out.println("AC is OFF");
    }

    // Updates the target temperature and reports if active.
    public void setTemperature(int temperature) {
        this.temperature = temperature;
        if (on) {
            System.out.println("AC temperature adjusted to " + temperature + "°C");
        }
    }

    // Indicates whether the unit is currently running.
    public boolean isOn() {
        return on;
    }

    // Returns the last requested temperature.
    public int getTemperature() {
        return temperature;
    }
}
