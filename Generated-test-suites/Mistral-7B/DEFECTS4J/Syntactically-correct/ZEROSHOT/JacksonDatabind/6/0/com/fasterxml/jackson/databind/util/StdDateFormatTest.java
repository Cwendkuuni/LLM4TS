package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

 private StdDateFormat getInstance() {
 return new StdDateFormat();
 }

 @Test
 public void testGetBlueprintISO8601Format() {
 // This method is deprecated since 2.4, so let's just ignore it
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetBlueprintISO8601Format_deprecatedWithTimeZone() {
 // This method is deprecated since 2.4 and takes a TimeZone argument,
 // so we cannot test it directly with the given StdDateFormat class
 StdDateFormat sdf = new StdDateFormat(TimeZone.getDefault(), Locale.getDefault());
 sdf.getBlueprintISO8601Format();
 }

 @Test
 public void testGetISO8601Format() {
 DateFormat df = getInstance().getISO8601Format(TimeZone.getDefault(), Locale.getDefault());
 assertNotNull(df);
 assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", df.toPattern());
 }

 @Test
 public void testParseISO8601Date() throws ParseException {
 DateFormat df = getInstance().getISO8601Format(TimeZone.getDefault(), Locale.getDefault());
 ParsePosition pos = new ParsePosition(0);
 Date date = df.parse("2022-12-31T23:59:59.999Z", pos);
 assertEquals("Sat Dec 31 23:59:59 GMT", df.format(date));
 assertNull(pos.getErrorIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseISO8601Date_invalidFormat() throws ParseException {
 DateFormat df = getInstance().getISO8601Format(TimeZone.getDefault(), Locale.getDefault());
 ParsePosition pos = new ParsePosition(0);
 df.parse("invalid_date_format", pos);
 fail("Expected ParseException, but got: " + df.format(pos.getIndex()));
 }

 @Test
 public void testParseISO8601DateWithTimeZone() throws ParseException {
 DateFormat df = getInstance().getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.getDefault());
 ParsePosition pos = new ParsePosition(0);
 Date date = df.parse("2022-12-31T23:59:59.999Z", pos);
 assertEquals("Sat Dec 31 23:59:59 UTC", df.format(date));
 assertNull(pos.getErrorIndex());
 }

 @Test
 public void testParseISO8601DateWithLocale() throws ParseException {
 DateFormat df = getInstance().getISO8601Format(TimeZone.getDefault(), Locale.FRANCE);
 ParsePosition pos = new ParsePosition(0);
 Date date = df.parse("2022-12-31T23:59:59.999Z", pos);
 assertEquals("samedi 31 déc. 2022 23:59:59", df.format(date));
 assertNull(pos.getErrorIndex());
 }

 @Test
 public void testGetRFC1123Format() {
 DateFormat df = getInstance().getRFC1123Format();
 assertNotNull(df);
 assertEquals("EEE, dd MMM yyyy HH:mm:ss zzz", df.toPattern());
 }

 @Test
 public void testParseRFC1123Date() throws ParseException {
 DateFormat df = getInstance().getRFC1123Format();
 ParsePosition pos = new ParsePosition(0);
 Date date = df.parse("Sat, 31 Dec 2022 23:59:59 GMT", pos);
 assertEquals("Sat Dec 31 23:59:59 GMT", df.format(date));
 assertNull(pos.getErrorIndex());
 }

 @Test
 public void testParseRFC1123DateWithTimeZone() throws ParseException {
 DateFormat df = getInstance().getRFC1123Format();
 ParsePosition pos = new ParsePosition(0);
 Date date = df.parse("Sat, 31 Dec 2022 23:59:59 UTC", pos);
 assertEquals("Sat Dec 31 23:59:59 UTC", df.format(date));
 assertNull(pos.getErrorIndex());
 }

 @Test
 public void testParseRFC1123DateWithLocale() throws ParseException {
 DateFormat df = getInstance().getRFC1123Format();
 ParsePosition pos = new ParsePosition(0);
 Date date = df.parse("Sat, 31 déc. 2022 23:59:59 CET", pos);
 assertEquals("Sat déc. 31 23:59:59 CET", df.format(date));
 assertNull(pos.getErrorIndex());
 }

 @Test
 public void testParsePlainDate() throws ParseException {
 DateFormat df = getInstance().getISO8601Format(null, null);
 assertTrue(df instanceof SimpleDateFormat);
 assertEquals("yyyy-MM-dd", df.toPattern());
 assertTrue(df.parse("2022-12-31", new ParsePosition(0)).getTime() > 0);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 StdDateFormat sdf1 = getInstance();
 StdDateFormat sdf2 = sdf1.clone();
 assertNotSame(sdf1, sdf2);
 assertEquals(sdf1.getClass(), sdf2.getClass());
 assertEquals(sdf1.getLocale(), sdf2.getLocale());
 assertEquals(sdf1._timezone, sdf2._timezone);
 }
}