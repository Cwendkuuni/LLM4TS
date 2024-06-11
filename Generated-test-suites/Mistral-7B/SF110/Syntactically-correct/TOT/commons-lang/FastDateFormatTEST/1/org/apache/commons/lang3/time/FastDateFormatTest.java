import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat format = FastDateFormat.getInstance();
 assertNotNull(format);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
 assertNotNull(dateFormat);
 Calendar calendar = new GregorianCalendar(2022, 11, 25);
 Date date = dateFormat.parse(dateFormat.format(calendar));
 assertEquals(calendar.getTime(), date);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
 assertNotNull(timeFormat);
 Calendar calendar = new GregorianCalendar(2022, 11, 25, 15, 30, 0);
 Date date = timeFormat.parse(timeFormat.format(calendar));
 assertEquals(calendar.getTime(), date);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG);
 assertNotNull(dateTimeFormat);
 Calendar calendar = new GregorianCalendar(2022, 11, 25, 15, 30, 0);
 Date date = dateTimeFormat.parse(dateTimeFormat.format(calendar));
 assertEquals(calendar.getTime(), date);
 }

 @Test
 public void testParse() throws ParseException {
 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG);
 Calendar calendar = new GregorianCalendar(2022, 11, 25, 15, 30, 0);
 String dateString = simpleDateFormat.format(calendar.getTime());
 Date date = fastDateFormat.parse(dateString);
 assertEquals(calendar.getTime(), date);
 }

 @Test
 public void testFormat() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
 Calendar calendar = new GregorianCalendar(2022, 11, 25);
 String dateString = dateFormat.format(calendar);
 assertEquals("25-Dec-22", dateString);
 }

 @Test
 public void testFormatWithTimeZone() {
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.ENGLISH, timeZone);
 Calendar calendar = new GregorianCalendar(2022, 11, 25, 15, 30, 0);
 String dateString = dateFormat.format(calendar);
 assertEquals("Fri Dec 23 14:30:00 UTC 2022", dateString);
 }

 @Test
 public void testFormatWithLocale() {
 Locale locale = Locale.FRENCH;
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, locale);
 Calendar calendar = new GregorianCalendar(2022, 11, 25, 15, 30, 0);
 String dateString = dateFormat.format(calendar);
 assertEquals("vendredi 23 déc. 2022 14:30:00", dateString);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFormat() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
 String invalidDateString = "25-Dec-23";
 dateFormat.parse(invalidDateString);
 fail("Expected ParseException but no exception was thrown.");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFormatWithTimeZone() {
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.ENGLISH, timeZone);
 String invalidDateString = "25-Dec-23";
 dateFormat.parse(invalidDateString);
 fail("Expected ParseException but no exception was thrown.");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFormatWithLocale() {
 Locale locale = Locale.FRENCH;
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, locale);
 String invalidDateString = "25-Dec-23";
 dateFormat.parse(invalidDateString);
 fail("Expected ParseException but no exception was thrown.");
 }
}