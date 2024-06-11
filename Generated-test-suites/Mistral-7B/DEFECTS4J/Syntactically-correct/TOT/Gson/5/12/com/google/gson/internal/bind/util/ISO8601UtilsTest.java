package com.google.gson.internal.bind.util;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    private ISO8601Utils utils;

    @Before
    public void setup() {
        utils = new ISO8601Utils();
    }

    @Test
    public void testFormat_DefaultTimeZone() {
        Date date = new GregorianCalendar(2022, 11, 25).getTime();
        String formattedDate = utils.format(date);
        String expected = "2022-11-25T00:00:00Z";
        assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormat_CustomTimeZone() {
        TimeZone customTimeZone = TimeZone.getTimeZone("GMT+2");
        Date date = new GregorianCalendar(2022, 11, 25).getTime();
        String formattedDate = utils.format(date, null, customTimeZone);
        String expected = "2022-11-25T02:00:00+02:00";
        assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormat_Milliseconds() {
        Date date = new GregorianCalendar(2022, 11, 25, 10, 30, 45, 123).getTime();
        String formattedDate = utils.format(date, true, null);
        String expected = "2022-11-25T10:30:45.123Z";
        assertEquals(expected, formattedDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormat_InvalidDate() {
        Date invalidDate = new Date(Long.MIN_VALUE);
        utils.format(invalidDate, false, null);
    }

    @Test
    public void testParse_DefaultTimeZone() throws ParseException {
        String dateString = "2022-11-25T00:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = utils.parse(dateString, pos);
        assertEquals("Expected date [2022-11-25]", parsedDate.toString());
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParse_CustomTimeZone() throws ParseException {
        String dateString = "2022-11-25T02:00:00+02:00";
        ParsePosition pos = new ParsePosition(0);
        TimeZone customTimeZone = TimeZone.getTimeZone("GMT+2");
        Date parsedDate = utils.parse(dateString, pos, customTimeZone);
        assertEquals("Expected date [2022-11-25]", parsedDate.toString());
        assertEquals(12, pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidDateFormat() throws ParseException {
        String invalidDateString = "2022-11-25T02:00:00+02:00:01";
        ParsePosition pos = new ParsePosition(0);
        utils.parse(invalidDateString, pos, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_NullInput() {
        ParsePosition pos = new ParsePosition(0);
        utils.parse(null, pos, null);
    }
}