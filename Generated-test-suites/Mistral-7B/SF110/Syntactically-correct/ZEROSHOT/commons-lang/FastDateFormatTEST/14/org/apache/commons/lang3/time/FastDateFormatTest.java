package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a z");
        assertNotNull(fastDateFormat);
        assertEquals("dd/MM/yyyy hh:mm:ss a z", fastDateFormat.getPattern());
    }

    @org.junit.Test
    public void testGetInstanceWithPatternTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a z", timeZone);
        assertNotNull(fastDateFormat);
        assertEquals("dd/MM/yyyy hh:mm:ss a z", fastDateFormat.getPattern());
        assertEquals(timeZone, fastDateFormat.getTimeZone());
    }

    @org.junit.Test
    public void testGetInstanceWithPatternLocale() {
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a z", locale);
        assertNotNull(fastDateFormat);
        assertEquals("dd/MM/yyyy hh:mm:ss a z", fastDateFormat.getPattern());
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a z", timeZone, locale);
        assertNotNull(fastDateFormat);
        assertEquals("dd/MM/yyyy hh:mm:ss a z", fastDateFormat.getPattern());
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
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getDateStyle());
    }

    @org.junit.Test
    public void testGetDateInstanceWithStyleLocale() {
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(fastDateFormat);
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getDateStyle());
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetDateInstanceWithStyleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(fastDateFormat);
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getDateStyle());
        assertEquals(timeZone, fastDateFormat.getTimeZone());
    }

    @org.junit.Test
    public void testGetDateInstanceWithStyleTimeZoneLocale() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(fastDateFormat);
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getDateStyle());
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
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getTimeStyle());
    }

    @org.junit.Test
    public void testGetTimeInstanceWithStyleLocale() {
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(fastDateFormat);
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getTimeStyle());
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetTimeInstanceWithStyleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(fastDateFormat);
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getTimeStyle());
        assertEquals(timeZone, fastDateFormat.getTimeZone());
    }

    @org.junit.Test
    public void testGetTimeInstanceWithStyleTimeZoneLocale() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(fastDateFormat);
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getTimeStyle());
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
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getDateStyle());
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getTimeStyle());
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleLocale() {
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, locale);
        assertNotNull(fastDateFormat);
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getDateStyle());
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getTimeStyle());
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone);
        assertNotNull(fastDateFormat);
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getDateStyle());
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getTimeStyle());
        assertEquals(timeZone, fastDateFormat.getTimeZone());
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleTimeZoneLocale() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(fastDateFormat);
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getDateStyle());
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getTimeStyle());
        assertEquals(timeZone, fastDateFormat.getTimeZone());
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testFormat() throws ParseException {
        Calendar calendar = new GregorianCalendar(2023, Calendar.FEBRUARY, 28);
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance();
        Date date = calendar.getTime();
        String formattedDate = fastDateFormat.format(date);
        System.out.println(formattedDate);
        assertEquals("Fri Feb 24 13:14:56 CET 2023", formattedDate);
    }

    @org.junit.Test
    public void testFormatWithPattern() throws ParseException {
        Calendar calendar = new GregorianCalendar(2023, Calendar.FEBRUARY, 28);
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yy hh:mm:ss a z");
        Date date = calendar.getTime();
        String formattedDate = fastDateFormat.format(date);
        System.out.println(formattedDate);
        assertEquals("28/02/23 13:14:56 CET", formattedDate);
    }

    @org.junit.Test
    public void testParse() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String dateString = "Fri Feb 24 13:14:56 CET 2023";
        Date parsedDate = fastDateFormat.parse(dateString);
        assertEquals(new Date(1677253456000L), parsedDate);
    }

    @org.junit.Test
    public void testParseWithPattern() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yy hh:mm:ss a z");
        String dateString = "28/02/23 13:14:56 CET";
        Date parsedDate = fastDateFormat.parse(dateString);
        assertEquals(new Date(1677253456000L), parsedDate);
    }

    @org.junit.Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance();
        assertTrue(fastDateFormat1.equals(fastDateFormat2));
    }

    @org.junit.Test
    public void testHashCode() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance();
        assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
    }

    @org.junit.Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String toString = fastDateFormat.toString();
        System.out.println(toString);
        assertTrue(toString.contains("FastDateFormat[dd/MM/yy hh:mm:ss a z, en_US, UTC]"));
    }
}