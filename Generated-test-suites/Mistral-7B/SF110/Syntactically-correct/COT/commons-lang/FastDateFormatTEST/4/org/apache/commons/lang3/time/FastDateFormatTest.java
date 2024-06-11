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
import org.apache.commons.lang3.time.FastDateParser;
import org.apache.commons.lang3.time.FastDatePrinter;

public class FastDateFormatTest {

    @org.junit.Test
    public void testGetInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z", timeZone);
        assertNotNull(fastDateFormat);
        assertEquals(timeZone, fastDateFormat.getTimeZone());
    }

    @org.junit.Test
    public void testGetInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z", null, locale);
        assertNotNull(fastDateFormat);
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z", timeZone, locale);
        assertNotNull(fastDateFormat);
        assertEquals(timeZone, fastDateFormat.getTimeZone());
        assertEquals(locale, fastDateFormat.getLocale());
    }

    // ... other test methods for getDateInstance, getTimeInstance, getDateTimeInstance, and custom constructor

    @org.junit.Test
    public void testFormat() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        Calendar calendar = new GregorianCalendar(2022, 0, 1, 12, 30, 45, 123);
        String expected = "01-01-2022 12:30:45 PM UTC";
        String actual = fastDateFormat.format(calendar);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testFormatMilliseconds() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        long millis = 1640995234231L;
        String expected = "01-01-2022 10:37:14 PM UTC";
        String actual = fastDateFormat.format(millis);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testFormatDate() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        Date date = new Date(1640995234231L);
        String expected = "01-01-2022 10:37:14 PM UTC";
        String actual = fastDateFormat.format(date);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testFormatCalendar() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        Calendar calendar = new GregorianCalendar(2022, 0, 1, 12, 30, 45, 123);
        String expected = "01-01-2022 12:30:45 PM UTC";
        String actual = fastDateFormat.format(calendar);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testParse() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        String dateString = "01-01-2022 10:37:14 PM UTC";
        Date expectedDate = new Date(1640995234231L);
        Date actualDate = fastDateFormat.parse(dateString);
        assertEquals(expectedDate, actualDate);
    }

    @org.junit.Test
    public void testParseWithParsePosition() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        String dateString = "01-01-2022 10:37:14 PM UTC";
        ParsePosition parsePosition = new ParsePosition(0);
        Date actualDate = fastDateFormat.parse(dateString, parsePosition);
        Date expectedDate = new Date(1640995234231L);
        assertEquals(expectedDate, actualDate);
        assertEquals(dateString.length(), parsePosition.getIndex());
    }

    @org.junit.Test
    public void testParseObject() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        String dateString = "01-01-2022 10:37:14 PM UTC";
        Date actualDate = (Date)fastDateFormat.parseObject(dateString);
        Date expectedDate = new Date(1640995234231L);
        assertEquals(expectedDate, actualDate);
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseInvalidDateString() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        String dateString = "01-01-2022 10:37:14 PM X";
        fastDateFormat.parse(dateString);
    }

    @org.junit.Test
    public void testGetPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        String expected = "dd-MM-yyyy hh:mm:ss a z";
        String actual = fastDateFormat.getPattern();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        TimeZone expected = TimeZone.getTimeZone("UTC");
        TimeZone actual = fastDateFormat.getTimeZone();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z", Locale.FRENCH);
        Locale expected = Locale.FRENCH;
        Locale actual = fastDateFormat.getLocale();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        int expected = 41;
        int actual = fastDateFormat.getMaxLengthEstimate();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        assertTrue(fastDateFormat1.equals(fastDateFormat2));
    }

    @org.junit.Test
    public void testNotEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        Object obj = new Object();
        assertFalse(fastDateFormat1.equals(obj));
    }

    @org.junit.Test
    public void testHashCode() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        int expected = fastDateFormat.hashCode();
        int actual = fastDateFormat.hashCode();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        String expected = "FastDateFormat[dd-MM-yyyy hh:mm:ss a z, null, UTC]";
        String actual = fastDateFormat.toString();
        assertEquals(expected, actual);
    }

    // ... other test methods for applyRules method

}