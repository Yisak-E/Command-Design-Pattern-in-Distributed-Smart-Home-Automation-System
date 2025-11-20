package smarthome.commands;

// Null Object command: avoids null checks for unassigned slots.
public final class NoCommand implements Command {
    @Override
    public void execute() {
        // intentional no-op to simplify invoker defaults
    }

    @Override
    public void undo() {
        // intentional no-op to simplify invoker defaults
    }
}
