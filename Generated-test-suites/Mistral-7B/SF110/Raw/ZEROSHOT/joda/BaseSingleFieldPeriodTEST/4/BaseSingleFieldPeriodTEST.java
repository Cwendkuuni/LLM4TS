package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.period.*;

import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod_Constructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertEquals(1, period.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_Constructor_NullArgument() {
        new BaseSingleFieldPeriod(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_Between_NullReadableInstant() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        try {
            BaseSingleFieldPeriod.between(null, new InstantMillis(), period.getFieldType());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_Between_NullReadableInstant2() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        try {
            BaseSingleFieldPeriod.between(new InstantMillis(), null, period.getFieldType());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_Between_NullDurationFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        try {
            BaseSingleFieldPeriod.between(new InstantMillis(), new InstantMillis(), null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBaseSingleFieldPeriod_Between_SameInstants() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        Instant instant = new InstantMillis(63072000000L);
        int result = BaseSingleFieldPeriod.between(instant, instant, period.getFieldType());
        assertEquals(0, result);
    }

    @Test
    public void testBaseSingleFieldPeriod_Between_DifferentInstants() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        Instant instant1 = new InstantMillis(63072000000L);
        Instant instant2 = new InstantMillis(63072001000L);
        int result = BaseSingleFieldPeriod.between(instant1, instant2, period.getFieldType());
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_Between_NullReadablePartial() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        try {
            BaseSingleFieldPeriod.between(null, new LocalDateTime(), period);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_Between_NullReadablePartial2() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        try {
            BaseSingleFieldPeriod.between(new LocalDateTime(), null, period);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_Between_NullReadablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        try {
            BaseSingleFieldPeriod.between(new LocalDateTime(), new LocalDateTime(), null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBaseSingleFieldPeriod_Between_SameReadablePartial() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        LocalDateTime localDateTime = new LocalDateTime();
        int result = BaseSingleFieldPeriod.between(localDateTime, localDateTime, period);
        assertEquals(0, result);
    }

    @Test
    public void testBaseSingleFieldPeriod_Between_DifferentReadablePartial() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        LocalDateTime localDateTime1 = new LocalDateTime();
        LocalDateTime localDateTime2 = new LocalDateTime().withFieldAdded(DateTimeFieldType.DAY_OF_YEAR, 1);
        int result = BaseSingleFieldPeriod.between(localDateTime1, localDateTime2, period);
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriod_StandardPeriodIn_NullReadablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        try {
            BaseSingleFieldPeriod.standardPeriodIn(null, 1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBaseSingleFieldPeriod_StandardPeriodIn() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        Period period1 = new Period(1, 0, 0, 0, 0);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period1, 1000);
        assertEquals(1, result);
    }
}