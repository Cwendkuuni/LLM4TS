package org.joda.time.tests.chrono;

import org.joda.time.BaseDateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.JulianFields;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class BasicMonthOfYearDateTimeFieldTest {

    private static final int TEST_YEAR = 2023;
    private static final int TEST_MONTH = 1;
    private static final int TEST_MONTH_2 = 12;
    private static final int TEST_MONTH_3 = 13;
    private static final int TEST_DOW = 2;
    private static final long TEST_INSTANT = BaseDateTime.UTC.getMillis() + TEST_DOW * JulianFields.DAYS_PER_WEEK;
    private static final int TEST_MONTHS = 3;
    private static final int TEST_MONTHS_NEGATIVE = -3;
    private static final int TEST_LEAP_MONTH = 2;
    private static final int TEST_MAX_MONTH = 12;

    private final BasicMonthOfYearDateTimeField field;

    public BasicMonthOfYearDateTimeFieldTest(BasicChronology chronology) {
        field = new BasicMonthOfYearDateTimeField(chronology, TEST_LEAP_MONTH);
    }

    @Parameterized.Parameters(name = "test with chronology: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{new ISOChronology()},
                new Object[]{new BasicChronology(365)},
                new Object[]{new BasicChronology(366)}
        });
    }

    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        assertEquals(TEST_MONTH, field.get(TEST_INSTANT));
    }

    @Test
    public void testAdd() {
        long addResult = field.add(TEST_INSTANT, TEST_MONTHS);
        int expectedYear = TEST_YEAR + ((TEST_MONTHS >= 0) ? 1 : -1);
        int expectedMonth = (TEST_MONTH + TEST_MONTHS) % TEST_MAX_MONTH + 1;
        long expectedMillis = BaseDateTime.UTC.getMillis() + (expectedYear - TEST_YEAR) * 12 * 30 * 29 +
                (expectedMonth - 1) * 30 + TEST_DOW * JulianFields.DAYS_PER_WEEK;
        assertEquals(expectedMillis, addResult);
    }

    @Test
    public void testAddNegative() {
        long addResult = field.add(TEST_INSTANT, TEST_MONTHS_NEGATIVE);
        int expectedYear = TEST_YEAR - ((TEST_MONTHS_NEGATIVE < 0) ? 1 : -1);
        int expectedMonth = (TEST_MONTH + TEST_MONTHS_NEGATIVE + TEST_MAX_MONTH) % TEST_MAX_MONTH + 1;
        long expectedMillis = BaseDateTime.UTC.getMillis() + (expectedYear - TEST_YEAR) * 12 * 30 * 29 +
                (expectedMonth - 1) * 30 + TEST_DOW * JulianFields.DAYS_PER_WEEK;
        assertEquals(expectedMillis, addResult);
    }

    @Test
    public void testAddMonths() {
        long addResult = field.add(TEST_INSTANT, TEST_MONTHS);
        assertEquals(TEST_MONTHS, FieldUtils.getWrappedValue(TEST_MONTH, TEST_MONTHS, 1, TEST_MAX_MONTH));
        assertEquals(expectedMillis, addResult);
    }

    @Test
    public void testAddWrapField() {
        long addResult = field.addWrapField(TEST_INSTANT, TEST_MONTHS);
        assertEquals(TEST_MONTHS, FieldUtils.getWrappedValue(TEST_MONTH, TEST_MONTHS, 1, TEST_MAX_MONTH));
        assertEquals(expectedMillis, addResult);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long difference = field.getDifferenceAsLong(TEST_INSTANT, TEST_INSTANT);
        assertEquals(0, difference);

        long minuendInstant = TEST_INSTANT - TEST_DOW * JulianFields.DAYS_PER_WEEK;
        long subtrahendInstant = TEST_INSTANT;
        long difference2 = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(0, difference2);

        long minuendInstant2 = TEST_INSTANT;
        long subtrahendInstant2 = minuendInstant - TEST_DOW * JulianFields.DAYS_PER_WEEK;
        long difference3 = field.getDifferenceAsLong(minuendInstant2, subtrahendInstant2);
        assertEquals(-1, difference3);

        minuendInstant = TEST_INSTANT;
        subtrahendInstant = TEST_INSTANT + TEST_MONTHS * 30;
        long difference4 = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(-TEST_MONTHS * 30, difference4);
    }

    @Test
    public void testSet() {
        long setResult = field.set(TEST_INSTANT, TEST_MONTH_2);
        int expectedYear = TEST_YEAR;
        int expectedMonth = TEST_MONTH_2;
        long expectedMillis = BaseDateTime.UTC.getMillis() + (expectedYear - TEST_YEAR) * 12 * 30 * 29 +
                (expectedMonth - 1) * 30 + TEST_DOW * JulianFields.DAYS_PER_WEEK;
        assertEquals(expectedMillis, setResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        field.set(TEST_INSTANT, 14);
    }

    @Test
    public void testGetRangeDurationField() {
        assertEquals(JulianFields.YEARS, field.getRangeDurationField());
    }

    @Test
    public void testIsLeap() {
        assertTrue(field.isLeap(TEST_INSTANT + (TEST_YEAR - 1) * 12 * 30 * 29 +
                (TEST_LEAP_MONTH - 1) * 30 + 28));
        assertFalse(field.isLeap(TEST_INSTANT + (TEST_YEAR - 1) * 12 * 30 * 29 +
                (TEST_LEAP_MONTH - 1) * 30 + 29));
    }

    @Test
    public void testGetLeapAmount() {
        assertEquals(1, field.getLeapAmount(TEST_INSTANT + (TEST_YEAR - 1) * 12 * 30 * 29 +
                (TEST_LEAP_MONTH - 1) * 30 + 28));
    }

    @Test
    public void testGetLeapDurationField() {
        assertEquals(JulianFields.DAYS, field.getLeapDurationField());
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(TEST_MAX_MONTH, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long roundFloorResult = field.roundFloor(TEST_INSTANT);
        int expectedYear = TEST_YEAR;
        int expectedMonth = TEST_MONTH;
        long expectedMillis = BaseDateTime.UTC.getMillis() + (expectedYear - TEST_YEAR) * 12 * 30 * 29 +
                (expectedMonth - 1) * 30;
        assertEquals(expectedMillis, roundFloorResult);
    }

    @Test
    public void testRemainder() {
        long remainderResult = field.remainder(TEST_INSTANT);
        int expectedDOW = TEST_DOW;
        long expectedMillis = TEST_INSTANT % (JulianFields.DAYS_PER_WEEK * 7);
        assertEquals(expectedMillis, remainderResult);
    }
}