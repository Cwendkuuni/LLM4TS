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
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", new TimeZone("UTC"));
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithLocale() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithTimeZoneAndLocale() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", new TimeZone("UTC"), Locale.FRANCE);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new TimeZone("UTC"));
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new TimeZone("UTC"), Locale.FRANCE);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRANCE);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new TimeZone("UTC"));
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new TimeZone("UTC"), Locale.FRANCE);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRANCE);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new TimeZone("UTC"));
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new TimeZone("UTC"), Locale.FRANCE);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testFormat() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertEquals("12/nov/19", formattedDate.substring(0, 10));
    }

    @Test
    public void testFormatWithStringBuffer() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        StringBuffer stringBuffer = new StringBuffer();
        dateFormat.format(new Date(), stringBuffer, new FieldPosition(0));
        assertEquals("12/nov/19", stringBuffer.toString());
    }

    @Test
    public void testFormatWithMilliseconds() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        long millis = System.currentTimeMillis();
        String formattedDate = dateFormat.format(millis);
        assertEquals("12/nov/19", formattedDate.substring(0, 10));
    }

    @Test
    public void testFormatWithStringBufferAndMilliseconds() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        StringBuffer stringBuffer = new StringBuffer();
        dateFormat.format(System.currentTimeMillis(), stringBuffer);
        assertEquals("12/nov/19", stringBuffer.toString().substring(0, 10));
    }

    @Test
    public void testParse() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        String dateString = "12/nov/19";
        Date parsedDate = dateFormat.parse(dateString);
        assertNotNull(parsedDate);
    }

    @Test
    public void testParseWithParsePosition() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        String dateString = "12/nov/19";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = dateFormat.parse(dateString, parsePosition);
        assertNotNull(parsedDate);
        assertEquals(dateString.length(), parsePosition.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidDateString() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        String dateString = "12nov19";
        dateFormat.parse(dateString);
    }

    @Test(expected = ParseException.class)
    public void testParseObjectWithInvalidDateString() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        String dateString = "12nov19";
        dateFormat.parseObject(dateString);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        assertEquals("dd/MM/yy", dateFormat.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        TimeZone timeZone = dateFormat.getTimeZone();
        assertEquals("Europe/Paris", timeZone.getID());
    }

    @Test
    public void testGetLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        Locale locale = dateFormat.getLocale();
        assertEquals("fr_FR", locale.getLanguage());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        int maxLengthEstimate = dateFormat.getMaxLengthEstimate();
        assertEquals(11, maxLengthEstimate);
    }

    @Test
    public void testEquals() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        assertTrue(dateFormat1.equals(dateFormat2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        assertEquals(dateFormat1.hashCode(), dateFormat2.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        String toString = dateFormat.toString();
        assertTrue(toString.contains("FastDateFormat[" + "dd/MM/yy" + "," + "fr_FR" + "," + "Europe/Paris" + "]"));
    }
}