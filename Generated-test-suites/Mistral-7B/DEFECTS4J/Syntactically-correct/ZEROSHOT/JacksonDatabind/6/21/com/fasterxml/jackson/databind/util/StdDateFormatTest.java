package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.util.StdDateFormat;

public class StdDateFormatTest {

    @Test
    public void testStdDateFormat() {
        StdDateFormat dateFormat = StdDateFormat.instance;

        // Test parse method with valid dates
        Date date1 = dateFormat.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Expected valid date", new Date(1671561599123L), date1);

        Date date2 = dateFormat.parse("Sat, 31 Dec 2022 23:59:59 GMT");
        assertEquals("Expected valid date", new Date(1671561599000L), date2);

        Date date3 = dateFormat.parse("2022-12-31");
        assertEquals("Expected valid date", new Date(1671484800000L), date3);

        // Test parse method with invalid dates
        try {
            dateFormat.parse("Invalid date");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            dateFormat.parse("2022-12-32");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            dateFormat.parse("2022-12-31T23:59:60.123Z");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        // Test format method
        String formattedDate1 = dateFormat.format(new Date(1671561599123L));
        assertEquals("Expected valid formatted date", "2022-12-31T23:59:59.123Z", formattedDate1);

        String formattedDate2 = dateFormat.format(new Date(1671561599000L));
        assertEquals("Expected valid formatted date", "Sat, 31 Dec 2022 23:59:59 GMT", formattedDate2);

        String formattedDate3 = dateFormat.format(new Date(1671484800000L));
        assertEquals("Expected valid formatted date", "2022-12-31", formattedDate3);

        // Test withTimeZone method
        StdDateFormat dateFormatWithTimeZone = dateFormat.withTimeZone(TimeZone.getTimeZone("UTC+1"));
        Date dateWithTimeZone1 = dateFormatWithTimeZone.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Expected valid date", new Date(1671575299123L), dateWithTimeZone1);

        String formattedDateWithTimeZone1 = dateFormatWithTimeZone.format(new Date(1671575299123L));
        assertEquals("Expected valid formatted date", "2022-12-31T23:59:59.123+01:00", formattedDateWithTimeZone1);

        // Test withLocale method
        StdDateFormat dateFormatWithLocale = dateFormat.withLocale(Locale.UK);
        Date dateWithLocale1 = dateFormatWithLocale.parse("2022-12-31T23:59:59.123Z");
        assertEquals("Expected valid date", new Date(1671561599123L), dateWithLocale1);

        String formattedDateWithLocale1 = dateFormatWithLocale.format(new Date(1671561599123L));
        assertEquals("Expected valid formatted date", "Sat, 31 Dec 2022 23:59:59.123 GMT", formattedDateWithLocale1);
    }
}