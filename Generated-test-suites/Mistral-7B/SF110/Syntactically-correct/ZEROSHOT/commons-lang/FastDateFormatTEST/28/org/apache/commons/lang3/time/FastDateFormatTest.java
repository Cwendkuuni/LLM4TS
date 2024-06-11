package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance("HH:mm", timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance("dd-MM-yyyy HH:mm", timeZone, locale);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testFormat() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = simpleDateFormat.parse("25-12-2022");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String formattedDate = fastDateFormat.format(date);
        assertEquals("25-Dec-2022", formattedDate);
    }

    @org.junit.Test
    public void testFormatWithPattern() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = simpleDateFormat.parse("25-12-2022 10:20:30");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
        String formattedDate = fastDateFormat.format(date);
        assertEquals("25-Dec-2022 10:20:30", formattedDate);
    }

    @org.junit.Test
    public void testFormatWithLocale() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRENCH);
        Date date = simpleDateFormat.parse("25-12-2022 10:20:30");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss", Locale.FRENCH);
        String formattedDate = fastDateFormat.format(date);
        assertEquals("25-déc-2022 10:20:30", formattedDate);
    }

    @org.junit.Test
    public void testFormatWithTimeZone() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", TimeZone.getTimeZone("UTC"));
        Date date = simpleDateFormat.parse("25-12-2022 10:20:30");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss", TimeZone.getTimeZone("UTC"));
        String formattedDate = fastDateFormat.format(date);
        assertEquals("25-Dec-2022 10:20:30", formattedDate);
    }

    @org.junit.Test
    public void testFormatWithPatternTimeZoneLocale() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
        Date date = simpleDateFormat.parse("25-12-2022 10:20:30");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
        String formattedDate = fastDateFormat.format(date);
        assertEquals("25-déc-2022 10:20:30", formattedDate);
    }

    @org.junit.Test
    public void testParse() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Date date = fastDateFormat.parse("25-Dec-2022");
        assertNotNull(date);
    }

    @org.junit.Test
    public void testParseWithPattern() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
        Date date = fastDateFormat.parse("25-Dec-2022 10:20:30");
        assertNotNull(date);
    }

    @org.junit.Test
    public void testParseWithLocale() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss", Locale.FRENCH);
        Date date = fastDateFormat.parse("25-déc-2022 10:20:30");
        assertNotNull(date);
    }

    @org.junit.Test
    public void testParseWithTimeZone() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss", TimeZone.getTimeZone("UTC"));
        Date date = fastDateFormat.parse("25-Dec-2022 10:20:30");
        assertNotNull(date);
    }

    @org.junit.Test
    public void testParseWithPatternTimeZoneLocale() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
        Date date = fastDateFormat.parse("25-déc-2022 10:20:30");
        assertNotNull(date);
    }

    @org.junit.Test
    public void testGetPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertEquals("dd-MMM-yy hh:mm a z", fastDateFormat.getPattern());
    }

    @org.junit.Test
    public void testGetTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertEquals(TimeZone.getDefault(), fastDateFormat.getTimeZone());
    }

    @org.junit.Test
    public void testGetLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertEquals(Locale.getDefault(), fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
        assertEquals(32, maxLengthEstimate);
    }

    @org.junit.Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance();
        assertTrue(fastDateFormat1.equals(fastDateFormat2));
    }

    @org.junit.Test
    public void testHashCode() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        int hashCode = fastDateFormat.hashCode();
        assertEquals(-599254709, hashCode);
    }

    @org.junit.Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String toString = fastDateFormat.toString();
        assertTrue(toString.contains("FastDateFormat[dd-MMM-yy hh:mm a z]"));
    }

    @org.junit.Test
    public void testApplyRules() {
        Calendar calendar = new GregorianCalendar(2022, 11, 25, 10, 20, 30);
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        StringBuffer stringBuffer = new StringBuffer();
        fastDateFormat.applyRules(calendar, stringBuffer);
        assertEquals("25-Dec-2022 10:20:30", stringBuffer.toString());
    }
}