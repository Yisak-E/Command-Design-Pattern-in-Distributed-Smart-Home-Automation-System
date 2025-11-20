package smarthome.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//  groups multiple commands into a reusable routine.
public class GeneralCommand implements Command {
    private final List<Command> commands;

    // Captures a defensive copy of the provided command list.
    public GeneralCommand(List<Command> commands) {
        this.commands = new ArrayList<>(commands);
    }

    // Executes each child command in insertion order.
    @Override
    public void execute() {
        for (Command command : commands) {
            command.execute();
        }
    }

    // Undoes each child command in reverse order for safety.
    @Override
    public void undo() {
        List<Command> reversed = new ArrayList<>(commands);
        Collections.reverse(reversed);
        for (Command command : reversed) {
            command.undo();
        }
    }

    // Exposes an immutable view of the macro's contents.
    public List<Command> getCommands() {
        return Collections.unmodifiableList(commands);
    }
}
