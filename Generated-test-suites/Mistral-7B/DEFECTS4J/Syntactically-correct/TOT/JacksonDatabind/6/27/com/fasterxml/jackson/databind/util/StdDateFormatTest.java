package com.fasterxml.jackson.databind.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdDateFormatTest {

    @Test
    public void testParseValidISO8601Date() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-03-01T14:30:45.123Z");
        assertEquals("Expected valid date but got null", new Date(1646352245000L), date);
    }

    @Test
    public void testParseValidRFC1123Date() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("Wed, 01 Mar 2022 14:30:45 GMT");
        assertEquals("Expected valid date but got null", new Date(1646352245000L), date);
    }

    @Test
    public void testParseValidDateWithTimeZoneOffset() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-03-01T14:30:45-05:00");
        assertEquals("Expected valid date but got null", new Date(1646308445000L), date);
    }

    @Test
    public void testParseValidDateWithTimeZoneOffsetAndMilliseconds() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = dateFormat.parse("2022-03-01T14:30:45.123-05:00");
        assertEquals("Expected valid date but got null", new Date(1646308445123L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateFormat() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("Invalid Date");
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateFormatWithMissingMilliseconds() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("2022-03-01T14:30:45-05:00"); // missing .mmm
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateFormatWithMissingTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("2022-03-01T14:30:45.123"); // missing Z or +hh:mm
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateFormatWithMissingYear() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("03-01-2022T14:30:45.123Z"); // missing yyyy
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateFormatWithMissingMonth() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("2022-01-01T14:30:45.123Z"); // missing MM
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateFormatWithMissingDay() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("2022-03-32T14:30:45.123Z"); // missing dd
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateFormatWithMissingColon() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("2022-03-01T1430:45.123Z"); // missing ':'
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateFormatWithMissingColon2() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("2022-03-01T14:30:45.123Z"); // missing ':'
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateFormatWithInvalidTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        dateFormat.parse("2022-03-01T14:30:45.123InvalidTimeZone");
    }
}