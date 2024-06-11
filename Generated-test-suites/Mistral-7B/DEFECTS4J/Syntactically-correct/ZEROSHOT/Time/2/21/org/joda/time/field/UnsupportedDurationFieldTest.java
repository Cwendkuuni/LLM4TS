package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

/**
 * Test class for UnsupportedDurationField.
 *
 * @author John Doe
 */
public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        assertEquals(durationFieldType, unsupportedDurationField.getType());
        assertEquals(durationFieldType.getName(), unsupportedDurationField.getName());
        assertFalse(unsupportedDurationField.isSupported());
        assertTrue(unsupportedDurationField.isPrecise());
    }

    @Test
    public void testGetInstanceSameThread() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(durationFieldType);
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(durationFieldType);
        assertSame(unsupportedDurationField1, unsupportedDurationField2);
    }

    @Test
    public void testGetInstanceDifferentThreads() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(durationFieldType);
        UnsupportedDurationField unsupportedDurationField2 = null;
        Thread thread = new Thread(() -> unsupportedDurationField2 = UnsupportedDurationField.getInstance(durationFieldType));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertSame(unsupportedDurationField1, unsupportedDurationField2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        long duration = 1;
        unsupportedDurationField.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        long duration = 1;
        unsupportedDurationField.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        long instant = 1;
        unsupportedDurationField.getValue(instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        long instant = 1;
        unsupportedDurationField.getValueAsLong(instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        int value = 1;
        unsupportedDurationField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        long value = 1;
        unsupportedDurationField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueInstant() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        int value = 1;
        long instant = 1;
        unsupportedDurationField.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueInstant() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        long value = 1;
        long instant = 1;
        unsupportedDurationField.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        long instant = 1;
        int value = 1;
        unsupportedDurationField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        long instant = 1;
        long value = 1;
        unsupportedDurationField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        long minuendInstant = 1;
        long subtrahendInstant = 1;
        unsupportedDurationField.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        long minuendInstant = 1;
        long subtrahendInstant = 1;
        unsupportedDurationField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType durationFieldType1 = DurationFieldType.durationWeeks();
        DurationFieldType durationFieldType2 = DurationFieldType.durationDays();
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(durationFieldType1);
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(durationFieldType2);
        assertEquals(0, unsupportedDurationField1.compareTo(unsupportedDurationField2));
    }

    @Test
    public void testEquals() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(durationFieldType);
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(durationFieldType);
        assertTrue(unsupportedDurationField1.equals(unsupportedDurationField2));
    }

    @Test
    public void testHashCode() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        assertEquals(durationFieldType.getName().hashCode(), unsupportedDurationField.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType durationFieldType = DurationFieldType.durationWeeks();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(durationFieldType);
        assertEquals("UnsupportedDurationField[durationWeeks]", unsupportedDurationField.toString());
    }

}