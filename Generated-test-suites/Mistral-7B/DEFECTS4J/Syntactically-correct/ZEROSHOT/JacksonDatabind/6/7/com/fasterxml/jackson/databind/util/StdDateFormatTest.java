package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testStdDateFormat() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;

        // Test parsing with default format
        Date date1 = stdDateFormat.parse("2022-12-31T23:59:59.000Z");
        assertEquals("Invalid date parsed", new Date(1671568000000L), date1);

        // Test parsing with specific format
        StdDateFormat standardFormatWithTimeZone = stdDateFormat.withTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        Date date2 = standardFormatWithTimeZone.parse("2022-12-31T23:59:59.000Z");
        assertEquals("Invalid date parsed with timezone", new Date(1671568000000L + 3600000), date2);

        // Test parsing with specific locale
        StdDateFormat standardFormatWithLocale = stdDateFormat.withLocale(Locale.CHINESE);
        Date date3 = standardFormatWithLocale.parse("2022-12-31T23:59:59.000Z");
        assertEquals("Invalid date parsed with locale", new Date(1671568000000L), date3);

        // Test format method
        StringDateFormat stringDateFormat = new StringDateFormat();
        String formattedDate = stringDateFormat.format(new Date(1671568000000L), new StringBuffer(), null);
        assertEquals("Invalid formatted date", "2022-12-31T23:59:59.000Z", formattedDate);

        // Test parse method with incorrect format
        try {
            stdDateFormat.parse("InvalidDateFormat");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected ParseException
        }

        // Test parse method with empty string
        try {
            stdDateFormat.parse("");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected ParseException
        }
    }

    private class StringDateFormat extends DateFormat {

        @Override
        public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
            return new StringBuffer(StdDateFormat.instance.format(date));
        }
    }
}