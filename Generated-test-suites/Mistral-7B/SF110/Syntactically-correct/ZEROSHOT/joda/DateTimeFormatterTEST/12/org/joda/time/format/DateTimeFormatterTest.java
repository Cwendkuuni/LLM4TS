import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 @BeforeClass
 public static void setup() {
 // Set up any class level variables or initialization here
 }

 @AfterClass
 public static void tearDown() {
 // Clean up any class level resources here
 }

 @Before
 public void setUp() {
 // Set up any instance variables or initialization here
 }

 @After
 public void tearDown() {
 // Clean up any instance resources here
 }

 @Test
 public void testNewInstance() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 assertTrue(formatter.isPrinter());
 assertFalse(formatter.isParser());
 assertNull(formatter.getLocale());
 assertFalse(formatter.isOffsetParsed());
 assertNull(formatter.getChronology());
 assertNull(formatter.getZone());
 assertNull(formatter.getPivotYear());
 assertEquals(2000, formatter.getDefaultYear());
 }

 @Test
 public void testNewInstanceWithLocale() {
 Locale locale = Locale.US;
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser(), locale);
 assertEquals(locale, formatter.getLocale());
 }

 @Test
 public void testNewInstanceWithParserAndLocale() {
 Locale locale = Locale.US;
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser(), locale, true, null, null, null, 2000);
 assertTrue(formatter.isPrinter());
 assertTrue(formatter.isParser());
 assertEquals(locale, formatter.getLocale());
 assertTrue(formatter.isOffsetParsed());
 assertNull(formatter.getChronology());
 assertNull(formatter.getZone());
 assertNull(formatter.getPivotYear());
 assertEquals(2000, formatter.getDefaultYear());
 }

 @Test
 public void testIsPrinter() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 assertTrue(formatter.isPrinter());
 }

 @Test
 public void testIsParser() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 assertFalse(formatter.isParser());
 }

 @Test
 public void testGetPrinter() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 assertEquals(formatter.getPrinter(), formatter.iPrinter);
 }

 @Test
 public void testGetParser() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 assertEquals(formatter.getParser(), formatter.iParser);
 }

 @Test
 public void testGetLocale() {
 Locale locale = Locale.US;
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser(), locale);
 assertEquals(locale, formatter.getLocale());
 }

 @Test
 public void testGetOffsetParsed() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 assertFalse(formatter.isOffsetParsed());
 }

 @Test
 public void testGetChronology() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 assertNull(formatter.getChronology());
 }

 @Test
 public void testGetChronologyDeprecated() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 assertEquals(formatter.getChronology(), formatter.getChronolgy());
 }

 @Test
 public void testGetZone() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 assertNull(formatter.getZone());
 }

 @Test
 public void testGetPivotYear() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 assertNull(formatter.getPivotYear());
 }

 @Test
 public void testGetDefaultYear() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 assertEquals(2000, formatter.getDefaultYear());
 }

 @Test
 public void testPrintToStringBuffer() throws Exception {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 Instant instant = new Instant(1473273600000L);
 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, instant);
 String expected = "2016-11-14T13:30:00.000Z";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testPrintToWriter() throws Exception {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 Instant instant = new Instant(1473273600000L);
 StringWriter sw = new StringWriter();
 formatter.printTo(sw, instant);
 String expected = "2016-11-14T13:30:00.000Z";
 assertEquals(expected, sw.toString());
 }

 @Test
 public void testPrintToAppendable() throws Exception {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 Instant instant = new Instant(1473273600000L);
 StringWriter sw = new StringWriter();
 Appendable appendable = sw;
 formatter.printTo(appendable, instant);
 String expected = "2016-11-14T13:30:00.000Z";
 assertEquals(expected, sw.toString());
 }

 @Test
 public void testPrintToStringBufferWithOffset() throws Exception {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser(), Locale.US, true, null, DateTimeZone.forOffsetHours(5), null, 2000);
 Instant instant = new Instant(1473273600000L);
 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, instant);
 String expected = "2016-11-14T18:30:00.000+05:00";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testPrintToWriterWithOffset() throws Exception {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser(), Locale.US, true, null, DateTimeZone.forOffsetHours(5), null, 2000);
 Instant instant = new Instant(1473273600000L);
 StringWriter sw = new StringWriter();
 formatter.printTo(sw, instant);
 String expected = "2016-11-14T18:30:00.000+05:00";
 assertEquals(expected, sw.toString());
 }

 @Test
 public void testPrintToAppendableWithOffset() throws Exception {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser(), Locale.US, true, null, DateTimeZone.forOffsetHours(5), null, 2000);
 Instant instant = new Instant(1473273600000L);
 StringWriter sw = new StringWriter();
 Appendable appendable = sw;
 formatter.printTo(appendable, instant);
 String expected = "2016-11-14T18:30:00.000+05:00";
 assertEquals(expected, sw.toString());
 }

 @Test
 public void testParseIntoReadWritableInstant() throws Exception {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 ReadWritableInstant rwi = new DateTime(1473273600000L, DateTimeUtils.UTC);
 String dateString = "2016-11-14T13:30:00.000Z";
 int result = formatter.parseInto(rwi, dateString, 0);
 assertEquals(dateString.length(), result);
 assertEquals(1473273600000L, rwi.getMillis());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIntoReadWritableInstantInvalidFormat() throws Exception {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 ReadWritableInstant rwi = new DateTime(1473273600000L, DateTimeUtils.UTC);
 String dateString = "InvalidFormat";
 int result = formatter.parseInto(rwi, dateString, 0);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseMillis() throws Exception {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 long millis = 1473273600000L;
 String dateString = "2016-11-14T13:30:00.000Z";
 DateTime instant = formatter.parseMillis(dateString);
 assertEquals(instant.getMillis(), millis);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMillisInvalidFormat() throws Exception {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 long millis = 1473273600000L;
 String dateString = "InvalidFormat";
 DateTime instant = formatter.parseMillis(dateString);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseLocalDate() throws Exception {
 DateTimeFormatter formatter = new DefaultDateTimeFormatter();
 String dateString = "2016-11-14";
 LocalDate date = formatter.parseLocalDate(dateString);
 assertEquals(new LocalDate(2016, 11, 14), date);
 }

 @Test
 public void testParseLocalTime() throws Exception {
 DateTimeFormatter formatter = new DefaultDateTimeFormatter();
 String timeString = "13:30:00.000";
 LocalTime time = formatter.parseLocalTime(timeString);
 assertEquals(new LocalTime(13, 30, 0), time);
 }

 @Test
 public void testParseLocalDateTime() throws Exception {
 DateTimeFormatter formatter = new DefaultDateTimeFormatter();
 String datetimeString = "2016-11-14T13:30:00.000Z";
 LocalDateTime datetime = formatter.parseLocalDateTime(datetimeString);
 assertEquals(new LocalDateTime(2016, 11, 14, 13, 30), datetime);
 }

 @Test
 public void testParseDateTime() throws Exception {
 DateTimeFormatter formatter = new DefaultDateTimeFormatter();
 String datetimeString = "2016-11-14T13:30:00.000Z";
 DateTime datetime = formatter.parseDateTime(datetimeString);
 assertEquals(new DateTime(1473273600000L, DateTimeUtils.UTC), datetime);
 }

 @Test
 public void testParseMutableDateTime() throws Exception {
 DateTimeFormatter formatter = new DefaultDateTimeFormatter();
 String datetimeString = "2016-11-14T13:30:00.000Z";
 MutableDateTime mutableDateTime = new MutableDateTime();
 int result = formatter.parseInto(mutableDateTime, datetimeString, 0);
 assertEquals(datetimeString.length(), result);
 assertEquals(new DateTime(1473273600000L, DateTimeUtils.UTC), mutableDateTime.toDateTime());
 }

}