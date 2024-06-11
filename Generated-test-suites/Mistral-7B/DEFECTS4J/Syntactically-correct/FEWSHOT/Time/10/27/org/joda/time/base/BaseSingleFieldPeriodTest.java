package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    /**
     * Test the between method with instants.
     */
    @Test
    public void testBetweenInstant() {
        // Test with zero period
        assertEquals(0, BaseSingleFieldPeriod.between(new BaseDateTime(0, ISOChronology.UTC), new BaseDateTime(0, ISOChronology.UTC), DurationFieldType.days()));

        // Test with positive period
        ReadableInstant start = new BaseDateTime(2012, 1, 1, 0, 0, 0, 0, ISOChronology.UTC);
        ReadableInstant end = new BaseDateTime(2012, 1, 2, 0, 0, 0, 0, ISOChronology.UTC);
        assertEquals(1, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));

        // Test with negative period
        start = new BaseDateTime(2012, 1, 2, 0, 0, 0, 0, ISOChronology.UTC);
        end = new BaseDateTime(2012, 1, 1, 0, 0, 0, 0, ISOChronology.UTC);
        assertEquals(-1, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));
    }

    /**
     * Test the between method with partials.
     */
    @Test
    public void testBetweenPartial() {
        // Test with zero period
        ReadablePartial start = new BaseLocalDate(2012, 1, 1, ISOChronology.UTC);
        ReadablePartial end = new BaseLocalDate(2012, 1, 1, ISOChronology.UTC);
        assertEquals(0, BaseSingleFieldPeriod.between(start, end, Period.class));

        // Test with positive period
        start = new BaseLocalDate(2012, 1, 1, ISOChronology.UTC);
        end = new BaseLocalDate(2012, 1, 2, ISOChronology.UTC);
        assertEquals(1, BaseSingleFieldPeriod.between(start, end, Period.class));

        // Test with negative period
        start = new BaseLocalDate(2012, 1, 2, ISOChronology.UTC);
        end = new BaseLocalDate(2012, 1, 1, ISOChronology.UTC);
        assertEquals(-1, BaseSingleFieldPeriod.between(start, end, Period.class));
    }

    /**
     * Test the standardPeriodIn method.
     */
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new BasePeriod(1, DurationFieldType.days());
        assertEquals(24 * 60 * 60 * 1000L, BaseSingleFieldPeriod.standardPeriodIn(period, 1000L));
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertEquals(1, period.getValue());
        assertEquals(fieldType, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Test getValue method.
     */
    @Test
    public void testGetValue() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertEquals(1, period.getValue());
    }

    /**
     * Test setValue method.
     */
    @Test
    public void testSetValue() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.setValue(2);
        assertEquals(2, period.getValue());
    }

    /**
     * Test getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertEquals(fieldType, period.getFieldType());
    }

    /**
     * Test getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertEquals(1, period.size());
    }

    /**
     * Test getFieldType method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInvalidIndex() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.getFieldType(2);
    }

    /**
     * Test getValue method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueInvalidIndex() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.getValue(2);
    }

    /**
     * Test get method.
     */
    @Test
    public void testGet() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertEquals(1, period.get(fieldType));
        assertFalse(period.isSupported(DurationFieldType.hours()));
    }

    /**
     * Test isSupported method.
     */
    @Test
    public void testIsSupported() {
        DurationFieldType fieldTypeDays = DurationFieldType.days();
        DurationFieldType fieldTypeHours = DurationFieldType.hours();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertTrue(period.isSupported(fieldTypeDays));
        assertFalse(period.isSupported(fieldTypeHours));
    }

    /**
     * Test toPeriod method.
     */
    @Test
    public void testToPeriod() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        Period periodInstance = period.toPeriod();
        assertEquals(1, periodInstance.getYears());
        assertEquals(0, periodInstance.getMonths());
        assertEquals(0, periodInstance.getWeeks());
        assertEquals(1, periodInstance.getDays());
        assertEquals(0, periodInstance.getHours());
        assertEquals(0, periodInstance.getMinutes());
        assertEquals(0, periodInstance.getSeconds());
        assertEquals(0, periodInstance.getMillis());
        assertEquals(PeriodType.standard(), periodInstance.getPeriodType());
    }

    /**
     * Test toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        assertEquals(1, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
        assertEquals(PeriodType.standard(), mutablePeriod.getPeriodType());
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(2);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period6 = new BaseSingleFieldPeriod(1, ISOChronology.getInstanceUTC());

        // Test equality with same instance
        assertEquals(period1, period1);

        // Test equality with another instance of the same class
        assertEquals(period1, period2);
        assertEquals(period1, period4);
        assertEquals(period1, period5);

        // Test inequality with different class
        assertFalse(period1.equals(new Object()));

        // Test inequality with different value
        assertFalse(period1.equals(period3));
        assertFalse(period1.equals(period6));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(2);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period6 = new BaseSingleFieldPeriod(1, ISOChronology.getInstanceUTC());

        // Test that same instance has same hash code
        int hashCode1 = period1.hashCode();
        assertEquals(hashCode1, period1.hashCode());

        // Test that different instances have different hash codes
        int hashCode2 = period2.hashCode();
        assertNotSame(hashCode1, hashCode2);
        int hashCode3 = period3.hashCode();
        assertNotSame(hashCode1, hashCode3);
        int hashCode4 = period4.hashCode();
        assertEquals(hashCode1, hashCode4);
        int hashCode5 = period5.hashCode();
        assertEquals(hashCode1, hashCode5);
        int hashCode6 = period6.hashCode();
        assertNotSame(hashCode1, hashCode6);
    }

    /**
     * Test compareTo method.
     */
    @Test
    public void testCompareTo() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(1);

        // Test that equal instances have equal comparison results
        assertEquals(0, period1.compareTo(period3));

        // Test that smaller instance is less than equal instance
        assertEquals(-1, period1.compareTo(period2));

        // Test that equal instance is equal to itself
        assertEquals(0, period2.compareTo(period2));

        // Test that larger instance is greater than equal instance
        assertEquals(1, period2.compareTo(period1));
    }
}