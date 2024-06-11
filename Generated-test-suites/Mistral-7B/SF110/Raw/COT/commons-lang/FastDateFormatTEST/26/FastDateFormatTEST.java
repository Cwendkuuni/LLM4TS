package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatCache;

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
    public void testGetInstanceWithPatternTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", new SimpleDateFormat("UTC").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPatternLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", Locale.FRENCH);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", Locale.FRENCH, new SimpleDateFormat("UTC").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.FRENCH);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, new SimpleDateFormat("UTC").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithTimeZoneLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.FRENCH, new SimpleDateFormat("UTC").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.FRENCH);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, new SimpleDateFormat("UTC").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithTimeZoneLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.FRENCH, new SimpleDateFormat("UTC").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.FRENCH);
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, new SimpleDateFormat("UTC").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithTimeZoneLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.FRENCH, new SimpleDateFormat("UTC").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @org.junit.Test
    public void testFormat() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse("25-12-2022 10:30:00");
        String formattedDate = fastDateFormat.format(date);
        assertEquals("25 December 2022 10:30:00", formattedDate);
    }

    @org.junit.Test
    public void testFormatWithBuffer() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse("25-12-2022 10:30:00");
        StringBuffer sb = new StringBuffer();
        fastDateFormat.format(date, sb);
        assertEquals("25 December 2022 10:30:00", sb.toString());
    }

    @org.junit.Test
    public void testParse() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        String dateString = "25 December 2022 10:30:00";
        Date parsedDate = fastDateFormat.parse(dateString);
        assertEquals(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse("25-12-2022 10:30:00").getTime(), parsedDate.getTime());
    }

    @org.junit.Test
    public void testParseWithParsePosition() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        String dateString = "25 December 2022 10:30:00";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = fastDateFormat.parse(dateString, parsePosition);
        assertEquals(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse("25-12-2022 10:30:00").getTime(), parsedDate.getTime());
        assertEquals(dateString.length(), parsePosition.getIndex());
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseWithInvalidPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        String dateString = "25 December, 2022 10:30:00";
        fastDateFormat.parse(dateString);
    }

    @org.junit.Test
    public void testGetPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertEquals("dd MMM yyyy HH:mm:ss", fastDateFormat.getPattern());
    }

    @org.junit.Test
    public void testGetTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertEquals(TimeZone.getDefault(), fastDateFormat.getTimeZone());
    }

    @org.junit.Test
    public void testGetLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertEquals(Locale.getDefault(), fastDateFormat.getLocale());
    }

    @org.junit.Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertEquals(32, fastDateFormat.getMaxLengthEstimate());
    }

    @org.junit.Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertTrue(fastDateFormat1.equals(fastDateFormat2));
    }

    @org.junit.Test
    public void testHashCode() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        int hashCode1 = fastDateFormat.hashCode();
        FastDateFormat fastDateFormatNew = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        int hashCode2 = fastDateFormatNew.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @org.junit.Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        String toString = fastDateFormat.toString();
        assertTrue(toString.contains("[dd MMM yyyy HH:mm:ss]"));
    }

    @org.junit.Test
    public void testApplyRules() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse("25-12-2022 10:30:00"));
        StringBuffer sb = new StringBuffer();
        fastDateFormat.applyRules(calendar, sb);
        assertEquals("25 December 2022 10:30:00", sb.toString());
    }
}