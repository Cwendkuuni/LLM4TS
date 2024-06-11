package org.joda.time.format;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.format.*;

public class DateTimeFormatterTest {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertTrue(formatter.isPrinter());

        formatter = new DateTimeFormatter(null, null, null, false, null, null, null, 2000);
        assertTrue(formatter.isPrinter());

        formatter = new DateTimeFormatter(null, null, null, false, null, null, null, 2000, 2000);
        assertTrue(formatter.isPrinter());

        formatter = new DateTimeFormatter(null, null, null, true, null, null, null, 2000, 2000);
        assertFalse(formatter.isPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertFalse(formatter.isParser());

        formatter = new DateTimeFormatter(null, null, null, false, null, null, null, 2000, 2000);
        assertFalse(formatter.isParser());

        formatter = new DateTimeFormatter(null, null, null, false, null, null, null, 2000, 2000, null, null);
        assertTrue(formatter.isParser());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter = formatter.withLocale(Locale.ENGLISH);
        assertNotSame(Locale.getDefault(), formatter.getLocale());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter = formatter.withOffsetParsed();
        assertTrue(formatter.isOffsetParsed());

        formatter = new DateTimeFormatter(null, null, null, false, null, null, null, 2000, 2000);
        formatter = formatter.withOffsetParsed();
        assertFalse(formatter.isOffsetParsed());
    }

    @Test
    public void testWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter = formatter.withChronology(DateTimeUtils.UTC);
        assertSame(DateTimeUtils.UTC, formatter.getChronology());

        formatter = new DateTimeFormatter(null, null, null, false, null, null, null, 2000, 2000);
        formatter = formatter.withChronology(DateTimeUtils.GREGORIAN);
        assertSame(DateTimeUtils.GREGORIAN, formatter.getChronology());
    }

    @Test
    public void testWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter = formatter.withZoneUTC();
        assertSame(DateTimeZone.UTC, formatter.getZone());
    }

    @Test
    public void testWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeZone zone = new DateTimeZone("America/Los_Angeles");
        formatter = formatter.withZone(zone);
        assertSame(zone, formatter.getZone());
    }

    @Test
    public void testWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter = formatter.withPivotYear(2010);
        assertEquals(2010, formatter.getPivotYear().intValue());

        formatter = new DateTimeFormatter(null, null, null, false, null, null, null, 2000, 2000);
        formatter = formatter.withPivotYear(2020);
        assertEquals(2020, formatter.getPivotYear().intValue());
    }

    @Test
    public void testWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter = formatter.withDefaultYear(2030);
        assertEquals(2030, formatter.getDefaultYear());
    }

    @Test
    public void testPrintTo() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        StringBuffer sb = new StringBuffer();
        Instant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0).toInstant();
        formatter.printTo(sb, instant);
        String expected = "2022-01-01T00:00:00.000Z";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testParseInto() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null, null, false, null, null, null, 2000, 2000);
        LocalDate date = LocalDate.of(2022, 1, 1);
        ReadWritableInstant readWritableInstant = new DateTime(2022, 1, 1, 0, 0, 0, 0).toInstant();
        int result = formatter.parseInto(readWritableInstant, "2022-01-01", 0);
        assertEquals(result, 13);
        assertEquals(date, readWritableInstant.toLocalDate());
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null, null, false, null, null, null, 2000, 2000);
        long result = formatter.parseMillis("2022-01-01T12:34:56.789Z");
        assertEquals(1640915096789L, result);
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null, null, false, null, null, null, 2000, 2000);
        LocalDate result = formatter.parseLocalDate("2022-01-01");
        assertEquals(LocalDate.of(2022, 1, 1), result);
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null, null, false, null, null, null, 2000, 2000);
        LocalTime result = formatter.parseLocalTime("12:34:56.789");
        assertEquals(LocalTime.of(12, 34, 56, 789), result);
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null, null, false, null, null, null, 2000, 2000);
        LocalDateTime result = formatter.parseLocalDateTime("2022-01-01T12:34:56.789");
        assertEquals(LocalDateTime.of(2022, 1, 1, 12, 34, 56, 789), result);
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null, null, false, null, null, null, 2000, 2000);
        DateTime result = formatter.parseDateTime("2022-01-01T12:34:56.789");
        assertEquals(new DateTime(2022, 1, 1, 12, 34, 56, 789), result);
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null, null, false, null, null, null, 2000, 2000);
        MutableDateTime result = formatter.parseMutableDateTime("2022-01-01T12:34:56.789");
        assertEquals(new MutableDateTime(2022, 1, 1, 12, 34, 56, 789), result);
    }
}