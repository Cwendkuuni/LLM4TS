package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a");
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a", TimeZone.getTimeZone("UTC"));
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithLocale() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a", Locale.FRENCH);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithTimeZoneAndLocale() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRENCH);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"));
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"), Locale.FRENCH);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRENCH);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"));
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"), Locale.FRENCH);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRENCH);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"));
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"), Locale.FRENCH);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testFormat() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertEquals("Expected a formatted date", date.toString(), formattedDate);
    }

    @Test
    public void testFormatWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String pattern = "dd-MM-yyyy";
        Date date = new Date();
        String formattedDate = dateFormat.format(date, new StringBuffer(), null);
        assertEquals("Expected a formatted date", pattern + " " + date.toString(), formattedDate.toString());
    }

    @Test
    public void testFormatWithCalendar() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Calendar calendar = new GregorianCalendar(2022, Calendar.JANUARY, 1);
        String formattedDate = dateFormat.format(calendar);
        assertEquals("Expected a formatted date", calendar.getTime().toString(), formattedDate);
    }

    @Test
    public void testFormatWithMilliseconds() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        long millis = System.currentTimeMillis();
        String formattedDate = dateFormat.format(millis);
        assertEquals("Expected a formatted date", new Date(millis).toString(), formattedDate);
    }

    @Test
    public void testFormatWithCalendarAndBuffer() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Calendar calendar = new GregorianCalendar(2022, Calendar.JANUARY, 1);
        StringBuffer buffer = new StringBuffer();
        String formattedDate = dateFormat.format(calendar, buffer, null);
        assertEquals("Expected a formatted date", calendar.getTime().toString(), formattedDate.toString());
    }

    @Test
    public void testFormatWithMillisecondsAndBuffer() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        long millis = System.currentTimeMillis();
        StringBuffer buffer = new StringBuffer();
        String formattedDate = dateFormat.format(millis, buffer, null);
        assertEquals("Expected a formatted date", new Date(millis).toString(), formattedDate.toString());
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String dateString = "25-01-2022";
        Date parsedDate = dateFormat.parse(dateString);
        Date expectedDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
        assertEquals("Expected a parsed date", expectedDate, parsedDate);
    }

    @Test
    public void testParseObject() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String dateString = "25-01-2022";
        Date parsedDate = (Date) dateFormat.parseObject(dateString);
        Date expectedDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
        assertEquals("Expected a parsed date", expectedDate, parsedDate);
    }

    @Test
    public void testParseWithParsePosition() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String dateString = "25-01-2022";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = dateFormat.parse(dateString, parsePosition);
        Date expectedDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
        assertEquals("Expected a parsed date", expectedDate, parsedDate);
        assertEquals("Expected the parse position to be at the end of the string", dateString.length(), parsePosition.getIndex());
    }

    @Test
    public void testParseWithInvalidDateString() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String invalidDateString = "invalidDate";
        assertThrows(ParseException.class, () -> dateFormat.parse(invalidDateString));
    }

    @Test
    public void testGetPattern() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String pattern = "dd-MM-yyyy";
        assertEquals("Expected the pattern to be the same", pattern, dateFormat.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        assertEquals("Expected the time zone to be the same", timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testGetLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Locale locale = Locale.US;
        assertEquals("Expected the locale to be the same", locale, dateFormat.getLocale());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        int maxLengthEstimate = 50;
        assertEquals("Expected the max length estimate to be the same", maxLengthEstimate, dateFormat.getMaxLengthEstimate());
    }

    @Test
    public void testEquals() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertTrue("Expected the FastDateFormat objects to be equal", dateFormat1.equals(dateFormat2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        int hashCode1 = dateFormat1.hashCode();
        int hashCode2 = dateFormat2.hashCode();
        assertEquals("Expected the hash codes to be the same", hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String expectedToString = "FastDateFormat[dd-MM-yyyy, US, UTC]";
        assertEquals("Expected the toString method to return the correct String representation", expectedToString, dateFormat.toString());
    }
}