package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicConstruction() {
        BasicChronology gregorianChrono = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChrono, 2);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
        assertEquals(gregorianChrono.getAverageMillisPerMonth(), field.getPrecision());
    }

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicChronology gregorianChrono = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChrono, 2);
        long testDate = LocalDate.of(2023, 2, 28).toInstant().toEpochMilli();
        int expectedMonth = 2;
        int actualMonth = field.get(testDate);
        assertEquals(expectedMonth, actualMonth);
    }

    @Test
    public void testAdd() {
        BasicChronology gregorianChrono = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChrono, 2);
        long testDate = LocalDate.of(2023, 2, 28).toInstant().toEpochMilli();
        long expectedDate = LocalDate.of(2023, 4, 28).toInstant().toEpochMilli();
        long actualDate = field.add(testDate, 2);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testAddWithLong() {
        BasicChronology gregorianChrono = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChrono, 2);
        long testDate = LocalDate.of(2023, 2, 28).toInstant().toEpochMilli();
        long expectedDate = LocalDate.of(2023, 4, 28).toInstant().toEpochMilli();
        long actualDate = field.add(testDate, 2L);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testAddWrapField() {
        BasicChronology gregorianChrono = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChrono, 2);
        long testDate = LocalDate.of(2023, 1, 28).toInstant().toEpochMilli();
        long expectedDate = LocalDate.of(2023, 13, 28).toInstant().toEpochMilli();
        long actualDate = field.addWrapField(testDate, 11);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology gregorianChrono = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChrono, 2);
        long testDate1 = LocalDate.of(2023, 2, 28).toInstant().toEpochMilli();
        long testDate2 = LocalDate.of(2023, 3, 31).toInstant().toEpochMilli();
        long expectedDifference = 30;
        long actualDifference = field.getDifferenceAsLong(testDate1, testDate2);
        assertEquals(expectedDifference, actualDifference);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithTooLargeArgument() {
        BasicChronology gregorianChrono = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChrono, 2);
        long testDate = LocalDate.of(2023, 1, 28).toInstant().toEpochMilli();
        long tooLargeArgument = Long.MAX_VALUE + 1;
        field.add(testDate, tooLargeArgument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithNegativeArgument() {
        BasicChronology gregorianChrono = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChrono, 2);
        long testDate = LocalDate.of(2023, 1, 28).toInstant().toEpochMilli();
        long negativeArgument = -1;
        field.add(testDate, negativeArgument);
    }

    @Test
    public void testSet() {
        BasicChronology gregorianChrono = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChrono, 2);
        long testDate = LocalDate.of(2023, 2, 28).toInstant().toEpochMilli();
        int expectedMonth = 4;
        long actualDate = field.set(testDate, expectedMonth);
        assertEquals(expectedMonth, field.get(actualDate));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithInvalidArgument() {
        BasicChronology gregorianChrono = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChrono, 2);
        long testDate = LocalDate.of(2023, 2, 28).toInstant().toEpochMilli();
        int invalidArgument = 14;
        field.set(testDate, invalidArgument);
    }

    @Test
    public void testGetMaximumValue() {
        BasicChronology gregorianChrono = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChrono, 2);
        int expectedMax = GregorianChronology.getInstance().getMaxMonth();
        int actualMax = field.getMaximumValue();
        assertEquals(expectedMax, actualMax);
    }

    @Test
    public void testGetMinimumValue() {
        BasicChronology gregorianChrono = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChrono, 2);
        int expectedMin = 1;
        int actualMin = field.getMinimumValue();
        assertEquals(expectedMin, actualMin);
    }

    @Test(expected = IllegalStateException.class)
    public void testObjectReadResolve() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(BasicChronology.UTC, 2);
        field = (BasicMonthOfYearDateTimeField) field.readResolve();
    }
}