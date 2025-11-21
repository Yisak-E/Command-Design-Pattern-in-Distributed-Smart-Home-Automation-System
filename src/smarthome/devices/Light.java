package smarthome.devices;

// Receiver: models a simple light fixture.
public class Light {
    private boolean on;

    // Powers the light on and reports status.
    public void turnOn() {
        on = true;
        System.out.println("Light is ON");
    }

    // Powers the light off and reports status.
    public void turnOff() {
        on = false;
        System.out.println("Light is OFF");
    }

    // Indicates whether the light is currently on.
    public boolean isOn() {
        return on;
    }
}
