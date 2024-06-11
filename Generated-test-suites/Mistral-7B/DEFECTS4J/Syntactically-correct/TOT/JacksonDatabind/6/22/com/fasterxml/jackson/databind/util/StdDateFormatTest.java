package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws ParseException {
        StdDateFormat df = new StdDateFormat();
        Date date = df.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Incorrect date parsed from ISO-8601 format", new Date(1672501199123L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601InvalidFormat() throws ParseException {
        StdDateFormat df = new StdDateFormat();
        df.parse("2022-12-31T23:59:59.123X"); // X is invalid timezone indicator
    }

    @Test
    public void testParseISO8601WithTimezone() throws ParseException {
        StdDateFormat df = new StdDateFormat(TimeZone.getTimeZone("UTC+1"), Locale.US);
        Date date = df.parse("2022-12-31T23:59:59.123+01:00");
        assertEquals("Incorrect date parsed from ISO-8601 format with timezone", new Date(1672504799123L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601WithInvalidTimezone() throws ParseException {
        StdDateFormat df = new StdDateFormat(TimeZone.getTimeZone("UTC+1"), Locale.US);
        df.parse("2022-12-31T23:59:59.123+03:30"); // Invalid timezone format
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        StdDateFormat df = new StdDateFormat();
        Date date = df.parse("Thu, 31 Dec 2022 23:59:59 GMT");
        assertEquals("Incorrect date parsed from RFC-1123 format", new Date(1672501199000L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123InvalidFormat() throws ParseException {
        StdDateFormat df = new StdDateFormat();
        df.parse("Thu, 31 Dec 2022 23:59:59 ABC"); // Invalid format: ABC is not a valid timezone
    }

    @Test
    public void testFormat() {
        StdDateFormat df = new StdDateFormat();
        String format = df.format(new Date(1672501199123L), new StringBuffer(), null);
        assertEquals("Incorrect format string generated from ISO-8601 format", "2022-12-31T23:59:59.123Z", format);
    }
}