package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.joda.time.BaseSingleFieldPeriod;
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
import org.joda.time.field.FieldType;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadableInstant, ReadableInstant, DurationFieldType)}.
     */
    @Test
    public void testBetweenInstant() {
        Instant start = Instant.ofEpochMilli(1234567890L);
        Instant end = Instant.ofEpochMilli(1234567900L);
        DurationFieldType field = DurationFieldType.DAYS;
        int expected = 1;
        int actual = BaseSingleFieldPeriod.between(start, end, field);
        assertEquals("Instant between should return 1 day", expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadablePartial, ReadablePartial, ReadablePeriod)}.
     */
    @Test
    public void testBetweenPartial() {
        ReadablePartial start = new LocalDate(2021, 1, 1);
        ReadablePartial end = new LocalDate(2021, 1, 2);
        ReadablePeriod zeroInstance = new LocalDate();
        int expected = 1;
        int actual = BaseSingleFieldPeriod.between(start, end, zeroInstance);
        assertEquals("Partial between should return 1 day", expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#standardPeriodIn(ReadablePeriod, long)}.
     */
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(1, 2, 3, 4, 5, 6, 7);
        long millisPerUnit = 1000L * 60L * 60L * 24L; // one day in millis
        int expected = 12 * 2 + 3 * 24 + 4 * 30 * 24 + 5 * 12 * 30 * 24 + 6 * 12 * 365 * 24 + 7 * 7 * 365 * 24;
        int actual = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals("StandardPeriodIn should return 3461 days", expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#BaseSingleFieldPeriod(int)}.
     */
    @Test
    public void testBaseSingleFieldPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        assertNotNull("New period should not be null", period);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue()}.
     */
    @Test
    public void testValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        int expected = 123;
        int actual = period.getValue();
        assertEquals("Value should be 123", expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#setValue(int)}.
     */
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        period.setValue(456);
        int expected = 456;
        int actual = period.getValue();
        assertEquals("Value should be 456", expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType()}.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }
        };
        DurationFieldType expected = DurationFieldType.HOURS;
        DurationFieldType actual = period.getFieldType();
        assertEquals("FieldType should be HOURS", expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getPeriodType()}.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        PeriodType expected = PeriodType.standard();
        PeriodType actual = period.getPeriodType();
        assertEquals("PeriodType should be standard", expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#size()}.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        int expected = 1;
        int actual = period.size();
        assertEquals("Size should be 1", expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType(int)}.
     */
    @Test
    public void testGetFieldTypeInt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        DurationFieldType expected = DurationFieldType.DAYS;
        DurationFieldType actual = period.getFieldType(0);
        assertEquals("FieldType should be DAYS", expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue(int)}.
     */
    @Test
    public void testgetValueInt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        int expected = 123;
        int actual = period.getValue(0);
        assertEquals("Value should be 123", expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#get(DurationFieldType)}.
     */
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        DurationFieldType fieldType = DurationFieldType.DAYS;
        int expected = 1;
        int actual = period.get(fieldType);
        assertEquals("Get should return 1 day", expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#isSupported(DurationFieldType)}.
     */
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        DurationFieldType fieldType = DurationFieldType.DAYS;
        boolean expected = true;
        boolean actual = period.isSupported(fieldType);
        assertTrue("IsSupported should return true", expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toPeriod()}.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        Period expected = new Period(123, 0, 0, 0, 0, 0, 0);
        Period actual = period.toPeriod();
        assertEquals("ToPeriod should return Period(123, 0, 0, 0, 0, 0, 0)", expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toMutablePeriod()}.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        MutablePeriod expected = new MutablePeriod();
        MutablePeriod actual = period.toMutablePeriod();
        assertEquals("ToMutablePeriod should return new MutablePeriod", expected, actual);
        expected.add(DurationFieldType.DAYS, 123);
        assertEquals("ToMutablePeriod should add 123 days", expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#equals(Object)}.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(123);
        Object other = new Object();
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(456);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(123);
        boolean expected = true;
        boolean actual1 = period1.equals(period2);
        assertTrue("Equals should return true for equal periods", expected, actual1);
        expected = false;
        boolean actual2 = period1.equals(other);
        assertFalse("Equals should return false for non-period objects", expected, actual2);
        expected = true;
        boolean actual3 = period1.equals(period3);
        assertFalse("Equals should return false for different periods", expected, actual3);
        expected = true;
        boolean actual4 = period1.equals(period4);
        assertTrue("Equals should return true for equal periods", expected, actual4);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#hashCode()}.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(123);
        int expected1 = 27 * 27 * 123 + DurationFieldType.DAYS.hashCode();
        int actual1 = period1.hashCode();
        assertEquals("HashCode should return " + expected1, expected1, actual1);
        int expected2 = expected1;
        int actual2 = period2.hashCode();
        assertEquals("HashCode should return " + expected1, expected2, actual2);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#compareTo(BaseSingleFieldPeriod)}.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(456);
        int expected = 0;
        int actual1 = period1.compareTo(period2);
        assertEquals("CompareTo should return 0 for equal periods", expected, actual1);
        expected = -1;
        int actual2 = period1.compareTo(period3);
        assertEquals("CompareTo should return -1 for smaller periods", expected, actual2);
        expected = 1;
        int actual3 = period3.compareTo(period1);
        assertEquals("CompareTo should return 1 for larger periods", expected, actual3);
    }

}