package org.joda.time.base;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.field.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;

public class BasePeriodTest {

    @Test
    public void testBasePeriodWithValidArguments() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        int[] values = period.getValues();
        assertEquals(8, values.length);
        assertEquals(1, values[DurationFieldType.years().getIndex()]);
        assertEquals(2, values[DurationFieldType.months().getIndex()]);
        assertEquals(3, values[DurationFieldType.weeks().getIndex()]);
        assertEquals(4, values[DurationFieldType.days().getIndex()]);
        assertEquals(5, values[DurationFieldType.hours().getIndex()]);
        assertEquals(6, values[DurationFieldType.minutes().getIndex()]);
        assertEquals(7, values[DurationFieldType.seconds().getIndex()]);
        assertEquals(8, values[DurationFieldType.millis().getIndex()]);
    }

    @Test
    public void testBasePeriodWithNullArguments() {
        try {
            new BasePeriod(null, null, null, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBasePeriodWithInvalidArguments() {
        try {
            new BasePeriod("invalid", "arguments", null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBasePeriodWithValidInstantAndDuration() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(DateTimeUtils.currentTime(), Duration.standardHours(2), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(2, period.getValues()[DurationFieldType.hours().getIndex()]);
    }

    @Test
    public void testBasePeriodWithValidReadablePeriods() {
        PeriodType checkPeriodType = PeriodType.standard();
        BaseLocal dateTime1 = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);
        BaseLocal dateTime2 = new DateTime(2022, 1, 1, 14, 0, 0, 0, DateTimeZone.UTC);
        BasePeriod period = new BasePeriod(dateTime1, dateTime2, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(2, period.getValues()[DurationFieldType.hours().getIndex()]);
    }

    @Test
    public void testBasePeriodWithValidReadablePartialAndReadablePartial2() {
        PeriodType checkPeriodType = PeriodType.standard();
        BaseLocal dateTime1 = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);
        BaseLocal dateTime2 = new DateTime(2022, 1, 1, 14, 0, 0, 0, DateTimeZone.UTC);
        BasePeriod period = new BasePeriod(dateTime1.toLocalDate(), dateTime2.toLocalDate(), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(2, period.getValues()[DurationFieldType.hours().getIndex()]);
    }

    @Test
    public void testBasePeriodWithNullReadablePartialOrReadablePartial2() {
        try {
            new BasePeriod(null, null, PeriodType.standard());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new BasePeriod(null, new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC), PeriodType.standard());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBasePeriodWithInvalidReadablePartialOrReadablePartial2() {
        try {
            new BasePeriod(new DateTime(2022, 13, 1, 12, 0, 0, 0, DateTimeZone.UTC), new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC), PeriodType.standard());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new BasePeriod(new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC), new DateTime(2022, 1, 1, 12, 1, 0, 0, DateTimeZone.UTC), PeriodType.standard());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBasePeriodWithValidReadableInstantAndReadableDuration() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(DateTimeUtils.currentTime(), Duration.standardDays(3), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(3, period.getValues()[DurationFieldType.days().getIndex()]);
    }

    @Test
    public void testBasePeriodWithValidReadableDurationAndReadableInstant() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(Duration.standardDays(3), DateTimeUtils.currentTime(), checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(-3, period.getValues()[DurationFieldType.days().getIndex()]);
    }

    @Test
    public void testBasePeriodWithLong() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(1234567890L, checkPeriodType, DateTimeUtils.UTC);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(1, period.getValues()[DurationFieldType.years().getIndex()]);
        assertEquals(2, period.getValues()[DurationFieldType.months().getIndex()]);
        assertEquals(3, period.getValues()[DurationFieldType.weeks().getIndex()]);
        assertEquals(4, period.getValues()[DurationFieldType.days().getIndex()]);
        assertEquals(5, period.getValues()[DurationFieldType.hours().getIndex()]);
        assertEquals(6, period.getValues()[DurationFieldType.minutes().getIndex()]);
        assertEquals(7, period.getValues()[DurationFieldType.seconds().getIndex()]);
        assertEquals(8, period.getValues()[DurationFieldType.millis().getIndex()]);
    }

    @Test
    public void testBasePeriodWithValidLongAndPeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(1234567890L, checkPeriodType, null);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(1, period.getValues()[DurationFieldType.years().getIndex()]);
        assertEquals(2, period.getValues()[DurationFieldType.months().getIndex()]);
        assertEquals(3, period.getValues()[DurationFieldType.weeks().getIndex()]);
        assertEquals(4, period.getValues()[DurationFieldType.days().getIndex()]);
        assertEquals(5, period.getValues()[DurationFieldType.hours().getIndex()]);
        assertEquals(6, period.getValues()[DurationFieldType.minutes().getIndex()]);
        assertEquals(7, period.getValues()[DurationFieldType.seconds().getIndex()]);
        assertEquals(8, period.getValues()[DurationFieldType.millis().getIndex()]);
    }

    @Test
    public void testBasePeriodWithValidObjectAndPeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(DateTime.parse("2022-01-01T12:00:00.123Z"), checkPeriodType, null);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(0, period.getValues()[DurationFieldType.years().getIndex()]);
        assertEquals(0, period.getValues()[DurationFieldType.months().getIndex()]);
        assertEquals(0, period.getValues()[DurationFieldType.weeks().getIndex()]);
        assertEquals(0, period.getValues()[DurationFieldType.days().getIndex()]);
        assertEquals(12, period.getValues()[DurationFieldType.hours().getIndex()]);
        assertEquals(0, period.getValues()[DurationFieldType.minutes().getIndex()]);
        assertEquals(0, period.getValues()[DurationFieldType.seconds().getIndex()]);
        assertEquals(123, period.getValues()[DurationFieldType.millis().getIndex()]);
    }

    @Test
    public void testBasePeriodWithValidIntArrayAndPeriodType() {
        PeriodType checkPeriodType = PeriodType.standard();
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, checkPeriodType);
        assertEquals(checkPeriodType, period.getPeriodType());
        assertEquals(1, period.getValues()[DurationFieldType.years().getIndex()]);
        assertEquals(2, period.getValues()[DurationFieldType.months().getIndex()]);
        assertEquals(3, period.getValues()[DurationFieldType.weeks().getIndex()]);
        assertEquals(4, period.getValues()[DurationFieldType.days().getIndex()]);
        assertEquals(5, period.getValues()[DurationFieldType.hours().getIndex()]);
        assertEquals(6, period.getValues()[DurationFieldType.minutes().getIndex()]);
        assertEquals(7, period.getValues()[DurationFieldType.seconds().getIndex()]);
        assertEquals(8, period.getValues()[DurationFieldType.millis().getIndex()]);
    }
}