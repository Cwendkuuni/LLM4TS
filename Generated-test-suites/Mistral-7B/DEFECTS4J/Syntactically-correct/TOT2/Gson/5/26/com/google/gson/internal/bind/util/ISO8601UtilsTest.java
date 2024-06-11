package com.google.gson.internal.bind.util;

import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * ISO8601UtilsTest.java
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24); // Yesterday

        // Test default format (no milliseconds precision, UTC timezone)
        String formattedDefault = ISO8601Utils.format(date);
        Assert.assertEquals("2022-03-25", formattedDefault.substring(0, 10));
        Assert.assertEquals("Z", formattedDefault.substring(formattedDefault.length() - 1));

        // Test format with milliseconds precision and UTC timezone
        String formattedMillisUTC = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        Assert.assertEquals("2022-03-25T12:34:56.789Z", formattedMillisUTC);

        // Test format with milliseconds precision and a custom timezone
        TimeZone customTimeZone = TimeZone.getTimeZone("Europe/Paris");
        String formattedMillisCustom = ISO8601Utils.format(date, true, customTimeZone);
        Assert.assertEquals("2022-03-25T13:34:56.789+01:00", formattedMillisCustom);
    }

    @Test
    public void testParse() {
        // Test parsing a valid date in the format 'yyyy-MM-ddTHH:mm:ss' (with or without milliseconds)
        String dateString = "2022-03-25T12:34:56";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        Assert.assertNotNull(parsedDate);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(parsedDate);
        Assert.assertEquals(2022, calendar.get(Calendar.YEAR));
        Assert.assertEquals(3, calendar.get(Calendar.MONTH) + 1);
        Assert.assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(34, calendar.get(Calendar.MINUTE));
        Assert.assertEquals(56, calendar.get(Calendar.SECOND));

        // Test parsing a valid date in the format 'yyyy-MM-ddTHH:mm:ss.sss' (with or without milliseconds)
        dateString += ".123";
        parsedDate = ISO8601Utils.parse(dateString, pos);
        Assert.assertNotNull(parsedDate);
        calendar.setTime(parsedDate);
        Assert.assertEquals(2022, calendar.get(Calendar.YEAR));
        Assert.assertEquals(3, calendar.get(Calendar.MONTH) + 1);
        Assert.assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(34, calendar.get(Calendar.MINUTE));
        Assert.assertEquals(56, calendar.get(Calendar.SECOND));
        Assert.assertEquals(123, calendar.get(Calendar.MILLISECOND));

        // Test parsing a date with timezone 'Z' (UTC)
        dateString = "2022-03-25T12:34:56Z";
        parsedDate = ISO8601Utils.parse(dateString, pos);
        Assert.assertNotNull(parsedDate);
        Assert.assertEquals("UTC", parsedDate.toZonesPattern());

        // Test parsing a date with timezone '+hh:mm'
        dateString = "2022-03-25T12:34:56+01:00";
        TimeZone customTimeZone = TimeZone.getTimeZone("Europe/Paris");
        parsedDate = ISO8601Utils.parse(dateString, pos, customTimeZone);
        Assert.assertNotNull(parsedDate);
        calendar.setTime(parsedDate);
        Assert.assertEquals("Europe/Paris", parsedDate.toZonesPattern());
    }

    @Test(expected = ParseException.class)
    public void testParseExceptionInvalidFormat() {
        String dateString = "2022-03-25T12:34:56Invalid";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseExceptionInvalidTimeZone() {
        String dateString = "2022-03-25T12:34:56-99:99";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos, TimeZone.getTimeZone("UTC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseExceptionInvalidInput() {
        String dateString = "2022-03-25T12:34:56";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(null, pos);
    }
}