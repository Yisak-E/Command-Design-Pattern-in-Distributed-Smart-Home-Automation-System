package smarthome.commands;

public interface Command {
    void execute();

    void undo();
}
