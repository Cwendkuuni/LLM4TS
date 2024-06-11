package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructorWithValues() {
        PeriodType periodType = PeriodType.standard();
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, periodType);

        assertEquals(periodType, period.getPeriodType());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], period.getValue(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodConstructorWithNullValues() {
        PeriodType periodType = PeriodType.standard();
        int[] values = null;
        new BasePeriod(values, periodType);
    }

    @Test
    public void testBasePeriodConstructorWithPeriodType() {
        PeriodType periodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(0, periodType);

        assertEquals(periodType, period.getPeriodType());
        for (int i = 0; i < period.size(); i++) {
            assertEquals(0, period.getValue(i));
        }
    }

    @Test
    public void testBasePeriodConstructorWithInstantAndPeriodType() {
        PeriodType periodType = PeriodType.standard();
        long instantMillis = 1234567890L;
        BasePeriod period = new BasePeriod(instantMillis, periodType, DateTimeUtils.UTC);

        assertEquals(periodType, period.getPeriodType());
        for (int i = 0; i < period.size(); i++) {
            assertEquals(0, period.getValue(i));
        }
    }

    @Test
    public void testBasePeriodConstructorWithReadableInstantAndPeriodType() {
        PeriodType periodType = PeriodType.standard();
        ReadableInstant instant = new DateTime(1234567890L, DateTimeZone.UTC);
        BasePeriod period = new BasePeriod(instant, periodType);

        assertEquals(periodType, period.getPeriodType());
        for (int i = 0; i < period.size(); i++) {
            assertEquals(0, period.getValue(i));
        }
    }

    @Test
    public void testBasePeriodConstructorWithReadablePartialAndPeriodType() {
        PeriodType periodType = PeriodType.standard();
        BaseLocal partial = new DateTime(1234567890L, DateTimeZone.UTC).withTimeAtStartOfDay();
        BasePeriod period = new BasePeriod(partial, periodType);

        assertEquals(periodType, period.getPeriodType());
        for (int i = 0; i < period.size(); i++) {
            assertEquals(0, period.getValue(i));
        }
    }

    @Test
    public void testBasePeriodConstructorWithReadableInstantAndReadableDurationAndPeriodType() {
        PeriodType periodType = PeriodType.standard();
        ReadableInstant instant = new DateTime(1234567890L, DateTimeZone.UTC);
        ReadableDuration duration = new Duration(1, 2, 3, 4, 5, 6, 7, 8, DateTimeUnits.MILLISECONDS);
        BasePeriod period = new BasePeriod(instant, duration, periodType);

        assertEquals(periodType, period.getPeriodType());
        for (int i = 0; i < period.size(); i++) {
            assertEquals(period.getValue(i), duration.getValues()[i]);
        }
    }

    @Test
    public void testBasePeriodConstructorWithReadableDurationAndReadableInstantAndPeriodType() {
        PeriodType periodType = PeriodType.standard();
        ReadableDuration duration = new Duration(1, 2, 3, 4, 5, 6, 7, 8, DateTimeUnits.MILLESECONDS);
        ReadableInstant instant = new DateTime(1234567890L, DateTimeZone.UTC);
        BasePeriod period = new BasePeriod(duration, instant, periodType);

        assertEquals(periodType, period.getPeriodType());
        for (int i = 0; i < period.size(); i++) {
            assertEquals(-period.getValue(i), duration.getValues()[i]);
        }
    }

    @Test
    public void testBasePeriodConstructorWithLongAndPeriodTypeAndChronology() {
        PeriodType periodType = PeriodType.standard();
        long periodMillis = 1234567890L;
        Chronology chronology = DateTimeUtils.UTC;
        BasePeriod period = new BasePeriod(periodMillis, periodType, chronology);

        assertEquals(periodType, period.getPeriodType());
        for (int i = 0; i < period.size(); i++) {
            assertEquals((int) (periodMillis / chronology.getMillisPerDay() * chronology.getMillisPerDay() / 1000L) % 60, period.getValue(i));
        }
    }

    @Test
    public void testBasePeriodConstructorWithObjectAndPeriodTypeAndChronology() {
        PeriodType periodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(new Duration(1, 2, 3, 4, 5, 6, 7, 8, DateTimeUnits.MILLISECONDS), periodType, DateTimeUtils.UTC);

        assertEquals(periodType, period.getPeriodType());
        for (int i = 0; i < period.size(); i++) {
            assertEquals(period.getValue(i), ((Duration) period.toDurationFrom(new DateTime())).getValues()[i]);
        }
    }

    @Test
    public void testBasePeriodConstructorWithIntsAndPeriodType() {
        PeriodType periodType = PeriodType.standard();
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, periodType);

        assertEquals(periodType, period.getPeriodType());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], period.getValue(i));
        }
    }

    @Test
    public void testBasePeriodGetPeriodType() {
        PeriodType periodType = PeriodType.standard();
        BasePeriod period = new BasePeriod(new int[8], periodType);
        assertEquals(periodType, period.getPeriodType());
    }

    @Test
    public void testBasePeriodGetValue() {
        PeriodType periodType = PeriodType.standard();
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, periodType);

        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], period.getValue(i));
        }
    }

    @Test
    public void testBasePeriodToDurationFrom() {
        PeriodType periodType = PeriodType.standard();
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, periodType);

        ReadableInstant instant = new DateTime(1234567890L, DateTimeZone.UTC);
        Duration duration = period.toDurationFrom(instant);

        assertEquals(duration.getMillis(), 1234567890L + duration.getPeriod().getMillis());
    }

    @Test
    public void testBasePeriodToDurationTo() {
        PeriodType periodType = PeriodType.standard();
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, periodType);

        ReadableInstant instant = new DateTime(1234567890L, DateTimeZone.UTC);
        Duration duration = period.toDurationTo(instant);

        assertEquals(duration.getMillis(), 1234567890L - duration.getPeriod().getMillis());
    }

    @Test
    public void testBasePeriodSetPeriod() {
        PeriodType periodType = PeriodType.standard();
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, periodType);

        ReadablePeriod periodInternal = new BasePeriod(new int[] {2, 3, 4, 5, 6, 7, 8, 9}, periodType);
        period.setPeriod(periodInternal);

        for (int i = 0; i < periodInternal.size(); i++) {
            assertEquals(periodInternal.getValue(i), period.getValue(i));
        }
    }

    @Test
    public void testBasePeriodSetPeriodInternal() {
        PeriodType periodType = PeriodType.standard();
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, periodType);

        ReadablePeriod periodInternal = new BasePeriod(new int[] {2, 3, 4, 5, 6, 7, 8, 9}, periodType);
        period.setPeriodInternal(periodInternal);

        for (int i = 0; i < periodInternal.size(); i++) {
            assertEquals(periodInternal.getValue(i), period.getValue(i));
        }
    }

    @Test
    public void testBasePeriodSetPeriodWithNull() {
        PeriodType periodType = PeriodType.standard();
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, periodType);

        period.setPeriod(null);

        for (int i = 0; i < values.length; i++) {
            assertEquals(0, period.getValue(i));
        }
    }

    @Test
    public void testBasePeriodSetValues() {
        PeriodType periodType = PeriodType.standard();
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod period = new BasePeriod(values, periodType);

        int[] newValues = {2, 3, 4, 5, 6, 7, 8, 9};
        period.setValues(newValues);

        for (int i = 0; i < newValues.length; i++) {
            assertEquals(newValues[i], period.getValue(i));
        }
    }
}