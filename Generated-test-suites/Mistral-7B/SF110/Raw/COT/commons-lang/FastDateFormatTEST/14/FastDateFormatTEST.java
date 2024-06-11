package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
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
    public void testGetInstanceWithTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", new TimeZone("UTC"));
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithAllParameters() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", new TimeZone("UTC"), Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, new TimeZone("UTC"));
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithAllParameters() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, new TimeZone("UTC"), Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, new TimeZone("UTC"));
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithAllParameters() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, new TimeZone("UTC"), Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, new TimeZone("UTC"));
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithAllParameters() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, new TimeZone("UTC"), Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetInstanceArguments() {
        FastDateFormat.getInstance(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetInstanceArgumentsWithPattern() {
        FastDateFormat.getInstance("", new TimeZone("UTC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetInstanceArgumentsWithLocale() {
        FastDateFormat.getInstance("dd-MM-yyyy", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetInstanceArgumentsWithTimeZoneAndLocale() {
        FastDateFormat.getInstance("dd-MM-yyyy", new TimeZone("UTC"), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetInstanceArgumentsWithAllParameters() {
        FastDateFormat.getInstance("dd-MM-yyyy", new TimeZone("UTC"), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetDateInstanceArguments() {
        FastDateFormat.getDateInstance(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetDateInstanceArgumentsWithLocale() {
        FastDateFormat.getDateInstance(FastDateFormat.FULL, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetDateInstanceArgumentsWithTimeZone() {
        FastDateFormat.getDateInstance(FastDateFormat.FULL, new TimeZone("UTC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetDateInstanceArgumentsWithAllParameters() {
        FastDateFormat.getDateInstance(FastDateFormat.FULL, new TimeZone("UTC"), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetTimeInstanceArguments() {
        FastDateFormat.getTimeInstance(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetTimeInstanceArgumentsWithLocale() {
        FastDateFormat.getTimeInstance(FastDateFormat.FULL, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetTimeInstanceArgumentsWithTimeZone() {
        FastDateFormat.getTimeInstance(FastDateFormat.FULL, new TimeZone("UTC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetTimeInstanceArgumentsWithAllParameters() {
        FastDateFormat.getTimeInstance(FastDateFormat.FULL, new TimeZone("UTC"), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetDateTimeInstanceArguments() {
        FastDateFormat.getDateTimeInstance(-1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetDateTimeInstanceArgumentsWithLocale() {
        FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetDateTimeInstanceArgumentsWithTimeZone() {
        FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, new TimeZone("UTC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetDateTimeInstanceArgumentsWithAllParameters() {
        FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, new TimeZone("UTC"), null);
    }

    @Test
    public void testFormatObject() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        Date date = new Date();
        String formattedDate = fastDateFormat.format(date);
        assertEquals("Invalid formatted date", "dd-MM-yyyy HH:mm:ss zzz", formattedDate);
    }

    @Test
    public void testFormatObjectWithBuffer() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        Date date = new Date();
        StringBuffer stringBuffer = new StringBuffer();
        fastDateFormat.format(date, stringBuffer);
        assertEquals("Invalid formatted date", "dd-MM-yyyy HH:mm:ss zzz", stringBuffer.toString());
    }

    @Test(expected = ParseException.class)
    public void testInvalidFormat() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss zzzzzz");
        fastDateFormat.parse("2022-12-31");
    }

    @Test(expected = ParseException.class)
    public void testInvalidParsePosition() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss zzz");
        ParsePosition parsePosition = new ParsePosition(0);
        fastDateFormat.parse("2022-12-31", parsePosition);
    }

    @Test(expected = ParseException.class)
    public void testParseObjectWithInvalidParsePosition() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss zzz");
        ParsePosition parsePosition = new ParsePosition(10);
        fastDateFormat.parseObject("2022-12-31", parsePosition);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        String pattern = fastDateFormat.getPattern();
        assertEquals("Invalid pattern", "dd-MM-yyyy HH:mm:ss zzz", pattern);
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, new TimeZone("UTC"));
        TimeZone timeZone = fastDateFormat.getTimeZone();
        assertEquals("Invalid timezone", "UTC", timeZone.getID());
    }

    @Test
    public void testGetLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.US);
        Locale locale = fastDateFormat.getLocale();
        assertEquals("Invalid locale", "US", locale.getDisplayCountry());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
        assertEquals("Invalid max length estimate", 40, maxLengthEstimate);
    }

    @Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertTrue("FastDateFormat objects are not equal", fastDateFormat1.equals(fastDateFormat2));
    }

    @Test
    public void testNotEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        Object obj = new Object();
        assertFalse("FastDateFormat and Object are equal", fastDateFormat1.equals(obj));
    }

    @Test
    public void testHashCode() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        int hashCode = fastDateFormat.hashCode();
        assertTrue("Invalid hashcode", hashCode > 0);
    }

    @Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        String toString = fastDateFormat.toString();
        assertTrue("Invalid toString", toString.contains("FastDateFormat"));
    }

    @Test
    public void testApplyRulesWithNullCalendar() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        StringBuffer stringBuffer = new StringBuffer();
        assertThrows(NullPointerException.class, () -> fastDateFormat.applyRules(null, stringBuffer));
    }

    @Test
    public void testApplyRules() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        Calendar calendar = Calendar.getInstance();
        StringBuffer stringBuffer = new StringBuffer();
        fastDateFormat.applyRules(calendar, stringBuffer);
        assertEquals("Invalid formatted date", "dd-MM-yyyy HH:mm:ss zzz", stringBuffer.toString());
    }
}