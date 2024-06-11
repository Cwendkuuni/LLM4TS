package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.joda.time.period.*;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testEmptyPeriod() {
        BasePeriod period = new BasePeriod(0);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, period.getValues());
    }

    @Test
    public void testPeriodCreationWithInstantAndInstant() {
        DateTimeZone utc = DateTimeZone.forID("UTC");
        BasePeriod period = new BasePeriod(new DateTime(0L, 0, 1, 0, 0, 0, 0, utc).toInstant(), new DateTime(1000L, 0, 0, 0, 0, 0, 0, utc).toInstant(), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 1, 0, 0, 0, 0, 0}, period.getValues());
    }

    @Test
    public void testPeriodCreationWithInstantAndDuration() {
        DateTimeZone utc = DateTimeZone.forID("UTC");
        BasePeriod period = new BasePeriod(new DateTime(0L, 0, 1, 0, 0, 0, 0, utc).toInstant(), new Duration(1, TimeUnit.SECONDS), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 1, 0, 0, 0, 0, 0}, period.getValues());
    }

    @Test
    public void testPeriodCreationWithInstantAndReadablePeriod() {
        DateTimeZone utc = DateTimeZone.forID("UTC");
        BasePeriod period = new BasePeriod(new DateTime(0L, 0, 1, 0, 0, 0, 0, utc).toInstant(), new Interval(1, TimeUnit.SECONDS, new DateTime(0L, 0, 0, 0, 0, 0, 0, utc), new DateTime(1000L, 0, 0, 0, 0, 0, 0, utc)), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 1, 0, 0, 0, 0, 0}, period.getValues());
    }

    @Test
    public void testPeriodCreationWithReadablePartialAndReadablePartial() {
        DateTimeZone utc = DateTimeZone.forID("UTC");
        BaseLocalDate date1 = new GregorianLocalDate(2001, 1, 1);
        BaseLocalDate date2 = new GregorianLocalDate(2001, 1, 2);
        BasePeriod period = new BasePeriod(date1, date2, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 1, 0, 0, 0, 0, 0}, period.getValues());
    }

    @Test
    public void testPeriodCreationWithDurationAndReadableInstant() {
        DateTimeZone utc = DateTimeZone.forID("UTC");
        BasePeriod period = new BasePeriod(new Duration(1, TimeUnit.HOURS), new DateTime(0L, 0, 0, 0, 0, 0, 0, utc).toInstant(), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 1, 0, 0, 0, 0, 0}, period.getValues());
    }

    @Test
    public void testPeriodCreationWithReadableDurationAndReadableInstant() {
        DateTimeZone utc = DateTimeZone.forID("UTC");
        BasePeriod period = new BasePeriod(new Duration(1, TimeUnit.HOURS), new DateTime(0L, 0, 0, 0, 0, 0, 0, utc).toInstant(), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 1, 0, 0, 0, 0, 0}, period.getValues());
    }

    @Test
    public void testPeriodCreationWithReadablePeriodAndReadablePeriod() {
        DateTimeZone utc = DateTimeZone.forID("UTC");
        BasePeriod period1 = new BasePeriod(new DateTime(0L, 0, 1, 0, 0, 0, 0, utc).toInstant(), new Interval(1, TimeUnit.DAYS, new DateTime(0L, 0, 1, 0, 0, 0, 0, utc), new DateTime(1000L, 0, 1, 0, 0, 0, 0, utc)), PeriodType.standard());
        BasePeriod period2 = new BasePeriod(new DateTime(0L, 0, 2, 0, 0, 0, 0, utc).toInstant(), new Interval(2, TimeUnit.DAYS, new DateTime(0L, 0, 2, 0, 0, 0, 0, utc), new DateTime(2000L, 0, 1, 0, 0, 0, 0, utc)), PeriodType.standard());
        BasePeriod period3 = new BasePeriod(period1, period2, PeriodType.standard());
        assertEquals(PeriodType.standard(), period3.getPeriodType());
        assertArrayEquals(new int[]{0, 0, 3, 0, 0, 0, 0, 0}, period3.getValues());
    }

    @Test
    public void testGetPeriodType() {
        DateTimeZone utc = DateTimeZone.forID("UTC");
        BasePeriod period = new BasePeriod(new DateTime(0L, 0, 1, 0, 0, 0, 0, utc).toInstant(), new DateTime(1000L, 0, 0, 0, 0, 0, 0, utc).toInstant(), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        period = new BasePeriod(new Duration(1, TimeUnit.HOURS), new DateTime(0L, 0, 0, 0, 0, 0, 0, utc).toInstant(), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        period = new BasePeriod(new Duration(1, TimeUnit.HOURS), new DateTime(0L, 0, 0, 0, 0, 0, 0, utc).toInstant(), PeriodType.time());
        assertEquals(PeriodType.time(), period.getPeriodType());
    }

    @Test
    public void testGetValue() {
        DateTimeZone utc = DateTimeZone.forID("UTC");
        BasePeriod period = new BasePeriod(new DateTime(0L, 0, 1, 0, 0, 0, 0, utc).toInstant(), new DateTime(1000L, 0, 0, 0, 0, 0, 0, utc).toInstant(), PeriodType.standard());
        assertEquals(1, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.years().getCode()));
        period = new BasePeriod(new Duration(1, TimeUnit.HOURS), new DateTime(0L, 0, 0, 0, 0, 0, 0, utc).toInstant(), PeriodType.standard());
        assertEquals(1, period.getValue(DurationFieldType.hours().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.days().getCode()));
        period = new BasePeriod(new Duration(1, TimeUnit.HOURS), new DateTime(0L, 0, 0, 0, 0, 0, 0, utc).toInstant(), PeriodType.time());
        assertEquals(1, period.getValue(DurationFieldType.millis().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.hours().getCode()));
    }

    @Test
    public void testToDurationFrom() {
        DateTimeZone utc = DateTimeZone.forID("UTC");
        BasePeriod period = new BasePeriod(new DateTime(0L, 0, 1, 0, 0, 0, 0, utc).toInstant(), new DateTime(1000L, 0, 0, 0, 0, 0, 0, utc).toInstant(), PeriodType.standard());
        Duration duration = period.toDurationFrom(new DateTime(0L, 0, 0, 0, 0, 0, 0, utc));
        assertEquals(new Duration(1, TimeUnit.DAYS), duration);
        period = new BasePeriod(new Duration(1, TimeUnit.HOURS), new DateTime(0L, 0, 0, 0, 0, 0, 0, utc).toInstant(), PeriodType.standard());
        duration = period.toDurationFrom(new DateTime(0L, 0, 0, 0, 0, 0, 0, utc));
        assertEquals(new Duration(1, TimeUnit.HOURS), duration);
        period = new BasePeriod(new Duration(1, TimeUnit.HOURS), new DateTime(0L, 0, 0, 0, 0, 0, 0, utc).toInstant(), PeriodType.time());
        duration = period.toDurationFrom(new DateTime(0L, 0, 0, 0, 0, 0, 0, utc));
        assertEquals(new Duration(1, TimeUnit.MILLISECONDS), duration);
    }

    @Test
    public void testToDurationTo() {
        DateTimeZone utc = DateTimeZone.forID("UTC");
        BasePeriod period = new BasePeriod(new DateTime(0L, 0, 1, 0, 0, 0, 0, utc).toInstant(), new DateTime(1000L, 0, 0, 0, 0, 0, 0, utc).toInstant(), PeriodType.standard());
        Duration duration = period.toDurationTo(new DateTime(0L, 0, 0, 0, 0, 0, 0, utc));
        assertEquals(new Duration(-1, TimeUnit.DAYS), duration);
        period = new BasePeriod(new Duration(1, TimeUnit.HOURS), new DateTime(0L, 0, 0, 0, 0, 0, 0, utc).toInstant(), PeriodType.standard());
        duration = period.toDurationTo(new DateTime(0L, 0, 0, 0, 0, 0, 0, utc));
        assertEquals(new Duration(-1, TimeUnit.HOURS), duration);
        period = new BasePeriod(new Duration(1, TimeUnit.HOURS), new DateTime(0L, 0, 0, 0, 0, 0, 0, utc).toInstant(), PeriodType.time());
        duration = period.toDurationTo(new DateTime(0L, 0, 0, 0, 0, 0, 0, utc));
        assertEquals(new Duration(-1, TimeUnit.MILLISECONDS), duration);
    }

    @Test
    public void testSetPeriod() {
        int[] values = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        BasePeriod period2 = new BasePeriod();
        period2.setPeriod(period);
        assertEquals(PeriodType.standard(), period2.getPeriodType());
        assertArrayEquals(values, period2.getValues());
    }

    @Test
    public void testSetPeriodInternal() {
        int[] values = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
        BasePeriod period = new BasePeriod();
        period.setPeriodInternal(new BasePeriod(values, PeriodType.standard()));
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(values, period.getValues());
    }

    @Test
    public void testSetPeriodWithNull() {
        BasePeriod period = new BasePeriod();
        period.setPeriod(null);
        assertEquals(new int[0], period.getValues());
        assertEquals(PeriodType.time(), period.getPeriodType());
    }

    @Test
    public void testMergePeriodInto() {
        int[] values1 = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
        int[] values2 = new int[]{0, 2, 4, 6, 8, 10, 12, 14};
        BasePeriod period = new BasePeriod();
        period.mergePeriodInto(period.getValues(), new BasePeriod(values1, PeriodType.standard()));
        assertArrayEquals(values1, period.getValues());
        period.mergePeriodInto(period.getValues(), new BasePeriod(values2, PeriodType.standard()));
        assertArrayEquals(new int[]{0, 3, 6, 9, 13, 15, 12, 14}, period.getValues());
    }

    @Test
    public void testAddPeriodInto() {
        int[] values1 = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
        int[] values2 = new int[]{2, 3, 4, 5, 6, 7, 8, 9};
        BasePeriod period = new BasePeriod();
        period.addPeriodInto(period.getValues(), new BasePeriod(values1, PeriodType.standard()));
        assertArrayEquals(values1, period.getValues());
        period.addPeriodInto(period.getValues(), new BasePeriod(values2, PeriodType.standard()));
        assertArrayEquals(new int[]{2, 4, 6, 8, 11, 12, 9, 10}, period.getValues());
    }
}