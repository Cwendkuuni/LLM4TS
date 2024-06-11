import org.joda.time.*;
import org.joda.time.format.*;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 private DateTimeFormatter formatter;
 private DateTimeFormatter formatterWithLocale;
 private DateTimeFormatter formatterWithOffsetParsed;
 private DateTimeFormatter formatterWithChronology;
 private DateTimeFormatter formatterWithZoneUTC;
 private DateTimeFormatter formatterWithPivotYear;
 private DateTimeFormatter formatterWithDefaultYear;

 @Before
 public void setup() {
 formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
 formatterWithLocale = formatter.withLocale(Locale.US);
 formatterWithOffsetParsed = formatter.withOffsetParsed();
 formatterWithChronology = formatter.withChronology(ISODateTimeFormat.dateTimeNoTimeZone());
 formatterWithZoneUTC = formatter.withZoneUTC();
 formatterWithPivotYear = formatter.withPivotYear(2010);
 formatterWithDefaultYear = formatter.withDefaultYear(2020);
 }

 @Test
 public void testIsPrinter() {
 assertTrue(formatter.isPrinter());
 assertTrue(formatterWithLocale.isPrinter());
 assertTrue(formatterWithOffsetParsed.isPrinter());
 assertTrue(formatterWithChronology.isPrinter());
 assertTrue(formatterWithZoneUTC.isPrinter());
 assertTrue(formatterWithPivotYear.isPrinter());
 assertTrue(formatterWithDefaultYear.isPrinter());
 }

 @Test
 public void testIsParser() {
 assertTrue(formatter.isParser());
 assertTrue(formatterWithLocale.isParser());
 assertTrue(formatterWithOffsetParsed.isParser());
 assertTrue(formatterWithChronology.isParser());
 assertTrue(formatterWithZoneUTC.isParser());
 assertTrue(formatterWithPivotYear.isParser());
 assertTrue(formatterWithDefaultYear.isParser());
 }

 @Test
 public void testGetPrinter() {
 assertEquals(formatter.getPrinter(), new DefaultDateTimePrinter());
 assertEquals(formatterWithLocale.getPrinter(), new DefaultDateTimePrinter(Locale.US));
 assertNull(formatterWithOffsetParsed.getPrinter());
 assertNull(formatterWithChronology.getPrinter());
 assertNull(formatterWithZoneUTC.getPrinter());
 assertNull(formatterWithPivotYear.getPrinter());
 assertNull(formatterWithDefaultYear.getPrinter());
 }

 @Test
 public void testGetParser() {
 assertEquals(formatter.getParser(), new DefaultDateTimeParser());
 assertEquals(formatterWithLocale.getParser(), new DefaultDateTimeParser(Locale.US));
 assertNull(formatterWithOffsetParsed.getParser());
 assertNull(formatterWithChronology.getParser());
 assertNull(formatterWithZoneUTC.getParser());
 assertNull(formatterWithPivotYear.getParser());
 assertNull(formatterWithDefaultYear.getParser());
 }

 @Test
 public void testGetLocale() {
 assertNull(formatter.getLocale());
 assertEquals(Locale.US, formatterWithLocale.getLocale());
 assertNull(formatterWithOffsetParsed.getLocale());
 assertNull(formatterWithChronology.getLocale());
 assertNull(formatterWithZoneUTC.getLocale());
 assertNull(formatterWithPivotYear.getLocale());
 assertNull(formatterWithDefaultYear.getLocale());
 }

 @Test
 public void testWithLocale() {
 DateTimeFormatter formatterWithCustomLocale = formatter.withLocale(Locale.FRANCE);
 assertNotSame(formatter, formatterWithCustomLocale);
 assertEquals(Locale.FRANCE, formatterWithCustomLocale.getLocale());
 }

 @Test
 public void testWithOffsetParsed() {
 DateTimeFormatter formatterWithParsedOffset = formatter.withOffsetParsed();
 assertNotSame(formatter, formatterWithParsedOffset);
 assertTrue(formatterWithParsedOffset.isOffsetParsed());
 }

 @Test
 public void testIsOffsetParsed() {
 assertFalse(formatter.isOffsetParsed());
 assertTrue(formatterWithOffsetParsed.isOffsetParsed());
 }

 @Test
 public void testWithChronology() {
 DateTimeFormatter formatterWithCustomChronology = formatter.withChronology(ISODateTimeFormat.dateTimeNoTimeZone());
 assertNotSame(formatter, formatterWithCustomChronology);
 assertSame(ISODateTimeFormat.dateTimeNoTimeZone(), formatterWithCustomChronology.getChronology());
 }

 @Test
 public void testGetChronology() {
 assertNull(formatter.getChronology());
 assertSame(ISODateTimeFormat.dateTimeNoTimeZone(), formatterWithChronology.getChronology());
 assertNull(formatterWithOffsetParsed.getChronology());
 assertNull(formatterWithZoneUTC.getChronology());
 assertNull(formatterWithPivotYear.getChronology());
 assertNull(formatterWithDefaultYear.getChronology());
 }

 @Test
 public void testWithZoneUTC() {
 DateTimeFormatter formatterWithUTCZone = formatter.withZoneUTC();
 assertNotSame(formatter, formatterWithUTCZone);
 assertEquals(DateTimeZone.UTC, formatterWithUTCZone.getZone());
 }

 @Test
 public void testGetZone() {
 assertNull(formatter.getZone());
 assertEquals(DateTimeZone.UTC, formatterWithZoneUTC.getZone());
 assertNull(formatterWithOffsetParsed.getZone());
 assertNull(formatterWithChronology.getZone());
 assertNull(formatterWithPivotYear.getZone());
 assertNull(formatterWithDefaultYear.getZone());
 }

 @Test
 public void testWithPivotYear() {
 DateTimeFormatter formatterWithCustomPivotYear = formatter.withPivotYear(2015);
 assertNotSame(formatter, formatterWithCustomPivotYear);
 assertEquals(2015, formatterWithCustomPivotYear.getPivotYear().intValue());
 }

 @Test
 public void testGetPivotYear() {
 assertEquals(Integer.valueOf(2000), formatter.getPivotYear());
 assertEquals(Integer.valueOf(2010), formatterWithPivotYear.getPivotYear());
 assertNull(formatterWithOffsetParsed.getPivotYear());
 assertNull(formatterWithChronology.getPivotYear());
 assertNull(formatterWithZoneUTC.getPivotYear());
 assertNull(formatterWithDefaultYear.getPivotYear());
 }

 @Test
 public void testWithDefaultYear() {
 DateTimeFormatter formatterWithCustomDefaultYear = formatter.withDefaultYear(2030);
 assertNotSame(formatter, formatterWithCustomDefaultYear);
 assertEquals(2030, formatterWithCustomDefaultYear.getDefaultYear());
 }

 @Test
 public void testGetDefaultYear() {
 assertEquals(2000, formatter.getDefaultYear());
 assertEquals(2020, formatterWithDefaultYear.getDefaultYear());
 }

 @Test
 public void testPrintTo() {
 LocalDateTime dateTime = new LocalDateTime(2022, 11, 12, 13, 14, 15, 16);
 LocalDateTime formattedDateTime = null;

 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, dateTime);
 formattedDateTime = new LocalDateTime(DateTimeFormat.parse(sb.toString()).getLong(), DateTimeUtils.getIsoDateTimeFormat().getChronology());

 assertEquals(dateTime, formattedDateTime);

 StringWriter sw = new StringWriter();
 formatter.printTo(sw, dateTime);
 formattedDateTime = new LocalDateTime(DateTimeFormat.parse(sw.toString()).getLong(), DateTimeUtils.getIsoDateTimeFormat().getChronology());

 assertEquals(dateTime, formattedDateTime);

 StringAppender sa = new StringAppender();
 formatter.printTo(sa, dateTime);
 formattedDateTime = new LocalDateTime(DateTimeFormat.parse(sa.getText()).getLong(), DateTimeUtils.getIsoDateTimeFormat().getChronology());

 assertEquals(dateTime, formattedDateTime);
 }

 @Test
 public void testPrintToLong() {
 long millis = 1640995200000L; // 2022-01-01 00:00:00

 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, millis);
 formatterWithChronology.printTo(sb, millis, formatterWithChronology, 0, DateTimeZone.UTC, null);

 assertEquals("2022-01-01T00:00:00.000Z", sb.toString());
 }

 @Test
 public void testPrintToReadablePartial() {
 ReadablePartial readablePartial = new DateTime(1640995200000L, DateTimeUtils.getIsoDateTimeFormat().getChronology()).toLocalDate();

 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, readablePartial);

 assertEquals("2022-01-01", sb.toString());
 }

 @Test
 public void testParseInto() throws Exception {
 MutableDateTime mutableDateTime = new MutableDateTime();
 ReadWritableInstant readWritableInstant = new DateTime(1640995200000L, DateTimeUtils.getIsoDateTimeFormat().getChronology());

 int into = formatter.parseInto(readWritableInstant, "2022-01-01T00:00:00.000Z", 0);
 assertEquals(22, into);

 mutableDateTime.setMillis(formatter.parseMillis("2022-01-01T00:00:00.000Z"));
 assertEquals(readWritableInstant, mutableDateTime);
 }

 @Test
 public void testParseMillis() {
 long millis = formatter.parseMillis("2022-01-01T00:00:00.000Z");
 LocalDateTime dateTime = new LocalDateTime(millis, DateTimeUtils.getIsoDateTimeFormat().getChronology());

 assertEquals("2022-01-01T00:00:00.000Z", formatter.print(dateTime));
 }

 @Test
 public void testParseLocalDate() {
 LocalDate date = formatter.parseLocalDate("2022-01-01");
 assertEquals("2022-01-01", date.toString());
 }

 @Test
 public void testParseLocalTime() {
 LocalTime time = formatter.parseLocalTime("13:14:15.167");
 assertEquals("13:14:15.167", time.toString());
 }

 @Test
 public void testParseLocalDateTime() {
 LocalDateTime datetime = formatter.parseLocalDateTime("2022-01-01T13:14:15.167");
 assertEquals("2022-01-01T13:14:15.167", formatter.print(datetime));
 }

 @Test
 public void testParseDateTime() {
 DateTime dateTime = formatter.parseDateTime("2022-01-01T13:14:15.167Z");
 assertEquals("2022-01-01T13:14:15.167Z", formatter.print(dateTime));
 }

 @Test
 public void testParseMutableDateTime() {
 MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2022-01-01T13:14:15.167Z");
 assertEquals("2022-01-01T13:14:15.167Z", formatter.print(mutableDateTime.toDateTime()));
 }

}