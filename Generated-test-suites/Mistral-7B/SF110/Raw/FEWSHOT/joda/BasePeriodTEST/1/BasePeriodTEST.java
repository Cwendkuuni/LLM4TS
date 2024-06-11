package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.*;
import org.joda.time.field.*;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructionWithLong() {
        BasePeriod period = new BasePeriod(1234567890L);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(7, period.size());
        assertEquals(0, period.getValue(DurationFieldType.years().getCode()));
        assertEquals(1, period.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(2, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(3, period.getValue(DurationFieldType.hours().getCode()));
        assertEquals(4, period.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(5, period.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(6, period.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testBasePeriodConstructionWithLongAndChronology() {
        BasePeriod period = new BasePeriod(1234567890L, ISOChronology.getInstanceUTC());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(7, period.size());
        assertEquals(0, period.getValue(DurationFieldType.years().getCode()));
        assertEquals(1, period.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(2, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(3, period.getValue(DurationFieldType.hours().getCode()));
        assertEquals(4, period.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(5, period.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(6, period.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testBasePeriodConstructionWithReadableInstant() {
        Instant instant = new DateTime(2023, 5, 12, 15, 30, 0, 0).toInstant();
        BasePeriod period = new BasePeriod(instant, PeriodType.standard(), DateTimeUtils.UTC);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(7, period.size());
        assertEquals(0, period.getValue(DurationFieldType.years().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(12, period.getValue(DurationFieldType.hours().getCode()));
        assertEquals(30, period.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testBasePeriodConstructionWithReadablePartial() {
        BaseLocalDateTime localDateTime = new DateTime(2023, 5, 12, 15, 30, 0, 0, DateTimeZone.UTC);
        BasePeriod period = new BasePeriod(localDateTime, localDateTime.plus(Period.days(1)), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(7, period.size());
        assertEquals(0, period.getValue(DurationFieldType.years().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(1, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.hours().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testBasePeriodConstructionWithReadableInstantAndReadableDuration() {
        Instant instant = new DateTime(2023, 5, 12, 15, 30, 0, 0).toInstant();
        Duration duration = Duration.standardHours(1);
        BasePeriod period = new BasePeriod(instant, duration, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(7, period.size());
        assertEquals(0, period.getValue(DurationFieldType.years().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(1, period.getValue(DurationFieldType.hours().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testBasePeriodConstructionWithReadableDurationAndReadableInstant() {
        Instant instant = new DateTime(2023, 5, 12, 15, 30, 0, 0).toInstant();
        Duration duration = Duration.standardHours(1);
        BasePeriod period = new BasePeriod(duration, instant, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(7, period.size());
        assertEquals(0, period.getValue(DurationFieldType.years().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.hours().getCode()));
        assertEquals(1, period.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(0, period.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(-15 * 60 * 60 * 1000L, period.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testBasePeriodConstructionWithObject() {
        Period period = Period.days(1);
        BasePeriod basePeriod = new BasePeriod(period, PeriodType.standard(), DateTimeUtils.UTC);
        assertEquals(PeriodType.standard(), basePeriod.getPeriodType());
        assertEquals(7, basePeriod.size());
        assertEquals(0, basePeriod.getValue(DurationFieldType.years().getCode()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(1, basePeriod.getValue(DurationFieldType.days().getCode()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.hours().getCode()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(0, basePeriod.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testBasePeriodConstructionWithIntArrayAndPeriodType() {
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(values, period.iValues);
    }

    @Test
    public void testBasePeriodGetPeriodType() {
        BasePeriod period = new BasePeriod(1234567890L, ISOChronology.getInstanceUTC());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testBasePeriodGetValue() {
        BasePeriod period = new BasePeriod(1234567890L, ISOChronology.getInstanceUTC());
        assertEquals(0, period.getValue(DurationFieldType.years().getCode()));
        assertEquals(1, period.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(2, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(3, period.getValue(DurationFieldType.hours().getCode()));
        assertEquals(4, period.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(5, period.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(6, period.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testBasePeriodToDurationFrom() {
        Instant instant = new DateTime(2023, 5, 12, 15, 30, 0, 0).toInstant();
        BasePeriod period = new BasePeriod(instant, PeriodType.standard(), DateTimeUtils.UTC);
        Duration duration = period.toDurationFrom(instant);
        assertEquals(Duration.standardHours(1), duration);
    }

    @Test
    public void testBasePeriodToDurationTo() {
        Instant instant = new DateTime(2023, 5, 12, 15, 30, 0, 0).toInstant();
        BasePeriod period = new BasePeriod(instant, PeriodType.standard(), DateTimeUtils.UTC);
        Duration duration = period.toDurationTo(instant.plus(Duration.standardHours(1)));
        assertEquals(Duration.standardHours(1), duration);
    }

    @Test
    public void testBasePeriodSetPeriod() {
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        BasePeriod newPeriod = new BasePeriod();
        period.setPeriod(newPeriod);
        assertEquals(0, newPeriod.size());
    }

    @Test
    public void testBasePeriodSetPeriodInternal() {
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        BasePeriod newPeriod = new BasePeriod();
        period.setPeriodInternal(newPeriod);
        assertEquals(7, newPeriod.size());
        assertEquals(1, newPeriod.getValue(DurationFieldType.years().getCode()));
        assertEquals(2, newPeriod.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(3, newPeriod.getValue(DurationFieldType.days().getCode()));
        assertEquals(4, newPeriod.getValue(DurationFieldType.hours().getCode()));
        assertEquals(5, newPeriod.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(6, newPeriod.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(7, newPeriod.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testBasePeriodSetPeriodWithNull() {
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        period.setPeriod(null);
        assertEquals(0, period.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodSetPeriodWithInvalidFields() {
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        period.setPeriod(new BasePeriod() {{
            setField(DurationFieldType.years(), 12);
            setField(DurationFieldType.days(), 30);
        }});
    }

    @Test
    public void testBasePeriodAddField() {
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        period.addField(DurationFieldType.days(), 1);
        assertEquals(2, period.size());
        assertEquals(1, period.getValue(DurationFieldType.years().getCode()));
        assertEquals(3, period.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(5, period.getValue(DurationFieldType.days().getCode()));
        assertEquals(6, period.getValue(DurationFieldType.hours().getCode()));
        assertEquals(7, period.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(8, period.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(9, period.getValue(DurationFieldType.millis().getCode()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodAddFieldWithInvalidField() {
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        period.addField(DurationFieldType.years(), 12);
    }

    @Test
    public void testBasePeriodMergePeriod() {
        int[] values1 = {1, 2, 3, 4, 5, 6, 7};
        BasePeriod period1 = new BasePeriod(values1, PeriodType.standard());
        int[] values2 = {2, 3, 4, 5, 6, 7, 8};
        BasePeriod period2 = new BasePeriod(values2, PeriodType.standard());
        period1.mergePeriod(period2);
        assertEquals(7, period1.size());
        assertEquals(1, period1.getValue(DurationFieldType.years().getCode()));
        assertEquals(3, period1.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(6, period1.getValue(DurationFieldType.days().getCode()));
        assertEquals(6, period1.getValue(DurationFieldType.hours().getCode()));
        assertEquals(7, period1.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(8, period1.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(9, period1.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testBasePeriodAddPeriod() {
        int[] values1 = {1, 2, 3, 4, 5, 6, 7};
        BasePeriod period1 = new BasePeriod(values1, PeriodType.standard());
        int[] values2 = {2, 3, 4, 5, 6, 7, 8};
        BasePeriod period2 = new BasePeriod(values2, PeriodType.standard());
        period1.addPeriod(period2);
        assertEquals(7, period1.size());
        assertEquals(1, period1.getValue(DurationFieldType.years().getCode()));
        assertEquals(3, period1.getValue(DurationFieldType.weeks().getCode()));
        assertEquals(8, period1.getValue(DurationFieldType.days().getCode()));
        assertEquals(6, period1.getValue(DurationFieldType.hours().getCode()));
        assertEquals(7, period1.getValue(DurationFieldType.minutes().getCode()));
        assertEquals(8, period1.getValue(DurationFieldType.seconds().getCode()));
        assertEquals(9, period1.getValue(DurationFieldType.millis().getCode()));
    }

    @Test
    public void testBasePeriodSetValue() {
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        period.setValue(DurationFieldType.days().getCode(), 10);
        assertEquals(1, period.size());
        assertEquals(10, period.getValue(DurationFieldType.days().getCode()));
    }

    @Test
    public void testBasePeriodSetValues() {
        int[] values = {1, 2, 3, 4, 5, 6, 7};
        BasePeriod period = new BasePeriod(values, PeriodType.standard());
        int[] newValues = {11, 12, 13, 14, 15, 16, 17};
        period.setValues(newValues);
        assertEquals(1, period.size());
        assertEquals(11, period.getValue(DurationFieldType.days().getCode()));
    }
}