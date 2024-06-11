package org.apache.commons.lang3.time;

import static org.junit.Assert.*;
import java.text.*;
import java.time.*;
import java.util.*;

import org.junit.Test;

public class FastDateFormatTest {
    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yy h:mm a zzzz", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());

        dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        assertEquals("yyyy-MM-dd HH:mm:ss.SSS", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());

        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yy h:mm a zzzz", timeZone);
        assertEquals("dd/MM/yy h:mm a zzzz", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());

        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yy h:mm a zzzz", null, locale);
        assertEquals("dd/MM/yy h:mm a zzzz", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());

        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yy h:mm a zzzz", timeZone, locale);
        assertEquals("dd/MM/yy h:mm a zzzz", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());

        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yy", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());

        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG, locale);
        assertEquals("MM/dd/yy", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());

        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG, timeZone);
        assertEquals("dd/MM/yy", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());

        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG, timeZone, locale);
        assertEquals("dd/MM/yy", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());

        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(timeFormat);
        assertEquals("hh:mm a z", timeFormat.getPattern());
        assertNull(timeFormat.getTimeZone());
        assertNull(timeFormat.getLocale());

        Date date = new Date();
        String formattedDate = timeFormat.format(date);
        Date parsedDate = timeFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
        assertEquals("hh:mm a z", timeFormat.getPattern());
        assertNull(timeFormat.getTimeZone());
        assertEquals(locale, timeFormat.getLocale());

        Date date = new Date();
        String formattedDate = timeFormat.format(date);
        Date parsedDate = timeFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
        assertEquals("hh:mm a z", timeFormat.getPattern());
        assertEquals(timeZone, timeFormat.getTimeZone());
        assertNull(timeFormat.getLocale());

        Date date = new Date();
        String formattedDate = timeFormat.format(date);
        Date parsedDate = timeFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.US;
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertEquals("hh:mm a z", timeFormat.getPattern());
        assertEquals(timeZone, timeFormat.getTimeZone());
        assertEquals(locale, timeFormat.getLocale());

        Date date = new Date();
        String formattedDate = timeFormat.format(date);
        Date parsedDate = timeFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM);
        assertNotNull(dateTimeFormat);
        assertEquals("dd/MM/yy h:mm a zzzz", dateTimeFormat.getPattern());
        assertNull(dateTimeFormat.getTimeZone());
        assertNull(dateTimeFormat.getLocale());

        Date date = new Date();
        String formattedDate = dateTimeFormat.format(date);
        Date parsedDate = dateTimeFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM, locale);
        assertEquals("MM/dd/yy h:mm a zzzz", dateTimeFormat.getPattern());
        assertNull(dateTimeFormat.getTimeZone());
        assertEquals(locale, dateTimeFormat.getLocale());

        Date date = new Date();
        String formattedDate = dateTimeFormat.format(date);
        Date parsedDate = dateTimeFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM, timeZone);
        assertEquals("dd/MM/yy h:mm a zzzz", dateTimeFormat.getPattern());
        assertEquals(timeZone, dateTimeFormat.getTimeZone());
        assertNull(dateTimeFormat.getLocale());

        Date date = new Date();
        String formattedDate = dateTimeFormat.format(date);
        Date parsedDate = dateTimeFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.US;
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM, timeZone, locale);
        assertEquals("dd/MM/yy h:mm a zzzz", dateTimeFormat.getPattern());
        assertEquals(timeZone, dateTimeFormat.getTimeZone());
        assertEquals(locale, dateTimeFormat.getLocale());

        Date date = new Date();
        String formattedDate = dateTimeFormat.format(date);
        Date parsedDate = dateTimeFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }
}