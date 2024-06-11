import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FastDateFormatUtil;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat format = FastDateFormat.getInstance();
 assertEquals("Default format instance created", "dd/MM/yyyy HH:mm:ss zzz", format.getPattern());
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yy HH:mm:ss.SSS z");
 assertEquals("Format instance created with specified pattern", "dd-MM-yy HH:mm:ss.SSS z", format.getPattern());
 }

 @Test
 public void testGetInstanceWithLocale() {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yy HH:mm:ss.SSS z", Locale.FRENCH);
 assertEquals("Format instance created with specified locale", "dd-MM-yy HH:mm:ss.SSS z", format.getPattern());
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yy HH:mm:ss.SSS z", Locale.ENGLISH, TimeZone.getTimeZone("Asia/Kolkata"));
 assertEquals("Format instance created with specified timezone", "dd-MM-yy HH:mm:ss.SSS z", format.getPattern());
 }

 @Test
 public void testGetInstanceWithAllParams() {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yy HH:mm:ss.SSS z", Locale.ENGLISH, TimeZone.getTimeZone("Asia/Kolkata"), new java.util.GregorianCalendar(2000, 0, 1));
 assertEquals("Format instance created with all parameters", "dd-MM-yy HH:mm:ss.SSS z", format.getPattern());
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance();
 assertEquals("Default date format instance created", "dd/MM/yyyy", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(Locale.FRENCH);
 assertEquals("Date format instance created with specified locale", "dd/MM/yyyy", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(Locale.ENGLISH, TimeZone.getTimeZone("Asia/Kolkata"));
 assertEquals("Date format instance created with specified timezone", "dd/MM/yyyy", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithAllParams() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(Locale.ENGLISH, TimeZone.getTimeZone("Asia/Kolkata"), new java.util.GregorianCalendar(2000, 0, 1));
 assertEquals("Date format instance created with all parameters", "dd/MM/yyyy", dateFormat.getPattern());
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance();
 assertEquals("Default time format instance created", "HH:mm:ss z", timeFormat.getPattern());
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(Locale.FRENCH);
 assertEquals("Time format instance created with specified locale", "HH:mm:ss z", timeFormat.getPattern());
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(Locale.ENGLISH, TimeZone.getTimeZone("Asia/Kolkata"));
 assertEquals("Time format instance created with specified timezone", "HH:mm:ss z", timeFormat.getPattern());
 }

 @Test
 public void testGetTimeInstanceWithAllParams() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(Locale.ENGLISH, TimeZone.getTimeZone("Asia/Kolkata"), new java.util.GregorianCalendar(2000, 0, 1));
 assertEquals("Time format instance created with all parameters", "HH:mm:ss z", timeFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance();
 assertEquals("Default date-time format instance created", "dd/MM/yyyy HH:mm:ss z", dateTimeFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(Locale.FRENCH);
 assertEquals("Date-time format instance created with specified locale", "dd/MM/yyyy HH:mm:ss z", dateTimeFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(Locale.ENGLISH, TimeZone.getTimeZone("Asia/Kolkata"));
 assertEquals("Date-time format instance created with specified timezone", "dd/MM/yyyy HH:mm:ss z", dateTimeFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithAllParams() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(Locale.ENGLISH, TimeZone.getTimeZone("Asia/Kolkata"), new java.util.GregorianCalendar(2000, 0, 1));
 assertEquals("Date-time format instance created with all parameters", "dd/MM/yyyy HH:mm:ss z", dateTimeFormat.getPattern());
 }

 @Test
 public void testFormat() {
 FastDateFormat format = FastDateFormat.getDateTimeInstance();
 Date date = new Date();
 String expected = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
 String actual = format.format(date);
 assertEquals("Formatted date-time matches expected format", expected, actual);
 }

 @Test
 public void testParse() {
 FastDateFormat format = FastDateFormat.getDateTimeInstance();
 String dateString = "31/12/2021 23:59:59";
 Date expected = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateString);
 Date actual = null;
 try {
 actual = format.parse(dateString);
 } catch (ParseException e) {
 fail("Expected parse to succeed");
 }
 assertEquals("Parsed date-time matches expected date", expected, actual);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFormat() {
 FastDateFormat format = FastDateFormat.getDateTimeInstance();
 String invalidDateString = "31/13/2021 23:59:59";
 format.parse(invalidDateString);
 fail("Expected ParseException to be thrown");
 }

 @Test
 public void testEquals() {
 FastDateFormat format1 = FastDateFormat.getDateTimeInstance();
 FastDateFormat format2 = FastDateFormat.getDateTimeInstance();
 assertTrue("Two format instances with same pattern are equal", format1.equals(format2));
 }

 @Test
 public void testNotEquals() {
 FastDateFormat format1 = FastDateFormat.getDateTimeInstance();
 FastDateFormat format2 = FastDateFormat.getDateInstance();
 assertFalse("Two format instances with different patterns are not equal", format1.equals(format2));
 }
}