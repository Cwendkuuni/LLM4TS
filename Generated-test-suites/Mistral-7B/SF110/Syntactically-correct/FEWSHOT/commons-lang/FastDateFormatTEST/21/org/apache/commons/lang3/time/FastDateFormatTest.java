package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatParser;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithPatternTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", timeZone);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithPatternLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", timeZone, locale);
        assertNotNull(dateFormat);
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
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocaleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocaleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.US;
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, locale);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocaleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.US;
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        Date parsedDate = dateFormat.parse("2022-01-01 12:00:00");
        assertEquals("Sat Jan 01 00:00:00 IST 2022", dateFormat.format(parsedDate));
    }

    @Test
    public void testFormat() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatWithBuffer() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        StringBuffer buffer = new StringBuffer();
        dateFormat.format(new Date(), buffer);
        String formattedDate = buffer.toString();
        assertNotNull(formattedDate);
    }

    @Test
    public void testEquals() {
        FastDateFormat dateFormat1 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        FastDateFormat dateFormat2 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertTrue(dateFormat1.equals(dateFormat2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat dateFormat1 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        FastDateFormat dateFormat2 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertEquals(dateFormat1.hashCode(), dateFormat2.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        String expectedString = "FastDateFormat[yyyy-MM-dd HH:mm:ss, null, null]";
        String actualString = dateFormat.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
        StringBuffer buffer = new StringBuffer();
        dateFormat.applyRules(calendar, buffer);
        String expectedString = "12-01-2022";
        String actualString = buffer.toString();
        assertEquals(expectedString, actualString);
    }
}