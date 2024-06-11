package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testEmptyPeriod() {
        BasePeriod period = new BasePeriod(0);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, period.getValues());
    }

    @Test
    public void testPeriodWithValues() {
        BasePeriod period = new BasePeriod(3, 2, 1, 5, 2, 30, 15, 30, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{3, 2, 1, 5, 2, 30, 15, 30}, period.getValues());
    }

    @Test
    public void testPeriodWithInstants() {
        DateTime start = new DateTime(2022, 1, 1, 0, 0);
        DateTime end = new DateTime(2022, 1, 3, 12, 0);
        BasePeriod period = new BasePeriod(start, end, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{2, 1, 2, 0, 12, 0, 0, 0}, period.getValues());
    }

    @Test
    public void testPeriodWithReadablePartials() {
        BaseLocalDate start = new LocalDate(2022, 1, 1);
        BaseLocalDate end = new LocalDate(2022, 1, 3);
        BasePeriod period = new BasePeriod(start, end, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{2, 1, 0, 0, 0, 0, 0, 0}, period.getValues());
    }

    @Test
    public void testPeriodWithDuration() {
        Duration duration = new Duration(2, 3, 4, 5, 6, 7, 8, 9);
        BasePeriod period = new BasePeriod(duration, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 2, 3, 4, 5, 6, 7, 9}, period.getValues());
    }

    @Test
    public void testPeriodWithDurationAndInstant() {
        Duration duration = new Duration(2, 3, 4, 5, 6, 7, 8, 9);
        DateTime instant = new DateTime(2022, 1, 1, 0, 0);
        BasePeriod period = new BasePeriod(instant, duration, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 2, 3, 4, 5, 6, 7, 9}, period.getValues());
    }

    @Test
    public void testPeriodWithObject() {
        Duration duration = new Duration(2, 3, 4, 5, 6, 7, 8, 9);
        BasePeriod period = new BasePeriod(duration, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 2, 3, 4, 5, 6, 7, 9}, period.getValues());

        DateTime instant = new DateTime(2022, 1, 1, 0, 0);
        period = new BasePeriod(instant, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, period.getValues());

        BaseLocalDate localDate = new LocalDate(2022, 1, 1);
        period = new BasePeriod(localDate, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, period.getValues());

        LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 0, 0);
        period = new BasePeriod(localDateTime, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, period.getValues());

        LocalTime localTime = new LocalTime(0, 0);
        period = new BasePeriod(localTime, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, period.getValues());

        BaseLocalDateTime baseLocalDateTime = new BaseLocalDateTime();
        period = new BasePeriod(baseLocalDateTime, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, period.getValues());
    }

    @Test
    public void testPeriodWithIntArray() {
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(values, period.getValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPeriodWithTwoNullObjects() {
        new BasePeriod(null, null, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPeriodWithNullObjectAndIntArray() {
        new BasePeriod(null, new int[]{1, 2, 3}, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPeriodWithTwoPartialObjectsOfDifferentTypes() {
        BaseLocalDate start = new LocalDate(2022, 1, 1);
        BaseDateTime end = new DateTime(2022, 1, 1, 0, 0);
        new BasePeriod(start, end, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPeriodWithTwoReadableObjectsOfDifferentTypes() {
        BaseLocalDateTime start = new BaseLocalDateTime();
        DateTime end = new DateTime(2022, 1, 1, 0, 0);
        new BasePeriod(start, end, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPeriodWithTwoInstantObjectsWithDifferentChronologies() {
        DateTime instant1 = new DateTime(2022, 1, 1, 0, 0, DateTimeZone.UTC);
        DateTime instant2 = new DateTime(2022, 1, 1, 0, 0, DateTimeZone.forOffsetHours(1));
        new BasePeriod(instant1, instant2, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPeriodWithTwoDurationObjects() {
        Duration duration1 = new Duration(2, 3, 4, 5, 6, 7, 8, 9);
        Duration duration2 = new Duration(1, 2, 3, 4, 5, 6, 7, 8);
        new BasePeriod(duration1, duration2, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPeriodWithInstantAndDurationWithDifferentChronologies() {
        DateTime instant = new DateTime(2022, 1, 1, 0, 0, DateTimeZone.UTC);
        Duration duration = new Duration(2, 3, 4, 5, 6, 7, 8, 9);
        new BasePeriod(instant, duration, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPeriodWithNullPeriodType() {
        new BasePeriod(new int[]{1, 2, 3}, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPeriodWithNegativeValues() {
        new BasePeriod(new int[]{-1, -2, -3, -4, -5, -6, -7, -8}, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPeriodWithIncompatibleFieldTypes() {
        BaseLocalDate start = new LocalDate(2022, 1, 1);
        BaseDateTime end = new DateTime(2022, 1, 1, 0, 0);
        new BasePeriod(start, end, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPeriodWithNonContiguousReadablePartials() {
        BaseLocalDate start = new LocalDate(2022, 1, 1);
        BaseLocalDate end = new LocalDate(2022, 1, 3);
        LocalDate intermediate = new LocalDate(2022, 1, 2);
        new BasePeriod(start, intermediate, end, PeriodType.standard());
    }

    @Test
    public void testPeriodWithSameValuesButDifferentOrder() {
        BasePeriod period1 = new BasePeriod(new int[]{3, 2, 1, 5, 2, 30, 15, 30}, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(new int[]{1, 5, 2, 3, 30, 15, 2, 30}, PeriodType.standard());
        assertEquals(period1, period2);
    }

    @Test
    public void testPeriodWithZeroValues() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[0], period.getValues());
    }

    @Test
    public void testPeriodWithEmptyValues() {
        BasePeriod period = new BasePeriod(new int[0], PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[0], period.getValues());
    }

    @Test
    public void testPeriodWithNullValues() {
        BasePeriod period = new BasePeriod(null, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[0], period.getValues());
    }
}