import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FastDateFormatTest {

 @Test
 public void testFormatDate() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 Date date = new Date();
 String expected = FormatUtils.format(date, dateFormat.getPattern());
 String actual = dateFormat.format(date);
 assertNotNull(actual);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatCalendar() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 Calendar calendar = new GregorianCalendar(2022, 11, 30);
 String expected = FormatUtils.format(calendar.getTime(), dateFormat.getPattern());
 String actual = dateFormat.format(calendar);
 assertNotNull(actual);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatLong() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
 long millis = System.currentTimeMillis();
 String expected = FormatUtils.format(millis, timeFormat.getPattern());
 String actual = timeFormat.format(millis);
 assertNotNull(actual);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseDate() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 String dateString = "12/30/2022";
 Date expected = new Date();
 expected.setTime(Long.parseLong(FormatUtils.parseDate(dateString, dateFormat.getPattern()).getTime()));
 Date actual = dateFormat.parse(dateString);
 assertNotNull(actual);
 assertEquals(expected.getTime(), actual.getTime());
 }

 @Test
 public void testParseCalendar() throws ParseException {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 String dateTimeString = "12/30/2022 23:59:59";
 Calendar expected = new GregorianCalendar();
 expected.setTime(new SimpleDateFormat("MM/dd/yyyy h:mm:ss a").parse(dateTimeString));
 Calendar actual = dateTimeFormat.parseCalendar(dateTimeString);
 assertNotNull(actual);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLong() throws ParseException {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
 long millis = System.currentTimeMillis();
 Date expected = new Date(millis);
 Date actual = timeFormat.parse(FormatUtils.format(millis, timeFormat.getPattern()));
 assertNotNull(actual);
 assertEquals(expected, actual);
 }

 @Test(expected = ParseException.class)
 public void testInvalidFormat() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 String invalidDateString = "12/32/2022";
 dateFormat.parse(invalidDateString);
 }

 @Test(expected = ParseException.class)
 public void testInvalidFormatWithMillis() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 String invalidDateTimeString = "13/32/2022 23:59:60";
 dateFormat.parse(invalidDateTimeString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPattern() {
 FastDateFormat dateFormat = new FastDateFormat(null, null, null);
 }
}