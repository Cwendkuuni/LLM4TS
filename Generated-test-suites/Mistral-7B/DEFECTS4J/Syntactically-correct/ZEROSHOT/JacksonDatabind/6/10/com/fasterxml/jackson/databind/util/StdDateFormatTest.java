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
    public void testStdDateFormat() {
        StdDateFormat dateFormat = StdDateFormat.instance;

        // Test default parsing
        Date date = dateFormat.parse("2022-01-01T12:34:56.789Z");
        assertEquals("Incorrect date parsed from ISO 8601 format", new Date(1645150846000L), date);

        // Test default parsing with different timezone
        dateFormat = dateFormat.withTimeZone(TimeZone.getTimeZone("Europe/London"));
        date = dateFormat.parse("2022-01-01T12:34:56.789Z");
        assertEquals("Incorrect date parsed from ISO 8601 format with Europe/London timezone",
                new Date(1645154446000L), date);

        // Test parsing with RFC 1123 format
        DateFormat rfc1123Format = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("Europe/London"), Locale.US);
        date = rfc1123Format.parse("Monday, 01-Jan-22 12:34:56 GMT");
        assertEquals("Incorrect date parsed from RFC 1123 format", new Date(1645154446000L), date);

        // Test parsing with ISO 8601 format and milliseconds
        DateFormat iso8601Format = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("Europe/London"), Locale.US);
        date = iso8601Format.parse("2022-01-01T12:34:56.789Z");
        assertEquals("Incorrect date parsed from ISO 8601 format with milliseconds",
                new Date(1645150846789L), date);

        // Test parsing with ISO 8601 format and missing timezone
        dateFormat = StdDateFormat.instance;
        date = dateFormat.parse("2022-01-01T12:34:56");
        assertNotNull("Date should not be null", date);
        assertTrue("Date should be before current date", date.before(new Date()));

        // Test parsing with RFC 1123 format and missing timezone
        rfc1123Format = StdDateFormat.getRFC1123Format(Locale.US);
        date = rfc1123Format.parse("Monday, 01-Jan-22 12:34:56");
        assertNotNull("Date should not be null", date);
        assertTrue("Date should be before current date", date.before(new Date()));

        // Test parsing with ISO 8601 format and invalid date
        try {
            dateFormat.parse("2022-01-33T12:34:56.789Z");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        // Test parsing with RFC 1123 format and invalid date
        try {
            rfc1123Format.parse("Monday, 33-Jan-22 12:34:56");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }
}