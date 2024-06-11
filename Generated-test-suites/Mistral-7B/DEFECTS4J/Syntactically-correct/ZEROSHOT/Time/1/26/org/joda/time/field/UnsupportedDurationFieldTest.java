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
 * Test file for UnsupportedDurationField class.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupportedField.getType());
    }

    /**
     * Test getting an unsupported field instance using getInstance method.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupportedField1, unsupportedField2);
    }

    /**
     * Test getting an unsupported field instance using null type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceNullType() {
        UnsupportedDurationField.getInstance(null);
    }

    /**
     * Test isSupported method.
     */
    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(unsupportedField.isSupported());
    }

    /**
     * Test isPrecise method.
     */
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertTrue(unsupportedField.isPrecise());
    }

    /**
     * Test getValue method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long duration = 1000L;
        unsupportedField.getValue(duration);
    }

    /**
     * Test getValueAsLong method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long duration = 1000L;
        unsupportedField.getValueAsLong(duration);
    }

    /**
     * Test getValue method with instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long duration = 1000L;
        long instant = 1234567890L;
        unsupportedField.getValue(duration, instant);
    }

    /**
     * Test getValueAsLong method with instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long duration = 1000L;
        long instant = 1234567890L;
        unsupportedField.getValueAsLong(duration, instant);
    }

    /**
     * Test getMillis method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        int value = 10;
        unsupportedField.getMillis(value);
    }

    /**
     * Test getMillis method with long value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long value = 10L;
        unsupportedField.getMillis(value);
    }

    /**
     * Test getMillis method with instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        int value = 10;
        long instant = 1234567890L;
        unsupportedField.getMillis(value, instant);
    }

    /**
     * Test getMillis method with long value and instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long value = 10L;
        long instant = 1234567890L;
        unsupportedField.getMillis(value, instant);
    }

    /**
     * Test add method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long instant = 1234567890L;
        int value = 10;
        unsupportedField.add(instant, value);
    }

    /**
     * Test add method with long value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long instant = 1234567890L;
        long value = 10L;
        unsupportedField.add(instant, value);
    }

    /**
     * Test getDifference method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long minuendInstant = 1234567890L;
        long subtrahendInstant = 1234567880L;
        unsupportedField.getDifference(minuendInstant, subtrahendInstant);
    }

    /**
     * Test getDifferenceAsLong method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long minuendInstant = 1234567890L;
        long subtrahendInstant = 1234567880L;
        unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    /**
     * Test getUnitMillis method.
     */
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(0, unsupportedField.getUnitMillis());
    }

    /**
     * Test compareTo method.
     */
    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        int comparisonResult = unsupportedField1.compareTo(unsupportedField2);
        assertTrue(comparisonResult > 0);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedFieldSame = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedFieldDifferent = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertEquals(unsupportedField, unsupportedFieldSame);
        assertFalse(unsupportedField.equals(unsupportedFieldDifferent));
        UnsupportedDurationField unsupportedFieldWithSameName = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals(unsupportedField, unsupportedFieldWithSameName);
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long hashCode = unsupportedField.hashCode();
        UnsupportedDurationField unsupportedFieldSame = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(hashCode, unsupportedFieldSame.hashCode());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        String expected = "UnsupportedDurationField[DAYS]";
        assertEquals(expected, unsupportedField.toString());
    }

    /**
     * Test readResolve method.
     */
    @Test
    public void testReadResolve() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertSame(UnsupportedDurationField.getInstance(DurationFieldType.DAYS), unsupportedField.readResolve());
    }

    /**
     * Test unsupported method.
     */
    @Test
    public void testUnsupported() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedOperationException exception = unsupportedField.unsupported();
        String expectedMessage = "DAYS field is unsupported";
        assertEquals(expectedMessage, exception.getMessage());
    }
}