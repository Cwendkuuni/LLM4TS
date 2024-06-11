import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import static org.junit.Assert.*;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertEquals("Default format", dateFormat.getPattern());
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
 assertEquals("Custom format: yyyy-MM-dd HH:mm:ss", dateFormat.getPattern());
 }

 @Test
 public void testGetInstanceWithPatternLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.FRENCH);
 assertEquals("Custom format with French locale: yyyy-MM-dd HH:mm:ss", dateFormat.getPattern());
 }

 @Test
 public void testGetInstanceWithPatternTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH, new GregorianCalendar(2020, Calendar.AUGUST, 1, 2, 3, 4, 5, 5, 5, Calendar.GERMANY).getTimeZone());
 assertEquals("Custom format with English locale and German timezone: yyyy-MM-dd HH:mm:ss", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertEquals("Medium date format", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRENCH);
 assertEquals("Medium date format with French locale", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Calendar.getInstance(Locale.ENGLISH, new GregorianCalendar(2020, Calendar.AUGUST, 1, 2, 3, 4, 5, 5, 5, Calendar.GERMANY).getTimeZone()).getTimeZone());
 assertEquals("Medium date format with English locale and German timezone", dateFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertEquals("Medium date-time format with short time format", dateTimeFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRENCH);
 assertEquals("Medium date-time format with short time format and French locale", dateTimeFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Calendar.getInstance(Locale.ENGLISH, new GregorianCalendar(2020, Calendar.AUGUST, 1, 2, 3, 4, 5, 5, 5, Calendar.GERMANY).getTimeZone()).getTimeZone());
 assertEquals("Medium date-time format with short time format and English locale and German timezone", dateTimeFormat.getPattern());
 }

 @Test
 public void testFormatDate() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
 Date date = FormatUtils.parseDate("2022-03-15", dateFormat);
 String formattedDate = dateFormat.format(date);
 assertEquals("Formatted date: 2022-03-15", formattedDate);
 }

 @Test
 public void testFormatLong() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 long millis = System.currentTimeMillis();
 String formattedDate = dateFormat.format(millis);
 assertEquals("Formatted long: " + Formats.formatDateTime(millis, null, "yyyy-MM-dd HH:mm:ss.SSS zzz", Locale.ENGLISH), formattedDate);
 }

 @Test
 public void testFormatDateWithBuffer() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
 Date date = FormatUtils.parseDate("2022-03-15 14:30:45", dateFormat);
 StringBuffer buffer = new StringBuffer();
 dateFormat.format(date, buffer);
 assertEquals("Formatted date with buffer: 2022-03-15 14:30:45", buffer.toString());
 }

 @Test
 public void testFormatLongWithBuffer() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 long millis = System.currentTimeMillis();
 StringBuffer buffer = new StringBuffer();
 dateFormat.format(millis, buffer);
 assertEquals("Formatted long with buffer: " + Formats.formatDateTime(millis, null, "yyyy-MM-dd HH:mm:ss.SSS zzz", Locale.ENGLISH), buffer.toString());
 }

 @Test
 public void testFormatCalendar() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
 Calendar calendar = new GregorianCalendar(2022, Calendar.MARCH, 15, 14, 30, 45, 5, 5, 5, Calendar.GERMANY);
 String formattedDate = dateFormat.format(calendar.getTime());
 assertEquals("Formatted calendar: 2022-03-15 14:30:45", formattedDate);
 }

 @Test
 public void testFormatCalendarWithBuffer() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
 Calendar calendar = new GregorianCalendar(2022, Calendar.MARCH, 15, 14, 30, 45, 5, 5, 5, Calendar.GERMANY);
 StringBuffer buffer = new StringBuffer();
 dateFormat.format(calendar, buffer);
 assertEquals("Formatted calendar with buffer: 2022-03-15 14:30:45", buffer.toString());
 }

 @Test
 public void testParseDate() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
 Date date = dateFormat.parse("2022-03-15");
 assertEquals("Parsed date: 2022-03-15", date.toString());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDate() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
 dateFormat.parse("2022-13-15");
 fail("Expected ParseException");
 }

 @Test
 public void testParseLong() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 long millis = System.currentTimeMillis();
 Date date = dateFormat.parse(dateFormat.format(millis));
 assertEquals("Parsed long: " + millis, date.getTime());
 }

 @Test
 public void testParseObject() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
 String stringDate = "2022-03-15 14:30:45";
 Date date = (Date)dateFormat.parseObject(stringDate);
 assertEquals("Parsed object: 2022-03-15 14:30:45", date.toString());
 }
}