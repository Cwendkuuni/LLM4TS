package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24); // Yesterday

        String formatWithoutMillis = ISO8601Utils.format(date);
        assertEquals("2023-03-13", formatWithoutMillis);

        String formatWithMillis = ISO8601Utils.format(date, true, TimeZone.getDefault());
        assertEquals("2023-03-13T10:15:26.123Z", formatWithMillis); // Replace with current time

        String formatWithTz = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/Los_Angeles"));
        assertEquals("2023-03-13T17:15:26.123-07:00", formatWithTz); // Replace with current time and Pacific Time
    }

    @Test
    public void testParse() {
        String dateString = "2023-03-13T10:15:26.123Z"; // Replace with a valid ISO-8601 formatted string

        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);

        assertEquals("Mon Mar 13 10:15:26 CET 2023", new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy").format(parsedDate));
        assertEquals(0, pos.getIndex());

        assertNull(ISO8601Utils.parse(null, new ParsePosition(0)));
        assertNull(ISO8601Utils.parse("", new ParsePosition(0)));
        assertNull(ISO8601Utils.parse("not a date", new ParsePosition(0)));
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2023-13-13", new ParsePosition(0)));
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2023-03-13T10:15:26.123+01:00", new ParsePosition(0)));
    }
}