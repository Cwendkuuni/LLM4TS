import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testDateTimeFormatterInstance() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
        DateTimeFormatter withLocale = formatter.withLocale(Locale.US);
        assertEquals(Locale.US, withLocale.getLocale());
    }

    @Test
    public void testDateTimeFormatterWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSSZ"), new SimpleDateTimeParser());
        DateTimeFormatter withOffsetParsed = formatter.withOffsetParsed();
        assertTrue(withOffsetParsed.isOffsetParsed());
    }

    @Test
    public void testDateTimeFormatterWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
        DateTimeFormatter withChronology = formatter.withChronology(ISODateTimeFormat.isoDateChronology());
        assertEquals(ISODateTimeFormat.isoDateChronology(), withChronology.getChronology());
    }

    @Test
    public void testDateTimeFormatterWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSSZ"), new SimpleDateTimeParser());
        DateTimeFormatter withZoneUTC = formatter.withZoneUTC();
        assertEquals(DateTimeZone.UTC, withZoneUTC.getZone());
    }

    @Test
    public void testDateTimeFormatterWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
        DateTimeFormatter withPivotYear = formatter.withPivotYear(2005);
        assertEquals(2005, withPivotYear.getPivotYear().intValue());
    }

    @Test
    public void testDateTimeFormatterWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
        DateTimeFormatter withDefaultYear = formatter.withDefaultYear(2010);
        assertEquals(2010, withDefaultYear.getDefaultYear());
    }

    @Test
    public void testPrintTo() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
        StringBuffer buffer = new StringBuffer();
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 30, 45);
        formatter.printTo(buffer, dateTime);
        assertEquals("2022-01-01 12:30:45", buffer.toString());
    }

    @Test
    public void testPrintToWithOffset() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSSZ"), new SimpleDateTimeParser());
        StringBuffer buffer = new StringBuffer();
        Instant instant = new DateTime(2022, 1, 1, 12, 30, 45, 0, DateTimeZone.forOffsetHours(3).getID());
        formatter.printTo(buffer, instant);
        assertEquals("2022-01-01 15:30:45.000+03:00", buffer.toString());
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
        assertEquals(1640995205000L, formatter.parseMillis("2015-10-12 14:20:45"));
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), new SimpleDateTimeParser());
        assertEquals(new LocalDate(2022, 1, 1), formatter.parseLocalDate("2022-01-01"));
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss"), new SimpleDateTimeParser());
        assertEquals(new LocalTime(12, 30, 45), formatter.parseLocalTime("12:30:45"));
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
        assertEquals(new LocalDateTime(2022, 1, 1, 12, 30, 45), formatter.parseLocalDateTime("2022-01-01 12:30:45"));
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
        assertEquals(new DateTime(2022, 1, 1, 12, 30, 45), formatter.parseDateTime("2022-01-01 12:30:45"));
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
        MutableDateTime mutableDateTime = new MutableDateTime();
        assertEquals(2022, formatter.parseInto(mutableDateTime, "2022-01-01 12:30:45", 0));
        assertEquals(1, mutableDateTime.getMonthOfYear());
        assertEquals(1, mutableDateTime.getDayOfMonth());
        assertEquals(12, mutableDateTime.getHourOfDay());
        assertEquals(30, mutableDateTime.getMinuteOfHour());
        assertEquals(45, mutableDateTime.getSecondOfMinute());
    }
}