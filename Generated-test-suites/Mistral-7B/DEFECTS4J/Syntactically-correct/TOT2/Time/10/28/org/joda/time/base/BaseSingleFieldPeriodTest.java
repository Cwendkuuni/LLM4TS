package org.joda.time.base;

import org.junit.Test;
import org.junit.Assert.*;

import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;

/**
 * Tests for the BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.days();
        assertEquals(fieldType, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(5, period.getValue());
    }

    /**
     * Test getter and setter methods.
     */
    @Test
    public void testGettersAndSetters() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.days();
        assertEquals(fieldType, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(5, period.getValue());

        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    /**
     * Test method toPeriod() which returns an immutable Period object.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period expected = new Period(5, DurationFieldType.days(), PeriodType.standard());
        Period actual = period.toPeriod();
        assertEquals(expected, actual);
    }

    /**
     * Test method toMutablePeriod() which returns a MutablePeriod object.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod expected = new MutablePeriod(5, DurationFieldType.days());
        MutablePeriod actual = period.toMutablePeriod();
        assertEquals(expected, actual);
    }

    /**
     * Test equals() method with same instance.
     */
    @Test
    public void testEqualsSameInstance() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertTrue(period.equals(period));
    }

    /**
     * Test equals() method with different instance having same values.
     */
    @Test
    public void testEqualsDifferentInstanceSameValues() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
    }

    /**
     * Test equals() method with different instance having different values.
     */
    @Test
    public void testEqualsDifferentInstancesDifferentValues() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertFalse(period1.equals(period2));
    }

    /**
     * Test equals() method with null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testEqualsNull() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.equals(null);
    }

    /**
     * Test equals() method with different class.
     */
    @Test
    public void testEqualsDifferentClass() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertFalse(period.equals(new Object()));
    }

    /**
     * Test compareTo() method with same instance.
     */
    @Test
    public void testCompareToSameInstance() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(0, period.compareTo(period));
    }

    /**
     * Test compareTo() method with different instance having smaller value.
     */
    @Test
    public void testCompareToDifferentInstanceSmaller() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertTrue(period1.compareTo(period2) < 0);
    }

    /**
     * Test compareTo() method with different instance having larger value.
     */
    @Test
    public void testCompareToDifferentInstanceLarger() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertTrue(period1.compareTo(period2) > 0);
    }

    /**
     * Test compareTo() method with same field type.
     */
    @Test
    public void testCompareToSameFieldType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5, DurationFieldType.days());
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5, DurationFieldType.days());
        assertEquals(0, period1.compareTo(period2));
    }

    /**
     * Test compareTo() method with different field types.
     */
    @Test
    public void testCompareToDifferentFieldTypes() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5, DurationFieldType.days());
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10, DurationFieldType.hours());
        assertTrue(period1.compareTo(period2) < 0);
    }

    /**
     * Test getValue() method with valid arguments.
     */
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    /**
     * Test getValue() method with null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testGetValueNull() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(null);
    }

    /**
     * Test getFieldType() method with valid arguments.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(DurationFieldType.days(), period.getFieldType());
    }

    /**
     * Test getPeriodType() method with valid arguments.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Test standardPeriodIn() method with valid arguments.
     */
    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(new Period(5, DurationFieldType.days(), PeriodType.standard()));
        long millisPerUnit = 1000 * 60 * 60 * 24; // 1 day in milliseconds
        int expected = 5;
        int actual = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(expected, actual);
    }

    /**
     * Test between() method with valid arguments.
     */
    @Test
    public void testBetween() {
        ReadableInstant start = DateTimeUtils.currentTime().withFieldAdded(DurationFieldType.days(), 5);
        ReadableInstant end = DateTimeUtils.currentTime().withFieldAdded(DurationFieldType.days(), 10);
        int expected = 5;
        int actual = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(expected, actual);
    }
}