import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertNotNull(dateFormat);
 }

 @Test
 public void testFormatDate() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a z");
 Date date = new Date();
 String formattedDate = dateFormat.format(date);
 System.out.println("Formatted date: " + formattedDate);
 assertNotNull(formattedDate);
 }

 @Test
 public void testParseDate() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a z");
 Date date;
 try {
 date = dateFormat.parse("01-12-2022 12:00 PM EST");
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Date parsing failed");
 }
 assertNotNull(date);
 }

 @Test
 public void testFormatInvalidDate() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a z");
 Date invalidDate = new Date(-1L);
 String formattedDate = dateFormat.format(invalidDate);
 assertEquals("", formattedDate);
 }

 @Test
 public void testParseInvalidString() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a z");
 String invalidString = "abcdefg";
 try {
 dateFormat.parse(invalidString);
 fail("Invalid string parsing succeeded");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testFormatNullObject() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a z");
 String formattedDate = dateFormat.format(null);
 assertEquals("", formattedDate);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertNotNull(dateTimeFormat);
 }

 @Test
 public void testFormatDateTime() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 Calendar calendar = new GregorianCalendar(2022, 11, 3, 13, 45, 0);
 String formattedDateTime = dateTimeFormat.format(calendar.getTime());
 System.out.println("Formatted date time: " + formattedDateTime);
 assertNotNull(formattedDateTime);
 }

 @Test
 public void testParseDateTime() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 String dateTimeString = "12-Nov-2022 1:30 PM";
 Calendar calendar;
 try {
 calendar = Calendar.getInstance();
 dateTimeFormat.parse(dateTimeString, calendar);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Date time parsing failed");
 }
 assertNotNull(calendar);
 }

 @Test
 public void testApplyRules() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 Calendar calendar = new GregorianCalendar(2022, 11, 3, 13, 45, 0);
 StringBuffer buf = new StringBuffer();
 dateFormat.applyRules(calendar, buf);
 System.out.println("Applied rules: " + buf.toString());
 assertNotNull(buf.toString());
 }

 @Test
 public void testFormatDateWithLocaleAndTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.FRENCH, TimeZone.getTimeZone("Europe/Paris"));
 Calendar calendar = new GregorianCalendar(2022, 11, 3, 13, 45, 0);
 String formattedDateTime = dateFormat.format(calendar.getTime());
 System.out.println("Formatted date time with locale and time zone: " + formattedDateTime);
 assertNotNull(formattedDateTime);
 }

 @Test
 public void testParseDateWithLocaleAndTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.FRENCH, TimeZone.getTimeZone("Europe/Paris"));
 String dateTimeString = "mardi 03 novembre 2022 13:45:00 CET";
 Date date;
 try {
 date = dateFormat.parse(dateTimeString);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Date parsing failed");
 }
 assertNotNull(date);
 }
}