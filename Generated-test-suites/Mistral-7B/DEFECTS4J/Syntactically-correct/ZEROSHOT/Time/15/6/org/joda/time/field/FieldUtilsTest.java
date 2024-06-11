import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 10;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-10, result);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, result);

        long lVal1 = Long.MAX_VALUE;
        long lVal2 = 1;
        try {
            FieldUtils.safeAdd((int) lVal1, (int) lVal2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        lVal1 = Long.MIN_VALUE;
        lVal2 = 1;
        try {
            FieldUtils.safeAdd((int) lVal1, (int) lVal2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 5;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(5, result);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, result);

        int val1_min = Integer.MIN_VALUE;
        int val2 = 2;
        try {
            FieldUtils.safeMultiply(val1_min, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        long val1_long = Long.MIN_VALUE;
        int val2 = 2;
        try {
            FieldUtils.safeMultiply((int) val1_long, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        long val1_long = Long.MIN_VALUE;
        long val2_long = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1_long, val2_long);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5;
        int val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, result);

        long val1 = 5;
        int val2 = -1;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(-5L, result);

        long val1 = 5;
        int val2 = 0;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        long val1 = 0;
        int val2 = 3;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        long val1 = Long.MIN_VALUE;
        int val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5;
        long val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, result);

        long val1 = Long.MIN_VALUE;
        long val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        long val1 = Long.MIN_VALUE;
        long val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeToInt() {
        long value = Long.MAX_VALUE;
        int result = FieldUtils.safeToInt((int) value);
        assertEquals(Integer.MAX_VALUE, result);

        value = Long.MIN_VALUE;
        try {
            FieldUtils.safeToInt((int) value);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5;
        long val2 = 3;
        int result = FieldUtils.safeMultiplyToInt((int) val1, (int) val2);
        assertEquals(15, result);

        long val1 = Long.MIN_VALUE;
        long val2 = 2;
        try {
            FieldUtils.safeMultiplyToInt((int) val1, (int) val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth", DateTimeFields.standard());

        int value = 31;
        FieldUtils.verifyValueBounds(field, value, 1, 31);

        value = 0;
        try {
            FieldUtils.verifyValueBounds(field, value, 1, 31);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        value = 32;
        try {
            FieldUtils.verifyValueBounds(field, value, 1, 31);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        FieldUtils.verifyValueBounds(fieldType, value, 1, 7);

        String fieldName = "dayOfMonth";
        FieldUtils.verifyValueBounds(fieldName, value, 1, 31);

        try {
            FieldUtils.verifyValueBounds(fieldName, value, 1, 30);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        int currentValue = 5;
        int wrapValue = 2;
        int minValue = 1;
        int maxValue = 5;
        int result = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(3, result);

        currentValue = 1;
        result = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(1, result);

        currentValue = 6;
        result = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(3, result);

        currentValue = 0;
        result = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(1, result);

        currentValue = Integer.MIN_VALUE;
        result = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(minValue, result);

        currentValue = Integer.MAX_VALUE;
        result = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(maxValue, result);

        minValue = maxValue;
        try {
            FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        minValue = maxValue - 1;
        result = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(minValue, result);
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj2 = obj1;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        assertFalse(FieldUtils.equals(obj1, obj2));
    }
}