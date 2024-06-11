package org.joda.time.chrono.test;

import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationFields;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.field.FieldUtils;

import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        Chronology julianChronology = JulianChronology.getInstance();
        BasicMonthOfYearDateTimeField monthOfYearField = new BasicMonthOfYearDateTimeField(julianChronology, 2); // February

        // Test getter and setter methods
        assertEquals(1, monthOfYearField.get(0L));
        assertEquals(12, monthOfYearField.set(0L, 12));
        assertEquals(12, monthOfYearField.get(0L));

        // Test isLenient method
        assertFalse(monthOfYearField.isLenient());

        // Test add method with positive value
        long testDate = new BaseDateTime(0, 2018, 1, 1, 0, 0).getMillis();
        long resultDate = monthOfYearField.add(testDate, 6);
        LocalDateTime expectedResult = new LocalDateTime(2018, 7, 7, 0, 0);
        assertEquals(expectedResult.getMillis(), resultDate);

        // Test add method with negative value
        resultDate = monthOfYearField.add(testDate, -5);
        expectedResult = new LocalDateTime(2018, 6, 25, 0, 0);
        assertEquals(expectedResult.getMillis(), resultDate);

        // Test add method with large value
        resultDate = monthOfYearField.add(testDate, 1000);
        long expectedResultMillis = new BaseDateTime(0, 2019, 4, 2, 0, 0).getMillis();
        assertEquals(expectedResultMillis, resultDate);

        // Test add method with out-of-bounds value
        try {
            resultDate = monthOfYearField.add(testDate, 123456789);
            fail("Exception should be thrown due to large add amount.");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        // Test add method with null argument
        try {
            monthOfYearField.add(null, 12);
            fail("Exception should be thrown due to null argument.");
        } catch (NullPointerException e) {
            assertTrue(true);
        }

        // Test addWrapField method
        long testDateWrap = new BaseDateTime(0, 2018, 1, 31, 0, 0).getMillis();
        long resultDateWrap = monthOfYearField.addWrapField(testDateWrap, 13);
        LocalDate expectedResultDate = new LocalDate(2018, 2, 14);
        assertEquals(expectedResultDate.getMillis(), resultDateWrap);

        // Test getDifferenceAsLong method
        long testDateDifference = new BaseDateTime(0, 2018, 2, 28, 0, 0).getMillis();
        long resultDateDifference = monthOfYearField.getDifferenceAsLong(testDateDifference, testDate);
        assertEquals(29, resultDateDifference);

        // Test set method
        long testDateSet = new BaseDateTime(0, 2018, 1, 1, 0, 0).getMillis();
        long resultDateSet = monthOfYearField.set(testDateSet, 5);
        LocalDateTime expectedResultSet = new LocalDateTime(2018, 1, 5, 0, 0);
        assertEquals(expectedResultSet.getMillis(), resultDateSet);

        // Test getRangeDurationField method
        DurationField durationField = monthOfYearField.getRangeDurationField();
        assertEquals(DurationFieldType.YEARS, durationField.getType());
        assertEquals(1, durationField.getValues()[0]);

        // Test isLeap method
        long testDateLeap = new BaseDateTime(0, 2018, 2, 29, 0, 0).getMillis();
        assertTrue(monthOfYearField.isLeap(testDateLeap));

        // Test getLeapAmount method
        long testDateLeapAmount = new BaseDateTime(0, 2018, 2, 28, 0, 0).getMillis();
        int resultLeapAmount = monthOfYearField.getLeapAmount(testDateLeapAmount);
        assertEquals(0, resultLeapAmount);

        // Test getLeapDurationField method
        DurationField leapDurationField = monthOfYearField.getLeapDurationField();
        assertEquals(DurationFieldType.DAYS, leapDurationField.getType());
        assertEquals(1, leapDurationField.getValues()[0]);

        // Test getMinimumValue method
        int minimumValue = monthOfYearField.getMinimumValue();
        assertEquals(1, minimumValue);

        // Test getMaximumValue method
        int maximumValue = monthOfYearField.getMaximumValue();
        assertEquals(12, maximumValue);

        // Test roundFloor method
        long testDateRound = new BaseDateTime(0, 2018, 1, 31, 0, 0).getMillis();
        long resultDateRound = monthOfYearField.roundFloor(testDateRound);
        LocalDate expectedResultDateRound = new LocalDate(2018, 1, 31);
        assertEquals(expectedResultDateRound.getMillis(), resultDateRound);

        // Test remainder method
        long testDateRemainder = new BaseDateTime(0, 2018, 1, 31, 0, 1).getMillis();
        long resultDateRemainder = monthOfYearField.remainder(testDateRemainder);
        assertEquals(1, resultDateRemainder);

        // Test add method with ReadablePartial argument
        LocalDateTime readablePartial = new LocalDateTime(2018, 1, 1, 0, 0);
        long[] testArray = {0, 1, 2, 3};
        int resultIndex = monthOfYearField.add(readablePartial, 2, testArray, 4).length;
        int expectedIndex = 3;
        assertEquals(expectedIndex, resultIndex);

        // Test addWrapField method with out-of-bounds value
        long testDateWrapOutOfBounds = new BaseDateTime(0, 2018, 1, 32, 0, 0).getMillis();
        try {
            resultDateWrap = monthOfYearField.addWrapField(testDateWrapOutOfBounds, 13);
            fail("Exception should be thrown due to out-of-bounds add amount.");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        // Test add method with negative ReadablePartial argument
        LocalDateTime readablePartialNegative = new LocalDateTime(2018, 1, 1, 0, 0).withMonthOfYear(0);
        long resultDateAddNegative = monthOfYearField.add(readablePartialNegative.getMillis(), 2);
        LocalDateTime expectedResultDateAddNegative = new LocalDateTime(2017, 12, 30, 0, 0);
        assertEquals(expectedResultDateAddNegative.getMillis(), resultDateAddNegative);

        // Test add method with large ReadablePartial argument
        long[] testArrayLarge = {Long.MAX_VALUE, Long.MAX_VALUE};
        LocalDateTime readablePartialLarge = new LocalDateTime(2018, 1, 1, 0, 0).withFields(FieldUtils.localDateFields(testArrayLarge));
        long resultDateAddLarge = monthOfYearField.add(readablePartialLarge.getMillis(), 1);
        long expectedResultDateAddLargeMillis = new BaseDateTime(0, 2018, 1, 1, 0, 1).getMillis();
        assertEquals(expectedResultDateAddLargeMillis, resultDateAddLarge);
    }
}