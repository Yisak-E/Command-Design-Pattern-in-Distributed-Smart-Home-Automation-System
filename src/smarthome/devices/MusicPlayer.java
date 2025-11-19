package smarthome.devices;

public class MusicPlayer {
    private boolean playing;

    public void play() {
        playing = true;
        System.out.println("Music is Playing");
    }

    public void stop() {
        playing = false;
        System.out.println("Music Stopped");
    }

    public boolean isPlaying() {
        return playing;
    }
}
