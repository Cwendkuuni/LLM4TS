package org.joda.time.base;

import org.junit.Test;
import org.junit.Assert.*;

public class BasePeriodTest {

    

    @Test
    public void testBasePeriodConstructionWithInts() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        int[] iValues = period.getValues();

        assertEquals(PeriodType.standard(), periodType);
        Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 0, 0, 0, 0, 0, 0, 0, 0}, iValues);
    }

    @Test
    public void testBasePeriodConstructionWithLongsAndPeriodType() {
        BasePeriod period = new BasePeriod(1L, 2L, PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        int[] iValues = period.getValues();

        assertEquals(PeriodType.standard(), periodType);
        Assert.assertArrayEquals(new int[]{0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, iValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadableInstantAndReadableInstant() {
        BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-01-03"), PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        int[] iValues = period.getValues();

        assertEquals(PeriodType.standard(), periodType);
        Assert.assertArrayEquals(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 31, 0, 0, 0}, iValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadablePartialAndReadablePartial() {
        BasePeriod period = new BasePeriod(DateTimeUtils.localDate(2022, 1, 1), DateTimeUtils.localDate(2022, 1, 3), PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        int[] iValues = period.getValues();

        assertEquals(PeriodType.standard(), periodType);
        Assert.assertArrayEquals(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 31, 0, 0, 0}, iValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadableInstantAndReadableDuration() {
        BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), new Duration(1, TimeUnit.DAYS), PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        int[] iValues = period.getValues();

        assertEquals(PeriodType.standard(), periodType);
        Assert.assertArrayEquals(new int[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, iValues);
    }

    @Test
    public void testBasePeriodConstructionWithReadableDurationAndReadableInstant() {
        BasePeriod period = new BasePeriod(new Duration(1, TimeUnit.DAYS), DateTimeUtils.parseLocalDate("2022-01-01"), PeriodType.standard());
        PeriodType periodType = period.getPeriodType();
        int[] iValues = period.getValues();

        assertEquals(PeriodType.standard(), periodType);
        Assert.assertArrayEquals(new int[]{0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, iValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructionWithNullReadableInstant() {
        new BasePeriod(null, DateTimeUtils.parseLocalDate("2022-01-01"), PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructionWithNullReadablePartial() {
        new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), null, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructionWithNullReadableDuration() {
        new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), null, PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructionWithNullPeriodType() {
        new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructionWithNullChronology() {
        new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-01-03"), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructionWithNullObject() {
        new BasePeriod(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructionWithDifferentReadablePartialClasses() {
        new BasePeriod(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDateTimeOfInstant("2022-01-01T12:00:00.000Z"), PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructionWithReadablePartialObjectsWithDifferentChronologies() {
        new BasePeriod(DateTimeUtils.localDate(2022, 1, 1, DateTimeZone.forID("America/New_York")), DateTimeUtils.localDate(2022, 1, 3, DateTimeZone.forID("Europe/London")), PeriodType.standard());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructionWithReadablePartialObjectsWithDifferentFieldTypes() {
        new BasePeriod(DateTimeUtils.localDate(2022, 1, 1), new DateTime(2022, 1, 1, 12, 0), PeriodType.standard());
    }

    @Test
    public void testBasePeriodGetPeriodType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        PeriodType periodType = period.getPeriodType();

        assertEquals(PeriodType.standard(), periodType);
    }

    @Test
    public void testBasePeriodgetValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());

        assertEquals(1, period.getValue(DurationFieldType.years()));
        assertEquals(2, period.getValue(DurationFieldType.months()));
        assertEquals(3, period.getValue(DurationFieldType.weeks()));
        assertEquals(4, period.getValue(DurationFieldType.days()));
        assertEquals(5, period.getValue(DurationFieldType.hours()));
        assertEquals(6, period.getValue(DurationFieldType.minutes()));
        assertEquals(7, period.getValue(DurationFieldType.seconds()));
        assertEquals(8, period.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testBasePeriodToDurationFrom() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration = period.toDurationFrom(DateTimeUtils.parseLocalDate("2022-01-01"));

        assertEquals(new Duration(1, TimeUnit.DAYS), duration);
    }

    @Test
    public void testBasePeriodToDurationTo() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration = period.toDurationTo(DateTimeUtils.parseLocalDate("2022-01-01"));

        assertEquals(new Duration(-1, TimeUnit.DAYS), duration);
    }

    
}