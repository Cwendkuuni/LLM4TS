package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseDaily();
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseDaily();
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseDaily();
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.DAYS, periodType);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWrongIndex() {
        BaseSingleFieldPeriod period = new BaseDaily();
        period.getFieldType(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWrongIndex() {
        BaseSingleFieldPeriod period = new BaseDaily(3);
        period.getValue(2);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseDaily(5);
        int value = period.getValue();
        assertEquals(5, value);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseDaily();
        period.setValue(3);
        int value = period.getValue();
        assertEquals(3, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullReadablePartial() {
        ReadablePeriod period = Period.ZERO;
        ReadablePartial readablePartial = null;
        BaseSingleFieldPeriod.between(null, readablePartial, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullReadablePeriod() {
        ReadablePartial readablePartial = new GeneralBaseMinute(30);
        BaseSingleFieldPeriod.between(null, readablePartial, null);
    }

    @Test
    public void testBetween() throws Exception {
        DateTime start = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
        DateTime end = new DateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC);
        Period period = Period.days(1);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.MILLIS);
        assertEquals(86400001, result);

        DateTime start2 = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
        DateTime end2 = new DateTime(2022, 1, 2, 12, 30, 0, 0, DateTimeZone.UTC);
        Period period2 = Period.hours(12).minutes(30);
        int result2 = BaseSingleFieldPeriod.between(start2, end2, DurationFieldType.MILLIS);
        assertEquals(46200000, result2);
    }

    @Test
    public void testStandardPeriodIn() {
        Period period = Period.days(2);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 2);
        assertEquals(2, result);

        Period period2 = Period.hours(12).minutes(30);
        int result2 = BaseSingleFieldPeriod.standardPeriodIn(period2, 7);
        assertEquals(1712, result2);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseDaily(3);
        BaseSingleFieldPeriod period2 = new BaseDaily(3);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testNotEquals() {
        BaseSingleFieldPeriod period1 = new BaseDaily(3);
        BaseSingleFieldPeriod period2 = new BaseHourly(3);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseDaily(3);
        BaseSingleFieldPeriod period2 = new BaseDaily(3);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testCompareToSameClass() {
        BaseSingleFieldPeriod period1 = new BaseDaily(3);
        BaseSingleFieldPeriod period2 = new BaseDaily(3);
        int result = period1.compareTo(period2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareToDifferentClass() {
        BaseSingleFieldPeriod period1 = new BaseDaily(3);
        BaseSingleFieldPeriod period2 = new BaseHourly(3);
        int result = period1.compareTo(period2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareToZero() {
        BaseSingleFieldPeriod period = new BaseDaily(0);
        int result = period.compareTo(null);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareToNegative() {
        BaseSingleFieldPeriod period = new BaseDaily(-3);
        int result = period.compareTo(new BaseDaily(3));
        assertEquals(-6, result);
    }

    @Test
    public void testCompareToPositive() {
        BaseSingleFieldPeriod period = new BaseDaily(3);
        int result = period.compareTo(new BaseDaily(0));
        assertEquals(3, result);
    }
}