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
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPatternTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", timeZone);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPatternLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", null, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        Locale locale = Locale.FRENCH;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithLocaleTimeZone() {
        Locale locale = Locale.FRENCH;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithLocaleTimeZone() {
        Locale locale = Locale.FRENCH;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithLocaleTimeZone() {
        Locale locale = Locale.FRENCH;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testFormatLong() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        String expected = fastDateFormat.format(date);
        String actual = fastDateFormat.format(FastDateFormat.parse(expected));
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testFormatDate() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        Date date = new Date();
        String expected = fastDateFormat.format(date);
        String actual = fastDateFormat.format(new Date(date.getTime()));
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testFormatCalendar() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String expected = fastDateFormat.format(date);
        String actual = fastDateFormat.format(calendar);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testParseLong() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        String dateString = fastDateFormat.format(new Date());
        Date parsed = fastDateFormat.parse(dateString);
        assertEquals(new Date().getTime(), parsed.getTime());
    }

    @org.junit.Test
    public void testParseDate() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        Date date = new Date();
        String dateString = fastDateFormat.format(date);
        Date parsed = fastDateFormat.parse(dateString);
        assertEquals(date.getTime(), parsed.getTime());
    }

    @org.junit.Test
    public void testParseCalendar() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        Calendar calendar = Calendar.getInstance();
        String dateString = fastDateFormat.format(calendar.getTime());
        Calendar parsed = FastDateFormat.parse(dateString, calendar);
        assertEquals(calendar.getTimeInMillis(), parsed.getTimeInMillis());
    }

    @org.junit.Test
    public void testGetPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        String pattern = fastDateFormat.getPattern();
        assertEquals("dd-MM-yyyy HH:mm:ss z", pattern);
    }

    @org.junit.Test
    public void testGetTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        TimeZone timeZone = fastDateFormat.getTimeZone();
        assertEquals("UTC", timeZone.getID());
    }

    @org.junit.Test
    public void testGetLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        Locale locale = fastDateFormat.getLocale();
        assertEquals(Locale.US, locale);
    }

    @org.junit.Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
        assertEquals(32, maxLengthEstimate);
    }

    @org.junit.Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        assertTrue(fastDateFormat1.equals(fastDateFormat2));
    }

    @org.junit.Test
    public void testHashCode() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        int hashCode = fastDateFormat.hashCode();
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        int hashCode2 = fastDateFormat2.hashCode();
        assertEquals(hashCode, hashCode2);
    }

    @org.junit.Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        String toString = fastDateFormat.toString();
        String expected = "FastDateFormat[dd-MM-yyyy HH:mm:ss z, java.util.Locale[US], sun.util.calendar.ZoneInfo[id=UTC, offset=0, dbcs=false, area=null, transitions=0, startMonth=0, startDayOfWeek=1, startDayOfYear=1, transition1=null, transition2=null, transition3=null, transition4=null, transition5=null, transition6=null, transition7=null, transitionsCount=0]]";
        assertEquals(expected, toString);
    }

    @org.junit.Test
    public void testApplyRules() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        Calendar calendar = Calendar.getInstance();
        String expected = "2023-03-22";
        StringBuffer buf = new StringBuffer();
        fastDateFormat.applyRules(calendar, buf);
        String actual = buf.toString();
        assertEquals(expected, actual);
    }

}