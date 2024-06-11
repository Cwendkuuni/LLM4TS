package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

/**
 * Tests for the UnsupportedDurationField class.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Tests getting a singleton instance.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field, field2);
    }

    /**
     * Tests getting a null instance.
     */
    @Test(expected = NullPointerException.class)
    public void testGetInstanceNullType() {
        UnsupportedDurationField.getInstance(null);
    }

    /**
     * Tests the getName method.
     */
    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("days", field.getName());
    }

    /**
     * Tests the isSupported method.
     */
    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }

    /**
     * Tests the isPrecise method.
     */
    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertTrue(field.isPrecise());
    }

    /**
     * Tests the getValue method with long duration.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLongDuration() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 1;
        field.getValue(duration);
    }

    /**
     * Tests the getValueAsLong method with long duration.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongLongDuration() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 1;
        field.getValueAsLong(duration);
    }

    /**
     * Tests the getValue method with long duration and long instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLongDurationLongInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 1;
        long instant = 1;
        field.getValue(duration, instant);
    }

    /**
     * Tests the getValueAsLong method with long duration and long instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongLongDurationLongInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 1;
        long instant = 1;
        field.getValueAsLong(duration, instant);
    }

    /**
     * Tests the getMillis method with int value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisIntValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        int value = 1;
        field.getMillis(value);
    }

    /**
     * Tests the getMillis method with long value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long value = 1;
        field.getMillis(value);
    }

    /**
     * Tests the getMillis method with int value and long instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisIntValueLongInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        int value = 1;
        long instant = 1;
        field.getMillis(value, instant);
    }

    /**
     * Tests the getMillis method with long value and long instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongValueLongInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long value = 1;
        long instant = 1;
        field.getMillis(value, instant);
    }

    /**
     * Tests the add method with long instant and int value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddLongInstantIntValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 1;
        int value = 1;
        field.add(instant, value);
    }

    /**
     * Tests the add method with long instant and long value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddLongInstantLongValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 1;
        long value = 1;
        field.add(instant, value);
    }

    /**
     * Tests the getDifference method with long minuendInstant and long subtrahendInstant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceLongMinuendInstantLongSubtrahendInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1;
        long subtrahendInstant = 1;
        field.getDifference(minuendInstant, subtrahendInstant);
    }

    /**
     * Tests the getDifferenceAsLong method with long minuendInstant and long subtrahendInstant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLongLongMinuendInstantLongSubtrahendInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1;
        long subtrahendInstant = 1;
        field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    /**
     * Tests the getUnitMillis method.
     */
    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }

    /**
     * Tests the compareTo method.
     */
    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(0, field1.compareTo(field2));
    }

    /**
     * Tests the equals method with null object.
     */
    @Test
    public void testEqualsNullObject() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.equals(null));
    }

    /**
     * Tests the equals method with different object.
     */
    @Test
    public void testEqualsDifferentObject() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        Object anotherObject = new Object();
        assertFalse(field1.equals(anotherObject));
    }

    /**
     * Tests the equals method with same instance.
     */
    @Test
    public void testEqualsSameInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertTrue(field.equals(field));
    }

    /**
     * Tests the hashCode method.
     */
    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        String name = field.getName();
        int expectedHashCode = name.hashCode();
        int actualHashCode = field.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    /**
     * Tests the toString method.
     */
    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        String expectedString = "UnsupportedDurationField[days]";
        String actualString = field.toString();
        assertEquals(expectedString, actualString);
    }

    /**
     * Tests the readResolve method.
     */
    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNull(field.readResolve());
    }

    /**
     * Tests the unsupported method.
     */
    @Test
    public void testUnsupported() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        UnsupportedOperationException expectedException = new UnsupportedOperationException("days field is unsupported");
        UnsupportedOperationException actualException = field.unsupported();
        assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

}