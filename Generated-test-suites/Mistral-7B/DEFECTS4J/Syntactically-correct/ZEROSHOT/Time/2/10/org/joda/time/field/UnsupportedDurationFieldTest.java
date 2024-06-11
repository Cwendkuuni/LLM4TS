import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        assertNotNull(unsupportedField);
        assertEquals(type, unsupportedField.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1000;
        unsupportedField.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLong() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1000;
        unsupportedField.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1000;
        long instant = 123456789;
        unsupportedField.getValue(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1000;
        long instant = 123456789;
        unsupportedField.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        int value = 100;
        unsupportedField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long value = 1000;
        unsupportedField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        int value = 100;
        long instant = 123456789;
        unsupportedField.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long value = 1000;
        long instant = 123456789;
        unsupportedField.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = 123456789;
        int value = 100;
        unsupportedField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = 123456789;
        long value = 1000;
        unsupportedField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 123456789;
        long subtrahendInstant = 123456788;
        unsupportedField.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 123456789;
        long subtrahendInstant = 123456788;
        unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long unitMillis = unsupportedField.getUnitMillis();
        assertEquals(0, unitMillis);
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.MILLISECONDS;
        DurationFieldType type2 = DurationFieldType.SECONDS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type2);
        int comparison = unsupportedField1.compareTo(unsupportedField2);
        assertEquals(0, comparison);
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        int hashCode = unsupportedField.hashCode();
        assertEquals(-1469763671, hashCode);
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        String toString = unsupportedField.toString();
        assertEquals("UnsupportedDurationField[MILLISECONDS]", toString);
    }

    @Test
    public void testEquals() {
        DurationFieldType type1 = DurationFieldType.MILLISECONDS;
        DurationFieldType type2 = DurationFieldType.SECONDS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type2);
        boolean equal = unsupportedField1.equals(unsupportedField2);
        assertEquals(false, equal);

        UnsupportedDurationField unsupportedField3 = UnsupportedDurationField.getInstance(type1);
        boolean equal2 = unsupportedField1.equals(unsupportedField3);
        assertEquals(true, equal2);
    }
}