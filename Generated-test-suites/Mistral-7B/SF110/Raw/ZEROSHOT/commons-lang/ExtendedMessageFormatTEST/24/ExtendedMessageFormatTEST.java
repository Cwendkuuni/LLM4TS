package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

 private static final String DEFAULT_PATTERN = "{0}, {1}{2}";
 private static final String REGISTRY_PATTERN = "{0, number, currency} {1, date, short} {2, choice, 0#Invalid|1#Valid}";

 @org.junit.Before
 public void setUp() {
 Formatter formatter = new DecimalFormat("#,##0.00");
 MessageFormat messageFormat = new MessageFormat(DEFAULT_PATTERN);
 MessageFormat extendedMessageFormat = new ExtendedMessageFormat(DEFAULT_PATTERN);
 MessageFormat registryMessageFormat = new MessageFormat(REGISTRY_PATTERN);
 Map<String, FormatFactory> registry = new HashMap<String, FormatFactory>() {{
 put("number", (format, args, locale) -> formatter);
 put("currency", (format, args, locale) -> formatter);
 put("date", (format, args, locale) -> registryMessageFormat);
 put("choice", (format, args, locale) -> registryMessageFormat);
 }};
 ExtendedMessageFormat testExtendedMessageFormat = new ExtendedMessageFormat(DEFAULT_PATTERN, registry);
 ExtendedMessageFormat testExtendedMessageFormatRegistry = new ExtendedMessageFormat(REGISTRY_PATTERN, registry);
 }

 @org.junit.Test
 public void testConstructor() {
 ExtendedMessageFormat extFormat = new ExtendedMessageFormat("");
 assertEquals("", extFormat.toPattern());
 }

 @org.junit.Test
 public void testConstructorWithPattern() {
 ExtendedMessageFormat extFormat = new ExtendedMessageFormat(DEFAULT_PATTERN);
 assertEquals(DEFAULT_PATTERN, extFormat.toPattern());
 }

 @org.junit.Test
 public void testConstructorWithPatternAndLocale() {
 ExtendedMessageFormat extFormat = new ExtendedMessageFormat(DEFAULT_PATTERN, Locale.ENGLISH);
 assertEquals(DEFAULT_PATTERN, extFormat.toPattern());
 }

 @org.junit.Test
 public void testConstructorWithPatternAndRegistry() {
 ExtendedMessageFormat extFormat = new ExtendedMessageFormat(DEFAULT_PATTERN, new HashMap<String, FormatFactory>());
 assertEquals(DEFAULT_PATTERN, extFormat.toPattern());
 }

 @org.junit.Test
 public void testConstructorWithPatternAndLocaleAndRegistry() {
 ExtendedMessageFormat extFormat = new ExtendedMessageFormat(DEFAULT_PATTERN, Locale.ENGLISH, new HashMap<String, FormatFactory>());
 assertEquals(DEFAULT_PATTERN, extFormat.toPattern());
 }

 @org.junit.Test
 public void testApplyPattern() {
 String[] args = {"Apple", "Green", "Valid"};
 ExtendedMessageFormat testExtendedMessageFormat = new ExtendedMessageFormat(DEFAULT_PATTERN, registry);
 Object[] objects = {args[0], args[1], args[2]};
 assertArrayEquals(new String[]{"Apple, GreenValid"}, testExtendedMessageFormat.format(objects).split(", "));
 }

 @org.junit.Test
 public void testApplyPatternWithRegistry() {
 String[] args = {"123456.78", "2022-01-01", "Valid"};
 ExtendedMessageFormat testExtendedMessageFormatRegistry = new ExtendedMessageFormat(REGISTRY_PATTERN, registry);
 Object[] objects = {args[0], args[1], args[2]};
 try {
 String formatted = testExtendedMessageFormatRegistry.format(objects);
 System.out.println(formatted);
 } catch (ParseException e) {
 fail("ParseException expected, but not thrown.");
 }
 assertEquals("$12,345.67 {2, date, short}", testExtendedMessageFormatRegistry.toPattern());
 }

 @org.junit.Test
 public void testToPattern() {
 ExtendedMessageFormat testExtendedMessageFormat = new ExtendedMessageFormat(DEFAULT_PATTERN, registry);
 assertEquals(DEFAULT_PATTERN, testExtendedMessageFormat.toPattern());
 }

 @org.junit.Test
 public void testSetFormat() {
 try {
 testExtendedMessageFormat.setFormat(0, new DateFormat());
 fail("UnsupportedOperationException expected, but not thrown.");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testSetFormatByArgumentIndex() {
 try {
 testExtendedMessageFormat.setFormatByArgumentIndex(0, new DateFormat());
 fail("UnsupportedOperationException expected, but not thrown.");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testSetFormats() {
 try {
 testExtendedMessageFormat.setFormats(new Format[0]);
 fail("UnsupportedOperationException expected, but not thrown.");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testSetFormatsByArgumentIndex() {
 try {
 testExtendedMessageFormat.setFormatsByArgumentIndex(new Format[0]);
 fail("UnsupportedOperationException expected, but not thrown.");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testEquals() {
 ExtendedMessageFormat testExtendedMessageFormat1 = new ExtendedMessageFormat(DEFAULT_PATTERN, registry);
 ExtendedMessageFormat testExtendedMessageFormat2 = new ExtendedMessageFormat(DEFAULT_PATTERN, registry);
 assertTrue(testExtendedMessageFormat1.equals(testExtendedMessageFormat2));
 }

 @org.junit.Test
 public void testHashCode() {
 ExtendedMessageFormat testExtendedMessageFormat1 = new ExtendedMessageFormat(DEFAULT_PATTERN, registry);
 ExtendedMessageFormat testExtendedMessageFormat2 = new ExtendedMessageFormat(DEFAULT_PATTERN, registry);
 int hashCode1 = testExtendedMessageFormat1.hashCode();
 int hashCode2 = testExtendedMessageFormat2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @org.junit.Test
 public void testGetFormat() {
 ExtendedMessageFormat testExtendedMessageFormatRegistry = new ExtendedMessageFormat(REGISTRY_PATTERN, registry);
 Format format = testExtendedMessageFormatRegistry.getFormat("date");
 assertTrue(format instanceof MessageFormat);
 }

 @org.junit.Test
 public void testReadArgumentIndex() {
 String pattern = "{}";
 ParsePosition pos = new ParsePosition(0);
 assertEquals(0, testExtendedMessageFormat.readArgumentIndex(pattern, pos));
 }

 @org.junit.Test
 public void testParseFormatDescription() {
 String pattern = "{}";
 ParsePosition pos = new ParsePosition(0);
 assertEquals(null, testExtendedMessageFormat.parseFormatDescription(pattern, pos));
 }

 @org.junit.Test
 public void testInsertFormats() {
 String pattern = "{}";
 ArrayList<String> customPatterns = new ArrayList<>();
 assertEquals(pattern, testExtendedMessageFormat.insertFormats(pattern, customPatterns));
 }

 @org.junit.Test
 public void testSeekNonWs() {
 String pattern = "{}";
 ParsePosition pos = new ParsePosition(0);
 int len = 0;
 assertEquals(0, len);
 pos.setIndex(1);
 len = StrMatcher.splitMatcher().isMatch(pattern.toCharArray(), pos.getIndex());
 assertEquals(1, len);
 pos.setIndex(pos.getIndex() + len);
 len = StrMatcher.splitMatcher().isMatch(pattern.toCharArray(), pos.getIndex());
 assertEquals(0, len);
 }

 @org.junit.Test
 public void testNext() {
 ParsePosition pos = new ParsePosition(0);
 pos.setIndex(1);
 assertEquals(1, pos.getIndex());
 }

 @org.junit.Test
 public void testAppendQuotedString() {
 String pattern = "{}'{}'{}";
 StringBuilder appendTo = new StringBuilder();
 ExtendedMessageFormat testExtendedMessageFormat = new ExtendedMessageFormat(pattern, registry);
 ParsePosition pos = new ParsePosition(0);
 appendQuotedString(testExtendedMessageFormat, pattern, pos, appendTo, false);
 assertEquals("", appendTo.toString());
 pos.setIndex(1);
 appendQuotedString(testExtendedMessageFormat, pattern, pos, appendTo, false);
 assertEquals("", appendTo.toString());
 pos.setIndex(2);
 appendQuotedString(testExtendedMessageFormat, pattern, pos, appendTo, false);
 assertEquals("'", appendTo.toString());
 pos.setIndex(3);
 appendQuotedString(testExtendedMessageFormat, pattern, pos, appendTo, true);
 assertEquals("", appendTo.toString());
 pos.setIndex(4);
 appendQuotedString(testExtendedMessageFormat, pattern, pos, appendTo, true);
 assertEquals("''", appendTo.toString());
 pos.setIndex(5);
 appendQuotedString(testExtendedMessageFormat, pattern, pos, appendTo, true);
 assertEquals("''", appendTo.toString());
 pos.setIndex(6);
 appendQuotedString(testExtendedMessageFormat, pattern, pos, appendTo, true);
 assertEquals("''", appendTo.toString());
 pos.setIndex(7);
 appendQuotedString(testExtendedMessageFormat, pattern, pos, appendTo, true);
 assertEquals("", appendTo.toString());
 }

 @org.junit.Test
 public void testGetQuotedString() {
 String pattern = "{}'{}'{}";
 ExtendedMessageFormat testExtendedMessageFormat = new ExtendedMessageFormat(pattern, registry);
 ParsePosition pos = new ParsePosition(0);
 getQuotedString(testExtendedMessageFormat, pattern, pos, false);
 }

 @org.junit.Test
 public void testContainsElements() {
 assertEquals(false, testExtendedMessageFormat.containsElements(null));
 assertEquals(false, testExtendedMessageFormat.containsElements(Arrays.asList()));
 assertEquals(true, testExtendedMessageFormat.containsElements(Arrays.asList("number")));
 assertEquals(false, testExtendedMessageFormat.containsElements(Arrays.asList("invalidFormat")));
 }
}