package org.apache.commons.lang3.time;

import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import org.junit.Test;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat format = FastDateFormat.getInstance();
 assertNotNull(format);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy");
 assertNotNull(format);
 }

 @Test
 public void testGetInstanceWithPatternTimeZone() {
 FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy", new SimpleDateFormat("hh:mm a z").getTimeZone());
 assertNotNull(format);
 }

 @Test
 public void testGetInstanceWithPatternLocale() {
 FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy", Locale.FRENCH);
 assertNotNull(format);
 }

 @Test
 public void testGetInstanceWithPatternTimeZoneLocale() {
 FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy", Locale.FRENCH, new SimpleDateFormat("hh:mm a z").getTimeZone());
 assertNotNull(format);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.FRENCH);
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, new SimpleDateFormat("hh:mm a z").getTimeZone());
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocaleTimeZone() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.FRENCH, new SimpleDateFormat("hh:mm a z").getTimeZone());
 assertNotNull(dateFormat);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertNotNull(timeFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRENCH);
 assertNotNull(timeFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new SimpleDateFormat("hh:mm a z").getTimeZone());
 assertNotNull(timeFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocaleTimeZone() {
 FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRENCH, new SimpleDateFormat("hh:mm a z").getTimeZone());
 assertNotNull(timeFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
 assertNotNull(dateTimeFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, Locale.FRENCH);
 assertNotNull(dateTimeFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, new SimpleDateFormat("hh:mm a z").getTimeZone());
 assertNotNull(dateTimeFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocaleTimeZone() {
 FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, Locale.FRENCH, new SimpleDateFormat("hh:mm a z").getTimeZone());
 assertNotNull(dateTimeFormat);
 }

 @Test
 public void testFormat() {
 Date date = new Date();
 FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 String formattedDate = format.format(date);
 assertNotNull(formattedDate);
 }

 @Test
 public void testFormatWithPattern() {
 Date date = new Date();
 FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a z");
 String formattedDate = format.format(date);
 assertNotNull(formattedDate);
 }

 @Test
 public void testFormatWithCalendar() {
 Calendar calendar = new GregorianCalendar(2022, 11, 11);
 FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 String formattedDate = format.format(calendar);
 assertNotNull(formattedDate);
 }

 @Test
 public void testParse() throws ParseException {
 String dateString = "11/11/2022";
 FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy");
 Date parsedDate = format.parse(dateString);
 assertNotNull(parsedDate);
 }

 @Test
 public void testParseWithPattern() throws ParseException {
 String dateString = "11/11/2022 hh:mm a z";
 FastDateFormat format = FastDateFormat.getInstance(dateString);
 Date parsedDate = format.parse(dateString);
 assertNotNull(parsedDate);
 }

 @Test
 public void testParseWithCalendar() throws ParseException {
 String dateString = "11/11/2022";
 FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy");
 Calendar calendar = new GregorianCalendar();
 format.parse(dateString, calendar);
 assertNotNull(calendar);
 }

 @Test
 public void testToString() {
 FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy hh:mm a z");
 String toString = format.toString();
 assertNotNull(toString);
 }
}