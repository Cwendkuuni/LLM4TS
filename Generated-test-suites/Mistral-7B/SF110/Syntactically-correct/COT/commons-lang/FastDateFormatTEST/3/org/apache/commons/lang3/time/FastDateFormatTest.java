package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatCache;
import org.apache.commons.lang3.time.FastDatePrinter;
import org.apache.commons.lang3.time.FastDateParser;

public class FastDateFormatTest {

    @org.junit.Test
    public void testGetInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a");
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a", timeZone);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithLocale() {
        Locale locale = Locale.ITALIAN;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a", locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        Locale locale = Locale.ITALIAN;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a", timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.ITALIAN;
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        Locale locale = Locale.ITALIAN;
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.ITALIAN;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        Locale locale = Locale.ITALIAN;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.ITALIAN;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        Locale locale = Locale.ITALIAN;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testFormatLong() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        long millis = System.currentTimeMillis();
        String formatted = fastDateFormat.format(millis);
        assertNotNull(formatted);
        assertFalse(formatted.isEmpty());
    }

    @org.junit.Test
    public void testFormatDate() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Date date = new Date();
        String formatted = fastDateFormat.format(date);
        assertNotNull(formatted);
        assertFalse(formatted.isEmpty());
    }

    @org.junit.Test
    public void testFormatCalendar() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Calendar calendar = new GregorianCalendar();
        String formatted = fastDateFormat.format(calendar);
        assertNotNull(formatted);
        assertFalse(formatted.isEmpty());
    }

    @org.junit.Test
    public void testFormatLongBuffer() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        StringBuffer stringBuffer = new StringBuffer();
        long millis = System.currentTimeMillis();
        fastDateFormat.format(millis, stringBuffer);
        assertNotNull(stringBuffer);
        assertFalse(stringBuffer.isEmpty());
    }

    @org.junit.Test
    public void testFormatDateBuffer() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        StringBuffer stringBuffer = new StringBuffer();
        Date date = new Date();
        fastDateFormat.format(date, stringBuffer);
        assertNotNull(stringBuffer);
        assertFalse(stringBuffer.isEmpty());
    }

    @org.junit.Test
    public void testFormatCalendarBuffer() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        StringBuffer stringBuffer = new StringBuffer();
        Calendar calendar = new GregorianCalendar();
        fastDateFormat.format(calendar, stringBuffer);
        assertNotNull(stringBuffer);
        assertFalse(stringBuffer.isEmpty());
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseInvalidDate() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String invalidDate = "13:60:00";
        fastDateFormat.parse(invalidDate);
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseInvalidDateWithParsePosition() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        ParsePosition parsePosition = new ParsePosition(0);
        String invalidDate = "13:60:00";
        fastDateFormat.parse(invalidDate, parsePosition);
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseInvalidObject() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String invalidObject = "Invalid Object";
        fastDateFormat.parseObject(invalidObject, new ParsePosition(0));
    }

    @org.junit.Test
    public void testGetPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String pattern = fastDateFormat.getPattern();
        assertNotNull(pattern);
        assertFalse(pattern.isEmpty());
    }

    @org.junit.Test
    public void testGetTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        TimeZone timeZone = fastDateFormat.getTimeZone();
        assertNotNull(timeZone);
        assertFalse(timeZone.inDaylightTime(new Date()) ^ Calendar.getInstance().getTimeInMillis() > System.currentTimeMillis());
    }

    @org.junit.Test
    public void testGetLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Locale locale = fastDateFormat.getLocale();
        assertEquals(Locale.getDefault(), locale);
    }

    @org.junit.Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
        assertNotNull(maxLengthEstimate);
        assertTrue(maxLengthEstimate > 0);
    }

    @org.junit.Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance();
        assertTrue(fastDateFormat1.equals(fastDateFormat2));
    }

    @org.junit.Test
    public void testEqualsDifferentInstance() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
        FastDateFormat fastDateFormat2 = new FastDateFormat("dd/MM/yyyy hh:mm:ss a");
        assertFalse(fastDateFormat1.equals(fastDateFormat2));
    }

    @org.junit.Test
    public void testHashCode() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        int hashCode = fastDateFormat.hashCode();
        FastDateFormat fastDateFormat2 = new FastDateFormat("dd/MM/yyyy hh:mm:ss a");
        int hashCode2 = fastDateFormat2.hashCode();
        assertNotSame(hashCode, hashCode2);
    }

    @org.junit.Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String toString = fastDateFormat.toString();
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
    }

    @org.junit.Test
    public void testApplyRulesWithCalendar() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Calendar calendar = new GregorianCalendar(2019, 1, 1);
        String formatted = fastDateFormat.applyRules(calendar, new StringBuffer());
        assertNotNull(formatted);
        assertFalse(formatted.isEmpty());
    }
}