package org.joda.time.base;

import org.junit.Test;
import org.joda.time.BasePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.MutablePeriod;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        ReadableInstant instant1 = new Instant(63072000000L); // Jan 1, 1972
        ReadableInstant instant2 = new Instant(630848400000L); // Jan 2, 1972
        DurationFieldType durationFieldType = DurationFieldType.MILLISECONDS;
        int result = BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
        assertEquals(24 * 60 * 60 * 1000, result); // 24 hours
    }

    @Test
    public void testBetween_NullObjects() {
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, null, DurationFieldType.MILLISECONDS));
    }

    @Test
    public void testBetween_ReadablePartial() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3600000); // 1 hour
        ReadablePartial partial1 = new LocalDateTime(2022, 1, 1, 10, 0, 0).toLocalDate().toDateTimeAtStartOfDay().toLocalDate();
        ReadablePartial partial2 = new LocalDateTime(2022, 1, 1, 11, 0, 0).toLocalDate().toDateTimeAtStartOfDay().toLocalDate();
        ReadablePeriod periodObject = new Period(period);
        int result = BaseSingleFieldPeriod.between(partial1, partial2, periodObject);
        assertEquals(period.getValue(), result);
    }

    @Test
    public void testBetween_NullObjects_ReadablePartial() {
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, new LocalDateTime(2022, 1, 1, 10, 0, 0).toLocalDate().toDateTimeAtStartOfDay().toLocalDate(), DurationFieldType.MILLISECONDS));
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(new LocalDateTime(2022, 1, 1, 10, 0, 0).toLocalDate().toDateTimeAtStartOfDay().toLocalDate(), null, DurationFieldType.MILLISECONDS));
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(new LocalDateTime(2022, 1, 1, 10, 0, 0).toLocalDate().toDateTimeAtStartOfDay().toLocalDate(), new LocalDateTime(2022, 1, 1, 10, 0, 0).toLocalDate().toDateTimeAtStartOfDay().toLocalDate(), null));
    }

    @Test
    public void testBetween_InvalidReadablePartial() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3600000); // 1 hour
        ReadablePartial partial1 = new LocalDateTime(2022, 1, 1, 10, 0, 0).toLocalDate().toDateTimeAtStartOfDay();
        ReadablePartial partial2 = new LocalDateTime(2022, 1, 1, 10, 0, 1).toLocalDate().toDateTimeAtStartOfDay();
        ReadablePeriod periodObject = new Period(period);
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(partial1, partial2, periodObject));
    }

    @Test
    public void testStandardPeriodIn() {
        Period period = new Period(3600000, DurationFieldType.MILLISECONDS); // 1 hour
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 2); // 0.5 hours
        assertEquals(1800000, result);
    }

    @Test
    public void testStandardPeriodIn_NullPeriod() {
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.standardPeriodIn(null, 2));
    }

    @Test
    public void testStandardPeriodIn_InvalidPeriod() {
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.standardPeriodIn(new Period(1, DurationFieldType.HOURS, 30, DurationFieldType.MINUTES), 2));
    }

    @Test
    public void testStandardPeriodIn_InvalidArgument() {
        Period period = new Period(3600000, DurationFieldType.MILLISECONDS); // 1 hour
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.standardPeriodIn(period, 0));
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.standardPeriodIn(period, 3));
    }

    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3600000); // 1 hour
        assertEquals(DurationFieldType.MILLISECONDS, period.getFieldType());
        assertEquals(PeriodType.fixedPeriod, period.getPeriodType());
        assertEquals(1, period.size());
        assertEquals(3600000, period.getValue());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3600000); // 1 hour
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3600000); // 1 hour
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(7200000); // 2 hours
        assertEquals(period1, period1);
        assertEquals(period1, period2);
        assertNotEquals(period1, period3);
        assertNotEquals(period1, new Object());
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3600000); // 1 hour
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3600000); // 1 hour
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(7200000); // 2 hours
        assertEquals(27 * (27 * 17 + period1.getValue()) + period1.getFieldType().hashCode(), 27 * (27 * 17 + period2.getValue()) + period2.getFieldType().hashCode());
        assertNotEquals(period1.hashCode(), period3.hashCode());
        assertNotEquals(period1.hashCode(), new Object().hashCode());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3600000); // 1 hour
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3600000); // 1 hour
        assertEquals(DurationFieldType.MILLISECONDS, period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3600000); // 1 hour
        assertEquals(PeriodType.fixedPeriod, period.getPeriodType());
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3600000); // 1 hour
        Period result = period.toPeriod();
        assertEquals(new Period(period), result);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3600000); // 1 hour
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(new Period(period), result);
        assertEquals(period, result.toPeriod());
    }

    @Test
    public void testToString() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3600000); // 1 hour
        assertEquals("PT1H", period.toString());
    }
}