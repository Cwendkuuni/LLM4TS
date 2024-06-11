package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Tests for ISO8601Utils class.
 */
public class ISO8601UtilsTest {

    /**
     * Test format method with valid date and default timezone.
     */
    @Test
    public void testFormatValidDateDefaultTimezone() {
        Date date = new Date(1516044423000L); // Mon Aug 20 14:30:23 CEST 2018
        String expectedOutput = "2018-08-20T13:30:23+02:00";
        String actualOutput = ISO8601Utils.format(date);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test format method with valid date, custom timezone, and milliseconds precision.
     */
    @Test
    public void testFormatValidDateCustomTimezoneMillisPrecision() {
        Date date = new Date(1516044423000L); // Mon Aug 20 14:30:23 CEST 2018
        String expectedOutput = "2018-08-20T13:30:23.000+02:00";
        String actualOutput = ISO8601Utils.format(date, true, TimeZone.getTimeZone("Europe/Paris"));
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test format method with null date.
     */
    @Test
    public void testFormatNullDate() {
        String expectedOutput = null;
        String actualOutput = ISO8601Utils.format(null);
        assertNull(actualOutput);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test parse method with valid ISO8601 string and default timezone.
     */
    @Test
    public void testParseValidISOStringDefaultTimezone() throws ParseException {
        String dateString = "2018-08-20T13:30:23Z";
        Date expectedDate = new Date(1535196823000L); // Mon Aug 20 12:30:23 UTC 2018
        Date actualDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        assertEquals(expectedDate.getTime(), actualDate.getTime());
    }

    /**
     * Test parse method with valid ISO8601 string and custom timezone.
     */
    @Test
    public void testParseValidISOSringCustomTimezone() throws ParseException {
        String dateString = "2018-08-20T13:30:23+02:00";
        Date expectedDate = new Date(1535196823000L + 2 * 60 * 60 * 1000); // Mon Aug 20 15:30:23 CEST 2018
        Date actualDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        assertEquals(expectedDate.getTime(), actualDate.getTime());
    }

    /**
     * Test parse method with invalid ISO8601 string.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidISOSring() throws ParseException {
        String dateString = "2018-08-20T13:30:23+02:00 invalid format";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    /**
     * Test parse method with null ISO8601 string.
     */
    @Test
    public void testParseNullISOSring() {
        ParsePosition parsePosition = new ParsePosition(0);
        Date expectedDate = null;
        Date actualDate = ISO8601Utils.parse(null, parsePosition);
        assertEquals(expectedDate, actualDate);
        assertEquals(parsePosition.getIndex(), 0);
    }
}