package org.joda.time.base;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        assertEquals(123, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    /**
     * Test getting and setting value.
     */
    @Test
    public void testGetValueSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        int value = period.getValue();
        period.setValue(456);
        assertEquals(456, period.getValue());
    }

    /**
     * Test getting field type.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    /**
     * Test getting period type.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    /**
     * Test size.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        assertEquals(1, period.size());
    }

    /**
     * Test getting field type at index.
     */
    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        DurationFieldType fieldType = period.getFieldType(0);
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    /**
     * Test getting value at index.
     */
    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        int value = period.getValue(0);
        assertEquals(123, value);
    }

    /**
     * Test getting value of field.
     */
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        int value = period.get(DurationFieldType.DAYS);
        assertEquals(123, value);
    }

    /**
     * Test is supported.
     */
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        assertTrue(period.isSupported(DurationFieldType.DAYS));
        assertFalse(period.isSupported(DurationFieldType.HOURS));
    }

    /**
     * Test between.
     */
    @Test
    public void testBetween() {
        // Instant to Instant
        ReadableInstant start = new BaseDateTime(0, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        ReadableInstant end = new BaseDateTime(1, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(1, result);

        // Partial to Partial
        ReadablePartial startPartial = new BaseDateTime(0, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toLocalDate();
        ReadablePartial endPartial = new BaseDateTime(1, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toLocalDate();
        result = BaseSingleFieldPeriod.between(startPartial, endPartial, new BaseSingleFieldPeriod(1, DurationFieldType.DAYS));
        assertEquals(1, result);

        // Invalid case: different number of fields
        ReadablePartial startPartial2 = new BaseDateTime(0, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toLocalDate();
        ReadablePartial endPartial2 = new BaseDateTime(1, 1, 1, 0, 0, 1, 0, ISOChronology.getInstanceUTC()).toLocalDate();
        try {
            BaseSingleFieldPeriod.between(startPartial2, endPartial2, new BaseSingleFieldPeriod(1, DurationFieldType.DAYS));
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Invalid case: null input
        try {
            BaseSingleFieldPeriod.between(null, endPartial, DurationFieldType.DAYS);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            BaseSingleFieldPeriod.between(start, null, DurationFieldType.DAYS);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Valid case: zero period
        ReadablePartial zeroPartial = new BaseDateTime(0, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toLocalDate();
        result = BaseSingleFieldPeriod.between(startPartial, zeroPartial, new BaseSingleFieldPeriod(1, DurationFieldType.DAYS));
        assertEquals(0, result);
    }

    /**
     * Test standardPeriodIn.
     */
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new BaseSingleFieldPeriod(123, DurationFieldType.DAYS);
        long millisPerUnit = 1000 * 60 * 60 * 24; // 1 day in milliseconds
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(123, result);
    }

    /**
     * Test standardPeriodIn with invalid period.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInInvalidPeriod() {
        ReadablePeriod period = new BaseSingleFieldPeriod(123, DurationFieldType.DAYS);
        long millisPerUnit = 1000 * 60; // 1 hour in milliseconds
        BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }

    /**
     * Test toPeriod.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123, DurationFieldType.DAYS);
        Period result = period.toPeriod();
        assertEquals(new Period(123, PeriodType.standard()), result);
    }

    /**
     * Test toMutablePeriod.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123, DurationFieldType.DAYS);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(new MutablePeriod(123, PeriodType.standard()), result);
    }

    /**
     * Test equals.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123, DurationFieldType.DAYS);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(123, DurationFieldType.DAYS);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(456, DurationFieldType.DAYS);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(123, DurationFieldType.HOURS);
        BaseSingleFieldPeriod period5 = null;

        assertTrue(period1.equals(period1));
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
        assertFalse(period1.equals(period4));
        assertFalse(period1.equals(period5));
    }

    /**
     * Test hashCode.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123, DurationFieldType.DAYS);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(123, DurationFieldType.DAYS);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(456, DurationFieldType.DAYS);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(123, DurationFieldType.HOURS);

        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        int hashCode3 = period3.hashCode();
        int hashCode4 = period4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
        assertNotSame(hashCode1, hashCode4);
    }

    /**
     * Test compareTo.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123, DurationFieldType.DAYS);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(456, DurationFieldType.DAYS);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(123, DurationFieldType.HOURS);

        int result = period1.compareTo(period1);
        assertEquals(0, result);

        result = period1.compareTo(period2);
        assertEquals(-1, result);

        result = period1.compareTo(period3);
        assertEquals(-1, result);
    }

    /**
     * Test compareTo with invalid period.
     */
    @Test(expected = ClassCastException.class)
    public void testCompareToInvalidPeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123, DurationFieldType.DAYS);
        Object period2 = new Object();
        period1.compareTo(period2);
    }
}