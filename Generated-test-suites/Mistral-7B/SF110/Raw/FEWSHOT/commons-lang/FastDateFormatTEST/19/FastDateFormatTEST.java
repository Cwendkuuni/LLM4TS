package org.apache.commons.lang3.time;

import static org.junit.Assert.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.junit.Test;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat format = FastDateFormat.getInstance();
 assertNull(format.getPattern());
 assertNull(format.getTimeZone());
 assertNull(format.getLocale());
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
 assertEquals("dd-MM-yyyy HH:mm:ss z", format.getPattern());
 assertNull(format.getTimeZone());
 assertNull(format.getLocale());
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", new TimeZone("UTC"));
 assertEquals("dd-MM-yyyy HH:mm:ss z", format.getPattern());
 assertEquals("UTC", format.getTimeZone().getID());
 assertNull(format.getLocale());
 }

 @Test
 public void testGetInstanceWithLocale() {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", new Locale("fr", "FR"));
 assertEquals("dd-MM-yyyy HH:mm:ss z", format.getPattern());
 assertNull(format.getTimeZone());
 assertEquals("fr_FR", format.getLocale().getDisplayName());
 }

 @Test
 public void testGetInstanceWithAllParameters() {
 FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z", new Locale("fr", "FR"), new TimeZone("UTC"));
 assertEquals("dd-MM-yyyy HH:mm:ss z", format.getPattern());
 assertEquals("UTC", format.getTimeZone().getID());
 assertEquals("fr_FR", format.getLocale().getDisplayName());
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
 assertEquals("dd.MM.yy", dateFormat.getPattern());
 assertNull(dateFormat.getTimeZone());
 assertNull(dateFormat.getLocale());
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, new Locale("fr", "FR"));
 assertEquals("dd.MM.yy", dateFormat.getPattern());
 assertNull(dateFormat.getTimeZone());
 assertEquals("fr_FR", dateFormat.getLocale().getDisplayName());
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, new TimeZone("UTC"));
 assertEquals("dd.MM.yy", dateFormat.getPattern());
 assertEquals("UTC", dateFormat.getTimeZone().getID());
 assertNull(dateFormat.getLocale());
 }

 @Test
 public void testGetDateInstanceWithAllParameters() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, new Locale("fr", "FR"), new TimeZone("UTC"));
 assertEquals("dd.MM.yy", dateFormat.getPattern());
 assertEquals("UTC", dateFormat.getTimeZone().getID());
 assertEquals("fr_FR", dateFormat.getLocale().getDisplayName());
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertEquals("HH:mm:ss z", timeFormat.getPattern());
 assertNull(timeFormat.getTimeZone());
 assertNull(timeFormat.getLocale());
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new Locale("fr", "FR"));
 assertEquals("HH:mm:ss z", timeFormat.getPattern());
 assertNull(timeFormat.getTimeZone());
 assertEquals("fr_FR", timeFormat.getLocale().getDisplayName());
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new TimeZone("UTC"));
 assertEquals("HH:mm:ss z", timeFormat.getPattern());
 assertEquals("UTC", timeFormat.getTimeZone().getID());
 assertNull(timeFormat.getLocale());
 }

 @Test
 public void testGetTimeInstanceWithAllParameters() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new Locale("fr", "FR"), new TimeZone("UTC"));
 assertEquals("HH:mm:ss z", timeFormat.getPattern());
 assertEquals("UTC", timeFormat.getTimeZone().getID());
 assertEquals("fr_FR", timeFormat.getLocale().getDisplayName());
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertEquals("d MMM yyyy HH:mm:ss z", dateTimeFormat.getPattern());
 assertNull(dateTimeFormat.getTimeZone());
 assertNull(dateTimeFormat.getLocale());
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new Locale("fr", "FR"));
 assertEquals("d MMM yyyy HH:mm:ss z", dateTimeFormat.getPattern());
 assertNull(dateTimeFormat.getTimeZone());
 assertEquals("fr_FR", dateTimeFormat.getLocale().getDisplayName());
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new TimeZone("UTC"));
 assertEquals("d MMM yyyy HH:mm:ss z", dateTimeFormat.getPattern());
 assertEquals("UTC", dateTimeFormat.getTimeZone().getID());
 assertNull(dateTimeFormat.getLocale());
 }

 @Test
 public void testGetDateTimeInstanceWithAllParameters() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new Locale("fr", "FR"), new TimeZone("UTC"));
 assertEquals("d MMM yyyy HH:mm:ss z", dateTimeFormat.getPattern());
 assertEquals("UTC", dateTimeFormat.getTimeZone().getID());
 assertEquals("fr_FR", dateTimeFormat.getLocale().getDisplayName());
 }

 @Test
 public void testParseDate() {
 String dateString = "20-12-2010";
 Calendar calendar = new GregorianCalendar();
 Date parsedDate = FastDateFormat.getDateInstance(FastDateFormat.SHORT).parse(dateString);
 assertEquals(new Date(1257676800000L), parsedDate);
 assertEquals(20, calendar.get(Calendar.DAY_OF_MONTH));
 assertEquals(12, calendar.get(Calendar.MONTH));
 assertEquals(2010, calendar.get(Calendar.YEAR));
 }

 @Test
 public void testParseDateWithLocale() {
 String dateString = "20-12-2010";
 Locale locale = new Locale("fr", "FR");
 Calendar calendar = new GregorianCalendar(new TimeZone("Europe/Paris"), locale);
 Date parsedDate = FastDateFormat.getDateInstance(FastDateFormat.SHORT, locale).parse(dateString);
 assertEquals(new Date(1257676800000L), parsedDate);
 assertEquals(20, calendar.get(Calendar.DAY_OF_MONTH));
 assertEquals(11, calendar.get(Calendar.MONTH)); // Note: Months are zero-indexed but in France, December is 11
 assertEquals(2010, calendar.get(Calendar.YEAR));
 }

 @Test
 public void testParseDateWithTimeZone() {
 String dateString = "20-12-2010 19:45:00 EST";
 TimeZone timeZone = TimeZone.getTimeZone("EST");
 Calendar calendar = new GregorianCalendar(timeZone);
 Date parsedDate = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, timeZone).parse(dateString);
 assertEquals(new Date(1265650400000L), parsedDate);
 assertEquals(20, calendar.get(Calendar.DAY_OF_MONTH));
 assertEquals(11, calendar.get(Calendar.MONTH));
 assertEquals(2010, calendar.get(Calendar.YEAR));
 assertEquals(19, calendar.get(Calendar.HOUR_OF_DAY));
 assertEquals(45, calendar.get(Calendar.MINUTE));
 }

 @Test
 public void testParseDateWithAllParameters() {
 String dateString = "20-12-2010 19:45:00 EST";
 TimeZone timeZone = TimeZone.getTimeZone("EST");
 Locale locale = new Locale("fr", "FR");
 Calendar calendar = new GregorianCalendar(timeZone, locale);
 Date parsedDate = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, locale, timeZone).parse(dateString);
 assertEquals(new Date(1265650400000L), parsedDate);
 assertEquals(20, calendar.get(Calendar.DAY_OF_MONTH));
 assertEquals(11, calendar.get(Calendar.MONTH));
 assertEquals(2010, calendar.get(Calendar.YEAR));
 assertEquals(19, calendar.get(Calendar.HOUR_OF_DAY));
 assertEquals(45, calendar.get(Calendar.MINUTE));
 }

 @Test(expected = ParseException.class)
 public void testInvalidParseDate() {
 String dateString = "20-12-2010 25:60:00 EST";
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT);
 Date parsedDate = dateFormat.parse(dateString);
 fail("Expected ParseException but didn't get one");
 }

 @Test(expected = ParseException.class)
 public void testInvalidParseDateWithLocale() {
 String dateString = "20-12-2010 25:60:00 EST";
 Locale locale = new Locale("fr", "FR");
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, locale);
 Date parsedDate = dateFormat.parse(dateString);
 fail("Expected ParseException but didn't get one");
 }

 @Test(expected = ParseException.class)
 public void testInvalidParseDateWithTimeZone() {
 String dateString = "20-12-2010 25:60:00 EST";
 TimeZone timeZone = TimeZone.getTimeZone("EST");
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, timeZone);
 Date parsedDate = dateFormat.parse(dateString);
 fail("Expected ParseException but didn't get one");
 }

 @Test(expected = ParseException.class)
 public void testInvalidParseDateWithAllParameters() {
 String dateString = "20-12-2010 25:60:00 EST";
 Locale locale = new Locale("fr", "FR");
 TimeZone timeZone = TimeZone.getTimeZone("EST");
 FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, locale, timeZone);
 Date parsedDate = dateFormat.parse(dateString);
 fail("Expected ParseException but didn't get one");
 }
}