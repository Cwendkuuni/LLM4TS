package Newzgrabber;
import org.junit.Test;
import static org.junit.Assert.*;

public class NewzgrabberTest {

    @Test
    public void testNewzgrabberConstructor() {
        Newzgrabber newzgrabber = new Newzgrabber();
        assertNotNull(newzgrabber);
        assertEquals(false, Newzgrabber.IsBatch);
        assertEquals("Newzgrabber", newzgrabber.getTitle());
        // Add other assertions for default values
    }
}