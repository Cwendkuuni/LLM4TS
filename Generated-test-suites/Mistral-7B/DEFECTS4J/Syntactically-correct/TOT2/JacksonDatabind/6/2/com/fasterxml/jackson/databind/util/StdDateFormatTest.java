package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Unit tests for {@link StdDateFormat} class.
 */
public class StdDateFormatTest {

    /**
     * Test parsing of known good dates using default instance.
     */
    @Test
    public void testParseGoodDates() throws ParseException {
        StdDateFormat df = StdDateFormat.instance;
        Date date;
        ParsePosition pos = new ParsePosition(0);

        // Test parsing of various ISO-8601 compliant dates
        date = df.parse("2001-01-01T12:00:00.123Z");
        assertEquals("Invalid date: 2001-01-01T12:00:00.123Z", df.getFormat(), df.getFormat());
        assertEquals("Incorrect date parsed", new Date(1022101200000L), date);
        pos.setIndex(date.getTime());

        date = df.parse("2001-01-01T12:00:00.123+01:00");
        assertEquals("Invalid date: 2001-01-01T12:00:00.123+01:00", df.getFormat(), df.getFormat());
        assertEquals("Incorrect date parsed", new Date(1022102100000L), date);

        date = df.parse("2001-01-01");
        assertEquals("Invalid date: 2001-01-01", df.getFormat(), df.getFormat());
        assertEquals("Incorrect date parsed", new Date(1022100000000L), date);

        // Test parsing of RFC-1123 compliant dates
        df = StdDateFormat.instance.withLocale(Locale.US);
        date = df.parse("Tue, 01 Jan 2001 12:00:00 GMT");
        assertEquals("Invalid date: Tue, 01 Jan 2001 12:00:00 GMT", df.getFormat(), df.getFormat());
        assertEquals("Incorrect date parsed", new Date(1022101200000L), date);
    }

    /**
     * Test parsing of known bad dates using default instance.
     */
    @Test(expected = ParseException.class)
    public void testParseBadDates() throws ParseException {
        StdDateFormat df = StdDateFormat.instance;

        // Test parsing of invalid formats
        df.parse("Invalid date format");

        // Test parsing of ill-formed dates with valid format strings
        df.parse("2001-01-33");
        df.parse("2001-02-29T12:00:00.123Z"); // non-existent month
        df.parse("2001-13-01T12:00:00.123Z"); // invalid month
        df.parse("2001-01-01T25:00:00.123Z"); // invalid hour
        df.parse("2001-01-01T12:60:00.123Z"); // invalid minute
        df.parse("2001-01-01T12:00:60.123Z"); // invalid second
        df.parse("2001-01-01T12:00:00.123A"); // invalid timezone indicator
    }

    /**
     * Test parsing of known good dates using custom instance.
     */
    @Test
    public void testParseGoodDatesCustom() throws ParseException {
        StdDateFormat df = new StdDateFormat(Locale.US, TimeZone.getTimeZone("Europe/Paris"));

        Date date;
        ParsePosition pos = new ParsePosition(0);

        // Test parsing of various ISO-8601 compliant dates
        date = df.parse("2001-01-01T12:00:00.123Z");
        assertEquals("Invalid date: 2001-01-01T12:00:00.123Z", df.getFormat(), df.getFormat());
        assertEquals("Incorrect date parsed", new Date(1022101200000L + 3600000), date);
        pos.setIndex(date.getTime());

        date = df.parse("2001-01-01T12:00:00.123+01:00");
        assertEquals("Invalid date: 2001-01-01T12:00:00.123+01:00", df.getFormat(), df.getFormat());
        assertEquals("Incorrect date parsed", new Date(1022102100000L + 3600000), date);

        date = df.parse("2001-01-01");
        assertEquals("Invalid date: 2001-01-01", df.getFormat(), df.getFormat());
        assertEquals("Incorrect date parsed", new Date(1022100000000L + 3600000), date);

        // Test parsing of RFC-1123 compliant dates
        date = df.parse("Tue, 01 Jan 2001 12:00:00 GMT");
        assertEquals("Invalid date: Tue, 01 Jan 2001 12:00:00 GMT", df.getFormat(), df.getFormat());
        assertEquals("Incorrect date parsed", new Date(1022101200000L + 3600000), date);
    }

    /**
     * Test parsing of known bad dates using custom instance.
     */
    @Test(expected = ParseException.class)
    public void testParseBadDatesCustom() throws ParseException {
        StdDateFormat df = new StdDateFormat(Locale.US, TimeZone.getTimeZone("Europe/Paris"));

        // Test parsing of invalid formats
        df.parse("Invalid date format");

        // Test parsing of ill-formed dates with valid format strings
        df.parse("2001-01-33");
        df.parse("2001-02-29T12:00:00.123Z"); // non-existent month
        df.parse("2001-13-01T12:00:00.123Z"); // invalid month
        df.parse("2001-01-01T25:00:00.123Z"); // invalid hour
        df.parse("2001-01-01T12:60:00.123Z"); // invalid minute
        df.parse("2001-01-01T12:00:60.123Z"); // invalid second
        df.parse("2001-01-01T12:00:00.123A"); // invalid timezone indicator
    }

