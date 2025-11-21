package smarthome.commands;

import smarthome.receiver.MusicPlayer;

// Concrete command: starts playback on the music player.
public class PlayMusicCommand implements Command {
    private final MusicPlayer musicPlayer;

    // Creates a command bound to the provided music player.
    public PlayMusicCommand(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    // Executes the play action on the receiver.
    @Override
    public void execute() {
        musicPlayer.play();
    }

    // Reverts the action by stopping playback.
    @Override
    public void undo() {
        musicPlayer.stop();
    }
}
