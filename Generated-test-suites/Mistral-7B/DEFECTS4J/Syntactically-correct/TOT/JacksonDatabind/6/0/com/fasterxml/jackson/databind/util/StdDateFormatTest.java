package com.fasterxml.jackson.databind.util;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdDateFormatTest {

    @Test
    public void testISO8601Format() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date parse = dateFormat.parse("2021-12-31T23:59:59.123Z");
        assertEquals("2021-12-31 23:59:59.123", dateFormat.format(parse));
    }

    @Test
    public void testISO8601FormatWithTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/Paris"), Locale.FRENCH);
        Date parse = dateFormat.parse("2021-12-31T23:59:59.123Z");
        assertEquals("31 déc. 2021 23:59:59.123", dateFormat.format(parse));
    }

    @Test
    public void testISO8601FormatWithPlainDate() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date parse = dateFormat.parse("2021-12-31");
        assertEquals("31 déc. 2021", dateFormat.format(parse));
    }

    @Test
    public void testRFC1123Format() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        Date parse = dateFormat.parse("Fri, 31 Dec 2021 23:59:59 GMT");
        assertEquals("Fri, 31 Dec 2021 23:59:59 GMT", dateFormat.format(parse));
    }

    @Test
    public void testParseInvalidFormat() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat();
        assertNull(dateFormat.parse("Invalid date"));
    }

    @Test
    public void testParseInvalidFormatWithCustomTimeZone() throws ParseException {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/Paris"), Locale.FRENCH);
        assertNull(dateFormat.parse("Invalid date"));
    }
}