import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

 private FastDateFormat dateFormat;
 private FastDateFormat timeFormat;
 private FastDateFormat datetimeFormat;

 @Before
 public void setup() {
 dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, new Locale("en", "US"));
 timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, new Locale("en", "US"));
 datetimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new Locale("en", "US"));
 }

 @Test
 public void testDateFormatShort() {
 // Test valid date
 String dateString = "01/01/2022";
 Date date = dateFormat.parse(dateString);
 Calendar cal = Calendar.getInstance();
 cal.setTime(date);
 assert(cal.get(Calendar.DAY_OF_MONTH) == 1 && cal.get(Calendar.MONTH) == 0 && cal.get(Calendar.YEAR) == 2022);

 // Test invalid date
 String invalidDateString = "31/12/2022";
 try {
 dateFormat.parse(invalidDateString);
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testDateFormatLong() {
 // Test valid date
 String dateString = "January 1, 2022";
 Date date = dateFormat.parse(dateString);
 Calendar cal = Calendar.getInstance();
 cal.setTime(date);
 assert(cal.get(Calendar.DAY_OF_MONTH) == 1 && cal.get(Calendar.MONTH) == 0 && cal.get(Calendar.YEAR) == 2022 && cal.get(Calendar.HOUR_OF_DAY) == 0 && cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == 0);

 // Test invalid date
 String invalidDateString = "January 32, 2022";
 try {
 dateFormat.parse(invalidDateString);
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testTimeFormatLong() {
 // Test valid time
 String timeString = "12:30:45 PM";
 Date time = timeFormat.parse(timeString);
 Calendar cal = Calendar.getInstance();
 cal.setTime(time);
 assert(cal.get(Calendar.HOUR_OF_DAY) == 12 && cal.get(Calendar.MINUTE) == 30 && cal.get(Calendar.SECOND) == 45 && cal.get(Calendar.AM_PM) == 1);

 // Test invalid time
 String invalidTimeString = "24:60:59 PM";
 try {
 timeFormat.parse(invalidTimeString);
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testDateTimeFormatMedium() {
 // Test valid datetime
 String datetimeString = "January 1, 2022, 12:30:45 PM";
 Date datetime = datetimeFormat.parse(datetimeString);
 Calendar cal = Calendar.getInstance();
 cal.setTime(datetime);
 assert(cal.get(Calendar.DAY_OF_MONTH) == 1 && cal.get(Calendar.MONTH) == 0 && cal.get(Calendar.YEAR) == 2022 && cal.get(Calendar.HOUR_OF_DAY) == 12 && cal.get(Calendar.MINUTE) == 30 && cal.get(Calendar.SECOND) == 45 && cal.get(Calendar.AM_PM) == 1);

 // Test invalid datetime
 String invalidDatetimeString = "January 32, 2022, 24:60:59 PM";
 try {
 datetimeFormat.parse(invalidDatetimeString);
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testSetTimeZone() {
 // Test valid date with TimeZone
 TimeZone tz = TimeZone.getTimeZone("Asia/Kolkata");
 dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, new Locale("en", "US"), tz);
 String dateString = "01/01/2022";
 Date date = dateFormat.parse(dateString);
 Calendar cal = Calendar.getInstance(tz);
 cal.setTime(date);
 assert(cal.get(Calendar.DAY_OF_MONTH) == 1 && cal.get(Calendar.MONTH) == 0 && cal.get(Calendar.YEAR) == 2022 && cal.get(Calendar.HOUR_OF_DAY) == 11 && cal.get(Calendar.MINUTE) == 30 && cal.get(Calendar.SECOND) == 0);
 }

 @Test
 public void testSetLocale() {
 // Test valid date with Locale
 Locale locale = new Locale("fr", "FR");
 dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, locale);
 String dateString = "01/01/2022";
 Date date = dateFormat.parse(dateString);
 Calendar cal = Calendar.getInstance();
 cal.setTime(date);
 assert(cal.get(Calendar.DAY_OF_MONTH) == 1 && cal.get(Calendar.MONTH) == 1 && cal.get(Calendar.YEAR) == 2022 && cal.get(Calendar.HOUR_OF_DAY) == 0 && cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == 0);
 }
}