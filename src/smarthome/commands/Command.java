package smarthome.commands;

// Command interface: abstraction for device actions with reversible behaviour.
public interface Command {
    void execute();

    void undo();
}
