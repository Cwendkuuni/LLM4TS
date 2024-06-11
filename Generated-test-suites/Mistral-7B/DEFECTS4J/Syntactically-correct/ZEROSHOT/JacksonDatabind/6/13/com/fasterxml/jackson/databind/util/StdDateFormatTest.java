package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test class for StdDateFormat.
 */
public class StdDateFormatTest {

    @Test
    public void testStandardFormats() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;

        // Test standard formats
        Date date = stdDateFormat.parse("2022-12-31T23:59:59.999Z");
        assertEquals("Unexpected date for ISO8601 format", new Date(1672502399000L), date);

        date = stdDateFormat.parse("Sat, 31 Dec 2022 23:59:59 GMT");
        assertEquals("Unexpected date for RFC1123 format", new Date(1672502399000L), date);

        date = stdDateFormat.parse("31 Dec 2022");
        assertEquals("Unexpected date for plain format", new Date(1672486400000L), date);
    }

    @Test(expected = ParseException.class)
    public void testInvalidDateFormats() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;

        stdDateFormat.parse("InvalidDateFormat");
    }

    @Test
    public void testCustomTimeZone() {
        TimeZone customTimeZone = TimeZone.getTimeZone("America/New_York");
        StdDateFormat stdDateFormat = new StdDateFormat(customTimeZone, Locale.US);

        Date date = stdDateFormat.parse("2022-12-31T23:59:59.999Z");
        long timeInCustomTimeZone = date.getTime() + customTimeZone.getOffset(date.getTime());
        assertEquals("Unexpected date for ISO8601 format in custom timezone", timeInCustomTimeZone, 1672516099000L);
    }

    @Test
    public void testCustomLocale() {
        Locale customLocale = Locale.FRENCH;
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getDefault(), customLocale);

        Date date = stdDateFormat.parse("2022-12-31T23:59:59.999Z");
        String expectedFormat = "samedi, 31 déc 2022 23:59:59 GMT";
        String actualFormat = stdDateFormat.format(date, new StringBuffer(), new FieldPosition(0, 0));
        assertEquals("Unexpected format for ISO8601 format in custom locale", expectedFormat, actualFormat);
    }
}