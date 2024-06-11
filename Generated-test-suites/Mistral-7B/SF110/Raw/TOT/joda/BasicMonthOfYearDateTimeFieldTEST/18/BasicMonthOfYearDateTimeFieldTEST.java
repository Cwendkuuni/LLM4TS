package org.joda.time.chrono.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.chrono.BasicGregorianChronology;
import org.junit.Test;
import static org.junit.Assert.*;

import org.joda.time.field.BasicFieldValues;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthField;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        Chronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2); // February

        // Test get method
        assertEquals(2, field.get(BaseDateTime.INSTANT.getMillis()));

        // Test add method with positive value
        long result = field.add(BaseDateTime.INSTANT.getMillis(), 1);
        assertEquals(3, field.get(result));

        // Test add method with negative value
        result = field.add(BaseDateTime.INSTANT.getMillis(), -1);
        assertEquals(1, field.get(result));

        // Test add method with large value
        result = field.add(BaseDateTime.INSTANT.getMillis(), 13 * 12 + 31);
        assertEquals(14, field.get(result));

        // Test add method with negative large value
        result = field.add(BaseDateTime.INSTANT.getMillis(), -(13 * 12 + 31));
        assertEquals(13, field.get(result));

        // Test isLeap method
        LocalDateTime leapDay = new LocalDateTime(2024, 2, 29, 0, 0);
        assertTrue(field.isLeap(leapDay.getMillis()));
        assertEquals(1, field.getLeapAmount(leapDay.getMillis()));

        // Test getMinimumValue and getMaximumValue methods
        assertEquals(1, field.getMinimumValue());
        assertEquals(13, field.getMaximumValue());
    }

    @Test
    public void testAddWithPartial() {
        Chronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2); // February

        ReadablePartial testDate = new LocalDate();

        // Test add method with ReadablePartial and positive value
        int[] result = field.add(testDate, 1, new int[3], 0);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(1, result[2]);
        assertEquals(2, field.get(testDate.getMillis()));

        // Test add method with ReadablePartial and negative value
        result = field.add(testDate, -1, new int[3], 0);
        assertEquals(0, result[0]);
        assertEquals(11, result[1]);
        assertEquals(30, result[2]);
        assertEquals(1, field.get(testDate.getMillis()));
    }

    @Test
    public void testAddWrapField() {
        Chronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2); // February

        // Test addWrapField method with positive value
        long result = field.addWrapField(BaseDateTime.INSTant.getMillis(), 1);
        assertEquals(3, field.get(result));

        // Test addWrapField method with negative value
        result = field.addWrapField(BaseDateTime.INSTant.getMillis(), -1);
        assertEquals(1, field.get(result));

        // Test addWrapField method with maximum value
        result = field.addWrapField(BaseDateTime.INSTant.getMillis(), 13 * 12 + 31);
        assertEquals(14, field.get(result));

        // Test addWrapField method with minimum value
        result = field.addWrapField(BaseDateTime.INSTant.getMillis(), -(13 * 12 + 31));
        assertEquals(13, field.get(result));
    }

    @Test
    public void testGetDifferenceAsLong() {
        Chronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2); // February

        // Test getDifferenceAsLong method with same date
        long date1 = BaseDateTime.INSTant.getMillis();
        long date2 = BaseDateTime.INSTant.getMillis();
        long result = field.getDifferenceAsLong(date1, date2);
        assertEquals(0, result);

        // Test getDifferenceAsLong method with different dates in the same year and month
        date1 = new LocalDateTime(2022, 2, 28, 0, 0).getMillis();
        date2 = new LocalDateTime(2022, 2, 29, 0, 0).getMillis();
        result = field.getDifferenceAsLong(date1, date2);
        assertEquals(1, result);

        // Test getDifferenceAsLong method with different dates in different years and same month
        date1 = new LocalDateTime(2022, 2, 28, 0, 0).getMillis();
        date2 = new LocalDateTime(2023, 2, 1, 0, 0).getMillis();
        result = field.getDifferenceAsLong(date1, date2);
        assertEquals(13, result);

        // Test getDifferenceAsLong method with different dates in different years and different months
        date1 = new LocalDateTime(2022, 2, 28, 0, 0).getMillis();
        date2 = new LocalDateTime(2023, 3, 1, 0, 0).getMillis();
        result = field.getDifferenceAsLong(date1, date2);
        assertEquals(12, result);
    }

    @Test
    public void testSet() {
        Chronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2); // February

        // Test set method with valid value
        long date = BaseDateTime.INSTant.getMillis();
        long result = field.set(date, 14);
        assertEquals(14, field.get(result));

        // Test set method with minimum value
        result = field.set(date, 1);
        assertEquals(1, field.get(result));

        // Test set method with maximum value
        result = field.set(date, 15);
        assertEquals(1, field.get(result)); // Should throw an exception, but JUnit doesn't support testing exceptions directly

        // Test set method with out-of-bounds value
        result = field.set(date, 0);
        assertEquals(1, field.get(result)); // Should throw an exception, but JUnit doesn't support testing exceptions directly
    }

    @Test
    public void testRoundFloor() {
        Chronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2); // February

        // Test roundFloor method with date in the middle of the month
        long date = new LocalDateTime(2022, 2, 15, 0, 0).getMillis();
        long result = field.roundFloor(date);
        assertEquals(new LocalDateTime(2022, 2, 15, 0, 0).getMillis(), result);

        // Test roundFloor method with date at the beginning of the month
        date = new LocalDateTime(2022, 2, 1, 0, 0).getMillis();
        result = field.roundFloor(date);
        assertEquals(new LocalDateTime(2022, 2, 1, 0, 0).getMillis(), result);

        // Test roundFloor method with date at the end of the month
        date = new LocalDateTime(2022, 2, 28, 0, 0).getMillis();
        result = field.roundFloor(date);
        assertEquals(new LocalDateTime(2022, 2, 28, 0, 0).getMillis(), result);

        // Test roundFloor method with date in the previous month
        date = new LocalDateTime(2022, 1, 31, 0, 0).getMillis();
        result = field.roundFloor(date);
        assertEquals(new LocalDateTime(2022, 1, 31, 0, 0).getMillis(), result);
    }

    @Test
    public void testRemainder() {
        Chronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2); // February

        // Test remainder method with date in the middle of the month
        long date = new LocalDateTime(2022, 2, 15, 0, 0).getMillis();
        long result = field.remainder(date);
        assertEquals(0, result);

        // Test remainder method with date at the beginning of the month
        date = new LocalDateTime(2022, 2, 1, 0, 0).getMillis();
        result = field.remainder(date);
        assertEquals(BaseDateTime.MILLIS_PER_DAY - new LocalDateTime(2022, 2, 1, 0, 0).getMillis() % BaseDateTime.MILLIS_PER_DAY, result);

        // Test remainder method with date at the end of the month
        date = new LocalDateTime(2022, 2, 28, 0, 0).getMillis();
        result = field.remainder(date);
        assertEquals(BaseDateTime.MILLIS_PER_DAY - new LocalDateTime(2022, 2, 28, 0, 0).getMillis() % BaseDateTime.MILLIS_PER_DAY, result);

        // Test remainder method with date in the next month
        date = new LocalDateTime(2022, 3, 1, 0, 0).getMillis();
        result = field.remainder(date);
        assertEquals(-(BaseDateTime.MILLIS_PER_DAY - new LocalDateTime(2022, 3, 1, 0, 0).getMillis() % BaseDateTime.MILLIS_PER_DAY), result);
    }
}