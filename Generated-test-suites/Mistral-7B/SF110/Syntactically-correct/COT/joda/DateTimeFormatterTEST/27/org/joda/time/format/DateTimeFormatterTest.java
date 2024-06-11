import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeParserBucket;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class DateTimeFormatterTest {

 private DateTimeFormatter formatter;

 @Test
 public void testIsPrinter() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
 assertTrue(formatter.isPrinter());
 }

 @Test
 public void testIsParser() {
 formatter = new DateTimeFormatter(null, new SimpleDateTimeFormat());
 assertTrue(formatter.isParser());
 }

 @Test
 public void testGetPrinter() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
 assertSame(new SimpleDateTimeFormat(), formatter.getPrinter());
 }

 @Test
 public void testGetParser() {
 formatter = new DateTimeFormatter(null, new SimpleDateTimeFormat());
 assertSame(new SimpleDateTimeFormat(), formatter.getParser());
 }

 @Test
 public void testWithLocale() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
 formatter = formatter.withLocale(Locale.US);
 assertNotSame(Locale.getDefault(), formatter.getLocale());
 }

 @Test
 public void testWithOffsetParsed() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
 formatter = formatter.withOffsetParsed();
 assertTrue(formatter.isOffsetParsed());
 }

 @Test
 public void testIsOffsetParsed() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
 assertFalse(formatter.isOffsetParsed());
 formatter = formatter.withOffsetParsed();
 assertTrue(formatter.isOffsetParsed());
 }

 @Test
 public void testWithChronology() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
 formatter = formatter.withChronology(ISODate.getInstance().getChronology());
 assertSame(ISODate.getInstance().getChronology(), formatter.getChronology());
 }

 @Test
 public void testWithZoneUTC() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
 formatter = formatter.withZoneUTC();
 assertSame(DateTimeZone.UTC, formatter.getZone());
 }

 @Test
 public void testWithZone() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
 formatter = formatter.withZone(DateTimeZone.forOffsetHours(2));
 assertSame(DateTimeZone.forOffsetHours(2), formatter.getZone());
 }

 @Test
 public void testGetZone() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
 assertNull(formatter.getZone());
 formatter = formatter.withZone(DateTimeZone.forOffsetHours(2));
 assertSame(DateTimeZone.forOffsetHours(2), formatter.getZone());
 }

 @Test
 public void testWithPivotYear() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
 formatter = formatter.withPivotYear(2020);
 assertEquals(2020, formatter.getPivotYear().intValue());
 }

 @Test
 public void testGetPivotYear() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
 assertNull(formatter.getPivotYear());
 formatter = formatter.withPivotYear(2020);
 assertEquals(2020, formatter.getPivotYear().intValue());
 }

 @Test
 public void testPrintToStringBuffer() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
 Instant instant = new Instant(1302052800000L);
 formatter.printTo(new StringBuffer(), instant);
 assertEquals("2004-11-03 10:00:00.000", formatter.print(instant));
 }

 @Test
 public void testPrintToWriter() throws IOException {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
 Instant instant = new Instant(1302052800000L);
 StringWriter writer = new StringWriter();
 formatter.printTo(writer, instant);
 assertEquals("2004-11-03 10:00:00.000", writer.toString());
 }

 @Test
 public void testPrintToAppendable() throws IOException {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), null);
 Instant instant = new Instant(1302052800000L);
 StringWriter writer = new StringWriter();
 Appendable appendable = writer;
 formatter.printTo(appendable, instant);
 assertEquals("2004-11-03 10:00:00.000", writer.toString());
 }

 @Test
 public void testPrintToStringBufferWithLocalDate() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), null);
 LocalDate localDate = LocalDate.of(2020, 12, 31);
 formatter.printTo(new StringBuffer(), localDate);
 assertEquals("2020-12-31", formatter.print(localDate));
 }

 @Test
 public void testPrintToStringBufferWithLocalTime() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss"), null);
 LocalTime localTime = LocalTime.of(12, 30);
 formatter.printTo(new StringBuffer(), localTime);
 assertEquals("12:30:00.000", formatter.print(localTime));
 }

 @Test
 public void testPrintToWriterWithLocalDateTime() throws IOException {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
 LocalDateTime localDateTime = new LocalDateTime(2020, 12, 31, 12, 30, 0, 0);
 StringWriter writer = new StringWriter();
 formatter.printTo(writer, localDateTime);
 assertEquals("2020-12-31 12:30:00.000", writer.toString());
 }

 @Test
 public void testPrintToAppendableWithLocalDateTime() throws IOException {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
 LocalDateTime localDateTime = new LocalDateTime(2020, 12, 31, 12, 30, 0, 0);
 StringWriter writer = new StringWriter();
 Appendable appendable = writer;
 formatter.printTo(appendable, localDateTime);
 assertEquals("2020-12-31 12:30:00.000", writer.toString());
 }

 @Test
 public void testParseIntoReadWritableInstantWithString() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
 Instant instant = Instant.now();
 String s = formatter.print(instant);
 MutableDateTime mutableDateTime = new MutableDateTime();
 int into = formatter.parseInto(mutableDateTime, s.substring(0, s.length() - 3), 0);
 assertEquals(s.length() - 3, into);
 assertEquals(instant.getMillis(), mutableDateTime.getMillis());
 }

 @Test
 public void testParseMillisWithString() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
 String s = "2020-12-31 23:59:59.999";
 assertThrows(IllegalArgumentException.class, () -> formatter.parseMillis(s));
 }

 @Test
 public void testParseLocalDateWithString() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), null);
 String s = "2020-12-31";
 assertEquals(LocalDate.of(2020, 12, 31), formatter.parseLocalDate(s));
 }

 @Test
 public void testParseLocalTimeWithString() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss"), null);
 String s = "12:30:00.000";
 assertEquals(LocalTime.of(12, 30), formatter.parseLocalTime(s));
 }

 @Test
 public void testParseLocalDateTimeWithString() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
 String s = "2020-12-31 12:30:00.000";
 assertEquals(new LocalDateTime(2020, 12, 31, 12, 30, 0, 0), formatter.parseLocalDateTime(s));
 }

 @Test
 public void testParseDateTimeWithString() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
 String s = "2020-12-31 12:30:00.000";
 assertEquals(new DateTime(2020, 12, 31, 12, 30, 0, 0), formatter.parseDateTime(s));
 }

 @Test
 public void testParseMutableDateTimeWithString() {
 formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
 String s = "2020-12-31 12:30:00.000";
 MutableDateTime mutableDateTime = new MutableDateTime();
 formatter.parseInto(mutableDateTime, s, 0);
 assertEquals(new DateTime(2020, 12, 31, 12, 30, 0, 0), mutableDateTime.toDateTime());
 }
}