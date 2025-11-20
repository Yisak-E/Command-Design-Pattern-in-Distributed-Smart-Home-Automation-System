package smarthome.commands;

// Null Object command: avoids null checks for unassigned slots.
public final class NoCommand implements Command {
    // No-op execution preserves invoker flow.
    @Override
    public void execute() {
        // intentional no-op to simplify invoker defaults
    }

    // No-op undo keeps history semantics consistent.
    @Override
    public void undo() {
        // intentional no-op to simplify invoker defaults
    }
}
