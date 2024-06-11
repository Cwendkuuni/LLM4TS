package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.junit.platform.commons.util.ReflectionUtils;

/**
 * Test class for ISO8601Utils.
 */
public class ISO8601UtilsTest {

    @BeforeClass
    public static void setupClass() {
        // Empty
    }

    @AfterClass
    public static void teardownClass() {
        // Empty
    }

    @Before
    public void setup() {
        // Empty
    }

    @After
    public void teardown() {
        // Empty
    }

    /**
     * Test for format method with milliseconds and default timezone.
     */
    @Test
    public void testFormatDefaultTimezoneWithMilliseconds() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true);
        assertEquals("Expected format to match the expected string.",
                "yyyy-MM-ddThh:mm:ss.sssZ", formattedDate.length());
        assertEquals("Year should be correct.", date.getYear() + 1900,
                Integer.parseInt(formattedDate.substring(0, 4)));
        assertEquals("Month should be correct.", date.getMonth() + 1,
                Integer.parseInt(formattedDate.substring(5, 7)));
        assertEquals("Day of month should be correct.", date.getDate(),
                Integer.parseInt(formattedDate.substring(8, 10)));
        assertEquals("Hour should be correct.", date.getHours(),
                Integer.parseInt(formattedDate.substring(11, 13)));
        assertEquals("Minute should be correct.", date.getMinutes(),
                Integer.parseInt(formattedDate.substring(14, 16)));
        assertEquals("Second should be correct.", date.getSeconds(),
                Integer.parseInt(formattedDate.substring(17, 19)));
        assertEquals("Millisecond should be correct.", date.getMilliseconds(),
                Integer.parseInt(formattedDate.substring(20, 23)));
    }

    /**
     * Test for format method with milliseconds and specified timezone.
     */
    @Test
    public void testFormatSpecifiedTimezoneWithMilliseconds() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/Los_Angeles"));
        assertEquals("Expected format to match the expected string.",
                "yyyy-MM-ddThh:mm:ss.sss[+-]hh:mm", formattedDate.length());
        // The timezone information is not tested here, as it is assumed that the timezone information is correct in the TimeZone class.
        assertEquals("Year should be correct.", date.getYear() + 1900,
                Integer.parseInt(formattedDate.substring(0, 4)));
        assertEquals("Month should be correct.", date.getMonth() + 1,
                Integer.parsedInt(formattedDate.substring(5, 7)));
        assertEquals("Day of month should be correct.", date.getDate(),
                Integer.parsedInt(formattedDate.substring(8, 10)));
        assertEquals("Hour should be correct.", date.getHours(),
                Integer.parsedInt(formattedDate.substring(11, 13)));
        assertEquals("Minute should be correct.", date.getMinutes(),
                Integer.parsedInt(formattedDate.substring(14, 16)));
        assertEquals("Second should be correct.", date.getSeconds(),
                Integer.parsedInt(formattedDate.substring(17, 19)));
        assertEquals("Millisecond should be correct.", date.getMilliseconds(),
                Integer.parsedInt(formattedDate.substring(20, 23)));
        assertEquals("Offset hour should be correct.", TimeZone.getTimeZone("America/Los_Angeles").getOffset(date.getTime()) / (60 * 60),
                Integer.parsedInt(formattedDate.substring(24, 26)) / 60);
        assertEquals("Offset minute should be correct.", TimeZone.getTimeZone("America/Los_Angeles").getOffset(date.getTime()) / (60 * 60) % 60,
                Integer.parsedInt(formattedDate.substring(27, 29)));
    }

    /**
     * Test for format method without milliseconds and default timezone.
     */
    @Test
    public void testFormatDefaultTimezoneWithoutMilliseconds() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("Expected format to match the expected string.",
                "yyyy-MM-ddTHh:mm:ssZ", formattedDate.length());
        assertEquals("Year should be correct.", date.getYear() + 1900,
                Integer.parseInt(formattedDate.substring(0, 4)));
        assertEquals("Month should be correct.", date.getMonth() + 1,
                Integer.parseInt(formattedDate.substring(5, 7)));
        assertEquals("Day of month should be correct.", date.getDate(),
                Integer.parseInt(formattedDate.substring(8, 10)));
        assertEquals("Hour should be correct.", date.getHours(),
                Integer.parseInt(formattedDate.substring(11, 13)));
        assertEquals("Minute should be correct.", date.getMinutes(),
                Integer.parseInt(formattedDate.substring(14, 16)));
    }

    /**
     * Test for format method without milliseconds and specified timezone.
     */
    @Test
    public void testFormatSpecifiedTimezoneWithoutMilliseconds() {
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date, null, TimeZone.getTimeZone("America/Los_Angeles"));
        assertEquals("Expected format to match the expected string.",
                "yyyy-MM-ddTHh:mm:ss[+-]hh:mm", formattedDate.length());
        // The timezone information is not tested here, as it is assumed that the timezone information is correct in the TimeZone class.
        assertEquals("Year should be correct.", date.getYear() + 1900,
                Integer.parseInt(formattedDate.substring(0, 4)));
        assertEquals("Month should be correct.", date.getMonth() + 1,
                Integer.parsedInt(formattedDate.substring(5, 7)));
        assertEquals("Day of month should be correct.", date.getDate(),
                Integer.parsedInt(formattedDate.substring(8, 10)));
        assertEquals("Hour should be correct.", date.getHours(),
                Integer.parsedInt(formattedDate.substring(11, 13)));
        assertEquals("Minute should be correct.", date.getMinutes(),
                Integer.parsedInt(formattedDate.substring(14, 16)));
        assertEquals("Offset hour should be correct.", TimeZone.getTimeZone("America/Los_Angeles").getOffset(date.getTime()) / (60 * 60),
                Integer.parsedInt(formattedDate.substring(17, 19)) / 60);
        assertEquals("Offset minute should be correct.", TimeZone.getTimeZone("America/Los_Angeles").getOffset(date.getTime()) / (60 * 60) % 60,
                Integer.parsedInt(formattedDate.substring(18, 20)));
    }

    /**
     * Test for parse method with valid ISO 8601 formatted string.
     */
    @Test
    public void testParseValidString() {
        String iso8601Date = "2022-12-31T23:59:59.999Z";
        Date parsedDate = ISO8601Utils.parse(iso8601Date, new ParsePosition(0));
        Date expectedDate = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365 * 2 + 30));
        assertEquals("Expected parsed date to match the expected date.", expectedDate, parsedDate);
    }

    /**
     * Test for parse method with valid ISO 8601 formatted string with timezone.
     */
    @Test
    public void testParseValidStringWithTimezone() {
        String iso8601Date = "2022-12-31T23:59:59.999-08:00";
        Date parsedDate = ISO8601Utils.parse(iso8601Date, new ParsePosition(0));
        Date expectedDate = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365 * 2 + 30) - TimeUnit.HOURS.toMillis(8));
        assertEquals("Expected parsed date to match the expected date.", expectedDate, parsedDate);
    }

    /**
     * Test for parse method with invalid ISO 8601 formatted string without timezone.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidStringWithoutTimezone() {
        String iso8601Date = "2022-12-31T23:59:59Zzzz";
        ISO8601Utils.parse(iso8601Date, new ParsePosition(0));
    }

    /**
     * Test for parse method with invalid ISO 8601 formatted string with timezone.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidStringWithTimezone() {
        String iso8601Date = "2022-12-31T23:59:59.999-08:00zzz";
        ISO8601Utils.parse(iso8601Date, new ParsePosition(0));
    }
}