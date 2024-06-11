package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.io.NumberInput;

/**
 * Test class for StdDateFormat.
 */
public class StdDateFormatTest {

    private StdDateFormat dateFormat;

    @Before
    public void setUp() {
        dateFormat = new StdDateFormat();
    }

    /**
     * Test parsing with default format.
     */
    @Test
    public void testParseDefaultFormat() {
        Date date = dateFormat.parse("2022-12-31T23:59:59.123-05:00");
        assertNotNull(date);
    }

    /**
     * Test parsing with ISO-8601 format.
     */
    @Test
    public void testParseISO8601Format() {
        Date date = dateFormat.parse("2022-12-31T23:59:59.123Z");
        assertNotNull(date);
    }

    /**
     * Test parsing with RFC-1123 format.
     */
    @Test
    public void testParseRFC1123Format() {
        Date date = dateFormat.parse("Sat, 31 Dec 2022 23:59:59 -0500");
        assertNotNull(date);
    }

    /**
     * Test parsing with plain format.
     */
    @Test
    public void testParsePlainFormat() {
        Date date = dateFormat.parse("2022-12-31");
        assertNotNull(date);
    }

    /**
     * Test parsing with invalid format.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() {
        dateFormat.parse("InvalidFormat");
    }

    /**
     * Test formatting with default format.
     */
    @Test
    public void testFormatDefaultFormat() {
        String dateString = dateFormat.format(new Date());
        assertNotNull(dateString);
        assertEquals("Sat Jan 01 00:00:00 GMT+00:00 2022", dateString);
    }

    /**
     * Test formatting with ISO-8601 format.
     */
    @Test
    public void testFormatISO8601Format() {
        String dateString = dateFormat.format(new Date());
        assertNotNull(dateString);
        assertEquals("2022-01-01T00:00:00.000Z", dateString);
    }

    /**
     * Test formatting with RFC-1123 format.
     */
    @Test
    public void testFormatRFC1123Format() {
        String dateString = dateFormat.format(new Date());
        assertNotNull(dateString);
        assertEquals("Sun, 01 Jan 2022 00:00:00 GMT", dateString);
    }

    /**
     * Test formatting with plain format.
     */
    @Test
    public void testFormatPlainFormat() {
        String dateString = dateFormat.format(new Date());
        assertNotNull(dateString);
        assertEquals("2022-01-01", dateString);
    }
}