package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test for ISO8601Utils class.
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Testing format method with various input dates and timezones.
        Date date1 = new Date(1633059200000L); // Thu, 01 Jan 2021 12:00:00 GMT
        Date date2 = new Date(1633059200300L); // Thu, 01 Jan 2021 12:00:30 GMT
        Date date3 = new Date(1633059200300L); // Thu, 01 Jan 2021 12:00:30 EST
        Date date4 = new Date(1633145600000L); // Fri, 02 Jan 2021 00:00:00 GMT

        String formatResult1 = ISO8601Utils.format(date1);
        String formatResult2 = ISO8601Utils.format(date2, true);
        String formatResult3 = ISO8601Utils.format(date3, true, TimeZone.getTimeZone("America/New_York"));
        String formatResult4 = ISO8601Utils.format(date4, false, TimeZone.getTimeZone("Europe/London"));

        // Asserting format results are as expected.
        assertEquals("2021-01-01T12:00:00Z", formatResult1);
        assertEquals("2021-01-01T12:00:30.0Z", formatResult2);
        assertEquals("2021-01-01T17:00:30.0-05:00", formatResult3);
        assertEquals("2021-01-02T00:00:00Z", formatResult4);
    }

    @Test
    public void testParse() throws ParseException {
        // Testing parse method with various ISO-8601 formatted strings.
        String dateString1 = "2021-01-01T12:00:00Z";
        String dateString2 = "2021-01-01T12:00:30.0Z";
        String dateString3 = "2021-01-01T17:00:30.0-05:00";
        String dateString4 = "2021-01-02T00:00:00Z";

        ParsePosition parsePosition1 = new ParsePosition(0);
        Date parsedDate1 = ISO8601Utils.parse(dateString1, parsePosition1);
        ParsePosition parsePosition2 = new ParsePosition(0);
        Date parsedDate2 = ISO8601Utils.parse(dateString2, parsePosition2);
        ParsePosition parsePosition3 = new ParsePosition(0);
        Date parsedDate3 = ISO8601Utils.parse(dateString3, parsePosition3);
        ParsePosition parsePosition4 = new ParsePosition(0);
        Date parsedDate4 = ISO8601Utils.parse(dateString4, parsePosition4);

        // Asserting parsed dates are as expected.
        assertEquals(new Date(1633059200000L), parsedDate1);
        assertEquals(new Date(1633059230000L), parsedDate2);
        assertEquals(new Date(1633056730000L), parsedDate3);
        assertEquals(new Date(1633145600000L), parsedDate4);

        // Testing parse method with invalid ISO-8601 formatted strings.
        String invalidDateString1 = "2021-01-01T12:00:00X";
        String invalidDateString2 = "2021-01-01T12:00:30.X";
        String invalidDateString3 = "2021-01-01T17:00:30.0-05:01";
        String invalidDateString4 = "2021-01-02T00:00:00X";

        ParsePosition parsePositionInvalid1 = new ParsePosition(0);
        ParsePosition parsePositionInvalid2 = new ParsePosition(0);
        ParsePosition parsePositionInvalid3 = new ParsePosition(0);
        ParsePosition parsePositionInvalid4 = new ParsePosition(0);

        try {
            ISO8601Utils.parse(invalidDateString1, parsePositionInvalid1);
            fail("Expected ParseException with message: 'Invalid format: \"2021-01-01T12:00:00X\"'");
        } catch (ParseException e) {
            assertEquals("Invalid format: \"" + invalidDateString1 + "\"" , e.getMessage());
        }

        try {
            ISO8601Utils.parse(invalidDateString2, parsePositionInvalid2);
            fail("Expected ParseException with message: 'Invalid format: \"2021-01-01T12:00:30.X\"'");
        } catch (ParseException e) {
            assertEquals("Invalid format: \"" + invalidDateString2 + "\"" , e.getMessage());
        }

        try {
            ISO8601Utils.parse(invalidDateString3, parsePositionInvalid3);
            fail("Expected ParseException with message: 'Invalid format: \"2021-01-01T17:00:30.0-05:01\"'");
        } catch (ParseException e) {
            assertEquals("Invalid format: \"" + invalidDateString3 + "\"" , e.getMessage());
        }

        try {
            ISO8601Utils.parse(invalidDateString4, parsePositionInvalid4);
            fail("Expected ParseException with message: 'Invalid format: \"2021-01-02T00:00:00X\"'");
        } catch (ParseException e) {
            assertEquals("Invalid format: \"" + invalidDateString4 + "\"" , e.getMessage());
        }
    }
}