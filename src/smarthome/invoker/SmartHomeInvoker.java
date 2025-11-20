package smarthome.invoker;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import smarthome.commands.Command;
import smarthome.commands.NoCommand;

// Invoker: stores command mappings and manages undo/redo history.
public class SmartHomeInvoker {
    private final Map<String, Command> commandRegistry = new HashMap<>();
    private final Deque<Command> undoHistory = new ArrayDeque<>();
    private final Deque<Command> redoHistory = new ArrayDeque<>();
    private Command defaultCommand = new NoCommand();

    // Changes the default fallback command used for missing keys.
    public void setDefaultCommand(Command defaultCommand) {
        this.defaultCommand = Objects.requireNonNull(defaultCommand);
    }

    // Registers a command under a string key.
    public void registerCommand(String key, Command command) {
        commandRegistry.put(key, Objects.requireNonNull(command));
    }

    // Retrieves a previously registered command if present.
    public Optional<Command> getRegisteredCommand(String key) {
        return Optional.ofNullable(commandRegistry.get(key));
    }

    // Executes a command identified by key and records it in history.
    public void execute(String key) {
        Command command = commandRegistry.getOrDefault(key, defaultCommand);
        execute(command);
    }

    // Executes a command instance and updates undo/redo stacks.
    public void execute(Command command) {
        Objects.requireNonNull(command).execute();
        undoHistory.push(command);
        redoHistory.clear();
    }

    // Steps back one command if available.
    public boolean undo() {
        if (undoHistory.isEmpty()) {
            return false;
        }
        Command last = undoHistory.pop();
        last.undo();
        redoHistory.push(last);
        return true;
    }

    // Reapplies the most recently undone command.
    public boolean redo() {
        if (redoHistory.isEmpty()) {
            return false;
        }
        Command command = redoHistory.pop();
        command.execute();
        undoHistory.push(command);
        return true;
    }

    // Clears both history stacks.
    public void clearHistory() {
        undoHistory.clear();
        redoHistory.clear();
    }
}
