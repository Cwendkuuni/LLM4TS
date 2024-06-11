import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Test;
import java.text.ParseException;
import java.util.*;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss.SSS zzz");
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithPatternTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss.SSS zzz", new TimeZone("UTC+2"));
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithPatternLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss.SSS zzz", Locale.US);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithPatternTimeZoneLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss.SSS zzz", new TimeZone("UTC+2"), Locale.US);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new TimeZone("UTC+2"));
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocaleTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US, new TimeZone("UTC+2"));
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
 assertNotNull(timeFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.US);
 assertNotNull(timeFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, new TimeZone("UTC+2"));
 assertNotNull(timeFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocaleTimeZone() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.US, new TimeZone("UTC+2"));
 assertNotNull(timeFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
 assertNotNull(dateTimeFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, Locale.US);
 assertNotNull(dateTimeFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, new TimeZone("UTC+2"));
 assertNotNull(dateTimeFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocaleTimeZone() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, Locale.US, new TimeZone("UTC+2"));
 assertNotNull(dateTimeFormat);
 }

 @Test
 public void testFormatDate() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 String dateString = dateFormat.format(calendar);
 assertNotNull(dateString);
 assertEquals(FormatUtils.DEFAULT_DATETIME_FORMAT_LONG, dateString);
 }

 @Test
 public void testFormatDateWithLocale() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US);
 String dateString = dateFormat.format(calendar);
 assertNotNull(dateString);
 assertEquals(FormatUtils.ISO_DATE_FORMAT_LONG, dateString);
 }

 @Test
 public void testFormatDateWithTimeZone() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, new TimeZone("UTC+2"));
 String dateString = dateFormat.format(calendar);
 assertNotNull(dateString);
 assertEquals(FormatUtils.DEFAULT_DATETIME_FORMAT_LONG + " UTC+02:00", dateString);
 }

 @Test
 public void testFormatDateWithLocaleTimeZone() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US, new TimeZone("UTC+2"));
 String dateString = dateFormat.format(calendar);
 assertNotNull(dateString);
 assertEquals(FormatUtils.ISO_DATE_FORMAT_LONG + " UTC+02:00", dateString);
 }

 @Test
 public void testParseDate() {
 String dateString = "25/12/2021";
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 Date date = dateFormat.parse(dateString);
 assertNotNull(date);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDate() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 dateFormat.parse("29/02/2021");
 }

 @Test
 public void testFormatTime() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
 String timeString = timeFormat.format(calendar);
 assertNotNull(timeString);
 assertEquals(FormatUtils.DEFAULT_DATETIME_FORMAT_LONG, timeString.substring(11));
 }

 @Test
 public void testFormatTimeWithLocale() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.US);
 String timeString = timeFormat.format(calendar);
 assertNotNull(timeString);
 assertEquals(FormatUtils.ISO_DATETIME_FORMAT_LONG, timeString);
 }

 @Test
 public void testFormatTimeWithTimeZone() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, new TimeZone("UTC+2"));
 String timeString = timeFormat.format(calendar);
 assertNotNull(timeString);
 assertEquals(FormatUtils.DEFAULT_DATETIME_FORMAT_LONG + " UTC+02:00", timeString);
 }

 @Test
 public void testFormatTimeWithLocaleTimeZone() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.US, new TimeZone("UTC+2"));
 String timeString = timeFormat.format(calendar);
 assertNotNull(timeString);
 assertEquals(FormatUtils.ISO_DATETIME_FORMAT_LONG + " UTC+02:00", timeString);
 }

 @Test
 public void testParseTime() {
 String timeString = "15:32:43";
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
 Date date = timeFormat.parse(timeString);
 assertNotNull(date);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidTime() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
 timeFormat.parse("24:60:00");
 }

 @Test
 public void testFormatDateTime() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 String dateTimeString = dateTimeFormat.format(calendar);
 assertNotNull(dateTimeString);
 assertEquals(FormatUtils.DEFAULT_DATETIME_FORMAT_LONG, dateTimeString);
 }

 @Test
 public void testFormatDateTimeWithLocale() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.US);
 String dateTimeString = dateTimeFormat.format(calendar);
 assertNotNull(dateTimeString);
 assertEquals(FormatUtils.ISO_DATETIME_FORMAT_LONG, dateTimeString);
 }

 @Test
 public void testFormatDateTimeWithTimeZone() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, new TimeZone("UTC+2"));
 String dateTimeString = dateTimeFormat.format(calendar);
 assertNotNull(dateTimeString);
 assertEquals(FormatUtils.DEFAULT_DATETIME_FORMAT_LONG + " UTC+02:00", dateTimeString);
 }

 @Test
 public void testFormatDateTimeWithLocaleTimeZone() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.US, new TimeZone("UTC+2"));
 String dateTimeString = dateTimeFormat.format(calendar);
 assertNotNull(dateTimeString);
 assertEquals(FormatUtils.ISO_DATETIME_FORMAT_LONG + " UTC+02:00", dateTimeString);
 }

 @Test
 public void testParseDateTime() {
 String dateTimeString = "25/12/2021 15:32:43";
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
 Date date = dateTimeFormat.parse(dateTimeString);
 assertNotNull(date);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDateTime() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
 dateTimeFormat.parse("29/02/2021 24:60:00");
 }
}