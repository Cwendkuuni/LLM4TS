import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithPatternTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", java.util.TimeZone.getTimeZone("UTC"));
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithPatternLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", Locale.FRENCH);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithPatternTimeZoneLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", Locale.FRENCH, java.util.TimeZone.getTimeZone("UTC"));
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG, Locale.FRENCH);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG, java.util.TimeZone.getTimeZone("UTC"));
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocaleTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG, Locale.FRENCH, java.util.TimeZone.getTimeZone("UTC"));
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, Locale.FRENCH);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, java.util.TimeZone.getTimeZone("UTC"));
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocaleTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, Locale.FRENCH, java.util.TimeZone.getTimeZone("UTC"));
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.FRENCH);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, java.util.TimeZone.getTimeZone("UTC"));
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocaleTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.FRENCH, java.util.TimeZone.getTimeZone("UTC"));
 assertNotNull(dateFormat);
 }

 @Test
 public void testFormatDate() throws ParseException {
 Date date = FormatUtils.parseDatetime("15-03-2022 12:34:56 GMT-05:00");
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 String formattedDate = dateFormat.format(date);
 assertEquals("15-Mar-2022 12:34:56 PM", formattedDate);
 }

 @Test
 public void testFormatLong() {
 long millis = System.currentTimeMillis();
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 String formattedDate = dateFormat.format(millis);
 assertTrue(formattedDate.length() > 0);
 }

 @Test
 public void testFormatDateWithCustomPattern() throws ParseException {
 Date date = FormatUtils.parseDatetime("15-03-2022 12:34:56 GMT-05:00");
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
 String formattedDate = dateFormat.format(date);
 assertEquals("15-03-2022 12:34:56 GMT-05:00", formattedDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss z"); // incorrect hour format
 assertThrows(IllegalArgumentException.class, () -> dateFormat.parse("15-03-2022 12:34:56 GMT-05:00"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance(null);
 assertNull(dateFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance(null, null);
 assertNull(dateFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getInstance(null, null, null);
 assertNull(dateFormat);
 }

 @Test
 public void testParseDate() throws ParseException {
 String dateString = "15-Mar-2022 12:34:56 PM";
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 Date parsedDate = dateFormat.parse(dateString);
 assertNotNull(parsedDate);
 }

 @Test(expected = ParseException.class)
 public void testInvalidDateFormat() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertThrows(ParseException.class, () -> dateFormat.parse("15-03-2022 12:34:56 GMT-05:00:01"));
 }

 @Test(expected = ParseException.class)
 public void testNullDate() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertThrows(NullPointerException.class, () -> dateFormat.parse((String)null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPatternParse() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertThrows(IllegalArgumentException.class, () -> dateFormat.parse((String)null, new ParsePosition(0)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullParsePosition() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertThrows(NullPointerException.class, () -> dateFormat.parseObject((String)null, new ParsePosition(0)));
 }
}