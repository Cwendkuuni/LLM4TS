import org.junit.Test;
import org.joda.time.field.FieldUtils;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Basic test
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        // Edge case: minimum int value
        int minValue = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(minValue);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Error scenario: negative value
        int negativeValue = -5;
        int errorResult = FieldUtils.safeNegate(negativeValue);
        assertEquals(5, errorResult);
    }

    // ... Similar test cases for all other methods in FieldUtils

    // Test start
    @Test
    public void testFieldUtils() {
        // Test all methods here
    }

    // Test end
}