package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.Calends;
import org.joda.time.field.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Calends.JULIAN, 2);;
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Calends.JULIAN, 2);
        DateTime date = new DateTime(2022, 2, 29);
        int monthOfYear = field.get(date.getMillis());
        assertEquals(2, monthOfYear);
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Calends.JULIAN, 2);
        DateTime date = new DateTime(2022, 2, 29);
        long newMillis = field.add(date.getMillis(), 3);
        DateTime newDate = new DateTime(newMillis);
        assertEquals(new DateMeths().days(3), newDate.getDayOfMonth());
        assertEquals(new DateMeths().months(3), newDate.getMonthOfYear());
        assertEquals(2022, newDate.getYear());
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Calends.JULIAN, 2);
        long newMillis = field.addWrapField(Long.MIN_VALUE, 12);
        DateTime newDate = new DateTime(newMillis);
        assertEquals(12, field.get(newDate.getMillis()));
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Calends.JULIAN, 2);
        DateTime date1 = new DateTime(2022, 2, 29);
        DateTime date2 = new DateTime(2023, 3, 30);
        long difference = field.getDifferenceAsLong(date1.getMillis(), date2.getMillis());
        assertEquals(13, difference);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Calends.JULIAN, 2);
        DateTime date = new DateTime(2022, 2, 29);
        long newMillis = field.set(date.getMillis(), 4);
        DateTime newDate = new DateTime(newMillis);
        assertEquals(4, field.get(newDate.getMillis()));
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Calends.JULIAN, 2);
        DurationField rangeDurationField = field.getRangeDurationField();
        assertEquals(DurationFieldType.years(), rangeDurationField.getFieldType());
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Calends.JULIAN, 2);
        DateTime date = new DateTime(2022, 2, 29);
        boolean isLeap = field.isLeap(date.getMillis());
        assertFalse(isLeap);
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Calends.GREGORIAN, 2);
        DateTime date = new DateTime(2022, 2, 29);
        int leapAmount = field.getLeapAmount(date.getMillis());
        assertEquals(0, leapAmount);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Calends.GREGORIAN, 2);
        DurationField leapDurationField = field.getLeapDurationField();
        assertEquals(DurationFieldType.days(), leapDurationField.getFieldType());
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Calends.JULIAN, 2);
        int minimumValue = field.getMinimumValue();
        assertEquals(1, minimumValue);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Calends.JULIAN, 2);
        int maximumValue = field.getMaximumValue();
        assertEquals(12, maximumValue);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Calends.JULIAN, 2);
        DateTime date = new DateTime(2022, 2, 28);
        long roundedFloor = field.roundFloor(date.getMillis());
        DateTime roundedDate = new DateTime(roundedFloor);
        assertEquals(28, roundedDate.getDayOfMonth());
        assertEquals(2, roundedDate.getMonthOfYear());
        assertEquals(2022, roundedDate.getYear());
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Calends.JULIAN, 2);
        DateTime date = new DateTime(2022, 2, 29);
        long remainder = field.remainder(date.getMillis());
        assertEquals(1, remainder);
    }
}