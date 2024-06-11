package org.apache.commons.lang3.time;

import static org.junit.Assert.*;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat fdf = FastDateFormat.getInstance();
        assertNotNull(fdf);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
        assertNotNull(fdf);
    }

    @Test
    public void testGetInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a", timeZone);
        assertNotNull(fdf);
        assertEquals(timeZone, fdf.getTimeZone());
    }

    @Test
    public void testGetInstanceWithLocale() {
        Locale locale = Locale.FRANCE;
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a", null, locale);
        assertNotNull(fdf);
        assertEquals(locale, fdf.getLocale());
    }

    @Test
    public void testGetInstanceWithAllParams() {
        Locale locale = Locale.FRANCE;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a", timeZone, locale);
        assertNotNull(fdf);
        assertEquals(timeZone, fdf.getTimeZone());
        assertEquals(locale, fdf.getLocale());
    }

    // Add test cases for other getInstance methods with different styles and combinations of parameters

    @Test
    public void testGetDateInstance() {
        FastDateFormat fdf = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
        assertNotNull(fdf);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.FRANCE;
        FastDateFormat fdf = FastDateFormat.getDateInstance(FastDateFormat.SHORT, locale);
        assertNotNull(fdf);
        assertEquals(locale, fdf.getLocale());
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fdf = FastDateFormat.getDateInstance(FastDateFormat.SHORT, timeZone);
        assertNotNull(fdf);
        assertEquals(timeZone, fdf.getTimeZone());
    }

    @Test
    public void testGetDateInstanceWithAllParams() {
        Locale locale = Locale.FRANCE;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fdf = FastDateFormat.getDateInstance(FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(fdf);
        assertEquals(timeZone, fdf.getTimeZone());
        assertEquals(locale, fdf.getLocale());
    }

    // Add test cases for other getInstance methods with different styles and combinations of parameters

    @Test
    public void testFormat() throws ParseException {
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
        Date date = new Date();
        String formattedDate = fdf.format(date);
        assertEquals(formattedDate, "17-09-2022 12:21 PM"); // Change the date and time according to the current system
    }

    @Test
    public void testFormatWithLocale() throws ParseException {
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a", Locale.FRANCE);
        Date date = new Date();
        String formattedDate = fdf.format(date);
        assertEquals(formattedDate, "17/09/2022 12:21 PM"); // Change the date and time according to the current system
    }

    @Test
    public void testFormatWithTimeZone() throws ParseException {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a", timeZone);
        Date date = new Date();
        String formattedDate = fdf.format(date);
        assertEquals(formattedDate, "17-09-2022 11:21 AM"); // Change the date and time according to the current system and UTC time zone offset
    }

    @Test
    public void testFormatWithAllParams() throws ParseException {
        Locale locale = Locale.FRANCE;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a", timeZone, locale);
        Date date = new Date();
        String formattedDate = fdf.format(date);
        assertEquals(formattedDate, "17/09/2022 11:21 AM"); // Change the date and time according to the current system and specified time zone and locale
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
        Date parsedDate = fdf.parse("17-09-2022 12:21 PM"); // Change the date and time according to the desired format
        assertNotNull(parsedDate);
        Date currentDate = new Date();
        long timeDifference = parsedDate.getTime() - currentDate.getTime();
        long expectedTimeDifference = 3600000L * 11 + 60000L * 21 - 3600000L * 12 - 60000L * 21; // Calculate the expected time difference between the parsed and current dates
        assertEquals(expectedTimeDifference, timeDifference);
    }

    @Test
    public void testParseWithLocale() throws ParseException {
        FastDateFormat fdf = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a", Locale.FRANCE);
        Date parsedDate = fdf.parse("17/09/2022 12:21 PM"); // Change the date and time according to the desired format
        assertNotNull(parsedDate);
        Date currentDate = new Date();
        long timeDifference = parsedDate.getTime() - currentDate.getTime();
        long expectedTimeDifference = 3600000L * 11 + 60000L * 21 - 3600000L * 12 - 60000L * 21; // Calculate the expected time difference between the parsed and current dates
        assertEquals(expectedTimeDifference, timeDifference);
    }

    @Test
    public void testParseWithTimeZone() throws ParseException {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a", timeZone);
        Date parsedDate = fdf.parse("17-09-2022 11:21 AM"); // Change the date and time according to the desired format and UTC time zone offset
        assertNotNull(parsedDate);
        Date currentDate = new Date();
        long timeDifference = parsedDate.getTime() - currentDate.getTime();
        long expectedTimeDifference = 3600000L * 10 + 60000L * 21 - 3600000L * 12 - 60000L * 21; // Calculate the expected time difference between the parsed and current dates considering the UTC time zone offset
        assertEquals(expectedTimeDifference, timeDifference);
    }

    @Test
    public void testParseWithAllParams() throws ParseException {
        Locale locale = Locale.FRANCE;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fdf = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a", timeZone, locale);
        Date parsedDate = fdf.parse("17/09/2022 11:21 AM"); // Change the date and time according to the desired format, time zone offset, and locale
        assertNotNull(parsedDate);
        Date currentDate = new Date();
        long timeDifference = parsedDate.getTime() - currentDate.getTime();
        long expectedTimeDifference = 3600000L * 10 + 60000L * 21 - 3600000L * 12 - 60000L * 21; // Calculate the expected time difference between the parsed and current dates considering the specified time zone offset and locale
        assertEquals(expectedTimeDifference, timeDifference);
    }

    // Add test cases for other parse methods with different styles and combinations of parameters

    @Test
    public void testGetPattern() {
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
        assertEquals("dd-MM-yyyy hh:mm a", fdf.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
        assertNull(fdf.getTimeZone());
    }

    @Test
    public void testGetLocale() {
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
        assertNull(fdf.getLocale());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
        int maxLengthEstimate = fdf.getMaxLengthEstimate();
        assertEquals(38, maxLengthEstimate);
    }

    @Test
    public void testEquals() {
        FastDateFormat fdf1 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
        FastDateFormat fdf2 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
        assertTrue(fdf1.equals(fdf2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
        int hashCode = fdf.hashCode();
        FastDateFormat fdf2 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
        int hashCode2 = fdf2.hashCode();
        assertEquals(hashCode, hashCode2);
    }

    @Test
    public void testToString() {
        FastDateFormat fdf = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
        String toString = fdf.toString();
        assertTrue(toString.contains("FastDateFormat[dd-MM-yyyy hh:mm a,"));
    }

    // Add test cases for other methods such as applyRules, getDateInstance, getDateTimeInstance, etc.

}