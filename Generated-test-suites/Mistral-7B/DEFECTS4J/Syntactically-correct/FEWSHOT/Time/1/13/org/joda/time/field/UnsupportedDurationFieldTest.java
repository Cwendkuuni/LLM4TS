package org.joda.time.field;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertSame(field1, field2);
    }

    @Test
    public void testGetInstanceNullType() {
        assertThrows(NullPointerException.class, () -> UnsupportedDurationField.getInstance(null));
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertTrue(field.isPrecise());
    }

    @Test
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long duration = 1234567890L;
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(duration));
    }

    @Test
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long duration = 1234567890L;
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(duration));
    }

    @Test
    public void testGetValueNullDuration() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(NullPointerException.class, () -> field.getValue(null));
    }

    @Test
    public void testGetValueNullInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long duration = 1234567890L;
        assertThrows(NullPointerException.class, () -> field.getValue(duration, null));
    }

    @Test
    public void testGetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(0));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1234567890L));
        assertThrows(NullPointerException.class, () -> field.getMillis(0, null));
        assertThrows(NullPointerException.class, () -> field.getMillis(1234567890L, null));
    }

    @Test
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long instant = 1234567890L;
        int value = 42;
        assertThrows(UnsupportedOperationException.class, () -> field.add(instant, value));
    }

    @Test
    public void testAddLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long instant = 1234567890L;
        long value = 42L;
        assertThrows(UnsupportedOperationException.class, () -> field.add(instant, value));
    }

    @Test
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long minuendInstant = 1234567890L;
        long subtrahendInstant = 987654321L;
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(minuendInstant, subtrahendInstant));
    }

    @Test
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long minuendInstant = 1234567890L;
        long subtrahendInstant = 987654321L;
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(minuendInstant, subtrahendInstant));
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        int comparison = field1.compareTo(field2);
        assertEquals(1, comparison);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertTrue(field1.equals(field2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        int hashCode = field.hashCode();
        assertEquals(hashCode, field.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        String string = field.toString();
        assertTrue(string.startsWith("UnsupportedDurationField["));
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertSame(field, field.readResolve());
    }

    @Test
    public void testUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, field::unsupported);
    }
}