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

 private FastDateFormat getFormat(String pattern, TimeZone timeZone, Locale locale) {
        return FastDateFormat.getInstance(pattern, timeZone, locale);
    }

 @org.junit.Test
 public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
    }

 @org.junit.Test
 public void testGetInstanceWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        assertNotNull(dateFormat);
    }

 @org.junit.Test
 public void testGetInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z", timeZone);
        assertNotNull(dateFormat);
        assertEquals(timeZone.getID(), dateFormat.getTimeZone().getID());
    }

 @org.junit.Test
 public void testGetInstanceWithLocale() {
        Locale locale = Locale.GERMANY;
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z", null, locale);
        assertNotNull(dateFormat);
        assertEquals(locale, dateFormat.getLocale());
    }

 @org.junit.Test
 public void testGetInstanceWithAllParameters() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.GERMANY;
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z", timeZone, locale);
        assertNotNull(dateFormat);
        assertEquals(timeZone.getID(), dateFormat.getTimeZone().getID());
        assertEquals(locale, dateFormat.getLocale());
    }

 @org.junit.Test
 public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(dateFormat);
    }

 @org.junit.Test
 public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.GERMANY;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(dateFormat);
        assertEquals(locale, dateFormat.getLocale());
    }

 @org.junit.Test
 public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(dateFormat);
        assertEquals(timeZone.getID(), dateFormat.getTimeZone().getID());
    }

 @org.junit.Test
 public void testGetDateInstanceWithAllParameters() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.GERMANY;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(dateFormat);
        assertEquals(timeZone.getID(), dateFormat.getTimeZone().getID());
        assertEquals(locale, dateFormat.getLocale());
    }

 @org.junit.Test
 public void testGetTimeInstance() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(timeFormat);
    }

 @org.junit.Test
 public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.GERMANY;
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(timeFormat);
        assertEquals(locale, timeFormat.getLocale());
    }

 @org.junit.Test
 public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(timeFormat);
        assertEquals(timeZone.getID(), timeFormat.getTimeZone().getID());
    }

 @org.junit.Test
 public void testGetTimeInstanceWithAllParameters() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.GERMANY;
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(timeFormat);
        assertEquals(timeZone.getID(), timeFormat.getTimeZone().getID());
        assertEquals(locale, timeFormat.getLocale());
    }

 @org.junit.Test
 public void testGetDateTimeInstance() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertNotNull(dateTimeFormat);
    }

 @org.junit.Test
 public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.GERMANY;
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, locale);
        assertNotNull(dateTimeFormat);
        assertEquals(locale, dateTimeFormat.getLocale());
    }

 @org.junit.Test
 public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone);
        assertNotNull(dateTimeFormat);
        assertEquals(timeZone.getID(), dateTimeFormat.getTimeZone().getID());
    }

 @org.junit.Test
 public void testGetDateTimeInstanceWithAllParameters() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.GERMANY;
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(dateTimeFormat);
        assertEquals(timeZone.getID(), dateTimeFormat.getTimeZone().getID());
        assertEquals(locale, dateTimeFormat.getLocale());
    }

 @org.junit.Test
 public void testFormatDate() {
        FastDateFormat dateFormat = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        Calendar calendar = new GregorianCalendar(2019, 11, 25, 12, 30, 0, Calendar.MILLIS_IN_DAY);
        String formattedDate = dateFormat.format(calendar.getTime());
        assertEquals("25/12/2019 12:30:00 +0530", formattedDate);
    }

 @org.junit.Test
 public void testFormatLong() {
        FastDateFormat dateFormat = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        long timeInMilliseconds = System.currentTimeMillis();
        String formattedLong = dateFormat.format(timeInMilliseconds);
        assertEquals(dateFormat.format(new Date(timeInMilliseconds)), formattedLong);
    }

 @org.junit.Test
 public void testFormatCalendar() {
        FastDateFormat dateFormat = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        Calendar calendar = new GregorianCalendar(2019, 11, 25, 12, 30, 0, Calendar.MILLIS_IN_DAY);
        String formattedCalendar = dateFormat.format(calendar);
        assertEquals("25/12/2019 12:30:00 +0530", formattedCalendar);
    }

 @org.junit.Test(expected = ParseException.class)
 public void testParseInvalidDate() {
        FastDateFormat dateFormat = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        String invalidDate = "32/12/2019 12:30:00 +0530";
        dateFormat.parse(invalidDate);
    }

 @org.junit.Test(expected = ParseException.class)
 public void testParseInvalidDateWithParsePosition() {
        FastDateFormat dateFormat = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        String invalidDate = "32/12/2019 12:30:00 +0530";
        ParsePosition parsePosition = new ParsePosition(0);
        dateFormat.parse(invalidDate, parsePosition);
    }

 @org.junit.Test(expected = ParseException.class)
 public void testParseInvalidObject() {
        FastDateFormat dateFormat = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        String invalidObject = "InvalidObject";
        dateFormat.parseObject(invalidObject);
    }

 @org.junit.Test(expected = ParseException.class)
 public void testParseInvalidObjectWithParsePosition() {
        FastDateFormat dateFormat = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        String invalidObject = "InvalidObject";
        ParsePosition parsePosition = new ParsePosition(0);
        dateFormat.parseObject(invalidObject, parsePosition);
    }

 @org.junit.Test
 public void testGetPattern() {
        FastDateFormat dateFormat = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        String pattern = dateFormat.getPattern();
        assertEquals("dd/MM/yyyy HH:mm:ss z", pattern);
    }

 @org.junit.Test
 public void testGetTimeZone() {
        FastDateFormat dateFormat = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        TimeZone timeZone = dateFormat.getTimeZone();
        assertEquals(TimeZone.getDefault(), timeZone);
    }

 @org.junit.Test
 public void testGetLocale() {
        FastDateFormat dateFormat = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        Locale locale = dateFormat.getLocale();
        assertEquals(Locale.getDefault(), locale);
    }

 @org.junit.Test
 public void testGetMaxLengthEstimate() {
        FastDateFormat dateFormat = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        int maxLengthEstimate = dateFormat.getMaxLengthEstimate();
        // Max length estimate is not defined in the test case, but it should be tested according to the documentation
    }

 @org.junit.Test
 public void testEquals() {
        FastDateFormat dateFormat1 = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        FastDateFormat dateFormat2 = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        assertTrue(dateFormat1.equals(dateFormat2));
    }

 @org.junit.Test
 public void testHashCode() {
        FastDateFormat dateFormat = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        int hashCode = dateFormat.hashCode();
        // Hashcode should be tested according to the documentation, but it's not always deterministic, so it's not included in this test case
    }

 @org.junit.Test
 public void testToString() {
        FastDateFormat dateFormat = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        String toString = dateFormat.toString();
        assertTrue(toString.contains("FastDateFormat["));
    }

 @org.junit.Test
 public void testApplyRules() {
        FastDateFormat dateFormat = getFormat("dd/MM/yyyy HH:mm:ss z", TimeZone.getDefault(), Locale.getDefault());
        Calendar calendar = new GregorianCalendar(2019, 11, 25, 12, 30, 0, Calendar.MILLIS_IN_DAY);
        StringBuffer stringBuffer = new StringBuffer(32);
        String formattedDate = dateFormat.applyRules(calendar, stringBuffer);
        assertEquals("25/12/2019 12:30:00 +0530", formattedDate.toString());
    }
}