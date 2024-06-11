package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.DateTimeUtils;
import org.joda.time.chrono.ISOChronology;

/**
 * Test cases for {@link BaseSingleFieldPeriod} class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test case for {@link BaseSingleFieldPeriod#between(ReadableInstant, ReadableInstant, DurationFieldType)}.
     */
    @org.junit.Test
    public void testBetween() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant start = chrono.plus(Duration.standardDays(1));
        ReadableInstant end = chrono.plus(Duration.standardDays(2));
        DurationFieldType field = DurationFieldType.DAYS;
        int expected = 1;
        int actual = BaseSingleFieldPeriod.between(start, end, field);
        assertEquals(expected, actual);
    }

    /**
     * Test case for {@link BaseSingleFieldPeriod#between(ReadablePartial, ReadablePartial, ReadablePeriod)}.
     */
    @org.junit.Test
    public void testBetweenPartial() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadablePartial start = new MutableLocalDate().with(chrono, 2022, 1, 1);
        ReadablePartial end = new MutableLocalDate().with(chrono, 2022, 1, 2);
        ReadablePeriod zeroInstance = Period.ZERO;
        int expected = 1;
        int actual = BaseSingleFieldPeriod.between(start, end, zeroInstance);
        assertEquals(expected, actual);
    }

    /**
     * Test case for {@link BaseSingleFieldPeriod#standardPeriodIn(ReadablePeriod, long)} with a valid period.
     */
    @org.junit.Test
    public void testStandardPeriodInValidPeriod() {
        ReadablePeriod period = Period.parse("P1W");
        long millisPerUnit = Duration.standardHours(1).getMillis();
        int expected = 168; // 1 week = 168 hours
        int actual = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(expected, actual);
    }

    /**
     * Test case for {@link BaseSingleFieldPeriod#standardPeriodIn(ReadablePeriod, long)} with an invalid period.
     */
    @org.junit.Test
    public void testStandardPeriodInInvalidPeriod() {
        ReadablePeriod period = new MutablePeriod().withFields(new Period(1, 2, 3, new Period(1, 2, 3, 4)));
        long millisPerUnit = Duration.standardHours(1).getMillis();
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit));
    }

    /**
     * Test case for {@link BaseSingleFieldPeriod#BaseSingleFieldPeriod(int)} constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.size());
    }

    /**
     * Test case for {@link BaseSingleFieldPeriod#getValue()} and {@link BaseSingleFieldPeriod#setValue(int)} methods.
     */
    @org.junit.Test
    public void testValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        assertEquals(42, period.getValue());
        period.setValue(13);
        assertEquals(13, period.getValue());
    }

    /**
     * Test case for {@link BaseSingleFieldPeriod#getFieldType()} and {@link BaseSingleFieldPeriod#getPeriodType()}.
     */
    @org.junit.Test
    public void testGetters() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Test case for {@link BaseSingleFieldPeriod#size()} method.
     */
    @org.junit.Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        assertEquals(1, period.size());
    }

    /**
     * Test case for {@link BaseSingleFieldPeriod#getFieldType(int)} and {@link BaseSingleFieldPeriod#getValue(int)} methods.
     */
    @org.junit.Test
    public void testGetFields() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        assertEquals(DurationFieldType.DAYS, period.getFieldType(0));
        assertEquals(42, period.getValue(0));
    }

    /**
     * Test case for {@link BaseSingleFieldPeriod#get(DurationFieldType)} method.
     */
    @org.junit.Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        assertEquals(42, period.get(DurationFieldType.DAYS));
        assertThrows(IllegalArgumentException.class, () -> period.get(DurationFieldType.MONTHS));
    }

    /**
     * Test case for {@link BaseSingleFieldPeriod#isSupported(DurationFieldType)} method.
     */
    @org.junit.Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        assertTrue(period.isSupported(DurationFieldType.DAYS));
        assertFalse(period.isSupported(DurationFieldType.MONTHS));
    }

    /**
     * Test case for {@link BaseSingleFieldPeriod#toPeriod()} method.
     */
    @org.junit.Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        Period expected = new Period(42, PeriodUnit.DAYS);
        Period actual = period.toPeriod();
        assertEquals(expected, actual);
    }

    /**
     * Test case for {@link BaseSingleFieldPeriod#toMutablePeriod()} method.
     */
    @org.junit.Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        MutablePeriod expected = new MutablePeriod();
        expected.setFields(42, PeriodUnit.DAYS);
        MutablePeriod actual = period.toMutablePeriod();
        assertEquals(expected, actual);
    }

    /**
     * Test case for {@link BaseSingleFieldPeriod#equals(Object)} method.
     */
    @org.junit.Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(42);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(42);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(41);
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
        assertFalse(period1.equals(null));
        assertFalse(period1.equals("test"));
    }

    /**
     * Test case for {@link BaseSingleFieldPeriod#hashCode()} method.
     */
    @org.junit.Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(42);
        int expected = 15524122;
        int actual = period.hashCode();
        assertEquals(expected, actual);
    }
}