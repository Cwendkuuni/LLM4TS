import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import static org.junit.Assert.*;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertEquals("Default date format", "dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yy hh:mm a z");
 assertEquals("Custom date format", "dd-MM-yy hh:mm a z", dateFormat.getPattern());
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z", java.util.TimeZone.getTimeZone("UTC"));
 assertEquals("Date format with timezone", "dd/MM/yyyy HH:mm:ss z", dateFormat.getPattern());
 assertEquals("UTC TimeZone", "UTC", dateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z", Locale.GERMAN);
 assertEquals("Date format with locale", "dd.MM.yyyy HH:mm:ss z", dateFormat.getPattern());
 }

 @Test
 public void testGetInstanceWithAllParameters() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z", Locale.GERMAN, java.util.TimeZone.getTimeZone("UTC"));
 assertEquals("Date format with locale and timezone", "dd.MM.yyyy HH:mm:ss z", dateFormat.getPattern());
 assertEquals("UTC TimeZone", "UTC", dateFormat.getTimeZone().getID());
 assertEquals("German Locale", Locale.GERMAN, dateFormat.getLocale());
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 assertEquals("Full date format", "EEEE, dd 'of' MMMM 'in' yyyy", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.CHINESE);
 assertEquals("Date format with locale", "EEEE, dd 月 MMMM '年' yyyy", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, java.util.TimeZone.getTimeZone("UTC"));
 assertEquals("Date format with timezone", "EEEE, dd 'of' MMMM 'in' yyyy 'at' h:mm a z", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithAllParameters() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.CHINESE, java.util.TimeZone.getTimeZone("UTC"));
 assertEquals("Date format with locale and timezone", "EEEE, dd 月 MMMM '年' yyyy 'at' h:mm a z", dateFormat.getPattern());
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertEquals("Short time format", "hh:mm a z", dateFormat.getPattern());
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRENCH);
 assertEquals("Time format with locale", "h 'heures' mm 'minutes' z", dateFormat.getPattern());
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, java.util.TimeZone.getTimeZone("UTC"));
 assertEquals("Time format with timezone", "HH:mm z", dateFormat.getPattern());
 }

 @Test
 public void testGetTimeInstanceWithAllParameters() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRENCH, java.util.TimeZone.getTimeZone("UTC"));
 assertEquals("Time format with locale and timezone", "HH 'heures' mm 'minutes' z", dateFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertEquals("Medium date-time format", "dd.MM.yyyy, HH:mm", dateFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.ITALIAN);
 assertEquals("DateTime format with locale", "dd.MM.yyyy, HH:mm", dateFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, java.util.TimeZone.getTimeZone("UTC"));
 assertEquals("DateTime format with timezone", "dd.MM.yyyy, HH:mm z", dateFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithAllParameters() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.ITALIAN, java.util.TimeZone.getTimeZone("UTC"));
 assertEquals("DateTime format with locale and timezone", "dd.MM.yyyy, HH:mm z", dateFormat.getPattern());
 }

 @Test
 public void testFormat() {
 Calendar calendar = new GregorianCalendar(2022, 11, 28, 15, 30, 45);
 FastDateFormat dateFormat = new FastDateFormat("dd/MM/yy hh:mm a z", java.util.TimeZone.getTimeZone("UTC"));
 String expected = "28/12/22 03:30 PM UTC";
 String formatted = dateFormat.format(calendar);
 assertEquals("Formatted date with custom pattern and timezone", expected, formatted);
 }

 @Test
 public void testParse() {
 String dateString = "28/12/22 03:30 PM UTC";
 Calendar expectedCalendar = new GregorianCalendar(2022, 11, 28, 15, 30, 45);
 FastDateFormat dateFormat = new FastDateFormat("dd/MM/yy hh:mm a z", java.util.TimeZone.getTimeZone("UTC"));
 Calendar parsedCalendar = dateFormat.parse(dateString);
 assertEquals("Parsed date with custom pattern and timezone", expectedCalendar, parsedCalendar);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDate() {
 FastDateFormat dateFormat = new FastDateFormat("dd/MM/yy hh:mm a z", java.util.TimeZone.getTimeZone("UTC"));
 dateFormat.parse("32/12/22 03:30 PM UTC");
 }
}