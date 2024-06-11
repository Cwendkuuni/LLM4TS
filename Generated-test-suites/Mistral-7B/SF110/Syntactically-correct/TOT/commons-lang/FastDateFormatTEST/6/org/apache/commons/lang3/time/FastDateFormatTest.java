import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Assert;
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
 assertEquals("Default format", "dd/MM/yyyy", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 assertEquals("Long date format", "dd MMM yyyy", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG, Locale.FRENCH);
 assertEquals("French long date format", "dd MMMM yyyy", dateFormat.getPattern());
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG, Locale.ENGLISH, new GregorianCalendar(2012, 1, 1).getTimeZone());
 assertEquals("Long date format with timezone", "dd MMM yyyy 'at' h:mm a z", dateFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM);
 assertEquals("Long date and medium time format", "dd MMM yyyy h:mm a", dateTimeFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM, Locale.FRENCH);
 assertEquals("French long date and medium time format", "dd MMMM yyyy h 'heures' m 'minutes' s 'secondes' zzzz", dateTimeFormat.getPattern());
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM, Locale.ENGLISH, new GregorianCalendar(2012, 1, 1).getTimeZone());
 assertEquals("Long date and medium time format with timezone", "dd MMM yyyy h:mm:ss a z", dateTimeFormat.getPattern());
 }

 @Test
 public void testParseDate() throws ParseException {
 Date date = FastDateFormat.parse("25 December 2012");
 assertEquals("Parsed date", new Date(1355849600000L), date);
 }

 @Test
 public void testParseDateWithLocale() throws ParseException {
 Date date = FastDateFormat.getDateInstance(FastDateFormat.LONG, Locale.FRENCH).parse("25 décembre 2012");
 assertEquals("Parsed date with locale", new Date(1355849600000L), date);
 }

 @Test
 public void testParseDateWithTimeZone() throws ParseException {
 Date date = FastDateFormat.getDateInstance(FastDateFormat.LONG, Locale.ENGLISH, new GregorianCalendar(2012, 1, 1).getTimeZone()).parse("01 January 2012 at 12:00 PM Eastern Standard Time");
 assertEquals("Parsed date with timezone", new Date(1325379200000L), date);
 }

 @Test
 public void testFormatDate() {
 Date date = new Date(1355849600000L);
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 String formattedDate = dateFormat.format(date);
 assertEquals("Formatted date", "25/12/2012", formattedDate);
 }

 @Test
 public void testFormatDateWithLocale() {
 Date date = new Date(1355849600000L);
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG, Locale.FRENCH);
 String formattedDate = dateFormat.format(date);
 assertEquals("Formatted date with locale", "25 décembre 2012", formattedDate);
 }

 @Test
 public void testFormatDateWithTimeZone() {
 Date date = new Date(1325379200000L);
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG, Locale.ENGLISH, new GregorianCalendar(2012, 1, 1).getTimeZone());
 String formattedDate = dateFormat.format(date);
 assertEquals("Formatted date with timezone", "01 January 2012 at 12:00 PM Eastern Standard Time", formattedDate);
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM, Locale.ENGLISH, new GregorianCalendar(2012, 1, 1).getTimeZone());
 int maxLength = dateFormat.getMaxLengthEstimate();
 assertEquals("Max length estimate", 33, maxLength);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidParse() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 assertThrows(IllegalArgumentException.class, () -> dateFormat.parse("Invalid date format"));
 }
}