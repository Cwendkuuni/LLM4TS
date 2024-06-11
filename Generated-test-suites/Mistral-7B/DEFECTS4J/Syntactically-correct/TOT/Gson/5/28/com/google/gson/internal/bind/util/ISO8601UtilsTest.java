package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;

/**
 * Test class for ISO8601Utils
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date(0);

        // Test default format
        String formattedDefault = ISO8601Utils.format(date);
        assertEquals("1970-01-01T00:00:00Z", formattedDefault);

        // Test format with milliseconds
        String formattedWithMillis = ISO8601Utils.format(date, true);
        assertEquals("1970-01-01T00:00:00.000Z", formattedWithMillis);

        // Test format with custom timezone
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date customDate = customFormat.parse("2022-03-31 15:30:45");
        String formattedCustomTimezone = ISO8601Utils.format(customDate, false, TimeZone.getTimeZone("Europe/Paris"));
        assertEquals("2022-03-31T15:30:45+01:00", formattedCustomTimezone);
    }

    @Test
    public void testParse() {
        String dateString1 = "1970-01-01T00:00:00Z";
        Date parsedDate1 = null;
        try {
            parsedDate1 = ISO8601Utils.parse(dateString1, new ParsePosition(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals("Sun Jan 01 00:00:00 GMT 1970", new SimpleDateFormat("E MMM dd hh:mm:ss zzz yyyy").format(parsedDate1));

        String dateString2 = "2022-12-31T23:59:59.999Z";
        Date parsedDate2 = null;
        try {
            parsedDate2 = ISO8601Utils.parse(dateString2, new ParsePosition(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals("Sat Dec 31 23:59:59 GMT 2022", new SimpleDateFormat("E MMM dd hh:mm:ss zzz yyyy").format(parsedDate2));

        String dateString3 = "2022-12-31T23:59:59.999+01:00";
        Date parsedDate3 = null;
        try {
            parsedDate3 = ISO8601Utils.parse(dateString3, new ParsePosition(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals("Sun Dec 31 22:59:59 GMT 2022", new SimpleDateFormat("E MMM dd hh:mm:ss zzz yyyy").format(parsedDate3));

        String dateString4 = "2022-12-31T23:59:59.999-01:00";
        Date parsedDate4 = null;
        try {
            parsedDate4 = ISO8601Utils.parse(dateString4, new ParsePosition(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals("Mon Dec 31 23:59:59 GMT 2023", new SimpleDateFormat("E MMM dd hh:mm:ss zzz yyyy").format(parsedDate4));

        String dateString5 = "2022-12-31T23:59:59.999+02:30";
        Date parsedDate5 = null;
        try {
            parsedDate5 = ISO8601Utils.parse(dateString5, new ParsePosition(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals("Tue Dec 31 21:29:59 GMT 2022", new SimpleDateFormat("E MMM dd hh:mm:ss zzz yyyy").format(parsedDate5));
    }
}