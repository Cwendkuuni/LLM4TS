package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws ParseException {
        DateFormat dateFormat = new StdDateFormat().withTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = "2022-03-14T15:26:37.123Z";
        Date parsedDate = dateFormat.parse(dateString);
        assertEquals("Unexpected parsed date", new GregorianCalendar(2022, Calendar.MARCH, 14, 15, 26, 37, 123).getTimeInMillis(), parsedDate.getTime());
    }

    @Test
    public void testParseISO8601WithTimeZoneOffset() throws ParseException {
        DateFormat dateFormat = new StdDateFormat().withTimeZone(TimeZone.getTimeZone("GMT+02:00"));
        String dateString = "2022-03-14T15:26:37.123+02:00";
        Date parsedDate = dateFormat.parse(dateString);
        assertEquals("Unexpected parsed date", new GregorianCalendar(2022, Calendar.MARCH, 14, 15, 26, 37, 123).getTimeInMillis() + (2 * 60 * 60 * 1000), parsedDate.getTime());
    }

    @Test
    public void testParseISOPlain() throws ParseException {
        DateFormat dateFormat = new StdDateFormat().withTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = "2022-03-14";
        Date parsedDate = dateFormat.parse(dateString);
        assertEquals("Unexpected parsed date", new GregorianCalendar(2022, Calendar.MARCH, 14).getTimeInMillis(), parsedDate.getTime());
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        DateFormat dateFormat = new StdDateFormat().withTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = "Monday, 14-Mar-22 15:26:37 GMT";
        Date parsedDate = dateFormat.parse(dateString);
        assertEquals("Unexpected parsed date", new GregorianCalendar(2022, Calendar.MARCH, 14, 15, 26, 37, 0).getTimeInMillis(), parsedDate.getTime());
    }

    @Test
    public void testParseInvalidFormat1() throws ParseException {
        DateFormat dateFormat = new StdDateFormat().withTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = "2022-03-14T15:26:37";
        assertNull("Date should not be parsed", dateFormat.parse(dateString));
    }

    @Test
    public void testParseInvalidFormat2() throws ParseException {
        DateFormat dateFormat = new StdDateFormat().withTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = "Monday, 14-Mar-22 15:26:37 GMT+";
        assertNull("Date should not be parsed", dateFormat.parse(dateString));
    }

    @Test
    public void testParseInvalidFormat3() throws ParseException {
        DateFormat dateFormat = new StdDateFormat().withTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = "Monday, 14-Mar-22 15:26:37";
        assertNull("Date should not be parsed", dateFormat.parse(dateString));
    }

    @Test
    public void testParseInvalidFormat4() throws ParseException {
        DateFormat dateFormat = new StdDateFormat().withTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = "2022-03-14T15:26:37.123";
        assertNull("Date should not be parsed", dateFormat.parse(dateString));
    }
}