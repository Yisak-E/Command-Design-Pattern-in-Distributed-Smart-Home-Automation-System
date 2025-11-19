package smarthome.commands;

import smarthome.devices.MusicPlayer;

public class StopMusicCommand implements Command {
    private final MusicPlayer musicPlayer;

    public StopMusicCommand(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    @Override
    public void execute() {
        musicPlayer.stop();
    }

    @Override
    public void undo() {
        musicPlayer.play();
    }
}
