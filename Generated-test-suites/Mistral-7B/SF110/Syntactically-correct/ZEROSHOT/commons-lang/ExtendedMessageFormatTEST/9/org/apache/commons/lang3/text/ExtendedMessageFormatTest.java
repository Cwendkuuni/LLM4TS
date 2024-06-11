package org.apache.commons.lang3.test;

import static org.junit.Assert.*;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;

public class ExtendedMessageFormatTest {

 private static final String TEST_PATTERN = "{0,'date','EEEE d, MMMM dd, ''yyyy'} {1,'number','#,##0.##'} {2,'symbol','%'}";
 private static final Map<String, FormatFactory> FORMAT_FACTORY_MAP = new HashMap<>();

 static {
 FORMAT_FACTORY_MAP.put("date", new DateFormatFactory());
 FORMAT_FACTORY_MAP.put("number", new NumberFormatFactory());
 FORMAT_FACTORY_MAP.put("symbol", new SymbolFormatFactory());
 }

 @Test
 public void testExtendedMessageFormatConstruction() {
 // Test default constructor
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("");
 assertEquals("", messageFormat.toPattern());
 assertNull(messageFormat.getRegistry());

 // Test constructor with pattern
 messageFormat = new ExtendedMessageFormat(TEST_PATTERN);
 assertEquals(TEST_PATTERN, messageFormat.toPattern());
 assertNotNull(messageFormat.getRegistry());
 }

 @Test
 public void testExtendedMessageFormatWithRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(TEST_PATTERN, FORMAT_FACTORY_MAP);
 assertEquals(TEST_PATTERN, messageFormat.toPattern());
 assertNotNull(messageFormat.getRegistry());
 }

 @Test
 public void testApplyPattern() {
 String pattern = TEST_PATTERN;
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault());
 messageFormat.applyPattern(pattern);
 assertEquals(TEST_PATTERN, messageFormat.toPattern());

 messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), FORMAT_FACTORY_MAP);
 messageFormat.applyPattern(pattern);
 assertEquals(TEST_PATTERN, messageFormat.toPattern());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(TEST_PATTERN, Locale.getDefault(), FORMAT_FACTORY_MAP);
 messageFormat.setFormat(0, new DateFormat());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndex() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(TEST_PATTERN, Locale.getDefault(), FORMAT_FACTORY_MAP);
 messageFormat.setFormatByArgumentIndex(0, new DateFormat());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(TEST_PATTERN, Locale.getDefault(), FORMAT_FACTORY_MAP);
 Format[] formats = new Format[3];
 formats[0] = new DateFormat();
 messageFormat.setFormats(formats);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndex() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(TEST_PATTERN, Locale.getDefault(), FORMAT_FACTORY_MAP);
 Format[] formats = new Format[3];
 formats[0] = new DateFormat();
 messageFormat.setFormatsByArgumentIndex(formats);
 }

 @Test
 public void testEquals() {
 ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat(TEST_PATTERN, Locale.getDefault(), FORMAT_FACTORY_MAP);
 ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(TEST_PATTERN, Locale.getDefault(), FORMAT_FACTORY_MAP);
 assertTrue(messageFormat1.equals(messageFormat2));

 messageFormat2 = new ExtendedMessageFormat(TEST_PATTERN, Locale.ENGLISH, FORMAT_FACTORY_MAP);
 assertFalse(messageFormat1.equals(messageFormat2));

 messageFormat1 = new ExtendedMessageFormat("", Locale.getDefault(), FORMAT_FACTORY_MAP);
 assertFalse(messageFormat1.equals(messageFormat2));
 }

 @Test
 public void testHashCode() {
 ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat(TEST_PATTERN, Locale.getDefault(), FORMAT_FACTORY_MAP);
 ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(TEST_PATTERN, Locale.getDefault(), FORMAT_FACTORY_MAP);
 assertEquals(messageFormat1.hashCode(), messageFormat2.hashCode());

 messageFormat2 = new ExtendedMessageFormat(TEST_PATTERN, Locale.ENGLISH, FORMAT_FACTORY_MAP);
 assertNotEquals(messageFormat1.hashCode(), messageFormat2.hashCode());

 messageFormat1 = new ExtendedMessageFormat("", Locale.getDefault(), FORMAT_FACTORY_MAP);
 assertNotEquals(messageFormat1.hashCode(), messageFormat2.hashCode());
 }
}