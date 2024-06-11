import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertSame(dateFormat.getPattern(), FormatUtils.DEFAULT_DATE_FORMAT_PATTERN);
 assertSame(dateFormat.getLocale(), Locale.getDefault());
 assertSame(dateFormat.getTimeZone(), DateFormat.getDefaultTimeZone());
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a");
 assertSame(dateFormat.getPattern(), "dd/MM/yyyy hh:mm a");
 assertSame(dateFormat.getLocale(), Locale.getDefault());
 assertSame(dateFormat.getTimeZone(), DateFormat.getDefaultTimeZone());
 }

 @Test
 public void testParseDate() throws ParseException {
 String dateString = "21/12/2022 14:30 PM";
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a");
 Calendar calendar = new GregorianCalendar();
 dateFormat.parse(dateString, calendar);
 assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 21);
 assertEquals(calendar.get(Calendar.MONTH), Calendar.DECEMBER);
 assertEquals(calendar.get(Calendar.YEAR), 2022);
 assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 14);
 assertEquals(calendar.get(Calendar.MINUTE), 30);
 assertEquals(calendar.get(Calendar.AM_PM), Calendar.PM);
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithInvalidFormat() throws ParseException {
 String dateString = "21/12/2022 hh:mm a";
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a");
 dateFormat.parse(dateString); // expected ParseException
 }

 @Test
 public void testFormatDate() throws ParseException {
 Date date = new Date();
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a");
 String dateString = dateFormat.format(date);
 assertNotNull(dateString);
 }

 @Test
 public void testFormatDateWithNullDate() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a");
 String dateString = dateFormat.format((Date)null);
 assertNull(dateString);
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a");
 int maxLength = dateFormat.getMaxLengthEstimate();
 assertTrue(maxLength > 0);
 }

 @Test
 public void testSetTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 dateFormat.setTimeZone(timeZone);
 assertSame(dateFormat.getTimeZone(), timeZone);
 }

 @Test
 public void testSetLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 Locale locale = Locale.GERMAN;
 dateFormat.setLocale(locale);
 assertSame(dateFormat.getLocale(), locale);
 }

 @Test
 public void testEquals() {
 FastDateFormat dateFormat1 = FastDateFormat.getInstance();
 FastDateFormat dateFormat2 = FastDateFormat.getInstance();
 assertTrue(dateFormat1.equals(dateFormat2));
 FastDateFormat dateFormat3 = new FastDateFormat("dd/MM/yyyy hh:mm a", Locale.GERMAN, TimeZone.getTimeZone("UTC"));
 assertFalse(dateFormat1.equals(dateFormat3));
 }

 @Test
 public void testHashCode() {
 FastDateFormat dateFormat1 = FastDateFormat.getInstance();
 FastDateFormat dateFormat2 = FastDateFormat.getInstance();
 assertEquals(dateFormat1.hashCode(), dateFormat2.hashCode());
 FastDateFormat dateFormat3 = new FastDateFormat("dd/MM/yyyy hh:mm a", Locale.GERMAN, TimeZone.getTimeZone("UTC"));
 assertNotSame(dateFormat1.hashCode(), dateFormat3.hashCode());
 }

 @Test
 public void testToString() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a");
 String dateFormatString = dateFormat.toString();
 assertNotNull(dateFormatString);
 assertTrue(dateFormatString.contains("FastDateFormat["));
 assertTrue(dateFormatString.contains("[dd/MM/yyyy hh:mm a,"));
 assertTrue(dateFormatString.contains(","));
 assertTrue(dateFormatString.contains("]"));
 }
}