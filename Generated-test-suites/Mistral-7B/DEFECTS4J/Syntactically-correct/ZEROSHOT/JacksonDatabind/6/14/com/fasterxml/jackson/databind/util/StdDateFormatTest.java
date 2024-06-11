package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParsingISO8601WithTimezone() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date = stdDateFormat.parse("2022-12-31T23:59:59.123-01:00");
        assertNotNull(date);
    }

    @Test
    public void testParsingISO8601WithoutTimezone() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("2022-12-31T23:59:59.123Z");
        assertNotNull(date);
    }

    @Test
    public void testParsingISO8601WithoutTimezone_WithoutMilliseconds() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("2022-12-31T23:59:59Z");
        assertNotNull(date);
    }

    @Test
    public void testParsingISO8601WithPlainDate() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("2022-12-31");
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParsingNonISO8601Format() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat();
        stdDateFormat.parse("Invalid Date Format");
    }

    @Test
    public void testFormatting() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = new Date(1611846399000L);
        String formattedDate = stdDateFormat.format(date, new StringBuffer(), new FieldPosition(0, 0));
        assertEquals("2021-02-25 14:13:19.000", formattedDate.toString());
    }

    @Test
    public void testFormattingWithCustomTimezone() {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("Asia/Kolkata"), Locale.US);
        Date date = new Date(1611846399000L);
        String formattedDate = stdDateFormat.format(date, new StringBuffer(), new FieldPosition(0, 0));
        assertEquals("2021-02-25 22:23:19.000", formattedDate.toString());
    }

    @Test
    public void testFormattingWithCustomLocale() {
        StdDateFormat stdDateFormat = new StdDateFormat(null, Locale.FRENCH);
        Date date = new Date(1611846399000L);
        String formattedDate = stdDateFormat.format(date, new StringBuffer(), new FieldPosition(0, 0));
        assertEquals("vendredi 25 février 2021 21:23:19", formattedDate.toString());
    }
}