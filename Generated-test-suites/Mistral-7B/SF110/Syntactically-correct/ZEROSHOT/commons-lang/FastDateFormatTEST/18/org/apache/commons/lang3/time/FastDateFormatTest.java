import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FastDatePrinter;
import org.apache.commons.lang3.time.FastDateParser;
import org.apache.commons.lang3.time.FormatCache;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import static org.junit.Assert.*;

public class FastDateFormatTest {

 private FastDateFormat getFastDateFormat(String pattern, Locale locale, TimeZone timeZone) {
 return FastDateFormat.getInstance(pattern, locale, timeZone);
 }

 @Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a zzz");
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPatternLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a zzz", Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPatternLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US, TimeZone.getTimeZone("UTC"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US, TimeZone.getTimeZone("UTC"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.US, TimeZone.getTimeZone("UTC"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testFormat() throws ParseException {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 Date date = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 0, 0).getTime();
 String expected = "01-01-2022 12:00:00 PM UTC";
 String actual = fastDateFormat.format(date);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithCalendar() throws ParseException {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 Calendar calendar = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 0, 0);
 String expected = "01-01-2022 12:00:00 PM UTC";
 String actual = fastDateFormat.format(calendar);
 assertEquals(expected, actual);
 }

 @Test
 public void testParse() throws ParseException {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 String dateString = "01-01-2022 12:00:00 PM UTC";
 Date expected = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 0, 0).getTime();
 Date actual = fastDateFormat.parse(dateString);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithParsePosition() throws ParseException {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 String dateString = "01-01-2022 12:00:00 PM UTC";
 ParsePosition parsePosition = new ParsePosition(0);
 Date expected = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 0, 0).getTime();
 Date actual = fastDateFormat.parse(dateString, parsePosition);
 assertEquals(expected, actual);
 assertEquals(dateString.length(), parsePosition.getIndex());
 }

 @Test
 public void testParseObjectWithParsePosition() throws ParseException {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 String dateString = "01-01-2022 12:00:00 PM UTC";
 ParsePosition parsePosition = new ParsePosition(0);
 Date expected = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 0, 0).getTime();
 Object actual = fastDateFormat.parseObject(dateString, parsePosition);
 assertEquals(expected, actual);
 assertEquals(dateString.length(), parsePosition.getIndex());
 }

 @Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 String expected = "dd-MM-yyyy hh:mm:ss a zzz";
 String actual = fastDateFormat.getPattern();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 TimeZone expected = TimeZone.getTimeZone("UTC");
 TimeZone actual = fastDateFormat.getTimeZone();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 Locale expected = Locale.US;
 Locale actual = fastDateFormat.getLocale();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 int expected = 32;
 int actual = fastDateFormat.getMaxLengthEstimate();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 FastDateFormat fastDateFormat2 = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testNotEquals() {
 FastDateFormat fastDateFormat1 = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 FastDateFormat fastDateFormat2 = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("Etc/GMT+2"));
 assertFalse(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 int expected = fastDateFormat.hashCode();
 int actual = fastDateFormat.hashCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 String expected = "FastDateFormat[dd-MM-yyyy hh:mm:ss a zzz,US,UTC]";
 String actual = fastDateFormat.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testApplyRules() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a zzz", Locale.US, TimeZone.getTimeZone("UTC"));
 Calendar calendar = new GregorianCalendar(2022, Calendar.JANUARY, 1, 12, 0, 0);
 StringBuffer expected = new StringBuffer();
 StringBuffer actual = fastDateFormat.applyRules(calendar, expected);
 String expectedString = "January 01, 2022 12:00:00 PM UTC";
 String actualString = expected.toString();
 assertEquals(expectedString, actualString);
 }
}