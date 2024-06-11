package org.joda.time.format;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.format.*;

public class DateTimeFormatterTest {

    @Test
    public void testDefaultFormatter() {
        DateTimeFormatter formatter = new DateTimeFormatter();
        assertEquals("2022-01-01T00:00:00.000Z", formatter.print(new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC)));
        DateTime dateTime = formatter.parseDateTime("2022-01-01T00:00:00.000Z");
        assertEquals(new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC), dateTime);
    }

    @Test
    public void testCustomFormatterWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter().withLocale(Locale.US);
        assertEquals("1/1/2022", formatter.print(new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC)));
        DateTime dateTime = formatter.parseDateTime("1/1/2022");
        assertEquals(new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC), dateTime);
    }

    @Test
    public void testCustomFormatterWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter().withChronology(GregorianChronology.getInstance());
        assertEquals("2022-01-01T00:00:00Z", formatter.print(new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC)));
        DateTime dateTime = formatter.parseDateTime("2022-01-01T00:00:00Z");
        assertEquals(new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC), dateTime);
    }

    @Test
    public void testCustomFormatterWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter().withZoneUTC();
        assertEquals("2022-01-01T00:00:00Z", formatter.print(new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC)));
        DateTime dateTime = formatter.parseDateTime("2022-01-01T00:00:00Z");
        assertEquals(new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC), dateTime);
    }

    @Test
    public void testCustomFormatterWithZone() {
        DateTimeZone timeZone = DateTimeZone.forID("America/Los_Angeles");
        DateTimeFormatter formatter = new DateTimeFormatter().withZone(timeZone);
        assertEquals("12/31/2021 23:00:00 PST", formatter.print(new DateTime(2021, 12, 31, 23, 0, 0, 0, timeZone)));
        DateTime dateTime = formatter.parseDateTime("12/31/2021 23:00:00 PST");
        assertEquals(new DateTime(2021, 12, 31, 23, 0, 0, 0, timeZone), dateTime);
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter().withLocale(Locale.US);
        LocalDate date = formatter.parseLocalDate("1/1/2022");
        assertEquals(new LocalDate(2022, 1, 1), date);
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter().withLocale(Locale.US);
        LocalTime time = formatter.parseLocalTime("12:00:00");
        assertEquals(new LocalTime(12, 0), time);
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter().withLocale(Locale.US);
        LocalDateTime dateTime = formatter.parseLocalDateTime("1/1/2022 12:00:00");
        assertEquals(new LocalDateTime(2022, 1, 1, 12, 0), dateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidDate() {
        DateTimeFormatter formatter = new DateTimeFormatter();
        formatter.parseDateTime("invalid_date");
    }
}