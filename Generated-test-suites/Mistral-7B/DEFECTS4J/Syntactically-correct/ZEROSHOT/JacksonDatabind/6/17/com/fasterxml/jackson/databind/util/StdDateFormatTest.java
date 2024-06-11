package com.fasterxml.jackson.databind.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601Format() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String iso8601Date = "2022-12-31T23:59:59.123Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = dateFormat.parse(iso8601Date, pos);
        Assert.assertNotNull(date);
        Assert.assertEquals("Wrong date parsed", "Fri Dec 31 23:59:59 GMT", date.toString());
    }

    @Test
    public void testParseRFC1123Format() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String rfc1123Date = "Fri, 31 Dec 2022 23:59:59 GMT";
        ParsePosition pos = new ParsePosition(0);
        Date date = dateFormat.parse(rfc1123Date, pos);
        Assert.assertNotNull(date);
        Assert.assertEquals("Wrong date parsed", "Fri Dec 31 23:59:59 GMT", date.toString());
    }

    @Test
    public void testParsePlainFormat() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String plainDate = "2022-12-31";
        ParsePosition pos = new ParsePosition(0);
        Date date = dateFormat.parse(plainDate, pos);
        Assert.assertNotNull(date);
        Assert.assertEquals("Wrong date parsed", "Fri Dec 31 00:00:00 GMT", date.toString());
    }

    @Test(expected = ParseException.class)
    public void testInvalidFormat() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        String invalidDate = "InvalidDate";
        ParsePosition pos = new ParsePosition(0);
        dateFormat.parse(invalidDate, pos);
    }

    @Test
    public void testParseDateWithTimeZone() throws Exception {
        StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("Europe/Paris"), Locale.FRANCE);
        String iso8601Date = "2022-12-31T23:59:59.123+01:00";
        ParsePosition pos = new ParsePosition(0);
        Date date = dateFormat.parse(iso8601Date, pos);
        Assert.assertNotNull(date);
        Assert.assertEquals("Wrong date parsed for timezone Paris", "Sat Dec 31 22:59:59 CET Europe Standard Time 2022", date.toString());
    }

    @Test
    public void testFormatDate() throws Exception {
        StdDateFormat dateFormat = StdDateFormat.instance;
        Date date = new Date();
        String iso8601Format = dateFormat.format(date);
        Assert.assertEquals("Wrong date format", "Sat Jul 22 19:45:33 GMT 2023", iso8601Format);

        String rfc1123Format = dateFormat.format(date, new StringBuffer(), new FieldPosition(0));
        Assert.assertEquals("Wrong date format", "Sat, 22 Jul 2023 19:45:33 GMT", rfc1123Format.toString());
    }
}