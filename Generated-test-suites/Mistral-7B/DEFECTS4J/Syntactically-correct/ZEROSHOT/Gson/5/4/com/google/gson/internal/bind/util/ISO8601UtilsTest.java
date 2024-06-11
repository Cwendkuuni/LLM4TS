package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test class for ISO8601Utils.java
 */
public class ISO8601UtilsTest {

    /**
     * Test method for {@link ISO8601Utils#format(Date)}
     */
    @Test
    public void testFormat() {
        Date date = new GregorianCalendar(2023, 1, 31, 13, 45, 30, 123).getTime();
        String formatted = ISO8601Utils.format(date);
        String expected = "2023-02-01T13:45:30.123Z";
        assertEquals(expected, formatted);

        date = new GregorianCalendar(2023, 1, 31, 13, 45).getTime();
        formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("America/Los_Angeles"));
        expected = "2023-02-01T16:45:00-08:00";
        assertEquals(expected, formatted);

        date = new GregorianCalendar(2023, 1, 31, 13, 45, 30, 123).getTime();
        formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/Los_Angeles"));
        expected = "2023-02-01T16:45:30.123-08:00";
        assertEquals(expected, formatted);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)}
     */
    @Test
    public void testParse() throws Exception {
        String dateString = "2023-02-01T13:45:30.123Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        assertEquals(new GregorianCalendar(2023, 1, 1, 13, 45, 30, 123).getTime(), parsedDate.getTime());
        assertEquals(pos.getIndex(), dateString.length());

        dateString = "2023-02-01T16:45:00-08:00";
        pos = new ParsePosition(0);
        parsedDate = ISO8601Utils.parse(dateString, pos);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/Los_Angeles"), Locale.US);
        calendar.setTime(parsedDate);
        assertEquals(2023, calendar.get(Calendar.YEAR));
        assertEquals(2, calendar.get(Calendar.MONTH) + 1);
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(16, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(45, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(-8, calendar.get(Calendar.EXTRA));
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String)}
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws Exception {
        String dateString = "2023-02-01-13:45:30.123Z"; // missing T character
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Should throw an exception");
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String)}
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidFormat2() throws Exception {
        String dateString = "2023-02-01T13:45:30.123+12:30"; // incorrect timezone offset format
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Should throw an exception");
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String)}
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFormat3() throws Exception {
        String dateString = "2023-02-01"; // missing time component
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Should throw an exception");
    }
}