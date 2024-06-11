import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import static org.junit.Assert.*;

public class FastDateFormatTest {

 @Test
 public void testFormatDate() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 String expectedDateFormat = "Sep 13, 2022";
 String actualDateFormat = dateFormat.format(new Date());
 assertEquals(expectedDateFormat, actualDateFormat);
 }

 @Test
 public void testFormatCalendar() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 Calendar calendar = new GregorianCalendar(2022, 8, 13);
 String expectedDateFormat = "Saturday, September 13, 2022";
 String actualDateFormat = dateFormat.format(calendar);
 assertEquals(expectedDateFormat, actualDateFormat);
 }

 @Test
 public void testFormatLongMilliseconds() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
 long milliseconds = System.currentTimeMillis();
 String expectedDateFormat = "5:30:45 PM";
 String actualDateFormat = dateFormat.format(milliseconds);
 assertEquals(expectedDateFormat, actualDateFormat);
 }

 @Test
 public void testFormatShortMilliseconds() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 long milliseconds = System.currentTimeMillis();
 String expectedDateFormat = "5:30 PM";
 String actualDateFormat = dateFormat.format(milliseconds);
 assertEquals(expectedDateFormat, actualDateFormat);
 }

 @Test
 public void testParseDate() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 String dateString = "Sep 13, 2022";
 Date expectedDate = new Date();
 Date actualDate = dateFormat.parse(dateString);
 assertEquals(expectedDate, actualDate);
 }

 @Test
 public void testParseIncorrectFormat() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 String incorrectFormat = "Sep 13, 2023, 2022";
 try {
 dateFormat.parse(incorrectFormat);
 fail();
 } catch (ParseException e) {
 // Pass
 }
 }

 @Test
 public void testParseLongMilliseconds() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
 long milliseconds = System.currentTimeMillis() - 1000;
 Date expectedDate = new Date(milliseconds);
 Date actualDate = dateFormat.parse("4:29:59 AM");
 assertEquals(expectedDate, actualDate);
 }

 @Test
 public void testParseShortMilliseconds() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 long milliseconds = System.currentTimeMillis() - 1000;
 Date expectedDate = new Date(milliseconds);
 Date actualDate = dateFormat.parse("4:29 PM");
 assertEquals(expectedDate, actualDate);
 }

 @Test
 public void testGetPattern() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 String expectedPattern = "MMM dd, yyyy";
 String actualPattern = dateFormat.getPattern();
 assertEquals(expectedPattern, actualPattern);
 }

}