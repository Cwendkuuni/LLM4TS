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
import org.junit.Assert;
import org.junit.Test;

public class DateTimeFormatterTest {

 private DateTimeFormatter formatter;
 private DateTimePrinter printer;
 private DateTimeParser parser;

 @Before
 public void setup() {
 printer = new DefaultDateTimeFormatter();
 parser = new DefaultDateTimeParser();
 formatter = new DateTimeFormatter(printer, parser);
 }

 @Test
 public void testIsPrinter() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter() {
 @Override
 public void printTo(StringBuffer sb, ReadableInstant readableInstant) {
 // Implement your custom printer logic here
 }
 }, null);
 Assert.assertTrue(formatter.isPrinter());
 }

 @Test
 public void testIsParser() {
 DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeParser() {
 @Override
 public int parseInto(ReadWritableInstant readWritableInstant, String s, int n) {
 // Implement your custom parser logic here
 return 0;
 }
 });
 Assert.assertTrue(formatter.isParser());
 }

 @Test
 public void testGetPrinter() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter() {
 @Override
 public void printTo(StringBuffer sb, ReadableInstant readableInstant) {
 // Implement your custom printer logic here
 }
 }, null);
 Assert.assertEquals(printer, formatter.getPrinter());
 }

 @Test
 public void testGetParser() {
 DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeParser() {
 @Override
 public int parseInto(ReadWritableInstant readWritableInstant, String s, int n) {
 // Implement your custom parser logic here
 return 0;
 }
 });
 Assert.assertEquals(parser, formatter.getParser());
 }

 @Test
 public void testWithLocale() {
 DateTimeFormatter originalFormatter = formatter;
 Locale locale = Locale.US;
 DateTimeFormatter newFormatter = originalFormatter.withLocale(locale);
 Assert.assertNotSame(originalFormatter, newFormatter);
 Assert.assertEquals(locale, newFormatter.getLocale());
 }

 @Test
 public void testWithOffsetParsed() {
 DateTimeFormatter originalFormatter = formatter;
 DateTimeFormatter newFormatter = originalFormatter.withOffsetParsed();
 Assert.assertNotSame(originalFormatter, newFormatter);
 Assert.assertTrue(newFormatter.isOffsetParsed());
 }

 @Test
 public void testIsOffsetParsed() {
 formatter = new DateTimeFormatter(printer, parser);
 formatter.withOffsetParsed();
 Assert.assertTrue(formatter.isOffsetParsed());
 }

 @Test
 public void testWithChronology() {
 DateTimeFormatter originalFormatter = formatter;
 Chronology chronology = GregorianChronology.INSTANCE;
 DateTimeFormatter newFormatter = originalFormatter.withChronology(chronology);
 Assert.assertNotSame(originalFormatter, newFormatter);
 Assert.assertEquals(chronology, newFormatter.getChronology());
 }

 @Test
 public void testWithZoneUTC() {
 DateTimeFormatter originalFormatter = formatter;
 DateTimeFormatter newFormatter = originalFormatter.withZoneUTC();
 Assert.assertNotSame(originalFormatter, newFormatter);
 Assert.assertEquals(DateTimeZone.UTC, newFormatter.getZone());
 }

 @Test
 public void testWithZone() {
 DateTimeFormatter originalFormatter = formatter;
 DateTimeZone dateTimeZone = DateTimeZone.forOffsetHours(1);
 DateTimeFormatter newFormatter = originalFormatter.withZone(dateTimeZone);
 Assert.assertNotSame(originalFormatter, newFormatter);
 Assert.assertEquals(dateTimeZone, newFormatter.getZone());
 }

 @Test (expected = UnsupportedOperationException.class)
 public void testWithChronologyNull() {
 DateTimeFormatter originalFormatter = formatter;
 DateTimeFormatter newFormatter = originalFormatter.withChronology(null);
 }

 @Test
 public void testWithPivotYear() {
 DateTimeFormatter originalFormatter = formatter;
 Integer pivotYear = Integer.valueOf(2022);
 DateTimeFormatter newFormatter = originalFormatter.withPivotYear(pivotYear);
 Assert.assertNotSame(originalFormatter, newFormatter);
 Assert.assertEquals(pivotYear, newFormatter.getPivotYear());
 }

 @Test
 public void testWithDefaultYear() {
 DateTimeFormatter originalFormatter = formatter;
 int defaultYear = 2022;
 DateTimeFormatter newFormatter = originalFormatter.withDefaultYear(defaultYear);
 Assert.assertNotSame(originalFormatter, newFormatter);
 Assert.assertEquals(defaultYear, newFormatter.getDefaultYear());
 }

 @Test
 public void testPrintTo() {
 BaseDateTime baseDateTime = new BaseDateTime(1634567890000L, GregorianChronology.INSTANCE);
 formatter.printTo(new StringBuffer(), baseDateTime);
 String expected = "2022-01-13 15:24:50.000";
 String actual = formatter.getPrinter().print(baseDateTime);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testPrintToWriter() throws IOException {
 BaseDateTime baseDateTime = new BaseDateTime(1634567890000L, GregorianChronology.INSTANCE);
 String expected = "2022-01-13 15:24:50.000";
 StringWriter stringWriter = new StringWriter();
 formatter.printTo(stringWriter, baseDateTime);
 String actual = stringWriter.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testPrintToAppendable() throws IOException {
 BaseDateTime baseDateTime = new BaseDateTime(1634567890000L, GregorianChronology.INSTANCE);
 StringBuffer stringBuffer = new StringBuffer();
 formatter.printTo(stringBuffer, baseDateTime);
 String expected = "2022-01-13 15:24:50.000";
 String actual = stringBuffer.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testParseLocalDate() {
 String dateString = "2022-01-13";
 LocalDate expected = new LocalDate(2022, 1, 13);
 LocalDate actual = formatter.parseLocalDate(dateString);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testParseLocalTime() {
 String timeString = "15:24:50.000";
 LocalTime expected = new LocalTime(15, 24, 50, 0);
 LocalTime actual = formatter.parseLocalTime(timeString);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testParseLocalDateTime() {
 String dateTimeString = "2022-01-13 15:24:50.000";
 LocalDateTime expected = new LocalDateTime(2022, 1, 13, 15, 24, 50, 0);
 LocalDateTime actual = formatter.parseLocalDateTime(dateTimeString);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testParseDateTime() {
 String dateTimeString = "2022-01-13 15:24:50.000Z";
 DateTime expected = new DateTime(1634567890000L, GregorianChronology.getInstance());
 DateTime actual = formatter.parseDateTime(dateTimeString);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testParseMutableDateTime() {
 String dateTimeString = "2022-01-13 15:24:50.000Z";
 MutableDateTime expected = new MutableDateTime(1634567890000L, GregorianChronology.getInstance());
 MutableDateTime actual = formatter.parseMutableDateTime(dateTimeString);
 Assert.assertEquals(expected, actual);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testParseInvalidDate() {
 String invalidDateString = "2022-01-33";
 formatter.parseLocalDate(invalidDateString);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testParseInvalidTime() {
 String invalidTimeString = "24:60:00.000";
 formatter.parseLocalTime(invalidTimeString);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testParseInvalidDateTime() {
 String invalidDateTimeString = "2022-01-33 24:60:00.000";
 formatter.parseLocalDateTime(invalidDateTimeString);
 }

 @Test
 public void testParseMillis() {
 long expectedMillis = 1634567890000L;
 long actualMillis = formatter.parseMillis("2022-01-13 15:24:50.000");
 Assert.assertEquals(expectedMillis, actualMillis);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testParseInvalidMillis() {
 formatter.parseMillis("2022-01-13 15:24:50.000ZX");
 }
}