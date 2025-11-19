package smarthome.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MacroCommand implements Command {
    private final List<Command> commands;

    public MacroCommand(List<Command> commands) {
        this.commands = new ArrayList<>(commands);
    }

    @Override
    public void execute() {
        for (Command command : commands) {
            command.execute();
        }
    }

    @Override
    public void undo() {
        List<Command> reversed = new ArrayList<>(commands);
        Collections.reverse(reversed);
        for (Command command : reversed) {
            command.undo();
        }
    }

    public List<Command> getCommands() {
        return Collections.unmodifiableList(commands);
    }
}
