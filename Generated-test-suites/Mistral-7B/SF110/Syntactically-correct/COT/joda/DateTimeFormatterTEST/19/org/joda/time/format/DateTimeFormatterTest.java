import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 private DateTimeFormatter formatter;

 @Before
 public void setUp() {
 formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
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
 assertEquals(formatter.getPrinter(), new SimpleDateTimePrinter());
 }

 @Test
 public void testGetParser() {
 assertEquals(formatter.getParser(), new SimpleDateTimeParser());
 }

 @Test
 public void testWithLocale() {
 DateTimeFormatter formatterWithLocale = formatter.withLocale(Locale.ENGLISH);
 assertNotEquals(formatter, formatterWithLocale);
 assertEquals(formatterWithLocale.getLocale(), Locale.ENGLISH);
 }

 @Test
 public void testIsOffsetParsed() {
 assertFalse(formatter.isOffsetParsed());
 }

 @Test
 public void testWithOffsetParsed() {
 DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
 assertTrue(formatterWithOffsetParsed.isOffsetParsed());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPrintToNullStringBuffer() {
 formatter.printTo(null, new InstantMillis(System.currentTimeMillis()));
 }

 @Test
 public void testPrintToInstant() {
 String expected = "2023-03-10T15:30:45.000Z";
 StringBuffer stringBuffer = new StringBuffer(expected.length());
 formatter.printTo(stringBuffer, new InstantMillis(System.currentTimeMillis()));
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testPrintToNullWriter() {
 try {
 formatter.printTo(null, new InstantMillis(System.currentTimeMillis()));
 fail("IOException expected");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testPrintToWriterInstant() throws IOException {
 String expected = "2023-03-10T15:30:45.000Z";
 Writer writer = new StringWriter();
 formatter.printTo(writer, new InstantMillis(System.currentTimeMillis()));
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testParseIntoNullReadWritableInstant() {
 try {
 formatter.parseInto(null, "", 0);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseMillisNullString() {
 try {
 formatter.parseMillis(null);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseMillisValidString() {
 long expected = 1677746145000L;
 long actual = formatter.parseMillis("2023-03-10 15:30:45");
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLocalDateNullString() {
 try {
 formatter.parseLocalDate(null);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseLocalDateValidString() {
 LocalDate expected = new LocalDate(2023, 3, 10);
 LocalDate actual = formatter.parseLocalDate("2023-03-10");
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLocalTimeNullString() {
 try {
 formatter.parseLocalTime(null);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseLocalTimeValidString() {
 LocalTime expected = new LocalTime(15, 30);
 LocalTime actual = formatter.parseLocalTime("15:30");
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLocalDateTimeNullString() {
 try {
 formatter.parseLocalDateTime(null);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseLocalDateTimeValidString() {
 LocalDateTime expected = new LocalDateTime(2023, 3, 10, 15, 30);
 LocalDateTime actual = formatter.parseLocalDateTime("2023-03-10T15:30");
 assertEquals(expected, actual);
 }

 @Test
 public void testParseDateTimeNullString() {
 try {
 formatter.parseDateTime(null);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseDateTimeValidString() {
 DateTime expected = new DateTime(2023, 3, 10, 15, 30, 0, 0, DateTimeZone.UTC);
 DateTime actual = formatter.parseDateTime("2023-03-10T15:30:00Z");
 assertEquals(expected, actual);
 }

 @Test
 public void testParseMutableDateTimeNullString() {
 try {
 formatter.parseMutableDateTime(null);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseMutableDateTimeValidString() {
 MutableDateTime expected = new MutableDateTime(2023, 3, 10, 15, 30, 0, 0, DateTimeZone.UTC);
 MutableDateTime actual = formatter.parseMutableDateTime("2023-03-10T15:30:00Z");
 assertEquals(expected, actual);
 }
}