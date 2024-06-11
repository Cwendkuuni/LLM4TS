package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatUtcs() {
        Date utcDate = new Date(System.currentTimeMillis());
        String utcString = ISO8601Utils.format(utcDate, false, TimeZone.getTimeZone("UTC"));
        assertEquals("Expected UTC format to not include timezone offset", "yyyy-MM-dd'T'HH:mm:ss'Z'", utcString.substring(0, utcString.length() - 1));

        String utcStringWithMillis = ISO8601Utils.format(utcDate, true, TimeZone.getTimeZone("UTC"));
        assertEquals("Expected UTC format with milliseconds to include timezone offset", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", utcStringWithMillis.substring(0, utcStringWithMillis.length() - 1));
    }

    @Test
    public void testFormatOffsets() {
        Date utcDate = new Date(System.currentTimeMillis());

        // Create a test date in UTC and get the corresponding values for different offsets
        int hoursOffset = 2;
        int minutesOffset = 30;

        TimeZone timeZone = new TimeZone("Test TimeZone") {
            @Override
            public int getOffset(long time) {
                return hoursOffset * 60 * 60 * 1000 + minutesOffset * 60 * 1000; // 2 hours and 30 minutes offset from UTC
            }
        };

        String formattedDateWithOffset = ISO8601Utils.format(utcDate, false, timeZone);
        assertEquals("Expected ISO format without milliseconds to include timezone offset", "yyyy-MM-dd'T'HH:mm:ss'%s'", formattedDateWithOffset.substring(formattedDateWithOffset.length() - 2));
        assertEquals("Expected ISO format without milliseconds to include timezone offset (value)", String.format("Z%+02d:%02d", hoursOffset, minutesOffset), formattedDateWithOffset.substring(formattedDateWithOffset.length() - 7));

        String formattedDateWithMillisAndOffset = ISO8601Utils.format(utcDate, true, timeZone);
        assertEquals("Expected ISO format with milliseconds to include timezone offset", "yyyy-MM-dd'T'HH:mm:ss.SSS'%s'", formattedDateWithMillisAndOffset.substring(formattedDateWithMillisAndOffset.length() - 2));
        assertEquals("Expected ISO format with milliseconds to include timezone offset (value)", String.format("Z%+02d:%02d:%02d", hoursOffset, minutesOffset, 0), formattedDateWithMillisAndOffset.substring(formattedDateWithMillisAndOffset.length() - 9));
    }

    @Test
    public void testParseValidDates() throws Exception {
        String dateString1 = "2015-10-29T14:22:35Z";
        Date date1 = ISO8601Utils.parse(dateString1, new ParsePosition(0));
        assertEquals("Expected to parse a valid date string", new Date(1445843125000L), date1);

        String dateString2 = "2015-10-29T14:22:35.123Z";
        Date date2 = ISO8601Utils.parse(dateString2, new ParsePosition(0));
        assertEquals("Expected to parse a valid date string with milliseconds", new Date(1445843125123L), date2);

        String dateString3 = "2015-10-29T14:22:35+02:30";
        Date date3 = ISO8601Utils.parse(dateString3, new ParsePosition(0));
        assertEquals("Expected to parse a valid date string with a timezone offset", new Date(1445860525000L), date3);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDates() throws Exception {
        String dateString1 = "Invalid";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString1, pos);
        fail("Expected a ParseException to be thrown when parsing an invalid date string");
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws Exception {
        String dateString1 = "2015-10-29T14:22:35-"; // Missing 'Z' or '+' and hours/minutes for timezone offset
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString1, pos);
        fail("Expected a ParseException to be thrown when parsing an invalid date format without timezone offset");

        String dateString2 = "2015-10-29T14:22:35.123+-"; // Missing hours/minutes for timezone offset
        ParsePosition pos2 = new ParsePosition(0);
        ISO8601Utils.parse(dateString2, pos2);
        fail("Expected a ParseException to be thrown when parsing an invalid date format with an incorrect timezone offset");
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateValues() throws Exception {
        String dateString1 = "2015-13-29T14:22:35Z"; // Invalid month value
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString1, pos);
        fail("Expected a ParseException to be thrown when parsing an invalid date value for month");

        String dateString2 = "2015-02-32T14:22:35Z"; // Invalid day of month value
        ParsePosition pos2 = new ParsePosition(0);
        ISO8601Utils.parse(dateString2, pos2);
        fail("Expected a ParseException to be thrown when parsing an invalid date value for day of month");

        String dateString3 = "2015-02-29T14:22:35Z"; // Invalid day of month value (February has only 28 days)
        ParsePosition pos3 = new ParsePosition(0);
        ISO8601Utils.parse(dateString3, pos3);
        fail("Expected a ParseException to be thrown when parsing an invalid date value for day of month (leap year not considered)");

        String dateString4 = "2015-02-29T14:22:35+24:00"; // Invalid timezone offset value
        ParsePosition pos4 = new ParsePosition(0);
        ISO8601Utils.parse(dateString4, pos4);
        fail("Expected a ParseException to be thrown when parsing an invalid timezone offset value");
    }

    @Test
    public void testFormatAndParseRoundtrip() throws Exception {
        String inputDateString = "2015-10-29T14:22:35Z";
        Date inputDate = ISO8601Utils.parse(inputDateString, new ParsePosition(0));
        String outputDateString = ISO8601Utils.format(inputDate, false, TimeZone.getTimeZone("UTC"));

        Date outputDate = ISO8601Utils.parse(outputDateString, new ParsePosition(0));
        assertEquals("Expected the date to be equal when formatted and parsed", inputDate, outputDate);
    }
}