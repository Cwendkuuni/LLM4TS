package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * ISO8601Utils test cases.
 */
public class ISO8601UtilsTest {

    /**
     * Test formatting and parsing of basic ISO8601 formatted dates.
     *
     * @throws ParseException if the test case fails
     */
    @Test
    public void testBasicISO8601() throws ParseException {
        // Given
        Date date = new Date(1546300800000L); // 2019-01-01T00:00:00Z

        // When
        String formattedDate = ISO8601Utils.format(date);

        // Then
        assertEquals("2019-01-01T00:00:00Z", formattedDate);

        // Given
        String isoDate = "2019-01-01T00:00:00Z";

        // When
        Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));

        // Then
        assertEquals(date, parsedDate);

        // Given
        Date invalidDate = new Date(1546300800001L); // 2019-01-01T00:00:00.00001Z (invalid millisecond)

        // When / Then
        try {
            String formattedInvalidDate = ISO8601Utils.format(invalidDate);
            fail("Expected IllegalArgumentException when formatting invalid date.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Date parsedInvalidDate = ISO8601Utils.parse("2019-01-01T00:00:00.00001Z", new ParsePosition(0));
            fail("Expected ParseException when parsing invalid date.");
        } catch (ParseException e) {
            // expected
        }

        // Given
        String invalidFormatDate = "2019-01-01-01:01:01.01Z"; // invalid format

        // When / Then
        try {
            Date parsedInvalidFormatDate = ISO8601Utils.parse(invalidFormatDate, new ParsePosition(0));
            fail("Expected ParseException when parsing invalid format date.");
        } catch (ParseException e) {
            // expected
        }

        try {
            String formattedInvalidFormatDate = ISO8601Utils.format(new Date(1546300800000L), new ParsePosition(0));
            fail("Expected IllegalArgumentException when formatting invalid format date.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Given
        String timeZoneOffsetDate = "2019-01-01T00:00:00+01:00";

        // When
        Date parsedTimeZoneOffsetDate = ISO8601Utils.parse(timeZoneOffsetDate, new ParsePosition(0));

        // Then
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        Calendar calendar = new GregorianCalendar(timeZone);
        calendar.setTime(parsedTimeZoneOffsetDate);
        assertEquals(2019, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH) + 1);
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(60 * 60 * 1000, calendar.get(Calendar.MILLISECOND));

        // Given
        String timeZoneDate = "2019-01-01T00:00:00-05:00";

        // When
        Date parsedTimeZoneDate = ISO8601Utils.parse(timeZoneDate, new ParsePosition(0));

        // Then
        TimeZone timeZoneDateTimeZone = TimeZone.getTimeZone("America/New_York");
        Calendar calendarTimeZoneDate = new GregorianCalendar(timeZoneDateTimeZone);
        calendarTimeZoneDate.setTime(parsedTimeZoneDate);
        assertEquals(2019, calendarTimeZoneDate.get(Calendar.YEAR));
        assertEquals(1, calendarTimeZoneDate.get(Calendar.MONTH) + 1);
        assertEquals(1, calendarTimeZoneDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, calendarTimeZoneDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendarTimeZoneDate.get(Calendar.MINUTE));
        assertEquals(0, calendarTimeZoneDate.get(Calendar.SECOND));
        assertEquals(-5 * 60 * 60 * 1000, calendarTimeZoneDate.get(Calendar.MILLISECOND));

        // Given
        String millisDate = "2019-01-01T00:00:00.123Z";

        // When
        Date parsedMillisDate = ISO8601Utils.parse(millisDate, new ParsePosition(0));

        // Then
        assertEquals(millisDate, ISO8601Utils.format(parsedMillisDate, true, TimeZone.getTimeZone("UTC")));

        // Given
        String millisDate2 = "2019-01-01T00:00:00.12345Z";

        // When
        Date parsedMillisDate2 = ISO8601Utils.parse(millisDate2, new ParsePosition(0));

        // Then
        assertEquals(millisDate2, ISO8601Utils.format(parsedMillisDate2, true, TimeZone.getTimeZone("UTC")));

        // Given
        String millisDate3 = "2019-01-01T00:00:00.123456Z";

        // When
        Date parsedMillisDate3 = ISO8601Utils.parse(millisDate3, new ParsePosition(0));

        // Then
        assertEquals(millisDate3, ISO8601Utils.format(parsedMillisDate3, true, TimeZone.getTimeZone("UTC")));
    }

    /**
     * Test formatting and parsing of ISO8601 formatted dates with timezones and milliseconds precision.
     *
     * @throws ParseException if the test case fails
     */
    @Test
    public void testISO8601WithTimeZoneAndMillis() throws ParseException {
        // Given
        Date date = new Date(1546300800012L); // 2019-01-01T00:00:00.123Z

        // When
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));

        // Then
        assertEquals("2019-01-01T00:00:00.123Z", formattedDate);

        // Given
        String isoDate = "2019-01-01T00:00:00.123Z";

        // When
        Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));

        // Then
        assertEquals(date, parsedDate);

        // Given
        Date invalidDate = new Date(1546300800013L); // 2019-01-01T00:00:00.123.1Z (invalid millisecond)

        // When / Then
        try {
            String formattedInvalidDate = ISO8601Utils.format(invalidDate, true, TimeZone.getTimeZone("UTC"));
            fail("Expected IllegalArgumentException when formatting invalid date.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Date parsedInvalidDate = ISO8601Utils.parse("2019-01-01T00:00:00.123.1Z", new ParsePosition(0));
            fail("Expected ParseException when parsing invalid date.");
        } catch (ParseException e) {
            // expected
        }

        // Given
        String invalidFormatDate = "2019-01-01T00:00:00.123.1Z"; // invalid format

        // When / Then
        try {
            Date parsedInvalidFormatDate = ISO8601Utils.parse(invalidFormatDate, new ParsePosition(0));
            fail("Expected ParseException when parsing invalid format date.");
        } catch (ParseException e) {
            // expected
        }

        try {
            String formattedInvalidFormatDate = ISO8601Utils.format(new Date(1546300800000L), new ParsePosition(0));
            fail("Expected IllegalArgumentException when formatting invalid format date.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Given
        String timeZoneOffsetDate = "2019-01-01T00:00:00.123+01:00";

        // When
        Date parsedTimeZoneOffsetDate = ISO8601Utils.parse(timeZoneOffsetDate, new ParsePosition(0));

        // Then
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        Calendar calendar = new GregorianCalendar(timeZone);
        calendar.setTime(parsedTimeZoneOffsetDate);
        assertEquals(2019, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH) + 1);
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(60 * 60 * 1000 + 123, calendar.get(Calendar.MILLISECOND));

        // Given
        String timeZoneDate = "2019-01-01T00:00:00.123-05:00";

        // When
        Date parsedTimeZoneDate = ISO8601Utils.parse(timeZoneDate, new ParsePosition(0));

        // Then
        TimeZone timeZoneDateTimeZone = TimeZone.getTimeZone("America/New_York");
        Calendar calendarTimeZoneDate = new GregorianCalendar(timeZoneDateTimeZone);
        calendarTimeZoneDate.setTime(parsedTimeZoneDate);
        assertEquals(2019, calendarTimeZoneDate.get(Calendar.YEAR));
        assertEquals(1, calendarTimeZoneDate.get(Calendar.MONTH) + 1);
        assertEquals(1, calendarTimeZoneDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, calendarTimeZoneDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendarTimeZoneDate.get(Calendar.MINUTE));
        assertEquals(0, calendarTimeZoneDate.get(Calendar.SECOND));
        assertEquals(-5 * 60 * 60 * 1000 + 123, calendarTimeZoneDate.get(Calendar.MILLISECOND));

        // Given
        String millisDate = "2019-01-01T00:00:00.123456+01:00";

        // When
        Date parsedMillisDate = ISO8601Utils.parse(millisDate, new ParsePosition(0));

        // Then
        assertEquals(millisDate, ISO8601Utils.format(parsedMillisDate, true, TimeZone.getTimeZone("UTC")));

        // Given
        String millisDate2 = "2019-01-01T00:00:00.1234567+01:00";

        // When
        Date parsedMillisDate2 = ISO8601Utils.parse(millisDate2, new ParsePosition(0));

        // Then
        assertEquals(millisDate2, ISO8601Utils.format(parsedMillisDate2, true, TimeZone.getTimeZone("UTC")));

        // Given
        String millisDate3 = "2019-01-01T00:00:00.123456789+01:00";

        // When
        Date parsedMillisDate3 = ISO8601Utils.parse(millisDate3, new ParsePosition(0));

        // Then
        assertEquals(millisDate3, ISO8601Utils.format(parsedMillisDate3, true, TimeZone.getTimeZone("UTC")));
    }
}