package org.joda.time.test.chrono;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date = new DateTime(2015, 1, 1).getMillis();
        int actual = field.get(date);
        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWithValidArguments() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date = new DateTime(2015, 1, 1).getMillis();
        long expected = new DateTime(2016, 2, 29).getMillis();
        long actual = field.add(date, 13);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWithZeroArgument() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date = new DateTime(2015, 1, 1).getMillis();
        long actual = field.add(date, 0);
        assertEquals(date, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithNegativeArgument() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date = new DateTime(2015, 1, 1).getMillis();
        field.add(date, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithTooLargeArgument() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date = new DateTime(2015, 1, 1).getMillis();
        field.add(date, Integer.MAX_VALUE);
    }

    @Test
    public void testAddWithLongArgument() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date = new DateTime(2015, 1, 1).getMillis();
        long expected = new DateTime(2016, 3, 1).getMillis();
        long actual = field.add(date, 600L);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 12);
        long date = new DateTime(2015, 1, 1).getMillis();
        long expected = new DateTime(2016, 1, 1).getMillis();
        long actual = field.addWrapField(date, 11);
        assertEquals(expected, actual);

        expected = new DateTime(2015, 12, 31).getMillis();
        actual = field.addWrapField(date, -1);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field1 = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date1 = new DateTime(2015, 1, 1).getMillis();

        BasicMonthOfYearDateTimeField field2 = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date2 = new DateTime(2016, 2, 29).getMillis();

        long expected = 13;
        long actual = field1.getDifferenceAsLong(date1, date2);
        assertEquals(expected, actual);

        expected = -13;
        actual = field1.getDifferenceAsLong(date2, date1);
        assertEquals(expected, actual);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date = new DateTime(2015, 1, 1).getMillis();
        int expectedMonth = 2;
        long expectedDate = new DateTime(2016, 2, 29).getMillis();
        long actualDate = field.set(date, expectedMonth);
        assertEquals(expectedDate, actualDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithInvalidArgument() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date = new DateTime(2015, 1, 1).getMillis();
        field.set(date, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithInvalidMonth() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date = new DateTime(2015, 1, 1).getMillis();
        field.set(date, 13);
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date = new DateTime(2016, 2, 29).getMillis();
        int actual = field.getLeapAmount(date);
        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        DurationField actual = field.getLeapDurationField();
        DurationField expected = field.iChronology.days();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        int actual = field.getMinimumValue();
        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        int actual = field.getMaximumValue();
        int expected = 12;
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date = new DateTime(2015, 1, 2).getMillis();
        long expected = new DateTime(2015, 1, 1).getMillis();
        long actual = field.roundFloor(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date = new DateTime(2015, 1, 3).getMillis();
        long expected = 2;
        long actual = field.remainder(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date = new DateTime(2016, 2, 29).getMillis();
        boolean actual = field.isLeap(date);
        assertTrue(actual);
    }

    @Test
    public void testIsLeapWithNonLeapYear() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
        long date = new DateTime(2015, 2, 29).getMillis();
        boolean actual = field.isLeap(date);
        assertFalse(actual);
    }
}