package org.joda.time.chrono.tests;

import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.arrays.Array;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@code BasicMonthOfYearDateTimeField}.
 */
public class BasicMonthOfYearDateTimeFieldTest {

    /**
     * Test chronology.
     */
    private static final Chronology TEST_CHRONOLOGY = new BasicChronology("Test", DateTimeConstants.PROLEPTIC_GREGORIAN) {
        @Override
        public int getMaxYear() {
            return 1000;
        }

        @Override
        public int getMinYear() {
            return -1000;
        }

        @Override
        public int getMaxMonth() {
            return 13;
        }

        @Override
        public int getMinMonth() {
            return 0;
        }

        @Override
        public boolean isLeapYear(int year) {
            return (year % 4) == 0 && ((year % 100) != 0 || (year % 400) == 0);
        }

        @Override
        public int getDaysInYearMonth(int year, int month) {
            switch (month) {
                case 2:
                    return isLeapYear(year) ? 29 : 28;
                case 4:
                case 6:
                case 9:
                case 11:
                    return 30;
                default:
                    return 31;
            }
        }
    };

    /**
     * Source under test.
     */
    private static final BasicMonthOfYearDateTimeField TEST_FIELD = new BasicMonthOfYearDateTimeField(TEST_CHRONOLOGY, 2);

    /**
     * Test get method.
     */
    @Test
    public void testGet() {
        // Basic usage
        MutableDateTime date = new MutableDateTime(0L, DateTimeConstants.JANUARY, 1, 2005, 0, 0, 0);
        assertEquals(1, TEST_FIELD.get(date.getMillis()));

        // Leap year
        date.setYear(2008);
        assertEquals(2, TEST_FIELD.get(date.getMillis()));

        // Negative values
        date.setMillis(-1000L);
        assertEquals(12, TEST_FIELD.get(date.getMillis()));
    }

    /**
     * Test set method.
     */
    @Test
    public void testSet() {
        // Basic usage
        MutableDateTime date = new MutableDateTime(0L, DateTimeConstants.JANUARY, 1, 2005, 0, 0, 0);
        long result = TEST_FIELD.set(date.getMillis(), 12);
        assertEquals(LocalDateTime.parse("2005-12-31T00:00:00.000"), TEST_CHRONOLOGY.get(TEST_FIELD, result));

        // Leap year
        date.setYear(2008);
        result = TEST_FIELD.set(date.getMillis(), 2);
        assertEquals(LocalDateTime.parse("2008-02-29T00:00:00.000"), TEST_CHRONOLOGY.get(TEST_FIELD, result));

        // Negative values
        date.setMillis(-1000L);
        result = TEST_FIELD.set(date.getMillis(), 2);
        assertEquals(LocalDateTime.parse("1000-02-28T00:00:00.000"), TEST_CHRONOLOGY.get(TEST_FIELD, result));
    }

    /**
     * Test add method.
     */
    @Test
    public void testAdd() {
        // Basic usage
        MutableDateTime date = new MutableDateTime(0L, DateTimeConstants.JANUARY, 1, 2005, 0, 0, 0);
        long result = TEST_FIELD.add(date.getMillis(), 1);
        assertEquals(LocalDateTime.parse("2005-02-01T00:00:00.000"), TEST_CHRONOLOGY.get(TEST_FIELD, result));

        // Leap year
        date.setYear(2008);
        result = TEST_FIELD.add(date.getMillis(), 1);
        assertEquals(LocalDateTime.parse("2008-03-01T00:00:00.000"), TEST_CHRONOLOGY.get(TEST_FIELD, result));

        // Negative values
        date.setMillis(-1000L);
        result = TEST_FIELD.add(date.getMillis(), -1);
        assertEquals(LocalDateTime.parse("1000-12-31T00:00:00.000"), TEST_CHRONOLOGY.get(TEST_FIELD, result));
    }

