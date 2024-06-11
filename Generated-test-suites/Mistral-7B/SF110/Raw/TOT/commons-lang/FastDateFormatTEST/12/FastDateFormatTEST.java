import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Test;
import org.junit.Assert.*;
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
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testFormatDate() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
 Date date = new Date();
 String formattedDate = dateFormat.format(date);
 String expectedFormattedDate = FormatUtils.format(date, "dd-MM-yyyy");
 Assert.assertEquals(expectedFormattedDate, formattedDate);
 }

 @Test
 public void testParseDate() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
 Date date = dateFormat.parse("25-12-2021");
 Calendar calendar = new GregorianCalendar();
 calendar.setTime(date);
 int expectedYear = calendar.get(Calendar.YEAR);
 int expectedMonth = calendar.get(Calendar.MONTH);
 int expectedDay = calendar.get(Calendar.DAY_OF_MONTH);
 int year = calendar.get(Calendar.YEAR);
 int month = calendar.get(Calendar.MONTH);
 int day = calendar.get(Calendar.DAY_OF_MONTH);
 Assert.assertEquals(expectedYear, year);
 Assert.assertEquals(expectedMonth, month);
 Assert.assertEquals(expectedDay, day);
 }

 @Test(expected = ParseException.class)
 public void testParseDateInvalidFormat() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yy-yyyy");
 String dateToParse = "25/12/2021-2022";
 dateFormat.parse(dateToParse);
 }

 @Test
 public void testFormatLongMilliseconds() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("HH:mm:ss z");
 long milliseconds = System.currentTimeMillis();
 String formattedDate = dateFormat.format(milliseconds);
 String expectedFormattedDate = FormatUtils.format(System.currentTimeMillis(), "HH:mm:ss z");
 Assert.assertEquals(expectedFormattedDate, formattedDate);
 }

 @Test
 public void testFormatCalendar() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("EEEE, dd MMMM yyyy h:mm a z");
 Calendar calendar = new GregorianCalendar(2022, 11, 25);
 String formattedDate = dateFormat.format(calendar);
 String expectedFormattedDate = FormatUtils.format(calendar, "EEEE, dd MMMM yyyy h:mm a z");
 Assert.assertEquals(expectedFormattedDate, formattedDate);
 }

 @Test
 public void testGetPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
 String pattern = dateFormat.getPattern();
 Assert.assertEquals("dd-MM-yyyy", pattern);
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
 TimeZone timeZone = dateFormat.getTimeZone();
 Assert.assertEquals("UTC", timeZone.getID());
 }

 @Test
 public void testGetLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
 Locale locale = dateFormat.getLocale();
 Assert.assertEquals(Locale.getDefault(), locale);
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("EEE, dd MMMM yyyy HH:mm:ss zzzz");
 int maxLength = dateFormat.getMaxLengthEstimate();
 int expectedMaxLength = 74;
 Assert.assertEquals(expectedMaxLength, maxLength);
 }

 @Test
 public void testEquals() {
 FastDateFormat dateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy");
 FastDateFormat dateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy");
 FastDateFormat dateFormat3 = FastDateFormat.getInstance("dd/MM/yyyy");
 Assert.assertTrue(dateFormat1.equals(dateFormat2));
 Assert.assertFalse(dateFormat1.equals(dateFormat3));
 }

 @Test
 public void testHashCode() {
 FastDateFormat dateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy");
 FastDateFormat dateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy");
 FastDateFormat dateFormat3 = FastDateFormat.getInstance("dd/MM/yyyy");
 int hashCode1 = dateFormat1.hashCode();
 int hashCode2 = dateFormat2.hashCode();
 int hashCode3 = dateFormat3.hashCode();
 Assert.assertEquals(hashCode1, hashCode2);
 Assert.assertNotEquals(hashCode1, hashCode3);
 }
}