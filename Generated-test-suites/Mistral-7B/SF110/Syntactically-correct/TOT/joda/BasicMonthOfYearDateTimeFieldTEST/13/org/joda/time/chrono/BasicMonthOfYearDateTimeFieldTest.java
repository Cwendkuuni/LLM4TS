package org.joda.time.chrono.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationFieldTypeFactory;
import org.joda.time.ImmutableDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        Chronology isoChronology = org.joda.time.calendar.ISOCalendar.getInstance();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(isoChronology, 2);

        // Test isLenient()
        assertFalse(testField.isLenient());

        // Test get()
        long testDate = new MutableDateTime().withChronology(isoChronology).withYear(2019).withMonthOfYear(3).getMillis();
        int month = testField.get(testDate);
        assertEquals(3, month);

        // Test add() with valid inputs
        long newDate = testField.add(testDate, 1);
        assertEquals(4, testField.get(newDate));

        long anotherDate = new MutableDateTime().withChronology(isoChronology).withYear(2019).withMonthOfYear(12).getMillis();
        newDate = testField.add(anotherDate, -2);
        assertEquals(10, testField.get(newDate));

        // Test add() with negative inputs
        long negativeDate = testField.add(anotherDate, -12);
        assertEquals(1, testField.get(negativeDate));

        // Test add() with zero input
        long zeroDate = testField.add(testDate, 0);
        assertEquals(3, testField.get(zeroDate));

        // Test add() with overflow input
        long overflowDate = testField.add(testDate, 13);
        assertEquals(4, testField.get(overflowDate));

        // Test add() with long input
        long longDate = testField.add(testDate, 100);
        assertEquals(4, testField.get(longDate));

        // Test addWrapField()
        long wrappedDate = testField.addWrapField(anotherDate, 12);
        assertEquals(3, testField.get(wrappedDate));

        // Test add() with ReadablePartial and int[] array
        ReadablePartial readablePartial = new MutableDateTime().withChronology(isoChronology).withYear(2019).withMonthOfYear(4).toDateTime();
        int[] resultArray = new int[2];
        testField.add(readablePartial, 1, resultArray, 2);
        assertEquals(5, resultArray[0]);
        assertEquals(2, resultArray[1]);

        // Test add() with ReadablePartial and long
        long testDate2 = new MutableDateTime().withChronology(isoChronology).withYear(2019).withMonthOfYear(5).getMillis();
        ImmutableDateTime newDateTime = new MutableDateTime(testDate2).plus(DurationFieldType.standardDays(3));
        resultArray = new int[2];
        testField.add(newDateTime, 1, resultArray, 2);
        assertEquals(6, resultArray[0]);
        assertEquals(2, resultArray[1]);

        // Test getDifferenceAsLong()
        long date1 = new MutableDateTime().withChronology(isoChronology).withYear(2019).withMonthOfYear(1).getMillis();
        long date2 = new MutableDateTime().withChronology(isoChronology).withYear(2019).withMonthOfYear(2).getMillis();
        long difference = testField.getDifferenceAsLong(date1, date2);
        assertEquals(1, difference);

        // Test set()
        long setDate = testField.set(testDate, 5);
        assertEquals(5, testField.get(setDate));

        // Test getRangeDurationField()
        DurationFieldType rangeDurationField = testField.getRangeDurationField();
        assertEquals(DurationFieldType.YEARS, rangeDurationField);

        // Test isLeap()
        long leapDate = new MutableDateTime().withChronology(isoChronology).withYear(2019).withMonthOfYear(2).getMillis();
        assertTrue(testField.isLeap(leapDate));

        // Test getLeapAmount()
        int leapAmount = testField.getLeapAmount(leapDate);
        assertEquals(1, leapAmount);

        // Test getLeapDurationField()
        DurationFieldType leapDurationField = testField.getLeapDurationField();
        assertEquals(DurationFieldType.DAYS, leapDurationField);

        // Test getMinimumValue()
        int minValue = testField.getMinimumValue();
        assertEquals(1, minValue);

        // Test getMaximumValue()
        int maxValue = testFormat.getMaximumValue();
        assertEquals(12, maxValue);

        // Test roundFloor()
        long roundedDate = testField.roundFloor(testDate);
        assertEquals(new MutableDateTime().withChronology(isoChronology).withYear(2019).withMonthOfYear(3).getMillis(), roundedDate);

        // Test remainder()
        long remainingDate = testField.remainder(testDate);
        assertEquals(0, remainingDate);
    }

    @Test
    public void testEdgeCases() {
        // Minimum and maximum values
        Chronology gregorianChronology = org.joda.time.calendar.GregorianCalendar.getInstance();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(gregorianChronology, 2);
        long minDate = testField.getMinimumValueMillis(gregorianChronology);
        long maxDate = testField.getMaximumValueMillis(gregorianChronology);
        assertEquals(testField.getMinimumValue(), testField.get(minDate));
        assertEquals(testField.getMaximumValue(), testField.get(maxDate));

        // Test adding and getting the minimum and maximum values
        long minTestDate = testField.add(minDate, 1);
        assertEquals(testField.getMinimumValue(), testField.get(minTestDate));

        long maxTestDate = testField.add(maxDate, -1);
        assertEquals(testField.getMaximumValue(), testField.get(maxTestDate));

        // Test adding the minimum value to the maximum value
        long sumTestDate = testField.add(minDate, testField.getMaximumValue());
        assertEquals(maxDate, sumTestDate);

        // Test adding the maximum value to the minimum value
        long sumTestDate2 = testField.add(maxDate, testField.getMinimumValue());
        assertEquals(minDate, sumTestDate2);

        // Test adding the maximum value to itself
        long addMaxTestDate = testField.add(maxDate, testField.getMaximumValue());
        assertEquals(maxDate, addMaxTestDate);

        // Test adding the minimum value to itself
        long addMinTestDate = testField.add(minDate, testField.getMinimumValue());
        assertEquals(minDate, addMinTestDate);

        // Test adding a negative value to the minimum value
        long subMinTestDate = testField.add(minDate, -1);
        assertEquals(maxDate, subMinTestDate);

        // Test adding a negative value to the maximum value
        long subMaxTestDate = testField.add(maxDate, -1);
        assertEquals(minDate, subMaxTestDate);

        // Test adding a negative value to a date between minimum and maximum values
        long betweenMinAndMaxTestDate = testField.add(minDate, testField.getMaximumValue() / 2);
        long subBetweenMinAndMaxTestDate = testField.add(betweenMinAndMaxTestDate, -1);
        assertEquals(minDate, subBetweenMinAndMaxTestDate);
    }

    @Test
    public void testErrorScenarios() {
        // Test IllegalArgumentException when adding an invalid value
        Chronology unknownChronology = new UnknownChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(unknownChronology, 2);
        try {
            testField.add(Long.MIN_VALUE, 1);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: " + Long.MIN_VALUE, e.getMessage());
        }

        try {
            testField.add(Long.MAX_VALUE, 1);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: " + Long.MAX_VALUE, e.getMessage());
        }

        try {
            testField.add(Long.MIN_VALUE, testField.getMaximumValue());
        } catch (IllegalArgumentException e) {
            assertEquals("Magnitude of add amount is too large: " + testField.getMaximumValue(), e.getMessage());
        }

        try {
            testField.add(Long.MIN_VALUE, -1);
        } catch (IllegalArgumentException e) {
            assertEquals("Magnitude of add amount is too large: " + Long.MIN_VALUE, e.getMessage());
        }

        try {
            testField.add(Long.MAX_VALUE, -1);
        } catch (IllegalArgumentException e) {
            assertEquals("Magnitude of add amount is too large: " + Long.MAX_VALUE, e.getMessage());
        }

        // Test IllegalArgumentException when setting an invalid value
        try {
            testField.set(Long.MIN_VALUE, 1);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: " + Long.MIN_VALUE, e.getMessage());
        }

        try {
            testField.set(Long.MAX_VALUE, 1);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: " + Long.MAX_VALUE, e.getMessage());
        }

        try {
            testField.set(Long.MAX_VALUE, testField.getMaximumValue() + 1);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: " + (testField.getMaximumValue() + 1), e.getMessage());
        }

        try {
            testField.set(Long.MIN_VALUE, testField.getMinimumValue() - 1);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: " + (testField.getMinimumValue() - 1), e.getMessage());
        }

        try {
            testField.set(Long.MIN_VALUE, testField.getMinimumValue());
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: " + Long.MIN_VALUE, e.getMessage());
        }

        // Test IllegalArgumentException when adding ReadablePartial and an invalid value
        ReadablePartial readablePartial = new MutableDateTime().withChronology(isoChronology).withYear(2019).withMonthOfYear(3).toDateTime();
        int[] array = new int[2];
        try {
            testField.add(readablePartial, Long.MIN_VALUE, array, 2);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: " + Long.MIN_VALUE, e.getMessage());
        }

        try {
            testField.add(readablePartial, Long.MAX_VALUE, array, 2);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: " + Long.MAX_VALUE, e.getMessage());
        }
    }
}