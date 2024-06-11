import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import org.junit.runner.RunWith;
import org.junit.jupitersuite.runners.JUnit4;

@RunWith(JUnit4.class)
public class MP3Test {
    @Test
    public void testMP3Constructor() throws Exception {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        assertNotNull(mp3.in);
        assertNotNull(mp3.din);
        assertNotNull(mp3.filename);
        assertEquals(filename, mp3.filename);
    }

    @Test
    public void testMP3ConstructorWithNullFilename() {
        MP3 mp3 = new MP3(null);
        assertNull(mp3.filename);
    }

    @Test
    public void testMP3ConstructorWithEmptyFilename() {
        MP3 mp3 = new MP3("");
        assertNotNull(mp3.filename);
        assertEquals("", mp3.filename);
    }

    @Test
    public void testMP3ConstructorWithInvalidFilePath() {
        String invalidFilePath = "invalid_path/test.mp3";
        MP3 mp3 = new MP3(invalidFilePath);
        assertNull(mp3.in);
        assertNull(mp3.din);
        assertNull(mp3.filename);
    }

    @Test
    public void testRunMethod() throws Exception {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.run();
    }

    @Test(expected = Exception.class)
    public void testRunMethodWithThrowsException() throws Exception {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        throw new Exception("Test exception");
    }

    @Test
    public void testRunMethodWithEmptyInputStream() throws Exception {
        String filename = "test.mp3";
        File emptyFile = new File("test_empty.mp3");
        MP3 mp3 = new MP3(filename);
        AudioInputStream in = AudioSystem.getAudioInputStream(emptyFile);
        mp3.run();
    }

    @Test
    public void testRunMethodWithNullInputStream() throws Exception {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.run();
        mp3.in = null;
        mp3.run();
    }

    @Test
    public void testRunMethodWithIOException() throws Exception {
        String filename = "test.mp3";
        File file = new File("test.mp3");
        MP3 mp3 = new MP3(filename);
        FileInputStream inputStream = new FileInputStream(file);
        try {
            mp3.run();
        } finally {
            inputStream.close();
        }

        try {
            mp3.run();
        } catch (IOException ex) {
            // Expected
        }
    }
}