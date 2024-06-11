import org.junit.Test;
import org.joda.time.DateTimeFieldType;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(-5, negatedValue);

        value = -5;
        negatedValue = FieldUtils.safeNegate(value);
        assertEquals(5, negatedValue);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 1;
        int val2 = 2;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(3, sum);

        val1 = Integer.MAX_VALUE;
        val2 = 1;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(Integer.MAX_VALUE, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(Integer.MIN_VALUE - 1, sum);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 5;
        long difference = FieldUtils.safeSubtract(val1, val2);
        assertEquals(5, difference);

        val1 = 5;
        val2 = 10;
        difference = FieldUtils.safeSubtract(val1, val2);
        assertEquals(-5, difference);

        val1 = 1L << 63; // Long.MIN_VALUE plus one bit
        val2 = 1;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 3;
        int val2 = 4;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(12, product);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Integer.MAX_VALUE;
        val2 = Integer.MAX_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAddToInt() {
        long val1 = 3;
        long val2 = 4;
        int sum = FieldUtils.safeMultiplyToInt((int) val1, (int) val2);
        assertEquals(12, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiplyToInt((int) val1, (int) val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Integer.MAX_VALUE;
        val2 = Integer.MAX_VALUE;
        try {
            FieldUtils.safeMultiplyToInt((int) val1, (int) val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        try {
            FieldUtils.verifyValueBounds(fieldType, 7, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 1, 1, 7);
            // expected success
        } catch (IllegalFieldValueException e) {
            fail("Unexpected IllegalFieldValueException");
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 15;
        int minValue = 0;
        int maxValue = 7;
        int wrappedValue = FieldUtils.getWrappedValue(value, 3, minValue, maxValue);
        assertEquals(2, wrappedValue);

        value = -12;
        wrappedValue = FieldUtils.getWrappedValue(value, 5, minValue, maxValue);
        assertEquals(2, wrappedValue);

        value = 13;
        wrappedValue = FieldUtils.getWrappedValue(value, 3, minValue, maxValue);
        assertEquals(3, wrappedValue);

        value = 2;
        wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
        assertEquals(1, wrappedValue);

        value = Integer.MIN_VALUE;
        minValue = Integer.MIN_VALUE;
        maxValue = Integer.MIN_VALUE + 1;
        try {
            FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}