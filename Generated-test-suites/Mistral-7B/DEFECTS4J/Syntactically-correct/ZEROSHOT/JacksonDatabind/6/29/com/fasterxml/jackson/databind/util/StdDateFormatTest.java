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
        Date date = dateFormat.parse(dateString);
        assertNotNull(date);
        assertEquals("Wed Dec 31 23:59:59 GMT", dateFormat.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidISO8601() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "2022-12-31T23:59:59.123A";
        dateFormat.parse(dateString);
    }

    @Test
    public void testParseISO8601WithTimeZone() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        String dateString = "2022-12-31T23:59:59.123Z";
        Date date = dateFormat.parse(dateString);
        assertNotNull(date);
        assertEquals("Wed Dec 31 23:59:59 CET 2022", dateFormat.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidISO8601WithTimeZone() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        String dateString = "2022-12-31T23:59:59.123A";
        dateFormat.parse(dateString);
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "Wed, 31 Dec 2022 23:59:59 GMT";
        Date date = dateFormat.parse(dateString);
        assertNotNull(date);
        assertEquals("Wed Dec 31 23:59:59 GMT", dateFormat.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidRFC1123() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "Wed, 31 Dec 2022 23:59:59 A";
        dateFormat.parse(dateString);
    }

    @Test
    public void testParsePlainDate() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "2022-12-31";
        Date date = dateFormat.parse(dateString);
        assertNotNull(date);
        assertEquals("Sat Dec 31 00:00:00 GMT", dateFormat.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidPlainDate() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = "2022-12-31A";
        dateFormat.parse(dateString);
    }

    @Test
    public void testFormatISO8601() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = new Date();
        String dateString = dateFormat.format(date);
        assertNotNull(dateString);
        assertTrue(dateString.matches(".*[+-]\\d{4}"));
    }

    @Test
    public void testFormatRFC1123() {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = new Date();
        String dateString = dateFormat.format(date);
        assertNotNull(dateString);
        String[] patterns = {"E, d MMM yyyy", "EEE, dd MMM yyyy"};
        for (String pattern : patterns) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            String formattedDate = sdf.format(date);
            assertTrue(formattedDate.equals(dateString) || formattedDate.startsWith(dateString.substring(0, 25)));
        }
    }

    @Test
    public void testClone() {
        StdDateFormat original = StdDateFormat.instance;
        StdDateFormat clone = original.clone();
        assertNotSame(original, clone);
        assertArrayEquals(original.getAllFormats(), clone.getAllFormats());
    }
}