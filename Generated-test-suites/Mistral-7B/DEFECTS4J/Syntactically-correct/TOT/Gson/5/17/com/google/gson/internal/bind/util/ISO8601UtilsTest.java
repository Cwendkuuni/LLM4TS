package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * ISO8601UtilsTest.java
 */
public class ISO8601UtilsTest
{
    /**
     * Test formatting
     */
    @Test
    public void testFormat()
    {
        Date date = new GregorianCalendar(2018, 11, 30, 15, 20, 30, 500, TimeZone.getTimeZone("UTC")).getTime();

        String dateFormatted = ISO8601Utils.format(date);
        String expected = "2018-12-31T15:20:30.500Z";
        assertEquals(expected, dateFormatted);

        date = new GregorianCalendar(2018, 11, 30, 15, 20, 30, 500, TimeZone.getTimeZone("Europe/Paris")).getTime();
        dateFormatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("Europe/Paris"));
        expected = "2018-12-31T15:20:30.500+01:00";
        assertEquals(expected, dateFormatted);
    }

    /**
     * Test parsing
     */
    @Test
    public void testParse() throws ParseException
    {
        String dateString = "2018-12-31T15:20:30.500Z";
        ParsePosition pos = new ParsePosition(0);
        Date dateParsed = ISO8601Utils.parse(dateString, pos);
        assertEquals(new GregorianCalendar(2018, 11, 30, 15, 20, 30, 500, TimeZone.getTimeZone("UTC")).getTime(), dateParsed.getTime());
        assertEquals(pos.getIndex(), dateString.length());

        dateString = "2018-12-31T15:20:30+01:00";
        pos = new ParsePosition(0);
        dateParsed = ISO8601Utils.parse(dateString, pos);
        assertEquals(new GregorianCalendar(2018, 11, 30, 15, 20, 30, 500, TimeZone.getTimeZone("Europe/Paris")).getTime(), dateParsed.getTime());
        assertEquals(pos.getIndex(), dateString.length());

        try {
            ISO8601Utils.parse("Invalid", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        try {
            ISO8601Utils.parse("2018-12-32", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        try {
            ISO8601Utils.parse("2018-12-31T15:20:61", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        try {
            ISO8601Utils.parse("2018-12-31T15:20:30.500+10:60", new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    /**
     * Test formatting and parsing round trip
     */
    @Test
    public void testRoundTrip() throws ParseException
    {
        Date date = new GregorianCalendar(2018, 11, 30, 15, 20, 30, 500, TimeZone.getTimeZone("UTC")).getTime();
        String dateFormatted = ISO8601Utils.format(date);
        Date dateParsed = ISO8601Utils.parse(dateFormatted, new ParsePosition(0));
        assertEquals(date, dateParsed);

        date = new GregorianCalendar(2018, 11, 30, 15, 20, 30, 500, TimeZone.getTimeZone("Europe/Paris")).getTime();
        dateFormatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("Europe/Paris"));
        dateParsed = ISO8601Utils.parse(dateFormatted, new ParsePosition(0));
        assertEquals(date, dateParsed);
    }

    /**
     * Test parsing with wrong timezone indicator
     */
    @Test
    public void testParseWithWrongTimezoneIndicator() throws ParseException
    {
        try {
            String dateString = "2018-12-31T15:20:30+10:60"; // wrong timezone indicator
            Date dateParsed = ISO8601Utils.parse(dateString, new ParsePosition(0));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }
}