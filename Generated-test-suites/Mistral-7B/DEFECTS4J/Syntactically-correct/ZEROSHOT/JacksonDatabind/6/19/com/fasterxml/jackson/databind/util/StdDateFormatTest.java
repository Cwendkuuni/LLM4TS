package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    private static final StdDateFormat DATE_FORMAT = StdDateFormat.instance;

    @Test
    public void testGetBlueprintISO8601Format_deprecated() {
        assertSame(DATE_FORMAT.DATE_FORMAT_ISO8601, DATE_FORMAT.getBlueprintISO8601Format());
    }

    @Test(expected = Deprecated.class)
    public void testGetBlueprintISO8601Format_deprecatedWithTimeZone() {
        DATE_FORMAT.getBlueprintISO8601Format(null);
    }

    @Test
    public void testGetISO8601Format() {
        DateFormat dateFormat = DATE_FORMAT.getISO8601Format(null, null);
        assertSame(DATE_FORMAT.DATE_FORMAT_ISO8601, dateFormat);
    }

    @Test
    public void testGetISO8601FormatWithTimeZone() {
        DateFormat dateFormat = DATE_FORMAT.getISO8601Format(TimeZone.getTimeZone("UTC"), null);
        assertSame(DATE_FORMAT.DATE_FORMAT_ISO8601, dateFormat);
    }

    @Test
    public void testGetISO8601FormatWithLocale() {
        DateFormat dateFormat = DATE_FORMAT.getISO8601Format(null, Locale.FRENCH);
        assertSame(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.FRENCH), dateFormat);
    }

    @Test
    public void testGetISO8601FormatWithTimeZoneAndLocale() {
        DateFormat dateFormat = DATE_FORMAT.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.FRENCH);
        assertSame(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.FRENCH), dateFormat);
    }

    @Test
    public void testGetRFC1123Format() {
        DateFormat dateFormat = DATE_FORMAT.getRFC1123Format(null, null);
        assertSame(DATE_FORMAT.DATE_FORMAT_RFC1123, dateFormat);
    }

    @Test
    public void testGetRFC1123FormatWithTimeZone() {
        DateFormat dateFormat = DATE_FORMAT.getRFC1123Format(TimeZone.getTimeZone("UTC"), null);
        assertSame(DATE_FORMAT.DATE_FORMAT_RFC1123, dateFormat);
    }

    @Test
    public void testGetRFC1123FormatWithLocale() {
        DateFormat dateFormat = DATE_FORMAT.getRFC1123Format(null, Locale.FRENCH);
        assertSame(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.FRENCH), dateFormat);
    }

    @Test
    public void testGetRFC1123FormatWithTimeZoneAndLocale() {
        DateFormat dateFormat = DATE_FORMAT.getRFC1123Format(TimeZone.getTimeZone("UTC"), Locale.FRENCH);
        assertSame(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.FRENCH), dateFormat);
    }

    @Test
    public void testWithTimeZone() {
        StdDateFormat withTimeZone = DATE_FORMAT.withTimeZone(TimeZone.getTimeZone("UTC"));
        assertNotSame(DATE_FORMAT, withTimeZone);
        assertEquals(TimeZone.getTimeZone("UTC"), withTimeZone.getTimeZone());
        assertEquals(Locale.US, withTimeZone.getLocale());
    }

    @Test
    public void testWithLocale() {
        StdDateFormat withLocale = DATE_FORMAT.withLocale(Locale.FRENCH);
        assertNotSame(DATE_FORMAT, withLocale);
        assertEquals(TimeZone.getDefault(), withLocale.getTimeZone());
        assertEquals(Locale.FRENCH, withLocale.getLocale());
    }

    @Test
    public void testClone() {
        StdDateFormat original = DATE_FORMAT;
        StdDateFormat cloned = original.clone();
        assertNotSame(original, cloned);
        assertEquals(original.getTimeZone(), cloned.getTimeZone());
        assertEquals(original.getLocale(), cloned.getLocale());
    }

    @Test
    public void testParseISO8601Date() {
        DateFormat dateFormat = DATE_FORMAT.getISO8601Format(null, null);
        Date date = dateFormat.parse("2023-03-27T15:30:45.012Z");
        assertEquals(new Date(1679623045012L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601InvalidDate() {
        DateFormat dateFormat = DATE_FORMAT.getISO8601Format(null, null);
        dateFormat.parse("2023-3-27T15:30:45.012Z"); // invalid month
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123Date() {
        DateFormat dateFormat = DATE_FORMAT.getRFC1123Format(null, null);
        dateFormat.parse("Mon, 27 Mar 2023 15:30:45 GMT"); // missing timezone info
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123InvalidDate() {
        DateFormat dateFormat = DATE_FORMAT.getRFC1123Format(null, null);
        dateFormat.parse("Mon, 27 Mar 2023 15:30:45 ABC"); // invalid timezone
    }

    @Test
    public void testFormatDate() {
        DateFormat dateFormat = DATE_FORMAT.getISO8601Format(null, null);
        String formattedDate = dateFormat.format(new Date(1679623045012L));
        assertEquals("2023-03-27T15:30:45.012Z", formattedDate);
    }

    @Test
    public void testToString() {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.FRENCH);
        String toString = dateFormat.toString();
        assertTrue(toString.contains("DateFormat "));
        assertTrue(toString.contains("timezone: " + TimeZone.getTimeZone("UTC").getID()));
        assertTrue(toString.contains("locale: " + Locale.FRENCH));
    }
}