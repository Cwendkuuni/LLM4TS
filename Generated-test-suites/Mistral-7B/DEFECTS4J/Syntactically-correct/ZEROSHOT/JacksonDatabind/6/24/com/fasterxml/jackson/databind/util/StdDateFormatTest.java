package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "2022-12-31T23:59:59.123Z";
        Date parsedDate = dateFormat.parse(dateString);
        Date expectedDate = new Date(1672611199123L);
        assertEquals(expectedDate, parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601InvalidFormat() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "2022-12-31T23:59:59.123A";
        dateFormat.parse(dateString);
    }

    @Test
    public void testParseISO8601WithTimeZone() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        String dateString = "2022-12-31T23:59:59.123+01:00";
        Date parsedDate = dateFormat.parse(dateString);
        Date expectedDate = new Date(System.currentTimeMillis() - 60 * 60 * 1000L);
        assertEquals(expectedDate, parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601WithTimeZoneInvalidFormat() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        String dateString = "2022-12-31T23:59:59.123+02:00";
        dateFormat.parse(dateString);
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "Sat, 31 Dec 2022 23:59:59 GMT";
        Date parsedDate = dateFormat.parse(dateString);
        Date expectedDate = new Date(1672611199000L);
        assertEquals(expectedDate, parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123InvalidFormat() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "Sat, 31 Dec 2022 23:59:59 HH:MM";
        dateFormat.parse(dateString);
    }

    @Test
    public void testFormat() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = new Date();

        String expectedISO8601 = "Sat Dec 31 00:00:00 CET 2022";
        String formattedISO8601 = dateFormat.format(date);
        assertEquals(expectedISO8601, formattedISO8601);

        String expectedRFC1123 = "Sat, 03 Dec 2022 14:56:12 CET";
        String formattedRFC1123 = dateFormat.format(date, new StringBuffer(), null);
        assertEquals(expectedRFC1123, formattedRFC1123.toString());
    }

    @Test
    public void testClone() {
        StdDateFormat original = StdDateFormat.instance;
        StdDateFormat cloned = original.clone();

        Date date = new Date();
        String formattedOriginal = original.format(date);
        String formattedCloned = cloned.format(date);

        assertEquals(formattedOriginal, formattedCloned);
    }
}