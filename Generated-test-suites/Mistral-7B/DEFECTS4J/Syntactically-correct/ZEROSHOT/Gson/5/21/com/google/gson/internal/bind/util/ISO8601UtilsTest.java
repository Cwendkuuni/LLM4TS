package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * ISO8601UtilsTest.java
 *
 * @author John Doe
 * @since 1.0.0
 */
public class ISO8601UtilsTest
{
    /**
     * Test format method with default timezone and no milliseconds.
     *
     * @throws ParseException
     */
    @Test
    public void testFormatDefaultTimezoneNoMilliseconds() throws ParseException
    {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("Expected: yyyy-MM-ddThh:mm:ssZ, but was: " + formattedDate, "yyyy-MM-ddThh:mm:ssZ", formattedDate);
    }

    /**
     * Test format method with default timezone and milliseconds.
     *
     * @throws ParseException
     */
    @Test
    public void testFormatDefaultTimezoneMillis() throws ParseException
    {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true);
        assertEquals("Expected: yyyy-MM-ddThh:mm:ss[.sss]Z, but was: " + formattedDate, "yyyy-MM-ddThh:mm:ss[.sss]Z", formattedDate);
    }

    /**
     * Test format method with custom timezone and no milliseconds.
     *
     * @throws ParseException
     */
    @Test
    public void testFormatCustomTimezoneNoMilliseconds() throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        Date date = sdf.parse("2023-03-28 16:30:00 PST");
        String formattedDate = ISO8601Utils.format(date, false, timeZone);
        assertEquals("Expected: yyyy-MM-ddThh:mm:ssZ, but was: " + formattedDate, "2023-03-28T22:30:00Z", formattedDate);
    }

    /**
     * Test format method with custom timezone and milliseconds.
     *
     * @throws ParseException
     */
    @Test
    public void testFormatCustomTimezoneMillis() throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        Date date = sdf.parse("2023-03-28 16:30:01.234 PST");
        String formattedDate = ISO8601Utils.format(date, true, timeZone);
        assertEquals("Expected: yyyy-MM-ddThh:mm:ss[.sss]Z, but was: " + formattedDate, "2023-03-28T22:30:01.234Z", formattedDate);
    }

    /**
     * Test parse method with default timezone.
     *
     * @throws ParseException
     */
    @Test
    public void testParseDefaultTimezone() throws ParseException
    {
        String dateString = "2023-03-28T14:30:00Z";
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateString, parsePosition);
        assertEquals("Expected: yyyy-MM-ddThh:mm:ssZ, but was: " + date, "Tue Mar 28 14:30:00 GMT", date.toString());
        assertEquals("Expected: 0, but was: " + parsePosition.getIndex(), dateString.length(), parsePosition.getIndex());
    }

    /**
     * Test parse method with custom timezone.
     *
     * @throws ParseException
     */
    @Test
    public void testParseCustomTimezone() throws ParseException
    {
        String dateString = "2023-03-28T14:30:00-07:00";
        ParsePosition parsePosition = new ParsePosition(0);
        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        Date date = ISO8601Utils.parse(dateString, parsePosition, timeZone);
        assertEquals("Expected: yyyy-MM-ddThh:mm:ss[Z|[+-]hh:mm], but was: " + date, "Tue Mar 28 17:30:00 PDT", date.toString());
        assertEquals("Expected: 23, but was: " + parsePosition.getIndex(), 23, parsePosition.getIndex());
    }
}