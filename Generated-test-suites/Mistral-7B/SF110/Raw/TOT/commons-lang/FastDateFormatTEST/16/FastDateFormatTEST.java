import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithPattern() {
 String pattern = "dd-MM-yyyy HH:mm:ss";
 FastDateFormat dateFormat = FastDateFormat.getInstance(pattern);
 Assert.assertEquals(pattern, dateFormat.getPattern());
 }

 @Test
 public void testGetInstanceWithPatternTimeZone() {
 String pattern = "dd-MM-yyyy HH:mm:ss";
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 FastDateFormat dateFormat = FastDateFormat.getInstance(pattern, timeZone);
 Assert.assertEquals(pattern, dateFormat.getPattern());
 Assert.assertEquals(timeZone, dateFormat.getTimeZone());
 }

 @Test
 public void testGetInstanceWithPatternLocale() {
 String pattern = "dd-MM-yyyy HH:mm:ss";
 Locale locale = Locale.US;
 FastDateFormat dateFormat = FastDateFormat.getInstance(pattern, locale);
 Assert.assertEquals(pattern, dateFormat.getPattern());
 Assert.assertEquals(locale, dateFormat.getLocale());
 }

 @Test
 public void testGetInstanceWithPatternTimeZoneLocale() {
 String pattern = "dd-MM-yyyy HH:mm:ss";
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 Locale locale = Locale.US;
 FastDateFormat dateFormat = FastDateFormat.getInstance(pattern, timeZone, locale);
 Assert.assertEquals(pattern, dateFormat.getPattern());
 Assert.assertEquals(timeZone, dateFormat.getTimeZone());
 Assert.assertEquals(locale, dateFormat.getLocale());
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 Assert.assertEquals("Full date", dateFormat.getDateInstance().getClass().getName());
 }

 @Test
 public void testGetDateInstanceWithStyleLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
 Assert.assertEquals("Medium date", dateFormat.getDateInstance().getClass().getName());
 }

 @Test
 public void testGetDateInstanceWithStyleTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"));
 Assert.assertEquals("Medium date", dateFormat.getDateInstance().getClass().getName());
 }

 @Test
 public void testGetDateInstanceWithStyleTimeZoneLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"), Locale.US);
 Assert.assertEquals("Medium date", dateFormat.getDateInstance().getClass().getName());
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 Assert.assertEquals("Short time", dateFormat.getTimeInstance().getClass().getName());
 }

 @Test
 public void testGetTimeInstanceWithStyleLocale() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.US);
 Assert.assertEquals("Medium time", dateFormat.getTimeInstance().getClass().getName());
 }

 @Test
 public void testGetTimeInstanceWithStyleTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"));
 Assert.assertEquals("Medium time", dateFormat.getTimeInstance().getClass().getName());
 }

 @Test
 public void testGetTimeInstanceWithStyleTimeZoneLocale() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"), Locale.US);
 Assert.assertEquals("Medium time", dateFormat.getTimeInstance().getClass().getName());
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.LONG);
 Assert.assertEquals("Full date and long time", dateFormat.getDateTimeInstance().getClass().getName());
 }

 @Test
 public void testGetDateTimeInstanceWithStyleLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.US);
 Assert.assertEquals("Medium date and long time", dateFormat.getDateTimeInstance().getClass().getName());
 }

 @Test
 public void testGetDateTimeInstanceWithStyleTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, TimeZone.getTimeZone("UTC"));
 Assert.assertEquals("Medium date and long time", dateFormat.getDateTimeInstance().getClass().getName());
 }

 @Test
 public void testGetDateTimeInstanceWithStyleTimeZoneLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, TimeZone.getTimeZone("UTC"), Locale.US);
 Assert.assertEquals("Medium date and long time", dateFormat.getDateTimeInstance().getClass().getName());
 }

 @Test
 public void testFormatDate() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 Date date = new Date();
 String dateString = dateFormat.format(date);
 Assert.assertNotNull(dateString);
 }

 @Test
 public void testFormatLong() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 long timeInMilliseconds = System.currentTimeMillis();
 String timeString = dateFormat.format(timeInMilliseconds);
 Assert.assertNotNull(timeString);
 }

 @Test
 public void testFormatCalendar() {
 Calendar calendar = Calendar.getInstance();
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 String dateString = dateFormat.format(calendar);
 Assert.assertNotNull(dateString);
 }

 @Test
 public void testFormatDateWithPattern() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance("dd/MM/yyyy");
 Date date = new Date();
 String dateString = dateFormat.format(date);
 Assert.assertEquals("Invalid date format", "dd/MM/yyyy", dateFormat.getPattern());
 Assert.assertNotNull(dateString);
 }

 @Test
 public void testFormatLongWithPattern() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance("HH:mm:ss z");
 long timeInMilliseconds = System.currentTimeMillis();
 String timeString = dateFormat.format(timeInMilliseconds);
 Assert.assertEquals("Invalid time format", "HH:mm:ss z", dateFormat.getPattern());
 Assert.assertNotNull(timeString);
 }

 @Test
 public void testFormatCalendarWithPattern() {
 Calendar calendar = Calendar.getInstance();
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance("dd/MM/yyyy HH:mm:ss z", Locale.US);
 String dateTimeString = dateFormat.format(calendar);
 Assert.assertEquals("Invalid date time format", "dd/MM/yyyy HH:mm:ss z", dateFormat.getPattern());
 Assert.assertNotNull(dateTimeString);
 }

 @Test
 public void testParseDate() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 String dateString = "01-12-2021";
 Date date = dateFormat.parse(dateString);
 Assert.assertNotNull(date);
 }

 @Test
 public void testParseLong() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
 String timeString = "12:12:12 AM";
 long timeInMilliseconds = dateFormat.parse(timeString).getTime();
 Assert.assertNotNull(timeInMilliseconds);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDate() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance("dd-MM-yyyy");
 String dateString = "32-12-2021";
 dateFormat.parse(dateString);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidTime() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance("HH:mm:ss");
 String timeString = "25:60:00";
 dateFormat.parse(timeString);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDateTime() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance("dd-MM-yyyy HH:mm:ss", Locale.US);
 String dateTimeString = "32-12-2021 25:60:00";
 dateFormat.parse(dateTimeString);
 }

 @Test
 public void testToString() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance("dd/MM/yyyy HH:mm:ss z", Locale.US);
 Assert.assertNotNull(dateFormat.toString());
 }
}