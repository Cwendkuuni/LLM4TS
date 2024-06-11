package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatCache;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertTrue(dateFormat instanceof FastDateFormat);
        assertNull(dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
        assertTrue(dateFormat instanceof FastDateFormat);
        assertNull(dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());

        dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertTrue(dateFormat instanceof FastDateFormat);
        assertNull(dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());

        dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
        assertTrue(dateFormat instanceof FastDateFormat);
        assertNull(dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());

        dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertTrue(dateFormat instanceof FastDateFormat);
        assertNull(dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertTrue(timeFormat instanceof FastDateFormat);
        assertNull(timeFormat.getPattern());
        assertNull(timeFormat.getTimeZone());
        assertNull(timeFormat.getLocale());

        timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertTrue(timeFormat instanceof FastDateFormat);
        assertNull(timeFormat.getPattern());
        assertNull(timeFormat.getTimeZone());
        assertNull(timeFormat.getLocale());

        timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
        assertTrue(timeFormat instanceof FastDateFormat);
        assertNull(timeFormat.getPattern());
        assertNull(timeFormat.getTimeZone());
        assertNull(timeFormat.getLocale());

        timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        assertTrue(timeFormat instanceof FastDateFormat);
        assertNull(timeFormat.getPattern());
        assertNull(timeFormat.getTimeZone());
        assertNull(timeFormat.getLocale());
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT);
        assertTrue(dateTimeFormat instanceof FastDateFormat);
        assertNull(dateTimeFormat.getPattern());
        assertNull(dateTimeFormat.getTimeZone());
        assertNull(dateTimeFormat.getLocale());

        dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertTrue(dateTimeFormat instanceof FastDateFormat);
        assertNull(dateTimeFormat.getPattern());
        assertNull(dateTimeFormat.getTimeZone());
        assertNull(dateTimeFormat.getLocale());

        dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
        assertTrue(dateTimeFormat instanceof FastDateFormat);
        assertNull(dateTimeFormat.getPattern());
        assertNull(dateTimeFormat.getTimeZone());
        assertNull(dateTimeFormat.getLocale());

        dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertTrue(dateTimeFormat instanceof FastDateFormat);
        assertNull(dateTimeFormat.getPattern());
        assertNull(dateTimeFormat.getTimeZone());
        assertNull(dateTimeFormat.getLocale());
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        assertTrue(dateFormat instanceof FastDateFormat);
        assertEquals("yyyy-MM-dd", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());

        dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertTrue(dateFormat instanceof FastDateFormat);
        assertEquals("yyyy-MM-dd HH:mm:ss", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetInstanceWithPatternTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", new SimpleDateFormat("UTC").getTimeZone());
        assertTrue(dateFormat instanceof FastDateFormat);
        assertEquals("yyyy-MM-dd HH:mm:ss", dateFormat.getPattern());
        assertEquals("UTC", dateFormat.getTimeZone().getID());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetInstanceWithPatternLocale() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.FRENCH);
        assertTrue(dateFormat instanceof FastDateFormat);
        assertEquals("yyyy-MM-dd HH:mm:ss", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertEquals(Locale.FRENCH, dateFormat.getLocale());
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.FRENCH, new SimpleDateFormat("UTC").getTimeZone());
        assertTrue(dateFormat instanceof FastDateFormat);
        assertEquals("yyyy-MM-dd HH:mm:ss", dateFormat.getPattern());
        assertEquals("UTC", dateFormat.getTimeZone().getID());
        assertEquals(Locale.FRENCH, dateFormat.getLocale());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithInvalidPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("InvalidPattern");
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceWithNullPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance(null);
    }

    @Test
    public void testFormat() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
        Date date = new Date();

        String formattedDate = dateFormat.format(date);
        assertNotNull(formattedDate);

        FastDateFormat dateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        String formattedDate2 = dateFormat2.format(date);
        assertNotNull(formattedDate2);

        assertEquals(formattedDate, dateFormat2.format(date));
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
        String dateString = "2022-01-01 12:34:56";
        Date parsedDate = dateFormat.parse(dateString);
        assertNotNull(parsedDate);

        FastDateFormat dateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        String dateString2 = "Jan 1, 2022 12:34:56 PM";
        Date parsedDate2 = dateFormat2.parse(dateString2);
        assertNotNull(parsedDate2);

        assertEquals(parsedDate, dateFormat2.parse(dateString));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
        String dateString = "InvalidDate";
        dateFormat.parse(dateString);
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullInput() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
        dateFormat.parse(null);
    }

    @Test
    public void testEquals() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
        FastDateFormat dateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
        FastDateFormat dateFormat3 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);

        assertTrue(dateFormat1.equals(dateFormat1));
        assertTrue(dateFormat1.equals(dateFormat2));
        assertFalse(dateFormat1.equals(dateFormat3));
    }

    @Test
    public void testHashCode() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
        FastDateFormat dateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
        FastDateFormat dateFormat3 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);

        int hashCode1 = dateFormat1.hashCode();
        int hashCode2 = dateFormat2.hashCode();
        int hashCode3 = dateFormat3.hashCode();

        assertTrue(hashCode1 == hashCode2);
        assertFalse(hashCode1 == hashCode3);
    }

    @Test
    public void testToString() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
        String toString = dateFormat.toString();
        assertNotNull(toString);
    }
}