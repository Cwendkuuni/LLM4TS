import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertEquals("dd/MM/yyyy HH:mm:ss z", dateFormat.getPattern());
 assertNull(dateFormat.getTimeZone());
 assertNull(dateFormat.getLocale());
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
 assertEquals("dd-MM-yyyy HH:mm:ss z", dateFormat.getPattern());
 assertNull(dateFormat.getTimeZone());
 assertNull(dateFormat.getLocale());
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", Locale.US, TimeZone.getTimeZone("UTC"));
 assertEquals("dd-MM-yyyy HH:mm:ss z", dateFormat.getPattern());
 assertEquals(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
 assertNull(dateFormat.getLocale());
 }

 @Test
 public void testGetInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", null, Locale.FRENCH);
 assertEquals("dd-MM-yyyy HH:mm:ss z", dateFormat.getPattern());
 assertNull(dateFormat.getTimeZone());
 assertEquals(Locale.FRENCH, dateFormat.getLocale());
 }

 @Test
 public void testGetInstanceWithAllParameters() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", Locale.FRENCH, TimeZone.getTimeZone("UTC"));
 assertEquals("dd-MM-yyyy HH:mm:ss z", dateFormat.getPattern());
 assertEquals(Locale.FRENCH, dateFormat.getLocale());
 assertEquals(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
 assertEquals("dd/MM/yy", dateFormat.getPattern());
 assertNull(dateFormat.getTimeZone());
 assertNull(dateFormat.getLocale());
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.US);
 assertEquals("MM/dd/yy", dateFormat.getPattern());
 assertNull(dateFormat.getTimeZone());
 assertNull(dateFormat.getLocale());
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"));
 assertEquals("dd/MM/yy", dateFormat.getPattern());
 assertNull(dateFormat.getLocale());
 assertNull(dateFormat.getTimeZone());
 }

 @Test
 public void testGetDateInstanceWithAllParameters() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.FRENCH, TimeZone.getTimeZone("UTC"));
 assertEquals("dd/MM/yy", dateFormat.getPattern());
 assertNull(dateFormat.getLocale());
 assertNull(dateFormat.getTimeZone());
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
 assertEquals("HH:mm:ss z", dateFormat.getPattern());
 assertNull(dateFormat.getTimeZone());
 assertNull(dateFormat.getLocale());
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, Locale.US);
 assertEquals("HH:mm:ss z", dateFormat.getPattern());
 assertNull(dateFormat.getTimeZone());
 assertNull(dateFormat.getLocale());
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, TimeZone.getTimeZone("UTC"));
 assertEquals("HH:mm:ss z", dateFormat.getPattern());
 assertNull(dateFormat.getLocale());
 assertNull(dateFormat.getTimeZone());
 }

 @Test
 public void testGetTimeInstanceWithAllParameters() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, Locale.FRENCH, TimeZone.getTimeZone("UTC"));
 assertEquals("HH:mm:ss z", dateFormat.getPattern());
 assertNull(dateFormat.getLocale());
 assertNull(dateFormat.getTimeZone());
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG);
 assertEquals("dd-MM-yyyy HH:mm", dateFormat.getPattern());
 assertNull(dateFormat.getTimeZone());
 assertNull(dateFormat.getLocale());
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.US);
 assertEquals("MM/dd/yyyy h a", dateFormat.getPattern());
 assertNull(dateFormat.getTimeZone());
 assertNull(dateFormat.getLocale());
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, TimeZone.getTimeZone("UTC"));
 assertEquals("dd-MM-yyyy HH:mm", dateFormat.getPattern());
 assertNull(dateFormat.getLocale());
 assertNull(dateFormat.getTimeZone());
 }

 @Test
 public void testGetDateTimeInstanceWithAllParameters() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.FRENCH, TimeZone.getTimeZone("UTC"));
 assertEquals("dd-MM-yyyy HH:mm", dateFormat.getPattern());
 assertNull(dateFormat.getLocale());
 assertNull(dateFormat.getTimeZone());
 }

 @Test
 public void testFormatDate() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 Date date = new Date();
 String formattedDate = dateFormat.format(date);
 assertEquals("30/03/2023", formattedDate);
 }

 @Test
 public void testFormatDateWithCustomPattern() {
 FastDateFormat dateFormat = new FastDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
 Date date = new Date();
 String formattedDate = dateFormat.format(date);
 assertEquals("30/03/2023 14:32:12.345", formattedDate);
 }

 @Test
 public void testFormatCalendar() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 Calendar calendar = new GregorianCalendar(2023, 3, 30);
 String formattedDate = dateFormat.format(calendar.getTime());
 assertEquals("30/03/2023", formattedDate);
 }

 @Test
 public void testParseDate() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 String dateString = "30/03/2023";
 Date parsedDate = dateFormat.parse(dateString);
 assertEquals(new Date(1679356800000L), parsedDate);
 }

 @Test(expected = ParseException.class)
 public void testParseDateInvalidFormat() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 String dateString = "30/03/2023XX";
 assertThrows(ParseException.class, () -> dateFormat.parse(dateString));
 }

 @Test(expected = ParseException.class)
 public void testParseDateMissingYear() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 String dateString = "30/03/";
 assertThrows(ParseException.class, () -> dateFormat.parse(dateString));
 }

 @Test(expected = ParseException.class)
 public void testParseDateMissingMonth() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 String dateString = "30/";
 assertThrows(ParseException.class, () -> dateFormat.parse(dateString));
 }

 @Test(expected = ParseException.class)
 public void testParseDateMissingDay() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 String dateString = "/";
 assertThrows(ParseException.class, () -> dateFormat.parse(dateString));
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithInvalidMonth() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 String dateString = "32/03/2023";
 assertThrows(ParseException.class, () -> dateFormat.parse(dateString));
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithInvalidDay() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 String dateString = "31/32/2023";
 assertThrows(ParseException.class, () -> dateFormat.parse(dateString));
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithInvalidYear() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 String dateString = "30/03/1900";
 assertThrows(ParseException.class, () -> dateFormat.parse(dateString));
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithInvalidFormat() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 String dateString = "30/03/2023XX";
 assertThrows(ParseException.class, () -> dateFormat.parse(dateString));
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 int maxLength = dateFormat.getMaxLengthEstimate();
 assertEquals(52, maxLength);
 }

 @Test
 public void testEquals() {
 FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 assertTrue(dateFormat1.equals(dateFormat2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 assertEquals(dateFormat1.hashCode(), dateFormat2.hashCode());
 }

 @Test
 public void testToString() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 String toString = dateFormat.toString();
 assertEquals("FastDateFormat[dd/MM/yyyy, null, null]", toString);
 }
}