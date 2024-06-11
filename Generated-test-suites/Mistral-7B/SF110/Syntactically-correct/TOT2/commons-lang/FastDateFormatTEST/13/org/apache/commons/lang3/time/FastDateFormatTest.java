package org.apache.commons.lang3.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {
    @Test
    public void testGetInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertEquals("Default date format", fastDateFormat.getPattern());
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertEquals("Custom date format", "yyyy-MM-dd HH:mm:ss", fastDateFormat.getPattern());
    }

    // Add test cases for other methods here

    @Test
    public void testParseDateWithValidFormat() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        Date date = fastDateFormat.parse("2022-12-31 23:59:59");
        assertNotNull("Date should not be null", date);
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidFormat() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        fastDateFormat.parse("InvalidFormat");
    }

    @Test
    public void testFormatDateWithValidFormat() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        String formattedDate = fastDateFormat.format(new Date());
        assertNotNull("Formatted date should not be null", formattedDate);
    }

    @Test
    public void testFormatDateWithNullPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        String formattedDate = fastDateFormat.format(new Date());
        assertNotNull("Formatted date should not be null", formattedDate);
    }

    // Add test cases for other methods here
}