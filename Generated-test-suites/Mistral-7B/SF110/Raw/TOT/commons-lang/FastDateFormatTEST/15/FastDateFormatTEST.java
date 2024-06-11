import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import static org.junit.Assert.*;

public class FastDateFormatTest {

 private FastDateFormat fullFormat;
 private FastDateFormat mediumFormat;
 private FastDateFormat shortFormat;

 @Before
 public void setUp() {
 fullFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 mediumFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
 shortFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT);
 }

 @Test
 public void testFormatFullDate() throws ParseException {
 // Test formatting a Date object with the full format
 Date date = FormatUtils.parseDate("2022-03-15 12:30:45.123", "yyyy-MM-dd HH:mm:ss.SSS");
 String expected = "Tue Mar 15 12:30:45 CET 2022";
 assertEquals(expected, fullFormat.format(date));
 }

 @Test
 public void testFormatMediumDate() throws ParseException {
 // Test formatting a Date object with the medium format
 Date date = FormatUtils.parseDate("2022-03-15 12:30:45.123", "yyyy-MM-dd HH:mm:ss.SSS");
 String expected = "Mar 15, 12:30:45 CET 2022";
 assertEquals(expected, mediumFormat.format(date));
 }

 @Test
 public void testFormatShortDate() throws ParseException {
 // Test formatting a Date object with the short format
 Date date = FormatUtils.parseDate("2022-03-15 12:30:45.123", "yyyy-MM-dd HH:mm:ss.SSS");
 String expected = "15.03.2022, 12:30";
 assertEquals(expected, shortFormat.format(date));
 }

 @Test
 public void testFormatCurrentDate() {
 // Test formatting the current date with each format
 String expectedFull = FormatUtils.formatShortDate(new Date(), "dd.MM.yyyy, HH:mm:ss zzz");
 String expectedMedium = FormatUtils.formatShortDate(new Date(), "MMM dd, hh:mm aaa");
 String expectedShort = FormatUtils.formatShortDate(new Date(), "dd.MM.yy, HH:mm");
 assertEquals(expectedFull, fullFormat.format(new Date()));
 assertEquals(expectedMedium, mediumFormat.format(new Date()));
 assertEquals(expectedShort, shortFormat.format(new Date()));
 }

 @Test
 public void testParseDateFullFormat() throws ParseException {
 // Test parsing a date string with the full format
 String dateString = "Tue Mar 15 12:30:45 CET 2022";
 Date expected = new Date(1647343045000L);
 Date actual = fullFormat.parse(dateString);
 assertEquals(expected.getTime(), actual.getTime());
 }

 @Test
 public void testParseDateMediumFormat() throws ParseException {
 // Test parsing a date string with the medium format
 String dateString = "Mar 15, 12:30:45 CET 2022";
 Date expected = new Date(1647343045000L);
 Date actual = mediumFormat.parse(dateString);
 assertEquals(expected.getTime(), actual.getTime());
 }

 @Test
 public void testParseDateShortFormat() throws ParseException {
 // Test parsing a date string with the short format
 String dateString = "15.03.2022, 12:30";
 Date expected = new Date(1647343045000L);
 Date actual = shortFormat.parse(dateString);
 assertEquals(expected.getTime(), actual.getTime());
 }

 @Test
 public void testParseDateInvalidFormat() throws ParseException {
 // Test parsing an invalid date string
 String dateString = "InvalidDateFormat";
 try {
 shortFormat.parse(dateString);
 fail("Expected ParseException not thrown");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testFormatWithTimezone() throws ParseException {
 // Test formatting a Date object with a specific timezone
 Date date = FormatUtils.parseDate("2022-03-15 12:30:45.123", "yyyy-MM-dd HH:mm:ss.SSS");
 GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), 0, 0);
 calendar.setTime(date);
 LocalDateTime localDateTime = calendar.toZonedDateTime();
 ZoneId zoneId = ZoneId.of("America/Los_Angeles");
 LocalDateTime expectedLocalDateTime = localDateTime.withZoneSameInstant(zoneId);
 String expected = expectedLocalDateTime.format(FormatUtils.DATE_TIME_FORMAT_WITH_TIMEZONE);
 String actual = shortFormat.format(date, null, new Locale("en", "US"), TimeZone.getTimeZone("America/Los_Angeles"));
 assertEquals(expected, actual);
 }
}