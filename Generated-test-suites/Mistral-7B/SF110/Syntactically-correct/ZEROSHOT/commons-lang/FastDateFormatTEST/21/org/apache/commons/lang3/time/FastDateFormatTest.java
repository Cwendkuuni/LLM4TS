import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FastDateFormatTest {

 private FastDateFormat fullFormat;
 private FastDateFormat mediumFormat;
 private FastDateFormat shortFormat;

 @Before
 public void setup() {
 fullFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 mediumFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
 shortFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT);
 }

 @Test
 public void testFormatDate() {
 Date date = new Date();

 // Test full format
 String fullFormattedDate = fullFormat.format(date);
 System.out.println("Full formatted date: " + fullFormattedDate);

 // Test medium format
 String mediumFormattedDate = mediumFormat.format(date);
 System.out.println("Medium formatted date: " + mediumFormattedDate);

 // Test short format
 String shortFormattedDate = shortFormat.format(date);
 System.out.println("Short formatted date: " + shortFormattedDate);
 }

 @Test
 public void testFormatCalendar() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());

 // Test full format
 String fullFormattedCalendar = fullFormat.format(calendar);
 System.out.println("Full formatted calendar: " + fullFormattedCalendar);

 // Test medium format
 String mediumFormattedCalendar = mediumFormat.format(calendar);
 System.out.println("Medium formatted calendar: " + mediumFormattedCalendar);

 // Test short format
 String shortFormattedCalendar = shortFormat.format(calendar);
 System.out.println("Short formatted calendar: " + shortFormattedCalendar);
 }

 @Test
 public void testFormatMilliseconds() {
 long millis = System.currentTimeMillis();

 // Test full format
 String fullFormattedMilliseconds = fullFormat.format(millis);
 System.out.println("Full formatted milliseconds: " + fullFormattedMilliseconds);

 // Test medium format
 String mediumFormattedMilliseconds = mediumFormat.format(millis);
 System.out.println("Medium formatted milliseconds: " + mediumFormattedMilliseconds);

 // Test short format
 String shortFormattedMilliseconds = shortFormat.format(millis);
 System.out.println("Short formatted milliseconds: " + shortFormattedMilliseconds);
 }

 @Test
 public void testParseDate() throws ParseException {
 String dateString = "Wed Jan 25 15:30:23 CET 2023";
 Date parsedDate = fullFormat.parse(dateString);
 System.out.println("Parsed date: " + FormatUtils.format(parsedDate, "dd MMM yyyy HH:mm:ss z"));
 }

 @Test(expected = ParseException.class)
 public void testParseDateInvalidFormat() throws ParseException {
 String invalidDateString = "XXX-XX-XX XX:XX:XX zzz 2023";
 fullFormat.parse(invalidDateString);
 }
}