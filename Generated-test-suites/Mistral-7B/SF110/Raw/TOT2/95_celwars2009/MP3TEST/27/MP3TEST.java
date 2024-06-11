import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidFilename() {
        new MP3(null);
    }

    @Test
    public void testConstructorValidFilename() {
        new MP3("test.mp3");
        // Add additional assertions if necessary
    }
}