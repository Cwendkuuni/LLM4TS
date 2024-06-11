import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat format = FastDateFormat.getInstance();
 // Check default format
 Date date = format.parse("2022-12-31");
 assertNotNull(date);
 System.out.println("Default format: " + format.format(date));

 // Test with specific pattern
 FastDateFormat formatWithPattern = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS");
 Date dateWithPattern = null;
 try {
 dateWithPattern = formatWithPattern.parse("31-12-2022 23:59:59.123");
 } catch (ParseException e) {
 e.printStackTrace();
 }
 assertNotNull(dateWithPattern);
 System.out.println("Pattern format: " + formatWithPattern.format(dateWithPattern));
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 FastDateFormat formatWithTimeZone = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS", timeZone);
 Date dateWithTimeZone = null;
 try {
 dateWithTimeZone = formatWithTimeZone.parse("31-12-2022 23:59:59.123");
 } catch (ParseException e) {
 e.printStackTrace();
 }
 assertNotNull(dateWithTimeZone);
 System.out.println("Pattern format with timezone: " + formatWithTimeZone.format(dateWithTimeZone));
 }

 @Test
 public void testGetInstanceWithLocale() {
 Locale locale = Locale.US;
 FastDateFormat formatWithLocale = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS", locale);
 Date dateWithLocale = null;
 try {
 dateWithLocale = formatWithLocale.parse("12-31-2022 23:59:59.123");
 } catch (ParseException e) {
 e.printStackTrace();
 }
 assertNotNull(dateWithLocale);
 System.out.println("Pattern format with locale: " + formatWithLocale.format(dateWithLocale));
 }

 @Test
 public void testGetInstanceWithAllParameters() {
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 FastDateFormat formatWithAllParameters = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS", locale, timeZone);
 Date dateWithAllParameters = null;
 try {
 dateWithAllParameters = formatWithAllParameters.parse("31-12-2022 23:59:59.123");
 } catch (ParseException e) {
 e.printStackTrace();
 }
 assertNotNull(dateWithAllParameters);
 System.out.println("Pattern format with timezone and locale: " + formatWithAllParameters.format(dateWithAllParameters));
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 Date date = dateFormat.parse("12-31-2022");
 assertNotNull(date);
 System.out.println("Medium date format: " + dateFormat.format(date));

 FastDateFormat dateFormatWithLocale = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
 Date dateWithLocale = dateFormatWithLocale.parse("12-31-2022");
 assertNotNull(dateWithLocale);
 System.out.println("Medium date format with locale: " + dateFormatWithLocale.format(dateWithLocale));
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 Date date = new Date();
 assertNotNull(date);
 System.out.println("Short time format: " + timeFormat.format(date));

 FastDateFormat timeFormatWithLocale = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US);
 Date dateWithLocale = timeFormatWithLocale.parse("11:59 PM");
 assertNotNull(dateWithLocale);
 System.out.println("Short time format with locale: " + timeFormatWithLocale.format(dateWithLocale));
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 Date date = dateTimeFormat.parse("12-31-2022 11:59 PM");
 assertNotNull(date);
 System.out.println("Medium date-time format: " + dateTimeFormat.format(date));

 FastDateFormat dateTimeFormatWithLocale = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.US);
 Date dateWithLocale = dateTimeFormatWithLocale.parse("12-31-2022 11:59:59 PM");
 assertNotNull(dateWithLocale);
 System.out.println("Medium date-time format with locale: " + dateTimeFormatWithLocale.format(dateWithLocale));
 }

 @Test
 public void testParseException() {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy-MM-dd");
 try {
 format.parse("31-12-2022-31-12");
 fail("Should have thrown a ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testFormat() {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS");
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 String formattedDate = format.format(calendar);
 assertNotNull(formattedDate);
 System.out.println("Formatted date: " + formattedDate);
 }
}