package org.apache.commons.lang3.time;

import static org.junit.Assert.*;
import java.text.*;
import java.util.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithPatternTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy", timeZone);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithPatternLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy", null, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy", timeZone, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithStyleLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithStyleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithStyleTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithStyle() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithStyleLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithStyleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithStyleTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithStyleLocale() {
        Locale locale = Locale.US;
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, locale);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithStyleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithStyleTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, timeZone, locale);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithStyle() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithStyleLocale() {
        Locale locale = Locale.US;
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithStyleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithStyleTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithStyleStyleLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, locale);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithStyleStyleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, timeZone);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithStyleStyleTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, timeZone, locale);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyle() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.FULL);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.FULL, locale);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.FULL, timeZone);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.FULL, timeZone, locale);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testFormatDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String formattedDate = dateFormat.format(calendar.getTime());
        assertEquals("dd MM yyyy", dateFormat.getPattern());
        assertEquals(formattedDate, "dd MM yyyy");
    }

    @Test
    public void testFormatTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        String formattedTime = timeFormat.format(calendar.getTime());
        assertEquals("hh:mm a", timeFormat.getPattern());
        assertEquals(formattedTime, "hh:mm a");
    }

    @Test
    public void testFormatDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        String formattedDateTime = dateTimeFormat.format(calendar.getTime());
        assertEquals("dd MM yyyy hh:mm a", dateTimeFormat.getPattern());
        assertEquals(formattedDateTime, "dd MM yyyy hh:mm a");
    }

    @Test
    public void testParseDate() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String dateString = "25 03 2023";
        Date parsedDate = dateFormat.parse(dateString);
        assertEquals(parsedDate.getTime(), new Date().getTime() - (25 * 24 * 60 * 60 * 1000 * 30));
    }

    @Test
    public void testParseTime() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        String timeString = "15:30:00";
        Date parsedTime = timeFormat.parse(timeString);
        assertEquals(parsedTime.getTime(), System.currentTimeMillis() - (15 * 60 * 60 * 1000));
    }

    @Test
    public void testParseDateTime() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        String dateTimeString = "25 03 2023 15:30:00";
        Date parsedDateTime = dateTimeFormat.parse(dateTimeString);
        assertEquals(parsedDateTime.getTime(), new Date().getTime() - (25 * 24 * 60 * 60 * 1000 * 30 + 15 * 60 * 60 * 1000));
    }

    @Test
    public void testFormatLong() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        long millis = new Date().getTime();
        String formattedDate = dateFormat.format(millis);
        assertEquals(formattedDate, dateFormat.format(new Date(millis)));
    }

    @Test
    public void testFormatDateWithCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String formattedDate = dateFormat.format(calendar);
        assertEquals(formattedDate, dateFormat.format(calendar.getTime()));
    }

    @Test
    public void testFormatTimeWithCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        String formattedTime = timeFormat.format(calendar);
        assertEquals(formattedTime, timeFormat.format(calendar.getTime()));
    }

    @Test
    public void testFormatDateTimeWithCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        String formattedDateTime = dateTimeFormat.format(calendar);
        assertEquals(formattedDateTime, dateTimeFormat.format(calendar.getTime()));
    }

    @Test
    public void testGetPattern() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertEquals("dd MM yyyy", dateFormat.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        TimeZone timeZone = dateFormat.getTimeZone();
        assertEquals("UTC", timeZone.getID());
    }

    @Test
    public void testGetLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Locale locale = dateFormat.getLocale();
        assertEquals("en_US", locale.getLanguage());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        int maxLengthEstimate = dateFormat.getMaxLengthEstimate();
        assertEquals(19, maxLengthEstimate);
    }

    @Test
    public void testEquals() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertTrue(dateFormat1.equals(dateFormat2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertEquals(dateFormat1.hashCode(), dateFormat2.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String toString = dateFormat.toString();
        assertTrue(toString.contains("FastDateFormat["));
    }
}