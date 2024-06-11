package com.google.gson.internal.bind.util;

import org.junit.BeforeClass;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * ISO8601Utils test class.
 */
public class ISO8601UtilsTest {

    private static TimeZone utcTimeZone;
    private static Calendar calendar;

    @BeforeClass
    public static void setUp() {
        utcTimeZone = TimeZone.getTimeZone("UTC");
        calendar = new GregorianCalendar(utcTimeZone);
    }

    /**
     * Test format method with valid date and default timezone.
     */
    @Test
    public void testFormatWithValidDateAndDefaultTimezone() {
        Date date = new Date(1613381200000L); // 2021-03-15 14:00:00.0 UTC
        String expected = "2021-03-15T14:00:00Z";
        String result = ISO8601Utils.format(date);
        assertEquals(expected, result);
    }

    /**
     * Test format method with valid date, custom timezone, and milliseconds precision.
     */
    @Test
    public void testFormatWithValidDateCustomTimezoneAndMillisPrecision() {
        Date date = new Date(1613381200000L); // 2021-03-15 14:00:00.0 UTC
        String expected = "2021-03-15T14:00:00.0+01:00"; // Testing with a custom timezone of +1 hour (CEST)
        String result = ISO8601Utils.format(date, true, new TimeZone("Europe/Paris"));
        assertEquals(expected, result);
    }

    /**
     * Test format method with null date.
     */
    @Test
    public void testFormatWithNullDate() {
        assertNull(ISO8601Utils.format(null));
    }

    /**
     * Test parse method with a valid ISO-8601 string and default timezone.
     */
    @Test
    public void testParseWithValidIso8601StringAndDefaultTimezone() throws ParseException {
        String iso8601String = "2021-03-15T14:00:00Z";
        Date expectedDate = new Date(1613381200000L); // 2021-03-15 14:00:00.0 UTC
        Date parsedDate = ISO8601Utils.parse(iso8601String, new ParsePosition(0));
        assertEquals(expectedDate, parsedDate);
    }

    /**
     * Test parse method with a valid ISO-8601 string, custom timezone, and milliseconds precision.
     */
    @Test
    public void testParseWithValidIso8601StringCustomTimezoneAndMillisPrecision() throws ParseException {
        String iso8601String = "2021-03-15T14:00:00.123+01:00"; // Testing with a custom timezone of +1 hour (CEST)
        Date expectedDate = new Date(161338120123000L); // 2021-03-15 14:00:00.123+01:00
        Date parsedDate = ISO8601Utils.parse(iso8601String, new ParsePosition(0));
        assertEquals(expectedDate, parsedDate);
    }

    /**
     * Test parse method with null string.
     */
    @Test
    public void testParseWithNullString() {
        assertThrows(NullPointerException.class, () -> ISO8601Utils.parse(null, new ParsePosition(0)));
    }

    /**
     * Test parse method with an invalid ISO-8601 string.
     */
    @Test
    public void testParseWithInvalidIso8601String() {
        String invalidIso8601String = "2021-03-15T14:00:00:Z"; // Extra colon
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(invalidIso8601String, new ParsePosition(0)));
    }
}