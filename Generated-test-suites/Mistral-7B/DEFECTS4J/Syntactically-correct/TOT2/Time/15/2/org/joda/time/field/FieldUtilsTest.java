import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {
    // Test for safeNegate method
    @Test
    public void testSafeNegate() {
        int value = 5;
        int expected = -5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(expected, negatedValue);

        value = 0;
        expected = 0;
        negatedValue = FieldUtils.safeNegate(value);
        assertEquals(expected, negatedValue);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Test for safeAdd method
    @Test
    public void testSafeAdd() {
        int val1 = 1;
        int val2 = 2;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(sum, val1 + val2);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Test for safeMultiply method
    @Test
    public void testSafeMultiply() {
        int val1 = 2;
        int val2 = 3;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(product, val1 * val2);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Test for verifyValueBounds method
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsException() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        FieldUtils.verifyValueBounds(fieldType, 8, DateTimeField.values().length, DateTimeField.values().length);
    }

    @Test
    public void testVerifyValueBoundsValid() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        FieldUtils.verifyValueBounds(fieldType, 5, DateTimeField.values().length - 2, DateTimeField.values().length - 1);
    }
}


// Test cases for all methods in FieldUtils class
// ...
// Test for edge cases and exception handling scenarios
// ...
// Complete JUnit test file for FieldUtils class
