import static org.junit.Assert.*;
import org.joda.time.field.FieldUtils;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Test valid integer value
        int input = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(input);
        assertEquals("safeNegate failed for valid integer", expected, result);

        // Test Integer.MIN_VALUE
        int minValue = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(minValue);
            fail("Expected ArithmeticException when negating Integer.MIN_VALUE");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }
}