import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FastDateFormatCache;
import org.apache.commons.lang3.time.ParsePosition;
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
 FastDateFormat format = FastDateFormat.getInstance();
 assertEquals("Invalid pattern", null, format.getPattern());
 assertNull(format.getTimeZone());
 assertNull(format.getLocale());
 }

 @Test
 public void testGetInstanceWithDefaultLocaleAndTimeZone() {
 FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
 assertEquals("yyyy-MM-dd HH:mm:ss", format.getPattern());
 assertNull(format.getTimeZone());
 assertNull(format.getLocale());
 }

 @Test
 public void testGetInstanceWithLocale() {
 FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.FRENCH);
 assertEquals("yyyy-MM-dd HH:mm:ss", format.getPattern());
 assertEquals(Locale.FRENCH, format.getLocale());
 assertNull(format.getTimeZone());
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH, new GregorianCalendar(2010, 1, 1).getTimeZone());
 assertEquals("yyyy-MM-dd HH:mm:ss", format.getPattern());
 assertNull(format.getLocale());
 assertEquals(new GregorianCalendar(2010, 1, 1).getTimeZone(), format.getTimeZone());
 }

 @Test
 public void testGetInstanceWithAllArguments() {
 FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH, new GregorianCalendar(2010, 1, 1).getTimeZone());
 assertEquals("yyyy-MM-dd HH:mm:ss", format.getPattern());
 assertEquals(Locale.ENGLISH, format.getLocale());
 assertEquals(new GregorianCalendar(2010, 1, 1).getTimeZone(), format.getTimeZone());
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertEquals("Invalid pattern", null, format.getPattern());
 assertNull(format.getTimeZone());
 assertNull(format.getLocale());
 }

 @Test
 public void testGetDateInstanceWithDefaultLocale() {
 FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.DEFAULT);
 assertEquals("dd.MM.yyyy", format.getPattern());
 assertNull(format.getTimeZone());
 assertNull(format.getLocale());
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRENCH);
 assertEquals("dd/MM/yyyy", format.getPattern());
 assertEquals(Locale.FRENCH, format.getLocale());
 assertNull(format.getTimeZone());
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.ENGLISH, new GregorianCalendar(2010, 1, 1).getTimeZone());
 assertEquals("dd.MM.yyyy", format.getPattern());
 assertNull(format.getLocale());
 assertEquals(new GregorianCalendar(2010, 1, 1).getTimeZone(), format.getTimeZone());
 }

 @Test
 public void testGetDateInstanceWithAllArguments() {
 FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.ENGLISH, new GregorianCalendar(2010, 1, 1).getTimeZone());
 assertEquals("dd.MM.yyyy", format.getPattern());
 assertEquals(Locale.ENGLISH, format.getLocale());
 assertEquals(new GregorianCalendar(2010, 1, 1).getTimeZone(), format.getTimeZone());
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertEquals("Invalid pattern", null, format.getPattern());
 assertNull(format.getTimeZone());
 assertNull(format.getLocale());
 }

 @Test
 public void testGetTimeInstanceWithDefaultLocale() {
 FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.getDefault());
 assertEquals("h:mm a", format.getPattern());
 assertNull(format.getTimeZone());
 assertNull(format.getLocale());
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRENCH);
 assertEquals("h 'heures' mm 'minutes' a", format.getPattern());
 assertEquals(Locale.FRENCH, format.getLocale());
 assertNull(format.getTimeZone());
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.ENGLISH, new GregorianCalendar(2010, 1, 1).getTimeZone());
 assertEquals("h:mm a", format.getPattern());
 assertNull(format.getLocale());
 assertEquals(new GregorianCalendar(2010, 1, 1).getTimeZone(), format.getTimeZone());
 }

 @Test
 public void testGetTimeInstanceWithAllArguments() {
 FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.ENGLISH, new GregorianCalendar(2010, 1, 1).getTimeZone());
 assertEquals("h:mm a", format.getPattern());
 assertEquals(Locale.ENGLISH, format.getLocale());
 assertEquals(new GregorianCalendar(2010, 1, 1).getTimeZone(), format.getTimeZone());
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertEquals("Invalid pattern", null, format.getPattern());
 assertNull(format.getTimeZone());
 assertNull(format.getLocale());
 }

 @Test
 public void testGetDateTimeInstanceWithDefaultLocale() {
 FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.getDefault());
 assertEquals("dd.MM.yyyy h:mm a", format.getPattern());
 assertNull(format.getTimeZone());
 assertNull(format.getLocale());
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRENCH);
 assertEquals("dd/MM/yyyy h 'heures' mm 'minutes' a", format.getPattern());
 assertEquals(Locale.FRENCH, format.getLocale());
 assertNull(format.getTimeZone());
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.ENGLISH, new GregorianCalendar(2010, 1, 1).getTimeZone());
 assertEquals("dd.MM.yyyy h:mm a", format.getPattern());
 assertNull(format.getLocale());
 assertEquals(new GregorianCalendar(2010, 1, 1).getTimeZone(), format.getTimeZone());
 }

 @Test
 public void testGetDateTimeInstanceWithAllArguments() {
 FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.ENGLISH, new GregorianCalendar(2010, 1, 1).getTimeZone());
 assertEquals("dd.MM.yyyy h:mm a", format.getPattern());
 assertEquals(Locale.ENGLISH, format.getLocale());
 assertEquals(new GregorianCalendar(2010, 1, 1).getTimeZone(), format.getTimeZone());
 }

 @Test
 public void testFormatWithStringBuffer() throws ParseException {
 FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.ENGLISH);
 Calendar calendar = new GregorianCalendar(2010, 1, 5);
 String expected = "05.01.2010 12:30 PM";
 StringBuffer buffer = new StringBuffer();
 format.applyRules(calendar, buffer);
 assertEquals("Formatted date doesn't match the expected one", expected, buffer.toString());
 }

 @Test
 public void testFormatWithLong() throws ParseException {
 FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
 long timestamp = System.currentTimeMillis();
 String expected = FastDateFormat.getInstance().format(new Date(timestamp));
 assertEquals("Formatted date doesn't match the expected one", expected, format.format(timestamp));
 }

 @Test
 public void testFormatWithDate() throws ParseException {
 FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
 Date date = new Date();
 String expected = FastDateFormat.getInstance().format(date);
 assertEquals("Formatted date doesn't match the expected one", expected, format.format(date));
 }

 @Test
 public void testFormatWithCalendar() throws ParseException {
 FastDateFormat format = FastDateFormat.getInstance("dd.MM.yyyy h:mm a");
 Calendar calendar = new GregorianCalendar(2010, 1, 5);
 String expected = "05.01.2010 12:30 PM";
 StringBuffer buffer = new StringBuffer();
 format.applyRules(calendar, buffer);
 assertEquals("Formatted date doesn't match the expected one", expected, buffer.toString());
 }

 @Test
 public void testParse() throws ParseException {
 FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
 String dateString = "2010-01-05 12:30:45";
 Date date = format.parse(dateString);
 assertEquals("Parsed date doesn't match the expected one", new Date(1262304445000L), date);
 }

 @Test
 public void testParseWithParsePosition() throws ParseException {
 FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
 String dateString = "2010-01-05 12:30:45";
 ParsePosition position = new ParsePosition(0);
 Date date = format.parse(dateString, position);
 assertEquals("Parsed date doesn't match the expected one", new Date(1262304445000L), date);
 assertEquals("Incorrect position after parsing", 23, position.getIndex());
 assertEquals("Incorrect parsed length", 19, position.getErrorLength());
 }

 @Test
 public void testParseObject() throws ParseException {
 FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
 String dateString = "2010-01-05 12:30:45";
 Date date = (Date) format.parseObject(dateString);
 assertEquals("Parsed date doesn't match the expected one", new Date(1262304445000L), date);
 }
}