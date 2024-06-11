package org.apache.commons.lang3.time;

import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        assertNotNull(dateFormat);
        assertEquals("dd-MM-yyyy HH:mm:ss z", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss", timeZone);
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yyyy HH:mm:ss", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetInstanceWithLocale() {
        Locale locale = Locale.FRANCE;
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss", null, locale);
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yyyy HH:mm:ss", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRANCE;
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss", timeZone, locale);
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yyyy HH:mm:ss", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yy", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.FRANCE;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, locale);
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yy", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, timeZone);
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yy", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetDateInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRANCE;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance("dd/MM/yy", timeZone, locale);
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yy", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(dateFormat);
        assertEquals("HH:mm:ss z", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.FRANCE;
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, locale);
        assertNotNull(dateFormat);
        assertEquals("HH:mm:ss z", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone);
        assertNotNull(dateFormat);
        assertEquals("HH:mm:ss z", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetTimeInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRANCE;
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance("HH:mm:ss z", timeZone, locale);
        assertNotNull(dateFormat);
        assertEquals("HH:mm:ss z", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT);
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yy HH:mm:ss z", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.FRANCE;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, locale);
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yy HH:mm:ss z", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, timeZone);
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yy HH:mm:ss z", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetDateTimeInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRANCE;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance("dd/MM/yy HH:mm:ss z", timeZone, locale);
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yy HH:mm:ss z", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testFormat() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT);
        assertEquals("12/31/21 11:59:59 PM", dateFormat.format(calendar));
    }

    @Test
    public void testFormatWithPattern() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance("dd-MMM-yy hh:mm a z", null, Locale.ENGLISH);
        assertEquals("12-Dec-21 11:59 AM UTC", dateFormat.format(calendar));
    }

    @Test
    public void testFormatWithTimeZone() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, timeZone);
        assertEquals("12/31/21 06:29:57 IST", dateFormat.format(calendar));
    }

    @Test
    public void testFormatWithLocale() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        Locale locale = Locale.CHINA;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, locale);
        assertEquals("21年12月31日 晚上11时59分57秒", dateFormat.format(calendar));
    }

    @Test
    public void testFormatWithPatternTimeZoneLocale() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance("dd-MM-yy hh:mm a z", timeZone, locale);
        assertEquals("12-12-21 10:29 AM PST", dateFormat.format(calendar));
    }

    @Test
    public void testParse() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss z");
        Date date = simpleDateFormat.parse("12-31-21 11:59:59 PM");

        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance("dd-MM-yy HH:mm:ss z", null, Locale.ENGLISH);
        assertEquals(date, dateFormat.parse("12-31-21 11:59:59 PM"));
    }

    @Test
    public void testParseWithTimeZone() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss z");
        Date date = simpleDateFormat.parse("12/31/21 11:59:59 PM UTC");

        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance("dd/MM/yy HH:mm:ss z", timeZone, null);
        assertEquals(date, dateFormat.parse("12/31/21 11:59:59 PM UTC"));
    }

    @Test
    public void testParseWithLocale() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy hh:mm a z");
        Date date = simpleDateFormat.parse("12-Dec-21 11:59 AM UTC");

        Locale locale = Locale.ENGLISH;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance("dd-MM-yy hh:mm a z", null, locale);
        assertEquals(date, dateFormat.parse("12-Dec-21 11:59 AM UTC"));
    }

    @Test
    public void testParseWithPatternTimeZoneLocale() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy hh:mm a z");
        Date date = simpleDateFormat.parse("12-Dec-21 11:59 AM UTC");

        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.ENGLISH;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance("dd-MM-yy hh:mm a z", timeZone, locale);
        assertEquals(date, dateFormat.parse("12-Dec-21 11:59 AM UTC"));
    }

    @Test
    public void testEquals() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateTimeInstance("dd/MM/yy hh:mm:ss z", null, Locale.ENGLISH);
        FastDateFormat dateFormat2 = FastDateFormat.getDateTimeInstance("dd/MM/yy hh:mm:ss z", null, Locale.ENGLISH);

        assertTrue(dateFormat1.equals(dateFormat2));

        FastDateFormat dateFormat3 = FastDateFormat.getDateTimeInstance("dd/MM/yy HH:mm:ss z", null, Locale.ENGLISH);
        assertFalse(dateFormat1.equals(dateFormat3));
    }

    @Test
    public void testHashCode() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateTimeInstance("dd/MM/yy hh:mm:ss z", null, Locale.ENGLISH);
        FastDateFormat dateFormat2 = FastDateFormat.getDateTimeInstance("dd/MM/yy hh:mm:ss z", null, Locale.ENGLISH);

        int hashCode1 = dateFormat1.hashCode();
        int hashCode2 = dateFormat2.hashCode();

        assertTrue(hashCode1 == hashCode2);

        FastDateFormat dateFormat3 = FastDateFormat.getDateTimeInstance("dd/MM/yy HH:mm:ss z", null, Locale.ENGLISH);
        assertFalse(hashCode1 == hashCode3);
    }
}