package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test class for ISO8601Utils
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Test formatting with default timezone and no milliseconds
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        String expected = "2023-03-23T12:34:56Z";
        assertEquals(expected, formattedDate);

        // Test formatting with specified timezone and milliseconds
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        String formattedDateWithTimeZoneAndMillis = ISO8601Utils.format(date, true, timeZone);
        String expectedFormattedDateWithTimeZoneAndMillis = "2023-03-23T12:34:56.789Z";
        assertEquals(expectedFormattedDateWithTimeZoneAndMillis, formattedDateWithTimeZoneAndMillis);
    }

    @Test
    public void testParse() throws Exception {
        // Test parsing with default timezone and no timezone offset
        String iso8601String = "2023-03-23T12:34:56Z";
        Date parsedDate = ISO8601Utils.parse(iso8601String, new ParsePosition(0));
        assertEquals(iso8601String, ISO8601Utils.format(parsedDate));

        // Test parsing with specified timezone and timezone offset
        String iso8601StringWithTimezoneOffset = "2023-03-23T12:34:56-05:00";
        Date parsedDateWithTimezoneOffset = ISO8601Utils.parse(iso8601StringWithTimezoneOffset, new ParsePosition(0));
        assertEquals(ISO8601Utils.format(parsedDateWithTimezoneOffset, true, TimeZone.getTimeZone("America/New_York")),
                iso8601StringWithTimezoneOffset);

        // Test parsing with milliseconds
        String iso8601StringWithMillis = "2023-03-23T12:34:56.789Z";
        Date parsedDateWithMillis = ISO8601Utils.parse(iso8601StringWithMillis, new ParsePosition(0));
        assertEquals(ISO8601Utils.format(parsedDateWithMillis, true, TimeZone.getTimeZone("UTC")), iso8601StringWithMillis);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidFormat() throws Exception {
        // Test parsing with invalid format
        String iso8601StringWithInvalidFormat = "2023-03-23T12:34:56X";
        ISO8601Utils.parse(iso8601StringWithInvalidFormat, new ParsePosition(0));
    }
}