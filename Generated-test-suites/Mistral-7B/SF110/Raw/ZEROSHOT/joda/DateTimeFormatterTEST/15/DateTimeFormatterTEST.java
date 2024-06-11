import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Assert.*;

public class DateTimeFormatterTest {

 private DateTimeFormatter formatter;
 private DateTimeFormatter formatterWithLocale;
 private DateTimeFormatter formatterWithOffsetParsed;
 private DateTimeFormatter formatterWithChronology;
 private DateTimeFormatter formatterWithZoneUTC;
 private DateTimeFormatter formatterWithZone;
 private DateTimeFormatter formatterWithPivotYear;
 private DateTimeFormatter formatterWithDefaultYear;

 @Before
 public void setUp() {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
 formatterWithLocale = formatter.withLocale(Locale.US);
 formatterWithOffsetParsed = formatter.withOffsetParsed();
 formatterWithChronology = formatter.withChronology(ISOChronology.getInstance());
 formatterWithZoneUTC = formatter.withZoneUTC();
 formatterWithZone = formatter.withZone(DateTimeZone.forOffsetHours(5));
 formatterWithPivotYear = formatter.withPivotYear(2020);
 formatterWithDefaultYear = formatter.withDefaultYear(2021);
 }

 @Test
 public void testIsPrinter() {
 assertTrue(formatter.isPrinter());
 assertTrue(formatterWithLocale.isPrinter());
 assertTrue(formatterWithOffsetParsed.isPrinter());
 assertTrue(formatterWithChronology.isPrinter());
 assertTrue(formatterWithZoneUTC.isPrinter());
 assertTrue(formatterWithZone.isPrinter());
 assertTrue(formatterWithPivotYear.isPrinter());
 assertTrue(formatterWithDefaultYear.isPrinter());
 }

 @Test
 public void testIsParser() {
 assertFalse(formatter.isParser());
 assertFalse(formatterWithLocale.isParser());
 assertFalse(formatterWithOffsetParsed.isParser());
 assertFalse(formatterWithChronology.isParser());
 assertFalse(formatterWithZoneUTC.isParser());
 assertFalse(formatterWithZone.isParser());
 assertFalse(formatterWithPivotYear.isParser());
 assertFalse(formatterWithDefaultYear.isParser());
 }

 @Test
 public void testGetPrinter() {
 assertEquals(formatter.getPrinter(), formatter.iPrinter);
 assertEquals(formatterWithLocale.getPrinter(), formatterWithLocale.iPrinter);
 assertEquals(formatterWithOffsetParsed.getPrinter(), formatterWithOffsetParsed.iPrinter);
 assertEquals(formatterWithChronology.getPrinter(), formatterWithChronology.iPrinter);
 assertEquals(formatterWithZoneUTC.getPrinter(), formatterWithZoneUTC.iPrinter);
 assertEquals(formatterWithZone.getPrinter(), formatterWithZone.iPrinter);
 assertEquals(formatterWithPivotYear.getPrinter(), formatterWithPivotYear.iPrinter);
 assertEquals(formatterWithDefaultYear.getPrinter(), formatterWithDefaultYear.iPrinter);
 }

 @Test
 public void testGetParser() {
 assertEquals(formatter.getParser(), formatter.iParser);
 assertEquals(formatterWithLocale.getParser(), formatterWithLocale.iParser);
 assertEquals(formatterWithOffsetParsed.getParser(), formatterWithOffsetParsed.iParser);
 assertEquals(formatterWithChronology.getParser(), formatterWithChronology.iParser);
 assertEquals(formatterWithZoneUTC.getParser(), formatterWithZoneUTC.iParser);
 assertEquals(formatterWithZone.getParser(), formatterWithZone.iParser);
 assertEquals(formatterWithPivotYear.getParser(), formatterWithPivotYear.iParser);
 assertEquals(formatterWithDefaultYear.getParser(), formatterWithDefaultYear.iParser);
 }

 @Test
 public void testGetLocale() {
 assertEquals(formatter.getLocale(), null);
 assertEquals(formatterWithLocale.getLocale(), Locale.US);
 }

 @Test
 public void testGetOffsetParsed() {
 assertEquals(formatter.isOffsetParsed(), false);
 assertEquals(formatterWithOffsetParsed.isOffsetParsed(), true);
 }

 @Test
 public void testGetChronology() {
 assertEquals(formatter.getChronology(), null);
 assertEquals(formatterWithChronology.getChronology(), ISOChronology.getInstance());
 }

 @Test
 public void testGetZone() {
 assertEquals(formatter.getZone(), null);
 assertEquals(formatterWithZoneUTC.getZone(), DateTimeZone.UTC);
 assertEquals(formatterWithZone.getZone(), DateTimeZone.forOffsetHours(5));
 }

