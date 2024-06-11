import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {

    @Test
    public void testMP3Constructor() throws IOException {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        assertNotNull(mp3.in);
        assertNotNull(mp3.din);
        assertNotNull(mp3.filename);
        assertEquals(filename, mp3.filename);
    }
}