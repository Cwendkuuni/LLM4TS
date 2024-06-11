package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test class for ISO8601Utils.
 */
public class ISO8601UtilsTest {

    /**
     * Test case for format method with valid input.
     */
    @Test
    public void testFormatValidInput() {
        Date date = new Date(0L);
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("1970-01-01T00:00:00Z", formattedDate);

        date.setTime(1000L);
        formattedDate = ISO8601Utils.format(date, false, TimeZone.getDefault());
        assertEquals("1970-01-01T00:00:01Z", formattedDate);

        formattedDate = ISO8601Utils.format(date, true, TimeZone.getDefault());
        assertEquals("1970-01-01T00:00:01.000Z", formattedDate);

        TimeZone utc = TimeZone.getTimeZone("UTC");
        formattedDate = ISO8601Utils.format(date, false, utc);
        assertEquals("1970-01-01T00:00:01Z", formattedDate);

        formattedDate = ISO8601Utils.format(date, true, utc);
        assertEquals("1970-01-01T00:00:01.000Z", formattedDate);
    }

    /**
     * Test case for format method with invalid input.
     */
    @Test
    public void testFormatInvalidInput() {
        Date date = null;

        assertThrows(NullPointerException.class, () -> ISO8601Utils.format(date));
        assertThrows(IllegalArgumentException.class, () -> ISO8601Utils.format("String date", false, TimeZone.getDefault()));

        date = new Date(Long.MAX_VALUE);
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("Formatting failed at index 13", formattedDate.subSequence(0, 13).toString());
        assertNull(formattedDate.substring(13));
    }

    /**
     * Test case for parse method with valid input.
     */
    @Test
    public void testParseValidInput() {
        String dateString = "2022-11-17T14:30:45.123Z";
        Date date = ISO8601Utils.parse(dateString, new ParsePosition(0));
        assertEquals(2022, date.getYear());
        assertEquals(11, date.getMonth());
        assertEquals(17, date.getDate());
        assertEquals(14, date.getHours());
        assertEquals(30, date.getMinutes());
        assertEquals(45, date.getSeconds());
        assertEquals(123, date.getMilliseconds());

        Instant instant = date.toInstant();
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        assertEquals(2022, zdt.getYear());
        assertEquals(11, zdt.getMonthValue());
        assertEquals(17, zdt.getDayOfMonth());
        assertEquals(14, zdt.getHour());
        assertEquals(30, zdt.getMinute());
        assertEquals(45, zdt.getSecond());
        assertEquals(123, zdt.getNano());
    }

    /**
     * Test case for parse method with invalid input.
     */
    @Test
    public void testParseInvalidInput() {
        String dateString = null;
        ParsePosition pos = new ParsePosition(0);

        assertThrows(NullPointerException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertThrows(IndexOutOfBoundsException.class, () -> ISO8601Utils.parse("InvalidString", pos));

        dateString = "2022-11-31T23:59:59.999Z";
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals(13, pos.getIndex());
        assertEquals("Invalid format: Expected 'Z' but was '31'", pos.getErrorMessage());

        dateString = "2022-11-30T23:59:59+01:00";
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals(22, pos.getIndex());
        assertEquals("Invalid format: Invalid timezone offset: +01:01", pos.getErrorMessage());

        dateString = "2022-11-30T23:59:59+01:00:00";
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateString, pos));
        assertEquals(34, pos.getIndex());
        assertEquals("Invalid format: Invalid timezone offset: +01:01:01", pos.getErrorMessage());
    }
}