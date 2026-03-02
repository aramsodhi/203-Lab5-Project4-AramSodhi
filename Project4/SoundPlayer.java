import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    public static void playMusic(String audioPath) {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(audioPath));
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.loop(0);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio file");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO exception");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.out.println("Line unavailable");
        }

    }
}
