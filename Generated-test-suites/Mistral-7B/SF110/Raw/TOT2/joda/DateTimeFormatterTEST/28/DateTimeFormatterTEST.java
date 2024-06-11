package org.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.util.Date;

public class DateTimeFormatterTest {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertTrue(formatter.isPrinter());

        DateTimeFormatter formatterWithPrinter = new DateTimeFormatter(new StaticDateTimePrinter(), null);
        assertTrue(formatterWithPrinter.isPrinter());

        DateTimeFormatter formatterWithoutPrinter = new DateTimeFormatter(null, new StaticDateTimeParser());
        assertFalse(formatterWithoutPrinter.isPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertFalse(formatter.isParser());

        DateTimeFormatter formatterWithParser = new DateTimeFormatter(null, new StaticDateTimeParser());
        assertTrue(formatterWithParser.isParser());

        DateTimeFormatter formatterWithoutParser = new DateTimeFormatter(new StaticDateTimePrinter(), null);
        assertFalse(formatterWithoutParser.isParser());
    }

    @Test
    public void testParseMillis_ValidInput() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new StaticDateTimeParser());
        long millis = formatter.parseMillis("2022-01-01 12:34:56.789");
        Date date = new Date(millis);
        assertEquals("Sat Jan 01 12:34:56 GMT", date.toString());
    }

    @Test(expected = ParseException.class)
    public void testParseMillis_InvalidInput() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new StaticDateTimeParser());
        formatter.parseMillis("invalid-format");
    }

    @Test
    public void testParseMillis_EdgeCases() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new StaticDateTimeParser());

        // Minimum date (0001-01-01 00:00:00.000)
        long minDateMillis = formatter.parseMillis("0001-01-01 00:00:00.000");
        Date minDate = new Date(minDateMillis);
        assertEquals("Thu Jan 01 00:00:00 GMT", minDate.toString());

        // Maximum date (9999-12-31 23:59:59.999)
        long maxDateMillis = formatter.parseMillis("9999-12-31 23:59:59.999");
        Date maxDate = new Date(maxDateMillis);
        assertEquals("Tue Dec 31 23:59:59 GMT", maxDate.toString());

        // Maximum offset (14:00)
        DateTimeFormatter formatterWithOffset = new DateTimeFormatter(null, new StaticDateTimeParser(), null, true, null, null, null, 2000, DateTimeZone.forOffsetHours(14));
        long maxOffsetMillis = formatterWithOffset.parseMillis("2999-12-31 23:59:59.999");
        Date maxOffsetDate = new Date(maxOffsetMillis);
        assertEquals("Sun Dec 29 16:59:59 GMT+14:00", maxOffsetDate.toString());
    }
}