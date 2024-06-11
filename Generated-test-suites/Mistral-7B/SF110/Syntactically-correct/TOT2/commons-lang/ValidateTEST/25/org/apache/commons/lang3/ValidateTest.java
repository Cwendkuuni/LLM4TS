import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        assertTrue(Validate.isTrue(true));
        assertFalse(Validate.isTrue(false));
        try {
            Validate.isTrue(false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}