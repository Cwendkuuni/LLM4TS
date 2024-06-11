package org.apache.commons.lang3.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
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
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS", timeZone);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS", null, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithAllParameters() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS", timeZone, locale);
        assertNotNull(dateFormat);
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
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithAllParameters() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, timeZone);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetTimeInstanceWithAllParameters() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, timeZone, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithAllParameters() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testFormatWithObject() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        String expected = "2022-12-31 23:59:59.999"; // Change to the current date and time
        String actual = dateFormat.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithLong() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        long millis = System.currentTimeMillis();
        String expected = "2022-12-31 23:59:59.999"; // Change to the current date and time
        String actual = dateFormat.format(millis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithDate() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        String expected = "2022-12-31 23:59:59.999"; // Change to the current date and time
        String actual = dateFormat.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithCalendar() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        Calendar calendar = Calendar.getInstance();
        String expected = "2022-12-31 23:59:59.999"; // Change to the current date and time
        String actual = dateFormat.format(calendar);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatStringBufferWithObject() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        StringBuffer buffer = new StringBuffer(10);
        String expected = "2022-12-31 23:59:59.999"; // Change to the current date and time
        dateFormat.format(date, buffer);
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testFormatStringBufferWithLong() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        long millis = System.currentTimeMillis();
        StringBuffer buffer = new StringBuffer(10);
        String expected = "2022-12-31 23:59:59.999"; // Change to the current date and time
        dateFormat.format(millis, buffer);
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testFormatStringBufferWithDate() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        StringBuffer buffer = new StringBuffer(10);
        String expected = "2022-12-31 23:59:59.999"; // Change to the current date and time
        dateFormat.format(date, buffer);
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testFormatStringBufferWithCalendar() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer(10);
        String expected = "2022-12-31 23:59:59.999"; // Change to the current date and time
        dateFormat.format(calendar, buffer);
        assertEquals(expected, buffer.toString());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateString() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        String invalidDateString = "xxxx-xx-xx xx:xx:xx.xxx"; // Invalid date string
        dateFormat.parse(invalidDateString);
    }

    @Test
    public void testParseValidDateString() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        String validDateString = "2022-12-31 23:59:59.999"; // Change to the current date and time
        Date expectedDate = new Date(); // Change to the current date and time
        Date actualDate = dateFormat.parse(validDateString);
        assertEquals(expectedDate, actualDate);
    }

    @Test(expected = ParsePosition.IndexOutOfBoundsException.class)
    public void testParseInvalidDateStringWithParsePosition() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        String invalidDateString = "xxxx-xx-xx xx:xx:xx.xxx"; // Invalid date string
        ParsePosition parsePosition = new ParsePosition(0);
        dateFormat.parse(invalidDateString, parsePosition);
    }

    @Test
    public void testParseValidDateStringWithParsePosition() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        String validDateString = "2022-12-31 23:59:59.999"; // Change to the current date and time
        ParsePosition parsePosition = new ParsePosition(0);
        Date expectedDate = new Date(); // Change to the current date and time
        Date actualDate = dateFormat.parse(validDateString, parsePosition);
        assertEquals(expectedDate, actualDate);
        assertEquals(validDateString.length(), parsePosition.getIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseObjectWithInvalidClass() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        String validDateString = "2022-12-31 23:59:59.999"; // Change to the current date and time
        Integer invalidObject = 123;
        dateFormat.parseObject(validDateString, new ParsePosition(0));
    }

    @Test
    public void testParseObjectWithValidClass() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        String validDateString = "2022-12-31 23:59:59.999"; // Change to the current date and time
        Date expectedDate = new Date(); // Change to the current date and time
        Date actualDate = (Date)dateFormat.parseObject(validDateString, new ParsePosition(0));
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        String expectedPattern = "yyyy-MM-dd HH:mm:ss.SSS";
        String actualPattern = dateFormat.getPattern();
        assertEquals(expectedPattern, actualPattern);
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        TimeZone expectedTimeZone = TimeZone.getTimeZone("UTC");
        TimeZone actualTimeZone = dateFormat.getTimeZone();
        assertEquals(expectedTimeZone, actualTimeZone);
    }

    @Test
    public void testGetLocale() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        Locale expectedLocale = Locale.FRENCH;
        Locale actualLocale = dateFormat.getLocale();
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        int expectedLength = 19;
        int actualLength = dateFormat.getMaxLengthEstimate();
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testEqualsObject() {
        FastDateFormat dateFormat1 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        FastDateFormat dateFormat2 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        assertTrue(dateFormat1.equals(dateFormat2));
    }

    @Test
    public void testNotEqualsObjectWithDifferentInstance() {
        FastDateFormat dateFormat1 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        FastDateFormat dateFormat2 = new FastDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        assertFalse(dateFormat1.equals(dateFormat2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        int expectedHashCode = dateFormat.hashCode();
        int actualHashCode = dateFormat.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testToString() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        String expectedString = "FastDateFormat[yyyy-MM-dd HH:mm:ss.SSS, java.util.UTC, java.util.Locale[language=, country=]]";
        String actualString = dateFormat.toString();
        assertEquals(expectedString, actualString);
    }
}