import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 @Test
 public void testNewInstance() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"));
 assertTrue(formatter.isPrinter());
 assertFalse(formatter.isParser());
 assertNull(formatter.getLocale());
 assertFalse(formatter.isOffsetParsed());
 assertNull(formatter.getChronology());
 assertNull(formatter.getZone());
 assertNull(formatter.getPivotYear());
 assertDefaultYear(formatter, 2000);
 }

 @Test
 public void testNewInstanceWithLocale() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"), Locale.US);
 assertTrue(formatter.isPrinter());
 assertFalse(formatter.isParser());
 assertEquals(Locale.US, formatter.getLocale());
 assertFalse(formatter.isOffsetParsed());
 assertNull(formatter.getChronology());
 assertNull(formatter.getZone());
 assertNull(formatter.getPivotYear());
 assertDefaultYear(formatter, 2000);
 }

 @Test
 public void testNewInstanceWithOffsetParsed() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"), null, true, null, null, null, 2000);
 assertTrue(formatter.isPrinter());
 assertFalse(formatter.isParser());
 assertNull(formatter.getLocale());
 assertTrue(formatter.isOffsetParsed());
 assertNull(formatter.getChronology());
 assertNull(formatter.getZone());
 assertNull(formatter.getPivotYear());
 assertDefaultYear(formatter, 2000);
 }

 @Test
 public void testNewInstanceWithChronology() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"), null, false, GregorianChronology.getInstance(), null, null, 2000);
 assertTrue(formatter.isPrinter());
 assertFalse(formatter.isParser());
 assertNull(formatter.getLocale());
 assertFalse(formatter.isOffsetParsed());
 assertEquals(GregorianChronology.getInstance(), formatter.getChronology());
 assertNull(formatter.getZone());
 assertNull(formatter.getPivotYear());
 assertDefaultYear(formatter, 2000);
 }

 @Test
 public void testNewInstanceWithZone() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"), null, false, null, DateTimeZone.forOffsetHours(-5), null, 2000);
 assertTrue(formatter.isPrinter());
 assertFalse(formatter.isParser());
 assertNull(formatter.getLocale());
 assertFalse(formatter.isOffsetParsed());
 assertNull(formatter.getChronology());
 assertEquals(DateTimeZone.forOffsetHours(-5), formatter.getZone());
 assertNull(formatter.getPivotYear());
 assertDefaultYear(formatter, 2000);
 }

 @Test
 public void testNewInstanceWithPivotYear() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"), null, false, null, null, new Integer(2020), 2000);
 assertTrue(formatter.isPrinter());
 assertFalse(formatter.isParser());
 assertNull(formatter.getLocale());
 assertFalse(formatter.isOffsetParsed());
 assertNull(formatter.getChronology());
 assertNull(formatter.getZone());
 assertEquals(new Integer(2020), formatter.getPivotYear());
 assertDefaultYear(formatter, 2000);
 }

 @Test
 public void testNewInstanceWithDefaultYear() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"), null, false, null, null, null, 2020);
 assertTrue(formatter.isPrinter());
 assertFalse(formatter.isParser());
 assertNull(formatter.getLocale());
 assertFalse(formatter.isOffsetParsed());
 assertNull(formatter.getChronology());
 assertNull(formatter.getZone());
 assertNull(formatter.getPivotYear());
 assertEquals(new Integer(2020), formatter.getDefaultYear());
 }

 @Test
 public void testPrintTo() {
 final DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"));
 final Instant instant = new Instant(System.currentTimeMillis());
 StringBuffer sb = new StringBuffer(100);
 formatter.printTo(sb, instant);
 String expected = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").format(instant.toDate());
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testPrintToWithLocale() {
 final DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"), Locale.US);
 final Instant instant = new Instant(System.currentTimeMillis());
 StringBuffer sb = new StringBuffer(100);
 formatter.printTo(sb, instant);
 String expected = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a z").format(instant.toDate());
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testPrintToWithOffsetParsed() {
 final DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"), null, true);
 final Instant instant = new Instant(System.currentTimeMillis() + (60 * 60 * 1000 * 5));
 StringBuffer sb = new StringBuffer(100);
 formatter.printTo(sb, instant);
 String expected = "2023-03-12 11:00:00-05:00";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testPrintToWithChronology() {
 final DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"), null, false, GregorianChronology.getInstance());
 final Instant instant = new Instant(System.currentTimeMillis());
 StringBuffer sb = new StringBuffer(100);
 formatter.printTo(sb, instant);
 String expected = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(instant.toDate());
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testPrintToWithZone() {
 final DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"), null, false, null, DateTimeZone.forOffsetHours(5));
 final Instant instant = new Instant(System.currentTimeMillis());
 StringBuffer sb = new StringBuffer(100);
 formatter.printTo(sb, instant);
 String expected = "2023-03-12 16:00:00+05:00";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testPrintToWithPivotYear() {
 final DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"), null, false, null, null, new Integer(2020), 2000);
 final Instant instant = new Instant(System.currentTimeMillis());
 StringBuffer sb = new StringBuffer(100);
 formatter.printTo(sb, instant);
 String expected = "2020-03-12 11:00:00";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testParseInto() {
 final DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"));
 final MutableDateTime mutableDateTime = new MutableDateTime();
 final int result = formatter.parseInto(mutableDateTime, "2023-03-12 11:00:00");
 assertEquals(13, result);
 assertEquals(new DateTime("2023-03-12T11:00:00.000Z"), mutableDateTime.toDateTime());
 }

 @Test
 public void testParseMillis() {
 final DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"));
 long millis = formatter.parseMillis("2023-03-12 11:00:00");
 assertEquals(1678992200000L, millis);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMillisInvalidFormat() {
 final DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"));
 formatter.parseMillis("invalidFormat");
 }

 @Test
 public void testParseLocalDate() {
 final DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), new SimpleDateTimeParser("yyyy-MM-dd"));
 LocalDate expected = new LocalDate(2023, 3, 12);
 LocalDate result = formatter.parseLocalDate("2023-03-12");
 assertEquals(expected, result);
 }

 @Test
 public void testParseLocalTime() {
 final DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss"), new SimpleDateTimeParser("HH:mm:ss"));
 LocalTime expected = new LocalTime(11, 0, 0);
 LocalTime result = formatter.parseLocalTime("11:00:00");
 assertEquals(expected, result);
 }

 @Test
 public void testParseLocalDateTime() {
 final DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"));
 LocalDateTime expected = new LocalDateTime(2023, 3, 12, 11, 0, 0);
 LocalDateTime result = formatter.parseLocalDateTime("2023-03-12 11:00:00");
 assertEquals(expected, result);
 }

 @Test
 public void testParseDateTime() {
 final DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"));
 DateTime expected = new DateTime(2023, 3, 12, 11, 0, 0, 0, GregorianChronology.getInstance());
 DateTime result = formatter.parseDateTime("2023-03-12 11:00:00");
 assertEquals(expected, result);
 }

 }