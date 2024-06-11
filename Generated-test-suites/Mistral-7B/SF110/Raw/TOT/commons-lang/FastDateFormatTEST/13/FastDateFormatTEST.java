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
 public void testGetInstanceWithPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a");
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a", new TimeZone("UTC"));
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a", new Locale("fr_FR"));
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithPatternTimeZoneLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a", new Locale("fr_FR"), new TimeZone("UTC"));
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new Locale("fr_FR"));
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new TimeZone("UTC"));
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithPatternTimeZoneLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new Locale("fr_FR"), new TimeZone("UTC"));
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 Assert.assertNotNull(timeFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new Locale("fr_FR"));
 Assert.assertNotNull(timeFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new TimeZone("UTC"));
 Assert.assertNotNull(timeFormat);
 }

 @Test
 public void testGetTimeInstanceWithPatternTimeZoneLocale() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new Locale("fr_FR"), new TimeZone("UTC"));
 Assert.assertNotNull(timeFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 Assert.assertNotNull(dateTimeFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new Locale("fr_FR"));
 Assert.assertNotNull(dateTimeFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new TimeZone("UTC"));
 Assert.assertNotNull(dateTimeFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithPatternTimeZoneLocale() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new Locale("fr_FR"), new TimeZone("UTC"));
 Assert.assertNotNull(dateTimeFormat);
 }

 @Test
 public void testFormat() throws ParseException {
 Date date = new Date();
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a");
 String formattedDate = dateFormat.format(date);
 Assert.assertEquals(Formats.DATE_TIME_FORMAT.format(date), formattedDate);
 }

 @Test
 public void testFormatWithLocale() throws ParseException {
 Locale locale = new Locale("fr_FR");
 Date date = new Date();
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a", locale);
 String formattedDate = dateFormat.format(date);
 Assert.assertEquals(FormatUtils.format(date, "dd MMMM yyyy hh:mm:ss a", locale), formattedDate);
 }

 @Test
 public void testFormatWithTimeZone() throws ParseException {
 TimeZone timeZone = new TimeZone("UTC");
 Date date = new Date();
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a", null, timeZone);
 String formattedDate = dateFormat.format(date);
 Assert.assertEquals(FormatUtils.format(date, "dd-MM-yyyy hh:mm:ss z", timeZone), formattedDate);
 }

 @Test
 public void testFormatWithPatternTimeZoneLocale() throws ParseException {
 Locale locale = new Locale("fr_FR");
 TimeZone timeZone = new TimeZone("UTC");
 Date date = new Date();
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a", locale, timeZone);
 String formattedDate = dateFormat.format(date);
 Assert.assertEquals(FormatUtils.format(date, "dd MMMM yyyy hh:mm:ss a z", locale, timeZone), formattedDate);
 }

 @Test
 public void testParse() throws ParseException {
 String dateString = "21-12-2021 12:00:00 PM";
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a");
 Date parsedDate = dateFormat.parse(dateString);
 Assert.assertNotNull(parsedDate);
 }

 @Test
 public void testParseWithLocale() throws ParseException {
 String dateString = "21 décembre 2021 12:00:00 PM";
 Locale locale = new Locale("fr_FR");
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd MMMM yyyy hh:mm:ss a", locale);
 Date parsedDate = dateFormat.parse(dateString);
 Assert.assertNotNull(parsedDate);
 }

 @Test
 public void testParseWithTimeZone() throws ParseException {
 String dateString = "21-12-2021 12:00:00 UTC";
 TimeZone timeZone = new TimeZone("UTC");
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss z", null, timeZone);
 Date parsedDate = dateFormat.parse(dateString);
 Assert.assertNotNull(parsedDate);
 Assert.assertEquals(timeZone, parsedDate.getTimeZone());
 }

 @Test
 public void testParseWithPatternTimeZoneLocale() throws ParseException {
 String dateString = "21 décembre 2021 12:00:00 UTC";
 Locale locale = new Locale("fr_FR");
 TimeZone timeZone = new TimeZone("UTC");
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd MMMM yyyy hh:mm:ss z", locale, timeZone);
 Date parsedDate = dateFormat.parse(dateString);
 Assert.assertNotNull(parsedDate);
 Assert.assertEquals(timeZone, parsedDate.getTimeZone());
 }

 @Test
 public void testParseInvalid() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a");
 try {
 dateFormat.parse("Not a date");
 fail();
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseInvalidWithLocale() throws ParseException {
 String dateString = "21 décembre 2021 33:60:00 PM";
 Locale locale = new Locale("fr_FR");
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd MMMM yyyy hh:mm:ss a", locale);
 try {
 dateFormat.parse(dateString);
 fail();
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseInvalidWithTimeZone() throws ParseException {
 String dateString = "21-12-2021 12:60:00 UTC";
 TimeZone timeZone = new TimeZone("UTC");
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss z", null, timeZone);
 try {
 dateFormat.parse(dateString);
 fail();
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseInvalidWithPatternTimeZoneLocale() throws ParseException {
 String dateString = "21 décembre 2021 12:60:00 UTC";
 Locale locale = new Locale("fr_FR");
 TimeZone timeZone = new TimeZone("UTC");
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd MMMM yyyy hh:mm:ss z", locale, timeZone);
 try {
 dateFormat.parse(dateString);
 fail();
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseObject() throws ParseException {
 String dateString = "21-12-2021 12:00:00 PM";
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a");
 Date parsedDate = (Date)dateFormat.parseObject(dateString);
 Assert.assertNotNull(parsedDate);
 }

 @Test
 public void testParseObjectWithLocale() throws ParseException {
 String dateString = "21 décembre 2021 12:00:00 PM";
 Locale locale = new Locale("fr_FR");
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd MMMM yyyy hh:mm:ss a", locale);
 Date parsedDate = (Date)dateFormat.parseObject(dateString);
 Assert.assertNotNull(parsedDate);
 }

 @Test
 public void testParseObjectWithTimeZone() throws ParseException {
 String dateString = "21-12-2021 12:00:00 UTC";
 TimeZone timeZone = new TimeZone("UTC");
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss z", null, timeZone);
 Date parsedDate = (Date)dateFormat.parseObject(dateString);
 Assert.assertNotNull(parsedDate);
 Assert.assertEquals(timeZone, parsedDate.getTimeZone());
 }

 @Test
 public void testParseObjectWithPatternTimeZoneLocale() throws ParseException {
 String dateString = "21 décembre 2021 12:00:00 UTC";
 Locale locale = new Locale("fr_FR");
 TimeZone timeZone = new TimeZone("UTC");
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd MMMM yyyy hh:mm:ss z", locale, timeZone);
 Date parsedDate = (Date)dateFormat.parseObject(dateString);
 Assert.assertNotNull(parsedDate);
 Assert.assertEquals(timeZone, parsedDate.getTimeZone());
 }

 @Test
 public void testGetPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a");
 Assert.assertEquals("dd-MM-yyyy hh:mm:ss a", dateFormat.getPattern());
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a");
 Assert.assertNotNull(dateFormat.getTimeZone());
 }

 @Test
 public void testGetLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a");
 Assert.assertNull(dateFormat.getLocale());
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a");
 Assert.assertEquals(32, dateFormat.getMaxLengthEstimate());
 }

 @Test
 public void testEquals() {
 FastDateFormat dateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a");
 FastDateFormat dateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a");
 Assert.assertTrue(dateFormat1.equals(dateFormat2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat dateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a");
 FastDateFormat dateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a");
 Assert.assertEquals(dateFormat1.hashCode(), dateFormat2.hashCode());
 }

 @Test
 public void testToString() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a");
 Assert.assertEquals("FastDateFormat[dd-MM-yyyy hh:mm:ss a, ,]", dateFormat.toString());
 }

 @Test
 public void testApplyRules() {
 Calendar calendar = new GregorianCalendar(2021, 11, 21);
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a");
 StringBuffer buf = new StringBuffer();
 dateFormat.applyRules(calendar, buf);
 Assert.assertEquals("21-12-2021", buf.toString());
 }
}