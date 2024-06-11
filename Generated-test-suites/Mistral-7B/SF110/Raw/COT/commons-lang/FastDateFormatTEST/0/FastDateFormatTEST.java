import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.apache.commons.lang3.time.ParsePosition;
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
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a zzz");
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a zzz", TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a zzz", Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithTimeZoneAndLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a zzz", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZoneAndLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZoneAndLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testParseDateWithValidPattern() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 String dateString = "25 December, Anno Domini 2001 12:08:56";
 Date parsedDate = fastDateFormat.parse(dateString);
 Assert.assertNotNull(parsedDate);
 }

 @Test
 public void testParseDateWithInvalidPattern() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 String invalidDateString = "25 December, Anno Domini 2001 12:08:56 X";
 ParsePosition parsePosition = new ParsePosition(0);
 Date parsedDate = fastDateFormat.parse(invalidDateString, parsePosition);
 Assert.assertNull(parsedDate);
 Assert.assertEquals(invalidDateString.length(), parsePosition.getIndex());
 }

 @Test
 public void testFormatDateWithValidDate() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 Date date = new Date();
 String formattedDate = fastDateFormat.format(date);
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testFormatDateWithNullDate() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 String formattedDate = fastDateFormat.format(null);
 Assert.assertEquals("", formattedDate);
 }

 @Test
 public void testFormatDateWithDateInFuture() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 Date futureDate = FormatUtils.parseDate("31 December, Anno Domini 2030", fastDateFormat);
 String formattedDate = fastDateFormat.format(futureDate);
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testFormatDateWithDateInPast() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 Date pastDate = FormatUtils.parseDate("24 December, Anno Domini 1995", fastDateFormat);
 String formattedDate = fastDateFormat.format(pastDate);
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testFormatDateWithInvalidDate() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 String invalidDateString = "29 February, Anno Domini 2022";
 ParsePosition parsePosition = new ParsePosition(0);
 Date parsedDate = fastDateFormat.parse(invalidDateString, parsePosition);
 Assert.assertNull(parsedDate);
 Assert.assertEquals(invalidDateString.length(), parsePosition.getIndex());
 String formattedDate = fastDateFormat.format(parsedDate);
 Assert.assertEquals("", formattedDate);
 }

 @Test
 public void testFormatDateWithDateAndCalendar() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 Calendar calendar = new GregorianCalendar(2022, 11, 28);
 String formattedDate = fastDateFormat.format(calendar.getTime());
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testFormatDateWithNullCalendar() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 String formattedDate = fastDateFormat.format(null);
 Assert.assertEquals("", formattedDate);
 }

 @Test
 public void testFormatDateWithCalendarInFuture() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 Calendar calendar = new GregorianCalendar(2022, 12, 31);
 String formattedDate = fastDateFormat.format(calendar.getTime());
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testFormatDateWithCalendarInPast() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 Calendar calendar = new GregorianCalendar(2022, 11, 28);
 String formattedDate = fastDateFormat.format(calendar.getTime());
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testFormatDateWithInvalidCalendar() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 Calendar calendar = new GregorianCalendar(1900, 12, 32);
 String formattedDate = fastDateFormat.format(calendar.getTime());
 Assert.assertEquals("", formattedDate);
 }

 @Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
 String pattern = fastDateFormat.getPattern();
 Assert.assertEquals("dd-MM-yy hh:mm a z", pattern);
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"));
 TimeZone timeZone = fastDateFormat.getTimeZone();
 Assert.assertEquals("UTC", timeZone.getID());
 }

 @Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, Locale.FRENCH);
 Locale locale = fastDateFormat.getLocale();
 Assert.assertEquals("fr_FR", locale.getLanguage());
 Assert.assertEquals("France", locale.getCountry());
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
 Assert.assertEquals(42, maxLengthEstimate);
 }

 @Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 FastDateFormat fastDateFormat3 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
 Assert.assertTrue(fastDateFormat1.equals(fastDateFormat2));
 Assert.assertFalse(fastDateFormat1.equals(fastDateFormat3));
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 FastDateFormat fastDateFormat3 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
 int hashCode1 = fastDateFormat1.hashCode();
 int hashCode2 = fastDateFormat2.hashCode();
 int hashCode3 = fastDateFormat3.hashCode();
 Assert.assertEquals(hashCode1, hashCode2);
 Assert.assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 String toString = fastDateFormat.toString();
 Assert.assertTrue(toString.startsWith("FastDateFormat["));
 }

 @Test
 public void testApplyRulesWithValidCalendar() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 Calendar calendar = new GregorianCalendar(2022, 11, 28);
 StringBuffer stringBuffer = new StringBuffer();
 FastDateFormat.applyRules(calendar, stringBuffer);
 String formattedDate = stringBuffer.toString();
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testApplyRulesWithNullCalendar() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 StringBuffer stringBuffer = new StringBuffer();
 FastDateFormat.applyRules(null, stringBuffer);
 String formattedDate = stringBuffer.toString();
 Assert.assertEquals("", formattedDate);
 }

 @Test
 public void testApplyRulesWithCalendarInFuture() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 Calendar calendar = new GregorianCalendar(2022, 12, 31);
 StringBuffer stringBuffer = new StringBuffer();
 FastDateFormat.applyRules(calendar, stringBuffer);
 String formattedDate = stringBuffer.toString();
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testApplyRulesWithCalendarInPast() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 Calendar calendar = new GregorianCalendar(2022, 11, 28);
 StringBuffer stringBuffer = new StringBuffer();
 FastDateFormat.applyRules(calendar, stringBuffer);
 String formattedDate = stringBuffer.toString();
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testApplyRulesWithInvalidCalendar() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 Calendar calendar = new GregorianCalendar(1900, 12, 32);
 StringBuffer stringBuffer = new StringBuffer();
 FastDateFormat.applyRules(calendar, stringBuffer);
 String formattedDate = stringBuffer.toString();
 Assert.assertEquals("", formattedDate);
 }
}