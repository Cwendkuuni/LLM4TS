package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
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
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertNotNull(period);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(5, period.getValue());
    }

    /**
     * Test getting field type.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    /**
     * Test getting period type.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Test size.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    /**
     * Test getting field type at index.
     */
    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(DurationFieldType.DAYS, period.getFieldType(0));
    }

    /**
     * Test getting value at index.
     */
    @Test
    public void testgetValueAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue(0));
    }

    /**
     * Test getting value of a duration field.
     */
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.get(DurationFieldType.DAYS));
    }

    /**
     * Test is supported.
     */
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(true, period.isSupported(DurationFieldType.DAYS));
        assertEquals(false, period.isSupported(DurationFieldType.HOURS));
    }

    /**
     * Test toPeriod.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertEquals(5, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }

    /**
     * Test toMutablePeriod.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(5, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }

    /**
     * Test equals.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertEquals(period1, period2);

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        assertNotNull(period1.equals(period3));
    }

    /**
     * Test hashCode.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertEquals(period1.hashCode(), period2.hashCode());

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        assertNotEquals(period1.hashCode(), period3.hashCode());
    }

    /**
     * Test compareTo.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(7);

        assertEquals(0, period1.compareTo(period1));
        assertEquals(-1, period1.compareTo(period2));
        assertEquals(0, period1.compareTo(period3));
        assertEquals(1, period2.compareTo(period1));
        assertEquals(1, period3.compareTo(period1));
        assertEquals(1, period1.compareTo(period4));
    }

    /**
     * Test between.
     */
    @Test
    public void testBetween() {
        BaseDateTime start = new BaseDateTime(0, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        BaseDateTime end = new BaseDateTime(1, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(1, result);
    }

    /**
     * Test standardPeriodIn.
     */
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.days(5);
        long millisPerUnit = 86400000L;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(5, result);
    }
}