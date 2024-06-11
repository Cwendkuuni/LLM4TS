import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {
    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertTrue(mp3.filename.equals("test.mp3"));
    }
}