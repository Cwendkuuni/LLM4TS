package test.org.apache.commons.lang3.time;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatCache;
import org.apache.commons.lang3.time.FastDatePrinter;
import org.apache.commons.lang3.time.FastDateParser;
import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

 private FastDateFormat getFormattedInstance(String pattern, TimeZone timeZone, Locale locale) {
 return FastDateFormat.getInstance(pattern, timeZone, locale);
 }

 @Test
 public void testGetInstance() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithPatternTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithPatternLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy", Locale.FRANCE);
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithPatternTimeZoneLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.FRANCE);
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZoneLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"), Locale.FRANCE);
 Assert.assertNotNull(dateFormat);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 Assert.assertNotNull(timeFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRANCE);
 Assert.assertNotNull(timeFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(timeFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZoneLocale() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"), Locale.FRANCE);
 Assert.assertNotNull(timeFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 Assert.assertNotNull(dateTimeFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRANCE);
 Assert.assertNotNull(dateTimeFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(dateTimeFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZoneLocale() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"), Locale.FRANCE);
 Assert.assertNotNull(dateTimeFormat);
 }

 @Test
 public void testFormatDate() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 Date date = new Date();
 String formattedDate = dateFormat.format(date);
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testFormatDateWithPattern() {
 FastDateFormat dateFormat = getFormattedInstance("dd-MM-yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 Date date = new Date();
 String formattedDate = dateFormat.format(date);
 Assert.assertEquals("01-11-2022", formattedDate);
 }

 @Test
 public void testFormatDateWithLocale() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.FRANCE);
 Date date = new Date();
 String formattedDate = dateFormat.format(date);
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testFormatDateWithTimeZone() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 Date date = new Date(System.currentTimeMillis() + 3600 * 1000);
 String formattedDate = dateFormat.format(date);
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testFormatDateWithTimeZoneLocale() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.FRANCE);
 Date date = new Date(System.currentTimeMillis() + 3600 * 1000);
 String formattedDate = dateFormat.format(date);
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testFormatCalendar() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 Calendar calendar = new GregorianCalendar();
 String formattedDate = dateFormat.format(calendar);
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testFormatCalendarWithPattern() {
 FastDateFormat dateFormat = getFormattedInstance("dd-MM-yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 Calendar calendar = new GregorianCalendar();
 String formattedDate = dateFormat.format(calendar);
 Assert.assertEquals("01-11-2022", formattedDate);
 }

 @Test
 public void testFormatCalendarWithLocale() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.FRANCE);
 Calendar calendar = new GregorianCalendar();
 String formattedDate = dateFormat.format(calendar);
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testFormatCalendarWithTimeZone() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 Calendar calendar = new GregorianCalendar(2022, 10, 31);
 String formattedDate = dateFormat.format(calendar);
 Assert.assertEquals("31-11-2022", formattedDate);
 }

 @Test
 public void testFormatCalendarWithTimeZoneLocale() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.FRANCE);
 Calendar calendar = new GregorianCalendar(2022, 10, 31);
 String formattedDate = dateFormat.format(calendar);
 Assert.assertNotNull(formattedDate);
 }

 @Test
 public void testParseDate() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 String dateString = "01-11-2022";
 Date parsedDate = dateFormat.parse(dateString);
 Assert.assertNotNull(parsedDate);
 }

 @Test
 public void testParseDateWithPattern() {
 FastDateFormat dateFormat = getFormattedInstance("dd-MM-yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 String dateString = "01-11-2022";
 Date parsedDate = dateFormat.parse(dateString);
 Assert.assertEquals(new Date(1668537200000L), parsedDate);
 }

 @Test
 public void testParseDateWithLocale() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.FRANCE);
 String dateString = "01/11/2022";
 Date parsedDate = dateFormat.parse(dateString);
 Assert.assertNotNull(parsedDate);
 }

 @Test
 public void testParseDateWithTimeZone() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 String dateString = "01-11-2022 15:30:00";
 Date parsedDate = dateFormat.parse(dateString);
 Assert.assertEquals(new Date(1668540600000L), parsedDate);
 }

 @Test
 public void testParseDateWithTimeZoneLocale() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy HH:mm:ss", TimeZone.getTimeZone("UTC"), Locale.FRANCE);
 String dateString = "01/11/2022 15:30:00";
 Date parsedDate = dateFormat.parse(dateString);
 Assert.assertNotNull(parsedDate);
 }

 @Test
 public void testParseObject() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 String dateString = "01-11-2022";
 Date parsedDate = (Date)dateFormat.parseObject(dateString);
 Assert.assertNotNull(parsedDate);
 }

 @Test
 public void testGetPattern() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 String pattern = dateFormat.getPattern();
 Assert.assertEquals("dd/MM/yyyy", pattern);
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 TimeZone timeZone = dateFormat.getTimeZone();
 Assert.assertEquals(TimeZone.getTimeZone("UTC"), timeZone);
 }

 @Test
 public void testGetLocale() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 Locale locale = dateFormat.getLocale();
 Assert.assertEquals(Locale.US, locale);
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 int maxLengthEstimate = dateFormat.getMaxLengthEstimate();
 Assert.assertEquals(10, maxLengthEstimate);
 }

 @Test
 public void testEquals() {
 FastDateFormat dateFormat1 = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 FastDateFormat dateFormat2 = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 Assert.assertTrue(dateFormat1.equals(dateFormat2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat dateFormat1 = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 FastDateFormat dateFormat2 = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 Assert.assertEquals(dateFormat1.hashCode(), dateFormat2.hashCode());
 }

 @Test
 public void testToString() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 String toString = dateFormat.toString();
 Assert.assertEquals("FastDateFormat[dd/MM/yyyy, en_US, UTC]", toString);
 }

 @Test
 public void testApplyRules() {
 FastDateFormat dateFormat = getFormattedInstance("dd/MM/yyyy", TimeZone.getTimeZone("UTC"), Locale.US);
 Calendar calendar = new GregorianCalendar(2022, 10, 31);
 StringBuffer buf = new StringBuffer();
 dateFormat.applyRules(calendar, buf);
 Assert.assertEquals("31-11-2022", buf.toString());
 }
}