    /**
     * Test formatting of known good dates using default instance.
     */
    @Test
    public void testFormatGoodDates() {
        StdDateFormat df = StdDateFormat.instance;
        String str;
        Date date;
        ParsePosition pos = new ParsePosition(0);

        date = new Date();
        str = df.format(date, new StringBuilder(), null);
        assertEquals("Invalid format string", "Sat Jan 17 12:57:04 PDT 2020", str);

        date = new Date(1022101200000L);
        str = df.format(date, new StringBuilder(), null);
        assertEquals("Invalid format string", "Sun Jan 06 12:00:00 PST 2002", str);

        date = new Date(1022102100000L);
        str = df.format(date, new StringBuilder(), null);
        assertEquals("Invalid format string", "Mon Jan 07 13:00:00 PST 2002", str);

        date = new Date(1022100000000L);
        str = df.format(date, new StringBuilder(), null);
        assertEquals("Invalid format string", "Mon Jan 07 00:00:00 PST 2002", str);
    }

    /**
     * Test formatting of known bad dates using default instance.
     */
    @Test
    public void testFormatBadDates() {
        StdDateFormat df = StdDateFormat.instance;
        String str;
        ParsePosition pos;

        Date date;
        try {
            date = df.parse("Invalid date format");
            pos = new ParsePosition(0);
            str = df.format(date, new StringBuilder(), null);
            fail("Expected ParseException but received valid string: " + str);
        } catch (ParseException e) {
            // Expected
        }

        try {
            date = new Date(-1L);
            str = df.format(date, new StringBuilder(), null);
            fail("Expected IllegalArgumentException but received valid string: " + str);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test formatting of known good dates using custom instance.
     */
    @Test
    public void testFormatGoodDatesCustom() {
        StdDateFormat df = new StdDateFormat(Locale.US, TimeZone.getDefault());
        String str;
        Date date;
        ParsePosition pos = new ParsePosition(0);

        date = new Date();
        str = df.format(date, new StringBuilder(), null);
        assertEquals("Invalid format string", "Sun Jan 17 15:57:04 PST 2021", str);

        date = new Date(1022101200000L);
        str = df.format(date, new StringBuilder(), null);
        assertEquals("Invalid format string", "Fri Jan 04 12:00:00 PST 2002", str);

        date = new Date(1022102100000L);
        str = df.format(date, new StringBuilder(), null);
        assertEquals("Invalid format string", "Sat Jan 05 13:00:00 PST 2002", str);

        date = new Date(1022100000000L);
        str = df.format(date, new StringBuilder(), null);
        assertEquals("Invalid format string", "Sat Jan 05 00:00:00 PST 2002", str);
    }

    /**
     * Test formatting of known bad dates using custom instance.
     */
    @Test
    public void testFormatBadDatesCustom() {
        StdDateFormat df = new StdDateFormat(Locale.US, TimeZone.getDefault());
        String str;
        ParsePosition pos;

        Date date;
        try {
            date = df.parse("Invalid date format");
            pos = new ParsePosition(0);
            str = df.format(date, new StringBuilder(), null);
            fail("Expected ParseException but received valid string: " + str);
        } catch (ParseException e) {
            // Expected
        }

        try {
            date = new Date(-1L);
            str = df.format(date, new StringBuilder(), null);
            fail("Expected IllegalArgumentException but received valid string: " + str);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test setTimeZone() method.
     */
    @Test
    public void testSetTimeZone() {
        StdDateFormat df = StdDateFormat.instance;

        // Test setting timezone with valid arguments
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals("Unexpected timezone", TimeZone.getTimeZone("UTC"), df.getTimeZone());

        df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        assertEquals("Unexpected timezone", TimeZone.getTimeZone("America/Los_Angeles"), df.getTimeZone());

        // Test setting timezone with null argument
        df.setTimeZone(null);
        assertEquals("Unexpected timezone", TimeZone.getDefault(), df.getTimeZone());
    }

    /**
     * Test setLocale() method.
     */
    @Test
    public void testSetLocale() {
        StdDateFormat df = StdDateFormat.instance;

        // Test setting locale with valid arguments
        df.setLocale(Locale.CHINESE);
        assertEquals("Unexpected locale", Locale.CHINESE, df.getLocale());

        df.setLocale(Locale.FRENCH);
        assertEquals("Unexpected locale", Locale.FRENCH, df.getLocale());

        // Test setting locale with null argument
        df.setLocale(null);
        assertEquals("Unexpected locale", Locale.getDefault(), df.getLocale());
    }

    /**
     * Test clone() method.
     */
    @Test
    public void testClone() throws CloneNotSupportedException {
        StdDateFormat df = new StdDateFormat(Locale.US, TimeZone.getTimeZone("Europe/Paris"));

        // Test cloning with valid arguments
        StdDateFormat cloned = (StdDateFormat) df.clone();
        assertEquals("Unexpected timezone", TimeZone.getTimeZone("Europe/Paris"), cloned.getTimeZone());
        assertEquals("Unexpected locale", Locale.US, cloned.getLocale());

        // Test cloning with null arguments
        df = null;
        try {
            cloned = (StdDateFormat) df.clone();
            fail("Expected CloneNotSupportedException but received StdDateFormat instance.");
        } catch (CloneNotSupportedException e) {
            // Expected
        }
    }
}