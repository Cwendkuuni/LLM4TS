import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testDateTimeFormatterInstance() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterWithLocale() {
        Locale locale = new Locale("en", "US");
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser(), locale);
        assertEquals(locale, formatter.getLocale());
    }

    @Test
    public void testDateTimeFormatterWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
        DateTimeFormatter withOffsetParsed = formatter.withOffsetParsed();
        assertTrue(withOffsetParsed.isOffsetParsed());
        assertEquals(formatter, withOffsetParsed.withoutOffsetParsed());
    }

    @Test
    public void testDateTimeFormatterWithChronology() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
        DateTimeFormatter withChronology = formatter.withChronology(chronology);
        assertEquals(chronology, withChronology.getChronology());
    }

    @Test
    public void testDateTimeFormatterWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
        DateTimeFormatter withZoneUTC = formatter.withZoneUTC();
        assertEquals(DateTimeZone.UTC, withZoneUTC.getZone());
    }

    @Test
    public void testDateTimeFormatterWithZone() {
        DateTimeZone timeZone = DateTimeZone.forID("Asia/Kolkata");
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
        DateTimeFormatter withZone = formatter.withZone(timeZone);
        assertEquals(timeZone, withZone.getZone());
    }

    @Test
    public void testDateTimeFormatterWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
        DateTimeFormatter withPivotYear = formatter.withPivotYear(2020);
        assertEquals(2020, withPivotYear.getPivotYear().intValue());
    }

    @Test
    public void testDateTimeFormatterWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
        DateTimeFormatter withDefaultYear = formatter.withDefaultYear(2021);
        assertEquals(2021, withDefaultYear.getDefaultYear());
    }

    @Test
    public void testPrinting() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
        LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 30, 0, 0, GregorianChronology.getInstance());
        String result = formatter.print(localDateTime);
        assertEquals("2022-01-01T12:30:00", result);
    }

    @Test
    public void testParsingMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
        assertEquals(1640995200000L, formatter.parseMillis("2022-01-01T00:00:00Z"));
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
        assertEquals(new LocalDate(2022, 1, 1), formatter.parseLocalDate("2022-01-01"));
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
        assertEquals(new LocalTime(12, 30), formatter.parseLocalTime("12:30"));
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
        assertEquals(new LocalDateTime(2022, 1, 1, 12, 30), formatter.parseLocalDateTime("2022-01-01T12:30"));
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
        assertEquals(new DateTime(1640995200000L, GregorianChronology.getInstance()), formatter.parseDateTime("2022-01-01T00:00:00Z"));
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
        MutableDateTime mutableDateTime = new MutableDateTime();
        formatter.parseMutableDateTime("2022-01-01T00:00:00Z", mutableDateTime);
        assertEquals(new DateTime(1640995200000L, GregorianChronology.getInstance()), mutableDateTime.toDateTime());
    }
}