package smarthome.devices;

// Receiver: drives audio playback.
public class MusicPlayer {
    private boolean playing;

    // Starts playback and reports status.
    public void play() {
        playing = true;
        System.out.println("Music is Playing");
    }

    // Stops playback and reports status.
    public void stop() {
        playing = false;
        System.out.println("Music Stopped");
    }

    // Indicates whether music is currently playing.
    public boolean isPlaying() {
        return playing;
    }
}
