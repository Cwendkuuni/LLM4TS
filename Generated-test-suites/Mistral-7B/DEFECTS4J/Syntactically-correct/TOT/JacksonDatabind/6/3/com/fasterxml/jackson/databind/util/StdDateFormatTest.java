package com.fasterxml.jackson.databind.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.TimeZone;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() throws ParseException {
        DateFormat format = new StdDateFormat().withTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = "2022-12-31T23:59:59.123Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = format.parse(dateString, pos);
        Assert.assertNotNull(parsedDate);
        Assert.assertEquals("Unexpected offset in parsed date", 0, parsedDate.getTime() % (24 * 60 * 60 * 1000));
        Assert.assertEquals(dateString, format.format(parsedDate));
    }

    @Test
    public void testParseISO8601WithTimezone() throws ParseException {
        DateFormat format = new StdDateFormat().withTimeZone(TimeZone.getTimeZone("+01:00"));
        String dateString = "2022-12-31T23:59:59.123Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = format.parse(dateString, pos);
        Assert.assertNotNull(parsedDate);
        Assert.assertEquals("Unexpected offset in parsed date", 3600000, parsedDate.getTime() % (24 * 60 * 60 * 1000));
        Assert.assertEquals(dateString, format.format(parsedDate));
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        DateFormat format = new StdDateFormat().withTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = "Thu, 31 Dec 2022 23:59:59 GMT";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = format.parse(dateString, pos);
        Assert.assertNotNull(parsedDate);
        Assert.assertEquals("Unexpected offset in parsed date", 0, parsedDate.getTime() % (24 * 60 * 60 * 1000));
        Assert.assertEquals(dateString, format.format(parsedDate));
    }

    @Test
    public void testParseRFC1123WithTimezone() throws ParseException {
        DateFormat format = new StdDateFormat().withTimeZone(TimeZone.getTimeZone("+01:00"));
        String dateString = "Thu, 31 Dec 2022 23:59:59 GMT";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = format.parse(dateString, pos);
        Assert.assertNotNull(parsedDate);
        Assert.assertEquals("Unexpected offset in parsed date", 3600000, parsedDate.getTime() % (24 * 60 * 60 * 1000));
        Assert.assertEquals(dateString, format.format(parsedDate));
    }

    @Test
    public void testParseInvalidFormat() throws ParseException {
        DateFormat format = new StdDateFormat().withTimeZone(TimeZone.getDefault());
        String dateString = "InvalidDateFormat";
        ParsePosition pos = new ParsePosition(0);
        try {
            format.parse(dateString, pos);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
        Assert.assertEquals("Expected 'InvalidDateFormat' in error message", dateString, e.getMessage());
    }

    @Test
    public void testFormat() {
        DateFormat format = new StdDateFormat().withTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        String formattedDate = format.format(date);
        Assert.assertNotNull(formattedDate);
        Date parsedDate = format.parse(formattedDate);
        Assert.assertEquals("Parsed date should be equal to original date", date, parsedDate);
    }

    @Test
    public void testFormatWithTimezone() {
        DateFormat format = new StdDateFormat().withTimeZone(TimeZone.getTimeZone("+01:00"));
        Date date = new Date();
        String formattedDate = format.format(date);
        Assert.assertNotNull(formattedDate);
        Date parsedDate = format.parse(formattedDate);
        Assert.assertEquals("Parsed date should be equal to original date", date, parsedDate);
    }
}