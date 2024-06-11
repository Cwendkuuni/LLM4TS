package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;

import java.util.Arrays;

/**
 * Test cases for BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test the between method for different instants.
     */
    @Test
    public void testBetween() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant start = new BaseDateTime(0L, chrono);
        ReadableInstant end = new BaseDateTime(1L * 24 * 60 * 60 * 1000L, chrono);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS);
        assertEquals(24, result);

        start = new BaseDateTime(1L * 24 * 60 * 60 * 1000L, chrono);
        end = new BaseDateTime(2L * 24 * 60 * 60 * 1000L, chrono);
        result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS);
        assertEquals(24, result);

        start = new BaseDateTime(2L * 24 * 60 * 60 * 1000L, chrono);
        end = new BaseDateTime(3L * 24 * 60 * 60 * 1000L, chrono);
        result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS);
        assertEquals(24, result);

        start = new BaseDateTime(3L * 24 * 60 * 60 * 1000L, chrono);
        end = null;
        try {
            result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        start = null;
        end = new BaseDateTime(3L * 24 * 60 * 60 * 1000L, chrono);
        try {
            result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test the between method for different partials.
     */
    @Test
    public void testBetweenPartial() {
        Chronology chrono = DateTimeUtils.getChronology(DateTimeUtils.UTC);
        ReadablePartial start = new BaseDateTime(0, 0, 0, 0, 0, 0, chrono).getLocalDate();
        ReadablePartial end = new BaseDateTime(31, 12, 2022, 0, 0, 0, chrono).getLocalDate();
        int result = BaseSingleFieldPeriod.between(start, end, new Period(1, DurationFieldType.MONTHS).toPeriod());
        assertEquals(1, result);

        ReadablePartial start2 = new BaseDateTime(1, 1, 2023, 0, 0, 0, chrono).getLocalDate();
        ReadablePartial end2 = new BaseDateTime(31, 12, 2023, 0, 0, 0, chrono).getLocalDate();
        result = BaseSingleFieldPeriod.between(start2, end2, new Period(1, DurationFieldType.YEARS).toPeriod());
        assertEquals(1, result);

        ReadablePartial start3 = new BaseDateTime(31, 12, 2023, 0, 0, 0, chrono).getLocalDate();
        ReadablePartial end3 = new BaseDateTime(31, 1, 2024, 0, 0, 0, chrono).getLocalDate();
        result = BaseSingleFieldPeriod.between(start3, end3, new Period(1, DurationFieldType.MONTHS).toPeriod());
        assertEquals(1, result);

        ReadablePartial start4 = new BaseDateTime(31, 1, 2024, 0, 0, 0, chrono).getLocalDate();
        try {
            result = BaseSingleFieldPeriod.between(start4, null, new Period(1, DurationFieldType.MONTHS).toPeriod());
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        ReadablePartial start5 = null;
        try {
            result = BaseSingleFieldPeriod.between(start5, end3, new Period(1, DurationFieldType.MONTHS).toPeriod());
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test the standardPeriodIn method for different periods.
     */
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period1 = new Period(7, DurationFieldType.DAYS).toPeriod();
        int result = BaseSingleFieldPeriod.standardPeriodIn(period1, 1000 * 60 * 60 * 24 * 7);
        assertEquals(7, result);

        ReadablePeriod period2 = new Period(1, 23, 59, 59, 59, 59, new Period(12, 31, 2022, 0, 0, 0, DateTimeUtils.UTC)).toPeriod();
        result = BaseSingleFieldPeriod.standardPeriodIn(period2, 1000 * 60 * 60 * 24 * 30 * 12);
        assertEquals(1, result);

        ReadablePeriod period3 = new Period(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29).toPeriod();
        result = BaseSingleFieldPeriod.standardPeriodIn(period3, 1000 * 60 * 60 * 24 * 365 * 10);
        assertEquals(1, result);

        ReadablePeriod period4 = new Period(3, 2, 3, 4, 5, new Period(12, 31, 2022, 0, 0, 0, DateTimeUtils.UTC), new Period(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29).toPeriod());
        try {
            result = BaseSingleFieldPeriod.standardPeriodIn(period4, 1000 * 60 * 60 * 24 * 365 * 10);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test the constructor.
     */
    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
        assertEquals(DurationFieldType.DAYS, period1.getFieldType());
        assertEquals(PeriodType.standard(), period1.getPeriodType());
        assertEquals(1, period1.size());
        assertEquals(7, period1.getValue());

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1, DurationFieldType.MONTHS, PeriodType.standard());
        assertEquals(DurationFieldType.MONTHS, period2.getFieldType());
        assertEquals(PeriodType.standard(), period2.getPeriodType());
        assertEquals(1, period2.size());
        assertEquals(1, period2.getValue());

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(0);
        assertEquals(DurationFieldType.UNKNOWN, period3.getFieldType());
        assertEquals(PeriodType.standard(), period3.getPeriodType());
        assertEquals(0, period3.size());
        assertEquals(0, period3.getValue());
    }

    /**
     * Test the getValue method.
     */
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        assertEquals(7, period.getValue());
    }

    /**
     * Test the setValue method.
     */
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        period.setValue(12);
        assertEquals(12, period.getValue());
    }

    /**
     * Test the getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    /**
     * Test the getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Test the equals method.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7);
        assertEquals(period1, period2);
        assertEquals(period1.hashCode(), period2.hashCode());

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        assertNotEquals(period1, period3);
        assertNotEquals(period1.hashCode(), period3.hashCode());

        BaseSingleFieldPeriod period4 = null;
        assertNotEquals(period1, period4);
        assertNotEquals(period1.hashCode(), period4.hashCode());

        BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriod(new Period(7, DurationFieldType.DAYS).toPeriod());
        assertEquals(period1, period5);
        assertEquals(period1.hashCode(), period5.hashCode());
    }

    /**
     * Test the compareTo method.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(6);
        assertTrue(period1.compareTo(period2) > 0);

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        assertTrue(period2.compareTo(period3) > 0);
        assertTrue(period1.compareTo(period3) > 0);

        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(7);
        assertEquals(0, period1.compareTo(period4));

        BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriod(8);
        assertTrue(period1.compareTo(period5) < 0);
        assertTrue(period4.compareTo(period5) < 0);
    }

    /**
     * Test the toPeriod method.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        Period result = period.toPeriod();
        assertEquals(DurationFieldType.DAYS, result.getFieldType());
        assertEquals(7, result.getValues()[0]);
        assertEquals(PeriodType.standard(), result.getPeriodType());

        period = new BaseSingleFieldPeriod(12, PeriodType.months());
        result = period.toPeriod();
        assertEquals(DurationFieldType.MONTHS, result.getFieldType());
        assertEquals(1, result.getValues()[0]);
        assertEquals(PeriodType.months(), result.getPeriodType());
    }

    /**
     * Test the toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(DurationFieldType.DAYS, result.getFieldType());
        assertEquals(7, result.getValues()[0]);
        assertEquals(PeriodType.standard(), result.getPeriodType());

        period = new BaseSingleFieldPeriod(12, PeriodType.months());
        result = period.toMutablePeriod();
        assertEquals(DurationFieldType.MONTHS, result.getFieldType());
        assertEquals(1, result.getValue(0));
        assertEquals(PeriodType.months(), result.getPeriodType());
    }
}