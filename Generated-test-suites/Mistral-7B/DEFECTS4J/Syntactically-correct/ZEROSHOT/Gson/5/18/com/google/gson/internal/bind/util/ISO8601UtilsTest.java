package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test file for ISO8601Utils class
 */
public class ISO8601UtilsTest {

    /**
     * Test format method
     */
    @Test
    public void testFormat() {
        Date date = new GregorianCalendar(2023, Calendar.APRIL, 12).getTime();

        String expected = "2023-04-12T15:26:37.123Z";
        String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals(expected, formatted);

        expected = "2023-04-12T15:26:37Z";
        formatted = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        assertEquals(expected, formatted);

        expected = "2023-04-12T15:26:37+01:00";
        formatted = ISO8601Utils.format(date, false, new SimpleTimeZone(60 * 60 * 1000, "CET"));
        assertEquals(expected, formatted);
    }

    /**
     * Test parse method
     */
    @Test
    public void testParse() throws Exception {
        String isoDate = "2023-04-12T15:26:37.123Z";

        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals("Expected and parsed dates are not equal.", new GregorianCalendar(2023, Calendar.APRIL, 12, 15, 26, 37, 123).getTime(), parsedDate);
        assertEquals("Parse position should be the length of the string.", isoDate.length(), pos.getIndex());
    }
}