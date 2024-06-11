package com.fasterxml.jackson.databind.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class StdDateFormatTest {

    @Test
    public void testStdDateFormat() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;

        // Test parsing using default format (ISO-8601)
        Date date1 = stdDateFormat.parse("2022-12-31T23:59:59.999Z");
        Assert.assertNotNull(date1);
        Assert.assertEquals(2022, date1.getYear());
        Assert.assertEquals(11, date1.getMonth());
        Assert.assertEquals(31, date1.getDate());
        Assert.assertEquals(23, date1.getHours());
        Assert.assertEquals(59, date1.getMinutes());
        Assert.assertEquals(59, date1.getSeconds());
        Assert.assertEquals(999, date1.getMilliseconds());
        Assert.assertEquals(0, date1.getTimezoneOffset());

        // Test parsing using RFC-1123 format
        DateFormat rfc1123Format = stdDateFormat.getRFC1123Format(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date2 = rfc1123Format.parse("Sat, 31 Dec 2022 23:59:59 UTC");
        Assert.assertNotNull(date2);
        Assert.assertEquals(2022, date2.getYear());
        Assert.assertEquals(11, date2.getMonth());
        Assert.assertEquals(31, date2.getDate());
        Assert.assertEquals(23, date2.getHours());
        Assert.assertEquals(59, date2.getMinutes());
        Assert.assertEquals(59, date2.getSeconds());
        Assert.assertEquals(0, date2.getMilliseconds());
        Assert.assertEquals(0, date2.getTimezoneOffset());

        // Test parsing using plain format
        DateFormat plainFormat = stdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.US);
        Date date3 = plainFormat.parse("2022-12-31");
        Assert.assertNotNull(date3);
        Assert.assertEquals(2022, date3.getYear());
        Assert.assertEquals(11, date3.getMonth());
        Assert.assertEquals(31, date3.getDate());
        Assert.assertEquals(0, date3.getHours());
        Assert.assertEquals(0, date3.getMinutes());
        Assert.assertEquals(0, date3.getSeconds());
        Assert.assertEquals(0, date3.getMilliseconds());
        Assert.assertEquals(0, date3.getTimezoneOffset());

        // Test formatting using default format (ISO-8601)
        String formattedDate1 = stdDateFormat.format(date1, new StringBuffer(), new FieldPosition(0, 0));
        Assert.assertEquals("2022-12-31T23:59:59.999Z", formattedDate1.toString());

        // Test formatting using RFC-1123 format
        String formattedDate2 = rfc1123Format.format(date2, new StringBuffer(), new FieldPosition(0, 0));
        Assert.assertEquals("Sat, 31 Dec 2022 23:59:59 UTC", formattedDate2.toString());

        // Test formatting using plain format
        String formattedDate3 = plainFormat.format(date3, new StringBuffer(), new FieldPosition(0, 0));
        Assert.assertEquals("2022-12-31", formattedDate3.toString());
    }
}