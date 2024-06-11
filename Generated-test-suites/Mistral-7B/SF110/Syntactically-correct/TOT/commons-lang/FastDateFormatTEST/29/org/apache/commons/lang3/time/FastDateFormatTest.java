import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(JUnit4.class)
public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat format = FastDateFormat.getInstance();
 assertNotNull(format);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 assertNotNull(format);
 }

 @Test
 public void testGetInstanceWithPatternAndTimeZone() {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(format);
 }

 @Test
 public void testGetInstanceWithPatternAndLocale() {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", Locale.FRENCH);
 assertNotNull(format);
 }

 @Test
 public void testGetInstanceWithPatternTimeZoneAndLocale() {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", Locale.FRENCH, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(format);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotNull(format);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRENCH);
 assertNotNull(format);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(format);
 }

 @Test
 public void testGetDateInstanceWithLocaleAndTimeZone() {
 FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRENCH, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(format);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
 assertNotNull(format);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.FRENCH);
 assertNotNull(format);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(format);
 }

 @Test
 public void testGetTimeInstanceWithLocaleAndTimeZone() {
 FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.FRENCH, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(format);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
 assertNotNull(format);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, Locale.FRENCH);
 assertNotNull(format);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(format);
 }

 @Test
 public void testGetDateTimeInstanceWithLocaleAndTimeZone() {
 FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, Locale.FRENCH, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(format);
 }

 @Test
 public void testFormat() throws ParseException {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-12-31 23:59:59"));
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 String result = format.format(calendar.getTime());
 assertEquals("31-12-2022 23:59:59.000 +0200", result);
 }

 @Test
 public void testFormatWithPattern() throws ParseException {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-12-31 23:59:59"));
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", "dd-MM-yyyy HH:mm:ss.SSS z");
 String result = format.format(calendar.getTime());
 assertEquals("31-12-2022 23:59:59.000 +0200", result);
 }

 @Test
 public void testFormatWithLocale() throws ParseException {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-12-31 23:59:59"));
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", Locale.FRENCH);
 String result = format.format(calendar.getTime());
 assertEquals("31-12-2022 à 23:59:59.000 +02:00", result);
 }

 @Test
 public void testFormatWithTimeZone() throws ParseException {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-12-31 23:59:59"));
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", "GMT+02:00");
 String result = format.format(calendar.getTime());
 assertEquals("31-12-2022 21:59:59.000 +0100", result);
 }

 @Test
 public void testFormatWithLocaleAndTimeZone() throws ParseException {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-12-31 23:59:59"));
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", Locale.FRENCH, "GMT+02:00");
 String result = format.format(calendar.getTime());
 assertEquals("31-12-2022 à 21:59:59.000 +01:00", result);
 }

 @Test
 public void testParse() throws ParseException {
 String dateString = "31-12-2022 23:59:59.000 +0200";
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 Date parsedDate = format.parse(dateString);
 assertEquals(new Date(System.currentTimeMillis() - 315516800000L), parsedDate);
 }

 @Test
 public void testParseWithPattern() throws ParseException {
 String dateString = "dd-MM-yyyy HH:mm:ss.SSS z";
 FastDateFormat format = FastDateFormat.getInstance(dateString, "dd-MM-yyyy HH:mm:ss.SSS z");
 Date parsedDate = format.parse(dateString);
 assertEquals(new Date(System.currentTimeMillis() - 315516800000L), parsedDate);
 }

 @Test
 public void testParseWithLocale() throws ParseException {
 String dateString = "31-12-2022 à 23:59:59.000 +02:00";
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", Locale.FRENCH);
 Date parsedDate = format.parse(dateString);
 assertEquals(new Date(System.currentTimeMillis() - 315516800000L), parsedDate);
 }

 @Test
 public void testParseWithTimeZone() throws ParseException {
 String dateString = "31-12-2022 21:59:59.000 +0100";
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", "GMT+02:00");
 Date parsedDate = format.parse(dateString);
 assertEquals(new Date(System.currentTimeMillis() - 3155228800000L), parsedDate);
 }

 @Test
 public void testParseWithLocaleTimeZone() throws ParseException {
 String dateString = "31-12-2022 à 21:59:59.000 +01:00";
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", Locale.FRENCH, "GMT+02:00");
 Date parsedDate = format.parse(dateString);
 assertEquals(new Date(System.currentTimeMillis() - 3155228800000L), parsedDate);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFormat() throws ParseException {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 format.parse("Invalid format");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFormatWithPattern() throws ParseException {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", "dd-MM-yyyy HH:mm:ss.SSS z");
 format.parse("Invalid format");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFormatWithLocale() throws ParseException {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", Locale.FRENCH);
 format.parse("Invalid format");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFormatWithTimeZone() throws ParseException {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", "GMT+02:00");
 format.parse("Invalid format");
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFormatWithLocaleTimeZone() throws ParseException {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", Locale.FRENCH, "GMT+02:00");
 format.parse("Invalid format");
 }

}