package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * ISO8601UtilsTest.java - Test cases for ISO8601Utils class.
 */
public class ISO8601UtilsTest
{
    /**
     * Test for format method when date is given and no milliseconds are provided.
     */
    @Test
    public void testFormatNoMillis()
    {
        Date date = new GregorianCalendar(2022, 11, 30).getTime();

        String formattedDate = ISO8601Utils.format(date);
        String expected = "2022-12-30T00:00:00Z";

        assertEquals(expected, formattedDate);
    }

    /**
     * Test for format method when date, milliseconds, and timezone are provided.
     */
    @Test
    public void testFormatMillisAndTimezone()
    {
        Date date = new GregorianCalendar(2022, 11, 30, 12, 30, 45, 123).getTime();

        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/New_York"));
        String expected = "2022-12-30T17:30:45.123-05:00";

        assertEquals(expected, formattedDate);
    }

    /**
     * Test for parse method when valid date in ISO-8601 format is provided.
     */
    @Test
    public void testParseValidDate() throws ParseException
    {
        ParsePosition pos = new ParsePosition(0);
        String isoDate = "2022-12-30T17:30:45.123-05:00";
        Date parsedDate = ISO8601Utils.parse(isoDate, pos);

        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        calendar.setTime(parsedDate);
        int expectedYear = calendar.get(Calendar.YEAR);
        int expectedMonth = calendar.get(Calendar.MONTH);
        int expectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int expectedHours = calendar.get(Calendar.HOUR_OF_DAY);
        int expectedMinutes = calendar.get(Calendar.MINUTE);
        int expectedSeconds = calendar.get(Calendar.SECOND);
        int expectedMilliseconds = calendar.get(Calendar.MILLISECOND);

        assertEquals(expectedYear, 2022);
        assertEquals(expectedMonth, 11);
        assertEquals(expectedDayOfMonth, 30);
        assertEquals(expectedHours, 17);
        assertEquals(expectedMinutes, 30);
        assertEquals(expectedSeconds, 45);
        assertEquals(expectedMilliseconds, 123);
    }

    /**
     * Test for parse method when invalid date in ISO-8601 format is provided.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException
    {
        ParsePosition pos = new ParsePosition(0);
        String isoDate = "2022-12-31T17:30:45.123-05:00";
        ISO8601Utils.parse(isoDate, pos);
    }
}