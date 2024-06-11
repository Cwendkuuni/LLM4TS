package org.joda.time.base;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBasicFunctionality() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(5, period.getValue());
        assertEquals(fieldType, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(PeriodType.standard(), period.toPeriod().getPeriodType());
        assertEquals(DurationFieldType.DAYS, fieldType);
        assertEquals(PeriodType.days(), period.getPeriodType());
        assertEquals(Duration.days(5), period.toPeriod().toDuration());
        assertEquals(Period.ZERO.withFields(fieldType, 5), period.toPeriod());
        assertEquals(5, period.get(fieldType));
        assertEquals(true, period.isSupported(fieldType));
        assertEquals(5, period.compareTo(new BaseSingleFieldPeriod(5)));
        assertEquals(0, period.compareTo(new BaseSingleFieldPeriod(0)));
        assertEquals(-1, period.compareTo(new BaseSingleFieldPeriod(6)));
    }

    @Test
    public void testEdgeCasesAndExceptions() {
        try {
            new BaseSingleFieldPeriod(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new BaseSingleFieldPeriod(Integer.MAX_VALUE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5);

        // test equals
        assertTrue(period1.equals(period3));
        assertFalse(period1.equals(period2));
        assertFalse(period1.equals(null));
        assertFalse(period1.equals(""));

        // test hashCode
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        int hashCode3 = period1.hashCode();
        assertEquals(hashCode1, hashCode3);
        assertNotSame(hashCode1, hashCode2);

        // test add
        BaseSingleFieldPeriod period4 = period1.plus(period2);
        assertEquals(PeriodType.standard(), period4.getPeriodType());
        assertEquals(Duration.days(8), period4.toPeriod().toDuration());
        assertEquals(8, period4.getValue());

        // test negative add
        BaseSingleFieldPeriod period5 = period1.minus(period2);
        assertEquals(Duration.days(-2), period5.toPeriod().toDuration());
        assertEquals(-2, period5.getValue());

        // test add with different types
        BaseSingleFieldPeriod period6 = new BaseSingleFieldPeriod(5, DurationFieldType.HOURS);
        BaseSingleFieldPeriod period7 = new BaseSingleFieldPeriod(3, DurationFieldType.MINUTES);
        BaseSingleFieldPeriod period8 = period6.plus(period7);
        assertEquals(Duration.standardHours(8), period8.toPeriod().toDuration());
        assertEquals(8, period8.getValue());

        // test add with different PeriodTypes
        BaseSingleFieldPeriod period9 = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS, PeriodType.days());
        BaseSingleFieldPeriod period10 = new BaseSingleFieldPeriod(3, DurationFieldType.HOURS, PeriodType.hours());
        BaseSingleFieldPeriod period11 = period9.plus(period10, PeriodType.standard());
        assertEquals(Duration.standardDays(8), period11.toPeriod().toDuration());
        assertEquals(8, period11.getValue());

        // test add with zero period
        BaseSingleFieldPeriod period12 = period1.plus(Period.ZERO);
        assertEquals(period1, period12);
    }

    @Test
    public void testZeroInstance() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        Period zeroInstance = Period.ZERO;

        assertEquals(0, between(new LocalDateTime(), new LocalDateTime(), fieldType));
        assertEquals(0, between(new LocalDateTime(0, 0, 0, 0, 0, 0, 0), new LocalDateTime(0, 0, 0, 0, 0, 0, 0), fieldType));
        assertEquals(0, between(new Instant(0), new Instant(0), fieldType));

        assertEquals(0, BaseSingleFieldPeriod.between(new Instant(TestUtils.getUTCMilliseconds()), new Instant(TestUtils.getUTCMilliseconds()), fieldType));

        assertEquals(0, BaseSingleFieldPeriod.between(new LocalDateTime(), new LocalDateTime(), zeroInstance));
        assertEquals(0, BaseSingleFieldPeriod.between(new ReadablePartial(), new ReadablePartial(), zeroInstance));

        assertEquals(PeriodType.standard(), zeroInstance.getPeriodType());

        assertEquals(0, new BaseSingleFieldPeriod(0).getValue());
        assertEquals(0, new BaseSingleFieldPeriod(0, fieldType).getValue());
        assertEquals(0, new BaseSingleFieldPeriod(0, PeriodType.standard()).getValue());

        assertEquals(0, new BaseSingleFieldPeriod(0).get(fieldType));
        assertEquals(0, new BaseSingleFieldPeriod(0, fieldType).get(fieldType));
        assertEquals(0, new BaseSingleFieldPeriod(0, PeriodType.standard()).get(fieldType));

        assertEquals(new BaseSingleFieldPeriod(0), period.withFields(fieldType, 0));
        assertEquals(new BaseSingleFieldPeriod(0), period.withFields(zeroInstance));

        assertEquals(new BaseSingleFieldPeriod(0), period.withValueAdded(fieldType, 0));
        assertEquals(new BaseSingleFieldPeriod(0), period.withValueAdded(zeroInstance, 0));

        assertEquals(new BaseSingleFieldPeriod(0), period.withValueAdded(fieldType, -1));
        assertEquals(new BaseSingleFieldPeriod(0), period.withValueAdded(zeroInstance, -1));

        assertEquals(new BaseSingleFieldPeriod(0), period.withValueAdded(fieldType, Integer.MAX_VALUE));
        assertEquals(new BaseSingleFieldPeriod(0), period.withValueAdded(zeroInstance, Integer.MAX_VALUE));

        assertEquals(new BaseSingleFieldPeriod(0), period.withValueAdded(fieldType, Integer.MIN_VALUE));
        assertEquals(new BaseSingleFieldPeriod(0), period.withValueAdded(zeroInstance, Integer.MIN_VALUE));

        assertEquals(new BaseSingleFieldPeriod(0), period.withValueAdded(fieldType, Integer.MIN_VALUE + 1));
        assertEquals(new BaseSingleFieldPeriod(0), period.withValueAdded(zeroInstance, Integer.MIN_VALUE + 1));

        assertEquals(new BaseSingleFieldPeriod(0), period.withValueAdded(fieldType, Integer.MAX_VALUE - 1));
        assertEquals(new BaseSingleFieldPeriod(0), period.withValueAdded(zeroInstance, Integer.MAX_VALUE - 1));
    }
}