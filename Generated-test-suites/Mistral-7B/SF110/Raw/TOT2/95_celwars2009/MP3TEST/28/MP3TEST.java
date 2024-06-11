import org.junit.Test;
import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import javax.sound.sampled.*;

public class MP3Test {

    @Test
    public void testMP3Constructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3.in);
        assertNotNull(mp3.din);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMP3ConstructorNullFilename() {
        new MP3(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMP3ConstructorEmptyFilename() {
        new MP3("");
    }

    @Test(expected = FileNotFoundException.class)
    public void testMP3ConstructorNonExistentFile() {
        new MP3("nonExistentFile.mp3");
    }

    @Test(expected = IOException.class)
    public void testRunWithIOException() {
        MP3 mp3 = new MP3("test.mp3");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outputStream));
        mp3.run();
        String errorMessage = outputStream.toString();
        System.setErr(System.out);
        // The error message should include "IOException"
        assertTrue(errorMessage.contains("IOException"));
    }

    @Test(expected = NullPointerException.class)
    public void testRunWithNullDin() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.din = null;
        mp3.run();
    }

    @Test(expected = NullPointerException.class)
    public void testRunWithNullLine() {
        MP3 mp3 = new MP3("test.mp3");
        SourceDataLine line = null;
        mp3.run();
    }

    @Test(expected = UnsupportedAudioFileException.class)
    public void testRunWithUnsupportedFormat() {
        MP3 mp3 = new MP3("test.txt");
        mp3.run();
    }

    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
    }
}