import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 private DateTimeFormatter formatter;

 @Before
 public void setup() {
 formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new SimpleDateTimeParser());
 }

 @Test
 public void testIsPrinter() {
 assertTrue(formatter.isPrinter());
 }

 @Test
 public void testGetPrinter() {
 DateTimePrinter printer = formatter.getPrinter();
 assertNotNull(printer);
 }

 @Test
 public void testIsParser() {
 assertTrue(formatter.isParser());
 }

 @Test
 public void testGetParser() {
 DateTimeParser parser = formatter.getParser();
 assertNotNull(parser);
 }

 @Test
 public void testWithLocale() {
 Locale locale = Locale.ENGLISH;
 DateTimeFormatter newFormatter = formatter.withLocale(locale);
 assertNotSame(formatter, newFormatter);
 assertEquals(locale, newFormatter.getLocale());
 }

 @Test
 public void testWithOffsetParsed() {
 DateTimeFormatter newFormatter = formatter.withOffsetParsed();
 assertNotSame(formatter, newFormatter);
 assertTrue(newFormatter.isOffsetParsed());
 }

 @Test
 public void testWithChronology() {
 Chronology chronology = GregorianChronology.getInstance();
 DateTimeFormatter newFormatter = formatter.withChronology(chronology);
 assertNotSame(formatter, newFormatter);
 assertEquals(chronology, newFormatter.getChronology());
 }

 @Test
 public void testWithZoneUTC() {
 DateTimeFormatter newFormatter = formatter.withZoneUTC();
 assertNotSame(formatter, newFormatter);
 assertSame(DateTimeZone.UTC, newFormatter.getZone());
 }

 @Test
 public void testWithZone() {
 DateTimeZone zone = DateTimeZone.forID("America/New_York");
 DateTimeFormatter newFormatter = formatter.withZone(zone);
 assertNotSame(formatter, newFormatter);
 assertEquals(zone, newFormatter.getZone());
 }

 @Test
 public void testWithPivotYear() {
 DateTimeFormatter newFormatter = formatter.withPivotYear(2022);
 assertNotSame(formatter, newFormatter);
 assertEquals(2022, newFormatter.getPivotYear().intValue());
 }

 @Test
 public void testWithDefaultYear() {
 DateTimeFormatter newFormatter = formatter.withDefaultYear(2022);
 assertNotSame(formatter, newFormatter);
 assertEquals(2022, newFormatter.getDefaultYear());
 }

 @Test
 public void testPrintToStringBuffer() {
 DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss z")
 .withLocale(Locale.ENGLISH)
 .printTo(new StringBuffer(), new DateTime(2022, 1, 1, 12, 0, 0, 0, GregorianChronology.getInstance(), DateTimeZone.UTC));
 String expected = "2022-01-01 12:00:00 UTC";
 assertEquals(expected, new String(new StringBuffer().toString()));
 }

 @Test
 public void testPrintToWriter() throws IOException {
 Writer writer = new StringWriter();
 formatter.printTo(writer, new DateTime(2022, 1, 1, 12, 0, 0, 0, GregorianChronology.getInstance(), DateTimeZone.UTC));
 String expected = "2022-01-01 12:00:00.000Z\n";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testPrintToAppendable() throws IOException {
 Appendable appendable = new StringBuffer();
 formatter.printTo(appendable, new DateTime(2022, 1, 1, 12, 0, 0, 0, GregorianChronology.getInstance(), DateTimeZone.UTC));
 String expected = "2022-01-01 12:00:00.000Z";
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testPrintToStringBufferWithOffset() {
 DateTimeZone zone = DateTimeZone.forID("America/New_York");
 DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss z")
 .withLocale(Locale.ENGLISH)
 .withZone(zone)
 .printTo(new StringBuffer(), new DateTime(2022, 1, 1, 12, 0, 0, 0, GregorianChronology.getInstance()));
 String expected = "2022-01-01 16:00:00-05:00";
 assertEquals(expected, new String(new StringBuffer().toString()));
 }

 @Test
 public void testPrintToWriterWithOffset() throws IOException {
 Writer writer = new StringWriter();
 DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss z")
 .withLocale(Locale.ENGLISH)
 .withZone(DateTimeZone.forID("America/New_York"))
 .printTo(writer, new DateTime(2022, 1, 1, 12, 0, 0, 0, GregorianChronology.getInstance()));
 String expected = "2022-01-01 16:00:00-05:00\n";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testPrintToAppendableWithOffset() throws IOException {
 Appendable appendable = new StringBuffer();
 DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss z")
 .withLocale(Locale.ENGLISH)
 .withZone(DateTimeZone.forID("America/New_York"))
 .printTo(appendable, new DateTime(2022, 1, 1, 12, 0, 0, 0, GregorianChronology.getInstance()));
 String expected = "2022-01-01 16:00:00-05:00";
 assertEquals(expected, appendable.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPrintToStringBufferWithNullInstant() {
 formatter.printTo(new StringBuffer(), null);
 }

 @Test(expected = IOException.class)
 public void testPrintToWriterWithNullInstant() throws IOException {
 Writer writer = new StringWriter();
 formatter.printTo(writer, null);
 }

 @Test(expected = IOException.class)
 public void testPrintToAppendableWithNullInstant() throws IOException {
 Appendable appendable = new StringBuffer();
 formatter.printTo(appendable, null);
 }

 @Test
 public void testParseIntoReadWritableInstant() {
 ReadWritableInstant readWritableInstant = new DateTime(2022, 1, 1, 12, 0, 0, 0, GregorianChronology.getInstance(), DateTimeZone.UTC);
 formatter.parseInto(readWritableInstant, "2022-01-01 12:00:00", 0);
 assertEquals(readWritableInstant.getMillis(), 1640995200000L);
 }

 @Test
 public void testParseMillis() {
 long millis = formatter.parseMillis("2022-01-01 12:00:00");
 assertEquals(1640995200000L, millis);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMillisInvalidFormat() {
 formatter.parseMillis("2022-01-01");
 }

 @Test
 public void testParseLocalDate() {
 LocalDate localDate = formatter.parseLocalDate("2022-01-01");
 assertEquals(2022, localDate.getYear());
 assertEquals(1, localDate.getMonthOfYear().getValue());
 assertEquals(1, localDate.getDayOfMonth().getValue());
 }

 @Test
 public void testParseLocalTime() {
 LocalTime localTime = formatter.parseLocalTime("12:00:00");
 assertEquals(12, localTime.getHourOfDay().getValue());
 assertEquals(0, localTime.getMinuteOfHour().getValue());
 assertEquals(0, localTime.getSecondOfMinute().getValue());
 }

 @Test
 public void testParseLocalDateTime() {
 LocalDateTime localDateTime = formatter.parseLocalDateTime("2022-01-01 12:00:00");
 assertEquals(2022, localDateTime.getYear());
 assertEquals(1, localDateTime.getMonthOfYear().getValue());
 assertEquals(1, localDateTime.getDayOfMonth().getValue());
 assertEquals(12, localDateTime.getHourOfDay().getValue());
 assertEquals(0, localDateTime.getMinuteOfHour().getValue());
 assertEquals(0, localDateTime.getSecondOfMinute().getValue());
 }

 @Test
 public void testParseDateTime() {
 DateTime dateTime = formatter.parseDateTime("2022-01-01 12:00:00");
 assertEquals(2022, dateTime.getYear());
 assertEquals(1, dateTime.getMonthOfYear().getValue());
 assertEquals(1, dateTime.getDayOfMonth().getValue());
 assertEquals(12, dateTime.getHourOfDay().getValue());
 assertEquals(0, dateTime.getMinuteOfHour().getValue());
 assertEquals(0, dateTime.getSecondOfMinute().getValue());
 assertEquals(GregorianChronology.getInstance(), dateTime.getChronology());
 assertEquals(DateTimeZone.UTC, dateTime.getZone());
 }

 @Test
 public void testParseMutableDateTime() {
 MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2022-01-01 12:00:00");
 assertEquals(2022, mutableDateTime.getYear());
 assertEquals(1, mutableDateTime.getMonthOfYear().getValue());
 assertEquals(1, mutableDateTime.getDayOfMonth().getValue());
 assertEquals(12, mutableDateTime.getHourOfDay().getValue());
 assertEquals(0, mutableDateTime.getMinuteOfHour().getValue());
 assertEquals(0, mutableDateTime.getSecondOfMinute().getValue());
 assertEquals(GregorianChronology.getInstance(), mutableDateTime.getChronology());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseDateTimeInvalidFormat() {
 formatter.parseDateTime("2022-01-01");
 }

}