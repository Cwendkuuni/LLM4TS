import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {
    @Test
    public void testConstructor() {
        new MP3("test.mp3");
        // Verify if the constructor sets the filename correctly
        assertEquals("test.mp3", new MP3("test.mp3").filename);
    }
}