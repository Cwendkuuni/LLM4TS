package org.joda.time.format;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.format.*;

public class DateTimeFormatterTest {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertTrue(formatter.isPrinter());

        DateTimeFormatter formatter2 = new DateTimeFormatter(null, null, null, false, null, null, null, 2000);
        assertFalse(formatter2.isPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertFalse(formatter.isParser());

        DateTimeFormatter formatter2 = new DateTimeFormatter(null, null, null, false, null, null, null, 2000);
        assertFalse(formatter2.isParser());

        DateTimeFormatter formatter3 = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        assertTrue(formatter3.isParser());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeFormatter formatterWithLocale = formatter.withLocale(Locale.US);
        assertNotSame(formatter, formatterWithLocale);
        assertEquals(Locale.US, formatterWithLocale.getLocale());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
        assertNotSame(formatter, formatterWithOffsetParsed);
        assertFalse(formatterWithOffsetParsed.isOffsetParsed());
        assertTrue(formatterWithOffsetParsed.canSetZone(false));

        DateTimeFormatter formatterWithOffsetParsed2 = formatter.withOffsetParsed().withOffsetParsed();
        assertNotSame(formatterWithOffsetParsed, formatterWithOffsetParsed2);
        assertTrue(formatterWithOffsetParsed2.isOffsetParsed());
        assertFalse(formatterWithOffsetParsed2.canSetZone(false));
    }

    @Test
    public void testIsOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertFalse(formatter.isOffsetParsed());

        DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
        assertTrue(formatterWithOffsetParsed.isOffsetParsed());
    }

    @Test
    public void testWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeFormatter formatterWithChronology = formatter.withChronology(ISO.UTC);
        assertNotSame(formatter, formatterWithChronology);
        assertEquals(ISO.UTC, formatterWithChronology.getChronology());
    }

    @Test
    public void testWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeFormatter formatterWithZoneUTC = formatter.withZoneUTC();
        assertNotSame(formatter, formatterWithZoneUTC);
        assertEquals(DateTimeZone.UTC, formatterWithZoneUTC.getZone());
    }

    @Test
    public void testWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeZone zone = new DateTimeZone("America/Los_Angeles");
        DateTimeFormatter formatterWithZone = formatter.withZone(zone);
        assertNotSame(formatter, formatterWithZone);
        assertEquals(zone, formatterWithZone.getZone());
    }

    @Test
    public void testWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeFormatter formatterWithPivotYear = formatter.withPivotYear(2020);
        assertNotSame(formatter, formatterWithPivotYear);
        assertEquals(2020, formatterWithPivotYear.getPivotYear().intValue());

        DateTimeFormatter formatterWithPivotYear2 = formatter.withPivotYear(1999);
        assertNotSame(formatterWithPivotYear, formatterWithPivotYear2);
        assertEquals(1999, formatterWithPivotYear2.getPivotYear().intValue());
    }

    @Test
    public void testWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeFormatter formatterWithDefaultYear = formatter.withDefaultYear(2015);
        assertNotSame(formatter, formatterWithDefaultYear);
        assertEquals(2015, formatterWithDefaultYear.getDefaultYear());
    }

    @Test
    public void testPrintTo() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), null);
        Instant instant = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();

        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, instant);
        assertEquals("2022-01-01T12:00:00Z", sb.toString());

        Writer writer = new StringWriter();
        formatter.printTo(writer, instant);
        assertEquals("2022-01-01T12:00:00Z", writer.toString());

        Appendable appendable = new StringBuffer();
        formatter.printTo(appendable, instant);
        assertEquals("2022-01-01T12:00:00Z", appendable.toString());
    }

    @Test
    public void testPrintToLong() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), null);
        long time = 1652432000000L; // 25/08/2021

        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, time);
        assertEquals("2021-08-25", sb.toString());

        Writer writer = new StringWriter();
        formatter.printTo(writer, time);
        assertEquals("2021-08-25", writer.toString());

        Appendable appendable = new StringBuffer();
        formatter.printTo(appendable, time);
        assertEquals("2021-08-25", appendable.toString());
    }

    @Test
    public void testPrintToReadablePartial() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), null);
        DateTime d = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);
        LocalDateTime ldt = d.toLocalDateTime();

        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, ldt);
        assertEquals("2022-01-01T12:00:00", sb.toString());

        Writer writer = new StringWriter();
        formatter.printTo(writer, ldt);
        assertEquals("2022-01-01T12:00:00", writer.toString());

        Appendable appendable = new StringBuffer();
        formatter.printTo(appendable, ldt);
        assertEquals("2022-01-01T12:00:00", appendable.toString());
    }

    @Test
    public void testPrint() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), null);
        Instant instant = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();

        assertEquals("2022-01-01T12:00:00Z", formatter.print(instant));
        assertEquals("2022-01-01T12:00:00Z", formatter.print(instant.toEpochMilli()));
    }

    @Test
    public void testParseInto() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeParser());
        ReadWritableInstant rwi = new GregorianInstant(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);

        formatter.parseInto(rwi, "2022-01-01T12:00:00Z");
        assertEquals(2022, rwi.getYear());
        assertEquals(1, rwi.getMonthOfYear());
        assertEquals(1, rwi.getDayOfMonth());
        assertEquals(12, rwi.getHourOfDay());
        assertEquals(0, rwi.getMinuteOfHour());
        assertEquals(0, rwi.getSecondOfMinute());
        assertEquals(0, rwi.getMillisOfSecond());
        assertEquals(DateTimeZone.UTC, rwi.getZone());

        formatter.parseInto(rwi, "2022-01-01");
        assertEquals(2022, rwi.getYear());
        assertEquals(1, rwi.getMonthOfYear());
        assertEquals(1, rwi.getDayOfMonth());
        assertEquals(0, rwi.getHourOfDay());
        assertEquals(0, rwi.getMinuteOfHour());
        assertEquals(0, rwi.getSecondOfMinute());
        assertEquals(0, rwi.getMillisOfSecond());
        assertEquals(DateTimeZone.UTC, rwi.getZone());

        formatter.parseInto(rwi, "2022-01-01 12:00:00");
        assertEquals(2022, rwi.getYear());
        assertEquals(1, rwi.getMonthOfYear());
        assertEquals(1, rwi.getDayOfMonth());
        assertEquals(12, rwi.getHourOfDay());
        assertEquals(0, rwi.getMinuteOfHour());
        assertEquals(0, rwi.getSecondOfMinute());
        assertEquals(0, rwi.getMillisOfSecond());
        assertEquals(DateTimeZone.UTC, rwi.getZone());

        try {
            formatter.parseInto(rwi, "2022-01-01 12:00:00 Z");
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            formatter.parseInto(rwi, "2022-01-01 12:00:00 +01:00");
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeParser());

        long time = formatter.parseMillis("2022-01-01T12:00:00Z");
        assertEquals(1640995200000L, time);

        try {
            formatter.parseMillis("2022-01-01T12:00:00 +01:00");
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeParser());

        LocalDate localDate = formatter.parseLocalDate("2022-01-01");
        assertEquals(2022, localDate.getYear());
        assertEquals(1, localDate.getMonthOfYear());
        assertEquals(1, localDate.getDayOfMonth());

        try {
            formatter.parseLocalDate("2022-01-01T12:00:00");
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeParser());

        LocalTime localTime = formatter.parseLocalTime("12:00:00");
        assertEquals(12, localTime.getHourOfDay());
        assertEquals(0, localTime.getMinuteOfHour());
        assertEquals(0, localTime.getSecondOfMinute());

        try {
            formatter.parseLocalTime("2022-01-01T12:00:00");
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeParser());

        LocalDateTime localDateTime = formatter.parseLocalDateTime("2022-01-01T12:00:00");
        assertEquals(2022, localDateTime.getYear());
        assertEquals(1, localDateTime.getMonthOfYear());
        assertEquals(1, localDateTime.getDayOfMonth());
        assertEquals(12, localDateTime.getHourOfDay());
        assertEquals(0, localDateTime.getMinuteOfHour());
        assertEquals(0, localDateTime.getSecondOfMinute());

        try {
            formatter.parseLocalDateTime("2022-01-01");
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeParser());

        DateTime dateTime = formatter.parseDateTime("2022-01-01T12:00:00Z");
        assertEquals(2022, dateTime.getYear());
        assertEquals(1, dateTime.getMonthOfYear());
        assertEquals(1, dateTime.getDayOfMonth());
        assertEquals(12, dateTime.getHourOfDay());
        assertEquals(0, dateTime.getMinuteOfHour());
        assertEquals(0, dateTime.getSecondOfMinute());
        assertEquals(0, dateTime.getMillisOfSecond());
        assertEquals(DateTimeZone.UTC, dateTime.getZone());

        try {
            formatter.parseDateTime("2022-01-01T12:00:00 +01:00");
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeParser());

        MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2022-01-01T12:00:00Z");
        assertEquals(2022, mutableDateTime.getYear());
        assertEquals(1, mutableDateTime.getMonthOfYear());
        assertEquals(1, mutableDateTime.getDayOfMonth());
        assertEquals(12, mutableDateTime.getHourOfDay());
        assertEquals(0, mutableDateTime.getMinuteOfHour());
        assertEquals(0, mutableDateTime.getSecondOfMinute());
        assertEquals(0, mutableDateTime.getMillisOfSecond());
        assertEquals(DateTimeZone.UTC, mutableDateTime.getZone());

        mutableDateTime.setZone(DateTimeZone.forOffsetHours(1));
        assertEquals(DateTimeZone.forOffsetHours(1), mutableDateTime.getZone());
    }
}