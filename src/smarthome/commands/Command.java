package smarthome.commands;

// Command interface: abstraction for device actions with reversible behaviour.
public interface Command {
    // Performs the primary action.
    void execute();

    // Reverts the previously executed action.
    void undo();
}
