import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() throws Exception {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testFormatDate() throws Exception {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
 Date date = new Date();
 String formattedDate = dateFormat.format(date);
 Assert.assertNotNull(formattedDate);
 Assert.assertEquals("12-12-2021 12:12 PM", formattedDate);
 }

 @Test
 public void testParseDate() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
 Date parsedDate = dateFormat.parse("12-12-2021 12:12 PM");
 Assert.assertNotNull(parsedDate);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDate() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
 dateFormat.parse("13-12-2021 32:60 PM");
 }

 @Test
 public void testFormatCalendar() throws Exception {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a z");
 Calendar calendar = new GregorianCalendar(2021, Calendar.DECEMBER, 12);
 String formattedDate = dateFormat.format(calendar.getTime());
 Assert.assertNotNull(formattedDate);
 Assert.assertEquals("12-12-2021 12:12:00 PM EST", formattedDate);
 }

 @Test
 public void testParseCalendar() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a z");
 Calendar calendar = new GregorianCalendar(2021, Calendar.DECEMBER, 12);
 calendar.setTime(dateFormat.parse("12-12-2021 12:12:00 PM EST"));
 Assert.assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH));
 Assert.assertEquals(Calendar.DECEMBER, calendar.get(Calendar.DAY_OF_MONTH));
 Assert.assertEquals(2021, calendar.get(Calendar.YEAR));
 Assert.assertEquals(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR));
 Assert.assertEquals(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidCalendar() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a z");
 Calendar calendar = new GregorianCalendar(2021, Calendar.DECEMBER, 12);
 calendar.setTime(dateFormat.parse("12-13-2021 12:12:00 PM EST"));
 }

 @Test
 public void testGetPattern() throws Exception {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
 String pattern = dateFormat.getPattern();
 Assert.assertEquals("dd-MM-yyyy hh:mm a", pattern);
 }

 @Test
 public void testGetTimeZone() throws Exception {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a z");
 TimeZone timeZone = dateFormat.getTimeZone();
 Assert.assertNotNull(timeZone);
 Assert.assertEquals("EST", timeZone.getID());
 }

 @Test
 public void testGetLocale() throws Exception {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MMM-yy hh:mm a zzzz");
 Locale locale = dateFormat.getLocale();
 Assert.assertNotNull(locale);
 Assert.assertEquals("en_US_EST_daylight", locale.toString());
 }

 @Test
 public void testGetMaxLengthEstimate() throws Exception {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
 int maxLength = dateFormat.getMaxLengthEstimate();
 Assert.assertEquals(19, maxLength);
 }

 @Test
 public void testEqualsObject() throws Exception {
 FastDateFormat dateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
 FastDateFormat dateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
 FastDateFormat dateFormat3 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a z");
 Assert.assertEquals(dateFormat1, dateFormat1);
 Assert.assertEquals(dateFormat1, dateFormat2);
 Assert.assertNotEquals(dateFormat1, dateFormat3);
 }

 @Test
 public void testHashCode() throws Exception {
 FastDateFormat dateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
 FastDateFormat dateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
 FastDateFormat dateFormat3 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a z");
 int hashCode1 = dateFormat1.hashCode();
 int hashCode2 = dateFormat2.hashCode();
 int hashCode3 = dateFormat3.hashCode();
 Assert.assertEquals(hashCode1, hashCode2);
 Assert.assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() throws Exception {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
 String toString = dateFormat.toString();
 Assert.assertNotNull(toString);
 Assert.assertTrue(toString.contains("FastDateFormat"));
 }

 @Test
 public void testApplyRules() throws Exception {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a zzzz");
 Calendar calendar = new GregorianCalendar(2021, Calendar.DECEMBER, 12);
 StringBuffer buf = new StringBuffer();
 dateFormat.applyRules(calendar, buf);
 String formattedDate = buf.toString();
 Assert.assertNotNull(formattedDate);
 Assert.assertEquals("12-12-2021 12:12:00 PM EST", formattedDate);
 }
}