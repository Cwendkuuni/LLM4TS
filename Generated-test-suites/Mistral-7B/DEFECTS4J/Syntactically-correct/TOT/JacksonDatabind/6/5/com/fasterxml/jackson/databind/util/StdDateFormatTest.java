package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws ParseException {
        StdDateFormat df = StdDateFormat.getInstance();

        // Basic test: no timezone
        Date date1 = df.parse("2022-12-31");
        assertEquals("2022-12-31", df.format(date1));

        // Test with timezone
        Date date2 = df.parse("2022-12-31T12:00:00.000Z");
        assertEquals("2022-12-31T12:00:00.000Z", df.format(date2));

        // Test with timezone offset
        Date date3 = df.parse("2022-12-31T12:00:00.000-05:00");
        assertEquals("2022-12-31T17:00:00.000-05:00", df.format(date3));

        // Test with invalid date
        assertNull(df.parse("2022-12-32"));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        StdDateFormat df = StdDateFormat.getInstance();
        df.parse("InvalidFormat");
    }

    @Test
    public void testFormat() {
        StdDateFormat df = StdDateFormat.getInstance();
        Date date = new Date();

        String formattedDate = df.format(date);
        assertTrue(formattedDate.matches(".*[+-][0-9][0-9]:[0-9][0-9]:[0-9][0-9].[0-9]{3}[Zz]?.*"));

        String formattedDateWithLocale = df.format(date, Locale.FRENCH);
        assertTrue(formattedDateWithLocale.matches(".*[a-z]{3}, [a-z]{3} [a-z]+ [0-9]{4} [h:i]:[m:s]:[s]{3} [z]{1,2}.*"));

        String formattedDateWithTimeZone = df.format(date, new TimeZone("UTC+1"), Locale.US);
        assertTrue(formattedDateWithTimeZone.matches(".*[+-][0-9][0-9]:[0-9][0-9]:[0-9][0-9].[0-9]{3} [+]01:[0-9][0-9].*"));
    }
}