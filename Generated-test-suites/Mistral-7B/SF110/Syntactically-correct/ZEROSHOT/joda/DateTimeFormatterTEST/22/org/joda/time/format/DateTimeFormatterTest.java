import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 private DateTimeFormatter formatter;
 private DateTimePrinter printer;
 private DateTimeParser parser;

 @Before
 public void setup() {
 printer = new DefaultDateTimePrinter();
 parser = new DefaultDateTimeParser();
 formatter = new DateTimeFormatter(printer, parser);
 }

 @Test
 public void testIsPrinter() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 assertTrue(formatter.isPrinter());
 }

 @Test
 public void testIsParser() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 assertTrue(formatter.isParser());
 }

 @Test
 public void testGetPrinter() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 assertEquals(printer, formatter.getPrinter());
 }

 @Test
 public void testGetParser() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 assertEquals(parser, formatter.getParser());
 }

 @Test
 public void testWithLocale() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 Locale locale = new Locale("fr", "FR");
 DateTimeFormatter formatterWithLocale = formatter.withLocale(locale);
 assertEquals(locale, formatterWithLocale.getLocale());
 }

 @Test
 public void testWithOffsetParsed() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
 assertTrue(formatterWithOffsetParsed.isOffsetParsed());
 }

 @Test
 public void testIsOffsetParsed() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
 assertTrue(formatterWithOffsetParsed.isOffsetParsed());
 }

 @Test
 public void testWithChronology() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 Chronology chronology = DateTimeUtils.getISOChronology();
 DateTimeFormatter formatterWithChrono = formatter.withChronology(chronology);
 assertEquals(chronology, formatterWithChrono.getChronology());
 }

 @Test
 public void testWithZoneUTC() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 DateTimeFormatter formatterWithZoneUTC = formatter.withZoneUTC();
 assertEquals(DateTimeZone.UTC, formatterWithZoneUTC.getZone());
 }

 @Test
 public void testWithZone() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 DateTimeZone timeZone = DateTimeZone.forID("Europe/Paris");
 DateTimeFormatter formatterWithZone = formatter.withZone(timeZone);
 assertEquals(timeZone, formatterWithZone.getZone());
 }

 @Test
 public void testPrintToStringBuffer() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 LocalDateTime dateTime = new LocalDateTime();
 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, dateTime);
 assertTrue(sb.toString().length() > 0);
 }

 @Test
 public void testPrintToWriter() throws IOException {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 LocalDateTime dateTime = new LocalDateTime();
 Writer writer = new StringWriter();
 formatter.printTo(writer, dateTime);
 assertTrue(writer.toString().length() > 0);
 }

 @Test
 public void testPrintToAppendable() throws IOException {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 LocalDateTime dateTime = new LocalDateTime();
 Appendable appendable = new StringBuffer();
 formatter.printTo(appendable, dateTime);
 assertTrue(appendable.toString().length() > 0);
 }

 @Test
 public void testPrintToStringBufferInstant() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 Instant instant = new Instant(System.currentTimeMillis());
 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, instant);
 assertTrue(sb.toString().length() > 0);
 }

 @Test
 public void testPrintToWriterInstant() throws IOException {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 Instant instant = new Instant(System.currentTimeMillis());
 Writer writer = new StringWriter();
 formatter.printTo(writer, instant);
 assertTrue(writer.toString().length() > 0);
 }

 @Test
 public void testPrintToAppendableInstant() throws IOException {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 Instant instant = new Instant(System.currenttimeMillis());
 Appendable appendable = new StringBuffer();
 formatter.printTo(appendable, instant);
 assertTrue(appendable.toString().length() > 0);
 }

 @Test
 public void testParseIntoLocalDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 LocalDateTime dateTime = new LocalDateTime();
 String dateString = dateTime.toString();
 Instant instant = Instant.now();
 MutableDateTime mutableDateTime = new MutableDateTime();
 int result = formatter.parseInto(mutableDateTime, dateString, 0);
 assertEquals(result, dateString.length());
 assertEquals(dateTime, mutableDateTime.toLocalDateTime());
 }

 @Test
 public void testParseMillis() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 long millis = System.currentTimeMillis();
 String dateString = Long.toString(millis);
 long parsedMillis = formatter.parseMillis(dateString);
 assertEquals(millis, parsedMillis);
 }

 @Test
 public void testParseLocalDate() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 LocalDate date = new LocalDate();
 String dateString = date.toString();
 LocalDate parsedDate = formatter.parseLocalDate(dateString);
 assertEquals(date, parsedDate);
 }

 @Test
 public void testParseLocalTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 LocalTime time = new LocalTime();
 String timeString = time.toString();
 LocalTime parsedTime = formatter.parseLocalTime(timeString);
 assertEquals(time, parsedTime);
 }

 @Test
 public void testParseLocalDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 LocalDateTime dateTime = new LocalDateTime();
 String dateTimeString = dateTime.toString();
 LocalDateTime parsedDateTime = formatter.parseLocalDateTime(dateTimeString);
 assertEquals(dateTime, parsedDateTime);
 }

 @Test
 public void testParseDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 DateTime dateTime = new DateTime();
 String dateTimeString = dateTime.toString();
 DateTime parsedDateTime = formatter.parseDateTime(dateTimeString);
 assertEquals(dateTime, parsedDateTime);
 }

 @Test
 public void testParseMutableDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 MutableDateTime mutableDateTime = new MutableDateTime();
 String dateTimeString = "2022-03-30T14:30:00+01:00";
 int result = formatter.parseInto(mutableDateTime, dateTimeString, 0);
 assertEquals(result, dateTimeString.length());
 assertEquals(mutableDateTime.getYear(), 2022);
 assertEquals(mutableDateTime.getMonthOfYear(), 3);
 assertEquals(mutableDateTime.getDayOfMonth(), 30);
 assertEquals(mutableDateTime.getHourOfDay(), 14);
 assertEquals(mutableDateTime.getMinuteOfHour(), 30);
 assertEquals(mutableDateTime.getSecondOfMinute(), 0);
 assertEquals(mutableDateTime.getMillisOfSecond(), 0);
 assertEquals(mutableDateTime.getZone(), DateTimeZone.forID("Europe/Berlin"));
 }
}