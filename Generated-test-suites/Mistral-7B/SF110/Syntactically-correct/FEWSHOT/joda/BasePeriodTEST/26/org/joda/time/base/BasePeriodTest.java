package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstruction() {
        // Test default constructor
        BasePeriod period = new BasePeriod();
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(new int[8], period.iValues);

        // Test constructor with values
        int[] values = {3, 2, 1, 2, 3, 4, 5, 6};
        BasePeriod period2 = new BasePeriod(values, PeriodType.standard());
        assertEquals(PeriodType.standard(), period2.getPeriodType());
        assertArrayEquals(values, period2.iValues);

        // Test constructor with PeriodType and Chronology
        Chronology chrono = GregorianChronology.getInstance();
        BasePeriod period3 = new BasePeriod(123456789, PeriodType.standard(), chrono);
        assertEquals(PeriodType.standard(), period3.getPeriodType());
        assertArrayEquals(chrono.get(BasePeriod.DUMMY_PERIOD, 123456789), period3.iValues);
    }

    @Test
    public void testBasePeriodConstructorsWithReadableArguments() {
        // Test constructor with ReadableInstant and ReadableInstant
        DateTime date1 = new DateTime();
        DateTime date2 = new DateTime(date1.getMillis() + 1000);
        BasePeriod period = new BasePeriod(date1, date2, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(DateTimeUtils.getDifferenceAsMillis(date1, date2), period.iValues);

        // Test constructor with ReadablePartial and ReadablePartial
        BaseLocalDate date3 = new LocalDate();
        BaseLocalDate date4 = new LocalDate(date3.getYear(), date3.getMonthOfYear(), date3.getDayOfMonth().getValue() + 1);
        BasePeriod period2 = new BasePeriod(date3, date4, PeriodType.standard());
        assertEquals(PeriodType.standard(), period2.getPeriodType());
        assertArrayEquals(DateTimeUtils.getDifferenceAsMillis(date3.toDate(), date4.toDate()), period2.iValues);

        // Test constructor with ReadableInstant and ReadableDuration
        DateTime date5 = new DateTime();
        Duration duration = new Duration(1, TimeUnit.DAYS);
        BasePeriod period3 = new BasePeriod(date5, duration, PeriodType.standard());
        assertEquals(PeriodType.standard(), period3.getPeriodType());
        assertEquals(duration, period3.toDurationFrom(date5));

        // Test constructor with ReadableDuration and ReadableInstant
        Duration duration2 = new Duration(1, TimeUnit.DAYS);
        DateTime date6 = new DateTime();
        BasePeriod period4 = new BasePeriod(duration2, date6, PeriodType.standard());
        assertEquals(PeriodType.standard(), period4.getPeriodType());
        assertEquals(duration2, period4.toDurationTo(date6));

        // Test constructor with long
        long millis = 123456789;
        BasePeriod period5 = new BasePeriod(millis);
        assertEquals(PeriodType.time(), period5.getPeriodType());
        assertArrayEquals(ISOChronology.getInstanceUTC().get(BasePeriod.DUMMY_PERIOD, millis), period5.iValues);

        // Test constructor with long and PeriodType
        long millis2 = 123456789;
        BasePeriod period6 = new BasePeriod(millis2, PeriodType.standard());
        assertEquals(PeriodType.standard(), period6.getPeriodType());
        assertArrayEquals(ISOChronology.getInstanceUTC().get(BasePeriod.DUMMY_PERIOD, millis2), period6.iValues);

        // Test constructor with Object, PeriodType, and Chronology
        LocalDate date7 = new LocalDate();
        BasePeriod period7 = new BasePeriod(date7, PeriodType.standard(), ChronoUtils.UTC);
        assertEquals(PeriodType.standard(), period7.getPeriodType());
        assertArrayEquals(ISOChronology.getInstanceUTC().get(BasePeriod.DUMMY_PERIOD, DateTime.parse(date7.toString()).getMillis()), period7.iValues);

        // Test constructor with int[] and PeriodType
        int[] values2 = {3, 2, 1, 2, 3, 4, 5, 6};
        BasePeriod period8 = new BasePeriod(values2, PeriodType.standard());
        assertEquals(PeriodType.standard(), period8.getPeriodType());
        assertArrayEquals(values2, period8.iValues);
    }

    @Test
    public void testToDurationMethods() {
        DateTime date1 = new DateTime();
        DateTime date2 = new DateTime(date1.getMillis() + 1000);
        BasePeriod period = new BasePeriod(date1, date2, PeriodType.standard());
        Duration duration1 = period.toDurationFrom(date1);
        Duration duration2 = period.toDurationTo(date2);
        assertEquals(duration1, duration2);
        assertEquals(1, duration1.getSeconds());
    }

    @Test
    public void testSetPeriod() {
        int[] values = {3, 2, 1, 2, 3, 4, 5, 6};
        BasePeriod period = new BasePeriod();
        period.setPeriod(new BasePeriod(values, PeriodType.standard()));
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertArrayEquals(values, period.iValues);
    }

    @Test
    public void testSetField() {
        BasePeriod period = new BasePeriod();
        period.setField(DurationFieldType.days(), 3);
        assertEquals(3, period.getValue(DurationFieldType.days().getIndex()));
    }

    @Test
    public void testAddField() {
        BasePeriod period = new BasePeriod();
        period.addField(DurationFieldType.days(), 3);
        assertEquals(3, period.getValue(DurationFieldType.days().getIndex()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFieldInvalidField() {
        BasePeriod period = new BasePeriod();
        period.addField(DurationFieldType.MONTHS(), 3);
    }

    @Test
    public void testMergePeriod() {
        int[] values1 = {3, 2, 1, 2, 3, 4, 5, 6};
        int[] values2 = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period1 = new BasePeriod(values1, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(values2, PeriodType.standard());

        period1.mergePeriod(period2);
        assertArrayEquals(values2, period1.iValues);
    }

    @Test
    public void testAddPeriod() {
        int[] values1 = {3, 2, 1, 2, 3, 4, 5, 6};
        int[] values2 = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period1 = new BasePeriod(values1, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(values2, PeriodType.standard());

        period1.addPeriod(period2);
        assertArrayEquals(values1, period1.iValues);
        assertArrayEquals(values2, period2.iValues);

        period2.addPeriod(period1);
        assertArrayEquals(values1, period2.iValues);
    }
}