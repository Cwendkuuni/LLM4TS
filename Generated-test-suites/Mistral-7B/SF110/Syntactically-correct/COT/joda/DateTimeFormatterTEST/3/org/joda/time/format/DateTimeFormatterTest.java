import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertTrue(formatter.isPrinter());

        formatter = new DateTimeFormatter(null, null, Locale.ENGLISH, false, null, null, null, 2000);
        assertTrue(formatter.isPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertFalse(formatter.isParser());

        formatter = new DateTimeFormatter(null, new DateTimeParser(), null, false, null, null, null, 2000);
        assertTrue(formatter.isParser());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter = formatter.withLocale(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, formatter.getLocale());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter = formatter.withOffsetParsed();
        assertTrue(formatter.isOffsetParsed());
    }

    @Test
    public void testWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter = formatter.withChronology(ISOChronology.INSTANCE);
        assertEquals(ISOChronology.INSTANCE, formatter.getChronology());
    }

    @Test
    public void testWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter = formatter.withZoneUTC();
        assertEquals(DateTimeZone.UTC, formatter.getZone());
    }

    @Test
    public void testWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetHours(1);
        formatter = formatter.withZone(dateTimeZone);
        assertEquals(dateTimeZone, formatter.getZone());
    }

    @Test
    public void testWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter = formatter.withPivotYear(2022);
        assertEquals(2022, formatter.getPivotYear().intValue());
    }

    @Test
    public void testWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter = formatter.withDefaultYear(2023);
        assertEquals(2023, formatter.getDefaultYear());
    }

    @Test
    public void testPrintToStringBuffer() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, dateTime);
        String expected = "2022-01-01T12:00:00.000Z";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testPrintToWriter() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, dateTime);
        String expected = "2022-01-01T12:00:00.000Z";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrintToAppendable() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);
        StringWriter writer = new StringWriter();
        Appendable appendable = writer;
        formatter.printTo(appendable, dateTime);
        String expected = "2022-01-01T12:00:00.000Z";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrintToStringBufferWithLong() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        long millis = 1640995200000L; // 2022-01-01 00:00:00.0 UTC
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, millis);
        String expected = "2022-01-01T00:00:00.000Z";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testPrintToWriterWithLong() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        long millis = 1640995200000L; // 2022-01-01 00:00:00.0 UTC
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, millis);
        String expected = "2022-01-01T00:00:00.000Z";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrintToAppendableWithLong() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        long millis = 1640995200000L; // 2022-01-01 00:00:00.0 UTC
        StringWriter writer = new StringWriter();
        Appendable appendable = writer;
        formatter.printTo(appendable, millis);
        String expected = "2022-01-01T00:00:00.000Z";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrintToStringBufferWithReadablePartial() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC);
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, dateTime.toReadablePartial(ChronologyUnit.HOUR_OF_DAY));
        String expected = "12:30";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testParseIntoReadWritableInstant() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC);
        Instant instant = dateTime.toInstant();
        ReadWritableInstant readWritableInstant = new InstantFieldReaderWritableInstant();
        int result = formatter.parseInto(readWritableInstant, "2022-01-01T12:30:00.000Z", 0);
        assertEquals(result, 0);
        assertEquals(instant, readWritableInstant.getInstant());
        assertEquals(DateTimeZone.UTC, readWritableInstant.getChronology().getZone());
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        String dateString = "2022-01-01T12:30:00.000Z";
        long result = formatter.parseMillis(dateString);
        assertEquals(1640995200000L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        String dateString = "InvalidFormat";
        formatter.parseMillis(dateString);
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        String dateString = "2022-01-01";
        LocalDate result = formatter.parseLocalDate(dateString);
        assertEquals(new LocalDate(2022, 1, 1), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        String dateString = "InvalidFormat";
        formatter.parseLocalDate(dateString);
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        String timeString = "12:30:00";
        LocalTime result = formatter.parseLocalTime(timeString);
        assertEquals(new LocalTime(12, 30), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        String timeString = "InvalidFormat";
        formatter.parseLocalTime(timeString);
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        String dateTimeString = "2022-01-01T12:30:00.000Z";
        LocalDateTime result = formatter.parseLocalDateTime(dateTimeString);
        assertEquals(new LocalDateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        String dateTimeString = "InvalidFormat";
        formatter.parseLocalDateTime(dateTimeString);
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        String dateTimeString = "2022-01-01T12:30:00.000Z";
        DateTime result = formatter.parseDateTime(dateTimeString);
        assertEquals(new DateTime(1640995200000L, DateTimeZone.UTC), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        String dateTimeString = "InvalidFormat";
        formatter.parseDateTime(dateTimeString);
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        String dateTimeString = "2022-01-01T12:30:00.000Z";
        MutableDateTime result = formatter.parseMutableDateTime(dateTimeString);
        assertEquals(new MutableDateTime(1640995200000L, DateTimeZone.UTC), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMutableDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        String dateTimeString = "InvalidFormat";
        formatter.parseMutableDateTime(dateTimeString);
    }
}