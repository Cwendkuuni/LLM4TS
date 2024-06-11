package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws ParseException {
        DateFormat df = StdDateFormat.getInstance();
        Date date = df.parse("2022-12-31T23:59:59.123Z");
        assertEquals("2022-12-31 23:59:59.123", df.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601InvalidFormat() throws ParseException {
        DateFormat df = StdDateFormat.getInstance();
        df.parse("2022-12-31T23:59:59.123A"); // wrong format
    }

    @Test
    public void testParseISO8601WithTimezoneOffset() throws ParseException {
        DateFormat df = StdDateFormat.getInstance();
        Date date = df.parse("2022-12-31T23:59:59.123+01:00");
        assertEquals("2022-12-31 23:59:59.123", df.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601WithInvalidTimezoneOffset() throws ParseException {
        DateFormat df = StdDateFormat.getInstance();
        df.parse("2022-12-31T23:59:59.123+04:60"); // invalid timezone offset
    }

    @Test
    public void testParsePlainDate() throws ParseException {
        DateFormat df = StdDateFormat.getInstance();
        Date date = df.parse("2022-12-31");
        assertEquals("Dec 31, 2022", df.format(date));
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        DateFormat df = StdDateFormat.getInstance();
        Date date = df.parse("Thu, 31 Dec 2022 23:59:59 GMT");
        assertEquals("Dec 31, 2022 23:59:59", df.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123InvalidFormat() throws ParseException {
        DateFormat df = StdDateFormat.getInstance();
        df.parse("Thu, 31 Dec 2022 23:59:59 GMT+02:30"); // wrong format
    }
}