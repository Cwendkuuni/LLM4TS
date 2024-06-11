package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatCache;

public class FastDateFormatTest {

    @org.junit.BeforeClass
    public static void setup() {
        // Initialize the test with a known state
        FastDateFormat.setDefaultCalendar(Calendar.getInstance());
    }

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
        TimeZone tz = TimeZone.getTimeZone("Europe/London");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", tz);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPatternLocale() {
        Locale locale = new Locale("fr", "FR");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        TimeZone tz = TimeZone.getTimeZone("Europe/London");
        Locale locale = new Locale("fr", "FR");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", tz, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = new Locale("fr", "FR");
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("Europe/London");
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, tz);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithLocaleTimeZone() {
        Locale locale = new Locale("fr", "FR");
        TimeZone tz = TimeZone.getTimeZone("Europe/London");
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, tz, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = new Locale("fr", "FR");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("Europe/London");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, tz);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithLocaleTimeZone() {
        Locale locale = new Locale("fr", "FR");
        TimeZone tz = TimeZone.getTimeZone("Europe/London");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, tz, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = new Locale("fr", "FR");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("Europe/London");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, tz);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithLocaleTimeZone() {
        Locale locale = new Locale("fr", "FR");
        TimeZone tz = TimeZone.getTimeZone("Europe/London");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, tz, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testFormat() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Calendar calendar = Calendar.getInstance();
        String formattedDate = fastDateFormat.format(calendar.getTime());
        assertEquals("expected date format to be 'MMM dd, yyyy'", formattedDate.substring(0, 11), "Jan 01, 2023");
    }

    @org.junit.Test
    public void testFormatWithPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        Calendar calendar = Calendar.getInstance();
        String formattedDate = fastDateFormat.format(calendar.getTime());
        assertEquals("expected date format to be 'yyyy-MM-dd HH:mm:ss.SSS'", formattedDate, "2023-01-01 00:00:00.000");
    }

    @org.junit.Test
    public void testParse() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String dateString = "Jan 01, 2023";
        Date parsedDate = fastDateFormat.parse(dateString);
        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.setTime(new SimpleDateFormat("MMM dd, yyyy").parse("Jan 01, 2023"));
        assertEquals("expected parsed date to be equal to expected date", expectedCalendar.getTime(), parsedDate);
    }

    @org.junit.Test
    public void testParseWithPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        String dateString = "2023-01-01 00:00:00.000";
        Date parsedDate = fastDateFormat.parse(dateString);
        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("2023-01-01 00:00:00.000"));
        assertEquals("expected parsed date to be equal to expected date", expectedCalendar.getTime(), parsedDate);
    }

    @org.junit.Test
    public void testParseWithInvalidPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        String dateString = "2023-13-01 00:00:00.000";
        try {
            fastDateFormat.parse(dateString);
            fail("expected ParseException to be thrown");
        } catch (ParseException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testGetPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
        assertEquals("expected pattern to be 'dd-MM-yyyy'", fastDateFormat.getPattern(), "dd-MM-yyyy");
    }

    @org.junit.Test
    public void testGetTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        TimeZone timeZone = fastDateFormat.getTimeZone();
        assertEquals("expected time zone to be default time zone", TimeZone.getDefault(), timeZone);
    }

    @org.junit.Test
    public void testGetLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Locale locale = fastDateFormat.getLocale();
        assertEquals("expected locale to be default locale", Locale.getDefault(), locale);
    }

    @org.junit.Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
        assertEquals("expected max length estimate to be 23", maxLengthEstimate, 23);
    }

    @org.junit.Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy");
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy");
        assertTrue("expected fastDateFormat1 to be equal to fastDateFormat2", fastDateFormat1.equals(fastDateFormat2));
    }

    @org.junit.Test
    public void testHashCode() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
        int hashCode = fastDateFormat.hashCode();
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy");
        int hashCode2 = fastDateFormat2.hashCode();
        assertEquals("expected hashCode to be equal to hashCode2", hashCode, hashCode2);
    }

    @org.junit.Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
        String expectedString = "FastDateFormat[dd-MM-yyyy, default locale, default time zone]";
        String string = fastDateFormat.toString();
        assertEquals("expected toString to return expectedString", expectedString, string);
    }
}