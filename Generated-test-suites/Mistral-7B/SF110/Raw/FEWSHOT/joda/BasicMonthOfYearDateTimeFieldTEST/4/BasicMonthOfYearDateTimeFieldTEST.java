package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getDataType());
        assertEquals(chronology.getAverageMillisPerMonth(), field.getMillisPerUnit());
        assertEquals(false, field.isLenient());
        assertEquals(1, field.getMinimumValue());
        assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testGet() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        LocalDateTime date = new LocalDateTime(2000, 2, 15, 0, 0);
        long millis = date.getMillis();
        int month = field.get(millis);
        assertEquals(2, month);
    }

    @Test
    public void testAdd() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        LocalDateTime date = new LocalDateTime(2000, 2, 15, 0, 0);
        long millis = date.getMillis();

        long newMillis = field.add(millis, 1);
        LocalDateTime newDate = new LocalDateTime(newMillis);
        assertEquals(new DateMate(2000, 3, 15, 0, 0), newDate);

        long millis2 = field.add(millis, -1);
        LocalDateTime newDate2 = new LocalDateTime(millis2);
        assertEquals(new DateMate(2000, 1, 15, 0, 0), newDate2);

        long millis3 = field.add(millis, 12);
        LocalDateTime newDate3 = new LocalDateTime(millis3);
        assertEquals(new DateMate(2000, 3, 15, 0, 0), newDate3);

        long millis4 = field.add(millis, -12);
        LocalDateTime newDate4 = new LocalDateTime(millis4);
        assertEquals(new DateMate(2000, 1, 15, 0, 0), newDate4);
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        LocalDateTime date = new LocalDateTime(2000, 13, 15, 0, 0); // 13 is an invalid month
        long millis = date.getMillis();

        long newMillis = field.addWrapField(millis, 1);
        LocalDateTime newDate = new LocalDateTime(newMillis);
        assertEquals(new DateMate(2000, 1, 15, 0, 0), newDate); // The month should be wrapped around

        newMillis = field.addWrapField(millis, 12);
        newDate = new LocalDateTime(newMillis);
        assertEquals(new DateMate(2000, 3, 15, 0, 0), newDate); // The month should be wrapped around
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithInvalidArgument() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        LocalDateTime date = new LocalDateTime(2000, 2, 15, 0, 0);
        long millis = date.getMillis();

        long newMillis = field.add(millis, 13); // 13 is an invalid month
    }

    @Test
    public void testAddWithPartial() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        LocalDateTime date = new LocalDateTime(2000, 2, 15, 0, 0);
        long millis = date.getMillis();

        ReadablePartial readablePartial = new GeneralDate(date.toDate(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        int[] result = field.add(readablePartial, 1, new int[3], 0);
        assertEquals(new int[]{3, 0, 0}, result);

        date = new LocalDateTime(2000, 2, 15, 0, 0);
        millis = date.getMillis();

        readablePartial = new GeneralDate(date.toDate(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        result = field.add(readablePartial, 12, new int[3], 0);
        assertEquals(new int[]{3, 0, 0}, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        long millis1 = chronology.getMillis(new LocalDate(2000, 3, 15));
        long millis2 = chronology.getMillis(new LocalDate(2000, 2, 15));

        long difference = field.getDifferenceAsLong(millis1, millis2);
        assertEquals(1, difference);

        difference = field.getDifferenceAsLong(millis2, millis1);
        assertEquals(-1, difference);
    }

    @Test
    public void testSet() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        LocalDateTime date1 = new LocalDateTime(2000, 2, 15, 0, 0);
        long millis1 = date1.getMillis();
        int month1 = field.get(millis1);

        long newMillis = field.set(millis1, 3);
        LocalDateTime newDate = new LocalDateTime(newMillis);
        assertEquals(new DateMate(2000, 3, 15, 0, 0), newDate);

        int month2 = field.get(newMillis);
        assertEquals(3, month2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithInvalidArgument() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        long millis = 0;
        int month = 13; // Invalid month
        long newMillis = field.set(millis, month);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertEquals(chronology.years(), field.getRangeDurationField());
    }

    @Test
    public void testIsLeap() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        LocalDateTime date = new LocalDateTime(2000, 2, 29, 0, 0); // February 29 is a leap day in a leap year
        long millis = date.getMillis();
        assertTrue(field.isLeap(millis));

        date = new LocalDateTime(2000, 2, 28, 0, 0); // February 28 is not a leap day
        millis = date.getMillis();
        assertFalse(field.isLeap(millis));
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        LocalDateTime date = new LocalDateTime(2000, 2, 29, 0, 0); // February 29 is a leap day in a leap year
        int leapAmount = field.getLeapAmount(date.getMillis());
        assertEquals(1, leapAmount);

        date = new LocalDateTime(2000, 2, 28, 0, 0); // February 28 is not a leap day
        leapAmount = field.getLeapAmount(date.getMillis());
        assertEquals(0, leapAmount);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicChronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertEquals(chronology.days(), field.getLeapDurationField());
    }
}