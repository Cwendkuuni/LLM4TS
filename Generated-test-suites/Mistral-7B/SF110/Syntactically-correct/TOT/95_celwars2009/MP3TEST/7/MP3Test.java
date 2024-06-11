import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {
    @Test
    public void testMP3Construction() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        assertNotNull(mp3.getFilename());
        assertEquals(filename, mp3.getFilename());
        assertNull(mp3.in);
        assertNull(mp3.din);
        mp3.join();
    }

    @Test(expected = NullPointerException.class)
    public void testMP3ConstructionWithNullFilename() {
        MP3 mp3 = new MP3(null);
    }
}