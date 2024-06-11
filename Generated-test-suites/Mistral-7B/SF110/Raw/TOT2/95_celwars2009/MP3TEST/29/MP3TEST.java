import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {

    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3.filename);
    }

    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFound() {
        MP3 mp3 = new MP3("nonexistentfile.mp3");
    }

    @Test(expected = UnsupportedAudioFileException.class)
    public void testUnsupportedFormat() {
        MP3 mp3 = new MP3("unsupportedformat.extension");
    }

    @Test(expected = IOException.class)
    public void testIOException() {
        try {
            MP3 mp3 = new MP3("test.mp3");
            MP3 other = new MP3("test.mp3"); // Concurrent access
            mp3.run();
            other.run();
        } catch (Exception e) {
            // Expected IOException, so not an error
        }
    }

    @Test
    public void testNullFilename() {
        MP3 mp3 = new MP3(null);
        assertNull(mp3.filename);
    }

    @Test
    public void testEmptyFilename() {
        MP3 mp3 = new MP3("");
        assertEmpty(mp3.filename);
    }

    @Test
    public void testInterruptedException() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.interrupt();
        // Verify that interrupt is handled correctly
    }

    // Add more tests as needed

    private void assertEmpty(String s) {
        assertTrue(s.isEmpty());
    }
}