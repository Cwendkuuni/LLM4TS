package com.fasterxml.jackson.databind.util;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdDateFormatTest {

    private StdDateFormat instance;

    @Before
    public void setup() {
        instance = new StdDateFormat();
    }

    @Test
    public void testParseISO8601Format() throws ParseException {
        DateFormat df = instance.withLocale(Locale.US).getISO8601Format(null, Locale.US);

        // Parse valid date strings
        String[] validDates = {"2022-01-01T12:00:00.123Z", "2022-01-01T12:00:00.123+01:00",
                "2022-01-01T12:00:00.123-05:00", "2022-01-01T12:00:00.123Z07:00"};
        for (String dateString : validDates) {
            Date parsedDate = df.parse(dateString);
            assertNotNull(parsedDate);
            assertEquals("Expected parsed date to match input string", dateString, df.format(parsedDate));
        }

        // Parse invalid date strings
        String[] invalidDates = {"2022-01-01T12:00:00.123", "2022-01-01T12:00:00.123ZA",
                "2022-01-01T12:00:00.123+01:00:01", "2022-01-01T12:00:00.123-05:00:60"};
        for (String dateString : invalidDates) {
            assertThrows(ParseException.class, () -> df.parse(dateString));
        }
    }

    @Test
    public void testParseRFC1123Format() throws ParseException {
        DateFormat df = instance.withLocale(Locale.US).getRFC1123Format(null, Locale.US);

        // Parse valid date strings
        String[] validDates = {"Mon, 01 Jan 2022 12:00:00 GMT", "Mon, 01 Jan 2022 12:00:00 +0100",
                "Mon, 01 Jan 2022 12:00:00 -0500"};
        for (String dateString : validDates) {
            Date parsedDate = df.parse(dateString);
            assertNotNull(parsedDate);
            assertEquals("Expected parsed date to match input string", dateString, df.format(parsedDate));
        }

        // Parse invalid date strings
        String[] invalidDates = {"Mon, 01 Jan 2022 12:00:00 GMTZ", "Mon, 01 Jan 2022 12:00:00 +0100:01",
                "Mon, 01 Jan 2022 12:00:00 -0500:60"};
        for (String dateString : invalidDates) {
            assertThrows(ParseException.class, () -> df.parse(dateString));
        }
    }

    @Test
    public void testParsePlainFormat() throws ParseException {
        DateFormat df = instance.withLocale(Locale.US).getPlainFormat(null, Locale.US);

        // Parse valid date strings
        String[] validDates = {"2022-01-01", "2022-01-01 12:00:00"};
        for (String dateString : validDates) {
            Date parsedDate = df.parse(dateString);
            assertNotNull(parsedDate);
            assertEquals("Expected parsed date to match input string", dateString, df.format(parsedDate));
        }

        // Parse invalid date strings
        String[] invalidDates = {"2022-01-01T12:00:00.123", "2022-01-01T12:00:00.123Z",
                "Mon, 01 Jan 2022 12:00:00 GMT"};
        for (String dateString : invalidDates) {
            assertThrows(ParseException.class, () -> df.parse(dateString));
        }
    }

    @Test
    public void testParseInvalidFormat() throws ParseException {
        DateFormat df = instance.getISO8601Format();

        // Parse invalid date strings
        assertThrows(ParseException.class, () -> df.parse("Invalid date string"));
    }

    @Test
    public void testParseWithNullFormat() {
        assertNull("Expected null when parsing with a null DateFormat", instance.parse("2022-01-01"));
    }

    @Test
    public void testFormat() {
        Date date = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 0, 0, 123).getTime();

        // Format using ISO-8601 format
        DateFormat dfISO8601 = instance.getISO8601Format(null, Locale.US);
        assertEquals("Expected formatted date to match expected string", "2022-01-01T12:00:00.123Z", dfISO8601.format(date));

        // Format using RFC-1123 format
        DateFormat dfRFC1123 = instance.getRFC1123Format(null, Locale.US);
        assertEquals("Expected formatted date to match expected string", "Mon, 01 Jan 2022 12:00:00 GMT", dfRFC1123.format(date));

        // Format using plain format
        DateFormat dfPlain = instance.getPlainFormat(null, Locale.US);
        assertEquals("Expected formatted date to match expected string", "2022-01-01", dfPlain.format(date));
    }
}