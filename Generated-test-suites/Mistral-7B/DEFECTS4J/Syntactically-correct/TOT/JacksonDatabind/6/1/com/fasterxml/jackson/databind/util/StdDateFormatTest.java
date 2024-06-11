package com.fasterxml.jackson.databind.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for StdDateFormat class.
 */
public class StdDateFormatTest {

    /**
     * Test parsing of ISO-8601 compliant date strings using StdDateFormat.
     */
    @Test
    public void testParseISO8601() throws Exception {
        StdDateFormat df = StdDateFormat.instance;
        // Test basic (no timezone) parsing
        Date date = df.parse("2022-12-31T12:34:56.123Z");
        Assert.assertNotNull(date);
        Assert.assertEquals(2022, date.getYear());
        Assert.assertEquals(12, date.getMonth());
        Assert.assertEquals(31, date.getDate());
        Assert.assertEquals(12, date.getHours());
        Assert.assertEquals(34, date.getMinutes());
        Assert.assertEquals(56, date.getSeconds());
        Assert.assertEquals(123, date.getMilliseconds());
        // Test with UTC timezone
        df = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        date = df.parse("2022-12-31T12:34:56.123Z");
        Assert.assertEquals(date, df.parse("2022-12-31T12:34:56.123Z"));
        Assert.assertEquals("Sat Dec 31 12:34:56 UTC 2022", df.format(date));
        // Test with custom timezone
        df = new StdDateFormat(TimeZone.getTimeZone("Europe/Paris"), Locale.US);
        date = df.parse("2022-12-31T12:34:56.123Z");
        Assert.assertEquals("Sun Dec 31 13:34:56 CET 2022", df.format(date));
        // Test parsing with milliseconds missing
        date = df.parse("2022-12-31T12:34:56Z");
        Assert.assertEquals(date, df.parse("2022-12-31T12:34:56.000Z"));
        Assert.assertEquals("Sat Dec 31 12:34:56 UTC 2022", df.format(date));
        // Test parsing with seconds missing
        date = df.parse("2022-12-31T12:34Z");
        Assert.assertEquals(date, df.parse("2022-12-31T12:34.000Z"));
        Assert.assertEquals("Sat Dec 31 12:34 UTC 2022", df.format(date));
        // Test parsing with minutes missing
        date = df.parse("2022-12-31T12Z");
        Assert.assertEquals(date, df.parse("2022-12-31T12.000Z"));
        Assert.assertEquals("Sat Dec 31 12:00:00 UTC 2022", df.format(date));
        // Test parsing with hours missing
        date = df.parse("2022-12-31Z");
        Assert.assertEquals(date, df.parse("2022-12-31.000Z"));
        Assert.assertEquals("Sat Dec 31 00:00:00 UTC 2022", df.format(date));
        // Test parsing with date only
        date = df.parse("2022-12-31");
        Assert.assertEquals("Sat Dec 31 00:00:00 UTC 2022", df.format(date));
    }

    /**
     * Test parsing of RFC 1123 compliant date strings using StdDateFormat.
     */
    @Test
    public void testParseRFC1123() throws Exception {
        StdDateFormat df = StdDateFormat.instance;
        // Test basic parsing
        Date date = df.parse("Thu, 31 Dec 2022 12:34:56 GMT");
        Assert.assertNotNull(date);
        Assert.assertEquals(2022, date.getYear());
        Assert.assertEquals(12, date.getMonth());
        Assert.assertEquals(31, date.getDate());
        Assert.assertEquals(12, date.getHours());
        Assert.assertEquals(34, date.getMinutes());
        Assert.assertEquals(56, date.getSeconds());
        Assert.assertEquals(0, date.getMilliseconds());
        // Test with custom timezone
        df = new StdDateFormat(TimeZone.getTimeZone("Europe/Paris"), Locale.US);
        date = df.parse("Thu, 31 Dec 2022 12:34:56 GMT");
        Assert.assertEquals("Fri, 01 Jan 2023 13:34:56 CET", df.format(date));
        // Test parsing with timezone missing
        date = df.parse("Thu, 31 Dec 2022 12:34:56");
        Assert.assertEquals("Thu Dec 31 12:34:56 UTC 2022", df.format(date));
    }

    /**
     * Test formatting of dates using StdDateFormat.
     */
    @Test
    public void testFormat() throws Exception {
        StdDateFormat df = StdDateFormat.instance;
        // Test formatting of a given date with default timezone
        Date date = new GregorianCalendar(2022, 11, 31, 12, 34, 56, 123).getTime();
        Assert.assertEquals("2022-12-31T12:34:56.123Z", df.format(date));
        // Test formatting of a given date with custom timezone
        df = new StdDateFormat(TimeZone.getTimeZone("Europe/Paris"), Locale.US);
        date = new GregorianCalendar(2022, 11, 31, 12, 34, 56, 123).getTime();
        Assert.assertEquals("2022-12-31T13:34:56.123+01:00", df.format(date));
    }
}