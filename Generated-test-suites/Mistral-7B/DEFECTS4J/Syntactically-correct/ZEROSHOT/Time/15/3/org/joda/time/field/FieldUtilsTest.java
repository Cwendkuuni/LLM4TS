import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        value = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeNegate(value);
        });
    }

    @Test
    public void testSafeAdd() {
        int val1 = 1;
        int val2 = 2;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals(3, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeAdd(val1, val2);
        });

        val1 = 1;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeAdd(val1, val2);
        });

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeAdd(val1, val2);
        });
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 5L;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(5L, result);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeSubtract(val1, val2);
        });

        val1 = 1L;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeSubtract(val1, val2);
        });
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 2;
        int val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeMultiply(val1, val2);
        });

        val1 = 2;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeMultiply(val1, val2);
        });

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeMultiply(val1, val2);
        });
    }

    @Test
    public void testSafeMultiplyWithIntAndLong() {
        long val1 = 2;
        int val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6L, result);
    }

    @Test
    public void testSafeMultiplyWithLongAndInt() {
        int val1 = 2;
        long val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6, result);
    }

    @Test
    public void testSafeMultiplyWithLongAndLong() {
        long val1 = 2;
        long val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6L, result);
    }

    @Test
    public void testSafeToInt() {
        long value = 10L;
        int result = FieldUtils.safeToInt(value);
        assertEquals(10, result);

        value = Long.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeToInt(value);
        });

        value = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeToInt(value);
        });
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 10L;
        long val2 = 5L;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(50, result);

        val1 = Long.MIN_VALUE;
        val2 = 5L;
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeMultiplyToInt(val1, val2);
        });
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.millis(), "test");

        int value = 5;
        FieldUtils.verifyValueBounds(field, value, 0, 5);

        value = 6;
        assertThrows(IllegalFieldValueException.class, () -> {
            FieldUtils.verifyValueBounds(field, value, 0, 5);
        });
    }

    @Test
    public void testVerifyValueBoundsWithDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.millis();

        int value = 5;
        FieldUtils.verifyValueBounds(fieldType, value, 0, 5);

        value = 6;
        assertThrows(IllegalFieldValueException.class, () -> {
            FieldUtils.verifyValueBounds(fieldType, value, 0, 5);
        });
    }

    @Test
    public void testVerifyValueBoundsWithString() {
        String fieldName = "test";

        int value = 5;
        FieldUtils.verifyValueBounds(fieldName, value, 0, 5);

        value = 6;
        assertThrows(IllegalFieldValueException.class, () -> {
            FieldUtils.verifyValueBounds(fieldName, value, 0, 5);
        });
    }

    @Test
    public void testGetWrappedValue() {
        int value = 2;
        int minValue = 0;
        int maxValue = 5;
        int result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(value, result);

        value = minValue - 1;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(maxValue - (minValue - value), result);

        value = maxValue + 1;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(minValue - (value - maxValue), result);

        value = minValue;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(minValue, result);

        value = maxValue;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(maxValue, result);

        minValue = maxValue;
        assertThrows(IllegalArgumentException.class, () -> {
            FieldUtils.getWrappedValue(value, minValue, maxValue);
        });
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj1 = obj2;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        obj2 = null;
        assertTrue(FieldUtils.equals(obj1, obj2));
    }
}