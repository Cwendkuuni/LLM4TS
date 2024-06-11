package Newzgrabber;
import org.junit.Test;
import static org.junit.Assert.*; // Import assertion statements

public class NewzgrabberTest {

    @Test
    public void testNewzgrabber() {
        Newzgrabber newzgrabber = new Newzgrabber();
        assertNotNull(newzgrabber);
    }
}