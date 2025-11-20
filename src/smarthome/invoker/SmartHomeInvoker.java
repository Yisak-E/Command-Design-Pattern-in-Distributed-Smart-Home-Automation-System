package smarthome.invoker;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import smarthome.commands.Command;

// Invoker: stores command mappings and manages undo/redo history.
public class SmartHomeInvoker {
    private final Map<String, Command> commandRegistry = new HashMap<>();
    private final Deque<Command> undoHistory = new ArrayDeque<>();
    private final Deque<Command> redoHistory = new ArrayDeque<>();

    //set default command
    public void setDafaultCommand(Command defCommand) {
        commandRegistry.put("default", Objects.requireNonNull(defCommand));
    }

    // Registers a command under a string key.
    public void registerCommand(String key, Command command) {
        commandRegistry.put(key, Objects.requireNonNull(command));
    }


    // Executes a command identified by key and records it in history.
    public void execute(String key) {
        Command command = commandRegistry.get(key);
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
