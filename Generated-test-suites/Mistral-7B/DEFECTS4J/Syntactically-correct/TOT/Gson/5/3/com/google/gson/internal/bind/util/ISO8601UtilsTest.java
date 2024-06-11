package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import static org.junit.Assert.*;

/**
 * ISO8601UtilsTest.java - This test file contains various JUnit test cases for the ISO8601Utils class.
 */
public class ISO8601UtilsTest {

    /**
     * Test method for {@link ISO8601Utils#format(Date)} with default format and timezone.
     */
    @Test
    public void testFormatDateDefaultFormat() {
        Date date = new Date(1516041620000L); // Fri Nov 09 01:10:20 CET 2018
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2018-11-09T01:10:20Z", formattedDate);
    }

    /**
     * Test method for {@link ISO8601Utils#format(Date, boolean, TimeZone)} with milliseconds and custom timezone (UTC+1).
     */
    @Test
    public void testFormatDateCustomFormatAndTimezone() {
        Date date = new Date(1516041620000L); // Fri Nov 09 01:10:20 CET 2018
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC+01:00"));
        assertEquals("2018-11-09T01:10:20.000+01:00", formattedDate);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with valid ISO-8601 formatted date string.
     */
    @Test
    public void testParseDateValidFormat() throws ParseException {
        String dateString = "2018-11-09T01:10:20Z";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, parsePosition);
        assertEquals(new Date(1516041620000L), parsedDate);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with invalid ISO-8601 formatted date string - missing time zone.
     */
    @Test(expected = ParseException.class)
    public void testParseDateMissingTimeZone() throws ParseException {
        String dateString = "2018-11-09T01:10:20";
        ParsePosition parsePosition = new ParsePosition(0);
        ISO8601Utils.parse(dateString, parsePosition);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with invalid ISO-8601 formatted date string - invalid time zone indicator.
     */
    @Test(expected = ParseException.class)
    public void testParseDateInvalidTimeZoneIndicator() throws ParseException {
        String dateString = "2018-11-09T01:10:20X";
        ParsePosition parsePosition = new ParsePosition(0);
        ISO8601Utils.parse(dateString, parsePosition);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with invalid ISO-8601 formatted date string - missing year.
     */
    @Test(expected = ParseException.class)
    public void testParseDateMissingYear() throws ParseException {
        String dateString = "11-09T01:10:20Z";
        ParsePosition parsePosition = new ParsePosition(0);
        ISO8601Utils.parse(dateString, parsePosition);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with invalid ISO-8601 formatted date string - missing month.
     */
    @Test(expected = ParseException.class)
    public void testParseDateMissingMonth() throws ParseException {
        String dateString = "2018-09-09T01:10:20Z";
        ParsePosition parsePosition = new ParsePosition(0);
        ISO8601Utils.parse(dateString, parsePosition);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with invalid ISO-8601 formatted date string - missing day.
     */
    @Test(expected = ParseException.class)
    public void testParseDateMissingDay() throws ParseException {
        String dateString = "2018-11-09T01:10:20Z";
        ParsePosition parsePosition = new ParsePosition(0);
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
        calendar.set(Calendar.YEAR, 2018);
        calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
        ISO8601Utils.parse(dateString, parsePosition, calendar.getTime());
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with invalid ISO-8601 formatted date string - missing hours.
     */
    @Test(expected = ParseException.class)
    public void testParseDateMissingHours() throws ParseException {
        String dateString = "2018-11-09T:10:20Z";
        ParsePosition parsePosition = new ParsePosition(0);
        ISO8601Utils.parse(dateString, parsePosition);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with invalid ISO-8601 formatted date string - missing minutes.
     */
    @Test(expected = ParseException.class)
    public void testParseDateMissingMinutes() throws ParseException {
        String dateString = "2018-11-09T01:10Z";
        ParsePosition parsePosition = new ParsePosition(0);
        ISO8601Utils.parse(dateString, parsePosition);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with invalid ISO-8601 formatted date string - missing seconds.
     */
    @Test(expected = ParseException.class)
    public void testParseDateMissingSeconds() throws ParseException {
        String dateString = "2018-11-09T01:10";
        ParsePosition parsePosition = new ParsePosition(0);
        ISO8601Utils.parse(dateString, parsePosition);
    }

    /**
     * Test method for {@link ISO8601Utils#parse(String, ParsePosition)} with invalid ISO-8601 formatted date string - invalid format.
     */
    @Test(expected = ParseException.class)
    public void testParseDateInvalidFormat() throws ParseException {
        String dateString = "2018-11-09T01:10:20A";
        ParsePosition parsePosition = new ParsePosition(0);
        ISO8601Utils.parse(dateString, parsePosition);
    }
}