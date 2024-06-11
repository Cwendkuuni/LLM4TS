import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import static org.junit.Assert.*;

public class FastDateFormatTest {

 private FastDateFormat getInstance(String pattern) {
 return FastDateFormat.getInstance(pattern);
 }

 @Test
 public void testGetInstance() {
 // Test default constructor
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertNotNull(dateFormat);

 // Test with pattern
 FastDateFormat dateFormatWithPattern = FastDateFormat.getInstance("dd-MM-yyyy");
 assertNotNull(dateFormatWithPattern);
 assertEquals("dd-MM-yyyy", dateFormatWithPattern.getPattern());
 }

 @Test
 public void testGetDateInstance() {
 // Test default constructor
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotNull(dateFormat);

 // Test with Locale
 FastDateFormat localeDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
 assertNotNull(localeDateFormat);
 assertEquals(Locale.US, localeDateFormat.getLocale());
 }

 @Test
 public void testFormat() throws Exception {
 // Test format method with valid Date
 Date date = new SimpleDateFormat("dd-MM-yyyy").parse("25-12-2022");
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
 String result = dateFormat.format(date);
 assertEquals("25-12-2022", result);

 // Test format method with valid Calendar
 Calendar calendar = new GregorianCalendar(2022, Calendar.DECEMBER, 25);
 FastDateFormat calendarFormat = FastDateFormat.getInstance("dd-MM-yyyy");
 result = calendarFormat.format(calendar);
 assertEquals("25-12-2022", result);
 }

 @Test
 public void testParse() throws ParseException {
 // Test parse method with valid String
 String dateString = "25-12-2022";
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
 Date result = dateFormat.parse(dateString);
 assertEquals(new Date(2022, Calendar.DECEMBER, 25), result);

 // Test parse method with valid Calendar
 Calendar calendar = new GregorianCalendar(2022, Calendar.DECEMBER, 25);
 FastDateFormat calendarFormat = FastDateFormat.getInstance("dd-MM-yyyy");
 result = calendarFormat.parse(calendar);
 assertEquals(new Date(calendar.getTimeInMillis()), result);
 }

 @Test
 public void testGetPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
 assertEquals("dd-MM-yyyy", dateFormat.getPattern());
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertEquals("UTC", dateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertEquals(Locale.getDefault(), dateFormat.getLocale());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInstanceWithInvalidPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyy"); // Missing 'h' in the pattern
 assertNull(dateFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInstanceWithNullPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance(null);
 assertNull(dateFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDateTimeInstanceWithInvalidDateStyle() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(-1, FastDateFormat.SHORT);
 assertNull(dateFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDateTimeInstanceWithInvalidTimeStyle() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, -1);
 assertNull(dateFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDateTimeInstanceWithNullLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
 assertNull(dateFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDateTimeInstanceWithNullTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null);
 assertNull(dateFormat);
 }
}