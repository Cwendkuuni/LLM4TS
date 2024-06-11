package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test for ISO8601Utils class
 */
public class ISO8601UtilsTest
{
    @Test
    public void testFormatDate()
    {
        Date date = new Date(1609459200000L); // Wed Jun 17 00:00:00 CEST 2020

        // Test format with default timezone and no milliseconds
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2020-06-17T00:00:00Z", formattedDate);

        // Test format with custom timezone and milliseconds
        TimeZone customTimeZone = TimeZone.getTimeZone("Europe/Paris");
        String formattedDateCustomTimeZoneWithMillis = ISO8601Utils.format(date, true, customTimeZone);
        assertEquals("2020-06-17T01:00:00.000+02:00", formattedDateCustomTimeZoneWithMillis);

        // Test format with custom timezone and no milliseconds
        String formattedDateCustomTimeZone = ISO8601Utils.format(date, false, customTimeZone);
        assertEquals("2020-06-17T01:00:00+02:00", formattedDateCustomTimeZone);
    }

    @Test
    public void testFormatDateInvalidInput()
    {
        assertThrows(IllegalArgumentException.class, () -> ISO8601Utils.format(null));
    }

    @Test
    public void testParseDate() throws ParseException
    {
        // Test parsing date with default timezone and no milliseconds
        String dateString = "2020-06-17";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(parsedDate);
        assertEquals(Calendar.JUNE, calendar.get(Calendar.MONTH));
        assertEquals(17, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));

        // Test parsing date with custom timezone and milliseconds
        String dateStringWithTimeZoneAndMillis = "2020-06-17T13:45:01.234+02:00";
        Date parsedDateWithTimeZoneAndMillis = ISO8601Utils.parse(dateStringWithTimeZoneAndMillis, new ParsePosition(0));
        calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        calendar.setTime(parsedDateWithTimeZoneAndMillis);
        assertEquals(Calendar.JUNE, calendar.get(Calendar.MONTH));
        assertEquals(17, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(13, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(45, calendar.get(Calendar.MINUTE));
        assertEquals(1, calendar.get(Calendar.SECOND));
        assertEquals(234, calendar.get(Calendar.MILLISECOND));

        // Test parsing date with custom timezone and no milliseconds
        String dateStringWithTimeZone = "2020-06-17T13:45+02:00";
        Date parsedDateWithTimeZone = ISO8601Utils.parse(dateStringWithTimeZone, new ParsePosition(0));
        calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        calendar.setTime(parsedDateWithTimeZone);
        assertEquals(Calendar.JUNE, calendar.get(Calendar.MONTH));
        assertEquals(17, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(13, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(45, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));

        // Test parsing invalid date strings
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("invalidDate", new ParsePosition(0)));
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2020-06-17T13:45:61", new ParsePosition(0))); // invalid milliseconds
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2020-06-17T13:45:01ZXX", new ParsePosition(0))); // unknown suffix
    }

    @Test
    public void testParseDateInvalidInput()
    {
        assertThrows(IllegalArgumentException.class, () -> ISO8601Utils.parse(null, new ParsePosition(0)));
        assertThrows(IllegalArgumentException.class, () -> ISO8601Utils.parse("", new ParsePosition(0)));
    }

    @Test
    public void testFormatAndParseZonedDateTime() throws ParseException
    {
        // Test parsing zoned datetime with default timezone and no milliseconds
        String zonedDateTimeString = "2020-06-17T13:45:00Z";
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(zonedDateTimeString);
        Date date = Date.from(zonedDateTime.toInstant());
        String formattedDate = ISO8601Utils.format(date);
        assertEquals(zonedDateTimeString, formattedDate);

        // Test parsing zoned datetime with custom timezone and milliseconds
        ZoneId customTimeZone = ZoneId.of("Europe/Paris");
        ZonedDateTime zonedDateTimeCustomTimeZoneWithMillis = ZonedDateTime.parse("2020-06-17T14:45:01.234Z").withZoneSameInstant(customTimeZone);
        Date dateCustomTimeZoneWithMillis = new Date(zonedDateTimeCustomTimeZoneWithMillis.toInstant().toEpochMilli());
        String formattedDateCustomTimeZoneWithMillis = ISO8601Utils.format(dateCustomTimeZoneWithMillis, true, customTimeZone);
        assertEquals("2020-06-17T15:45:01.234+02:00", formattedDateCustomTimeZoneWithMillis);

        // Test parsing zoned datetime with custom timezone and no milliseconds
        ZonedDateTime zonedDateTimeCustomTimeZone = ZonedDateTime.parse("2020-06-17T14:45:00Z").withZoneSameInstant(customTimeZone);
        Date dateCustomTimeZone = new Date(zonedDateTimeCustomTimeZone.toInstant().toEpochMilli());
        String formattedDateCustomTimeZone = ISO8601Utils.format(dateCustomTimeZone, false, customTimeZone);
        assertEquals("2020-06-17T15:45:00+02:00", formattedDateCustomTimeZone);
    }

    @Test
    public void testParseDateWithInvalidTimeZoneIndicator() throws ParseException
    {
        // Test parsing with invalid timezone indicator
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2020-06-17T13:45:00E", new ParsePosition(0)));
    }
}