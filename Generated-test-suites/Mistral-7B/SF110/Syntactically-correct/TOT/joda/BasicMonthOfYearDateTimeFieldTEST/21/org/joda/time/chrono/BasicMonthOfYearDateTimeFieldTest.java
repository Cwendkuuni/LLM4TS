package org.joda.time.tests.chrono;

import org.joda.time.BaseDateTime;
import org.joda.time.ChronologyUtils;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.Seconds;
import org.joda.time.chrono.BasicGregorianChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicGregorianChronology.INSTANCE, DateTimeConstants.JANUARY);
        MutableDateTime dateTime = new MutableDateTime(0, 0, 1, 0, 0, 0, 0, BasicGregorianChronology.INSTANCE);
        int month = field.get(dateTime.getMillis());
        assertEquals(DateTimeConstants.JANUARY, month);

        dateTime.set(2000, 3, 29); // March 29, 2000 is not a valid date, but this test just checks the month value
        month = field.get(dateTime.getMillis());
        assertEquals(DateTimeConstants.MARCH, month);

        // Test with a leap year
        dateTime.set(2000, 2, 29); // February 29, 2000 is a valid date for a leap year
        month = field.get(dateTime.getMillis());
        assertEquals(DateTimeConstants.FEBRUARY, month);
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicGregorianChronology.INSTANCE, DateTimeConstants.JANUARY);
        MutableDateTime dateTime = new MutableDateTime(0, 0, 1, 0, 0, 0, 0, BasicGregorianChronology.INSTANCE);

        long result = field.add(dateTime.getMillis(), 12);
        MutableDateTime resultDateTime = new MutableDateTime(result);
        assertEquals(DateTimeConstants.DECEMBER, field.get(resultDateTime.getMillis()));

        dateTime.set(2000, 2, 28); // February 28, 2000 is not a valid date, but this test just checks the month value
        result = field.add(dateTime.getMillis(), 1);
        resultDateTime = new MutableDateTime(result);
        assertEquals(DateTimeConstants.MARCH, field.get(resultDateTime.getMillis()));

        // Test with a leap year
        dateTime.set(2000, 2, 28); // February 28, 2000 is not a valid date, but this test just checks the month value
        result = field.add(dateTime.getMillis(), 1);
        resultDateTime = new MutableDateTime(result);
        assertEquals(DateTimeConstants.MARCH, field.get(resultDateTime.getMillis()));

        result = field.add(dateTime.getMillis(), 12);
        resultDateTime = new MutableDateTime(result);
        assertEquals(DateTimeConstants.DECEMBER, field.get(resultDateTime.getMillis()));
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicGregorianChronology.INSTANCE, DateTimeConstants.JANUARY);
        MutableDateTime dateTime = new MutableDateTime(0, 0, 1, 0, 0, 0, 0, BasicGregorianChronology.INSTANCE);

        long result = field.addWrapField(dateTime.getMillis(), 13);
        MutableDateTime resultDateTime = new MutableDateTime(result);
        assertEquals(DateTimeConstants.FEBRUARY, field.get(resultDateTime.getMillis()));

        result = field.addWrapField(dateTime.getMillis(), -13);
        resultDateTime = new MutableDateTime(result);
        assertEquals(DateTimeConstants.DECEMBER, field.get(resultDateTime.getMillis()));

        // Test with a leap year
        dateTime.set(2000, 2, 28); // February 28, 2000 is not a valid date, but this test just checks the month value
        result = field.addWrapField(dateTime.getMillis(), 13);
        resultDateTime = new MutableDateTime(result);
        assertEquals(DateTimeConstants.MARCH, field.get(resultDateTime.getMillis()));

        result = field.addWrapField(dateTime.getMillis(), -13);
        resultDateTime = new MutableDateTime(result);
        assertEquals(DateTimeConstants.DECEMBER, field.get(resultDateTime.getMillis()));
    }

    @Test
    public void testAddWithInvalidAddAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicGregorianChronology.INSTANCE, DateTimeConstants.JANUARY);
        MutableDateTime dateTime = new MutableDateTime(0, 0, 1, 0, 0, 0, 0, BasicGregorianChronology.INSTANCE);

        // Test adding more than a year
        try {
            field.add(dateTime.getMillis(), 13 * 12 + 1);
            fail("Expected IllegalArgumentException for adding more than a year");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test adding less than a month
        try {
            field.add(dateTime.getMillis(), -12);
            fail("Expected IllegalArgumentException for adding less than a month");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test adding a negative number to a date before the minimum value
        dateTime.set(1900, 1, 1);
        try {
            field.add(dateTime.getMillis(), -1);
            fail("Expected IllegalArgumentException for adding a negative number to a date before the minimum value");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicGregorianChronology.INSTANCE, DateTimeConstants.JANUARY);
        MutableDateTime dateTime1 = new MutableDateTime(0, 0, 1, 0, 0, 0, 0, BasicGregorianChronology.INSTANCE);
        MutableDateTime dateTime2 = new MutableDateTime(0, 0, 1, 0, 0, 1, 0, BasicGregorianChronology.INSTANCE);
        long difference = field.getDifferenceAsLong(dateTime1.getMillis(), dateTime2.getMillis());
        assertEquals(1L, difference);

        dateTime1.set(2000, 2, 28); // February 28, 2000 is not a valid date, but this test just checks the month value
        dateTime2.set(2000, 3, 1);
        difference = field.getDifferenceAsLong(dateTime1.getMillis(), dateTime2.getMillis());
        assertEquals(1L, difference);

        // Test with a leap year
        dateTime1.set(2000, 2, 28); // February 28, 2000 is not a valid date, but this test just checks the month value
        dateTime2.set(2000, 3, 1);
        difference = field.getDifferenceAsLong(dateTime1.getMillis(), dateTime2.getMillis());
        assertEquals(1L, difference);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicGregorianChronology.INSTANCE, DateTimeConstants.JANUARY);
        MutableDateTime dateTime = new MutableDateTime(0, 0, 1, 0, 0, 0, 0, BasicGregorianChronology.INSTANCE);

        long result = field.set(dateTime.getMillis(), DateTimeConstants.DECEMBER);
        MutableDateTime resultDateTime = new MutableDateTime(result);
        assertEquals(DateTimeConstants.DECEMBER, field.get(resultDateTime.getMillis()));

        dateTime.set(2000, 2, 28); // February 28, 2000 is not a valid date, but this test just checks the month value
        result = field.set(dateTime.getMillis(), DateTimeConstants.MARCH);
        resultDateTime = new MutableDateTime(result);
        assertEquals(DateTimeConstants.MARCH, field.get(resultDateTime.getMillis()));

        // Test with a leap year
        dateTime.set(2000, 2, 28); // February 28, 2000 is not a valid date, but this test just checks the month value
        result = field.set(dateTime.getMillis(), DateTimeConstants.DECEMBER);
        resultDateTime = new MutableDateTime(result);
        assertEquals(DateTimeConstants.DECEMBER, field.get(resultDateTime.getMillis()));
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicGregorianChronology.INSTANCE, DateTimeConstants.JANUARY);
        DurationField rangeDurationField = field.getRangeDurationField();
        assertEquals(DurationFieldType.YEARS, rangeDurationField.getType());
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicGregorianChronology.INSTANCE, DateTimeConstants.JANUARY);
        MutableDateTime dateTime = new MutableDateTime(0, 0, 1, 0, 0, 0, 0, BasicGregorianChronology.INSTANCE);
        boolean isLeap = field.isLeap(dateTime.getMillis());
        assertFalse(isLeap);

        dateTime.set(2000, 2, 29); // February 29, 2000 is a valid date for a leap year
        isLeap = field.isLeap(dateTime.getMillis());
        assertTrue(isLeap);
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicGregorianChronology.INSTANCE, DateTimeConstants.JANUARY);
        MutableDateTime dateTime = new MutableDateTime(0, 0, 1, 0, 0, 0, 0, BasicGregorianChronology.INSTANCE);
        int leapAmount = field.getLeapAmount(dateTime.getMillis());
        assertEquals(0, leapAmount);

        dateTime.set(2000, 2, 29); // February 29, 2000 is a valid date for a leap year
        leapAmount = field.getLeapAmount(dateTime.getMillis());
        assertEquals(1, leapAmount);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicGregorianChronology.INSTANCE, DateTimeConstants.JANUARY);
        DurationField leapDurationField = field.getLeapDurationField();
        assertEquals(DurationFieldType.DAYS, leapDurationField.getType());
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicGregorianChronology.INSTANCE, DateTimeConstants.JANUARY);
        int minimumValue = field.getMinimumValue();
        assertEquals(1, minimumValue);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicGregorianChronology.INSTANCE, DateTimeConstants.JANUARY);
        int maximumValue = field.getMaximumValue();
        assertEquals(12, maximumValue);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicGregorianChronology.INSTANCE, DateTimeConstants.JANUARY);
        MutableDateTime dateTime = new MutableDateTime(0, 0, 1, 0, 0, 0, 0, BasicGregorianChronology.INSTANCE);
        long roundFloor = field.roundFloor(dateTime.getMillis());
        MutableDateTime roundFloorDateTime = new MutableDateTime(roundFloor);
        assertEquals(DateTimeConstants.JANUARY, field.get(roundFloorDateTime.getMillis()));
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicGregorianChronology.INSTANCE, DateTimeConstants.JANUARY);
        MutableDateTime dateTime = new MutableDateTime(0, 0, 1, 0, 0, 0, 0, BasicGregorianChronology.INSTANCE);
        long remainder = field.remainder(dateTime.getMillis());
        assertEquals(0L, remainder);

        dateTime.set(2000, 2, 29); // February 29, 2000 is a valid date for a leap year
        remainder = field.remainder(dateTime.getMillis());
        assertEquals(0L, remainder);

        dateTime.set(2000, 2, 28); // February 28, 2000 is not a valid date, but this test just checks the remainder value
        dateTime.add(1, DurationFieldType.DAYS.getLocalFields()[0]);
        remainder = field.remainder(dateTime.getMillis());
        assertEquals(1L, remainder);
    }
}