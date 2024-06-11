import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FastDateFormatTest {

 private FastDateFormat getInstance(String pattern) {
 return FastDateFormat.getInstance(pattern);
 }

 @Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertNotNull(fastDateFormat);
 assertEquals("dd-MM-yyyy HH:mm:ss zzz", fastDateFormat.getPattern());
 assertNull(fastDateFormat.getLocale());
 assertNull(fastDateFormat.getTimeZone());
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy HH:mm:ss.SSS z");
 assertEquals("dd/MM/yyyy HH:mm:ss.SSS z", fastDateFormat.getPattern());
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy HH:mm:ss z", DateFormatUtils.UTC);
 assertEquals("dd-MM-yyyy HH:mm:ss z", fastDateFormat.getPattern());
 assertEquals(DateFormatUtils.UTC, fastDateFormat.getTimeZone());
 }

 @Test
 public void testGetInstanceWithLocale() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy HH:mm:ss z", Locale.US);
 assertEquals("dd-MM-yyyy HH:mm:ss z", fastDateFormat.getPattern());
 assertEquals(Locale.US, fastDateFormat.getLocale());
 }

 @Test
 public void testGetInstanceWithAllParameters() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy HH:mm:ss z", Locale.US, DateFormatUtils.UTC);
 assertEquals("dd-MM-yyyy HH:mm:ss z", fastDateFormat.getPattern());
 assertEquals(Locale.US, fastDateFormat.getLocale());
 assertEquals(DateFormatUtils.UTC, fastDateFormat.getTimeZone());
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 assertEquals("dd-MM-yyyy", fastDateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US);
 assertEquals("MM/dd/yyyy", fastDateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, DateFormatUtils.UTC);
 assertEquals("dd-MM-yyyy", fastDateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithAllParameters() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US, DateFormatUtils.UTC);
 assertEquals("MM/dd/yyyy", fastDateFormat.getPattern());
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
 assertEquals("HH:mm:ss z", fastDateFormat.getPattern());
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.US);
 assertEquals("hh:mm a z", fastDateFormat.getPattern());
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, DateFormatUtils.UTC);
 assertEquals("HH:mm:ss z", fastDateFormat.getPattern());
 }

 @Test
 public void testGetTimeInstanceWithAllParameters() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.US, DateFormatUtils.UTC);
 assertEquals("hh:mm a z", fastDateFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 assertEquals("dd-MM-yyyy HH:mm:ss z", fastDateFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.US);
 assertEquals("MM/dd/yyyy hh:mm a z", fastDateFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, DateFormatUtils.UTC);
 assertEquals("dd-MM-yyyy HH:mm:ss z", fastDateFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithAllParameters() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.US, DateFormatUtils.UTC);
 assertEquals("MM/dd/yyyy hh:mm a z", fastDateFormat.getPattern());
 }

 @Test
 public void testFormat() throws ParseException {
 Calendar calendar = new GregorianCalendar(2023, 0, 31, 23, 59, 59);
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy HH:mm:ss z");
 String expected = "31-01-2023 23:59:59 UTC";
 String actual = fastDateFormat.format(calendar.getTime());
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithBuffer() throws ParseException {
 Calendar calendar = new GregorianCalendar(2023, 0, 31, 23, 59, 59);
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy HH:mm:ss z");
 StringBuffer expected = new StringBuffer();
 fastDateFormat.applyRules(calendar, expected);
 String actual = expected.toString();
 assertEquals("31-01-2023 23:59:59 UTC", actual);
 }

 @Test
 public void testParse() throws ParseException {
 String dateString = "31-01-2023 23:59:59 UTC";
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy HH:mm:ss z");
 Date expected = new Date(1677164799000L);
 Date actual = fastDateFormat.parse(dateString);
 assertEquals(expected.getTime(), actual.getTime());
 }

 @Test
 public void testParseWithParsePosition() throws ParseException {
 String dateString = "31-01-2023 23:59:59 UTC";
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy HH:mm:ss z");
 ParsePosition parsePosition = new ParsePosition(0);
 Date actual = fastDateFormat.parse(dateString, parsePosition);
 Date expected = new Date(1677164799000L);
 assertEquals(expected.getTime(), actual.getTime());
 assertEquals(dateString.length(), parsePosition.getIndex());
 assertEquals("", parsePosition.getErrorMessage());
 }

 @Test
 public void testParseObject() throws ParseException {
 String dateString = "31-01-2023 23:59:59 UTC";
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy HH:mm:ss z");
 Date expected = new Date(1677164799000L);
 Object actual = fastDateFormat.parseObject(dateString);
 assertEquals(expected.getClass(), actual.getClass());
 assertEquals(expected.getTime(), ((Date)actual).getTime());
 }

 @Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = getInstance("dd-MM-yyyy HH:mm:ss z");
 FastDateFormat fastDateFormat2 = getInstance("dd-MM-yyyy HH:mm:ss z");
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testNotEquals() {
 FastDateFormat fastDateFormat1 = getInstance("dd-MM-yyyy HH:mm:ss z");
 FastDateFormat fastDateFormat2 = getInstance("dd-MM-yyyy HH:mm:ss Z");
 assertFalse(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = getInstance("dd-MM-yyyy HH:mm:ss z");
 FastDateFormat fastDateFormat2 = getInstance("dd-MM-yyyy HH:mm:ss z");
 assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
 }

 @Test
 public void testToString() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy HH:mm:ss z");
 String expected = "FastDateFormat[dd-MM-yyyy HH:mm:ss z, locale = null, timezone = UTC]";
 String actual = fastDateFormat.toString();
 assertEquals(expected, actual);
 }
}