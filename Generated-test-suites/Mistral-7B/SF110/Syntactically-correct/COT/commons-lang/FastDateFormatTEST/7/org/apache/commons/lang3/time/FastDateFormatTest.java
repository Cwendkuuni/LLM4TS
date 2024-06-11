import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class FastDateFormatTest {

 private static final String PATTERN = "dd-MM-yyyy HH:mm:ss z";

 @Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 TimeZone timeZone = ZoneId.systemDefault().toZone();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN, timeZone);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithLocale() {
 Locale locale = Locale.getDefault();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN, locale);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithTimeZoneAndLocale() {
 TimeZone timeZone = ZoneId.systemDefault().toZone();
 Locale locale = Locale.getDefault();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN, timeZone, locale);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 Locale locale = Locale.getDefault();
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, locale);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 TimeZone timeZone = ZoneId.systemDefault().toZone();
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZoneAndLocale() {
 TimeZone timeZone = ZoneId.systemDefault().toZone();
 Locale locale = Locale.getDefault();
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone, locale);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 Locale locale = Locale.getDefault();
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, locale);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 TimeZone timeZone = ZoneId.systemDefault().toZone();
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, timeZone);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZoneAndLocale() {
 TimeZone timeZone = ZoneId.systemDefault().toZone();
 Locale locale = Locale.getDefault();
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, timeZone, locale);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 Locale locale = Locale.getDefault();
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, locale);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 TimeZone timeZone = ZoneId.systemDefault().toZone();
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, timeZone);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
 TimeZone timeZone = ZoneId.systemDefault().toZone();
 Locale locale = Locale.getDefault();
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, timeZone, locale);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testFormatObjectWithStringBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN);
 StringBuffer stringBuffer = new StringBuffer(100);
 Date date = new Date();
 stringBuffer.append(fastDateFormat.format(date));
 Assert.assertEquals(FormatUtils.format(date, PATTERN), stringBuffer.toString());
 }

 @Test
 public void testFormatObjectWithStringBufferAndFieldPosition() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN);
 StringBuffer stringBuffer = new StringBuffer(100);
 Date date = new Date();
 int position = 0;
 fastDateFormat.format(date, stringBuffer, new FieldPosition(position, 0));
 Assert.assertEquals(FormatUtils.format(date, PATTERN), stringBuffer.toString());
 }

 @Test
 public void testFormatObjectWithString() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN);
 String formattedDate = fastDateFormat.format(new Date());
 Assert.assertEquals(FormatUtils.format(new Date(), PATTERN), formattedDate);
 }

 @Test
 public void testFormatObjectWithStringAndLocale() {
 Locale locale = Locale.getDefault();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN, locale);
 String formattedDate = fastDateFormat.format(new Date());
 Assert.assertEquals(FormatUtils.format(new Date(), PATTERN, locale), formattedDate);
 }

 @Test
 public void testFormatObjectWithStringAndTimeZone() {
 TimeZone timeZone = ZoneId.systemDefault().toZone();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN, timeZone);
 String formattedDate = fastDateFormat.format(new Date());
 Assert.assertEquals(FormatUtils.format(new Date(), PATTERN, timeZone), formattedDate);
 }

 @Test
 public void testFormatObjectWithStringAndTimeZoneAndLocale() {
 TimeZone timeZone = ZoneId.systemDefault().toZone();
 Locale locale = Locale.getDefault();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN, timeZone, locale);
 String formattedDate = fastDateFormat.format(new Date());
 Assert.assertEquals(FormatUtils.format(new Date(), PATTERN, timeZone, locale), formattedDate);
 }

 @Test
 public void testParseObjectWithString() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN);
 Date date = fastDateFormat.parse("15-06-2022 11:55:34 CET");
 Assert.assertNotNull(date);
 }

 @Test
 public void testParseObjectWithStringAndParsePosition() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN);
 String source = "15-06-2022 11:55:34 CET";
 ParsePosition parsePosition = new ParsePosition(0);
 Date date = fastDateFormat.parse(source, parsePosition);
 Assert.assertNotNull(date);
 Assert.assertEquals(source.length(), parsePosition.getIndex());
 }

 @Test
 public void testParseObjectWithStringAndLocale() {
 Locale locale = Locale.getDefault();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN, locale);
 String source = "15-06-2022 11:55:34 CET";
 Date date = fastDateFormat.parse(source);
 Assert.assertNotNull(date);
 }

 @Test
 public void testParseObjectWithStringAndTimeZone() {
 TimeZone timeZone = ZoneId.systemDefault().toZone();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN, timeZone);
 String source = "15-06-2022 11:55:34 CET";
 Date date = fastDateFormat.parse(source);
 Assert.assertNotNull(date);
 }

 @Test
 public void testParseObjectWithStringAndTimeZoneAndLocale() {
 TimeZone timeZone = ZoneId.systemDefault().toZone();
 Locale locale = Locale.getDefault();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN, timeZone, locale);
 String source = "15-06-2022 11:55:34 CET";
 Date date = fastDateFormat.parse(source);
 Assert.assertNotNull(date);
 }

 @Test(expected = ParseException.class)
 public void testParseObjectWithInvalidString() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN);
 String source = "InvalidDateFormat";
 fastDateFormat.parse(source);
 }

 @Test(expected = ParseException.class)
 public void testParseObjectWithInvalidStringAndParsePosition() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN);
 String source = "InvalidDateFormat";
 ParsePosition parsePosition = new ParsePosition(0);
 fastDateFormat.parse(source, parsePosition);
 }

 @Test(expected = ParseException.class)
 public void testParseObjectWithInvalidStringAndLocale() {
 Locale locale = Locale.getDefault();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN, locale);
 String source = "InvalidDateFormat";
 fastDateFormat.parse(source);
 }

 @Test(expected = ParseException.class)
 public void testParseObjectWithInvalidStringAndTimeZone() {
 TimeZone timeZone = ZoneId.systemDefault().toZone();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN, timeZone);
 String source = "InvalidDateFormat";
 fastDateFormat.parse(source);
 }

 @Test(expected = ParseException.class)
 public void testParseObjectWithInvalidStringAndTimeZoneAndLocale() {
 TimeZone timeZone = ZoneId.systemDefault().toZone();
 Locale locale = Locale.getDefault();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN, timeZone, locale);
 String source = "InvalidDateFormat";
 fastDateFormat.parse(source);
 }

 @Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN);
 String pattern = fastDateFormat.getPattern();
 Assert.assertEquals(PATTERN, pattern);
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN);
 TimeZone timeZone = fastDateFormat.getTimeZone();
 Assert.assertEquals(ZoneId.systemDefault().toZone(), timeZone);
 }

 @Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN);
 Locale locale = fastDateFormat.getLocale();
 Assert.assertEquals(Locale.getDefault(), locale);
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN);
 int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
 Assert.assertEquals(FormatUtils.getLength(PATTERN), maxLengthEstimate);
 }

 @Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance(PATTERN);
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance(PATTERN);
 boolean equals = fastDateFormat1.equals(fastDateFormat2);
 Assert.assertTrue(equals);
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance(PATTERN);
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance(PATTERN);
 int hashCode1 = fastDateFormat1.hashCode();
 int hashCode2 = fastDateFormat2.hashCode();
 Assert.assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN);
 String toString = fastDateFormat.toString();
 String expectedToString = "FastDateFormat[dd-MM-yyyy HH:mm:ss z, default locale, default timezone]";
 Assert.assertEquals(expectedToString, toString);
 }

 @Test
 public void testApplyRules() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN);
 Calendar calendar = new GregorianCalendar(2022, 5, 15);
 StringBuffer stringBuffer = new StringBuffer(100);
 FastDateFormat.applyRules(calendar, stringBuffer);
 Assert.assertEquals(fastDateFormat.format(calendar), stringBuffer.toString());
 }
}