package org.joda.time.chrono.tests;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.*;
import org.joda.time.test.CalendarTestUtils;

import org.joda.time.format.DateTimeFormatter;

import java.util.GregorianCalendar;
import java.util.TimeZone;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final BasicChronology UTC = CalendarTestUtils.getUTC();

    @org.junit.Test
    public void testBasicGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 0);
        long datetime = DateTime.parse("2012-02-29T12:34:56.789Z", DateTimeFormat.parseDefault().withZone(DateTimeZone.UTC)).getMillis();
        int month = field.get(datetime);
        assertEquals(2, month);
    }

    @org.junit.Test
    public void testBasicAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 0);
        long datetime = DateTime.parse("2012-02-29T12:34:56.789Z", DateTimeFormat.parseDefault().withZone(DateTimeZone.UTC)).getMillis();
        long newDatetime = field.add(datetime, 12);
        DateTime newDateTime = new DateTime(newDatetime);
        assertEquals("2012-04-31T12:34:56.789Z", newDateTime.toString());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddTooLarge() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 0);
        long datetime = DateTime.parse("2012-01-31T12:34:56.789Z", DateTimeFormat.parseDefault().withZone(DateTimeZone.UTC)).getMillis();
        field.add(datetime, 13);
    }

    @org.junit.Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 0);
        long datetime = DateTime.parse("2012-12-31T12:34:56.789Z", DateTimeFormat.parseDefault().withZone(DateTimeZone.UTC)).getMillis();
        long newDatetime = field.addWrapField(datetime, 1);
        DateTime newDateTime = new DateTime(newDatetime);
        assertEquals("2013-01-01T12:34:56.789Z", newDateTime.toString());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddNegative() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 0);
        long datetime = DateTime.parse("2012-01-01T12:34:56.789Z", DateTimeFormat.parseDefault().withZone(DateTimeZone.UTC)).getMillis();
        field.add(datetime, -1);
    }

    @org.junit.Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 0);
        DurationField range = field.getRangeDurationField();
        assertEquals(DurationFieldType.years(), range.getType());
    }

    @org.junit.Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 2);
        long datetime = new GregorianCalendar(2012, 1, 28).getTimeInMillis();
        assertTrue(field.isLeap(datetime));
        datetime += 31 * 24 * 60 * 60 * 1000;
        assertFalse(field.isLeap(datetime));
    }

    @org.junit.Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 2);
        long datetime = new GregorianCalendar(2012, 1, 28).getTimeInMillis();
        assertEquals(1, field.getLeapAmount(datetime));
        datetime += 31 * 24 * 60 * 60 * 1000;
        assertEquals(0, field.getLeapAmount(datetime));
    }

    @org.junit.Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 2);
        DurationField leap = field.getLeapDurationField();
        assertEquals(DurationFieldType.days(), leap.getType());
    }

    @org.junit.Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 0);
        assertEquals(1, field.getMinimumValue());
    }

    @org.junit.Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 0);
        assertEquals(12, field.getMaximumValue());
    }

    @org.junit.Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 0);
        long datetime = DateTime.parse("2012-02-29T12:34:56.789Z", DateTimeFormat.parseDefault().withZone(DateTimeZone.UTC)).getMillis();
        long round = field.roundFloor(datetime);
        long expected = DateTime.parse("2012-02-28T00:00:00.000Z", DateTimeFormat.parseDefault().withZone(DateTimeZone.UTC)).getMillis();
        assertEquals(expected, round);
    }

    @org.junit.Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(UTC, 0);
        long datetime = DateTime.parse("2012-02-29T12:34:56.789Z", DateTimeFormat.parseDefault().withZone(DateTimeZone.UTC)).getMillis();
        long remainder = field.remainder(datetime);
        long expected = 86399000L;
        assertEquals(expected, remainder);
    }
}