package smarthome.devices;

public class AirConditioner {
    private boolean on;
    private int temperature = 24;

    public void turnOn() {
        on = true;
        System.out.println("AC is ON at " + temperature + "°C");
    }

    public void turnOff() {
        on = false;
        System.out.println("AC is OFF");
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        if (on) {
            System.out.println("AC temperature adjusted to " + temperature + "°C");
        }
    }

    public boolean isOn() {
        return on;
    }

    public int getTemperature() {
        return temperature;
    }
}
