package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BaseDuration;
import org.joda.time.BaseLocalDateTime;
import org.joda.time.BasePeriod;
import org.joda.time.DateTimeUtils;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructionWithValues() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
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
    public void testBasePeriodConstructionWithInstant() {
        BasePeriod period = new BasePeriod(new BaseLocalDateTime(), new BaseLocalDateTime(), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testBasePeriodConstructionWithReadablePeriod() {
        BaseLocalDateTime start = new BaseLocalDateTime();
        BaseLocalDateTime end = new BaseLocalDateTime();
        BasePeriod period = new BasePeriod(start, end, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testBasePeriodConstructionWithInstantAndDuration() {
        BasePeriod period = new BasePeriod(new BaseLocalDateTime(), new Duration(1, 2, 3, 4, 5, 6, 7), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testBasePeriodConstructionWithDurationAndInstant() {
        BasePeriod period = new BasePeriod(new Duration(1, 2, 3, 4, 5, 6, 7), new BaseLocalDateTime(), PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testBasePeriodConstructionWithObject() {
        BasePeriod period = new BasePeriod(new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard()), PeriodType.standard(), null);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testBasePeriodConstructionWithIntArray() {
        BasePeriod period = new BasePeriod(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testBasePeriodGetPeriodType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testBasePeriodGetValue() {
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
        Duration duration = period.toDurationFrom(new BaseLocalDateTime());
        assertEquals(new Duration(1, 2, 3, 4, 5, 6, 7, 8), duration);
    }

    @Test
    public void testBasePeriodToDurationTo() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration = period.toDurationTo(new BaseLocalDateTime());
        assertEquals(new Duration(-1, -2, -3, -4, -5, -6, -7, -8), duration);
    }

    @Test
    public void testBasePeriodCheckAndUpdate() {
        BasePeriod period = new BasePeriod();
        period.checkAndUpdate(DurationFieldType.years(), new int[]{0}, 1);
        assertEquals(1, period.getValue(DurationFieldType.years()));
        period.checkAndUpdate(DurationFieldType.years(), new int[]{1}, 2);
        assertEquals(2, period.getValue(DurationFieldType.years()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasePeriodCheckAndUpdateWithIllegalArgument() {
        BasePeriod period = new BasePeriod();
        period.checkAndUpdate(DurationFieldType.years(), new int[]{1}, 0);
    }

    @Test
    public void testBasePeriodSetPeriod() {
        BasePeriod period = new BasePeriod();
        period.setPeriod(new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard()));
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
    public void testBasePeriodSetPeriodInternal() {
        BasePeriod period = new BasePeriod();
        period.setPeriodInternal(new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard()));
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
    public void testBasePeriodMergePeriodInto() {
        int[] values1 = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] values2 = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
        int[] result = BasePeriod.mergePeriodInto(values1, new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard()));
        assertArrayEquals(new int[]{2, 3, 4, 5, 6, 7, 8, 8}, result);
    }

    @Test
    public void testBasePeriodAddPeriodInto() {
        int[] values1 = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] values2 = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] result = BasePeriod.addPeriodInto(values1, new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard()));
        assertArrayEquals(new int[]{2, 3, 4, 5, 6, 7, 8, 16}, result);
    }

    @Test
    public void testBasePeriodSetField() {
        BasePeriod period = new BasePeriod();
        period.setField(DurationFieldType.years(), 1);
        assertEquals(1, period.getValue(DurationFieldType.years()));
    }

    @Test
    public void testBasePeriodSetFieldInto() {
        int[] values = new int[]{0};
        period.setFieldInto(values, DurationFieldType.years(), 1);
        assertEquals(1, values[0]);
    }

    @Test
    public void testBasePeriodAddField() {
        BasePeriod period = new BasePeriod();
        period.addField(DurationFieldType.years(), 1);
        assertEquals(1, period.getValue(DurationFieldType.years()));
    }

    @Test
    public void testBasePeriodAddFieldInto() {
        int[] values = new int[]{0};
        period.addFieldInto(values, DurationFieldType.years(), 1);
        assertEquals(1, values[0]);
    }
}