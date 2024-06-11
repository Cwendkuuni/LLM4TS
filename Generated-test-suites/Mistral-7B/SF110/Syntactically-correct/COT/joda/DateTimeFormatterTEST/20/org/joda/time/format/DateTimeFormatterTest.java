package org.junit.tests; // Replace with your test package name

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import org.joda.time.format.DateTimeParserBucket;
import org.joda.time.format.FormatUtils;
import org.joda.time.format.ParserCaseInsensitivity;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.StringWriter;
import java.text.ParseException;
import java.util.Locale;

import static org.joda.time.LocalDate.*;
import static org.joda.time.LocalDateTime.*;
import static org.joda.time.LocalTime.*;
import static org.joda.time.format.DateTimeFormat.*;

public class DateTimeFormatterTest {
    

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), null);
        assertTrue(formatter.isPrinter());

        formatter = new DateTimeFormatter(null, new DateTimeParser());
        assertFalse(formatter.isPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeParser());
        assertTrue(formatter.isParser());

        formatter = new DateTimeFormatter(new DateTimePrinter(), null);
        assertFalse(formatter.isParser());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        Locale locale = Locale.US;
        DateTimeFormatter withLocale = formatter.withLocale(locale);
        assertEquals(locale, withLocale.getLocale());

        formatter = withLocale;
        locale = Locale.CHINESE;
        withLocale = formatter.withLocale(locale);
        assertEquals(locale, withLocale.getLocale());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        assertTrue(formatter.isOffsetParsed());
        DateTimeFormatter withOffsetParsed = formatter.withOffsetParsed();
        assertFalse(withOffsetParsed.isOffsetParsed());
    }

    @Test
    public void testIsOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        assertFalse(formatter.isOffsetParsed());

        formatter = formatter.withOffsetParsed();
        assertTrue(formatter.isOffsetParsed());
    }

    @Test
    public void testWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFormatter withChronology = formatter.withChronology(chronology);
        assertEquals(chronology, withChronology.getChronology());

        formatter = withChronology;
        chronology = ISOChronology.getInstance();
        withChronology = formatter.withChronology(chronology);
        assertEquals(chronology, withChronology.getChronology());
    }

    @Test
    public void testWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        DateTimeZone zoneUTC = DateTimeZone.UTC;
        DateTimeFormatter withZoneUTC = formatter.withZoneUTC();
        assertEquals(zoneUTC, withZoneUTC.getZone());
    }

    @Test
    public void testWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        DateTimeZone zone = DateTimeZone.forID("America/New_York");
        DateTimeFormatter withZone = formatter.withZone(zone);
        assertEquals(zone, withZone.getZone());

        formatter = withZone;
        zone = DateTimeZone.forID("Asia/Tokyo");
        withZone = formatter.withZone(zone);
        assertEquals(zone, withZone.getZone());
    }

    @Test
    public void testWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        Integer pivotYear = Integer.valueOf(2005);
        DateTimeFormatter withPivotYear = formatter.withPivotYear(pivotYear);
        assertEquals(pivotYear, withPivotYear.getPivotYear());

        formatter = withPivotYear;
        pivotYear = Integer.valueOf(2100);
        withPivotYear = formatter.withPivotYear(pivotYear);
        assertEquals(pivotYear, withPivotYear.getPivotYear());
    }

    @Test
    public void testWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        int defaultYear = 2010;
        DateTimeFormatter withDefaultYear = formatter.withDefaultYear(defaultYear);
        assertEquals(defaultYear, withDefaultYear.getDefaultYear());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPrintToWhenPrinterNotSupported() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeParser());
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, new BaseDateTime());
    }

    @Test
    public void testPrintToWithInstant() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String expected = "2000-12-31T23:59:59.999Z";
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, new BaseDateTime().getMillis(), null);
        assertEquals(expected, sb.toString());

        formatter = formatter.withLocale(Locale.FRENCH);
        expected = "31 déc. 2000 à 23:59:59.999";
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, new BaseDateTime().getMillis(), null);
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrintToWithReadablePartial() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        LocalDate localDate = new LocalDate();
        String expected = "3000-12-31";
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, localDate);
        assertEquals(expected, sb.toString());

        formatter = formatter.withLocale(Locale.GERMAN);
        expected = "3000-12-31.";
        sb = new StringBuffer();
        formatter.printTo(sb, localDate);
        assertEquals(expected, sb.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintToWithNullStringBuffer() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        formatter.printTo(null, new BaseDateTime());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseIntoWhenParserNotSupported() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), null);
        ReadWritableInstant readWritableInstant = new BaseDateTime();
        int into = formatter.parseInto(readWritableInstant, "2000-12-31", 0);
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String dateString = "2000-12-31T23:59:59.999Z";
        long expectedMillis = 1222302399999L;
        long actualMillis = formatter.parseMillis(dateString);
        assertEquals(expectedMillis, actualMillis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisWithNullString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        formatter.parseMillis(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateWithInvalidDateFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String invalidDate = "3000-13-31";
        try {
            formatter.parseLocalDate(invalidDate);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format: \"3000-13-31\"", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalTimeWithInvalidTimeFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String invalidTime = "24:60:00";
        try {
            formatter.parseLocalTime(invalidTime);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format: \"24:60:00\"", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateTimeWithInvalidDateTimeFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String invalidDateTime = "3000-13-31 24:60:00";
        try {
            formatter.parseLocalDateTime(invalidDateTime);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format: \"3000-13-31 24:60:00\"", e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testParseDateTimeWithInvalidFormatAndParseException() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String invalidDateTime = "3000-13-31-24:60:00";
        try {
            formatter.parseDateTime(invalidDateTime);
            fail();
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateTimeWithNullString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        formatter.parseDateTime(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMutableDateTimeWithInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String invalidDateTime = "3000-13-31-24:60:00";
        try {
            formatter.parseMutableDateTime(invalidDateTime);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format: \"3000-13-31-24:60:00\"", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMutableDateTimeWithNullString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        formatter.parseMutableDateTime(null);
    }

    @Test
    public void testParseMutableDateTimeWithValidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String validDateTime = "2000-12-31T23:59:59.999Z";
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime(validDateTime);
        assertEquals(new DateTime(1222302399999L, DateTimeZone.UTC), mutableDateTime.toDateTime());
    }

    @Test
    public void testParseMutableDateTimeWithCaseInsensitivity() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser())
                .withCaseSensitivity(ParserCaseInsensitivity.INSENSITIVE);
        String validDateTime = "2000-12-31T23:59:59.999z";
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime(validDateTime);
        assertEquals(new DateTime(1222302399999L, DateTimeZone.UTC), mutableDateTime.toDateTime());
    }

    @Test
    public void testPrintWithLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        LocalDate localDate = new LocalDate();
        String expected = "3000-12-31";
        String print = formatter.print(localDate);
        assertEquals(expected, print);

        formatter = formatter.withLocale(Locale.GERMAN);
        expected = "3000-12-31.";
        print = formatter.print(localDate);
        assertEquals(expected, print);
    }

    @Test
    public void testPrintWithLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        LocalTime localTime = new LocalTime();
        String expected = "00:00:00.000";
        String print = formatter.print(localTime);
        assertEquals(expected, print);

        formatter = formatter.withLocale(Locale.SPANISH);
        expected = "00:00:00.000";
        print = formatter.print(localTime);
        assertEquals(expected, print);

        formatter = formatter.withLocale(Locale.FRENCH);
        expected = "00:00:00";
        print = formatter.print(localTime);
        assertEquals(expected, print);
    }

    @Test
    public void testPrintWithLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        LocalDateTime localDateTime = new LocalDateTime();
        String expected = "3000-12-31T00:00:00.000";
        String print = formatter.print(localDateTime);
        assertEquals(expected, print);

        formatter = formatter.withLocale(Locale.GERMAN);
        expected = "3000-12-31. 00:00:00.000";
        print = formatter.print(localDateTime);
        assertEquals(expected, print);

        formatter = formatter.withLocale(Locale.FRENCH);
        expected = "3000-12-31 à 00:00:00";
        print = formatter.print(localDateTime);
        assertEquals(expected, print);
    }

    @Test
    public void testPrintWithMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        MutableDateTime mutableDateTime = new MutableDateTime();
        String expected = "3000-12-31T00:00:00.000Z";
        String print = formatter.print(mutableDateTime.toDateTime());
        assertEquals(expected, print);

        mutableDateTime.setZone(DateTimeZone.forID("America/Los_Angeles"));
        expected = "3000-12-31T07:00:00.000-08:00";
        print = formatter.print(mutableDateTime.toDateTime());
        assertEquals(expected, print);
    }

    
}