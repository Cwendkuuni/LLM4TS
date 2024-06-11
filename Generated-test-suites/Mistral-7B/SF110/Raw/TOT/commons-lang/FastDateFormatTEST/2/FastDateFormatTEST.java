import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Test;
import java.text.ParseException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import static org.junit.Assert.*;

public class FastDateFormatTest {

 @Test
 public void testDefaultFormat() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 String dateStr = "Mon Jan 23 13:24:56 EST 2012";
 Date date = dateFormat.parse(dateStr);
 assertEquals("Invalid date format", "Mon Jan 23 13:24:56 EST 2012", dateFormat.format(date));
 }

 @Test
 public void testCustomFormat() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss z");
 Date date = new Date();
 String dateStr = dateFormat.format(date);
 assertEquals("Invalid date format", dateStr, "yyyy-MM-dd HH:mm:ss z");
 }

 @Test(expected = ParseException.class)
 public void testInvalidFormat() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
 String dateStr = "31-13-2012 23:59:56 EST";
 dateFormat.parse(dateStr);
 }

 @Test
 public void testParseLong() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 long millis = 1356913325000L;
 Date date = dateFormat.parse(FormatUtils.formatLong(millis, "dd-MM-yyyy HH:mm:ss"));
 assertEquals("Invalid date format", "Thu Oct 18 17:22:05 CEST 2012", dateFormat.format(date));
 }

 @Test
 public void testParseDate() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 Date date = new Date();
 String dateStr = dateFormat.format(date);
 Date parsedDate = dateFormat.parse(dateStr);
 assertEquals("Dates do not match", date, parsedDate);
 }

 @Test
 public void testParseCalendar() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 Calendar calendar = Calendar.getInstance();
 String dateStr = dateFormat.format(calendar.getTime());
 Date parsedDate = dateFormat.parse(dateStr);
 assertEquals("Dates do not match", calendar.getTime(), parsedDate);
 }

 @Test
 public void testFormatLong() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 long millis = 1356913325000L;
 String dateStr = dateFormat.format(millis);
 assertEquals("Invalid date format", "Thu Oct 18 17:22:05 CEST 2012", dateStr);
 }

 @Test
 public void testFormatDate() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 Date date = new Date();
 String dateStr = dateFormat.format(date);
 assertEquals("Dates do not match", date.toString(), dateStr);
 }

 @Test
 public void testFormatCalendar() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 Calendar calendar = Calendar.getInstance();
 String dateStr = dateFormat.format(calendar);
 assertEquals("Invalid date format", calendar.toString(), dateStr);
 }

 @Test
 public void testParseInvalidLong() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 long millis = 1356913325000L;
 String invalidDateStr = "Invalid date";
 dateFormat.parse(FormatUtils.formatLong(millis, invalidDateStr));
 fail("Expected ParseException but no exception was thrown");
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat dateFormat1 = FastDateFormat.getInstance();
 FastDateFormat dateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
 assertNotSame("Instances must be different", dateFormat1, dateFormat2);
 }

 @Test
 public void testGetInstanceWithLocale() {
 FastDateFormat dateFormat1 = FastDateFormat.getInstance();
 FastDateFormat dateFormat2 = FastDateFormat.getInstance(Locale.US);
 assertNotSame("Instances must be different", dateFormat1, dateFormat2);
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 FastDateFormat dateFormat1 = FastDateFormat.getInstance();
 FastDateFormat dateFormat2 = FastDateFormat.getInstance(TimeZone.getTimeZone("UTC"));
 assertNotSame("Instances must be different", dateFormat1, dateFormat2);
 }

 @Test
 public void testGetInstanceWithAllParameters() {
 FastDateFormat dateFormat1 = FastDateFormat.getInstance();
 FastDateFormat dateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", Locale.US, TimeZone.getTimeZone("UTC"));
 assertNotSame("Instances must be different", dateFormat1, dateFormat2);
 }

 @Test
 public void testGetDateInstanceWithStyle() {
 FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotSame("Instances must be different", dateFormat1, dateFormat2);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US);
 FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
 assertNotSame("Instances must be different", dateFormat1, dateFormat2);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("UTC"));
 FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"));
 assertNotSame("Instances must be different", dateFormat1, dateFormat2);
 }

 @Test
 public void testGetDateInstanceWithAllParameters() {
 FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US, TimeZone.getTimeZone("UTC"));
 FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US, TimeZone.getTimeZone("UTC"));
 assertNotSame("Instances must be different", dateFormat1, dateFormat2);
 }

 @Test
 public void testGetTimeInstanceWithStyle() {
 FastDateFormat dateFormat1 = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
 FastDateFormat dateFormat2 = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertNotSame("Instances must be different", dateFormat1, dateFormat2);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat dateFormat1 = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.US);
 FastDateFormat dateFormat2 = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US);
 assertNotSame("Instances must be different", dateFormat1, dateFormat2);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat dateFormat1 = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("UTC"));
 FastDateFormat dateFormat2 = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"));
 assertNotSame("Instances must be different", dateFormat1, dateFormat2);
 }

 @Test
 public void testGetTimeInstanceWithAllParameters() {
 FastDateFormat dateFormat1 = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.US, TimeZone.getTimeZone("UTC"));
 FastDateFormat dateFormat2 = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US, TimeZone.getTimeZone("UTC"));
 assertNotSame("Instances must be different", dateFormat1, dateFormat2);
 }

 @Test
 public void testGetDateTimeInstanceWithAllParameters() {
 FastDateFormat dateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.US, TimeZone.getTimeZone("UTC"));
 FastDateFormat dateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.US, TimeZone.getTimeZone("UTC"));
 assertNotSame("Instances must be different", dateFormat1, dateFormat2);
 }

 @Test
 public void testApplyRules() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
 Calendar calendar = Calendar.getInstance();
 String dateStr = dateFormat.format(calendar);
 StringBuffer buf = new StringBuffer();
 dateFormat.applyRules(calendar, buf);
 assertEquals("Dates do not match", dateStr, buf.toString());
 }
}