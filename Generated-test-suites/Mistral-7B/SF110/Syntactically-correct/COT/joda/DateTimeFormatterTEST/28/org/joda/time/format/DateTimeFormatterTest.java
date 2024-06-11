import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 private DateTimeFormatter formatter;
 private DateTimeFormatter formatterWithLocale;
 private DateTimeFormatter formatterWithParser;
 private DateTimeFormatter formatterWithChronology;
 private DateTimeFormatter formatterWithZone;
 private DateTimeFormatter formatterWithPivotYear;
 private DateTimeFormatter formatterWithDefaultYear;

 @Test
 public void testIsPrinter() {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 assertTrue(formatter.isPrinter());
 }

 @Test
 public void testIsParser() {
 formatterWithParser = new DateTimeFormatter(null, new DefaultDateTimeParser());
 assertTrue(formatterWithParser.isParser());
 }

 @Test
 public void testGetPrinter() {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 assertSame(new DefaultDateTimePrinter(), formatter.getPrinter());
 }

 @Test
 public void testGetParser() {
 formatterWithParser = new DateTimeFormatter(null, new DefaultDateTimeParser());
 assertSame(new DefaultDateTimeParser(), formatterWithParser.getParser());
 }

 @Test
 public void testWithLocale() {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 formatterWithLocale = formatter.withLocale(Locale.US);
 assertNotSame(formatter, formatterWithLocale);
 }

 @Test
 public void testGetLocale() {
 formatterWithLocale = new DateTimeFormatter(new DefaultDateTimePrinter(), null).withLocale(Locale.US);
 assertEquals(Locale.US, formatterWithLocale.getLocale());
 }

 @Test
 public void testWithOffsetParsed() {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 formatterWithOffsetParsed = formatter.withOffsetParsed();
 assertNotSame(formatter, formatterWithOffsetParsed);
 }

 @Test
 public void testIsOffsetParsed() {
 formatterWithOffsetParsed = new DateTimeFormatter(new DefaultDateTimePrinter(), null).withOffsetParsed();
 assertTrue(formatterWithOffsetParsed.isOffsetParsed());
 }

 @Test
 public void testWithChronology() {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 formatterWithChronology = formatter.withChronology(ISOChronology.getInstance());
 assertNotSame(formatter, formatterWithChronology);
 }

 @Test
 public void testGetChronology() {
 formatterWithChronology = new DateTimeFormatter(new DefaultDateTimePrinter(), null).withChronology(ISOChronology.getInstance());
 assertEquals(ISOChronology.getInstance(), formatterWithChronology.getChronology());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithInvalidZone() {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 formatter.withZone(null);
 }

 @Test
 public void testWithZoneUTC() {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 formatterWithZoneUTC = formatter.withZoneUTC();
 assertNotSame(formatter, formatterWithZoneUTC);
 }

 @Test
 public void testGetZone() {
 formatterWithZoneUTC = new DateTimeFormatter(new DefaultDateTimePrinter(), null).withZoneUTC();
 assertEquals(DateTimeZone.UTC, formatterWithZoneUTC.getZone());
 }

 @Test
 public void testWithPivotYear() {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 formatterWithPivotYear = formatter.withPivotYear(2022);
 assertNotSame(formatter, formatterWithPivotYear);
 }

 @Test
 public void testGetPivotYear() {
 formatterWithPivotYear = new DateTimeFormatter(new DefaultDateTimePrinter(), null).withPivotYear(2022);
 assertEquals(Integer.valueOf(2022), formatterWithPivotYear.getPivotYear());
 }

 @Test
 public void testWithDefaultYear() {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 formatterWithDefaultYear = formatter.withDefaultYear(2022);
 assertNotSame(formatter, formatterWithDefaultYear);
 }

 @Test
 public void testGetDefaultYear() {
 formatterWithDefaultYear = new DateTimeFormatter(new DefaultDateTimePrinter(), null).withDefaultYear(2022);
 assertEquals(2022, formatterWithDefaultYear.getDefaultYear());
 }

 @Test
 public void testPrintToStringBuffer() {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 ReadableInstant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISO.UTC);
 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, instant);
 assertEquals("2022-01-01", sb.toString());
 }

 @Test
 public void testPrintToWriter() throws IOException {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 ReadableInstant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISO.UTC);
 StringWriter sw = new StringWriter();
 Writer writer = sw;
 formatter.printTo(writer, instant);
 assertEquals("2022-01-01", sw.toString());
 }

 @Test
 public void testPrintToAppendable() throws IOException {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 ReadableInstant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISO.UTC);
 StringWriter sw = new StringWriter();
 Appendable appendable = sw;
 formatter.printTo(appendable, instant);
 assertEquals("2022-01-01", sw.toString());
 }

 @Test
 public void testPrintToStringBufferWithLong() {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 long millis = 1640978200000L;
 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, millis);
 assertEquals("2022-01-01 00:23:20", sb.toString());
 }

 @Test
 public void testPrintToWriterWithLong() throws IOException {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 long millis = 1640978200000L;
 StringWriter sw = new StringWriter();
 Writer writer = sw;
 formatter.printTo(writer, millis);
 assertEquals("2022-01-01 00:23:20", sw.toString());
 }

 @Test
 public void testPrintToAppendableWithLong() throws IOException {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 long millis = 1640978200000L;
 StringWriter sw = new StringWriter();
 Appendable appendable = sw;
 formatter.printTo(appendable, millis);
 assertEquals("2022-01-01 00:23:20", sw.toString());
 }

 @Test
 public void testPrintToStringBufferWithReadablePartial() {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 ReadablePartial readablePartial = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISO.UTC).toLocalDate();
 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, readablePartial);
 assertEquals("2022-01-01", sb.toString());
 }

 @Test
 public void testPrintToWriterWithReadablePartial() throws IOException {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 ReadablePartial readablePartial = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISO.UTC).toLocalDate();
 StringWriter sw = new StringWriter();
 Writer writer = sw;
 formatter.printTo(writer, readablePartial);
 assertEquals("2022-01-01", sw.toString());
 }

 @Test
 public void testPrintToAppendableWithReadablePartial() throws IOException {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
 ReadablePartial readablePartial = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISO.UTC).toLocalDate();
 StringWriter sw = new StringWriter();
 Appendable appendable = sw;
 formatter.printTo(appendable, readablePartial);
 assertEquals("2022-01-01", sw.toString());
 }

 @Test
 public void testParseIntoReadableInstant() {
 formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
 ReadWritableInstant readWritableInstant = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISO.UTC);
 String s = "2022-01-01 00:23:20";
 int into = formatter.parseInto(readWritableInstant, s, 0);
 assertEquals(s.length(), into);
 assertEquals(readWritableInstant.getMillis(), 1640978200000L);
 }

 @Test
 public void testParseMillis() {
 formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
 long millis = formatter.parseMillis("2022-01-01 00:23:20");
 assertEquals(1640978200000L, millis);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidString() {
 formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
 long millis = formatter.parseMillis("InvalidString");
 fail("Exception not thrown");
 }

 @Test
 public void testParseLocalDate() {
 formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
 LocalDate localDate = formatter.parseLocalDate("2022-01-01");
 assertEquals(new LocalDate(2022, 1, 1), localDate);
 }

 @Test
 public void testParseLocalTime() {
 formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
 LocalTime localTime = formatter.parseLocalTime("14:15:16");
 assertEquals(new LocalTime(14, 15, 16), localTime);
 }

 @Test
 public void testParseLocalDateTime() {
 formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
 LocalDateTime localDateTime = formatter.parseLocalDateTime("2022-01-01T14:15:16");
 assertEquals(new LocalDateTime(2022, 1, 1, 14, 15, 16), localDateTime);
 }

 @Test
 public void testParseDateTime() {
 formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
 DateTime dateTime = formatter.parseDateTime("2022-01-01T14:15:16.123");
 assertEquals(new DateTime(2022, 1, 1, 14, 15, 16, 123, ISO.UTC), dateTime);
 }

 @Test
 public void testParseMutableDateTime() {
 formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
 MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2022-01-01T14:15:16.123");
 assertEquals(new DateTime(2022, 1, 1, 14, 15, 16, 123, ISO.UTC), mutableDateTime.toDateTime());
 }

}