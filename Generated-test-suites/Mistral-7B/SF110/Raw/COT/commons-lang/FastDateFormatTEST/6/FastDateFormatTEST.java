import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

 private final FastDateFormat getInstance(String pattern, TimeZone timeZone, Locale locale) {
 return FastDateFormat.getInstance(pattern, timeZone, locale);
 }

 @Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstance_String() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a z");
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstance_String_TimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a z", TimeZone.getTimeZone("UTC"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstance_String_Locale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a z", Locale.FRENCH);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstance_String_TimeZone_Locale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstance_Int() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstance_Int_Locale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRENCH);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstance_Int_TimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstance_Int_TimeZone_Locale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstance_Int() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstance_Int_Locale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRENCH);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstance_Int_TimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstance_Int_TimeZone_Locale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstance_Int_Int() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstance_Int_Int_Locale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRENCH);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstance_Int_Int_TimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstance_Int_Int_TimeZone_Locale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testFormat_Object() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 Object object = new GregorianCalendar(2022, 11, 12, 13, 14, 15, 16).getTime();
 String expected = "12/11/2022 01:14 PM UTC";
 String actual = fastDateFormat.format(object);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormat_Long() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 long millis = 1668435496000L; // Thu Jan 05 21:51:36 UTC 2023
 String expected = "05/01/2023 09:51 PM UTC";
 String actual = fastDateFormat.format(millis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormat_Date() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 Date date = new GregorianCalendar(2022, 11, 12).getTime();
 String expected = "12/11/2022 00:00 AM UTC";
 String actual = fastDateFormat.format(date);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormat_Calendar() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 Calendar calendar = new GregorianCalendar(2022, 11, 12, 13, 14, 15);
 String expected = "12/11/2022 01:14 PM UTC";
 String actual = fastDateFormat.format(calendar);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormat_Long_StringBuffer() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 long millis = 1668435496000L; // Thu Jan 05 21:51:36 UTC 2023
 StringBuffer buf = new StringBuffer();
 String expected = "05/01/2023 09:51 PM UTC";
 String actual = fastDateFormat.format(millis, buf);
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testFormat_Date_StringBuffer() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 Date date = new GregorianCalendar(2022, 11, 12).getTime();
 StringBuffer buf = new StringBuffer();
 String expected = "12/11/2022 00:00 AM UTC";
 String actual = fastDateFormat.format(date, buf);
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testFormat_Calendar_StringBuffer() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 Calendar calendar = new GregorianCalendar(2022, 11, 12, 13, 14, 15);
 StringBuffer buf = new StringBuffer();
 String expected = "12/11/2022 01:14 PM UTC";
 String actual = fastDateFormat.format(calendar, buf);
 assertEquals(expected, actual.toString());
 }

 @Test(expected = ParseException.class)
 public void testParse_String() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 String source = "13/12/2022 01:14 PM UTC";
 fastDateFormat.parse(source);
 fail("Expected ParseException to be thrown");
 }

 @Test(expected = ParseException.class)
 public void testParse_String_ParsePosition() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 String source = "13/12/2022 01:14 PM UTC";
 ParsePosition pos = new ParsePosition(0);
 fastDateFormat.parse(source, pos);
 fail("Expected ParseException to be thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseObject_String_ParsePosition() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 String source = "13/12/2022 01:14 PM UTC";
 ParsePosition pos = new ParsePosition(0);
 fastDateFormat.parseObject(source, pos);
 fail("Expected ParseException to be thrown");
 }

 @Test(expected = ParseException.class)
 public void testParse_String_InvalidPattern() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 String source = "13/12/2022/13:14:15 PM UTC";
 fastDateFormat.parse(source);
 fail("Expected ParseException to be thrown");
 }

 @Test
 public void testParse_String_ValidPattern() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 String source = "12/11/2022 01:14 PM UTC";
 Date expected = new GregorianCalendar(2022, 11, 12, 13, 14, 15).getTime();
 Date actual = fastDateFormat.parse(source);
 assertEquals(expected, actual);
 }

 @Test
 public void testParse_String_ParsePosition_ValidPattern() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 String source = "12/11/2022 01:14 PM UTC";
 ParsePosition pos = new ParsePosition(0);
 Date actual = fastDateFormat.parse(source, pos);
 assertEquals(0, pos.getIndex());
 Date expected = new GregorianCalendar(2022, 11, 12, 13, 14, 15).getTime();
 assertEquals(expected, actual);
 }

 @Test
 public void testParseObject_String_ParsePosition_ValidPattern() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 String source = "12/11/2022 01:14 PM UTC";
 ParsePosition pos = new ParsePosition(0);
 Object actual = fastDateFormat.parseObject(source, pos);
 assertEquals(Date.class, actual.getClass());
 Date expected = new GregorianCalendar(2022, 11, 12, 13, 14, 15).getTime();
 assertEquals(expected, actual);
 }

 @Test(expected = ParseException.class)
 public void testParse_String_InvalidFormat() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 String source = "12/11/2022 30:60:00 PM UTC";
 fastDateFormat.parse(source);
 fail("Expected ParseException to be thrown");
 }

 @Test(expected = ParseException.class)
 public void testParse_String_InvalidFormat_ParsePosition() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 String source = "12/11/2022 30:60:00 PM UTC";
 ParsePosition pos = new ParsePosition(0);
 fastDateFormat.parse(source, pos);
 fail("Expected ParseException to be thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseObject_String_InvalidFormat_ParsePosition() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 String source = "12/11/2022 30:60:00 PM UTC";
 ParsePosition pos = new ParsePosition(0);
 fastDateFormat.parseObject(source, pos);
 fail("Expected ParseException to be thrown");
 }

 @Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 String expected = "dd/MM/yyyy hh:mm a z";
 String actual = fastDateFormat.getPattern();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 TimeZone expected = TimeZone.getTimeZone("UTC");
 TimeZone actual = fastDateFormat.getTimeZone();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 Locale expected = Locale.US;
 Locale actual = fastDateFormat.getLocale();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 int expected = 42;
 int actual = fastDateFormat.getMaxLengthEstimate();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals_Object() {
 FastDateFormat fastDateFormat1 = getInstance("dd/MM/yyyy hh:mm a z");
 FastDateFormat fastDateFormat2 = getInstance("dd/MM/yyyy hh:mm a z");
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testEquals_Object_False() {
 FastDateFormat fastDateFormat1 = getInstance("dd/MM/yyyy hh:mm a z");
 Object obj = new Object();
 assertFalse(fastDateFormat1.equals(obj));
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = getInstance("dd/MM/yyyy hh:mm a z");
 FastDateFormat fastDateFormat2 = getInstance("dd/MM/yyyy hh:mm a z");
 int expected = fastDateFormat1.hashCode();
 int actual = fastDateFormat2.hashCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 String expected = "FastDateFormat[dd/MM/yyyy hh:mm a z, Locale[US, US], Sun Jan 01 00:00:00 UTC 1970]";
 String actual = fastDateFormat.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testApplyRules_Calendar_StringBuffer() throws Exception {
 FastDateFormat fastDateFormat = getInstance("dd/MM/yyyy hh:mm a z");
 Calendar calendar = new GregorianCalendar(2022, 11, 12, 13, 14, 15);
 StringBuffer buf = new StringBuffer();
 String expected = "12/11/2022 01:14:15 PM";
 String actual = fastDateFormat.applyRules(calendar, buf).toString();
 assertEquals(expected, actual);
 }
}