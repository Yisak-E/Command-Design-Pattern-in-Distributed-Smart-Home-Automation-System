package smarthome.invoker;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import smarthome.commands.Command;
import smarthome.commands.NoCommand;

public class SmartHomeInvoker {
    private final Map<String, Command> commandRegistry = new HashMap<>();
    private final Deque<Command> undoHistory = new ArrayDeque<>();
    private final Deque<Command> redoHistory = new ArrayDeque<>();
    private Command defaultCommand = new NoCommand();

    public void setDefaultCommand(Command defaultCommand) {
        this.defaultCommand = Objects.requireNonNull(defaultCommand);
    }

    public void registerCommand(String key, Command command) {
        commandRegistry.put(key, Objects.requireNonNull(command));
    }

    public Optional<Command> getRegisteredCommand(String key) {
        return Optional.ofNullable(commandRegistry.get(key));
    }

    public void execute(String key) {
        Command command = commandRegistry.getOrDefault(key, defaultCommand);
        execute(command);
    }

    public void execute(Command command) {
        Objects.requireNonNull(command).execute();
        undoHistory.push(command);
        redoHistory.clear();
    }

    public boolean undo() {
        if (undoHistory.isEmpty()) {
            return false;
        }
        Command last = undoHistory.pop();
        last.undo();
        redoHistory.push(last);
        return true;
    }

    public boolean redo() {
        if (redoHistory.isEmpty()) {
            return false;
        }
        Command command = redoHistory.pop();
        command.execute();
        undoHistory.push(command);
        return true;
    }

    public void clearHistory() {
        undoHistory.clear();
        redoHistory.clear();
    }
}
