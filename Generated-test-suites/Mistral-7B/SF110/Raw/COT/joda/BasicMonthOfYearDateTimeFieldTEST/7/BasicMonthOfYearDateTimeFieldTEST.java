package org.joda.time.test.chrono;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthAdjustable;
import org.joda.time.field.MonthOfYearField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("Test Chronology", DateTimeConstants.ISO), 2);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet_Valid() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("Test Chronology", DateTimeConstants.ISO), 2);
        Chronology chrono = new BasicChronology("Test Chronology", DateTimeConstants.ISO);
        long testDate = chrono.getMillis(2022, 1, 1);
        int expected = 1;

        int result = field.get(testDate);
        assertEquals(expected, result);
    }

    @Test
    public void testGet_EdgeCases() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("Test Chronology", DateTimeConstants.ISO), 2);

        // Minimum value
        long testDateMin = Long.MIN_VALUE;
        int expectedMin = 1;
        int resultMin = field.get(testDateMin);
        assertEquals(expectedMin, resultMin);

        // Maximum value
        long testDateMax = Long.MAX_VALUE;
        int expectedMax = Integer.MAX_VALUE;
        int resultMax = field.get(testDateMax);
        assertEquals(expectedMax, resultMax);
    }

    @Test
    public void testAdd_Valid() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("Test Chronology", DateTimeConstants.ISO), 2);
        Chronology chrono = new BasicChronology("Test Chronology", DateTimeConstants.ISO);
        long testDate = chrono.getMillis(2022, 1, 1);

        long expected = chrono.getMillis(2022, 2, 1);
        long result = field.add(testDate, 1);
        assertEquals(expected, result);
    }

    @Test
    public void testAdd_LeapYear() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("Test Chronology", DateTimeConstants.ISO), 2);
        Chronology chrono = new BasicChronology("Test Chronology", DateTimeConstants.ISO);
        long testDate = chrono.getMillis(2024, 1, 1);

        long expected = chrono.getMillis(2024, 3, 1);
        long result = field.add(testDate, 1);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_OutOfBounds() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("Test Chronology", DateTimeConstants.ISO), 2);
        Chronology chrono = new BasicChronology("Test Chronology", DateTimeConstants.ISO);
        long testDate = chrono.getMillis(2022, 1, 1);

        long tooFar = testDate + (Long.MAX_VALUE / 12 * 12);
        long result = field.add(testDate, 1);
        fail("Expected IllegalArgumentException but got: " + result);
    }

    @Test
    public void testAdd_WrapAround() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("Test Chronology", DateTimeConstants.ISO), 2);
        Chronology chrono = new BasicChronology("Test Chronology", DateTimeConstants.ISO);
        long testDate = chrono.getMillis(2000, 12, 31);

        long expected = chrono.getMillis(1999, 1, 1);
        long result = field.add(testDate, -1);
        assertEquals(expected, result);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("Test Chronology", DateTimeConstants.ISO), 2);
        long testDate = 1609459200000L;//2021-02-28
        int fieldValue = 13;

        long result = field.addWrapField(testDate, fieldValue);
        assertEquals(testDate + (fieldValue * 1000 * 60 * 60 * 24), result);
    }

    // Add test cases for other methods like add(ReadablePartial, int, int[], int), getDifferenceAsLong(long, long), set(long, int), getRangeDurationField(), isLeap(long), getLeapAmount(long), getLeapDurationField(), getMinimumValue(), getMaximumValue(), roundFloor(long), remainder(long), readResolve()

}