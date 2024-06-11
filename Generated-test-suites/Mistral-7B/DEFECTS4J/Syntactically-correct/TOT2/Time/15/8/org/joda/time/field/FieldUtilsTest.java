import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Test normal case
        int input = 5;
        int expectedOutput = -5;
        int output = FieldUtils.safeNegate(input);
        assertEquals(expectedOutput, output);

        // Test edge case: minimum integer value
        input = Integer.MIN_VALUE;
        expectedOutput = -Integer.MIN_VALUE;
        output = FieldUtils.safeNegate(input);
        assertEquals(expectedOutput, output);

        // Test error case: negative input
        input = -1;
        try {
            output = FieldUtils.safeNegate(input);
        } catch (ArithmeticException e) {
            assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
            return;
        }
        fail("Expected ArithmeticException");
    }
}