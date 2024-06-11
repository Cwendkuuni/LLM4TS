package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BaseDuration;
import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import org.junit.Test;

public class BasePeriodTest {

    

    @Test
    public void testConstructorWithInts() {
        Period period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
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
    public void testConstructorWithLongAndPeriodType() {
        Period period = new BasePeriod(123456789L, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        // Check values with DateTimeUtils.getInstantChronology(Instant.now()).getValues(period)
    }

    @Test
    public void testConstructorWithReadableInstantAndReadableInstant() {
        Instant start = new Instant(1516041623000L);
        Instant end = new Instant(1516041623500L);
        Period period = new BasePeriod(start, end, PeriodType.standard());
        // Check values with DateTimeUtils.getIntervalChronology(start, end).getValues(period)
    }

    @Test
    public void testConstructorWithReadablePartialAndReadablePartial() {
        LocalDateTime date1 = new LocalDateTime(2018, 1, 1, 12, 0);
        LocalDateTime date2 = new LocalDateTime(2018, 1, 1, 13, 0);
        Period period = new BasePeriod(date1, date2, PeriodType.standard());
        // Check values with DateTimeUtils.getChronology(date1.getChronology()).withUTC().getValues(period)
    }

    @Test
    public void testConstructorWithReadableInstantAndReadableDuration() {
        Instant instant = new Instant(1516041623000L);
        Duration duration = new BaseDuration(3, 4, 5, 6, 7, 8, 9, 10);
        Period period = new BasePeriod(instant, duration, PeriodType.standard());
        // Check values with DateTimeUtils.getInstantChronology(instant).get(period, instant.getMillis(), instant.getMillis() + duration.getMillis())
    }

    @Test
    public void testConstructorWithReadableDurationAndReadableInstant() {
        Duration duration = new BaseDuration(3, 4, 5, 6, 7, 8, 9, 10);
        Instant instant = new Instant(1516041623000L);
        Period period = new BasePeriod(duration, instant, PeriodType.standard());
        // Check values with DateTimeUtils.getInstantChronology(instant).get(period, instant.getMillis() - duration.getMillis(), instant.getMillis())
    }

    @Test
    public void testConstructorWithLong() {
        Period period = new BasePeriod(123456789L);
        assertEquals(PeriodType.time(), period.getPeriodType());
        // Check values with ISOChronology.getInstanceUTC().get(period, 123456789)
    }

    @Test
    public void testConstructorWithLongAndPeriodTypeAndChronology() {
        Period period = new BasePeriod(123456789L, PeriodType.standard(), DateTimeUtils.getUTC());
        // Check values with ISOChronology.getInstanceUTC().get(period, 123456789)
    }

    @Test
    public void testConstructorWithObject() {
        Period period = new BasePeriod(new Period(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard()), PeriodType.standard(), DateTimeUtils.getUTC());
        assertEquals(PeriodType.standard(), period.getPeriodType());
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
    public void testConstructorWithIntsAndPeriodType() {
        Period period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
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
    public void testGetPeriodType() {
        Period period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());

        Period period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.days());
        assertEquals(PeriodType.days(), period1.getPeriodType());

        Period period2 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.hours());
        assertEquals(PeriodType.hours(), period2.getPeriodType());
    }

    @Test
    public void testGetValue() {
        Period period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(1, period.getValue(DurationFieldType.years()));
        assertEquals(2, period.getValue(DurationFieldType.months()));
        assertEquals(3, period.getValue(DurationFieldType.weeks()));
        assertEquals(4, period.getValue(DurationFieldType.days()));
        assertEquals(5, period.getValue(DurationFieldType.hours()));
        assertEquals(6, period.getValue(DurationFieldType.minutes()));
        assertEquals(7, period.getValue(DurationFieldType.seconds()));
        assertEquals(8, period.getValue(DurationFieldType.millis()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueWithInvalidFieldType() {
        Period period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.getValue(-1);
    }

    @Test
    public void testToDurationFrom() {
        Instant instant = new Instant(1516041623000L);
        Period period = new BasePeriod(instant, PeriodType.standard());
        Duration duration = period.toDurationFrom(instant);
        assertEquals(period, duration.toPeriod());
    }

    @Test
    public void testToDurationTo() {
        Instant instant = new Instant(1516041623000L);
        Period period = new BasePeriod(instant, PeriodType.standard());
        Duration duration = period.toDurationTo(instant);
        assertEquals(new BaseDuration(-period.getValues()), duration);
    }

    @Test
    public void testCheckAndUpdate() {
        int[] values = new int[8];
        Period period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.checkAndUpdate(DurationFieldType.years(), values, 1);
        assertEquals(1, values[0]);

        period.checkAndUpdate(DurationFieldType.months(), values, 2);
        assertEquals(2, values[1]);

        period.checkAndUpdate(DurationFieldType.weeks(), values, 3);
        assertEquals(3, values[2]);

        period.checkAndUpdate(DurationFieldType.days(), values, 4);
        assertEquals(4, values[3]);

        period.checkAndUpdate(DurationFieldType.hours(), values, 5);
        assertEquals(5, values[4]);

        period.checkAndUpdate(DurationFieldType.minutes(), values, 6);
        assertEquals(6, values[5]);

        period.checkAndUpdate(DurationFieldType.seconds(), values, 7);
        assertEquals(7, values[6]);

        period.checkAndUpdate(DurationFieldType.millis(), values, 8);
        assertEquals(8, values[7]);
    }

    @Test
    public void testSetPeriod() {
        int[] values = new int[8];
        Period period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setPeriod(new BasePeriod(10, 20, 30, 40, 50, 60, 70, 80, PeriodType.standard()));
        assertArrayEquals(new int[] {10, 20, 30, 40, 50, 60, 70, 80}, period.getValues());
    }

    @Test
    public void testSetPeriodInternal() {
        int[] values = new int[8];
        Period period = new BasePeriod(values, PeriodType.standard());
        period.setPeriodInternal(new BasePeriod(10, 20, 30, 40, 50, 60, 70, 80, PeriodType.standard()));
        assertArrayEquals(new int[] {10, 20, 30, 40, 50, 60, 70, 80}, period.getValues());
    }

    @Test
    public void testSetPeriod() {
        int[] values = new int[8];
        Period period = new BasePeriod(values, PeriodType.standard());
        period.setPeriod(null);
        assertArrayEquals(new int[8], period.getValues());

        period.setPeriod(new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard()));
        assertArrayEquals(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, period.getValues());
    }

    @Test
    public void testSetValues() {
        Period period = new BasePeriod(new int[8], PeriodType.standard());
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        period.setValues(values);
        assertArrayEquals(values, period.getValues());
    }

    @Test
    public void testMergePeriod() {
        int[] values1 = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] values2 = {2, 3, 4, 5, 6, 7, 8, 9};
        Period period1 = new BasePeriod(values1, PeriodType.standard());
        Period period2 = new BasePeriod(values2, PeriodType.standard());
        period1.mergePeriod(period2);
        assertArrayEquals(new int[] {3, 5, 7, 9, 6, 7, 8, 9}, period1.getValues());
    }

    @Test
    public void testAddPeriod() {
        int[] values1 = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] values2 = {2, 3, 4, 5, 6, 7, 8, 9};
        Period period1 = new BasePeriod(values1, PeriodType.standard());
        Period period2 = new BasePeriod(values2, PeriodType.standard());
        period1.addPeriod(period2);
        assertArrayEquals(new int[] {3, 5, 7, 12, 11, 13, 15, 17}, period1.getValues());
    }

    @Test
    public void testSetField() {
        Period period = new BasePeriod(new int[8], PeriodType.standard());
        period.setField(DurationFieldType.years(), 10);
        assertEquals(10, period.getValue(DurationFieldType.years()));
    }

    @Test
    public void testSetFieldInto() {
        int[] values = new int[8];
        Period period = new BasePeriod(values, PeriodType.standard());
        period.setFieldInto(values, DurationFieldType.years(), 10);
        assertEquals(10, values[0]);
    }

    @Test
    public void testAddField() {
        Period period = new BasePeriod(new int[8], PeriodType.standard());
        period.addField(DurationFieldType.years(), 10);
        assertEquals(10, period.getValue(DurationFieldType.years()));
    }

    @Test
    public void testAddFieldInto() {
        int[] values = new int[8];
        Period period = new BasePeriod(values, PeriodType.standard());
        period.addFieldInto(values, DurationFieldType.years(), 10);
        assertEquals(10, values[0]);
    }

    
}