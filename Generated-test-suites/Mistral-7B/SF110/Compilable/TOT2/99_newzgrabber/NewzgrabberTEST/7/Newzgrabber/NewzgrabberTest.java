package Newzgrabber;
import org.junit.Test;
import static org.junit.Assert.*;

public class NewzgrabberTest {

    @Test
    public void testNewzgrabber() {
        Newzgrabber n = new Newzgrabber();
        assertNotNull(n);
        
        Newzgrabber anotherInstance = new Newzgrabber();
        assertNotSame(n, anotherInstance);
        
    }

    // Add more tests for other methods here
}