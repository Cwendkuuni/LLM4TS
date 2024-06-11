package com.fasterxml.jackson.databind.util;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for StdDateFormat.
 */
public class StdDateFormatTest {

    @Test
    public void testStdDateFormat() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;

        // Test getBlueprintISO8601Format() deprecated method
        @SuppressWarnings("deprecation")
        DateFormat blueprintISO8601Format = StdDateFormat.getBlueprintISO8601Format();
        assertEquals("Blueprint ISO8601 Format should be same", stdDateFormat.getISO8601Format(null, null), blueprintISO8601Format);

        // Test getISO8601Format() method
        DateFormat iso8601Format = stdDateFormat.getISO8601Format(null, null);
        Date date1 = iso8601Format.parse("2023-03-01T14:25:06.789Z");
        assertEquals("Date parse should be successful", "Thu Mar 01 14:25:06 GMT", iso8601Format.format(date1));

        // Test getRFC1123Format() method
        DateFormat rfc1123Format = stdDateFormat.getRFC1123Format(null, null);
        Date date2 = rfc1123Format.parse("Wed, 01 Mar 2023 14:25:06 GMT");
        assertEquals("Date parse should be successful", "Wed Mar 01 14:25:06 GMT", rfc1123Format.format(date2));

        // Test parse() method
        Date date3 = stdDateFormat.parse("2023-03-01T14:25:06.789Z");
        assertEquals("Date parse should be successful", date1, date3);

        TryParsingException exception = assertThrows(ParseException.class, () -> {
            stdDateFormat.parse("InvalidDate");
        });
        String expectedMessage = "Can not parse date \"InvalidDate\": not compatible with any of standard forms (\"yyyy-MM-dd'T'HH:mm:ss.SSSZ\", \"EEE, dd MMM yyyy HH:mm:ss zzz\")";
        String actualMessage = exception.getMessage();
        assertTrue("Expected message: " + expectedMessage, actualMessage.startsWith(expectedMessage));

        // Test clone() method
        StdDateFormat clonedStdDateFormat = (StdDateFormat) stdDateFormat.clone();
        Date date4 = clonedStdDateFormat.parse("2023-03-01T14:25:06.789Z");
        assertEquals("Date parse should be successful in cloned object", date1, date4);

        // Test withTimeZone() method
        StdDateFormat timeZoneStdDateFormat = stdDateFormat.withTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        DateFormat timeZoneISO8601Format = timeZoneStdDateFormat.getISO8601Format(null, null);
        Date date5 = timeZoneISO8601Format.parse("2023-03-01T14:25:06.789-07:00");
        Date date6 = iso8601Format.parse("2023-03-01T21:25:06.789Z");
        assertEquals("Dates should be equal despite timezone differences", date5, date6);

        // Test withLocale() method
        StdDateFormat localeStdDateFormat = stdDateFormat.withLocale(Locale.CHINESE);
        DateFormat localeISO8601Format = localeStdDateFormat.getISO8601Format(null, null);
        Date date7 = localeISO8601Format.parse("2023年03月01日T14:25:06.789Z");
        assertEquals("Dates should be equal despite locale differences", date1, date7);
    }
}