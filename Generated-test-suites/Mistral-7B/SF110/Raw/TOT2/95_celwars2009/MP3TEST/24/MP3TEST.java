import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import javax.sound.sampled.*;

public class MP3Test {
    private MP3 mp3;

    @Before
    public void setUp() {
        mp3 = new MP3("");
    }

    @Test
    public void testMP3Constructor() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        assertNotNull(mp3.getFilename());
        assertEquals(filename, mp3.getFilename());
    }

    @Test
    public void testEmptyFilename() {
        assertThrows(IllegalArgumentException.class, () -> new MP3(""));
    }

    @Test
    public void testNullFilename() {
        assertThrows(NullPointerException.class, () -> new MP3(null));
    }

    @Test
    public void testRun() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.run();
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFound() {
        String filename = "nonexistentfile.mp3";
        MP3 mp3 = new MP3(filename);
    }

    @Test(expected = UnsupportedAudioFileException.class)
    public void testIncorrectFileExtension() {
        String filename = "test.txt";
        MP3 mp3 = new MP3(filename);
    }

    @Test(expected = IOException.class)
    public void testInputOutputExceptions() throws IOException {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.run();
        File file = new File(filename);
        assertFalse(file.exists()); // Assuming the MP3 instance doesn't delete the file after playing
    }

    @Test(expected = UnsupportedAudioFileException.class)
    public void testFormatConversionExceptions() throws IOException {
        String filename = "test.wav";
        MP3 mp3 = new MP3(filename);
    }
}