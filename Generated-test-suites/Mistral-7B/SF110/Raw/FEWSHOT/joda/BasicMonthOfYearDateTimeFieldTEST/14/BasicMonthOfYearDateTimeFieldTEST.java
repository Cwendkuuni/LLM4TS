package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.CalendarUtils;
import org.joda.time.field.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicConstruction() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getType());
        assertEquals(chronology.getAverageMillisPerMonth(), field.getBaseMillis());
    }

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        long testDate = chronology.getMillis("2022-03-31T12:30:00.000Z");
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        int expected = 3;
        int result = field.get(testDate);
        assertEquals(expected, result);
    }

    @Test
    public void testAdd() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        long testDate = chronology.getMillis("2022-03-31T12:30:00.000Z");
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long expected = chronology.getMillis("2022-04-30T12:30:00.000Z");
        long result = field.add(testDate, 1);
        assertEquals(expected, result);

        expected = chronology.getMillis("2022-02-28T12:30:00.000Z");
        result = field.add(testDate, -1);
        assertEquals(expected, result);
    }

    @Test
    public void testAddLong() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        long testDate = chronology.getMillis("2022-03-31T12:30:00.000Z");
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long expected = chronology.getMillis("2022-04-30T12:30:00.000Z");
        long result = field.add(testDate, 31L * 29);
        assertEquals(expected, result);

        expected = chronology.getMillis("2022-02-28T12:30:00.000Z");
        result = field.add(testDate, -31L * 2);
        assertEquals(expected, result);
    }

    @Test
    public void testAddReadablePartial() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        DateTime dateTime = new DateTime(chronology.getMillis("2022-03-31T12:30:00.000Z"));
        ReadablePartial readablePartial = new DateTimeFieldElementList();
        readablePartial.add(new MonthOfYearField(chronology, 2, null), 1);
        long expected = chronology.getMillis("2022-04-30T12:30:00.000Z");
        long result = new BasicMonthOfYearDateTimeField(chronology, 2).add(dateTime, readablePartial, 1).getMillis();
        assertEquals(expected, result);

        expected = chronology.getMillis("2022-02-28T12:30:00.000Z");
        readablePartial.set(new MonthOfYearField(chronology, 2, null), -1);
        result = new BasicMonthOfYearDateTimeField(chronology, 2).add(dateTime, readablePartial, -1).getMillis();
        assertEquals(expected, result);
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        long testDate = chronology.getMillis("2022-03-31T12:30:00.000Z");
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long expected = chronology.getMillis("2022-04-30T12:30:00.000Z");
        long result = field.addWrapField(testDate, 1);
        assertEquals(expected, result);

        result = field.addWrapField(testDate, 13);
        assertEquals(expected, result);

        result = field.addWrapField(testDate, -1);
        assertEquals(chronology.getMillis("2022-02-29T12:30:00.000Z"), result);

        result = field.addWrapField(testDate, -13);
        assertEquals(chronology.getMillis("2022-02-28T12:30:00.000Z"), result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        long testDate1 = chronology.getMillis("2022-03-31T12:30:00.000Z");
        long testDate2 = chronology.getMillis("2022-04-30T12:30:00.000Z");
        long testDate3 = chronology.getMillis("2022-01-31T12:30:00.000Z");
        long testDate4 = chronology.getMillis("2022-02-28T12:30:00.000Z");

        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long result1 = field.getDifferenceAsLong(testDate1, testDate2);
        assertEquals(31L, result1);

        result1 = field.getDifferenceAsLong(testDate2, testDate1);
        assertEquals(-31L, result1);

        result1 = field.getDifferenceAsLong(testDate1, testDate3);
        assertEquals(chronology.getMillisOfYear("2022") + 2 * chronology.getMillisPerDay() - 1, result1);

        result1 = field.getDifferenceAsLong(testDate3, testDate4);
        assertEquals(chronology.getMillisOfYear("2022") + chronology.getMillisPerDay(), result1);

        result1 = field.getDifferenceAsLong(testDate4, testDate3);
        assertEquals(-1, result1);
    }

    @Test
    public void testSet() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        long testDate = chronology.getMillis("2022-03-31T12:30:00.000Z");
        int expectedMonth = 4;
        int expectedDayOfMonth = 30;

        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long result = field.set(testDate, expectedMonth);
        assertEquals(expectedMonth, field.get(result));
        assertEquals(expectedDayOfMonth, chronology.getDayOfMonth(result, chronology.getYear(testDate), expectedMonth - 1));

        result = field.set(testDate, 13);
        assertEquals(expectedMonth, field.get(result));
        assertEquals(expectedDayOfMonth, chronology.getDayOfMonth(result, chronology.getYear(testDate), expectedMonth - 1));

        result = field.set(testDate, 0);
        assertEquals(1, field.get(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOutOfRange() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        long testDate = chronology.getMillis("2022-03-31T12:30:00.000Z");
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        int value = 13 + chronology.getMaximumValue(DateTimeFieldType.monthOfYear());
        field.set(testDate, value);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        DurationField result = field.getRangeDurationField();
        assertEquals(DurationFieldType.days(), result.getType());
    }

    @Test
    public void testIsLeap() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        long testDate = chronology.getMillis("2022-02-29T12:30:00.000Z");
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertTrue(field.isLeap(testDate));
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        long testDate = chronology.getMillis("2022-02-29T12:30:00.000Z");
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        int result = field.getLeapAmount(testDate);
        assertEquals(1, result);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        DurationField result = field.getLeapDurationField();
        assertEquals(DurationFieldType.days(), result.getType());
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        int result = field.getMinimumValue();
        assertEquals(1, result);
    }

    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        int result = field.getMaximumValue();
        assertEquals(12, result);
    }

    @Test
    public void testRoundFloor() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        long testDate = chronology.getMillis("2022-03-31T12:30:00.000Z");
        long result = new BasicMonthOfYearDateTimeField(chronology, 2).roundFloor(testDate);
        assertEquals(chronology.getMillis("2022-03-01T00:00:00.000Z"), result);
    }

    @Test
    public void testRemainder() {
        BasicChronology chronology = CalendarUtils.getGregorianInstance();
        long testDate = chronology.getMillis("2022-03-31T12:30:00.000Z");
        long result = new BasicMonthOfYearDateTimeField(chronology, 2).remainder(testDate);
        assertEquals(chronology.getMillisOfDay(testDate) % (chronology.getMillisOfDay(1) / chronology.getMillisPerDay()), result);
    }

    @Test(expected = IllegalStateException.class)
    public void testReadResolve() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        field = (BasicMonthOfYearDateTimeField) field.readResolve();
    }
}