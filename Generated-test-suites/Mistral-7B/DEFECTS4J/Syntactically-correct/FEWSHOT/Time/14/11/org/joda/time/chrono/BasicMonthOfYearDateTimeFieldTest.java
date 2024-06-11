package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;
    private BasicChronology chronology;

    @org.junit.Before
    public void setUp() {
        chronology = new BasicGregorianChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
    }

    @org.junit.Test
    public void testGet() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        assertEquals(DateTimeConstants.JANUARY, field.get(instant));

        instant = LocalDateTime.of(1997, 11, 23, 14, 30).toInstanceInUTC().getMillis();
        assertEquals(11, field.get(instant));
    }

    @org.junit.Test
    public void testAdd() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        long result = field.add(instant, 1);
        assertEquals(DateTimeConstants.HOUR_OF_DAY, result);

        result = field.add(instant, -3);
        assertEquals(DateTimeConstants.MINUTE_OF_HOUR, result);

        result = field.add(instant, 13);
        assertEquals(DateTimeConstants.MONTH_OF_YEAR, result);

        result = field.add(instant, -8);
        assertEquals(DateTimeConstants.DECEMBER, field.get(result));
        assertEquals(31, field.getDayOfMonth(result));

        result = field.add(instant, 400);
        assertEquals(DateTimeConstants.DECEMBER, field.get(result));
        assertEquals(31, field.getDayOfMonth(result));

        result = field.add(instant, -400);
        assertEquals(DateTimeConstants.JANUARY, field.get(result));
        assertEquals(31, field.getDayOfMonth(result));

        result = field.add(instant, Long.MIN_VALUE);
        assertEquals(DateTimeConstants.JANUARY, field.get(result));
        assertEquals(31, field.getDayOfMonth(result));

        result = field.add(instant, Long.MAX_VALUE);
        assertEquals(DateTimeConstants.DECEMBER, field.get(result));
        assertEquals(31, field.getDayOfMonth(result));
    }

    @org.junit.Test
    public void testGetDifferenceAsLong() {
        long instant1 = DateTimeConstants.MINUTE_OF_DAY;
        long instant2 = DateTimeConstants.HOUR_OF_DAY;
        long difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(60, difference);

        instant1 = LocalDateTime.of(1997, 11, 23, 14, 30).toInstanceInUTC().getMillis();
        instant2 = LocalDateTime.of(1997, 11, 23, 15, 0).toInstanceInUTC().getMillis();
        difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(1, difference);

        instant1 = LocalDateTime.of(1997, 11, 23, 14, 30).toInstanceInUTC().getMillis();
        instant2 = LocalDateTime.of(1998, 12, 25, 18, 45).toInstanceInUTC().getMillis();
        difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(2, difference);

        instant1 = LocalDateTime.of(1997, 11, 23, 14, 30).toInstanceInUTC().getMillis();
        instant2 = LocalDateTime.of(1997, 10, 31, 23, 59).toInstanceInUTC().getMillis();
        difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(-2, difference);

        instant1 = LocalDateTime.of(1997, 11, 23, 14, 30).toInstanceInUTC().getMillis();
        instant2 = LocalDateTime.of(2000, 2, 29, 14, 30).toInstanceInUTC().getMillis();
        difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(365 + 1, difference);

        instant1 = LocalDateTime.of(2000, 2, 29, 14, 30).toInstanceInUTC().getMillis();
        instant2 = LocalDateTime.of(1999, 12, 31, 14, 30).toInstanceInUTC().getMillis();
        difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(366, difference);
    }

    @org.junit.Test
    public void testSet() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        long result = field.set(instant, DateTimeConstants.FEBRUARY);
        assertEquals(DateTimeConstants.FEBRUARY, field.get(result));
        assertEquals(28, field.getDayOfMonth(result));

        instant = DateTimeConstants.MINUTE_OF_DAY;
        result = field.set(instant, DateTimeConstants.DECEMBER + 1);
        assertEquals(DateTimeConstants.JANUARY, field.get(result));
        assertEquals(31, field.getDayOfMonth(result));

        instant = DateTimeConstants.MINUTE_OF_DAY;
        result = field.set(instant, DateTimeConstants.DECEMBER - 12);
        assertEquals(DateTimeConstants.NOVEMBER, field.get(result));
        assertEquals(30, field.getDayOfMonth(result));

        instant = LocalDateTime.of(1997, 11, 23, 14, 30).toInstanceInUTC().getMillis();
        result = field.set(instant, 12);
        assertEquals(12, field.get(result));
        assertEquals(31, field.getDayOfMonth(result));
    }

    @org.junit.Test
    public void testAddWrapField() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        long result = field.addWrapField(instant, 13);
        assertEquals(DateTimeConstants.JANUARY, field.get(result));
        assertEquals(31, field.getDayOfMonth(result));

        instant = LocalDateTime.of(1997, 11, 23, 14, 30).toInstanceInUTC().getMillis();
        result = field.addWrapField(instant, 12);
        assertEquals(DateTimeConstants.DECEMBER, field.get(result));
        assertEquals(31, field.getDayOfMonth(result));
    }

    @org.junit.Test
    public void testGetRangeDurationField() {
        DurationField durationField = field.getRangeDurationField();
        assertEquals(DateTimeFieldType.years(), durationField.getFieldType());
        assertEquals(0, durationField.getValues());
    }

    @org.junit.Test
    public void testIsLeap() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        assertFalse(field.isLeap(instant));

        instant = LocalDateTime.of(1998, 2, 29, 14, 30).toInstanceInUTC().getMillis();
        assertTrue(field.isLeap(instant));

        instant = LocalDateTime.of(2000, 2, 29, 14, 30).toInstanceInUTC().getMillis();
        assertTrue(field.isLeap(instant));

        instant = LocalDateTime.of(2100, 2, 29, 14, 30).toInstanceInUTC().getMillis();
        assertFalse(field.isLeap(instant));
    }

    @org.junit.Test
    public void testGetLeapAmount() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        assertEquals(0, field.getLeapAmount(instant));

        instant = LocalDateTime.of(1998, 2, 29, 14, 30).toInstanceInUTC().getMillis();
        assertEquals(1, field.getLeapAmount(instant));

        instant = LocalDateTime.of(2000, 2, 29, 14, 30).toInstanceInUTC().getMillis();
        assertEquals(1, field.getLeapAmount(instant));

        instant = LocalDateTime.of(2100, 2, 29, 14, 30).toInstanceInUTC().getMillis();
        assertEquals(0, field.getLeapAmount(instant));
    }

    @org.junit.Test
    public void testGetLeapDurationField() {
        DurationField durationField = field.getLeapDurationField();
        assertEquals(DateTimeFieldType.days(), durationField.getFieldType());
        assertEquals(0, durationField.getValues());
    }

    @org.junit.Test
    public void testGetMinimumValue() {
        assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
    }

    @org.junit.Test
    public void testGetMaximumValue() {
        assertEquals(DateTimeConstants.DECEMBER, field.getMaximumValue());
    }

    @org.junit.Test
    public void testRoundFloor() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        assertEquals(instant, field.roundFloor(instant));

        instant = LocalDateTime.of(1997, 11, 23, 14, 30).toInstanceInUTC().getMillis();
        assertEquals(instant, field.roundFloor(instant));

        instant = LocalDateTime.of(1997, 11, 23, 14, 29).toInstanceInUTC().getMillis();
        assertEquals(instant, field.roundFloor(instant));

        instant = LocalDateTime.of(1997, 11, 23, 14, 31).toInstanceInUTC().getMillis();
        assertEquals(instant + DateTimeConstants.MILLIS_PER_DAY,
                     field.roundFloor(instant));
    }

    @org.junit.Test
    public void testRemainder() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        assertEquals(0, field.remainder(instant));

        instant = LocalDateTime.of(1997, 11, 30, 14, 30).toInstanceInUTC().getMillis();
        assertEquals(-1, field.remainder(instant));

        instant = LocalDateTime.of(1998, 2, 29, 14, 30).toInstanceInUTC().getMillis();
        assertEquals(0, field.remainder(instant));

        instant = LocalDateTime.of(2000, 2, 29, 14, 30).toInstanceInUTC().getMillis();
        assertEquals(0, field.remainder(instant));

        instant = LocalDateTime.of(2100, 2, 28, 14, 30).toInstanceInUTC().getMillis();
        assertEquals(-1, field.remainder(instant));
    }

    @org.junit.Test
    public void testGetField() {
        assertEquals(field, field.getField(0));
    }

    @org.junit.Test
    public void testGetValues() {
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        ReadablePartial readablePartial = FieldUtils.localDate();
        int[] values = field.add(readablePartial, 0, new int[3], 1);
        assertEquals(DateTimeConstants.JANUARY + 1, values[0]);
        assertEquals(1, values[1]);
        assertEquals(DateTimeConstants.JANUARY, values[2]);

        long instant2 = LocalDateTime.of(1997, 11, 23, 14, 30).toInstanceInUTC().getMillis();
        values = field.add(readablePartial, 0, new int[3], 12);
        assertEquals(DateTimeConstants.DECEMBER + 1, values[0]);
        assertEquals(31, values[1]);
        assertEquals(DateTimeConstants.DECEMBER, values[2]);

        instant = LocalDateTime.of(1997, 11, 23, 14, 30).toInstanceInUTC().getMillis();
        values = field.add(readablePartial, 1, new int[3], -12);
        assertEquals(DateTimeConstants.NOVEMBER + 1, values[0]);
        assertEquals(30, values[1]);
        assertEquals(DateTimeConstants.NOVEMBER, values[2]);
    }
}