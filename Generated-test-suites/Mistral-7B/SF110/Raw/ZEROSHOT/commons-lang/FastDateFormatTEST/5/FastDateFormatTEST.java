package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

public class FastDateFormatTest {

    @org.junit.Test
    public void testGetInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPatternTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", timeZone);
        assertNotNull(fastDateFormat);
        assertEquals(timeZone, fastDateFormat.getTimeZone());
    }

    @org.junit.Test
    public void testGetInstanceWithPatternLocale() {
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", locale);
        assertNotNull(fastDateFormat);
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", timeZone, locale);
        assertNotNull(fastDateFormat);
        assertEquals(timeZone, fastDateFormat.getTimeZone());
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetDateInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance();
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithStyle() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithStyleLocale() {
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(fastDateFormat);
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetDateInstanceWithStyleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(fastDateFormat);
        assertEquals(timeZone, fastDateFormat.getTimeZone());
    }

    @org.junit.Test
    public void testGetDateInstanceWithStyleTimeZoneLocale() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(fastDateFormat);
        assertEquals(timeZone, fastDateFormat.getTimeZone());
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance();
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithStyle() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithStyleLocale() {
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, locale);
        assertNotNull(fastDateFormat);
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetTimeInstanceWithStyleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone);
        assertNotNull(fastDateFormat);
        assertEquals(timeZone, fastDateFormat.getTimeZone());
    }

    @org.junit.Test
    public void testGetTimeInstanceWithStyleTimeZoneLocale() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(fastDateFormat);
        assertEquals(timeZone, fastDateFormat.getTimeZone());
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance();
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyle() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleLocale() {
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT, locale);
        assertNotNull(fastDateFormat);
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT, timeZone);
        assertNotNull(fastDateFormat);
        assertEquals(timeZone, fastDateFormat.getTimeZone());
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleTimeZoneLocale() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(fastDateFormat);
        assertEquals(timeZone, fastDateFormat.getTimeZone());
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testFormat() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT);
        Date date = new Date();
        String formattedDate = fastDateFormat.format(date);
        assertNotNull(formattedDate);
        System.out.println("Formatted date: " + formattedDate);
    }

    @org.junit.Test
    public void testFormatWithBuffer() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT);
        Date date = new Date();
        StringBuffer stringBuffer = new StringBuffer();
        fastDateFormat.format(date, stringBuffer);
        String formattedDate = stringBuffer.toString();
        assertNotNull(formattedDate);
        System.out.println("Formatted date: " + formattedDate);
    }

    @org.junit.Test
    public void testParse() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT);
        String dateString = "26-12-2022 13:45:30 UTC";
        Date parsedDate = fastDateFormat.parse(dateString);
        assertNotNull(parsedDate);
        System.out.println("Parsed date: " + parsedDate);
    }

    @org.junit.Test
    public void testFormatWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT, locale);
        Date date = new Date();
        String formattedDate = fastDateFormat.format(date);
        assertNotNull(formattedDate);
        System.out.println("Formatted date (locale): " + formattedDate);
    }

    @org.junit.Test
    public void testFormatWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT, null, timeZone);
        Date date = new Date();
        String formattedDate = fastDateFormat.format(date);
        assertNotNull(formattedDate);
        System.out.println("Formatted date (timezone): " + formattedDate);
    }

    @org.junit.Test
    public void testFormatWithLocaleAndTimeZone() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT, locale, timeZone);
        Date date = new Date();
        String formattedDate = fastDateFormat.format(date);
        assertNotNull(formattedDate);
        System.out.println("Formatted date (locale and timezone): " + formattedDate);
    }

    @org.junit.Test
    public void testParseWithParsePosition() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT);
        String dateString = "26-12-2022 13:45:30 UTC";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = fastDateFormat.parse(dateString, parsePosition);
        assertNotNull(parsedDate);
        System.out.println("Parsed date with ParsePosition: " + parsedDate);
        assertEquals(dateString.length(), parsePosition.getIndex());
    }

    @org.junit.Test
    public void testGetPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT);
        String pattern = fastDateFormat.getPattern();
        assertNotNull(pattern);
        System.out.println("Pattern: " + pattern);
    }

    @org.junit.Test
    public void testGetTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT);
        TimeZone timeZone = fastDateFormat.getTimeZone();
        assertNotNull(timeZone);
        System.out.println("Timezone: " + timeZone.getID());
    }

    @org.junit.Test
    public void testGetLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT);
        Locale locale = fastDateFormat.getLocale();
        assertNotNull(locale);
        System.out.println("Locale: " + locale.getLanguage());
    }

    @org.junit.Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT);
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
        assertNotNull(maxLengthEstimate);
        System.out.println("Max length estimate: " + maxLengthEstimate);
    }

    @org.junit.Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT);
        FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT);
        assertTrue(fastDateFormat1.equals(fastDateFormat2));
    }

    @org.junit.Test
    public void testHashCode() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT);
        int hashCode = fastDateFormat.hashCode();
        System.out.println("Hashcode: " + hashCode);
    }

    @org.junit.Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT);
        String toString = fastDateFormat.toString();
        assertNotNull(toString);
        System.out.println("ToString: " + toString);
    }
}