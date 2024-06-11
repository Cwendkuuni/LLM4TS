import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        boolean expression = true;
        Validate.isTrue(expression);
        assertTrue(true);

        expression = false;
        try {
            Validate.isTrue(expression);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}