 @Test
 public void testGetPivotYear() {
 assertEquals(formatter.getPivotYear(), null);
 assertEquals(formatterWithPivotYear.getPivotYear(), Integer.valueOf(2020));
 }

 @Test
 public void testHasDefaultYear() {
 assertEquals(formatter.getDefaultYear(), 2000);
 assertEquals(formatterWithDefaultYear.getDefaultYear(), 2021);
 }

 @Test
 public void testPrintToStringBuffer() {
 LocalDateTime dateTime = LocalDateTime.parse("2020-01-01T12:30:45");
 String expected = "2020-01-01T12:30:45";
 StringBuffer stringBuffer = new StringBuffer(100);
 formatter.printTo(stringBuffer, dateTime);
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testPrintToWriter() throws IOException {
 Writer writer = new StringWriter();
 LocalDateTime dateTime = LocalDateTime.parse("2020-01-01T12:30:45");
 formatter.printTo(writer, dateTime);
 String expected = "2020-01-01T12:30:45";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testPrintToAppendable() throws IOException {
 Appendable appendable = new StringBuffer();
 LocalDateTime dateTime = LocalDateTime.parse("2020-01-01T12:30:45");
 formatter.printTo(appendable, dateTime);
 String expected = "2020-01-01T12:30:45";
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testPrintToStringBufferWithLong() {
 long millis = 1609459200000L;
 String expected = "2021-01-20T14:00:00Z";
 StringBuffer stringBuffer = new StringBuffer(100);
 formatter.printTo(stringBuffer, millis);
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testPrintToWriterWithLong() throws IOException {
 Writer writer = new StringWriter();
 long millis = 1609459200000L;
 formatter.printTo(writer, millis);
 String expected = "2021-01-20T14:00:00Z";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testPrintToAppendableWithLong() throws IOException {
 Appendable appendable = new StringBuffer();
 long millis = 1609459200000L;
 formatter.printTo(appendable, millis);
 String expected = "2021-01-20T14:00:00Z";
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testPrintToStringBufferWithReadablePartial() {
 LocalDateTime dateTime = LocalDateTime.parse("2020-01-01T12:30:45");
 LocalDateTime dateTimeWithOffset = dateTime.withOffsetHours(5);
 String expected = "12:30:45.000";
 StringBuffer stringBuffer = new StringBuffer(100);
 formatter.printTo(stringBuffer, dateTime.toLocalDate());
 formatter.printTo(stringBuffer, dateTimeWithOffset.getHourOfDay());
 formatter.printTo(stringBuffer, dateTimeWithOffset.getMinuteOfHour());
 formatter.printTo(stringBuffer, dateTimeWithOffset.getSecondOfMinute());
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testParseIntoReadWritableInstant() {
 ReadWritableInstant readWritableInstant = new GregorianMutableDateTime();
 int result = formatter.parseInto(readWritableInstant, "2020-01-01T12:30:45", 0);
 assertEquals(result, 0);
 assertEquals(readWritableInstant.getMillis(), 1623312245000L);
 }

 @Test
 public void testParseMillis() {
 long millis = 1609459200000L;
 long result = formatter.parseMillis("2021-01-20T14:00:00Z");
 assertEquals(millis, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMillisInvalidFormat() {
 formatter.parseMillis("InvalidFormat");
 }

 @Test
 public void testParseLocalDate() {
 LocalDate expected = LocalDate.of(2020, 1, 1);
 LocalDate result = formatter.parseLocalDate("2020-01-01");
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateInvalidFormat() {
 formatter.parseLocalDate("InvalidFormat");
 }

 @Test
 public void testParseLocalTime() {
 LocalTime expected = LocalTime.of(12, 30, 45);
 LocalTime result = formatter.parseLocalTime("12:30:45");
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalTimeInvalidFormat() {
 formatter.parseLocalTime("InvalidFormat");
 }

 @Test
 public void testParseLocalDateTime() {
 LocalDateTime expected = LocalDateTime.parse("2020-01-01T12:30:45");
 LocalDateTime result = formatter.parseLocalDateTime("2020-01-01T12:30:45");
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateTimeInvalidFormat() {
 formatter.parseLocalDateTime("InvalidFormat");
 }

 @Test
 public void testParseDateTime() {
 DateTime expected = new DateTime(1623312245000L, DateTimeZone.UTC);
 DateTime result = formatter.parseDateTime("2021-01-20T14:00:00Z");
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseDateTimeInvalidFormat() {
 formatter.parseDateTime("InvalidFormat");
 }

 @Test
 public void testParseMutableDateTime() {
 MutableDateTime expected = new MutableDateTime(1623312245000L, DateTimeZone.UTC);
 MutableDateTime result = formatter.parseMutableDateTime("2021-01-20T14:00:00Z");
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMutableDateTimeInvalidFormat() {
 formatter.parseMutableDateTime("InvalidFormat");
 }
}