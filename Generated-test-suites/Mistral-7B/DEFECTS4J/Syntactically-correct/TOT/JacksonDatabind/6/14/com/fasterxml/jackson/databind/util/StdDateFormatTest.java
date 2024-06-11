package com.fasterxml.jackson.databind.util.test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601WithMilliseconds() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.getInstance();
        Date date = dateFormat.parse("2022-01-01T12:34:56.789Z");
        assertEquals("2022-01-01 12:34:56.789", dateFormat.format(date));
    }

    @Test
    public void testParseISO8601WithoutMilliseconds() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.getInstance();
        Date date = dateFormat.parse("2022-01-01T12:34:56Z");
        assertEquals("2022-01-01 12:34:56", dateFormat.format(date));
    }

    @Test
    public void testParseISO8601WithTimezoneOffset() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.getInstance();
        Date date = dateFormat.parse("2022-01-01T12:34:56-05:00");
        assertEquals("2022-01-01 17:34:56", dateFormat.format(date));
    }

    @Test
    public void testParseISO8601WithTimezoneName() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.getInstance();
        Date date = dateFormat.parse("2022-01-01T12:34:56-05:00 America/New_York");
        assertEquals("2022-01-01 17:34:56", dateFormat.format(date));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        StdDateFormat dateFormat = StdDateFormat.getInstance();
        dateFormat.parse("InvalidDateString");
    }

    @Test
    public void testFormat() {
        StdDateFormat dateFormat = StdDateFormat.getInstance();
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
    }

    @Test
    public void testFormatWithLocale() {
        Locale locale = Locale.FRENCH;
        StdDateFormat dateFormat = new StdDateFormat(null, locale);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
        assertEquals("date : " + formattedDate, "date : " + dateFormat.format(new Date(0L)), dateFormat.toString());
    }

    @Test
    public void testFormatWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        StdDateFormat dateFormat = new StdDateFormat(timeZone, Locale.US);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        assertEquals(date, parsedDate);
        assertEquals("timezone : " + timeZone.getDisplayName(), "timezone : " + dateFormat.getTimeZone().getDisplayName(), dateFormat.toString());
    }

    @Test
    public void testClone() {
        StdDateFormat dateFormat = StdDateFormat.getInstance();
        StdDateFormat clonedDateFormat = dateFormat.clone();
        assertEquals(dateFormat.getClass(), clonedDateFormat.getClass());
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        Date parsedDate = dateFormat.parse(formattedDate);
        String clonedFormattedDate = clonedDateFormat.format(date);
        assertEquals(formattedDate, clonedFormattedDate);
        Date clonedParsedDate = clonedDateFormat.parse(clonedFormattedDate);
        assertEquals(parsedDate, clonedParsedDate);
    }
}