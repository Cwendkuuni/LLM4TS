package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatCache;
import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithTimeZone() {
        TimeZone timeZone = ZoneId.of("UTC").toZone();
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", timeZone);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", null, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithAllParameters() {
        TimeZone timeZone = ZoneId.of("UTC").toZone();
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = ZoneId.of("UTC").toZone();
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithAllParameters() {
        TimeZone timeZone = ZoneId.of("UTC").toZone();
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = ZoneId.of("UTC").toZone();
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithAllParameters() {
        TimeZone timeZone = ZoneId.of("UTC").toZone();
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, null, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = ZoneId.of("UTC").toZone();
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, null, timeZone);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithAllParameters() {
        TimeZone timeZone = ZoneId.of("UTC").toZone();
        Locale locale = Locale.US;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testFormatDate() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        Date date = new Date();
        String formattedDate = fastDateFormat.format(date);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatDateWithCustomFormat() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String formattedDate = fastDateFormat.format(date);
        assertNotNull(formattedDate);
        assertEquals("2023-03-22 15:33:23", formattedDate);
    }

    @Test
    public void testParseDate() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        String dateString = "23-03-2023 15:33:23 EDT";
        Date parsedDate = fastDateFormat.parse(dateString);
        assertNotNull(parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseDateInvalidFormat() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        String dateString = "23-03-2023 15:33:23 EDT2";
        fastDateFormat.parse(dateString);
    }

    @Test(expected = ParseException.class)
    public void testParseDateMissingComponents() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        String dateString = "23-03-2023 HH:mm:ss";
        fastDateFormat.parse(dateString);
    }

    @Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        assertTrue(fastDateFormat1.equals(fastDateFormat2));
    }

    @Test
    public void testNotEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("dd-MM-yy HH:mm:ss z");
        assertFalse(fastDateFormat1.equals(fastDateFormat2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        String toString = fastDateFormat.toString();
        assertTrue(toString.contains("FastDateFormat["));
    }
}