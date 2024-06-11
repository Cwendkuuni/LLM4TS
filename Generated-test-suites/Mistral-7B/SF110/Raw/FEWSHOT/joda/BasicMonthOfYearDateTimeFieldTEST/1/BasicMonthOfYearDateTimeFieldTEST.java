package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.field.*;
import org.joda.time.test.BaseTestCase;

public class BasicMonthOfYearDateTimeFieldTest extends BaseTestCase {

    @Test
    public void testBasicConstruction() {
        BasicChronology chrono = new GregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 2);
        assertEquals(DateTimeFieldType.monthOfYear(), testField.getFieldType());
        assertEquals(chrono, testField.getChronology());
        assertEquals(2, testField.getMaxValue());
        assertFalse(testField.isLenient());
    }

    @Test
    public void testGet() {
        BasicChronology chrono = new GregorianChronology();
        DateTime date = new DateTime(2022, 1, 1, 0, 0, 0, 0, chrono);
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 3);
        int month = testField.get(date.getMillis());
        assertEquals(1, month);

        date = new DateTime(2022, 3, 1, 0, 0, 0, 0, chrono);
        month = testField.get(date.getMillis());
        assertEquals(3, month);

        date = new DateTime(2022, 6, 1, 0, 0, 0, 0, chrono);
        month = testField.get(date.getMillis());
        assertEquals(6, month);

        date = new DateTime(2023, 2, 1, 0, 0, 0, 0, chrono);
        month = testField.get(date.getMillis());
        assertEquals(2, month);

        date = new DateTime(2023, 13, 1, 0, 0, 0, 0, chrono);
        month = testField.get(date.getMillis());
        assertEquals(1, month);
    }

    @Test
    public void testAdd() {
        BasicChronology chrono = new GregorianChronology();
        DateTime date = new DateTime(2022, 1, 1, 0, 0, 0, 0, chrono);
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 3);

        long result = testField.add(date.getMillis(), 1);
        DateTime resultDateTime = new DateTime(result);
        assertEquals(new DateTime(2022, 2, 1, 0, 0, 0, 0, chrono), resultDateTime);

        result = testField.add(date.getMillis(), 2);
        resultDateTime = new DateTime(result);
        assertEquals(new DateTime(2022, 3, 1, 0, 0, 0, 0, chrono), resultDateTime);

        result = testField.add(date.getMillis(), 13);
        resultDateTime = new DateTime(result);
        assertEquals(new DateTime(2022, 4, 1, 0, 0, 0, 0, chrono), resultDateTime);

        result = testField.add(date.getMillis(), 100);
        resultDateTime = new DateTime(result);
        assertEquals(new DateTime(2022, 5, 1, 0, 0, 0, 0, chrono), resultDateTime);

        result = testField.add(date.getMillis(), -1);
        resultDateTime = new DateTime(result);
        assertEquals(new DateTime(2022, 12, 31, 23, 59, 59, 999, chrono), resultDateTime); // Last day of previous year

        result = testField.add(date.getMillis(), -2);
        resultDateTime = new DateTime(result);
        assertEquals(new DateTime(2021, 12, 31, 23, 59, 59, 999, chrono), resultDateTime); // Last day of previous year
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chrono = new GregorianChronology();
        DateTime date = new DateTime(2022, 12, 31, 0, 0, 0, 0, chrono);
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 12);

        long result = testField.addWrapField(date.getMillis(), 13);
        DateTime resultDateTime = new DateTime(result);
        assertEquals(new DateTime(2023, 1, 1, 0, 0, 0, 0, chrono), resultDateTime);
    }

    @Test
    public void testAddWrapFieldNegative() {
        BasicChronology chrono = new GregorianChronology();
        DateTime date = new DateTime(2023, 1, 1, 0, 0, 0, 0, chrono);
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 12);

        long result = testField.addWrapField(date.getMillis(), -13);
        DateTime resultDateTime = new DateTime(result);
        assertEquals(new DateTime(2022, 12, 31, 0, 0, 0, 0, chrono), resultDateTime);
    }

    @Test
    public void testAddWrapping() {
        BasicChronology chrono = new GregorianChronology();
        DateTime date = new DateTime(2022, 12, 31, 0, 0, 0, 0, chrono);
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 12);

        long result = testField.add(date.getMillis(), 13);
        DateTime resultDateTime = new DateTime(result);
        assertEquals(new DateTime(2023, 1, 1, 0, 0, 0, 0, chrono), resultDateTime);

        result = testField.add(result, -12);
        resultDateTime = new DateTime(result);
        assertEquals(new DateTime(2022, 12, 31, 0, 0, 0, 0, chrono), resultDateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTooLarge() {
        BasicChronology chrono = new GregorianChronology();
        DateTime date = new DateTime(2022, 12, 31, 0, 0, 0, 0, chrono);
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 12);

        long result = testField.add(date.getMillis(), Integer.MAX_VALUE + 1);
    }

    @Test
    public void testAddPartial() {
        BasicChronology chrono = new GregorianChronology();
        DateTime date = new DateTime(2022, 1, 1, 0, 0, 0, 0, chrono);
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 3);

        ReadablePartial readablePartial = new DateTimeFieldsMutablePartial(DateTimeFieldType.dayOfMonth(), 31);

        int[] result = testField.add(readablePartial, 1, new int[3], 1);
        assertEquals(new int[]{31, 1}, result);

        result = testField.add(readablePartial, 1, new int[3], 2);
        assertEquals(new int[]{1, 1}, result);

        result = testField.add(readablePartial, -1, new int[3], 1);
        assertEquals(new int[]{30, 12}, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chrono = new GregorianChronology();
        DateTime date1 = new DateTime(2022, 1, 1, 0, 0, 0, 0, chrono);
        DateTime date2 = new DateTime(2022, 2, 1, 0, 0, 0, 0, chrono);
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 3);

        long result = testField.getDifferenceAsLong(date2.getMillis(), date1.getMillis());
        assertEquals(30, result);
    }

    @Test
    public void testSet() {
        BasicChronology chrono = new GregorianChronology();
        DateTime date = new DateTime(2022, 1, 1, 0, 0, 0, 0, chrono);
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 3);

        long result = testField.set(date.getMillis(), 2);
        DateTime resultDateTime = new DateTime(result);
        assertEquals(new DateTime(2022, 2, 1, 0, 0, 0, 0, chrono), resultDateTime);

        result = testField.set(date.getMillis(), 13);
        resultDateTime = new DateTime(result);
        assertEquals(new DateTime(2022, 3, 1, 0, 0, 0, 0, chrono), resultDateTime);

        result = testField.set(date.getMillis(), 0);
        resultDateTime = new DateTime(result);
        assertEquals(new DateTime(2022, 1, 1, 0, 0, 0, 0, chrono), resultDateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOutOfBounds() {
        BasicChronology chrono = new GregorianChronology();
        DateTime date = new DateTime(2022, 1, 1, 0, 0, 0, 0, chrono);
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 3);

        testField.set(date.getMillis(), 130);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicChronology chrono = new GregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 3);

        DurationField result = testField.getRangeDurationField();
        assertEquals(DurationFieldType.years(), result.getFieldType());
    }

    @Test
    public void testIsLeap() {
        BasicChronology chrono = new GregorianChronology();
        DateTime date = new DateTime(2022, 1, 1, 0, 0, 0, 0, chrono);
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 2);

        assertTrue(testField.isLeap(date.getMillis()));
        assertTrue(testField.isLeap(new DateTime(2022, 2, 28, 0, 0, 0, 0, chrono).getMillis()));

        date = new DateTime(2023, 1, 1, 0, 0, 0, 0, chrono);
        assertFalse(testField.isLeap(date.getMillis()));
        assertFalse(testField.isLeap(new DateTime(2023, 2, 28, 0, 0, 0, 0, chrono).getMillis()));

        date = new DateTime(2023, 2, 29, 0, 0, 0, 0, chrono);
        assertTrue(testField.isLeap(date.getMillis()));
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chrono = new GregorianChronology();
        DateTime date = new DateTime(2022, 1, 1, 0, 0, 0, 0, chrono);
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 2);

        assertEquals(0, testField.getLeapAmount(date.getMillis()));
        assertEquals(1, testField.getLeapAmount(new DateTime(2022, 2, 28, 0, 0, 0, 0, chrono).getMillis()));

        date = new DateTime(2023, 1, 1, 0, 0, 0, 0, chrono);
        assertEquals(0, testField.getLeapAmount(date.getMillis()));
        assertEquals(0, testField.getLeapAmount(new DateTime(2023, 2, 28, 0, 0, 0, 0, chrono).getMillis()));

        date = new DateTime(2023, 2, 29, 0, 0, 0, 0, chrono);
        assertEquals(1, testField.getLeapAmount(date.getMillis()));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicChronology chrono = new GregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 2);

        DurationField result = testField.getLeapDurationField();
        assertEquals(DurationFieldType.days(), result.getFieldType());
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 1);

        assertEquals(1, testField.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(new GregorianChronology(), 12);

        assertEquals(12, testField.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicChronology chrono = new GregorianChronology();
        DateTime date = new DateTime(2022, 1, 1, 0, 0, 0, 0, chrono);
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 3);

        long result = testField.roundFloor(date.getMillis());
        assertEquals(new DateTime(2022, 1, 1, 0, 0, 0, 0, chrono).getMillis(), result);
    }

    @Test
    public void testRemainder() {
        BasicChronology chrono = new GregorianChronology();
        DateTime date = new DateTime(2022, 1, 1, 0, 0, 0, 0, chrono);
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 3);

        long result = testField.remainder(date.getMillis());
        assertEquals(0, result);
    }

    @Test
    public void testSetWraps() {
        BasicChronology chrono = new GregorianChronology();
        DateTime date = new DateTime(2022, 12, 31, 0, 0, 0, 0, chrono);
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chrono, 12);

        long result = testField.set(date.getMillis(), 13);
        assertEquals(new DateTime(2023, 1, 1, 0, 0, 0, 0, chrono).getMillis(), result);
    }
}