package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws ParseException {
        DateFormat df = new StdDateFormat();
        Date date = df.parse("2022-12-31T23:59:59.123Z");
        assertEquals("2022-12-31 23:59:59.123", df.format(date));
    }

    @Test
    public void testParseISO8601WithTimeZone() throws ParseException {
        DateFormat df = new StdDateFormat(TimeZone.getTimeZone("UTC+1"), Locale.US);
        Date date = df.parse("2022-12-31T23:59:59.123Z");
        assertEquals("2022-12-31 00:59:59.123", df.format(date));
    }

    @Test
    public void testParseISO8601WithTimeZoneOffset() throws ParseException {
        DateFormat df = new StdDateFormat(Locale.US);
        Date date = df.parse("2022-12-31T23:59:59.123+01:00");
        assertEquals("2022-12-31 00:59:59.123", df.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidISO8601() throws ParseException {
        DateFormat df = new StdDateFormat();
        df.parse("2022-12-31T23:59:59.123A");
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601MissingTimeZone() throws ParseException {
        DateFormat df = new StdDateFormat();
        df.parse("2022-12-31T23:59:59.123");
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        DateFormat df = new StdDateFormat();
        Date date = df.parse("Tue, 31 Dec 2022 23:59:59 GMT");
        assertEquals("Tue Dec 31 23:59:59 GMT 2022", df.format(date));
    }

    @Test
    public void testParseRFC1123WithTimeZone() throws ParseException {
        DateFormat df = new StdDateFormat(TimeZone.getTimeZone("UTC+1"), Locale.US);
        Date date = df.parse("Tue, 31 Dec 2022 23:59:59 GMT");
        assertEquals("Wed Dec 31 00:59:59 GMT 2022", df.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidRFC1123() throws ParseException {
        DateFormat df = new StdDateFormat();
        df.parse("Mon, 31 Dec 2022 23:59:59 GMT01:00");
    }
}