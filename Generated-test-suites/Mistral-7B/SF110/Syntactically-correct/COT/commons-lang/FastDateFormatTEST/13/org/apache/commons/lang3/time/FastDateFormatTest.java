package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

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
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithPatternTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", timeZone);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithPatternLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", null, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, null, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone, null);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
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
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, null, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone, null);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, null, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, timeZone, null);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testFormat() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
        Date date = new Date();
        String expected = "24-12-2022 12:34:56.789 UTC";
        String actual = fastDateFormat.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWithNullObject() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String actual = fastDateFormat.format(null);
        assertNull(actual);
    }

    @Test
    public void testFormatWithNullStringBuffer() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        StringBuffer stringBuffer = null;
        fastDateFormat.format(new Date(), stringBuffer, new FieldPosition(0, 0));
        assertNull(stringBuffer);
    }

    @Test
    public void testFormatWithNullFieldPosition() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        StringBuffer stringBuffer = new StringBuffer();
        fastDateFormat.format(new Date(), stringBuffer, null);
        assertNotNull(stringBuffer);
        assertEquals("", stringBuffer.toString());
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
        String dateString = "24-12-2022 12:34:56.789 UTC";
        Date expected = new Date();
        expected.setTime(1671429896000L);
        Date actual = fastDateFormat.parse(dateString);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseWithNullObject() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String parseString = null;
        Date actual = fastDateFormat.parse(parseString);
        assertNull(actual);
    }

    @Test
    public void testParseWithNullParsePosition() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String parseString = "24-12-2022 12:34:56.789 UTC";
        ParsePosition parsePosition = null;
        Date actual = fastDateFormat.parse(parseString, parsePosition);
        assertNull(actual);
        assertEquals(-1, parsePosition.getIndex());
    }

    @Test
    public void testParseObject() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
        String dateString = "24-12-2022 12:34:56.789 UTC";
        Date expected = new Date();
        expected.setTime(1671429896000L);
        Object actual = fastDateFormat.parseObject(dateString, ParsePosition.POSITION_UNKNOWN);
        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected, actual);
    }

    @Test
    public void testParseObjectWithNullObject() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Object actual = fastDateFormat.parseObject(null, ParsePosition.POSITION_UNKNOWN);
        assertNull(actual);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
        String expected = "dd-MM-yyyy HH:mm:ss.SSS z";
        String actual = fastDateFormat.getPattern();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
        TimeZone expected = TimeZone.getTimeZone("UTC");
        TimeZone actual = fastDateFormat.getTimeZone();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
        Locale expected = Locale.US;
        Locale actual = fastDateFormat.getLocale();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
        int expected = 42;
        int actual = fastDateFormat.getMaxLengthEstimate();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
        assertTrue(fastDateFormat1.equals(fastDateFormat2));
    }

    @Test
    public void testEqualsWithDifferentObjectType() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
        String string = "test";
        assertFalse(fastDateFormat.equals(string));
    }

    @Test
    public void testHashCode() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
        int expected = 91;
        int actual = fastDateFormat.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
        String expected = "FastDateFormat[dd-MM-yyyy HH:mm:ss.SSS z, US, UTC]";
        String actual = fastDateFormat.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testApplyRules() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
        Calendar calendar = new GregorianCalendar(2022, Calendar.DECEMBER, 24);
        StringBuffer expected = new StringBuffer();
        expected.append("24");
        expected.append("-12-");
        expected.append("2022");
        expected.append(" 12:34:56.789");
        expected.append(" UTC");
        String expectedString = expected.toString();
        StringBuffer actual = fastDateFormat.applyRules(calendar, new StringBuffer());
        assertEquals(expectedString, actual.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        FastDateFormat fastDateFormat = new FastDateFormat("invalidPattern", null, null);
    }

}