package org.apache.commons.lang3.test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;

public class ExtendedMessageFormatTest {

 private static final FormatFactory DUMMY_FORMAT_FACTORY = new FormatFactory() {
 @Override
 public Format getFormat(String name, String argument, Locale locale) {
 throw new UnsupportedOperationException();
 }
 };

 @org.junit.Test
 public void testDefaultConstruction() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("");
 assertNull(format.toPattern());
 assertSame(Locale.getDefault(), format.getLocale());
 assertNull(format.getRegistry());
 }

 @org.junit.Test
 public void testConstructionWithPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}");
 assertEquals("{0},{1}", format.toPattern());
 assertSame(Locale.getDefault(), format.getLocale());
 assertNull(format.getRegistry());
 }

 @org.junit.Test
 public void testConstructionWithPatternAndLocale() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}", Locale.US);
 assertEquals("{0},{1}", format.toPattern());
 assertSame(Locale.US, format.getLocale());
 assertNull(format.getRegistry());
 }

 @org.junit.Test
 public void testConstructionWithPatternAndRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}", new HashMap<String, FormatFactory>());
 assertEquals("{0},{1}", format.toPattern());
 assertSame(Locale.getDefault(), format.getLocale());
 assertNotNull(format.getRegistry());
 }

 @org.junit.Test
 public void testConstructionWithPatternLocaleAndRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}", Locale.US, new HashMap<String, FormatFactory>());
 assertEquals("{0},{1}", format.toPattern());
 assertSame(Locale.US, format.getLocale());
 assertNotNull(format.getRegistry());
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testInvalidPattern() {
 new ExtendedMessageFormat("{0,1}");
 }

 @org.junit.Test
 public void testSetPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}");
 format.applyPattern("{2},{3}");
 assertEquals("{2},{3}", format.toPattern());
 }

 @org.junit.Test
 public void testAppendFormatToPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0}") {
 @Override
 public void setFormatByArgumentIndex(int index, Format newFormat) {
 super.setFormatByArgumentIndex(index, newFormat);
 }
 };
 format.applyPattern("{1},{0}");
 assertEquals("{1},{0}", format.toPattern());
 format.setFormatByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd"));
 assertEquals("{1},{dd MM yyyy}", format.toPattern());
 }

 @org.junit.Test
 public void testApplyFormatWithNoRegistry() throws ParseException {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}") {
 @Override
 public void setFormatByArgumentIndex(int index, Format newFormat) {
 super.setFormatByArgumentIndex(index, newFormat);
 }
 };
 format.setFormatByArgumentIndex(1, new SimpleDateFormat("yyyy-MM-dd"));
 String formatString = "{0,date,Short}{1,date,Short}";
 assertEquals(formatString, format.toPattern());
 Object[] args = new Object[] {"2022-01-01", "2022-02-01"};
 Object result = format.format(args);
 assertEquals("01-Jan-22{0,date,Short}-01-Feb-22", result.toString());
 }

 @org.junit.Test
 public void testApplyFormatWithRegistry() throws ParseException {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}") {
 @Override
 public void setFormatByArgumentIndex(int index, Format newFormat) {
 super.setFormatByArgumentIndex(index, newFormat);
 }
 };
 FormatFactory registry = new HashMap<String, FormatFactory>() {
 {
 put("date", new DateFormatFactory());
 }
 };
 format.setRegistry(registry);
 format.applyPattern("{0,date,Short}{1,date,Short}");
 assertEquals("{0,date,Short}{1,date,Short}", format.toPattern());
 Object[] args = new Object[] {"2022-01-01", "2022-02-01"};
 Object result = format.format(args);
 assertEquals("01-Jan-22{1,date,Short}", result.toString());
 }

 @org.junit.Test
 public void testGetFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0,date,Short}{1,date,Short}");
 Format getFormat = format.getFormat(1, "date");
 assertNotNull(getFormat);
 assertEquals("date", getFormat.getClass().getName());
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSetFormatWithRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}") {
 @Override
 public void setFormatByArgumentIndex(int index, Format newFormat) {
 super.setFormatByArgumentIndex(index, newFormat);
 }
 };
 format.setRegistry(new HashMap<String, FormatFactory>());
 try {
 format.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
 fail();
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsWithRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}") {
 @Override
 public void setFormats(Format[] formats) {
 super.setFormats(formats);
 }
 };
 format.setRegistry(new HashMap<String, FormatFactory>());
 try {
 format.setFormats(new Format[0]);
 fail();
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @org.junit.Test
 public void testEqualsAndHashCode() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0},{1}") {
 @Override
 public void setFormatByArgumentIndex(int index, Format newFormat) {
 super.setFormatByArgumentIndex(index, newFormat);
 }
 };
 ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0},{1}") {
 @Override
 public void setFormatByArgumentIndex(int index, Format newFormat) {
 super.setFormatByArgumentIndex(index, newFormat);
 }
 };
 ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0},{2}") {
 @Override
 public void setFormatByArgumentIndex(int index, Format newFormat) {
 super.setFormatByArgumentIndex(index, newFormat);
 }
 };
 assertTrue(format1.equals(format1));
 assertTrue(format1.equals(format2));
 assertTrue(format1.hashCode() == format2.hashCode());
 assertFalse(format1.equals(format3));
 }

}