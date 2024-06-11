package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriodValue() {
        BaseSingleFieldPeriod period = new Days(5);
        int value = period.getValue();
        assertEquals(5, value);
    }

    @Test
    public void testBaseSingleFieldPeriodToString() {
        BaseSingleFieldPeriod period = new Hours(3);
        String toString = period.toString();
        assertEquals("P3H3", toString);
    }

    @Test
    public void testBaseSingleFieldPeriodGetFieldType() {
        BaseSingleFieldPeriod period = new Minutes(10);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(Minutes.MINUTES, fieldType);
    }

    @Test
    public void testBaseSingleFieldPeriodGetPeriodType() {
        BaseSingleFieldPeriod period = new Milliseconds(10);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.MILLISECONDS, periodType);
    }

    @Test
    public void testBaseSingleFieldPeriodSize() {
        BaseSingleFieldPeriod period = new Days(3);
        int size = period.size();
        assertEquals(1, size);
    }

    @Test
    public void testBaseSingleFieldPeriodGetFieldTypeNull() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0);
        assertNull(period.getFieldType());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBaseSingleFieldPeriodGetValueNegative() {
        BaseSingleFieldPeriod period = new Days(5);
        period.getValue(-1);
    }

    @Test
    public void testBaseSingleFieldPeriodEquals() {
        BaseSingleFieldPeriod period1 = new Hours(3);
        BaseSingleFieldPeriod period2 = new Hours(3);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testBaseSingleFieldPeriodNotEquals() {
        BaseSingleFieldPeriod period1 = new Hours(3);
        Object obj = new Object();
        assertFalse(period1.equals(obj));
    }

    @Test
    public void testBaseSingleFieldPeriodNotEqualsDifferentType() {
        BaseSingleFieldPeriod period1 = new Hours(3);
        BaseSingleFieldPeriod period2 = new Minutes(3);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testBaseSingleFieldPeriodHashCode() {
        BaseSingleFieldPeriod period1 = new Hours(3);
        BaseSingleFieldPeriod period2 = new Hours(3);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testBaseSingleFieldPeriodCompareToZero() {
        BaseSingleFieldPeriod period1 = new Hours(3);
        int compareTo = period1.compareTo(new BaseSingleFieldPeriod(0));
        assertEquals(1, compareTo);
    }

    @Test
    public void testBaseSingleFieldPeriodCompareToNegative() {
        BaseSingleFieldPeriod period1 = new Hours(3);
        BaseSingleFieldPeriod period2 = new Hours(-3);
        int compareTo = period1.compareTo(period2);
        assertEquals(-1, compareTo);
    }

    @Test
    public void testBaseSingleFieldPeriodCompareToEqual() {
        BaseSingleFieldPeriod period1 = new Hours(3);
        BaseSingleFieldPeriod period2 = new Hours(3);
        int compareTo = period1.compareTo(period2);
        assertEquals(0, compareTo);
    }

    @Test
    public void testBaseSingleFieldPeriodCompareToDifferentType() {
        BaseSingleFieldPeriod period1 = new Hours(3);
        BaseSingleFieldPeriod period2 = new Minutes(3);
        int compareTo = period1.compareTo(period2);
        assertEquals(-1, compareTo);
    }

    @Test
    public void testBaseSingleFieldPeriodCompareToNull() {
        BaseSingleFieldPeriod period = new Hours(3);
        int compareTo = period.compareTo(null);
        assertEquals(-1, compareTo);
    }

    @Test
    public void testBaseSingleFieldPeriodGetValueFromReadablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123456789);
        int value = period.getValue();
        assertEquals(123456789, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodGetValueFromReadablePeriodInvalidType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123456789);
        period.getValue("invalidType");
    }

    @Test
    public void testBaseSingleFieldPeriodFromReadablePeriod() {
        ReadablePeriod readablePeriod = new GeneralMilliseconds(123456789);
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(readablePeriod);
        int value = period.getValue();
        assertEquals(123456789, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodFromReadablePeriodInvalid() {
        ReadablePeriod readablePeriod = null;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(readablePeriod);
    }

    @Test
    public void testBaseSingleFieldPeriodCompareToReadablePeriod() {
        BaseSingleFieldPeriod period1 = new Hours(3);
        ReadablePeriod readablePeriod2 = new GeneralHours(3);
        int compareTo = period1.compareTo(readablePeriod2);
        assertEquals(0, compareTo);
    }

    @Test
    public void testBaseSingleFieldPeriodCompareToReadablePeriodNegative() {
        BaseSingleFieldPeriod period1 = new Hours(3);
        ReadablePeriod readablePeriod2 = new GeneralMinutes(6);
        int compareTo = period1.compareTo(readablePeriod2);
        assertEquals(-1, compareTo);
    }

    @Test
    public void testBaseSingleFieldPeriodCompareToReadablePeriodPositive() {
        BaseSingleFieldPeriod period1 = new Hours(3);
        ReadablePeriod readablePeriod2 = new GeneralHours(4);
        int compareTo = period1.compareTo(readablePeriod2);
        assertEquals(1, compareTo);
    }

    @Test
    public void testBaseSingleFieldPeriodToPeriod() {
        BaseSingleFieldPeriod period = new Days(3);
        Period periodToPeriod = period.toPeriod();
        assertEquals(new Days(3), periodToPeriod);
    }

    @Test
    public void testBaseSingleFieldPeriodToMutablePeriod() {
        BaseSingleFieldPeriod period = new Hours(3);
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        assertEquals(3, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @Test
    public void testBaseSingleFieldPeriodBetween() {
        Instant instant1 = new DateTime(2023, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
        Instant instant2 = new DateTime(2023, 1, 1, 3, 0, 0, 0, DateTimeZone.UTC).toInstant();
        BaseSingleFieldPeriod period = new Hours(3);
        int result = BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.HOURS);
        assertEquals(3, result);
    }

    @Test
    public void testBaseSingleFieldPeriodStandardPeriodIn() {
        Period period = new Period(1234, 56, 78, 90, 123, 456);
        long n = 10;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        assertEquals(12340, result);
    }
}