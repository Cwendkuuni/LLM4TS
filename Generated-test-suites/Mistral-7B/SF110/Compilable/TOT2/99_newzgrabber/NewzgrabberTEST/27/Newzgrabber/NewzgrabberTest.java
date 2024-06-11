package Newzgrabber;
import org.junit.Test;
import static org.junit.Assert.*;

public class NewzgrabberTest {
    @Test
    public void testNewzgrabber() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        assertNotNull(newzgrabber);
    }
}