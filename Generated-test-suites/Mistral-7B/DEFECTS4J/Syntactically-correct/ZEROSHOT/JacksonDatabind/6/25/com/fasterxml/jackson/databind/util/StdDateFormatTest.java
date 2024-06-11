package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 test file for StdDateFormat class.
 */
public class StdDateFormatTest {

    private StdDateFormat dateFormat;

    @Before
    public void setUp() {
        dateFormat = new StdDateFormat();
    }

    @Test
    public void testParseISO8601Format() throws ParseException {
        Date expectedDate = new Date(1516044521000L); // Thu Oct 18 16:35:21 GMT 2017
        String iso8601Date = "2017-10-18T16:35:21.000Z";
        Date parsedDate = dateFormat.parse(iso8601Date);
        assertEquals(expectedDate, parsedDate);
    }

    @Test
    public void testParseISO8601FormatWithTimeZone() throws ParseException {
        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        StdDateFormat dateFormatWithTimeZone = new StdDateFormat(timeZone);
        Date expectedDate = new Date(1516044521000L - 7 * 60 * 60 * 1000); // Thu Oct 18 09:35:21 PDT 2017
        String iso8601Date = "2017-10-18T16:35:21.000Z";
        Date parsedDate = dateFormatWithTimeZone.parse(iso8601Date);
        assertEquals(expectedDate, parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidISO8601Format() throws ParseException {
        String invalidDate = "2017-10-18T16:35:21.000W"; // invalid timezone indicator 'W'
        dateFormat.parse(invalidDate);
    }

    @Test
    public void testParsePlainDateFormat() throws ParseException {
        Date expectedDate = new Date(1516044521000L); // Thu Oct 18 16:35:21 GMT 2017
        String plainDate = "2017-10-18";
        Date parsedDate = dateFormat.parse(plainDate);
        assertEquals(expectedDate, parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidPlainDateFormat() throws ParseException {
        String invalidDate = "2017-10-33"; // invalid day
        dateFormat.parse(invalidDate);
    }

    @Test
    public void testParseRFC1123Format() throws ParseException {
        Date expectedDate = new Date(1516044521000L); // Thu Oct 18 16:35:21 GMT 2017
        String rfc1123Date = "Thu, 18 Oct 2017 16:35:21 GMT";
        Date parsedDate = dateFormat.parse(rfc1123Date);
        assertEquals(expectedDate, parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidRFC1123Format() throws ParseException {
        String invalidDate = "Thu, 18 Oct 2017 16:35:21 WED"; // invalid weekday
        dateFormat.parse(invalidDate);
    }
}