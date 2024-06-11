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
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", timeZone);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", null, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
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
    public void testGetDateInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance("dd-MM-yyyy", timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance("hh:mm a", timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance("dd-MM-yyyy hh:mm:ss a z", timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testFormatWithObject() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        Date date = new Date();
        String expected = fastDateFormat.format(date);
        String formatResult = fastDateFormat.format(date.getTime());
        assertEquals(expected, formatResult);
    }

    @org.junit.Test
    public void testFormatWithStringBuffer() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        StringBuffer stringBuffer = new StringBuffer(10);
        fastDateFormat.format(new Date(), stringBuffer, new FieldPosition(0));
        String expected = fastDateFormat.format(new Date());
        String formatResult = stringBuffer.toString();
        assertEquals(expected, formatResult);
    }

    @org.junit.Test
    public void testFormatWithLong() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        long time = System.currentTimeMillis();
        String expected = fastDateFormat.format(time);
        String formatResult = fastDateFormat.format(time);
        assertEquals(expected, formatResult);
    }

    @org.junit.Test
    public void testFormatWithCalendar() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        Calendar calendar = new GregorianCalendar(2022, 11, 12);
        String expected = fastDateFormat.format(calendar.getTime());
        String formatResult = fastDateFormat.format(calendar);
        assertEquals(expected, formatResult);
    }

    @org.junit.Test
    public void testParse() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String dateString = "12-11-2022";
        Date date = fastDateFormat.parse(dateString);
        assertNotNull(date);
    }

    @org.junit.Test
    public void testParseObject() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String dateString = "12-11-2022";
        Date date = (Date) fastDateFormat.parseObject(dateString);
        assertNotNull(date);
    }

    @org.junit.Test
    public void testParseWithParsePosition() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String dateString = "12-11-2022";
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = fastDateFormat.parse(dateString, parsePosition);
        assertNotNull(date);
        assertEquals(dateString.length(), parsePosition.getIndex());
    }

    @org.junit.Test
    public void testGetPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        String expected = fastDateFormat.getPattern();
        String pattern = fastDateFormat.toString();
        assertEquals(expected, pattern.substring(0, pattern.lastIndexOf(" ")));
    }

    @org.junit.Test
    public void testGetTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        TimeZone expected = TimeZone.getDefault();
        TimeZone timeZone = fastDateFormat.getTimeZone();
        assertEquals(expected.getID(), timeZone.getID());
    }

    @org.junit.Test
    public void testGetLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        Locale expected = Locale.getDefault();
        Locale locale = fastDateFormat.getLocale();
        assertEquals(expected.getLanguage(), locale.getLanguage());
        assertEquals(expected.getCountry(), locale.getCountry());
    }

    @org.junit.Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        int expected = fastDateFormat.getMaxLengthEstimate();
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
        assertEquals(expected, maxLengthEstimate);
    }

    @org.junit.Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertTrue(fastDateFormat1.equals(fastDateFormat2));
    }

    @org.junit.Test
    public void testHashCode() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        int expectedHashCode = fastDateFormat.hashCode();
        int hashCode = fastDateFormat.hashCode();
        assertEquals(expectedHashCode, hashCode);
    }

    @org.junit.Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        String expected = "FastDateFormat[dd-MM-yyyy hh:mm:ss a z]";
        String toString = fastDateFormat.toString();
        assertEquals(expected, toString);
    }

    @org.junit.Test
    public void testApplyRulesWithCalendarAndStringBuffer() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        Calendar calendar = new GregorianCalendar(2022, 11, 12);
        String expected = fastDateFormat.format(calendar.getTime());
        StringBuffer stringBuffer = new StringBuffer(expected.length());
        fastDateFormat.applyRules(calendar, stringBuffer);
        String formatResult = stringBuffer.toString();
        assertEquals(expected, formatResult);
    }
}