package org.joda.time.format;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParserBucket;

public class DateTimeFormatterTest {

    @org.junit.Test
    public void testDateTimeFormatter() {
        // Create a DateTimeFormatter instance with default settings
        DateTimeFormatter formatter = new DateTimeFormatter();

        // Test isPrinter() and isParser() methods
        assertTrue(formatter.isParser());
        assertFalse(formatter.isPrinter());

        // Test getPrinter() and getParser() methods when no printer or parser is set
        assertNull(formatter.getPrinter());
        assertNull(formatter.getParser());

        // Test setting printer and parser
        DateTimePrinter printer = new DateTimePrinterDefault();
        DateTimeParser parser = new DateTimeParserDefault();
        DateTimeFormatter formatterWithPrinterAndParser = new DateTimeFormatter(printer, parser);

        // Test isPrinter() and isParser() methods after setting printer and parser
        assertTrue(formatterWithPrinterAndParser.isParser());
        assertTrue(formatterWithPrinterAndParser.isPrinter());

        // Test getPrinter() and getParser() methods after setting printer and parser
        assertEquals(printer, formatterWithPrinterAndParser.getPrinter());
        assertEquals(parser, formatterWithPrinterAndParser.getParser());

        // Test setting locale
        Locale locale = Locale.ENGLISH;
        DateTimeFormatter formatterWithLocale = formatterWithPrinterAndParser.withLocale(locale);

        // Test getLocale() method after setting locale
        assertEquals(locale, formatterWithLocale.getLocale());

        // Test setting offsetParsed
        DateTimeFormatter formatterWithOffsetParsed = formatterWithLocale.withOffsetParsed();

        // Test isOffsetParsed() method after setting offsetParsed
        assertTrue(formatterWithOffsetParsed.isOffsetParsed());

        // Test setting chronology
        Chronology chrono = ISOChronology.getInstance();
        DateTimeFormatter formatterWithChronology = formatterWithOffsetParsed.withChronology(chrono);

        // Test getChronology() method after setting chronology
        assertEquals(chrono, formatterWithChronology.getChronology());

        // Test setting zone
        DateTimeZone zone = DateTimeZone.UTC;
        DateTimeFormatter formatterWithZone = formatterWithChronology.withZoneUTC();

        // Test getZone() method after setting zone
        assertEquals(zone, formatterWithZone.getZone());

        // Test setting pivot year
        DateTimeFormatter formatterWithPivotYear = formatterWithZone.withPivotYear(2022);

        // Test getPivotYear() method after setting pivot year
        assertEquals(2022, formatterWithPivotYear.getPivotYear().intValue());

        // Test setting default year
        DateTimeFormatter formatterWithDefaultYear = formatterWithPivotYear.withDefaultYear(2010);

        // Test getDefaultYear() method after setting default year
        assertEquals(2010, formatterWithDefaultYear.getDefaultYear());

        // Test printTo() method with StringBuffer and Instant
        StringBuffer sb = new StringBuffer();
        DateTimeFormatter formatterWithDefaultYearAndZone = formatterWithDefaultYear.withZone(DateTimeZone.forOffsetHours(2));
        DateTime baseDate = new DateTime(1609459200000L); // 1st Jan 2021, 12:00:00 PM UTC
        formatterWithDefaultYearAndZone.printTo(sb, baseDate);
        String expectedString = "Jan 01, 2021 06:00:00 PM";
        assertEquals(expectedString, sb.toString());

        // Test printTo() method with Writer and Instant
        StringWriter sw = new StringWriter();
        try {
            formatterWithDefaultYearAndZone.printTo(sw, baseDate);
            assertEquals(expectedString, sw.toString());
        } finally {
            sw.close();
        }

        // Test printTo() method with Appendable and Instant
        StringBuilder sb2 = new StringBuilder();
        formatterWithDefaultYearAndZone.printTo(sb2, baseDate);
        assertEquals(expectedString, sb2.toString());

        // Test printTo() method with StringBuffer and Long
        long millis = baseDate.getMillis();
        StringBuffer sb3 = new StringBuffer();
        formatterWithDefaultYearAndZone.printTo(sb3, millis);
        assertEquals(expectedString, sb3.toString());

        // Test printTo() method with Writer and Long
        StringWriter sw2 = new StringWriter();
        try {
            formatterWithDefaultYearAndZone.printTo(sw2, millis);
            assertEquals(expectedString, sw2.toString());
        } finally {
            sw2.close();
        }

        // Test printTo() method with Appendable and Long
        StringBuilder sb4 = new StringBuilder();
        formatterWithDefaultYearAndZone.printTo(sb4, millis);
        assertEquals(expectedString, sb4.toString());

        // Test printTo() method with StringBuffer and ReadablePartial
        BaseDateTime readablePartial = new DateTime(millis).withZone(zone);
        StringBuffer sb5 = new StringBuffer();
        formatterWithDefaultYearAndZone.printTo(sb5, readablePartial);
        String expectedStringPartial = "2021-01-01T12:00:00Z";
        assertEquals(expectedStringPartial, sb5.toString());

        // Test printTo() method with Writer and ReadablePartial
        StringWriter sw3 = new StringWriter();
        try {
            formatterWithDefaultYearAndZone.printTo(sw3, readablePartial);
            assertEquals(expectedStringPartial, sw3.toString());
        } finally {
            sw3.close();
        }

        // Test printTo() method with Appendable and ReadablePartial
        StringBuilder sb6 = new StringBuilder();
        formatterWithDefaultYearAndZone.printTo(sb6, readablePartial);
        assertEquals(expectedStringPartial, sb6.toString());

        // Test print() method with ReadableInstant
        String printWithInstant = formatterWithDefaultYearAndZone.print(baseDate);
        assertEquals(expectedString, printWithInstant);

        // Test print() method with Long
        String printWithLong = formatterWithDefaultYearAndZone.print(millis);
        assertEquals(expectedString, printWithLong);

        // Test print() method with ReadablePartial
        String printWithPartial = formatterWithDefaultYearAndZone.print(readablePartial);
        assertEquals(expectedStringPartial, printWithPartial);

        // Test parseInto() method with ReadWritableInstant, String and int
        MutableDateTime mutableDateTime = new MutableDateTime();
        String inputString = "Jan 01, 2021 06:00:00 PM";
        int parsePositionIndex = 0;
        int parsedLength = formatterWithDefaultYearAndZone.parseInto(mutableDateTime, inputString, parsePositionIndex);
        assertEquals(inputString.length(), parsedLength);
        assertEquals(baseDate, mutableDateTime.toDateTime());

        // Test parseMillis() method with String
        long parsedMillis = formatterWithDefaultYearAndZone.parseMillis(expectedString);
        assertEquals(millis, parsedMillis);

        // Test parseLocalDate() method with String
        LocalDate localDate = formatterWithDefaultYearAndZone.parseLocalDate("2021-01-01");
        assertEquals(new LocalDate(2021, 1, 1), localDate);

        // Test parseLocalTime() method with String
        LocalTime localTime = formatterWithDefaultYearAndZone.parseLocalTime("12:00:00 PM");
        assertEquals(new LocalTime(12, 0), localTime);

        // Test parseLocalDateTime() method with String
        LocalDateTime localDateTime = formatterWithDefaultYearAndZone.parseLocalDateTime("2021-01-01T12:00:00");
        assertEquals(new LocalDateTime(new LocalDate(2021, 1, 1), new LocalTime(12, 0)), localDateTime);

        // Test parseDateTime() method with String
        DateTime parsedDateTime = formatterWithDefaultYearAndZone.parseDateTime("2021-01-01T12:00:00.123Z");
        assertEquals(baseDate, parsedDateTime);

        // Test parseMutableDateTime() method with String
        MutableDateTime parsedMutableDateTime = formatterWithDefaultYearAndZone.parseMutableDateTime("2021-01-01T12:00:00.123Z");
        assertEquals(mutableDateTime, parsedMutableDateTime);

        // Test parseMillis() method with invalid String
        String invalidInputString = "Jan 01, 2021 13:00:60 PM"; // Invalid hour value
        try {
            formatterWithDefaultYearAndZone.parseMillis(invalidInputString);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}