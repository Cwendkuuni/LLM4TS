import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {

    @Test
    public void testMP3Constructor() {
        String filename = "example.mp3";
        MP3 mp3 = new MP3(filename);
        assertNotNull(mp3);
    }
}