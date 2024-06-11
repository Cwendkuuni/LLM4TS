package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * ISO8601UtilsTest.java - Test cases for ISO8601Utils class
 *
 * @author John Doe
 */
public class ISO8601UtilsTest {

    /**
     * Test method for {@link ISO8601Utils#format(Date)}
     */
    @Test
    public void testFormatDate() {
        Date date = new Date(System.currentTimeMillis());

        String formattedDate = ISO8601Utils.format(date);
        String expectedFormat = "YYYY-MM-ddTHH:mm:ss.SSSZ";
        String formattedExpected = String.format(expectedFormat, date.getYear() + 1900, date.getMonth() + 1, date.getDate(),
                date.getHours(), date.getMinutes(), date.getSeconds(), date.getMilliseconds(),
                TimeZone.getDefault().getDisplayName(false));

        assertEquals(formattedExpected, formattedDate);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with valid input
     */
    @Test
    public void testParseValidInput() throws ParseException {
        String dateString = "2018-01-31T12:34:56.789Z";
        ParsePosition pos = new ParsePosition(0);

        Date parsedDate = ISO8601Utils.parse(dateString, pos);

        Calendar expectedCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
        expectedCalendar.setTime(parsedDate);
        int expectedYear = expectedCalendar.get(Calendar.YEAR);
        int expectedMonth = expectedCalendar.get(Calendar.MONTH) + 1;
        int expectedDay = expectedCalendar.get(Calendar.DAY_OF_MONTH);
        int expectedHour = expectedCalendar.get(Calendar.HOUR_OF_DAY);
        int expectedMinute = expectedCalendar.get(Calendar.MINUTE);
        int expectedSecond = expectedCalendar.get(Calendar.SECOND);
        int expectedMillisecond = expectedCalendar.get(Calendar.MILLISECOND);

        assertEquals(expectedYear, parsedDate.getYear() + 1900);
        assertEquals(expectedMonth, parsedDate.getMonth() + 1);
        assertEquals(expectedDay, parsedDate.getDate());
        assertEquals(expectedHour, parsedDate.getHours());
        assertEquals(expectedMinute, parsedDate.getMinutes());
        assertEquals(expectedSecond, parsedDate.getSeconds());
        assertEquals(expectedMillisecond, parsedDate.getMilliseconds());
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with invalid input - no year
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidInputNoYear() throws ParseException {
        String dateString = "MM-ddTHH:mm:ss.SSSZ";
        ParsePosition pos = new ParsePosition(0);

        ISO8601Utils.parse(dateString, pos);
        fail("Expected ParseException but got none.");
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with invalid input - no time
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidInputNoTime() throws ParseException {
        String dateString = "YYYY-MM-ddZ";
        ParsePosition pos = new ParsePosition(0);

        ISO8601Utils.parse(dateString, pos);
        fail("Expected ParseException but got none.");
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with invalid input - invalid format
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidInputInvalidFormat() throws ParseException {
        String dateString = "YYYY-MM-ddTHH:mm:ss.SSSAA";
        ParsePosition pos = new ParsePosition(0);

        ISO8601Utils.parse(dateString, pos);
        fail("Expected ParseException but got none.");
    }
}