import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeField;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 private DateTimeFormatter formatter;

 @Before
 public void setUp() {
 formatter = new DateTimeFormatter()
 .withLocale(Locale.US)
 .withOffsetParsed()
 .withZoneUTC()
 .withChronology(DateTimeUtils.getISO8601UTC());
 }

 @Test
 public void testIsPrinter() {
 assertTrue(formatter.isPrinter());
 }

 @Test
 public void testIsParser() {
 assertTrue(formatter.isParser());
 }

 @Test
 public void testWithLocale() {
 DateTimeFormatter formatterWithLocale = formatter.withLocale(Locale.FRENCH);
 assertEquals(Locale.FRENCH, formatterWithLocale.getLocale());
 }

 @Test
 public void testWithOffsetParsed() {
 DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
 assertTrue(formatterWithOffsetParsed.isOffsetParsed());
 }

 @Test
 public void testWithChronology() {
 DateTimeFormatter formatterWithChronology = formatter.withChronology(DateTimeUtils.getISO8601Date());
 assertEquals(DateTimeUtils.getISO8601Date(), formatterWithChronology.getChronology());
 }

 @Test
 public void testWithZoneUTC() {
 DateTimeFormatter formatterWithZoneUTC = formatter.withZoneUTC();
 assertEquals(DateTimeZone.UTC, formatterWithZoneUTC.getZone());
 }

 @Test
 public void testWithZone() {
 DateTimeFormatter formatterWithZone = formatter.withZone(DateTimeZone.forOffsetHours(3));
 assertEquals(DateTimeZone.forOffsetHours(3), formatterWithZone.getZone());
 }

 @Test
 public void testWithPivotYear() {
 DateTimeFormatter formatterWithPivotYear = formatter.withPivotYear(2025);
 assertEquals(Integer.valueOf(2025), formatterWithPivotYear.getPivotYear());
 }

 @Test
 public void testWithDefaultYear() {
 DateTimeFormatter formatterWithDefaultYear = formatter.withDefaultYear(2019);
 assertEquals(2019, formatterWithDefaultYear.getDefaultYear());
 }

 @Test
 public void testPrintToStringBuffer() {
 LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC);
 String expected = "2022-01-01T12:30:00.000";
 StringBuffer stringBuffer = new StringBuffer(19);
 formatter.printTo(stringBuffer, localDateTime);
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testPrintToWriter() throws IOException {
 LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC);
 String expected = "2022-01-01T12:30:00.000";
 StringWriter stringWriter = new StringWriter();
 formatter.printTo(stringWriter, localDateTime);
 assertEquals(expected, stringWriter.toString());
 }

 @Test
 public void testPrintToAppendable() throws IOException {
 LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC);
 StringBuilder stringBuilder = new StringBuilder(19);
 formatter.printTo(stringBuilder, localDateTime);
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testPrintToStringBufferLong() {
 long millis = DateTimeConstants.MILLIS_PER_DAY * DateTimeConstants.DAYS_PER_YEAR * 2;
 String expected = "1971-01-01T00:00:02Z";
 StringBuffer stringBuffer = new StringBuffer(23);
 formatter.printTo(stringBuffer, millis);
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testPrintToWriterLong() throws IOException {
 long millis = DateTimeConstants.MILLIS_PER_DAY * DateTimeConstants.DAYS_PER_YEAR * 2;
 String expected = "1971-01-01T00:00:02Z";
 StringWriter stringWriter = new StringWriter();
 formatter.printTo(stringWriter, millis);
 assertEquals(expected, stringWriter.toString());
 }

 @Test
 public void testPrintToAppendableLong() throws IOException {
 long millis = DateTimeConstants.MILLIS_PER_DAY * DateTimeConstants.DAYS_PER_YEAR * 2;
 StringBuilder stringBuilder = new StringBuilder(23);
 formatter.printTo(stringBuilder, millis);
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testPrintToStringBufferReadablePartial() {
 LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC);
 ReadablePartial readablePartial = new LocalTime(13, 45);
 String expected = "13:45";
 StringBuffer stringBuffer = new StringBuffer(7);
 formatter.printTo(stringBuffer, readablePartial);
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testPrintToWriterReadablePartial() throws IOException {
 LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC);
 ReadablePartial readablePartial = new LocalTime(13, 45);
 String expected = "13:45";
 StringWriter stringWriter = new StringWriter();
 formatter.printTo(stringWriter, readablePartial);
 assertEquals(expected, stringWriter.toString());
 }

 @Test
 public void testPrintToAppendableReadablePartial() throws IOException {
 LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC);
 ReadablePartial readablePartial = new LocalTime(13, 45);
 StringBuilder stringBuilder = new StringBuilder(7);
 formatter.printTo(stringBuilder, readablePartial);
 assertEquals(expected, stringBuilder.toString());
 }

 @Test
 public void testPrint() {
 LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC);
 String expected = "2022-01-01T12:30:00.000";
 assertEquals(expected, formatter.print(localDateTime));
 }

 @Test
 public void testPrintLong() {
 long millis = DateTimeConstants.MILLIS_PER_DAY * DateTimeConstants.DAYS_PER_YEAR * 2;
 String expected = "1971-01-01T00:00:02Z";
 assertEquals(expected, formatter.print(millis));
 }

 @Test
 public void testPrintReadablePartial() {
 LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC);
 ReadablePartial readablePartial = new LocalTime(13, 45);
 String expected = "13:45";
 assertEquals(expected, formatter.print(readablePartial));
 }

 @Test
 public void testParseIntoReadableInstant() {
 ReadableInstant readableInstant = DateTimeUtils.parseLocalDate("2022-01-01").toInstant();
 String dateString = "2022-01-01T12:30:00.000";
 int into = formatter.parseInto(readableInstant, dateString, DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
 assertEquals(0, into);
 assertEquals(DateTimeUtils.parseLocalDate("2022-01-01").toInstant(), readableInstant);
 }

 @Test
 public void testParseMillis() {
 String dateString = "2022-01-01T12:30:00.000";
 long millis = formatter.parseMillis(dateString);
 assertEquals(DateTimeConstants.MILLIS_PER_DAY * DateTimeConstants.DAYS_PER_YEAR * 2 + DateTimeConstants.MILLIS_PER_HOUR * 12 + DateTimeConstants.MILLIS_PER_MINUTE * 30 + DateTimeConstants.MILLIS_PER_SECOND * 60 + DateTimeConstants.MILLIS_PER_MILLISECOND * 0, millis);
 }

 @Test
 public void testParseLocalDate() {
 String dateString = "2022-01-01";
 LocalDateTime localDateTime = formatter.parseLocalDate(dateString);
 assertEquals(LocalDateTime.parse(dateString + "T00:00:00.000Z"), localDateTime);
 }

 @Test
 public void testParseLocalTime() {
 String timeString = "13:45";
 LocalDateTime localDateTime = formatter.parseLocalDateTime(timeString);
 assertEquals(LocalDateTime.parse("2000-01-01T" + timeString + ":00.000Z"), localDateTime);
 }

 @Test
 public void testParseLocalDateTime() {
 String dateTimeString = "2022-01-01T12:30:00.000";
 LocalDateTime localDateTime = formatter.parseLocalDateTime(dateTimeString);
 assertEquals(LocalDateTime.parse(dateTimeString), localDateTime);
 }

 @Test
 public void testParseMutableDateTime() {
 String dateTimeString = "2022-01-01T12:30:00.000";
 MutableDateTime mutableDateTime = formatter.parseMutableDateTime(dateTimeString);
 assertEquals(LocalDateTime.parse(dateTimeString), new LocalDateTime(mutableDateTime.getMillis(), mutableDateTime.getChronology()));
 }
}