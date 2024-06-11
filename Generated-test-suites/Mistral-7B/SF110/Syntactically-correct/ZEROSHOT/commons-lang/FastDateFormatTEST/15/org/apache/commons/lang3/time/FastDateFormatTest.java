package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.*;

import org.apache.commons.lang3.time.*;
import org.apache.commons.lang3.time.format.*;

public class FastDateFormatTest {

 private FastDateFormat dateFormat;
 private FastDateFormat timeFormat;
 private FastDateFormat dateTimeFormat;

 @BeforeClass
 public static void setupClass() {
 FastDateFormat.cache.clear();
 }

 @Before
 public void setUp() {
 dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 }

 @Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy", new SimpleTimeZone(0, "UTC"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy", Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithTimeZoneAndLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy", new SimpleTimeZone(0, "UTC"), Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new SimpleTimeZone(0, "UTC"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZoneAndLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new SimpleTimeZone(0, "UTC"), Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new SimpleTimeZone(0, "UTC"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZoneAndLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new SimpleTimeZone(0, "UTC"), Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new SimpleTimeZone(0, "UTC"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new SimpleTimeZone(0, "UTC"), Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testFormatDate() {
 Date date = new Date();
 String formattedDate = dateFormat.format(date);
 assertNotNull(formattedDate);
 }

 @Test
 public void testFormatLong() {
 long millis = System.currentTimeMillis();
 String formattedDate = dateFormat.format(millis);
 assertNotNull(formattedDate);
 }

 @Test
 public void testFormatCalendar() {
 Calendar calendar = Calendar.getInstance();
 String formattedDate = dateFormat.format(calendar.getTime());
 assertNotNull(formattedDate);
 }

 @Test
 public void testParseDate() throws ParseException {
 String dateString = "15/06/2022";
 Date parsedDate = dateFormat.parse(dateString);
 assertNotNull(parsedDate);
 }

 @Test
 public void testParseLong() throws ParseException {
 String dateString = "15/06/2022";
 long millis = System.currentTimeMillis();
 Date date = new Date(millis);
 String formattedDate = dateFormat.format(date);
 String parsedDateString = dateFormat.parse(dateString).toString();
 assertEquals(formattedDate, parsedDateString);
 }

 @Test
 public void testParseCalendar() throws ParseException {
 String dateString = "15/06/2022";
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(dateFormat.parse(dateString));
 String formattedDate = dateFormat.format(calendar.getTime());
 String parsedDateString = dateFormat.parse(dateString, new ParsePosition(0)).toString();
 assertEquals(formattedDate, parsedDateString);
 }

 @Test
 public void testGetPattern() {
 String pattern = dateFormat.getPattern();
 assertNotNull(pattern);
 assertEquals("dd/MM/yyyy", pattern);
 }

 @Test
 public void testGetTimeZone() {
 TimeZone timeZone = dateFormat.getTimeZone();
 assertNotNull(timeZone);
 }

 @Test
 public void testGetLocale() {
 Locale locale = dateFormat.getLocale();
 assertNotNull(locale);
 }

 @Test
 public void testGetMaxLengthEstimate() {
 int maxLength = dateFormat.getMaxLengthEstimate();
 assertGreaterThan(maxLength, 0);
 }

 @Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 FastDateFormat fastDateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 FastDateFormat fastDateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 int hashCode1 = fastDateFormat1.hashCode();
 int hashCode2 = fastDateFormat2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 String toString = dateFormat.toString();
 assertNotNull(toString);
 }

 @Test
 public void testApplyRules() {
 Calendar calendar = Calendar.getInstance();
 StringBuffer stringBuffer = new StringBuffer();
 dateFormat.applyRules(calendar, stringBuffer);
 String formattedDate = stringBuffer.toString();
 assertNotNull(formattedDate);
 }

 @Test
 public void testFormatWithCustomPattern() {
 Date date = new Date();
 FastDateFormat customDateFormat = new FastDateFormat("HH:mm:ss MMM dd, yyyy", new SimpleTimeZone(0, "UTC"));
 String formattedDate = customDateFormat.format(date);
 assertNotNull(formattedDate);
 }

 @Test
 public void testGetInstanceWithCustomPatternTimeZoneAndLocale() {
 FastDateFormat customDateFormat = FastDateFormat.getInstance("HH:mm:ss MMM dd, yyyy", new SimpleTimeZone(0, "UTC"), Locale.US);
 String formattedDate = customDateFormat.format(new Date());
 assertNotNull(formattedDate);
 }
}