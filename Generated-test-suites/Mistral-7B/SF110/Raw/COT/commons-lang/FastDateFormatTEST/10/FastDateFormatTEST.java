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

 private static final String DEFAULT_PATTERN = "dd/MM/yyyy HH:mm:ss z";
 private static final String DATE_PATTERN = "dd-MM-yyyy";
 private static final String TIME_PATTERN = "HH:mm:ss";
 private static final String DATETIME_PATTERN = "dd-MM-yyyy HH:mm:ss";
 private static final String INVALID_PATTERN = "XX-XX-XXXX X:XX:XX";

 @Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN);
 assertNotNull(fastDateFormat);
 assertEquals(DEFAULT_PATTERN, fastDateFormat.getPattern());
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN, Locale.US, TimeZone.getTimeZone("America/New_York"));
 assertNotNull(fastDateFormat);
 assertEquals(DEFAULT_PATTERN, fastDateFormat.getPattern());
 assertEquals(TimeZone.getTimeZone("America/New_York"), fastDateFormat.getTimeZone());
 }

 @Test
 public void testGetInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN, Locale.FRENCH);
 assertNotNull(fastDateFormat);
 assertEquals(DEFAULT_PATTERN, fastDateFormat.getPattern());
 assertEquals(Locale.FRENCH, fastDateFormat.getLocale());
 }

 @Test
 public void testGetInstanceWithAllParams() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DATETIME_PATTERN, Locale.US, TimeZone.getTimeZone("Europe/Paris"));
 assertNotNull(fastDateFormat);
 assertEquals(DATETIME_PATTERN, fastDateFormat.getPattern());
 assertEquals(Locale.US, fastDateFormat.getLocale());
 assertEquals(TimeZone.getTimeZone("Europe/Paris"), fastDateFormat.getTimeZone());
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRENCH);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("America/New_York"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithAllParams() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US, TimeZone.getTimeZone("Europe/Paris"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRENCH);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("America/New_York"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithAllParams() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US, TimeZone.getTimeZone("Europe/Paris"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRENCH);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("America/New_York"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithAllParams() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.US, TimeZone.getTimeZone("Europe/Paris"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testFormat() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DATETIME_PATTERN);
 Calendar calendar = new GregorianCalendar(2022, 11, 24, 15, 30, 45);
 String dateString = fastDateFormat.format(calendar);
 assertEquals("24-12-2022 15:30:45", dateString);
 }

 @Test
 public void testFormatWithBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DATETIME_PATTERN);
 Calendar calendar = new GregorianCalendar(2022, 11, 24, 15, 30, 45);
 StringBuffer stringBuffer = new StringBuffer();
 fastDateFormat.format(calendar, stringBuffer);
 assertEquals("24-12-2022 15:30:45", stringBuffer.toString());
 }

 @Test
 public void testFormatMilliseconds() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DATETIME_PATTERN);
 long milliseconds = 1668737600000L; // Thu Jan 01 00:00:00 EST 2023
 String dateString = fastDateFormat.format(milliseconds);
 assertEquals("01-01-2023 00:00:00", dateString);
 }

 @Test
 public void testFormatMillisecondsWithBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DATETIME_PATTERN);
 long milliseconds = 1668737600000L; // Thu Jan 01 00:00:00 EST 2023
 StringBuffer stringBuffer = new StringBuffer();
 fastDateFormat.format(milliseconds, stringBuffer);
 assertEquals("01-01-2023 00:00:00", stringBuffer.toString());
 }

 @Test
 public void testFormatDate() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN);
 Date date = new Date();
 String dateString = fastDateFormat.format(date);
 assertNotNull(dateString);
 }

 @Test
 public void testParse() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DATETIME_PATTERN);
 String dateString = "24-12-2022 15:30:45";
 Date parsedDate = fastDateFormat.parse(dateString);
 Calendar calendar = new GregorianCalendar();
 calendar.setTime(parsedDate);
 assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
 assertEquals(11, calendar.get(Calendar.MONTH));
 assertEquals(2022, calendar.get(Calendar.YEAR));
 assertEquals(15, calendar.get(Calendar.HOUR_OF_DAY));
 assertEquals(30, calendar.get(Calendar.MINUTE));
 assertEquals(45, calendar.get(Calendar.SECOND));
 }

 @Test
 public void testParseWithParsePosition() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DATETIME_PATTERN);
 String dateString = "24-12-2022 15:30:45";
 ParsePosition parsePosition = new ParsePosition(0);
 Date parsedDate = fastDateFormat.parse(dateString, parsePosition);
 Calendar calendar = new GregorianCalendar();
 calendar.setTime(parsedDate);
 assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
 assertEquals(11, calendar.get(Calendar.MONTH));
 assertEquals(2022, calendar.get(Calendar.YEAR));
 assertEquals(15, calendar.get(Calendar.HOUR_OF_DAY));
 assertEquals(30, calendar.get(Calendar.MINUTE));
 assertEquals(45, calendar.get(Calendar.SECOND));
 assertEquals(dateString.length(), parsePosition.getIndex());
 }

 @Test
 public void testParseObject() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DATETIME_PATTERN);
 String dateString = "24-12-2022 15:30:45";
 Object parsedObject = fastDateFormat.parseObject(dateString);
 assertEquals(parsedDate, parsedObject);
 }

 @Test(expected = ParseException.class)
 public void testInvalidPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(INVALID_PATTERN);
 String dateString = "XX-XX-XXXX X:XX:XX";
 try {
 fastDateFormat.parse(dateString);
 fail("ParseException expected");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(null);
 assertNull(fastDateFormat);
 }

 @Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance(DATETIME_PATTERN);
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance(DATETIME_PATTERN);
 FastDateFormat fastDateFormat3 = FastDateFormat.getInstance(DATE_PATTERN);
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 assertFalse(fastDateFormat1.equals(fastDateFormat3));
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance(DATETIME_PATTERN);
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance(DATETIME_PATTERN);
 FastDateFormat fastDateFormat3 = FastDateFormat.getInstance(DATE_PATTERN);
 assertEquals(fastDateFormat1.hashCode(), fastDateFormat1.hashCode());
 assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
 assertNotEquals(fastDateFormat1.hashCode(), fastDateFormat3.hashCode());
 }

 @Test
 public void testToString() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DATETIME_PATTERN);
 String toString = fastDateFormat.toString();
 assertTrue(toString.startsWith("FastDateFormat["));
 }

 @Test
 public void testApplyRules() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DATETIME_PATTERN);
 Calendar calendar = new GregorianCalendar(2022, 11, 24, 15, 30, 45, Calendar.MILLIS_IN_DAY);
 String dateString = fastDateFormat.applyRules(calendar, new StringBuffer()).toString();
 assertEquals("24-12-2022 15:30:45", dateString);
 }
}