package org.joda.time.base;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(1, period.size());
        assertFalse(period.isSupported(DurationFieldType.MILLISECONDS));
        Period p = period.toPeriod();
        assertEquals(5, p.getValues()[0]);
        assertEquals(PeriodType.fixed, p.getPeriodType());
        MutablePeriod mp = new MutablePeriod();
        mp.add(period);
        assertEquals(5, mp.getValues()[0]);
        assertEquals(PeriodType.fixed, mp.getPeriodType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodNullArgument() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(null);
    }

    @Test
    public void testBetween() throws Exception {
        ReadableInstant instant1 = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);
        ReadableInstant instant2 = new DateTime(2022, 1, 2, 12, 0, 0, 0, DateTimeZone.UTC);
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(DurationFieldType.DAYS.getPreciseValue() * 10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(DurationFieldType.DAYS.getPreciseValue() * 20);
        assertEquals(10, BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.DAYS));
        assertEquals(20, BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.DAYS));
        assertEquals(0, BaseSingleFieldPeriod.between(instant1, instant1, DurationFieldType.DAYS));
        assertEquals(10, period1.getValue());
        assertEquals(20, period2.getValue());
        assertEquals(DurationFieldType.DAYS, period1.getFieldType());
        assertEquals(DurationFieldType.DAYS, period2.getFieldType());
        assertEquals(1, period1.size());
        assertEquals(1, period2.size());

        ReadablePartial readablePartial = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toLocalDate();
        ReadablePeriod readablePeriod = new Period(10, DurationFieldType.DAYS);
        assertEquals(10, BaseSingleFieldPeriod.between(readablePartial, readablePartial, readablePeriod));

        // Test for non-contiguous ReadablePartial
        ReadablePartial nonContiguousPartial = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toLocalDate().withDayOfMonth(2);
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(readablePartial, nonContiguousPartial, readablePeriod));
    }

    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(DurationFieldType.DAYS.getPreciseValue() * 10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(DurationFieldType.DAYS.getPreciseValue() * 20);
        assertEquals(10, BaseSingleFieldPeriod.standardPeriodIn(period1, 1));
        assertEquals(20, BaseSingleFieldPeriod.standardPeriodIn(period2, 1));
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(Period.ZERO, 1));
    }
}