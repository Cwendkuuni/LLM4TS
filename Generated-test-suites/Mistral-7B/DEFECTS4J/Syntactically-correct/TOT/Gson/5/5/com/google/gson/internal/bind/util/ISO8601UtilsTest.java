package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test cases for ISO8601Utils class.
 */
public class ISO8601UtilsTest
{
    @Test
    public void testFormatUTCDate()
    {
        // Test formatting a date in UTC timezone with no milliseconds precision
        Date date = new GregorianCalendar(TimeZone.getTimeZone("UTC"), 2022, 12, 31, 23, 59, 0).getTime();
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2022-12-31T23:59:00Z", formattedDate);

        // Test formatting a date in UTC timezone with milliseconds precision
        Date dateWithMillis = new GregorianCalendar(TimeZone.getTimeZone("UTC"), 2022, 12, 31, 23, 59, 999);
        String formattedDateWithMillis = ISO8601Utils.format(dateWithMillis, true);
        assertEquals("2022-12-31T23:59:09.999Z", formattedDateWithMillis);
    }

    @Test
    public void testFormatDateWithTimeZone()
    {
        // Test formatting a date in a specific timezone with no milliseconds precision
        Date date = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"), 2022, 12, 31, 23, 59, 0).getTime();
        String formattedDate = ISO8601Utils.format(date, TimeZone.getTimeZone("America/New_York"));
        assertEquals("2022-12-31T04:59:00-05:00", formattedDate);

        // Test formatting a date in a specific timezone with milliseconds precision
        Date dateWithMillis = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"), 2022, 12, 31, 23, 59, 999);
        String formattedDateWithMillis = ISO8601Utils.format(dateWithMillis, TimeZone.getTimeZone("America/New_York"), true);
        assertEquals("2022-12-31T04:59:09.999-05:00", formattedDateWithMillis);
    }

    @Test
    public void testParseDateUTC()
    {
        // Test parsing a date in 'yyyy-MM-ddThh:mm:ssZ' format from UTC timezone
        String dateString = "2022-12-31T23:59:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertEquals(new GregorianCalendar(TimeZone.getTimeZone("UTC"), 2022, 12, 31, 23, 59, 0).getTime(), parsedDate.getTime());
        assertEquals(0, pos.getIndex());

        // Test parsing a date with milliseconds in 'yyyy-MM-ddThh:mm:ss.sssZ' format from UTC timezone
        String dateStringWithMillis = "2022-12-31T23:59:09.999Z";
        ParsePosition posWithMillis = new ParsePosition(0);
        Date parsedDateWithMillis = ISO8601Utils.parse(dateStringWithMillis, posWithMillis);
        assertEquals(new GregorianCalendar(TimeZone.getTimeZone("UTC"), 2022, 12, 31, 23, 59, 999).getTime(), parsedDateWithMillis.getTime());
        assertEquals(dateStringWithMillis.length(), posWithMillis.getIndex());
    }

    @Test
    public void testParseDateWithTimeZone()
    {
        // Test parsing a date in 'yyyy-MM-ddThh:mm:ss[Z|[+-]hh:mm]' format with a timezone
        String dateStringWithTimeZone = "2022-12-31T04:59:00-05:00";
        ParsePosition posWithTimeZone = new ParsePosition(0);
        Date parsedDateWithTimeZone = ISO8601Utils.parse(dateStringWithTimeZone, posWithTimeZone);
        assertEquals(new GregorianCalendar(TimeZone.getTimeZone("America/New_York"), 2022, 12, 31, 22, 59, 0).getTime(), parsedDateWithTimeZone.getTime());
        assertEquals(dateStringWithTimeZone.length(), posWithTimeZone.getIndex());

        // Test parsing a date in 'yyyy-MM-ddThh:mm:ss[.sss][Z|[+-]hh:mm]' format with a timezone and milliseconds
        String dateStringWithTimeZoneAndMillis = "2022-12-31T04:59:09.999-05:00";
        ParsePosition posWithTimeZoneAndMillis = new ParsePosition(0);
        Date parsedDateWithTimeZoneAndMillis = ISO8601Utils.parse(dateStringWithTimeZoneAndMillis, posWithTimeZoneAndMillis);
        assertEquals(new GregorianCalendar(TimeZone.getTimeZone("America/New_York"), 2022, 12, 31, 22, 59, 999).getTime(), parsedDateWithTimeZoneAndMillis.getTime());
        assertEquals(dateStringWithTimeZoneAndMillis.length(), posWithTimeZoneAndMillis.getIndex());
    }

    @Test
    public void testParseInvalidFormat()
    {
        // Test parsing an invalid date string
        String invalidDateString = "invalidDate";
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(invalidDateString, pos));
    }
}