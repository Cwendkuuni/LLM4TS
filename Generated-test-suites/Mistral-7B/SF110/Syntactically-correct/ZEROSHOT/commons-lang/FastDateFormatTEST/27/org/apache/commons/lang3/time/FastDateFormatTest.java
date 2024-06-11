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
    public void testGetInstancePattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss zzz");
        assertEquals("dd-MM-yyyy HH:mm:ss zzz", fastDateFormat.getPattern());
    }

    @org.junit.Test
    public void testGetInstanceNullPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertNull(fastDateFormat.getPattern());
    }

    @org.junit.Test
    public void testGetInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss zzz", timeZone);
        assertEquals(timeZone.getID(), fastDateFormat.getTimeZone().getID());
    }

    @org.junit.Test
    public void testGetInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss zzz", locale);
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss zzz", timeZone, locale);
        assertEquals(timeZone.getID(), fastDateFormat.getTimeZone().getID());
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetDateInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance();
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithStyle() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getDateStyle());
    }

    @org.junit.Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getDateStyle());
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getDateStyle());
        assertEquals(timeZone.getID(), fastDateFormat.getTimeZone().getID());
    }

    @org.junit.Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertEquals(FastDateFormat.MEDIUM, fastDateFormat.getDateStyle());
        assertEquals(locale, fastDateFormat.getLocale());
        assertEquals(timeZone.getID(), fastDateFormat.getTimeZone().getID());
    }

    @org.junit.Test
    public void testGetTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance();
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithStyle() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertEquals(FastDateFormat.SHORT, fastDateFormat.getTimeStyle());
    }

    @org.junit.Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, locale);
        assertEquals(FastDateFormat.SHORT, fastDateFormat.getTimeStyle());
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone);
        assertEquals(FastDateFormat.SHORT, fastDateFormat.getTimeStyle());
        assertEquals(timeZone.getID(), fastDateFormat.getTimeZone().getID());
    }

    @org.junit.Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone, locale);
        assertEquals(FastDateFormat.SHORT, fastDateFormat.getTimeStyle());
        assertEquals(locale, fastDateFormat.getLocale());
        assertEquals(timeZone.getID(), fastDateFormat.getTimeZone().getID());
    }

    @org.junit.Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance();
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithDateStyleAndTimeStyle() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.DATE_TIME, FastDateFormat.SHORT);
        assertEquals(FastDateFormat.DATE_TIME, fastDateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, fastDateFormat.getTimeStyle());
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithLocaleAndDateStyleAndTimeStyle() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.DATE_TIME, FastDateFormat.SHORT, locale);
        assertEquals(FastDateFormat.DATE_TIME, fastDateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, fastDateFormat.getTimeStyle());
        assertEquals(locale, fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithTimeZoneAndDateStyleAndTimeStyle() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.DATE_TIME, FastDateFormat.SHORT, timeZone);
        assertEquals(FastDateFormat.DATE_TIME, fastDateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, fastDateFormat.getTimeStyle());
        assertEquals(timeZone.getID(), fastDateFormat.getTimeZone().getID());
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocaleAndDateStyleAndTimeStyle() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.DATE_TIME, FastDateFormat.SHORT, timeZone, locale);
        assertEquals(FastDateFormat.DATE_TIME, fastDateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, fastDateFormat.getTimeStyle());
        assertEquals(locale, fastDateFormat.getLocale());
        assertEquals(timeZone.getID(), fastDateFormat.getTimeZone().getID());
    }

    @org.junit.Test
    public void testParseDate() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.DATE_TIME, FastDateFormat.SHORT);
        Date date = fastDateFormat.parse("25-12-2022 14:30:00 EST");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(12, calendar.get(Calendar.MONTH) + 1); // Month is zero-based in GregorianCalendar
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(14, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(330, calendar.get(Calendar.MILLISECOND)); // Millisecond is not specified in the input string
        assertEquals(Calendar.SHORT, calendar.get(Calendar.LENIENT)); // lenient == SHORT by default
        assertEquals("EST", fastDateFormat.getTimeZone().getID());
    }

    @org.junit.Test
    public void testParseDateWithPattern() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance("dd-MM-yyyy HH:mm:ss zzz", FastDateFormat.SHORT);
        Date date = fastDateFormat.parse("25-12-2022 14:30:00 EST");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(12, calendar.get(Calendar.MONTH) + 1);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(14, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(330, calendar.get(Calendar.MILLISECOND));
        assertEquals(Calendar.SHORT, calendar.get(Calendar.LENIENT));
        assertEquals("EST", fastDateFormat.getTimeZone().getID());
    }

    @org.junit.Test
    public void testParseDateWithParsePosition() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.DATE_TIME, FastDateFormat.SHORT);
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = fastDateFormat.parse("25-12-2022 14:30:00 EST", parsePosition);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(12, calendar.get(Calendar.MONTH) + 1);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(14, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(330, calendar.get(Calendar.MILLISECOND));
        assertEquals(Calendar.SHORT, calendar.get(Calendar.LENIENT));
        assertEquals("EST", fastDateFormat.getTimeZone().getID());
        assertEquals(19, parsePosition.getIndex());
    }

    @org.junit.Test
    public void testFormatDate() {
        Calendar calendar = new GregorianCalendar(2022, 11, 25, 14, 30, 0, 330);
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.DATE_TIME, FastDateFormat.SHORT);
        String formattedDate = fastDateFormat.format(calendar.getTime());
        assertEquals("25-12-2022 14:30:00 EST", formattedDate);
    }

    @org.junit.Test
    public void testFormatDateWithPattern() {
        Calendar calendar = new GregorianCalendar(2022, 11, 25, 14, 30, 0, 330);
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance("dd-MM-yyyy HH:mm:ss zzz", FastDateFormat.SHORT);
        String formattedDate = fastDateFormat.format(calendar.getTime());
        assertEquals("25-12-2022 14:30:00 EST", formattedDate);
    }

    @org.junit.Test
    public void testFormatDateWithStringBuffer() {
        Calendar calendar = new GregorianCalendar(2022, 11, 25, 14, 30, 0, 330);
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.DATE_TIME, FastDateFormat.SHORT);
        StringBuffer stringBuffer = new StringBuffer();
        fastDateFormat.format(calendar.getTime(), stringBuffer);
        assertEquals("25-12-2022 14:30:00 EST", stringBuffer.toString());
    }

    @org.junit.Test
    public void testFormatDateWithTimeZone() {
        Calendar calendar = new GregorianCalendar(2022, 11, 25, 14, 30, 0, 330);
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.DATE_TIME, FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"));
        String formattedDate = fastDateFormat.format(calendar.getTime());
        assertEquals("25-12-2022 19:30:00 UTC", formattedDate);
    }

    @org.junit.Test
    public void testFormatDateWithLocale() {
        Calendar calendar = new GregorianCalendar(2022, 11, 25, 14, 30, 0, 330);
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.DATE_TIME, FastDateFormat.SHORT, Locale.FRENCH);
        String formattedDate = fastDateFormat.format(calendar.getTime());
        assertEquals("25-12-2022 à 14 heures 30 minutes EST", formattedDate);
    }

    @org.junit.Test
    public void testFormatDateWithTimeZoneAndLocale() {
        Calendar calendar = new GregorianCalendar(2022, 11, 25, 14, 30, 0, 330);
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.DATE_TIME, FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"), Locale.FRENCH);
        String formattedDate = fastDateFormat.format(calendar.getTime());
        assertEquals("25-12-2022 à 19 heures 30 minutes UTC", formattedDate);
    }
}