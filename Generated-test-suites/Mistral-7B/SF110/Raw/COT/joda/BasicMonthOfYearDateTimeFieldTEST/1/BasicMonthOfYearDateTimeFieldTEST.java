package org.joda.time.test.chrono;

import org.joda.time.BaseTestCase;
import org.joda.time.DateTimeConstants;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.field.DateTimeFieldType;
import org.joda.time.local.LocalDate;
import org.junit.Test;

import java.util.Arrays;
import java.util.GregorianCalendar;

public class BasicMonthOfYearDateTimeFieldTest extends BaseTestCase {

    @Test
    public void testConstructor() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        assertFalse(field.isLenient());
        assertEquals(DateTimeFieldType.monthOfYear(), field.getDataType());
        assertEquals(chronology, field.getChronology());
        assertEquals(DateTimeConstants.JANUARY, field.getMaximumValue());
    }

    @Test
    public void testGet() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long date = GregorianCalendar.GREGORIAN.getTimeInMillis();
        int month = field.get(date);

        assertEquals(DateTimeConstants.JANUARY, month);
    }

    @Test
    public void testAddWithinRange() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long date = GregorianCalendar.GREGORIAN.getTimeInMillis();
        long newDate = field.add(date, 1);

        assertEquals(DateTimeFieldType.monthOfYear(), field.getDataType());
        assertEquals(chronology, field.getChronology());
        assertEquals(DateTimeConstants.FEBRUARY, field.get(newDate));

        newDate = field.add(newDate, 1);
        assertEquals(DateTimeConstants.MARCH, field.get(newDate));

        newDate = field.add(newDate, 11);
        assertEquals(DateTimeConstants.APRIL, field.get(newDate));

        newDate = field.add(newDate, -11);
        assertEquals(DateTimeConstants.MARCH, field.get(newDate));

        newDate = field.add(newDate, -1);
        assertEquals(DateTimeConstants.FEBRUARY, field.get(newDate));

        newDate = field.add(newDate, -1);
        assertEquals(DateTimeConstants.JANUARY, field.get(newDate));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOutOfRange() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long date = GregorianCalendar.GREGORIAN.getTimeInMillis();
        long newDate = field.add(date, Integer.MAX_VALUE);

        // This test case should fail with an IllegalArgumentException, since the magnitude of the add amount is too large
    }

    @Test
    public void testAddWithContiguousReadablePartial() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        LocalDate date = new LocalDate(2000, 1, 1);
        long millis = date.toMillis();

        int[] values = {DateTimeConstants.DECEMBER, 2};
        int[] expected = {DateTimeConstants.FEBRUARY, 2};

        int[] result = field.add(new ReadablePartialDateTime(chronology, DateTimeFieldType.monthOfYear(), values), 1, new int[values.length], 0);

        assertEquals(Arrays.asList(expected), Arrays.asList(result));

        long newMillis = chronology.get(new ReadablePartialDateTime(chronology, DateTimeFieldType.monthOfYear(), result), field.get(millis) + 1);
        assertEquals(date.plusYears(1).toDate().getTime(), newMillis);
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long date = GregorianCalendar.GREGORIAN.getTimeInMillis();
        long newDate = field.addWrapField(date, 13);

        assertEquals(DateTimeFieldType.monthOfYear(), field.getDataType());
        assertEquals(chronology, field.getChronology());
        assertEquals(DateTimeConstants.APRIL, field.get(newDate));
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long date1 = GregorianCalendar.GREGORIAN.getTimeInMillis();
        long date2 = date1 + DateTimeConstants.MILLIS_PER_DAY;

        long difference = field.getDifferenceAsLong(date1, date2);

        assertEquals(1, difference);
    }

    @Test
    public void testSet() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long date = GregorianCalendar.GREGORIAN.getTimeInMillis();
        long newDate = field.set(date, 13);

        assertEquals(DateTimeFieldType.monthOfYear(), field.getDataType());
        assertEquals(chronology, field.getChronology());
        assertEquals(DateTimeConstants.APRIL, field.get(newDate));
    }

    @Test
    public void testGetRangeDurationField() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        DurationField rangeDurationField = field.getRangeDurationField();

        assertEquals(DateTimeFieldType.years(), rangeDurationField.getType());
    }

    @Test
    public void testIsLeap() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long date = GregorianCalendar.GREGORIAN.getTimeInMillis();
        boolean isLeap = field.isLeap(date);

        assertFalse(isLeap);
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);

        long date = GregorianCalendar.GREGORIAN.getTimeInMillis();
        int leapAmount = field.getLeapAmount(date);

        assertEquals(0, leapAmount);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);

        DurationField leapDurationField = field.getLeapDurationField();

        assertEquals(DateTimeFieldType.days(), leapDurationField.getType());
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), DateTimeConstants.JANUARY);

        int minimumValue = field.getMinimumValue();

        assertEquals(1, minimumValue);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new GregorianChronology(), DateTimeConstants.JANUARY);

        int maximumValue = field.getMaximumValue();

        assertEquals(12, maximumValue);
    }

    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long date = GregorianCalendar.GREGORIAN.getTimeInMillis();
        long roundedDate = field.roundFloor(date);

        assertEquals(date, roundedDate);
    }

    @Test
    public void testRemainder() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long date = GregorianCalendar.GREGORIAN.getTimeInMillis();
        long remainder = field.remainder(date);

        assertEquals(0, remainder);
    }
}