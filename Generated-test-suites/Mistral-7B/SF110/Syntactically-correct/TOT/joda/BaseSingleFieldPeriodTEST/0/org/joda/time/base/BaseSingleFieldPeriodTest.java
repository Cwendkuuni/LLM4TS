package org.joda.time.base;

import org.joda.time.BaseDurationFieldType;
import org.joda.time.BasePeriod;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {
    @Test
    public void testBetweenMethod() {
        // Test with valid ReadableInstant objects
        final Instant instant1 = new Instant(1492560000000L); // 2017-01-01 00:00:00.000
        final Instant instant2 = new Instant(1492646400000L); // 2017-01-02 00:00:00.000
        final DurationFieldType durationFieldType = BaseDurationFieldType.days();
        final int result = BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
        assertEquals(1, result);

        // Test with valid ReadablePartial objects
        final LocalDateTime partial1 = new LocalDateTime(2017, 1, 1, 0, 0);
        final LocalDateTime partial2 = new LocalDateTime(2017, 1, 2, 0, 0);
        final ReadablePeriod period = Period.days(1);
        final int result2 = BaseSingleFieldPeriod.between(partial1, partial2, period);
        assertEquals(1, result2);

        // Test with null ReadableInstant or ReadablePartial objects
        try {
            BaseSingleFieldPeriod.between(null, instant2, durationFieldType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSingleFieldPeriod.between(instant1, null, durationFieldType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BaseSingleFieldPeriod.between(null, null, period);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with invalid ReadablePartial objects
        final LocalDateTime invalidPartial1 = new LocalDateTime(2017, 1, 1, 0, 0, 60); // 2017-01-01 00:00:60.000
        final LocalDateTime invalidPartial2 = new LocalDateTime(2017, 1, 2, 0, 0);
        try {
            BaseSingleFieldPeriod.between(invalidPartial1, invalidPartial2, period);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        final MutablePeriod invalidPeriod = new MutablePeriod();
        invalidPeriod.setField(new LocalDateField(1), 2, LocalDate.parse("2017-01-01"));
        try {
            BaseSingleFieldPeriod.between(partial1, invalidPeriod, period);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testStandardPeriodInMethod() {
        // Test with valid ReadablePeriod object
        final Period period = Period.days(3);
        final long n = 2;
        final int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        assertEquals(136400000, result); // 3 days * 24 hours/day * 60 minutes/hour * 60 seconds/minute * n

        // Test with null ReadablePeriod object
        try {
            BaseSingleFieldPeriod.standardPeriodIn(null, n);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEqualsMethod() {
        // Test with equal objects
        final BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        final BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(5);
        assertTrue(period1.equals(period2));

        // Test with null object
        final BaseSingleFieldPeriod period3 = null;
        assertFalse(period1.equals(period3));

        // Test with object of different class
        final Object unrelatedObject = new Object();
        assertFalse(period1.equals(unrelatedObject));

        // Test with object of same class but different value
        final BaseSingleFieldPeriod period4 = new MySingleFieldPeriod(6);
        assertFalse(period1.equals(period4));
    }

    public static class MySingleFieldPeriod extends BaseSingleFieldPeriod {
        public MySingleFieldPeriod(int iPeriod) {
            super(iPeriod);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.days();
        }
    }
}