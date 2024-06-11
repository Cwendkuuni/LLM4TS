import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 private DateTimeFormatter formatter;

 @Before
 public void setup() {
 formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
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
 public void testGetPrinter() {
 assertNotNull(formatter.getPrinter());
 }

 @Test
 public void testGetParser() {
 assertNotNull(formatter.getParser());
 }

 @Test
 public void testWithLocale() {
 DateTimeFormatter newFormatter = formatter.withLocale(Locale.US);
 assertNotSame(formatter, newFormatter);
 assertEquals(Locale.US, newFormatter.getLocale());
 }

 @Test
 public void testWithOffsetParsed() {
 DateTimeFormatter newFormatter = formatter.withOffsetParsed();
 assertNotSame(formatter, newFormatter);
 assertTrue(newFormatter.isOffsetParsed());
 }

 @Test
 public void testWithChronology() {
 DateTimeFormatter newFormatter = formatter.withChronology(ISODateTimeFormat.dateTimeParser());
 assertNotSame(formatter, newFormatter);
 assertEquals(ISODateTimeFormat.dateTimeParser(), newFormatter.getChronology());
 }

 @Test
 public void testGetChronology() {
 assertEquals(null, formatter.getChronology());
 }

 @Test
 public void testPrintToStringBuffer() {
 Instant instant = new Instant(1357041600000L); // Thu, 01 Jan 2004 00:00:00 GMT
 String expected = "2004-01-01T00:00:00.000Z";
 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, instant);
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testPrintToWriter() throws IOException {
 StringWriter sw = new StringWriter();
 Writer writer = sw;
 Instant instant = new Instant(1357041600000L); // Thu, 01 Jan 2004 00:00:00 GMT
 formatter.printTo(writer, instant);
 String result = sw.toString();
 String expected = "2004-01-01T00:00:00.000Z";
 assertEquals(expected, result);
 }

 @Test
 public void testPrintToAppendable() throws IOException {
 StringWriter sw = new StringWriter();
 Appendable appendable = sw;
 Instant instant = new Instant(1357041600000L); // Thu, 01 Jan 2004 00:00:00 GMT
 formatter.printTo(appendable, instant);
 String result = sw.toString();
 String expected = "2004-01-01T00:00:00.000Z";
 assertEquals(expected, result);
 }

 @Test
 public void testPrintToStringBufferWithLocale() {
 Locale locale = Locale.US;
 DateTimeFormatter newFormatter = formatter.withLocale(locale);
 Instant instant = new Instant(1357041600000L); // Thu, 01 Jan 2004 00:00:00 GMT
 String expected = "01-Jan-2004";
 StringBuffer sb = new StringBuffer();
 newFormatter.printTo(sb, instant);
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testPrintToWriterWithLocale() throws IOException {
 Locale locale = Locale.US;
 DateTimeFormatter newFormatter = formatter.withLocale(locale);
 Instant instant = new Instant(1357041600000L); // Thu, 01 Jan 2004 00:00:00 GMT
 StringWriter sw = new StringWriter();
 Writer writer = sw;
 newFormatter.printTo(writer, instant);
 String result = sw.toString();
 String expected = "01-Jan-2004";
 assertEquals(expected, result);
 }

 @Test
 public void testPrintToAppendableWithLocale() throws IOException {
 Locale locale = Locale.US;
 DateTimeFormatter newFormatter = formatter.withLocale(locale);
 Instant instant = new Instant(1357041600000L); // Thu, 01 Jan 2004 00:00:00 GMT
 StringWriter sw = new StringWriter();
 Appendable appendable = sw;
 newFormatter.printTo(appendable, instant);
 String result = sw.toString();
 String expected = "01-Jan-2004";
 assertEquals(expected, result);
 }

 @Test
 public void testPrintToStringBufferWithOffsetParsed() {
 DateTimeFormatter newFormatter = formatter.withOffsetParsed();
 Instant instant = new Instant(1357041600000L - 3600000); // Wed, 31 Dec 2003 23:00:00 GMT
 String expected = "2003-12-31T23:00:00.000-01:00";
 StringBuffer sb = new StringBuffer();
 newFormatter.printTo(sb, instant);
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testPrintToWriterWithOffsetParsed() throws IOException {
 DateTimeFormatter newFormatter = formatter.withOffsetParsed();
 Instant instant = new Instant(1357041600000L - 3600000); // Wed, 31 Dec 2003 23:00:00 GMT
 StringWriter sw = new StringWriter();
 Writer writer = sw;
 newFormatter.printTo(writer, instant);
 String result = sw.toString();
 String expected = "2003-12-31T23:00:00.000-01:00";
 assertEquals(expected, result);
 }

 @Test
 public void testPrintToAppendableWithOffsetParsed() throws IOException {
 DateTimeFormatter newFormatter = formatter.withOffsetParsed();
 Instant instant = new Instant(1357041600000L - 3600000); // Wed, 31 Dec 2003 23:00:00 GMT
 StringWriter sw = new StringWriter();
 Appendable appendable = sw;
 newFormatter.printTo(appendable, instant);
 String result = sw.toString();
 String expected = "2003-12-31T23:00:00.000-01:00";
 assertEquals(expected, result);
 }

 @Test
 public void testParseIntoInstant() {
 Instant expected = new Instant(1357041600000L); // Thu, 01 Jan 2004 00:00:00 GMT
 ReadWritableInstant readWritableInstant = new Instant();
 int numParsed = formatter.parseInto(readWritableInstant, "2004-01-01T00:00:00.000Z", 0);
 assertEquals(numParsed, 12);
 assertEquals(expected, readWritableInstant.getValue());
 }

 @Test
 public void testParseIntoInstantWithOffset() {
 Instant expected = new Instant(1357041600000L - 3600000); // Wed, 31 Dec 2003 23:00:00 GMT
 ReadWritableInstant readWritableInstant = new Instant();
 int numParsed = formatter.parseInto(readWritableInstant, "2003-12-31T23:00:00.000-01:00", 0);
 assertEquals(numParsed, 13);
 assertEquals(expected, readWritableInstant.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIntoInstantInvalidFormat() {
 ReadWritableInstant readWritableInstant = new Instant();
 int numParsed = formatter.parseInto(readWritableInstant, "invalidFormat", 0);
 fail();
 }

 @Test
 public void testParseMillis() {
 long expected = 1357041600000L; // Thu, 01 Jan 2004 00:00:00 GMT
 long result = formatter.parseMillis("2004-01-01T00:00:00.000Z");
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMillisInvalidFormat() {
 long result = formatter.parseMillis("invalidFormat");
 fail();
 }

 @Test
 public void testParseLocalDate() {
 LocalDate expected = new LocalDate(2004, 1, 1);
 LocalDate result = formatter.parseLocalDate("2004-01-01");
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateInvalidFormat() {
 LocalDate result = formatter.parseLocalDate("invalidFormat");
 fail();
 }

 @Test
 public void testParseLocalTime() {
 LocalTime expected = new LocalTime(0, 0);
 LocalTime result = formatter.parseLocalTime("00:00");
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalTimeInvalidFormat() {
 LocalTime result = formatter.parseLocalTime("invalidFormat");
 fail();
 }

 @Test
 public void testParseLocalDateTime() {
 LocalDateTime expected = new LocalDateTime(2004, 1, 1, 0, 0);
 LocalDateTime result = formatter.parseLocalDateTime("2004-01-01T00:00");
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateTimeInvalidFormat() {
 LocalDateTime result = formatter.parseLocalDateTime("invalidFormat");
 fail();
 }

 @Test
 public void testParseDateTime() {
 DateTime expected = new DateTime(1357041600000L); // Thu, 01 Jan 2004 00:00:00 GMT
 DateTime result = formatter.parseDateTime("2004-01-01T00:00:00.000Z");
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseDateTimeInvalidFormat() {
 DateTime result = formatter.parseDateTime("invalidFormat");
 fail();
 }

 @Test
 public void testParseMutableDateTime() {
 MutableDateTime expected = new MutableDateTime(1357041600000L); // Thu, 01 Jan 2004 00:00:00 GMT
 MutableDateTime result = formatter.parseMutableDateTime("2004-01-01T00:00:00.000Z");
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMutableDateTimeInvalidFormat() {
 MutableDateTime result = formatter.parseMutableDateTime("invalidFormat");
 fail();
 }
}