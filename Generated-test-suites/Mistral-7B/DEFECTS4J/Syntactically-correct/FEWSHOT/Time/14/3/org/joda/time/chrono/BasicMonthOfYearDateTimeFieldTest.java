package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DateTimeUtils;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest {

    private static BasicChronology chrono = new BasicGregorianChronology();

    @Test
    public void testGet() {
        long instant = DateTimeConstants.MILLIS_PER_DAY; // first day of 2000
        assertEquals(1, new BasicMonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY).get(instant));
        assertEquals(12, new BasicMonthOfYearDateTimeField(chrono, DateTimeConstants.DECEMBER).get(instant + (DateTimeConstants.MILLIS_PER_DAY * 365)));

        // Test negative month
        instant -= DateTimeConstants.MILLIS_PER_DAY * 30;
        assertEquals(11, new BasicMonthOfYearDateTimeField(chrono, DateTimeConstants.DECEMBER).get(instant));
        assertEquals(12, new BasicMonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY).get(instant - (DateTimeConstants.MILLIS_PER_DAY * 31)));
    }

    @Test
    public void testAdd() {
        long instant = DateTimeConstants.MILLIS_PER_DAY; // first day of 2000
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);
        long expected = DateTimeUtils.millis(2001, 2, 28);
        long result = field.add(instant, 12);
        assertEquals(expected, result);

        // Test negative months
        long result2 = field.add(instant, -12);
        long expected2 = DateTimeUtils.millis(1999, 10, 28);
        assertEquals(expected2, result2);

        // Test wrap around
        long result3 = field.addWrapField(instant, 13);
        long expected3 = DateTimeUtils.millis(2000, 1, 28);
        assertEquals(expected3, result3);

        // Test add with other fields
        ReadablePartial p = DateTimeUtils.daysBetween(DateTimeUtils.millis(2000, 1, 1), DateTimeUtils.millis(2000, 1, 15), field);
        long r = field.add(p.getMillis(), 2);
        long e = DateTimeUtils.millis(2000, 1, 18);
        assertEquals(e, r);
    }

    @Test
    public void testAddLong() {
        long instant = DateTimeConstants.MILLIS_PER_DAY; // first day of 2000
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);
        long expected = DateTimeUtils.millis(2001, 2, 28);
        long result = field.add(instant, 12 * 30);
        assertEquals(expected, result);

        // Test negative months
        long result2 = field.add(instant, -(12 * 30));
        long expected2 = DateTimeUtils.millis(1999, 10, 28);
        assertEquals(expected2, result2);

        // Test wrap around
        long result3 = field.add(instant, 13 * 30);
        long expected3 = DateTimeUtils.millis(2000, 1, 28);
        assertEquals(expected3, result3);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = DateTimeConstants.MILLIS_PER_DAY; // first day of 2000
        long subtrahendInstant = DateTimeUtils.millis(2000, 1, 15);
        long expected = -2;
        long result = new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expected, result);

        // Test positive months
        minuendInstant = DateTimeUtils.millis(2000, 1, 15);
        subtrahendInstant = DateTimeUtils.millis(2000, 1, 1);
        expected = 14;
        result = new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expected, result);

        // Test same instant
        minuendInstant = subtrahendInstant;
        result = new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(0, result);

        // Test negative instant
        minuendInstant = DateTimeConstants.MILLIS_PER_DAY * -1;
        subtrahendInstant = DateTimeUtils.millis(2000, 1, 1);
        expected = 1 + 12;
        result = new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expected, result);
    }

    @Test
    public void testSet() {
        long instant = DateTimeConstants.MILLIS_PER_DAY; // first day of 2000
        long expected = DateTimeUtils.millis(2000, 2, 28);
        long result = new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).set(instant, 2);
        assertEquals(expected, result);

        // Test maximum value
        expected = DateTimeUtils.millis(2000, 12, DateTimeConstants.DAYS_IN_MONTH);
        result = new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).set(instant, 12);
        assertEquals(expected, result);

        // Test minimum value
        expected = DateTimeUtils.millis(2000, 1, 1);
        result = new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).set(instant, 0);
        assertEquals(expected, result);

        // Test out-of-bounds value
        int min = DateTimeFieldType.monthOfYear().getMinimumValue();
        int max = DateTimeFieldType.monthOfYear().getMaximumValue();
        expected = DateTimeUtils.millis(2000, min, DateTimeConstants.DAYS_IN_MONTH);
        result = new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).set(instant, min - 1);
        assertEquals(expected, result);
        expected = DateTimeUtils.millis(2000, max + 1, DateTimeConstants.DAYS_IN_MONTH);
        result = new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).set(instant, max + 1);
        assertEquals(expected, result);
    }

    @Test
    public void testIsLeap() {
        long instant = DateTimeConstants.MILLIS_PER_DAY; // first day of 2000
        assertFalse(new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).isLeap(instant));

        instant = DateTimeUtils.millis(2000, DateTimeConstants.FEBRUARY, DateTimeConstants.DAYS_IN_FEBRUARY + 1);
        assertTrue(new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).isLeap(instant));

        instant = DateTimeUtils.millis(2000, DateTimeConstants.FEBRUARY, DateTimeConstants.DAYS_IN_FEBRUARY - 1);
        assertFalse(new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        long instant = DateTimeConstants.MILLIS_PER_DAY; // first day of 2000
        assertEquals(0, new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).getLeapAmount(instant));

        instant = DateTimeUtils.millis(2000, DateTimeConstants.FEBRUARY, DateTimeConstants.DAYS_IN_FEBRUARY + 1);
        assertEquals(1, new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).getLeapAmount(instant));

        instant = DateTimeUtils.millis(2000, DateTimeConstants.FEBRUARY, DateTimeConstants.DAYS_IN_FEBRUARY - 1);
        assertEquals(0, new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).getLeapAmount(instant));
    }

    @Test
    public void testGetLeapDurationField() {
        assertEquals(DurationField.days(), new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).getLeapDurationField());
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(DateTimeFieldType.MONTH_OF_YEAR.getMinimumValue(), new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(DateTimeFieldType.MONTH_OF_YEAR.getMaximumValue(), new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long instant = DateTimeConstants.MILLIS_PER_DAY; // first day of 2000
        long expected = DateTimeUtils.millis(2000, 1, 1);
        assertEquals(expected, new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).roundFloor(instant));

        instant += DateTimeConstants.MILLIS_PER_DAY * 15;
        expected = DateTimeUtils.millis(2000, 1, 15);
        assertEquals(expected, new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).roundFloor(instant));

        instant -= DateTimeConstants.MILLIS_PER_DAY * 15;
        expected = DateTimeUtils.millis(2000, 1, 1);
        assertEquals(expected, new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).roundFloor(instant));

        instant += DateTimeConstants.MILLIS_PER_DAY * 365;
        expected = DateTimeUtils.millis(2001, 1, 1);
        assertEquals(expected, new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).roundFloor(instant));
    }

    @Test
    public void testRemainder() {
        long instant = DateTimeConstants.MILLIS_PER_DAY; // first day of 2000
        long expected = 0;
        assertEquals(expected, new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).remainder(instant));

        instant += DateTimeConstants.MILLIS_PER_DAY * 15;
        expected = DateTimeConstants.MILLIS_PER_DAY * 15;
        assertEquals(expected, new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).remainder(instant));

        instant -= DateTimeConstants.MILLIS_PER_DAY * 15;
        expected = 0;
        assertEquals(expected, new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).remainder(instant));

        instant += DateTimeConstants.MILLIS_PER_DAY * 365;
        expected = DateTimeConstants.MILLIS_PER_DAY * (365 % 7);
        assertEquals(expected, new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).remainder(instant));
    }

    @Test
    public void testReadResolve() {
        assertEquals(chrono.monthOfYear(), new BasicMonthOfYearDateTimeField(chrono, DateTimeFieldType.monthOfYear()).readResolve());
    }
}