package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatUTCDate() throws Exception {
        Date date = new GregorianCalendar(2023, 5, 15).getTime();
        String formatted = ISO8601Utils.format(date);
        assertEquals("2023-06-15T00:00:00Z", formatted);
    }

    @Test
    public void testFormatDateWithMilliseconds() throws Exception {
        Date date = new GregorianCalendar(2023, 5, 15, 12, 34, 56).getTime();
        String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("2023-06-15T12:34:56.056Z", formatted);
    }

    @Test
    public void testFormatDateWithCustomTimezone() throws Exception {
        Date date = new GregorianCalendar(2023, 5, 15).getTime();
        String formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("America/New_York"));
        assertEquals("2023-06-15T05:00:00-04:00", formatted);
    }

    @Test
    public void testParseUTCDate() throws Exception {
        Date parsed = ISO8601Utils.parse("2023-06-15T00:00:00Z", new ParsePosition(0));
        assertEquals(new GregorianCalendar(2023, 5, 15).getTime(), parsed);
    }

    @Test
    public void testParseDateWithMilliseconds() throws Exception {
        Date parsed = ISO8601Utils.parse("2023-06-15T12:34:56.056Z", new ParsePosition(0));
        assertEquals(new GregorianCalendar(2023, 5, 15, 12, 34, 56).getTime(), parsed);
    }

    @Test
    public void testParseDateWithCustomTimezone() throws Exception {
        Date parsed = ISO8601Utils.parse("2023-06-15T05:00:00-04:00", new ParsePosition(0));
        assertEquals(new GregorianCalendar(2023, 5, 15, 11, 0, 0, Calendar.DAY_OF_WEEK, TimeZone.getTimeZone("America/New_York")).getTime(), parsed);
    }

    @Test
    public void testParseDateWithInvalidFormat() throws Exception {
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2023-06-15T12:34:56", new ParsePosition(0)));
    }

    @Test
    public void testParseDateWithMissingTimezone() throws Exception {
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2023-06-15T12:34:56", new ParsePosition(0)));
    }

    @Test
    public void testParseDateWithInvalidTimezone() throws Exception {
        assertThrows(ParseException.class, () -> ISO8601Utils.parse("2023-06-15T12:34:56+10:00:01", new ParsePosition(0)));
    }
}