package org.joda.time.base;

import static org.junit.Assert.*;
import org.joda.time.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(1);
        assertEquals(1, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.fixed, period.getPeriodType());
        assertEquals(1, period.size());

        Period p = period.toPeriod();
        assertEquals(1, p.getYears());
        assertEquals(0, p.getMonths());
        assertEquals(0, p.getWeeks());
        assertEquals(1, p.getDays());
        assertEquals(0, p.getHours());
        assertEquals(0, p.getMinutes());
        assertEquals(0, p.getSeconds());
        assertEquals(0, p.getMillis());
        assertEquals(1, p.getMillisWithoutMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodInvalidArguments() {
        new BaseSingleFieldPeriod(-1);
    }

    @Test
    public void testBetween() {
        Instant instant = new DateTime(1330572400000L).toInstant();
        Instant instant2 = new DateTime(1330572460000L).toInstant();
        assertEquals(3600000L, BaseSingleFieldPeriod.between(instant, instant2, DurationFieldType.MILLISECONDS));

        ReadablePartial readablePartial = new GeneralMillisField(1, DateTimeFieldType.MILLISECONDS, DateTimeUtils.UTC);
        ReadablePartial readablePartial2 = new GeneralMillisField(5, DateTimeFieldType.MILLISECONDS, DateTimeUtils.UTC);
        assertEquals(4, BaseSingleFieldPeriod.between(readablePartial, readablePartial2, DurationFieldType.MILLISECONDS));

        try {
            BaseSingleFieldPeriod.between(null, instant2, DurationFieldType.MILLISECONDS);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            BaseSingleFieldPeriod.between(instant, null, DurationFieldType.MILLISECONDS);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testStandardPeriodIn() {
        Period period = new Period(1, DurationFieldType.DAYS);
        assertEquals(86400000, BaseSingleFieldPeriod.standardPeriodIn(period, 1));
        assertEquals(1, BaseSingleFieldPeriod.standardPeriodIn(period, 86400000));
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(null, 1));
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(period, 0));
    }
}

class MySingleFieldPeriod extends BaseSingleFieldPeriod {
    MySingleFieldPeriod(int iPeriod) {
        super(iPeriod);
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.DAYS;
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.fixed;
    }
}