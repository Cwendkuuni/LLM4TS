package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertSame;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(type);
        assertSame(unsupported, other);
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("instant seconds", unsupported.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertFalse(unsupported.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertTrue(unsupported.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 123456L;
        unsupported.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 123456L;
        unsupported.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1234567890123L;
        unsupported.getValue(instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1234567890123L;
        unsupported.getValueAsLong(instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int value = 123;
        unsupported.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1234567890123L;
        unsupported.getMillis(instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1234567890123L;
        unsupported.add(instant, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1234567890123L;
        unsupported.add(instant, 123456L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1234567890123L;
        long subtrahendInstant = 1234567890122L;
        unsupported.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1234567890123L;
        long subtrahendInstant = 1234567890122L;
        unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType typeA = DurationFieldType.INSTANT_SECONDS;
        DurationFieldType typeB = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedA = UnsupportedDurationField.getInstance(typeA);
        UnsupportedDurationField unsupportedB = UnsupportedDurationField.getInstance(typeB);
        assertEquals(0, unsupportedA.compareTo(unsupportedB));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupportedA = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedB = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedC = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(unsupportedA, unsupportedA);
        assertEquals(unsupportedA, unsupportedB);
        assertNotSame(unsupportedA, unsupportedC);
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(type.hashCode(), unsupported.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[instant seconds]", unsupported.toString());
    }

    @Test
    public void testSingletonSerialization() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertNotNull(unsupported.readResolve());
    }

    @Test
    public void testUnsupportedOperationException() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(1));
    }

}