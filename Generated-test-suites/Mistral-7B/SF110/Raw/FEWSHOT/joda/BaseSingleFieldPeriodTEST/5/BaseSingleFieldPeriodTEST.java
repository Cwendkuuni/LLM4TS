package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.base.Defaults;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.DurationFieldType;

import java.io.Serializable;
import java.util.Arrays;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        final long[] testCases = new long[]{
                0L,
                63072000000L,
                126224000000L,
                244096000000L,
                145152000000L,
                31536000000L,
                151200000000L,
                31536000000L,
                12622400000L,
                3600000L,
                60000L,
                15 * 60 * 1000,
                30 * 60 * 1000,
                60 * 1000,
                1 * 1000};

        for (long testCase : testCases) {
            final Chronology chronology = ISOChronology.getInstanceUTC();
            final BaseDateTime start = new BaseDateTime(63072000000L, chronology);
            final BaseDateTime end = new BaseDateTime(start.getMillis() + testCase);
            final ReadableInstant startInstant = new BaseDateTime(start.getMillis(), chronology);
            final ReadableInstant endInstant = new BaseDateTime(end.getMillis(), chronology);
            final DurationFieldType durationFieldType = Defaults.DURATION_FIELD_TYPE_MILLIS;
            final int result = BaseSingleFieldPeriod.between(startInstant, endInstant, durationFieldType);
            final int expected = (int) testCase;
            assertEquals(expected, result);
        }
    }

    @Test
    public void testBetween_null_readableInstant() {
        final DurationFieldType durationFieldType = DurationFieldType.MILLISECONDS;
        try {
            BaseSingleFieldPeriod.between(null, new BaseDateTime(63072000000L, ISOChronology.getInstanceUTC()), durationFieldType);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBetween_null_readableInstant2() {
        final DurationFieldType durationFieldType = DurationFieldType.MILLISECONDS;
        final ReadableInstant readableInstant = new BaseDateTime(63072000000L, ISOChronology.getInstanceUTC());
        try {
            BaseSingleFieldPeriod.between(readableInstant, null, durationFieldType);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBetween_durationFieldType_null() {
        final ReadableInstant readableInstant = new BaseDateTime(63072000000L, ISOChronology.getInstanceUTC());
        final ReadableInstant readableInstant2 = new BaseDateTime(63072000010L, ISOChronology.getInstanceUTC());
        try {
            BaseSingleFieldPeriod.between(readableInstant, readableInstant2, null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testStandardPeriodIn() {
        final BaseSingleFieldPeriod testPeriod = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final long[] testCases = new long[]{1L, 2L, 3L, 4L, 5L};
        for (long n : testCases) {
            final int result = BaseSingleFieldPeriod.standardPeriodIn(testPeriod, n);
            final int expected = (int) Math.floorDiv((double) testPeriod.getValue(), (double) n) * n;
            assertEquals(expected, result);
        }
    }

    @Test
    public void testStandardPeriodIn_null_readablePeriod() {
        final long n = 15 * 60 * 1000;
        try {
            BaseSingleFieldPeriod.standardPeriodIn(null, n);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetFieldType() {
        final BaseSingleFieldPeriod testPeriod = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final DurationFieldType fieldType = testPeriod.getFieldType();
        assertEquals(DurationFieldType.MILLISECONDS, fieldType);
    }

    @Test
    public void testGetPeriodType() {
        final BaseSingleFieldPeriod testPeriod = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final PeriodType periodType = testPeriod.getPeriodType();
        assertEquals(PeriodType.MILLISECS, periodType);
    }

    @Test
    public void testSize() {
        final BaseSingleFieldPeriod testPeriod = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final int size = testPeriod.size();
        assertEquals(1, size);
    }

    @Test
    public void testGetFieldType_indexOutOfBoundsException() {
        final BaseSingleFieldPeriod testPeriod = Period.MILLISECS.withMillis(15 * 60 * 1000);
        try {
            testPeriod.getFieldType(2);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testGetValue() {
        final BaseSingleFieldPeriod testPeriod = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final int value = testPeriod.getValue();
        assertEquals(15 * 60 * 1000, value);
    }

    @Test
    public void testSetValue() {
        final BaseSingleFieldPeriod testPeriod = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final int newValue = 20 * 60 * 1000;
        testPeriod.setValue(newValue);
        final int result = testPeriod.getValue();
        assertEquals(newValue, result);
    }

    @Test
    public void testToPeriod() {
        final BaseSingleFieldPeriod testPeriod = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final Period result = testPeriod.toPeriod();
        final Period expected = Period.MILLISECS.withMillis(15 * 60 * 1000);
        assertSame(expected, result);
    }

    @Test
    public void testToMutablePeriod() {
        final BaseSingleFieldPeriod testPeriod = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final MutablePeriod result = testPeriod.toMutablePeriod();
        final MutablePeriod expected = new MutablePeriod(DurationFieldType.MILLISECONDS, 15 * 60 * 1000);
        assertSame(expected, result);
    }

    @Test
    public void testEquals() {
        final BaseSingleFieldPeriod testPeriod1 = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final BaseSingleFieldPeriod testPeriod2 = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final boolean result = testPeriod1.equals(testPeriod2);
        final boolean expected = true;
        assertEquals(expected, result);

        final BaseSingleFieldPeriod testPeriod3 = Period.HOURS.withHours(2);
        final boolean result2 = testPeriod1.equals(testPeriod3);
        final boolean expected2 = false;
        assertEquals(expected2, result2);
    }

    @Test
    public void testHashCode() {
        final BaseSingleFieldPeriod testPeriod = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final int result = testPeriod.hashCode();
        final int expected = 27 * (27 * 17 + 15 * 60 * 1000 + DurationFieldType.MILLISECONDS.hashCode());
        assertEquals(expected, result);
    }

    @Test
    public void testCompareTo_sameClass() {
        final BaseSingleFieldPeriod testPeriod1 = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final BaseSingleFieldPeriod testPeriod2 = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final int result = testPeriod1.compareTo(testPeriod2);
        final int expected = 0;
        assertEquals(expected, result);
    }

    @Test
    public void testCompareTo_differentClasses() {
        final BaseSingleFieldPeriod testPeriod1 = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final BaseSingleFieldPeriod testPeriod2 = Period.HOURS.withHours(2);
        final int result = testPeriod1.compareTo(testPeriod2);
        final int expected = -1;
        assertEquals(expected, result);
    }

    @Test
    public void testCompareTo_sameValue() {
        final BaseSingleFieldPeriod testPeriod1 = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final BaseSingleFieldPeriod testPeriod2 = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final int result = testPeriod1.compareTo(testPeriod2);
        final int expected = 0;
        assertEquals(expected, result);
    }

    @Test
    public void testCompareTo_differentValues() {
        final BaseSingleFieldPeriod testPeriod1 = Period.MILLISECS.withMillis(15 * 60 * 1000);
        final BaseSingleFieldPeriod testPeriod2 = Period.MILLISECS.withMillis(20 * 60 * 1000);
        final int result = testPeriod1.compareTo(testPeriod2);
        final int expected = -1;
        assertEquals(expected, result);
    }
}