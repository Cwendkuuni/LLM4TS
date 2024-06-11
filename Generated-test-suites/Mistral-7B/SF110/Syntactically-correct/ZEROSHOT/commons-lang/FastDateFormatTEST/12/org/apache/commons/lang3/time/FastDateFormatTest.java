package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

public class FastDateFormatTest {

    @org.junit.Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a");
        assertNotNull(dateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a", timeZone);
        assertNotNull(dateFormat);
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @org.junit.Test
    public void testGetInstanceWithLocale() {
        Locale locale = Locale.CHINESE;
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a", locale);
        assertNotNull(dateFormat);
        assertEquals(locale, dateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.CHINESE;
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a", timeZone, locale);
        assertNotNull(dateFormat);
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(dateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.CHINESE;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(dateFormat);
        assertEquals(locale, dateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(dateFormat);
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @org.junit.Test
    public void testGetDateInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.CHINESE;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(dateFormat);
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetTimeInstance() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(timeFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.CHINESE;
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(timeFormat);
        assertEquals(locale, timeFormat.getLocale());
    }

    @org.junit.Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(timeFormat);
        assertEquals(timeZone, timeFormat.getTimeZone());
    }

    @org.junit.Test
    public void testGetTimeInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.CHINESE;
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(timeFormat);
        assertEquals(timeZone, timeFormat.getTimeZone());
        assertEquals(locale, timeFormat.getLocale());
    }

    @org.junit.Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertNotNull(dateTimeFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.CHINESE;
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, locale);
        assertNotNull(dateTimeFormat);
        assertEquals(locale, dateTimeFormat.getLocale());
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone);
        assertNotNull(dateTimeFormat);
        assertEquals(timeZone, dateTimeFormat.getTimeZone());
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.CHINESE;
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(dateTimeFormat);
        assertEquals(timeZone, dateTimeFormat.getTimeZone());
        assertEquals(locale, dateTimeFormat.getLocale());
    }

    @org.junit.Test
    public void testFormat() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertNotNull(formattedDate);
        System.out.println(formattedDate);
    }

    @org.junit.Test
    public void testFormatWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance("dd-MM-yyyy");
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertNotNull(formattedDate);
        System.out.println(formattedDate);
    }

    @org.junit.Test
    public void testFormatWithStringBuffer() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Date date = new Date();
        StringBuffer stringBuffer = new StringBuffer();
        dateFormat.format(date, stringBuffer);
        String formattedDate = stringBuffer.toString();
        assertNotNull(formattedDate);
        System.out.println(formattedDate);
    }

    @org.junit.Test
    public void testFormatWithFieldPosition() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Date date = new Date();
        StringBuffer stringBuffer = new StringBuffer();
        dateFormat.format(date, stringBuffer, new FieldPosition(0, "Day"));
        String formattedDate = stringBuffer.toString();
        assertNotNull(formattedDate);
        System.out.println(formattedDate);
    }

    @org.junit.Test
    public void testParse() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String dateString = "25-03-2023";
        Date parsedDate = dateFormat.parse(dateString);
        assertNotNull(parsedDate);
        Date currentDate = new Date();
        long difference = parsedDate.getTime() - currentDate.getTime();
        long expectedDifference = 60 * 60 * 24 * 25 * 1000L * 30; // 30 days
        assertEquals(expectedDifference, difference);
    }

    @org.junit.Test
    public void testParseWithParsePosition() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String dateString = "25-03-2023";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = dateFormat.parse(dateString, parsePosition);
        assertNotNull(parsedDate);
        Date currentDate = new Date();
        long difference = parsedDate.getTime() - currentDate.getTime();
        long expectedDifference = 60 * 60 * 24 * 25 * 1000L * 30; // 30 days
        assertEquals(expectedDifference, difference);
        assertEquals(dateString.length(), parsePosition.getIndex());
    }

    @org.junit.Test
    public void testParseObject() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String dateString = "25-03-2023";
        Date parsedDate = (Date) dateFormat.parseObject(dateString);
        assertNotNull(parsedDate);
        Date currentDate = new Date();
        long difference = parsedDate.getTime() - currentDate.getTime();
        long expectedDifference = 60 * 60 * 24 * 25 * 1000L * 30; // 30 days
        assertEquals(expectedDifference, difference);
    }

    @org.junit.Test
    public void testGetPattern() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String pattern = dateFormat.getPattern();
        assertNotNull(pattern);
        System.out.println(pattern);
    }

    @org.junit.Test
    public void testGetTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        TimeZone timeZone = dateFormat.getTimeZone();
        assertNotNull(timeZone);
        System.out.println(timeZone.getID());
    }

    @org.junit.Test
    public void testGetLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Locale locale = dateFormat.getLocale();
        assertNotNull(locale);
        System.out.println(locale);
    }

    @org.junit.Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        int maxLengthEstimate = dateFormat.getMaxLengthEstimate();
        assertNotNull(maxLengthEstimate);
        System.out.println(maxLengthEstimate);
    }

    @org.junit.Test
    public void testEquals() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertTrue(dateFormat1.equals(dateFormat2));
    }

    @org.junit.Test
    public void testHashCode() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        int hashCode1 = dateFormat1.hashCode();
        int hashCode2 = dateFormat2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @org.junit.Test
    public void testToString() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String toString = dateFormat.toString();
        assertNotNull(toString);
        System.out.println(toString);
    }

    @org.junit.Test
    public void testApplyRules() {
        Locale locale = Locale.ENGLISH;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, locale);
        Calendar calendar = new GregorianCalendar(2023, 2, 25);
        StringBuffer stringBuffer = new StringBuffer();
        dateFormat.applyRules(calendar, stringBuffer);
        String formattedDate = stringBuffer.toString();
        assertNotNull(formattedDate);
        System.out.println(formattedDate);
    }
}