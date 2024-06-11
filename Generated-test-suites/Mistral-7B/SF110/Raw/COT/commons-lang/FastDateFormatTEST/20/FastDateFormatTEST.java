package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithPatternTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", new SimpleDateFormat("GMT+02:00").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithPatternLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", Locale.FRENCH);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", Locale.FRENCH, new SimpleDateFormat("GMT+02:00").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.FRENCH);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, new SimpleDateFormat("GMT+02:00").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocaleTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.FRENCH, new SimpleDateFormat("GMT+02:00").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.FRENCH);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, new SimpleDateFormat("GMT+02:00").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocaleTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.FRENCH, new SimpleDateFormat("GMT+02:00").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.FRENCH);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, new SimpleDateFormat("GMT+02:00").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocaleTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.FRENCH, new SimpleDateFormat("GMT+02:00").getTimeZone());
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testFormat() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        Date date = new Date();
        String formattedDate = fastDateFormat.format(date);
        assertNotNull(formattedDate);
    }

    @Test
    public void testFormatWithBuffer() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        StringBuffer stringBuffer = new StringBuffer();
        fastDateFormat.format(new Date(), stringBuffer);
        String formattedDate = stringBuffer.toString();
        assertNotNull(formattedDate);
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        String dateString = "12-12-2022 12:12:12";
        Date parsedDate = fastDateFormat.parse(dateString);
        assertNotNull(parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        String dateString = "12-12-2022 12:12:ab";
        fastDateFormat.parse(dateString);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        String pattern = fastDateFormat.getPattern();
        assertNotNull(pattern);
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        TimeZone timeZone = fastDateFormat.getTimeZone();
        assertNotNull(timeZone);
    }

    @Test
    public void testGetLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        Locale locale = fastDateFormat.getLocale();
        assertNotNull(locale);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
        assertNotNull(maxLengthEstimate);
    }

    @Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        FastDateFormat fastDateFormat3 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.FULL);
        assertTrue(fastDateFormat1.equals(fastDateFormat2));
        assertFalse(fastDateFormat1.equals(fastDateFormat3));
    }

    @Test
    public void testHashCode() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        FastDateFormat fastDateFormat3 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.FULL);
        int hashCode1 = fastDateFormat1.hashCode();
        int hashCode2 = fastDateFormat2.hashCode();
        int hashCode3 = fastDateFormat3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        String toString = fastDateFormat.toString();
        assertNotNull(toString);
    }

    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.FULL);
        StringBuffer stringBuffer = new StringBuffer();
        fastDateFormat.applyRules(calendar, stringBuffer);
        String formattedDate = stringBuffer.toString();
        assertNotNull(formattedDate);
    }
}