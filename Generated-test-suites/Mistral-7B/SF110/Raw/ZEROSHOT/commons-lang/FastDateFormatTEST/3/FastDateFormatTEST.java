package org.apache.commons.lang3.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS zzz");
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS zzz", timeZone);
        assertNotNull(dateFormat);
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testGetInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS zzz", null, locale);
        assertNotNull(dateFormat);
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS zzz", timeZone, locale);
        assertNotNull(dateFormat);
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, locale);
        assertNotNull(dateFormat);
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone);
        assertNotNull(dateFormat);
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testGetDateInstanceWithPatternTimeZoneLocale() {
        Locale locale = Locale.FRENCH;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance("d MMM y 'at' H:mm:ss zzz", timeZone, locale);
        assertNotNull(dateFormat);
        assertEquals(locale, dateFormat.getLocale());
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, locale);
        assertNotNull(dateFormat);
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone);
        assertNotNull(dateFormat);
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testGetTimeInstanceWithPatternTimeZoneLocale() {
        Locale locale = Locale.FRENCH;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance("HH:mm:ss zzz", timeZone, locale);
        assertNotNull(dateFormat);
        assertEquals(locale, dateFormat.getLocale());
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, locale);
        assertNotNull(dateFormat);
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, timeZone);
        assertNotNull(dateFormat);
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testGetDateTimeInstanceWithPatternTimeZoneLocale() {
        Locale locale = Locale.FRENCH;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance("d MMM y H:mm:ss zzz", timeZone, locale);
        assertNotNull(dateFormat);
        assertEquals(locale, dateFormat.getLocale());
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testFormatDate() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertEquals("Invalid date format", dateFormat.getPattern(), formattedDate.substring(0, dateFormat.getPattern().length()));
    }

    @Test
    public void testFormatLong() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        long millis = System.currentTimeMillis();
        String formattedDate = dateFormat.format(millis);
        assertEquals("Invalid date format", dateFormat.getPattern(), formattedDate.substring(0, dateFormat.getPattern().length()));
    }

    @Test
    public void testFormatCalendar() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Calendar calendar = new GregorianCalendar();
        String formattedDate = dateFormat.format(calendar);
        assertEquals("Invalid date format", dateFormat.getPattern(), formattedDate.substring(0, dateFormat.getPattern().length()));
    }

    @Test
    public void testParseString() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String dateString = "25-12-2021";
        Date parsedDate = dateFormat.parse(dateString);
        assertEquals("Invalid parsed date", new Date(System.currentTimeMillis() - 3153600000L), parsedDate);
    }

    @Test
    public void testParseStringParsePosition() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String dateString = "25-12-2021";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = dateFormat.parse(dateString, pos);
        assertEquals("Invalid parsed date", new Date(System.currentTimeMillis() - 3153600000L), parsedDate);
        assertEquals("Invalid parse position", dateString.length() + 1, pos.getIndex());
    }

    @Test
    public void testParseObjectStringParsePosition() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String dateString = "25-12-2021";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = (Date)dateFormat.parseObject(dateString, pos);
        assertEquals("Invalid parsed date", new Date(System.currentTimeMillis() - 3153600000L), parsedDate);
        assertEquals("Invalid parse position", dateString.length() + 1, pos.getIndex());
    }

    @Test
    public void testGetPattern() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String pattern = dateFormat.getPattern();
        assertTrue("Invalid pattern", pattern.matches("(d{1,2})-(M{1,3})-(y{2,4})"));
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        TimeZone timeZone = dateFormat.getTimeZone();
        assertNotNull(timeZone);
    }

    @Test
    public void testGetLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Locale locale = dateFormat.getLocale();
        assertNotNull(locale);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        int maxLength = dateFormat.getMaxLengthEstimate();
        assertTrue("Invalid max length estimate", maxLength >= 10);
    }

    @Test
    public void testEqualsObject() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertTrue(dateFormat1.equals(dateFormat2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertEquals(dateFormat1.hashCode(), dateFormat2.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String toString = dateFormat.toString();
        assertTrue("Invalid toString", toString.contains("FastDateFormat["));
    }
}