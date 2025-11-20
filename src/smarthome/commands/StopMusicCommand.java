package smarthome.commands;

import smarthome.devices.MusicPlayer;

// Concrete command: stops playback on the music player.
public class StopMusicCommand implements Command {
    private final MusicPlayer musicPlayer;

    // Creates a command bound to the provided music player.
    public StopMusicCommand(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    // Executes the stop action on the receiver.
    @Override
    public void execute() {
        musicPlayer.stop();
    }

    // Reverts the action by resuming playback.
    @Override
    public void undo() {
        musicPlayer.play();
    }
}
