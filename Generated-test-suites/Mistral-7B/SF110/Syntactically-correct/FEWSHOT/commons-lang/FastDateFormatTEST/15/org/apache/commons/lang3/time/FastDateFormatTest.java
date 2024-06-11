package org.apache.commons.lang3.time;

import static org.junit.Assert.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import org.junit.Test;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat dateFormat = FastDateFormat.getInstance();
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy", new java.util.SimpleTimeZone(0, "GMT"));
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy", Locale.FRENCH);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetInstanceWithAllParams() {
 FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy", new java.util.SimpleTimeZone(0, "GMT"), Locale.FRENCH);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRENCH);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new java.util.SimpleTimeZone(0, "GMT"));
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithAllParams() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new java.util.SimpleTimeZone(0, "GMT"), Locale.FRENCH);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRENCH);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new java.util.SimpleTimeZone(0, "GMT"));
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetTimeInstanceWithAllParams() {
 FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new java.util.SimpleTimeZone(0, "GMT"), Locale.FRENCH);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRENCH);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new java.util.SimpleTimeZone(0, "GMT"));
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithAllParams() {
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new java.util.SimpleTimeZone(0, "GMT"), Locale.FRENCH);
 assertNotNull(dateFormat);
 }

 @Test
 public void testFormatDate() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new java.util.SimpleTimeZone(0, "GMT"), Locale.FRENCH);
 Date date = new GregorianCalendar(2022, 11, 10).getTime();
 String formattedDate = dateFormat.format(date);
 assertEquals("10/11/2022", formattedDate);
 }

 @Test
 public void testFormatLongMilliseconds() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, new java.util.SimpleTimeZone(0, "GMT"), Locale.FRENCH);
 long milliseconds = 165580800000L;
 String formattedDate = dateFormat.format(milliseconds);
 assertEquals("12/12/2022", formattedDate);
 }

 @Test
 public void testFormatDateWithCustomFormat() throws ParseException {
 FastDateFormat dateFormat = new FastDateFormat("dd/MM/yyyy HH:mm:ss", new java.util.SimpleTimeZone(0, "GMT"), Locale.FRENCH);
 Date date = new GregorianCalendar(2022, 11, 10, 12, 30).getTime();
 String formattedDate = dateFormat.format(date);
 assertEquals("10/11/2022 12:30:00", formattedDate);
 }

 @Test
 public void testParseDate() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new java.util.SimpleTimeZone(0, "GMT"), Locale.FRENCH);
 String dateString = "10/11/2022";
 Date parsedDate = dateFormat.parse(dateString);
 assertEquals(new GregorianCalendar(2022, 10, 10).getTime(), parsedDate.getTime());
 }

 @Test
 public void testParseLongMilliseconds() throws ParseException {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, new java.util.SimpleTimeZone(0, "GMT"), Locale.FRENCH);
 long milliseconds = 165580800000L;
 Date parsedDate = dateFormat.parse(Long.toString(milliseconds));
 assertEquals(new GregorianCalendar(2022, 11, 12).getTime(), parsedDate.getTime());
 }

 @Test
 public void testParseDateWithCustomFormat() throws ParseException {
 FastDateFormat dateFormat = new FastDateFormat("dd/MM/yyyy HH:mm:ss", new java.util.SimpleTimeZone(0, "GMT"), Locale.FRENCH);
 String dateString = "10/11/2022 12:30:00";
 Date parsedDate = dateFormat.parse(dateString);
 assertEquals(new GregorianCalendar(2022, 10, 10, 12, 30).getTime(), parsedDate.getTime());
 }
}