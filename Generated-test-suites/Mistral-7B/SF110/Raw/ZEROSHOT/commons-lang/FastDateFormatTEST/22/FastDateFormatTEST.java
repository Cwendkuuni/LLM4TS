package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.lang3.time.FastDateFormat;

import org.junit.Test;

/**
 * FastDateFormatTest.java - JUnit 4 test file for FastDateFormat class.
 */

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat format = FastDateFormat.getInstance();
        assertNotNull(format);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a");
        assertNotNull(format);
    }

    @Test
    public void testGetInstanceWithPatternTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a", timeZone);
        assertNotNull(format);
    }

    @Test
    public void testGetInstanceWithPatternLocale() {
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a", locale);
        assertNotNull(format);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a", timeZone, locale);
        assertNotNull(format);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocaleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, locale);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, timeZone);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocaleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        Locale locale = Locale.US;
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, timeZone, locale);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, locale);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, timeZone);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocaleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        Locale locale = Locale.US;
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, timeZone, locale);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testFormat() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Calendar calendar = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 30, 15);
        String formattedDate = dateFormat.format(calendar);
        String expected = "12-Jan-22";
        assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormatWithCustomPattern() {
        FastDateFormat dateFormat = new FastDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.US, TimeZone.getTimeZone("UTC"));
        Calendar calendar = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 30, 15);
        String formattedDate = dateFormat.format(calendar);
        String expected = "12-Jan-22 12:30:15 PM";
        assertEquals(expected, formattedDate);
    }

    @Test
    public void testParse() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String dateString = "12-Jan-22";
        Date parsedDate = dateFormat.parse(dateString);
        Calendar expectedCalendar = new GregorianCalendar(2022, Calendar.JANUARY, 12);
        assertEquals(expectedCalendar.getTime(), parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String invalidDateString = "12-Jan-2233";
        dateFormat.parse(invalidDateString);
    }

    @Test
    public void testEquals() {
        FastDateFormat format1 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        FastDateFormat format2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertTrue(format1.equals(format2));
    }

    @Test
    public void testNotEqualsDifferentFormat() {
        FastDateFormat format1 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        FastDateFormat format2 = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
        assertFalse(format1.equals(format2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat format1 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        FastDateFormat format2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertEquals(format1.hashCode(), format2.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String expected = "FastDateFormat[Medium, null, null]";
        String actual = format.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String expected = "dd-MM-yy";
        String actual = format.getPattern();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        TimeZone expected = TimeZone.getDefault();
        TimeZone actual = format.getTimeZone();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocale() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Locale expected = Locale.getDefault();
        Locale actual = format.getLocale();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        int expected = 10;
        int actual = format.getMaxLengthEstimate();
        assertEquals(expected, actual);
    }
}