import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Basic test
        int input = 5;
        int expectedOutput = -5;
        int output = FieldUtils.safeNegate(input);
        assertEquals("safeNegate: basic test", expectedOutput, output);

        // Edge case: minimum integer value
        input = Integer.MIN_VALUE;
        try {
            output = FieldUtils.safeNegate(input);
            fail("safeNegate: expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Error case: null input
        try {
            FieldUtils.safeNegate(null);
            fail("safeNegate: expected ArithmeticException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        // Basic test
        int val1 = 3;
        int val2 = 2;
        int expectedOutput = 5;
        int output = FieldUtils.safeAdd(val1, val2);
        assertEquals("safeAdd: basic test", expectedOutput, output);

        // Edge case: overflow
        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            output = FieldUtils.safeAdd(val1, val2);
            fail("safeAdd: expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Error case: null input
        try {
            FieldUtils.safeAdd(null, val2);
            fail("safeAdd: expected ArithmeticException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Other test methods for other methods in the FieldUtils class can be added here...

    

    
}