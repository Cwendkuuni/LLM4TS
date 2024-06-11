import org.junit.Test;
import org.joda.time.DateTimeFieldType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));
    }

    @Test
    public void testSafeAdd() {
        int val1 = 1;
        int val2 = 2;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(3, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));

        val1 = 1;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 5L;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(5L, diff);

        val1 = 5L;
        val2 = 10L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));

        val1 = 1L;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 2;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(10, product);

        val1 = 1;
        val2 = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = Integer.MAX_VALUE;
        val2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = Integer.MAX_VALUE;
        val2 = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeMultiply_IntLong() {
        int val1 = 5;
        long val2 = 2;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(10L, product);
    }

    @Test
    public void testSafeMultiply_LongInt() {
        long val1 = 5L;
        int val2 = 2;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(10L, product);
    }

    @Test
    public void testSafeMultiply_LongLong() {
        long val1 = 5L;
        long val2 = 2L;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(10L, product);
    }

    @Test
    public void testSafeToInt() {
        long value = Long.MAX_VALUE;
        int intValue = FieldUtils.safeToInt(value);
        assertEquals(Integer.MAX_VALUE, intValue);

        value = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(value));
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L;
        long val2 = 2L;
        int product = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(10, product);
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth");
        int value = 31;
        int lowerBound = 1;
        int upperBound = 30;

        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        value = 32;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));

        value = -1;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));
    }

    @Test
    public void testGetWrappedValue() {
        int value = 6;
        int minValue = 1;
        int maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, wrappedValue);

        value = 0;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, wrappedValue);

        value = 5;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(5, wrappedValue);

        value = 7;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(2, wrappedValue);

        value = 6;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, wrappedValue);

        minValue = 5;
        maxValue = 5;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, minValue, maxValue));
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1;
        Object obj4 = null;

        assertEquals(obj1, obj1);
        assertEquals(obj1, obj3);
        assertNotEquals(obj1, obj2);
        assertNotEquals(obj1, obj4);
    }
}