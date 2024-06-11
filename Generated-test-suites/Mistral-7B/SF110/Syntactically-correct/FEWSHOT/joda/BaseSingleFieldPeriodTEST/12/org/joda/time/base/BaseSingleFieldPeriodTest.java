package org.joda.time.base;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod() {
        BaseSingleFieldPeriod period = new Day(1);
        assertEquals(1, period.getValue());
        assertEquals(ISODate.class, period.getFieldType().getField(ISOChronology.getInstanceUTC()).getClass());
    }

    @Test
    public void testBetween() {
        ReadableInstant instant1 = new DateTime(2019, 1, 1, 0, 0, 0, 0, ISO.UTC).toInstant();
        ReadableInstant instant2 = new DateTime(2019, 1, 2, 0, 0, 0, 0, ISO.UTC).toInstant();
        ReadablePeriod period = new Days(1);

        int between = BaseSingleFieldPeriod.between(instant1, instant2, DateTimeUtils.MILLISECONDS);
        int expected = BaseSingleFieldPeriod.between(instant1, instant2, new Days(1).getFieldType());
        assertEquals(expected, between);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullReadableInstant() {
        ReadableInstant instant1 = null;
        ReadableInstant instant2 = new DateTime(2019, 1, 1, 0, 0, 0, 0, ISO.UTC).toInstant();
        ReadablePeriod period = new Days(1);

        BaseSingleFieldPeriod.between(instant1, instant2, period.getFieldType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullReadableInstant2() {
        ReadableInstant instant1 = new DateTime(2019, 1, 1, 0, 0, 0, 0, ISO.UTC).toInstant();
        ReadableInstant instant2 = null;
        ReadablePeriod period = new Days(1);

        BaseSingleFieldPeriod.between(instant1, instant2, period.getFieldType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenDifferentFieldTypes() {
        ReadableInstant instant1 = new DateTime(2019, 1, 1, 0, 0, 0, 0, ISO.UTC).toInstant();
        ReadableInstant instant2 = new DateTime(2019, 1, 2, 0, 0, 0, 0, ISO.UTC).toInstant();
        ReadablePeriod period1 = new Days(1);
        ReadablePeriod period2 = new Hours(1);

        BaseSingleFieldPeriod.between(instant1, instant2, period1.getFieldType());
        BaseSingleFieldPeriod.between(instant1, instant2, period2.getFieldType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNonContiguousReadablePartial() {
        ReadablePartial readablePartial = new GeneralBaseDate(2019, 1, 1).withFieldAdded(BaseDateTime.HOUR_OF_DAY, 1).withFieldAdded(BaseDateTime.MINUTE_OF_HOUR, 30);
        ReadableInstant instant1 = new DateTime(readablePartial.toDateTimeAtStartOfNextDay().toLocalDate().toDate(), 0, 0, 0, 0, 0, 0, ISO.UTC).toInstant();
        ReadablePartial readablePartial2 = new GeneralBaseDate(2019, 1, 2).withFieldAdded(BaseDateTime.HOUR_OF_DAY, 1).withFieldAdded(BaseDateTime.MINUTE_OF_HOUR, 15);
        ReadablePeriod period = new Days(1);

        BaseSingleFieldPeriod.between(instant1, new DateTime(readablePartial2.toDateTimeAtStartOfNextDay().toLocalDate().toDate(), 0, 0, 0, 0, 0, 0, ISO.UTC).toInstant(), period.getFieldType());
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Days(1);
        long n = 2;

        int standardPeriodIn = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        int expected = Period.DAYS.getValues()[0] * 2;
        assertEquals(expected, standardPeriodIn);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNullReadablePeriod() {
        ReadablePeriod period = null;
        long n = 1;

        BaseSingleFieldPeriod.standardPeriodIn(period, n);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNonPreciseField() {
        ReadablePeriod period = new Years(1).withFieldAdded(Months.MONTHS, 2);
        long n = 2;

        BaseSingleFieldPeriod.standardPeriodIn(period, n);
    }
}