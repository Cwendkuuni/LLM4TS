package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date(1633059200000L); // Fri Jul 23 00:00:00 UTC 2021

        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2021-07-23T00:00:00Z", formattedDate);

        String formattedDateWithMilliseconds = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("2021-07-23T00:00:00.000Z", formattedDateWithMilliseconds);

        Date dateWithMillis = new Date(1633059200012L); // Fri Jul 23 00:00:00.012 UTC 2021
        String formattedDateWithMilliseconds2 = ISO8601Utils.format(dateWithMillis, true, TimeZone.getTimeZone("UTC"));
        assertEquals("2021-07-23T00:00:00.012Z", formattedDateWithMilliseconds2);
    }

    @Test
    public void testParse() throws ParseException {
        String isoDate = "2021-07-23T00:00:00Z";
        Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));
        assertNotNull(parsedDate);
        assertEquals("Fri Jul 23 00:00:00 UTC 2021", parsedDate.toString());

        String isoDateWithMilliseconds = "2021-07-23T00:00:00.012Z";
        Date parsedDateWithMilliseconds = ISO8601Utils.parse(isoDateWithMilliseconds, new ParsePosition(0));
        assertNotNull(parsedDateWithMilliseconds);
        assertEquals("Fri Jul 23 00:00:00.012 UTC 2021", parsedDateWithMilliseconds.toString());

        String isoDateWithOffset = "2021-07-23T00:00:00+02:00";
        Date parsedDateWithOffset = ISO8601Utils.parse(isoDateWithOffset, new ParsePosition(0));
        assertNotNull(parsedDateWithOffset);
        assertEquals("Fri Jul 23 00:00:00 EET 2021", parsedDateWithOffset.toString());

        String isoDateWithInvalidFormat = "2021-07-23T00:00:00Zabc";
        try {
            ISO8601Utils.parse(isoDateWithInvalidFormat, new ParsePosition(0));
            assert false : "Expected ParseException";
        } catch (ParseException e) {
            assertNotNull(e.getMessage());
            assertEquals("Text 'abc' could not be parsed at index 14", e.getMessage());
        }

        String isoDateWithMissingTimeComponent = "2021-07-23";
        try {
            ISO8601Utils.parse(isoDateWithMissingTimeComponent, new ParsePosition(0));
            assert false : "Expected ParseException";
        } catch (ParseException e) {
            assertNotNull(e.getMessage());
            assertEquals("Text '2021-07-23' is not a valid date", e.getMessage());
        }
    }
}