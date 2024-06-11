package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new SimpleDateTimeFormat());
        assertTrue(formatter.isPrinter());

        DateTimeFormatter formatterWithoutPrinter = new DateTimeFormatter(null, new SimpleDateTimeFormat());
        assertFalse(formatterWithoutPrinter.isPrinter());
    }

    @Test
    public void testGetPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new SimpleDateTimeFormat());
        DateTimePrinter expectedPrinter = new SimpleDateTimeFormat();
        DateTimePrinter actualPrinter = formatter.getPrinter();
        assertEquals(expectedPrinter, actualPrinter);

        DateTimeFormatter formatterWithoutPrinter = new DateTimeFormatter(null, new SimpleDateTimeFormat());
        assertNull(formatterWithoutPrinter.getPrinter());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new SimpleDateTimeFormat());
        Locale locale = new Locale("fr_FR");
        DateTimeFormatter newFormatter = formatter.withLocale(locale);
        assertTrue(newFormatter.getLocale().equals(locale));

        DateTimeFormatter formatterWithoutPrinter = new DateTimeFormatter(null, new SimpleDateTimeFormat());
        assertThrows(UnsupportedOperationException.class, () -> formatterWithoutPrinter.withLocale(locale));
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new SimpleDateTimeFormat());
        assertTrue(formatter.isParser());

        DateTimeFormatter formatterWithoutParser = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
        assertFalse(formatterWithoutParser.isParser());
    }

    @Test
    public void testGetParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new SimpleDateTimeFormat());
        DateTimeParser expectedParser = new SimpleDateTimeFormat();
        DateTimeParser actualParser = formatter.getParser();
        assertEquals(expectedParser, actualParser);

        DateTimeFormatter formatterWithoutParser = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
        assertNull(formatterWithoutParser.getParser());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new SimpleDateTimeFormat());
        assertTrue(formatter.isOffsetParsed());

        DateTimeFormatter formatterWithoutOffsetParsed = new DateTimeFormatter(new SimpleDateTimeFormat(), new SimpleDateTimeFormat());
        DateTimeFormatter newFormatter = formatterWithoutOffsetParsed.withOffsetParsed();
        assertTrue(newFormatter.isOffsetParsed());

        DateTimeFormatter formatterWithOffsetParsed = new DateTimeFormatter(new SimpleDateTimeFormat(), new SimpleDateTimeFormat(), null, true, null, null, null, 2000);
        assertFalse(formatterWithOffsetParsed.isOffsetParsed());
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new SimpleDateTimeFormat());
        long millis = 1609459200000L;
        String dateString = "2021-01-01 12:00:00";
        long parsedMillis = formatter.parseMillis(dateString);
        assertEquals(millis, parsedMillis);

        DateTimeFormatter formatterWithInvalidFormat = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
        String invalidDateString = "InvalidDateString";
        assertThrows(IllegalArgumentException.class, () -> formatterWithInvalidFormat.parseMillis(invalidDateString));
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), new SimpleDateTimeFormat("yyyy-MM-dd"));
        String dateString = "2021-01-01";
        LocalDate localDate = formatter.parseLocalDate(dateString);
        LocalDate expectedLocalDate = LocalDate.of(2021, 1, 1);
        assertEquals(expectedLocalDate, localDate);

        DateTimeFormatter formatterWithInvalidFormat = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), null);
        assertThrows(IllegalArgumentException.class, () -> formatterWithInvalidFormat.parseLocalDate("InvalidDateString"));
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss"), new SimpleDateTimeFormat("HH:mm:ss"));
        String timeString = "12:00:00";
        LocalTime localTime = formatter.parseLocalTime(timeString);
        LocalTime expectedLocalTime = LocalTime.of(12, 0, 0);
        assertEquals(expectedLocalTime, localTime);

        DateTimeFormatter formatterWithInvalidFormat = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss"), null);
        assertThrows(IllegalArgumentException.class, () -> formatterWithInvalidFormat.parseLocalTime("InvalidTimeString"));
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"));
        String dateTimeString = "2021-01-01 12:00:00";
        LocalDateTime localDateTime = formatter.parseLocalDateTime(dateTimeString);
        LocalDateTime expectedLocalDateTime = LocalDateTime.of(2021, 1, 1, 12, 0, 0);
        assertEquals(expectedLocalDateTime, localDateTime);

        DateTimeFormatter formatterWithInvalidFormat = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), null);
        assertThrows(IllegalArgumentException.class, () -> formatterWithInvalidFormat.parseLocalDateTime("InvalidDateTimeString"));
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        String dateTimeString = "2021-01-01 12:00:00.123";
        DateTime dateTime = formatter.parseDateTime(dateTimeString);
        DateTime expectedDateTime = new DateTime(160945920123L, DateTimeZone.UTC);
        assertEquals(expectedDateTime, dateTime);

        DateTimeFormatter formatterWithInvalidFormat = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
        assertThrows(IllegalArgumentException.class, () -> formatterWithInvalidFormat.parseDateTime("InvalidDateTimeString"));
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        String dateTimeString = "2021-01-01 12:00:00.123";
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime(dateTimeString);
        DateTime expectedDateTime = new DateTime(160945920123L, DateTimeZone.UTC);
        assertEquals(expectedDateTime, mutableDateTime.toDateTime());

        DateTimeFormatter formatterWithInvalidFormat = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
        assertThrows(IllegalArgumentException.class, () -> formatterWithInvalidFormat.parseMutableDateTime("InvalidDateTimeString"));
    }
}