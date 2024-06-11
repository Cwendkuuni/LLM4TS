import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotNull(dateFormat);
 }

 @Test
 public void testParseDateWithValidPattern() throws ParseException {
 String dateString = "2022-03-01";
 FastDateFormat dateFormat = FastDateFormat.getDateInstance("yyyy-MM-dd");
 Date parsedDate = dateFormat.parse(dateString);
 assertEquals("2022-03-01", dateFormat.format(parsedDate));
 }

 @Test
 public void testParseDateWithInvalidPattern() throws ParseException {
 String dateString = "2022-03-01-02";
 FastDateFormat dateFormat = FastDateFormat.getDateInstance("yyyy-MM-dd");
 assertThrows(ParseException.class, () -> dateFormat.parse(dateString));
 }

 @Test
 public void testFormatDateWithValidPattern() {
 Date date = new Date();
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 String formattedDate = dateFormat.format(date);
 assertNotNull(formattedDate);
 }

 @Test
 public void testFormatDateWithInvalidPattern() {
 Date date = new Date();
 FastDateFormat dateFormat = FastDateFormat.getDateInstance("EEE h:mm a");
 String formattedDate = dateFormat.format(date);
 assertNotNull(formattedDate);
 }

 @Test
 public void testParseCalendar() throws ParseException {
 Calendar calendar = new GregorianCalendar(2022, 2, 1);
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 Date parsedDate = dateFormat.parse(dateFormat.format(calendar.getTimeInMillis()));
 assertEquals(calendar.getTimeInMillis(), parsedDate.getTime());
 }

 @Test
 public void testParseDateWithLocaleAndTimeZone() throws ParseException {
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 Locale locale = new Locale("fr", "FR");
 FastDateFormat dateFormat = FastDateFormat.getDateInstance("dd/MM/yyyy", locale, timeZone);
 String dateString = "01/03/2022";
 Date parsedDate = dateFormat.parse(dateString);
 assertEquals("2022-03-01", dateFormat.format(parsedDate));
 }

 @Test
 public void testFormatDateWithLocaleAndTimeZone() {
 Calendar calendar = new GregorianCalendar(2022, 2, 1, 12, 30, 0, Calendar.MILLIS_IN_DAY);
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.US, TimeZone.getTimeZone("UTC"));
 String formattedDate = dateFormat.format(calendar.getTimeInMillis());
 assertNotNull(formattedDate);
 }

 @Test
 public void testGetPattern() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 assertEquals("dd MMMM yyyy hh:mm a zzzz", dateFormat.getPattern());
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 assertEquals(FormatUtils.getDefaultDateFormat(Locale.US).getTimeZone(), dateFormat.getTimeZone());
 }

 @Test
 public void testGetLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 assertEquals(Locale.US, dateFormat.getLocale());
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 assertEquals(52, dateFormat.getMaxLengthEstimate());
 }

 @Test
 public void testEquals() {
 FastDateFormat dateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 FastDateFormat dateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 assertTrue(dateFormat1.equals(dateFormat2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat dateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 FastDateFormat dateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 assertEquals(dateFormat1.hashCode(), dateFormat2.hashCode());
 }
}