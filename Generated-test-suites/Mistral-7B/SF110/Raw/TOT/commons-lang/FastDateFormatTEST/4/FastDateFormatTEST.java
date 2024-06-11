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
 assertEquals("Default format", dateFormat.getPattern());
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a");
 assertEquals("dd/MM/yyyy hh:mm a", dateFormat.getPattern());
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a", new java.util.SimpleTimeZone(0, "UTC"));
 assertEquals("dd/MM/yyyy hh:mm a", dateFormat.getPattern());
 }

 @Test
 public void testGetInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a", Locale.FRENCH);
 assertEquals("dd/MM/yyyy hh:mm a", dateFormat.getPattern());
 }

 @Test
 public void testGetInstanceWithPatternTimeZoneLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a", new java.util.SimpleTimeZone(0, "UTC"), Locale.FRENCH);
 assertEquals("dd/MM/yyyy hh:mm a", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertEquals("Medium date format", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRENCH);
 assertEquals("Medium date format", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new java.util.SimpleTimeZone(0, "UTC"));
 assertEquals("Medium date format", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithPatternTimeZoneLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new java.util.SimpleTimeZone(0, "UTC"), Locale.FRENCH);
 assertEquals("Medium date format", dateFormat.getPattern());
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertEquals("Short time format", dateFormat.getPattern());
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRENCH);
 assertEquals("Short time format", dateFormat.getPattern());
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new java.util.SimpleTimeZone(0, "UTC"));
 assertEquals("Short time format", dateFormat.getPattern());
 }

 @Test
 public void testGetTimeInstanceWithPatternTimeZoneLocale() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new java.util.SimpleTimeZone(0, "UTC"), Locale.FRENCH);
 assertEquals("Short time format", dateFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertEquals("Medium date and short time format", dateFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRENCH);
 assertEquals("Medium date and short time format", dateFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new java.util.SimpleTimeZone(0, "UTC"));
 assertEquals("Medium date and short time format", dateFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithPatternTimeZoneLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new java.util.SimpleTimeZone(0, "UTC"), Locale.FRENCH);
 assertEquals("Medium date and short time format", dateFormat.getPattern());
 }

 @Test
 public void testFormat() {
 Date date = new Date();
 FastDateFormat dateFormat = FastDateFormat.getDateInstance();
 String dateString = dateFormat.format(date);
 assertNotNull("Date format should not be null", dateString);
 }

 @Test
 public void testFormatWithPattern() {
 Date date = new Date();
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
 String dateString = dateFormat.format(date);
 assertEquals("dd/MM/yyyy", dateFormat.getPattern());
 assertEquals("Date string should match pattern", "dd/MM/yyyy", dateString);
 }

 @Test
 public void testFormatWithLocale() {
 Date date = new Date();
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy", Locale.FRENCH);
 String dateString = dateFormat.format(date);
 assertEquals("French date format", "dd/MM/yyyy", dateFormat.getPattern());
 assertEquals("Date string should match pattern", "dd/MM/yyyy", dateString);
 }

 @Test
 public void testFormatWithTimeZone() {
 Date date = new Date();
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy", new java.util.SimpleTimeZone(0, "UTC"));
 String dateString = dateFormat.format(date);
 assertEquals("UTC date format", "dd/MM/yyyy", dateFormat.getPattern());
 assertEquals("Date string should match pattern", "dd/MM/yyyy", dateString);
 }

 @Test
 public void testFormatWithPatternLocaleTimeZone() {
 Date date = new Date();
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy", Locale.FRENCH, new java.util.SimpleTimeZone(0, "UTC"));
 String dateString = dateFormat.format(date);
 assertEquals("French UTC date format", "dd/MM/yyyy", dateFormat.getPattern());
 assertEquals("Date string should match pattern", "dd/MM/yyyy", dateString);
 }

 @Test
 public void testParse() {
 String dateString = "25/12/2021";
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
 Date date = dateFormat.parse(dateString);
 assertNotNull("Date parse should not be null", date);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDate() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
 String dateString = "32/12/2021";
 try {
 dateFormat.parse(dateString);
 } catch (ParseException e) {
 // Expected exception
 }
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a");
 String dateString = "25/12/2021 32:60 PM";
 try {
 dateFormat.parse(dateString);
 } catch (ParseException e) {
 // Expected exception
 }
 }

 @Test
 public void testParseWithLocale() {
 String dateString = "25 décembre 2021";
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd MMMM yyyy", Locale.FRENCH);
 Date date = dateFormat.parse(dateString);
 assertNotNull("Date parse should not be null", date);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidLocale() {
 String dateString = "25 décembre 2021";
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd MMMM yyyy", Locale.GERMAN);
 try {
 dateFormat.parse(dateString);
 } catch (ParseException e) {
 // Expected exception
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithTimeZone() {
 String dateString = "25/12/2021 12:00:00 UTC";
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm zzz", new java.util.SimpleTimeZone(0, "UTC"));
 try {
 dateFormat.parse(dateString);
 } catch (ParseException e) {
 // Expected exception
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidTimeZone() {
 String dateString = "25/12/2021 12:00:00 EST";
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm zzz", new java.util.SimpleTimeZone(1, "EST"));
 try {
 dateFormat.parse(dateString);
 } catch (ParseException e) {
 // Expected exception
 }
 }

 @Test
 public void testParseWithPatternLocaleTimeZone() {
 String dateString = "25 décembre 2021 12:00:00 UTC";
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd MMMM yyyy hh:mm zzz", Locale.FRENCH, new java.util.SimpleTimeZone(0, "UTC"));
 Date date = dateFormat.parse(dateString);
 assertNotNull("Date parse should not be null", date);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidPatternLocaleTimeZone() {
 String dateString = "25 décembre 2021 12:00:00 EST";
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd MMMM yyyy hh:mm zzz", Locale.FRENCH, new java.util.SimpleTimeZone(1, "EST"));
 try {
 dateFormat.parse(dateString);
 } catch (ParseException e) {
 // Expected exception
 }
 }

 @Test
 public void testEquals() {
 FastDateFormat dateFormat1 = FastDateFormat.getInstance();
 FastDateFormat dateFormat2 = FastDateFormat.getInstance();
 assertTrue("Two instances of the same format should be equal", dateFormat1.equals(dateFormat2));
 }

 @Test
 public void testNotEquals() {
 FastDateFormat dateFormat1 = FastDateFormat.getInstance("dd/MM/yyyy");
 FastDateFormat dateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy");
 assertFalse("Two instances of different formats should not be equal", dateFormat1.equals(dateFormat2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat dateFormat1 = FastDateFormat.getInstance();
 FastDateFormat dateFormat2 = FastDateFormat.getInstance();
 int hashCode1 = dateFormat1.hashCode();
 int hashCode2 = dateFormat2.hashCode();
 assertEquals("Two instances of the same format should have the same hash code", hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a");
 String toString = dateFormat.toString();
 assertTrue("ToString method should return String representation of the format", toString.contains("FastDateFormat[dd/MM/yyyy hh:mm a, null, null]"));
 }

 @Test
 public void testApplyRules() {
 Calendar calendar = new GregorianCalendar(2021, Calendar.DECEMBER, 25);
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd MMMM yyyy");
 StringBuffer buf = new StringBuffer(64);
 String dateString = dateFormat.applyRules(calendar, buf);
 assertEquals("Date string should match calendar values", "25 décembre 2021", dateString.toString());
 }
}