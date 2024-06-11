import org.junit.Test;
import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MP3Test {
    @Test
    public void testMP3Constructor() {
        new MP3("test.mp3");
        assertNotNull(new MP3("test.mp3").getFilename());
    }

    @Test
    public void testRun() throws InterruptedException, IOException {
        new MP3("test.mp3");
        Thread.sleep(2000);
        // Add assertions for testing the audio playback
        // This might require additional setup and teardown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFileName() {
        new MP3("");
    }

    @Test(expected = FileNotFoundException.class)
    public void testNonexistentFile() {
        new MP3("nonexistentfile.mp3");
    }

    @Test(expected = UnsupportedAudioFileException.class)
    public void testInvalidFormat() {
        try {
            new MP3("test.txt");
        } catch (Exception e) {
            throw new UnsupportedAudioFileException(e.getMessage());
        }
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testUnsupportedEncoding() {
        new MP3("test.mp3"); // Assume this constructor call is successful
        Thread thread = new Thread(() -> new MP3("test.mp3?encoding=invalid"));
        thread.start();
        thread.join();
    }

    @Test(expected = InterruptedException.class)
    public void testInterruptedDuringPlayback() throws InterruptedException {
        new MP3("test.mp3");
        Thread.currentThread().interrupt();
        Thread.sleep(100);
        new MP3("test.mp3").join();
    }
}