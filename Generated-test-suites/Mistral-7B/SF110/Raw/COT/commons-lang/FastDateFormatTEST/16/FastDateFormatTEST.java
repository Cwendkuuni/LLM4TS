package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatCache;
import org.apache.commons.lang3.time.FastDatePrinter;
import org.apache.commons.lang3.time.FastDateParser;

public class FastDateFormatTest {

    @org.junit.Test
    public void testGetInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy", timeZone);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy", Locale.FRENCH);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy", timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    // other test methods for getDateInstance, getTimeInstance, getDateTimeInstance methods

    @org.junit.Test
    public void testFormat() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        Calendar calendar = new GregorianCalendar(2022, 11, 31);
        String expected = "31/12/2022";
        String actual = fastDateFormat.format(calendar);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testFormatLong() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
        Calendar calendar = new GregorianCalendar(2022, 11, 31);
        String expected = "December 31, 2022";
        String actual = fastDateFormat.format(calendar);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testFormatShort() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
        Calendar calendar = new GregorianCalendar(2022, 11, 31);
        String expected = "12/31/2022";
        String actual = fastDateFormat.format(calendar);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testFormatTime() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        Calendar calendar = new GregorianCalendar(2022, 11, 31, 23, 59, 59);
        String expected = "23:59:59";
        String actual = fastDateFormat.format(calendar);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testFormatDateTime() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        Calendar calendar = new GregorianCalendar(2022, 11, 31, 23, 59, 59);
        String expected = "31-Dec-2022 23:59:59";
        String actual = fastDateFormat.format(calendar);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testParse() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        String dateString = "31/12/2022";
        Date expected = new Date(1671235200000L);
        Date actual = fastDateFormat.parse(dateString);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testParseWithParsePosition() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        String dateString = "31/12/2022";
        ParsePosition pos = new ParsePosition(0);
        Date expected = new Date(1671235200000L);
        Date actual = fastDateFormat.parse(dateString, pos);
        assertEquals(expected, actual);
        assertEquals(dateString.length(), pos.getIndex());
    }

    // other test methods for parseObject, getPattern, getTimeZone, getLocale, getMaxLengthEstimate, equals, and hashCode methods.

    // Testing edge cases and exception handling

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        FastDateFormat fastDateFormat = new FastDateFormat("", null, null);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testNullCalendar() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        StringBuffer buf = new StringBuffer();
        fastDateFormat.format(null, buf, new FieldPosition(0, 0));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNegativeDateMilliseconds() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        long millis = -1;
        try {
            fastDateFormat.format(millis);
        } catch (IllegalArgumentException e) {
            assertEquals("Negative value encountered (-1)", e.getMessage());
            throw e;
        }
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        String dateString = "31/13/2022";
        ParsePosition pos = new ParsePosition(0);
        fastDateFormat.parse(dateString, pos);
        fail("Expected ParseException");
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseInvalidMilliseconds() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String dateString = "1234567890987654321";
        ParsePosition pos = new ParsePosition(0);
        Date date = fastDateFormat.parse(dateString, pos);
        fail("Expected ParseException");
    }
}