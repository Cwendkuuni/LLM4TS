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

 private final String PATTERN_FULL = "dd-MM-yyyy HH:mm:ss z";
 private final String PATTERN_LONG = "MM/dd/yyyy h:mm a z";
 private final String PATTERN_MEDIUM = "MM/dd/yy h:mm a";
 private final String PATTERN_SHORT = "M/d/yy h:mm a";

 @Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPatternTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPatternLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL, Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPatternTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL, TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("UTC"), Locale.FRENCH);
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
 public void testGetTimeInstanceWithTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.MEDIUM);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.MEDIUM, Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testFormat() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 Date date = new Date();
 String expected = FormatUtils.format(date, PATTERN_FULL);
 String actual = fastDateFormat.format(date);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testFormatLong() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_LONG);
 Date date = new Date();
 String expected = FormatUtils.format(date, PATTERN_LONG);
 String actual = fastDateFormat.format(date);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testFormatMedium() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_MEDIUM);
 Date date = new Date();
 String expected = FormatUtils.format(date, PATTERN_MEDIUM);
 String actual = fastDateFormat.format(date);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testFormatShort() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_SHORT);
 Date date = new Date();
 String expected = FormatUtils.format(date, PATTERN_SHORT);
 String actual = fastDateFormat.format(date);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 StringBuffer buffer = new StringBuffer(100);
 Date date = new Date();
 String expected = FormatUtils.format(date, PATTERN_FULL);
 fastDateFormat.format(date, buffer);
 String actual = buffer.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithBufferShort() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_SHORT);
 StringBuffer buffer = new StringBuffer(100);
 Date date = new Date();
 String expected = FormatUtils.format(date, PATTERN_SHORT);
 fastDateFormat.format(date, buffer);
 String actual = buffer.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testParse() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 String dateString = "12-12-2012 12:12:12 UTC";
 Date expected = new Date("Wed Dec 12 12:12:12 UTC 2012");
 Date actual = fastDateFormat.parse(dateString);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testParseWithParsePosition() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 String dateString = "12-12-2012 12:12:12 UTC";
 ParsePosition pos = new ParsePosition(0);
 Date expected = new Date("Wed Dec 12 12:12:12 UTC 2012");
 Date actual = fastDateFormat.parse(dateString, pos);
 Assert.assertEquals(expected, actual);
 Assert.assertEquals(dateString.length(), pos.getIndex());
 }

 @Test
 public void testParseObject() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 String dateString = "12-12-2012 12:12:12 UTC";
 Object expected = new Date("Wed Dec 12 12:12:12 UTC 2012");
 Object actual = fastDateFormat.parseObject(dateString, Date.class);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy-HH:mm:ss");
 String dateString = "12-12-2012 12:12:12 UTC";
 fastDateFormat.parse(dateString);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFormat() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 String dateString = "12-12-2012-12:12:12 UTC";
 fastDateFormat.parse(dateString);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 String dateString = "12-12-2012 12:12:12 INVALID_TIME_ZONE";
 fastDateFormat.parse(dateString);
 }

 @Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 String expected = PATTERN_FULL;
 String actual = fastDateFormat.getPattern();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 TimeZone expected = TimeZone.getDefault();
 TimeZone actual = fastDateFormat.getTimeZone();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 Locale expected = Locale.getDefault();
 Locale actual = fastDateFormat.getLocale();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 int expected = 33;
 int actual = fastDateFormat.getMaxLengthEstimate();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance(PATTERN_FULL);
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance(PATTERN_FULL);
 Assert.assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance(PATTERN_FULL);
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance(PATTERN_FULL);
 Assert.assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
 }

 @Test
 public void testToString() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 String expected = "FastDateFormat[dd-MM-yyyy HH:mm:ss z, default locale, default timezone]";
 String actual = fastDateFormat.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testApplyRules() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 Calendar calendar = new GregorianCalendar(2012, 11, 12);
 StringBuffer buffer = new StringBuffer(100);
 String expected = "Wednesday, 12-Dec-12";
 String actual = fastDateFormat.applyRules(calendar, buffer).toString();
 Assert.assertEquals(expected, actual);
 }
}