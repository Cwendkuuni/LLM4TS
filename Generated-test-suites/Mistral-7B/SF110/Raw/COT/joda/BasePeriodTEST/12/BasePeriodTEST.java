package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructorWithArguments() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = {1, 2, 3, 4, 5, 6, 7, 8};
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructorWithLong() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(12345678L);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = {1, 2, 3, 4, 5, 6, 7, 8};
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructorWithInstant() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDate("2015-01-01").toInstant(), DateTimeUtils.parseLocalDate("2015-01-03").toInstant(), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = new int[8];
        DateTimeUtils.getInstantChronology(DateTimeUtils.parseLocalDate("2015-01-01").toInstant()).get(period, 0L, 1L, expectedValues);
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructorWithReadablePartial() {
        PeriodType checkPeriodType = PeriodType.standard();
        BaseLocalDate baseLocalDate1 = new BaseLocalDate(DateTimeUtils.parseLocalDate("2015-01-01").getMillis(), DateTimeUtils.getUTC());
        BaseLocalDate baseLocalDate2 = new BaseLocalDate(DateTimeUtils.parseLocalDate("2015-01-03").getMillis(), DateTimeUtils.getUTC());
        BasePeriod period = new BasePeriod(baseLocalDate1, baseLocalDate2, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = new int[8];
        DateTimeUtils.getChronology(DateTimeUtils.getUTC()).get(period, baseLocalDate1.getMillis(), baseLocalDate2.getMillis(), expectedValues);
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructorWithInstantAndDuration() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(DateTimeUtils.parseLocalDate("2015-01-01").toInstant(), Duration.standardDays(2), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = new int[8];
        DateTimeUtils.getInstantChronology(DateTimeUtils.parseLocalDate("2015-01-01").toInstant()).get(period, 0L, 2L, expectedValues);
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructorWithDurationAndInstant() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(Duration.standardDays(2), DateTimeUtils.parseLocalDate("2015-01-01").toInstant(), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] expectedValues = new int[8];
        DateTimeUtils.getInstantChronology(DateTimeUtils.parseLocalDate("2015-01-01").toInstant()).get(period, -2L, 0L, expectedValues);
        assertArrayEquals(expectedValues, period.getValues());
    }

    @Test
    public void testBasePeriodConstructorWithObject() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(Duration.standardDays(2), DateTimeUtils.parseLocalDate("2015-01-01").toInstant(), checkPeriodType);
        Object periodObject = period;
        BasePeriod periodFromObject = new BasePeriod(periodObject, checkPeriodType, DateTimeUtils.getUTC());
        assertEquals(checkPeriodType, periodFromObject.getPeriodType());
        int[] expectedValues = new int[8];
        DateTimeUtils.getInstantChronology(DateTimeUtils.parseLocalDate("2015-01-01").toInstant()).get(periodFromObject, -2L, 0L, expectedValues);
        assertArrayEquals(expectedValues, periodFromObject.getValues());
    }

    @Test
    public void testBasePeriodWithIntArray() {
        PeriodType checkPeriodType = PeriodType.standard();
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(checkPeriodType, values);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertArrayEquals(values, period.getValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodWithNullIntArray() {
        PeriodType checkPeriodType = PeriodType.standard();
        int[] values = null;
        new BasePeriod(checkPeriodType, values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodWithInvalidValues() {
        PeriodType checkPeriodType = PeriodType.standard();
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        new BasePeriod(checkPeriodType, values);
    }

    @Test
    public void testGetPeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
    }

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(1, period.getValue(DurationFieldType.years().getCode()));
        assertEquals(2, period.getValue(DurationFieldType.months().getCode()));
        assertEquals(3, period.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(4, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(5, period.getValue(DurationFieldType.hours().getCode()));
        assertEquals(6, period.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(7, period.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(8, period.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testToDurationFrom() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        ReadableInstant instant = DateTimeUtils.parseLocalDate("2015-01-01").toInstant();
        Duration duration = period.toDurationFrom(instant);
        assertEquals(2, duration.getMillis());
    }

    @Test
    public void testToDurationTo() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        ReadableInstant instant = DateTimeUtils.parseLocalDate("2015-01-03").toInstant();
        Duration duration = period.toDurationTo(instant);
        assertEquals(-2, duration.getMillis());
    }

    @Test
    public void testSetPeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] values = {1, 1, 1, 1, 1, 1, 1, 1};
        period.setPeriod(new BasePeriod(values, PeriodType.standard()));
        assertArrayEquals(values, period.getValues());
    }

    @Test
    public void testSetPeriodInternal() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] values = {1, 1, 1, 1, 1, 1, 1, 1};
        period.setPeriodInternal(new BasePeriod(values, PeriodType.standard()));
        assertArrayEquals(values, period.getValues());
    }

    @Test
    public void testSetPeriodWithNull() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setPeriod(null);
        assertArrayEquals(new int[8], period.getValues());
    }

    @Test
    public void testCheckAndUpdate() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] values = new int[8];
        period.checkAndUpdate(DurationFieldType.years(), values, 1);
        assertEquals(1, values[DurationFieldType.YEARS.getCode()]);
        period.checkAndUpdate(DurationFieldType.years(), values, 2);
        assertEquals(2, values[DurationFieldType.YEARS.getCode()]);
    }

    @Test
    public void testMergePeriodInto() {
        BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(3, 4, 5, 6, 7, 8, 9, 10, PeriodType.standard());
        int[] values = new int[8];
        period1.mergePeriodInto(values, period2);
        assertArrayEquals(new int[]{4, 6, 9, 10, 7, 8, 5, 10}, values);
    }
}