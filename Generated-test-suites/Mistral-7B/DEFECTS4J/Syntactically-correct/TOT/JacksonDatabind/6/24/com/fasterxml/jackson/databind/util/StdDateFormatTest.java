package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * Test class for {@link StdDateFormat}.
 */
public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws ParseException {
        // Test parsing using default timezone and locale
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = dateFormat.parse("2022-01-01T12:34:56.789Z");
        assertEquals(new Date(1645273846000L), date);

        // Test parsing with specific timezone and locale
        StdDateFormat dateFormatWithTimeZone = dateFormat.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        date = dateFormatWithTimeZone.parse("2022-01-01T12:34:56.789Z");
        assertEquals(new Date(1645227446000L), date); // Paris is GMT+1

        StdDateFormat dateFormatWithLocale = dateFormatWithTimeZone.withLocale(Locale.FRANCE);
        date = dateFormatWithLocale.parse("2022-01-01T12:34:56.789Z");
        assertEquals(new Date(1645227446000L), date); // Paris is GMT+1 and locale is French
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        // Test parsing using default timezone and locale
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = dateFormat.parse("Wed, 01 Jan 2022 12:34:56 +0100");
        assertEquals(new Date(1645227446000L), date);

        // Test parsing with specific timezone and locale
        StdDateFormat dateFormatWithTimeZone = dateFormat.withTimeZone(TimeZone.getTimeZone("America/New_York"));
        date = dateFormatWithTimeZone.parse("Wed, 01 Jan 2022 12:34:56 +0100");
        assertEquals(new Date(1645160046000L), date); // New York is GMT-5

        StdDateFormat dateFormatWithLocale = dateFormatWithTimeZone.withLocale(Locale.US);
        date = dateFormatWithLocale.parse("Wed, 01 Jan 2022 12:34:56 +0100");
        assertEquals(new Date(1645160046000L), date); // New York is GMT-5 and locale is US
    }

    @Test
    public void testParseInvalidFormat() {
        // Test parsing with invalid format using default timezone and locale
        StdDateFormat dateFormat = StdDateFormat.instance;
        assertThrows(ParseException.class, () -> dateFormat.parse("InvalidFormat"));

        // Test parsing with invalid format with specific timezone and locale
        StdDateFormat dateFormatWithTimeZone = dateFormat.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        assertThrows(ParseException.class, () -> dateFormatWithTimeZone.parse("InvalidFormat"));

        StdDateFormat dateFormatWithLocale = dateFormatWithTimeZone.withLocale(Locale.FRANCE);
        assertThrows(ParseException.class, () -> dateFormatWithLocale.parse("InvalidFormat"));
    }

    @Test
    public void testFormat() {
        // Test formatting using default timezone and locale
        StdDateFormat dateFormat = StdDateFormat.instance;
        String dateString = dateFormat.format(new Date(1645273846000L));
        assertEquals("2022-01-01T12:34:56.789Z", dateString);

        // Test formatting with specific timezone and locale
        StdDateFormat dateFormatWithTimeZone = dateFormat.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        dateString = dateFormatWithTimeZone.format(new Date(1645227446000L));
        assertEquals("2022-01-01T13:34:56.789+01:00", dateString);

        StdDateFormat dateFormatWithLocale = dateFormatWithTimeZone.withLocale(Locale.FRANCE);
        dateString = dateFormatWithLocale.format(new Date(1645227446000L));
        assertEquals("2022-01-01 à 13:34:56.789", dateString);
    }

    @Test
    public void testClone() {
        // Test cloning using default timezone and locale
        StdDateFormat dateFormat = StdDateFormat.instance;
        StdDateFormat clonedDateFormat = dateFormat.clone();
        assertNotNull(clonedDateFormat);

        // Test cloning with specific timezone and locale
        StdDateFormat dateFormatWithTimeZone = dateFormat.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        StdDateFormat clonedDateFormatWithTimeZone = dateFormatWithTimeZone.clone();
        assertNotNull(clonedDateFormatWithTimeZone);

        StdDateFormat dateFormatWithLocale = dateFormatWithTimeZone.withLocale(Locale.FRANCE);
        StdDateFormat clonedDateFormatWithLocale = dateFormatWithLocale.clone();
        assertNotNull(clonedDateFormatWithLocale);
    }

    @Test
    public void testGetInstance() {
        // Test getting instance using default timezone and locale
        StdDateFormat dateFormat = StdDateFormat.instance;
        assertEquals(dateFormat, StdDateFormat.instance);

        // Test getting instance with specific timezone and locale
        StdDateFormat dateFormatWithTimeZone = dateFormat.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        StdDateFormat dateFormat2WithTimeZone = StdDateFormat.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        assertEquals(dateFormatWithTimeZone, dateFormat2WithTimeZone);

        StdDateFormat dateFormatWithLocale = dateFormatWithTimeZone.withLocale(Locale.FRANCE);
        StdDateFormat dateFormat2WithLocale = StdDateFormat.withTimeZone(TimeZone.getTimeZone("Europe/Paris")).withLocale(Locale.FRANCE);
        assertEquals(dateFormatWithLocale, dateFormat2WithLocale);
    }

    @Test
    public void testSetTimeZone() {
        // Test setting timezone using default timezone and locale
        StdDateFormat dateFormat = StdDateFormat.instance;
        assertNull(dateFormat.getTimeZone());
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        assertEquals(TimeZone.getTimeZone("Europe/Paris"), dateFormat.getTimeZone());

        // Test setting timezone with specific timezone and locale
        StdDateFormat dateFormatWithTimeZone = StdDateFormat.instance.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        assertNotNull(dateFormatWithTimeZone.getTimeZone());
        dateFormatWithTimeZone.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        assertEquals(TimeZone.getTimeZone("America/New_York"), dateFormatWithTimeZone.getTimeZone());
    }

    @Test
    public void testSetLocale() {
        // Test setting locale using default timezone and locale
        StdDateFormat dateFormat = StdDateFormat.instance;
        assertEquals(Locale.US, dateFormat.getLocale());
        dateFormat.setLocale(Locale.FRANCE);
        assertEquals(Locale.FRANCE, dateFormat.getLocale());

        // Test setting locale with specific timezone and locale
        StdDateFormat dateFormatWithTimeZone = StdDateFormat.instance.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        assertEquals(Locale.FRANCE, dateFormatWithTimeZone.getLocale());
        dateFormatWithTimeZone.setLocale(Locale.GERMAN);
        assertEquals(Locale.GERMAN, dateFormatWithTimeZone.getLocale());
    }

    @Test
    public void testGetBlueprintISO8601Format() {
        // Test getting blueprint ISO-8601 format
        StdDateFormat dateFormat = StdDateFormat.instance;
        assertEquals(DATE_FORMAT_ISO8601, dateFormat.getBlueprintISO8601Format());
    }

    @Test
    public void testGetISO8601Format() {
        // Test getting ISO-8601 format with default timezone and locale
        StdDateFormat dateFormat = StdDateFormat.instance;
        DateFormat dateFormatWithTimeZone = dateFormat.getISO8601Format();
        assertEquals(DATE_FORMAT_ISO8601, dateFormatWithTimeZone);

        // Test getting ISO-8601 format with specific timezone and locale
        StdDateFormat dateFormatWithTimeZone = StdDateFormat.instance.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        DateFormat dateFormatWithLocaleAndTimeZone = dateFormatWithTimeZone.getISO8601Format();
        assertEquals(DATE_FORMAT_ISO8601, dateFormatWithLocaleAndTimeZone);

        StdDateFormat dateFormatWithLocale = dateFormatWithTimeZone.withLocale(Locale.FRANCE);
        DateFormat dateFormatWithLocaleAndTimeZoneAndLocale = dateFormatWithLocale.getISO8601Format();
        assertEquals(DATE_FORMAT_ISO8601, dateFormatWithLocaleAndTimeZoneAndLocale);
    }

    @Test
    public void testGetRFC1123Format() {
        // Test getting RFC-1123 format with default timezone and locale
        StdDateFormat dateFormat = StdDateFormat.instance;
        DateFormat dateFormatWithTimeZone = dateFormat.getRFC1123Format();
        assertEquals(DATE_FORMAT_RFC1123, dateFormatWithTimeZone);

        // Test getting RFC-1123 format with specific timezone and locale
        StdDateFormat dateFormatWithTimeZone = StdDateFormat.instance.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        DateFormat dateFormatWithLocaleAndTimeZone = dateFormatWithTimeZone.getRFC1123Format();
        assertEquals(DATE_FORMAT_RFC1123, dateFormatWithLocaleAndTimeZone);

        StdDateFormat dateFormatWithLocale = dateFormatWithTimeZone.withLocale(Locale.FRANCE);
        DateFormat dateFormatWithLocaleAndTimeZoneAndLocale = dateFormatWithLocale.getRFC1123Format();
        assertEquals(DATE_FORMAT_RFC1123, dateFormatWithLocaleAndTimeZoneAndLocale);
    }

    @Test
    public void testGetDefaultTimeZone() {
        // Test getting default timezone
        assertEquals(TimeZone.getTimeZone("GMT"), StdDateFormat.getDefaultTimeZone());
    }
}