    /**
     * Test addWrapField method.
     */
    @Test
    public void testAddWrapField() {
        // Basic usage
        MutableDateTime date = new MutableDateTime(0L, DateTimeConstants.JANUARY, 1, 2005, 0, 0, 0);
        long result = TEST_FIELD.addWrapField(date.getMillis(), 13);
        assertEquals(LocalDateTime.parse("2005-01-31T00:00:00.000"), TEST_CHRONOLOGY.get(TEST_FIELD, result));

        // Leap year
        date.setYear(2008);
        result = TEST_FIELD.addWrapField(date.getMillis(), 13);
        assertEquals(LocalDateTime.parse("2007-12-31T00:00:00.000"), TEST_CHRONOLOGY.get(TEST_FIELD, result));

        // Negative values
        date.setMillis(-1000L);
        result = TEST_FIELD.addWrapField(date.getMillis(), -13);
        assertEquals(LocalDateTime.parse("1001-01-31T00:00:00.000"), TEST_CHRONOLOGY.get(TEST_FIELD, result));
    }

    /**
     * Test isLeap method.
     */
    @Test
    public void testIsLeap() {
        // Basic usage
        MutableDateTime date = new MutableDateTime(0L, DateTimeConstants.JANUARY, 1, 2005, 0, 0, 0);
        assertFalse(TEST_FIELD.isLeap(date.getMillis()));

        // Leap year
        date.setYear(2008);
        assertTrue(TEST_FIELD.isLeap(date.getMillis()));

        // Negative values
        date.setMillis(-1000L);
        assertFalse(TEST_FIELD.isLeap(date.getMillis()));
    }

    /**
     * Test getLeapAmount method.
     */
    @Test
    public void testGetLeapAmount() {
        // Basic usage
        MutableDateTime date = new MutableDateTime(0L, DateTimeConstants.JANUARY, 1, 2005, 0, 0, 0);
        assertEquals(0, TEST_FIELD.getLeapAmount(date.getMillis()));

        // Leap year
        date.setYear(2008);
        assertEquals(1, TEST_FIELD.getLeapAmount(date.getMillis()));

        // Negative values
        date.setMillis(-1000L);
        assertEquals(0, TEST_FIELD.getLeapAmount(date.getMillis()));
    }

    /**
     * Test getLeapDurationField method.
     */
    @Test
    public void testGetLeapDurationField() {
        assertEquals(DurationFieldType.days(), TEST_FIELD.getLeapDurationField().getType());
    }

    /**
     * Test getMinimumValue method.
     */
    @Test
    public void testGetMinimumValue() {
        assertEquals(0, TEST_FIELD.getMinimumValue());
    }

    /**
     * Test getMaximumValue method.
     */
    @Test
    public void testGetMaximumValue() {
        assertEquals(13, TEST_FIELD.getMaximumValue());
    }

    /**
     * Test roundFloor method.
     */
    @Test
    public void testRoundFloor() {
        MutableDateTime date = new MutableDateTime(0L, DateTimeConstants.JANUARY, 1, 2005, 0, 0, 0);
        long expected = TEST_CHRONOLOGY.getYearMonthMillis(2005, 1);
        long result = TEST_FIELD.roundFloor(date.getMillis());
        assertEquals(expected, result);

        date.setYear(2008);
        expected = TEST_CHRONOLOGY.getYearMonthMillis(2008, 2);
        result = TEST_FIELD.roundFloor(date.getMillis());
        assertEquals(expected, result);

        date.setMillis(-1000L);
        expected = TEST_CHRONOLOGY.getYearMonthMillis(-1000, 12);
        result = TEST_FIELD.roundFloor(date.getMillis());
        assertEquals(expected, result);
    }

    /**
     * Test remainder method.
     */
    @Test
    public void testRemainder() {
        MutableDateTime date = new MutableDateTime(0L, DateTimeConstants.JANUARY, 1, 2005, 0, 0, 0);
        long expected = 0;
        long result = TEST_FIELD.remainder(date.getMillis());
        assertEquals(expected, result);

        date.setYear(2008);
        expected = 28 * 24 * 60 * 60 * 1000;
        result = TEST_FIELD.remainder(date.getMillis());
        assertEquals(expected, result);

        date.setMillis(-1000L);
        expected = (31 * 24 * 60 * 60 * 1000 - 1) % (31 * 24 * 60 * 60 * 1000);
        result = TEST_FIELD.remainder(date.getMillis());
        assertEquals(expected, result);
    }
}