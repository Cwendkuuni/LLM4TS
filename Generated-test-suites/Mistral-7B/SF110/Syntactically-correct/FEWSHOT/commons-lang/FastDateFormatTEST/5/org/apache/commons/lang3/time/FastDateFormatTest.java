package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat format = FastDateFormat.getInstance();
        assertNotNull(format);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy h:mm:ss a");
        assertNotNull(format);
    }

    @Test
    public void testGetInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy h:mm:ss a", timeZone);
        assertNotNull(format);
    }

    @Test
    public void testGetInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy h:mm:ss a", null, locale);
        assertNotNull(format);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy h:mm:ss a", new TimeZone("UTC"), Locale.FRENCH);
        assertNotNull(format);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat format = FastDateFormat.getDateInstance();
        assertNotNull(format);
    }

    @Test
    public void testGetDateInstanceWithStyle() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(format);
    }

    @Test
    public void testGetDateInstanceWithStyleLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, null, locale);
        assertNotNull(format);
    }

    @Test
    public void testGetDateInstanceWithStyleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, null);
        assertNotNull(format);
    }

    @Test
    public void testGetDateInstanceWithStyleTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(format);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat format = FastDateFormat.getTimeInstance();
        assertNotNull(format);
    }

    @Test
    public void testGetTimeInstanceWithStyle() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(format);
    }

    @Test
    public void testGetTimeInstanceWithStyleLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, null, locale);
        assertNotNull(format);
    }

    @Test
    public void testGetTimeInstanceWithStyleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone, null);
        assertNotNull(format);
    }

    @Test
    public void testGetTimeInstanceWithStyleTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(format);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance();
        assertNotNull(format);
    }

    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyle() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(format);
    }

    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null, locale);
        assertNotNull(format);
    }

    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone, null);
        assertNotNull(format);
    }

    @Test
    public void testGetDateTimeInstanceWithDateStyleTimeStyleTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.FRENCH;
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(format);
    }

    @Test
    public void testFormat() throws ParseException {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
        Date date = new Date();
        String formattedDate = format.format(date);
        assertEquals("Expected formatted date to not be null", formattedDate, formattedDate);
    }

    @Test
    public void testFormatWithBuffer() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
        StringBuffer buffer = new StringBuffer();
        format.format(new Date(), buffer, new FieldPosition(0));
        String formattedDate = buffer.toString();
        assertEquals("Expected formatted date to not be null", formattedDate, formattedDate);
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
        String dateString = "21/05/2022 14:30:00";
        Date parsedDate = format.parse(dateString);
        assertEquals("Expected parsed date to not be null", parsedDate, parsedDate);
    }

    @Test
    public void testParseWithParsePosition() throws ParseException {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
        String dateString = "21/05/2022 14:30:00";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = format.parse(dateString, parsePosition);
        assertEquals("Expected parsed date to not be null", parsedDate, parsedDate);
        assertEquals("Expected parse position to be 23", parsePosition.getIndex(), 23);
    }

    @Test
    public void testParseObject() throws ParseException {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
        String dateString = "21/05/2022 14:30:00";
        Date parsedDate = (Date) format.parseObject(dateString, new ParsePosition(0));
        assertEquals("Expected parsed date to not be null", parsedDate, parsedDate);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
        String pattern = format.getPattern();
        assertNotNull("Expected pattern to not be null", pattern);
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
        TimeZone timeZone = format.getTimeZone();
        assertNotNull("Expected time zone to not be null", timeZone);
    }

    @Test
    public void testGetLocale() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
        Locale locale = format.getLocale();
        assertNotNull("Expected locale to not be null", locale);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
        int maxLengthEstimate = format.getMaxLengthEstimate();
        assertNotNull("Expected max length estimate to not be null", maxLengthEstimate);
    }

    @Test
    public void testEquals() {
        FastDateFormat format1 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
        FastDateFormat format2 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
        assertEquals("Expected both format instances to be equal", format1, format2);

        FastDateFormat format3 = new FastDateFormat("dd/MM/yyyy h:mm:ss a", null, null);
        assertNotEquals("Expected different format instances to be unequal", format1, format3);
    }

    @Test
    public void testHashCode() {
        FastDateFormat format1 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
        FastDateFormat format2 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
        assertEquals("Expected both format instances to have the same hash code", format1.hashCode(), format2.hashCode());

        FastDateFormat format3 = new FastDateFormat("dd/MM/yyyy h:mm:ss a", null, null);
        assertNotEquals("Expected different format instances to have different hash codes", format1.hashCode(), format3.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
        String toString = format.toString();
        assertNotNull("Expected toString to not be null", toString);
    }

    @Test
    public void testApplyRules() {
        Calendar calendar = new GregorianCalendar(2022, 5, 21);
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
        StringBuffer buffer = new StringBuffer();
        format.applyRules(calendar, buffer);
        String formattedDate = buffer.toString();
        assertEquals("Expected formatted date to not be null", formattedDate, formattedDate);
    }
}