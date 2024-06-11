package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.joda.time.DurationFieldType;
import org.junit.Test;

/**
 * Test class for UnsupportedDurationField.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test method for {@link UnsupportedDurationField#getType()} .
     */
    @Test
    public void testGetType() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        DurationFieldType expectedType = DurationFieldType.MILLISECONDS;
        DurationFieldType actualType = unsupportedDurationField.getType();
        assertEquals(expectedType, actualType);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getName()} .
     */
    @Test
    public void testGetName() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        String expectedName = "milliseconds";
        String actualName = unsupportedDurationField.getName();
        assertEquals(expectedName, actualName);
    }

    /**
     * Test method for {@link UnsupportedDurationField#isSupported()} .
     */
    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertFalse(unsupportedDurationField.isSupported());
    }

    /**
     * Test method for {@link UnsupportedDurationField#isPrecise()} .
     */
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertTrue(unsupportedDurationField.isPrecise());
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValue(long)} .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.getValue(1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValueAsLong(long)} .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.getValueAsLong(1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValue(long, long)} .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.getValue(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValueAsLong(long, long)} .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.getValueAsLong(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(int)} .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.getMillis(1);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(long)} .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithDuration() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.getMillis(1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(int, long)} .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.getMillis(1, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(long, long)} .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithDurationAndInstant() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.getMillis(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#add(long, int)} .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.add(1L, 1);
    }

    /**
     * Test method for {@link UnsupportedDurationField#add(long, long)} .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndDuration() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.add(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getDifference(long, long)} .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceWithInstant1AndInstant2() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.getDifference(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getDifferenceAsLong(long, long)} .
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLongWithInstant1AndInstant2() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        unsupportedDurationField.getDifferenceAsLong(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getUnitMillis()} .
     */
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        long expectedUnitMillis = 0;
        long actualUnitMillis = unsupportedDurationField.getUnitMillis();
        assertEquals(expectedUnitMillis, actualUnitMillis);
    }

    /**
     * Test method for {@link UnsupportedDurationField#compareTo(DurationField)} .
     */
    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        int expectedCompareResult = 1;
        int actualCompareResult = unsupportedDurationField1.compareTo(unsupportedDurationField2);
        assertEquals(expectedCompareResult, actualCompareResult);
    }

    /**
     * Test method for {@link UnsupportedDurationField#equals(Object)} .
     */
    @Test
    public void testEquals() {
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField unsupportedDurationField3 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertTrue(unsupportedDurationField1.equals(unsupportedDurationField1));
        assertTrue(unsupportedDurationField1.equals(unsupportedDurationField2));
        assertFalse(unsupportedDurationField1.equals(unsupportedDurationField3));
    }

    /**
     * Test method for {@link UnsupportedDurationField#hashCode()} .
     */
    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        int expectedHashCode = "milliseconds".hashCode();
        int actualHashCode = unsupportedDurationField.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    /**
     * Test method for {@link UnsupportedDurationField#toString()} .
     */
    @Test
    public void testToString() {
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        String expectedToString = "UnsupportedDurationField[milliseconds]";
        String actualToString = unsupportedDurationField.toString();
        assertEquals(expectedToString, actualToString);
    }

    /**
     * Test method for {@link UnsupportedDurationField#readResolve()} .
     */
    @Test
    public void testReadResolve() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertNotNull(unsupportedDurationField.readResolve());
    }

    /**
     * Test method for {@link UnsupportedDurationField#unsupported()} .
     */
    @Test
    public void testUnsupported() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertNotNull(unsupportedDurationField.unsupported());
    }
}