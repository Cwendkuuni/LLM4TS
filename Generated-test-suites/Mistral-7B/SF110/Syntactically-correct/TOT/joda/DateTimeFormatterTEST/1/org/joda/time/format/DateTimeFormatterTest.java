package org.joda.time.format;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.format.*;

public class DateTimeFormatterTest {

 private static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatter()
         .withLocale(Locale.US)
         .withChronology(ISODateChronology.INSTANCE)
         .withZone(DateTimeZone.UTC);

 @Test
 public void testPrintTo() {
     final Instant instant = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
     final String expected = "2022-01-01T12:00:00Z";
     final StringBuilder sb = new StringBuilder();
     DATE_FORMATTER.printTo(sb, instant);
     assertEquals(expected, sb.toString());
 }

 @Test
 public void testPrintToWithOffset() {
     final Instant instant = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
     final DateTimeZone zone = DateTimeZone.forOffsetHours(2);
     final long offset = zone.getOffset(instant.getTime());
     final String expected = "2022-01-01T14:00:00.000+02:00";
     final StringBuilder sb = new StringBuilder();
     DATE_FORMATTER.withZone(zone).printTo(sb, instant);
     assertEquals(expected, sb.toString());
 }

 @Test
 public void testPrintToWithPartial() {
     final LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 30);
     final String expected = "12:30";
     final StringBuilder sb = new StringBuilder();
     DATE_FORMATTER.printTo(sb, localDateTime);
     assertEquals(expected, sb.toString());
 }

 @Test
 public void testParseLocalDate() {
     final String dateString = "2022-01-01";
     final LocalDate localDate = DATE_FORMATTER.parseLocalDate(dateString);
     assertEquals(new LocalDate(2022, 1, 1), localDate);
 }

 @Test
 public void testParseLocalTime() {
     final String timeString = "12:30:00";
     final LocalTime localTime = DATE_FORMATTER.parseLocalTime(timeString);
     assertEquals(new LocalTime(12, 30), localTime);
 }

 @Test
 public void testParseLocalDateTime() {
     final String dateTimeString = "2022-01-01T12:30:00";
     final LocalDateTime localDateTime = DATE_FORMATTER.parseLocalDateTime(dateTimeString);
     assertEquals(new LocalDateTime(2022, 1, 1, 12, 30), localDateTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidDateFormat() {
     final String invalidDateString = "2022-01-33";
     DATE_FORMATTER.parseLocalDate(invalidDateString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTimeFormat() {
     final String invalidTimeString = "12:60:00";
     DATE_FORMATTER.parseLocalTime(invalidTimeString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidDateTimeFormat() {
     final String invalidDateTimeString = "2022-01-01T12:60:00";
     DATE_FORMATTER.parseLocalDateTime(invalidDateTimeString);
 }
}