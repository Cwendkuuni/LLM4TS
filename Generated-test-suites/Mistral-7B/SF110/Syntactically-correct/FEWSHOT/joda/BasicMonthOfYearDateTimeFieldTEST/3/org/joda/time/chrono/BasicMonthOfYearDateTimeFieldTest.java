package org.joda.time.chrono;

import static org.junit.Assert.*;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.DateTimeFieldType;
import org.joda.time.field.FieldUtils;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGet() {
        BasicChronology chrono = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 1);

        long testDate = LocalDateTime.parse("2015-03-15T12:30:00.000Z".toUTF8String()).getMillis();
        int month = field.get(testDate);
        assertEquals(3, month);

        testDate = LocalDateTime.parse("2014-02-28T12:30:00.000Z".toUTF8String()).getMillis();
        month = field.get(testDate);
        assertEquals(2, month);

        testDate = LocalDateTime.parse("2014-02-29T12:30:00.000Z".toUTF8String()).getMillis();
        month = field.get(testDate); // Leap year
        assertEquals(2, month);
    }

    @Test
    public void testAdd() {
        BasicChronology chrono = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 1);

        long testDate = LocalDateTime.parse("2015-03-15T12:30:00.000Z".toUTF8String()).getMillis();

        long newDate = field.add(testDate, 1);
        assertEquals(LocalDateTime.parse("2015-04-15T12:30:00.000Z".toUTF8String()).getMillis(), newDate);

        newDate = field.add(testDate, -1);
        assertEquals(LocalDateTime.parse("2015-02-15T12:30:00.000Z".toUTF8String()).getMillis(), newDate);

        newDate = field.add(testDate, 12);
        assertEquals(LocalDateTime.parse("2015-04-15T12:30:00.000Z".toUTF8String()).getMillis(), newDate);

        newDate = field.add(testDate, 13); // Should wrap around
        assertEquals(LocalDateTime.parse("2015-01-15T12:30:00.000Z".toUTF8String()).getMillis(), newDate);

        newDate = field.add(testDate, -13); // Should wrap around
        assertEquals(LocalDateTime.parse("2015-04-15T12:30:00.000Z".toUTF8String()).getMillis(), newDate);

        newDate = field.add(testDate, Integer.MAX_VALUE); // Should throw an exception
        assertThrows(IllegalArgumentException.class, () -> field.add(testDate, Integer.MAX_VALUE));
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chrono = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 1);

        long testDate = LocalDateTime.parse("2015-03-15T12:30:00.000Z".toUTF8String()).getMillis();

        long wrappedDate = field.addWrapField(testDate, 13);
        assertEquals(LocalDateTime.parse("2015-04-15T12:30:00.000Z".toUTF8String()).getMillis(), wrappedDate);

        wrappedDate = field.addWrapField(testDate, -13);
        assertEquals(LocalDateTime.parse("2015-01-15T12:30:00.000Z".toUTF8String()).getMillis(), wrappedDate);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chrono = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 1);

        long testDate1 = LocalDateTime.parse("2015-03-15T12:30:00.000Z".toUTF8String()).getMillis();
        long testDate2 = LocalDateTime.parse("2015-02-15T12:30:00.000Z".toUTF8String()).getMillis();

        long difference = field.getDifferenceAsLong(testDate1, testDate2);
        assertEquals(1, difference);

        testDate2 = LocalDateTime.parse("2015-04-15T12:30:00.000Z".toUTF8String()).getMillis();
        difference = field.getDifferenceAsLong(testDate1, testDate2);
        assertEquals(-1, difference);
    }

    @Test
    public void testSet() {
        BasicChronology chrono = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 1);

        long testDate = LocalDateTime.parse("2015-03-15T12:30:00.000Z".toUTF8String()).getMillis();

        long newDate = field.set(testDate, 2);
        assertEquals(LocalDateTime.parse("2015-02-15T12:30:00.000Z".toUTF8String()).getMillis(), newDate);

        newDate = field.set(testDate, 13); // Should wrap around
        assertEquals(LocalDateTime.parse("2015-01-15T12:30:00.000Z".toUTF8String()).getMillis(), newDate);

        newDate = field.set(testDate, 0); // Should throw an exception
        assertThrows(IllegalArgumentException.class, () -> field.set(testDate, 0));
    }

    @Test
    public void testIsLeap() {
        BasicChronology chrono = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 2); // February

        long testDate = LocalDateTime.parse("2015-02-29T12:30:00.000Z".toUTF8String()).getMillis();
        assertTrue(field.isLeap(testDate));

        testDate = LocalDateTime.parse("2015-02-28T12:30:00.000Z".toUTF8String()).getMillis();
        assertFalse(field.isLeap(testDate));
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chrono = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 2); // February

        long testDate = LocalDateTime.parse("2015-02-29T12:30:00.000Z".toUTF8String()).getMillis();
        assertEquals(1, field.getLeapAmount(testDate));

        testDate = LocalDateTime.parse("2015-02-28T12:30:00.000Z".toUTF8String()).getMillis();
        assertEquals(0, field.getLeapAmount(testDate));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicChronology chrono = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 2); // February

        DurationField df = field.getLeapDurationField();
        assertEquals(DateTimeFieldType.days(), df.getFieldType());
    }

    @Test
    public void testRoundFloor() {
        BasicChronology chrono = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 1);

        long testDate = LocalDateTime.parse("2015-03-15T12:30:00.000Z".toUTF8String()).getMillis();
        long floor = field.roundFloor(testDate);
        assertEquals(LocalDateTime.parse("2015-03-15T00:00:00.000Z".toUTF8String()).getMillis(), floor);

        testDate = LocalDateTime.parse("2015-03-14T12:30:00.000Z".toUTF8String()).getMillis();
        floor = field.roundFloor(testDate);
        assertEquals(LocalDateTime.parse("2015-03-14T00:00:00.000Z".toUTF8String()).getMillis(), floor);

        testDate = LocalDateTime.parse("2015-03-15T13:30:00.000Z".toUTF8String()).getMillis();
        floor = field.roundFloor(testDate);
        assertEquals(LocalDateTime.parse("2015-03-15T00:00:00.000Z".toUTF8String()).getMillis(), floor);
    }

    @Test
    public void testRemainder() {
        BasicChronology chrono = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 1);

        long testDate = LocalDateTime.parse("2015-03-15T12:30:00.000Z".toUTF8String()).getMillis();
        long remainder = field.remainder(testDate);
        assertEquals(0, remainder);

        testDate = LocalDateTime.parse("2015-03-14T12:30:01.000Z".toUTF8String()).getMillis();
        remainder = field.remainder(testDate);
        assertEquals(1, remainder);

        testDate = LocalDateTime.parse("2015-03-15T13:30:00.500Z".toUTF8String()).getMillis();
        remainder = field.remainder(testDate);
        assertEquals(0.5, remainder);
    }

    @Test
    public void testAddWithContiguousFields() {
        BasicChronology chrono = new ISOChronology();
        BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(chrono, 1);
        LocalDate dateField = new LocalDate();

        long testDate = LocalDateTime.parse("2015-03-15T12:30:00.000Z".toUTF8String()).getMillis();

        ReadablePartial readablePartial = DateTimeUtils.unparse(testDate, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        int[] result = monthField.add(readablePartial, 1, new int[DateTimeField.forName("MM").getSize()], 0);

        LocalDate newDate = new LocalDate();
        newDate.set(DateTimeUtils.getYear(testDate), result[0], DateTimeUtils.getDayOfMonth(testDate));
        assertEquals(newDate.plusDays(1).getMillis(), monthField.add(testDate, 1).longValue());
    }

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 1);
        assertFalse(field.isLenient());
    }
}