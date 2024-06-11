import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {
    @Test
    public void testMP3Constructor() {
        new MP3("test.mp3");
        // Check if the constructor initializes the filename correctly
        assertEquals("test.mp3", new MP3("test.mp3").filename);
    }
}