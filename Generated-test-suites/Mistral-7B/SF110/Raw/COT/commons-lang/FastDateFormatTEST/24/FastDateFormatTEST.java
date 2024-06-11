package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatCache;
import org.apache.commons.lang3.time.FastDatePrinter;
import org.apache.commons.lang3.time.FastDateParser;

import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy", timeZone);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy", null, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy", timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, timeZone);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testFormatDate() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.US);
        Date date = new Date();
        String formattedDate = fastDateFormat.format(date);
        assertNotNull(formattedDate);
        System.out.println("Formatted date: " + formattedDate);
    }

    @Test
    public void testParseDate() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.US);
        String dateString = "01/01/2022 12:34:56";
        Date parsedDate = fastDateFormat.parse(dateString);
        assertNotNull(parsedDate);
        System.out.println("Parsed date: " + parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseDateInvalidFormat() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.US);
        String dateString = "01/01/2022 12:34:56X";
        fastDateFormat.parse(dateString);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.US);
        String pattern = fastDateFormat.getPattern();
        assertNotNull(pattern);
        System.out.println("Pattern: " + pattern);
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.US);
        TimeZone timeZone = fastDateFormat.getTimeZone();
        assertNotNull(timeZone);
        System.out.println("Time zone: " + timeZone.getID());
    }

    @Test
    public void testGetLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.US);
        Locale locale = fastDateFormat.getLocale();
        assertNotNull(locale);
        System.out.println("Locale: " + locale.getDisplayName());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.US);
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
        assertNotNull(maxLengthEstimate);
        System.out.println("Max length estimate: " + maxLengthEstimate);
    }

    @Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.US);
        FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.US);
        assertTrue(fastDateFormat1.equals(fastDateFormat2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.US);
        FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.US);
        assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.US);
        String toString = fastDateFormat.toString();
        assertNotNull(toString);
        System.out.println("ToString: " + toString);
    }

    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.US);
        StringBuffer buf = new StringBuffer();
        fastDateFormat.applyRules(calendar, buf);
        assertNotNull(buf.toString());
        System.out.println("Formatted date: " + buf.toString());
    }
}