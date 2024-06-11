package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.Validate;
import org.junit.Test;

public class ExtendedMessageFormatTest {

 private static final String PATTERN_WITH_PLACEHOLDERS = "{0}, {1:date}, {2:date,format=\"dd-MM-yyyy\"}, {3}";
 private static final String DATE_FORMAT_1 = "dd-MM-yyyy";
 private static final String DATE_FORMAT_2 = "yyyy-MM-dd";

 @Test
 public void testConstructorWithPattern() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 assertEquals(PATTERN_WITH_PLACEHOLDERS, messageFormat.toPattern());
 }

 @Test
 public void testConstructorWithPatternAndLocale() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.US);
 assertEquals(PATTERN_WITH_PLACEHOLDERS, messageFormat.toPattern());
 }

 @Test
 public void testConstructorWithPatternAndRegistry() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", (format, arg, locale) -> new SimpleDateFormat(DATE_FORMAT_1, locale));
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, null, registry);
 assertEquals(PATTERN_WITH_PLACEHOLDERS, messageFormat.toPattern());
 }

 @Test
 public void testConstructorWithPatternAndLocaleAndRegistry() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", (format, arg, locale) -> new SimpleDateFormat(DATE_FORMAT_1, locale));
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.US, registry);
 assertEquals(PATTERN_WITH_PLACEHOLDERS, messageFormat.toPattern());
 }

 @Test
 public void testToPattern() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.US, registry);
 messageFormat.applyPattern(PATTERN_WITH_PLACEHOLDERS);
 assertEquals(PATTERN_WITH_PLACEHOLDERS, messageFormat.toPattern());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.US, registry);
 messageFormat.setFormat(0, new SimpleDateFormat(DATE_FORMAT_2));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndex() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.US, registry);
 messageFormat.setFormatByArgumentIndex(0, new SimpleDateFormat(DATE_FORMAT_2));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.US, registry);
 messageFormat.setFormats(new Format[0]);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndex() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.US, registry);
 messageFormat.setFormatsByArgumentIndex(new Format[0]);
 }

 @Test
 public void testEquals() {
 ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.US, registry);
 ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.US, registry);
 assertTrue(messageFormat1.equals(messageFormat2));
 }

 @Test
 public void testHashCode() {
 ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.US, registry);
 ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.US, registry);
 assertEquals(messageFormat1.hashCode(), messageFormat2.hashCode());
 }

 @Test
 public void testGetFormat() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.US, registry);
 messageFormat.applyPattern(PATTERN_WITH_PLACEHOLDERS);
 assertEquals(DateTimeFormat.forPattern(DATE_FORMAT_1), messageFormat.getFormat(0));
 }

 @Test(expected = NullPointerException.class)
 public void testGetFormatWithNullRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.US, null);
 messageFormat.applyPattern(PATTERN_WITH_PLACEHOLDERS);
 messageFormat.getFormat(0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFormatWithInvalidFormatName() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.US, registry);
 messageFormat.applyPattern(PATTERN_WITH_PLACEHOLDERS);
 messageFormat.getFormat(-1);
 }

 @Test
 public void testReadArgumentIndex() {
 char[] pattern = PATTERN_WITH_PLACEHOLDERS.toCharArray();
 ParsePosition pos = new ParsePosition(0);
 int index = ExtendedMessageFormat.this.readArgumentIndex(StringUtils.substring(pattern, 0, 4), pos);
 assertEquals(0, index);
 index = ExtendedMessageFormat.this.readArgumentIndex(StringUtils.substring(pattern, 5, 10), pos);
 assertEquals(1, index);
 index = ExtendedMessageFormat.this.readArgumentIndex(StringUtils.substring(pattern, 11, 16), pos);
 assertEquals(2, index);
 index = ExtendedMessageFormat.this.readArgumentIndex(StringUtils.substring(pattern, 17, pattern.length), pos);
 assertEquals(3, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadArgumentIndexWithInvalidFormatIndex() {
 char[] pattern = PATTERN_WITH_PLACEHOLDERS.toCharArray();
 ParsePosition pos = new ParsePosition(0);
 ExtendedMessageFormat.this.readArgumentIndex(StringUtils.substring(pattern, 0, 4), pos);
 ExtendedMessageFormat.this.readArgumentIndex(StringUtils.substring(pattern, 5, 6), pos);
 throw new AssertionError("Unexpected exception not thrown");
 }

 @Test
 public void testParseFormatDescription() {
 char[] pattern = PATTERN_WITH_PLACEHOLDERS.toCharArray();
 ParsePosition pos = new ParsePosition(0);
 String formatDescription = ExtendedMessageFormat.this.parseFormatDescription(StringUtils.substring(pattern, 5, 16), pos);
 assertEquals("date", formatDescription);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFormatDescriptionWithInvalidFormatDescription() {
 char[] pattern = PATTERN_WITH_PLACEHOLDERS.toCharArray();
 ParsePosition pos = new ParsePosition(0);
 ExtendedMessageFormat.this.parseFormatDescription(StringUtils.substring(pattern, 0, 4), pos);
 ExtendedMessageFormat.this.parseFormatDescription(StringUtils.substring(pattern, 5, 5), pos);
 throw new AssertionError("Unexpected exception not thrown");
 }

 @Test
 public void testInsertFormats() {
 String pattern = PATTERN_WITH_PLACEHOLDERS;
 ArrayList<String> customPatterns = new ArrayList<>();
 customPatterns.add("{4:date,format=\"dd-MM-yyyy\"}");
 ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat(pattern, null, null);
 String formattedPattern = extendedMessageFormat.insertFormats(pattern, customPatterns);
 assertEquals("{0}, {1:date}, {2:date,format=\"dd-MM-yyyy\"}, {3},{4:dd-MM-yyyy}", formattedPattern);
 }

 @Test
 public void testApplyPatternWithRegistry() {
 String pattern = PATTERN_WITH_PLACEHOLDERS;
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", (format, arg, locale) -> new SimpleDateFormat(DATE_FORMAT_1, locale));
 ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat(pattern, Locale.US, registry);
 Object[] arguments = {new Date(), new Date(), new Date(), "Test"};
 String formattedMessage = extendedMessageFormat.format(arguments);
 String expectedFormattedMessage = "Test, Mon Aug 22 10:12:13 CDT 2022, Mon Aug 22 10:12:13 CDT 2022, Mon Aug 22 2022, Test";
 assertEquals(expectedFormattedMessage, formattedMessage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyPatternWithInvalidPattern() {
 String invalidPattern = "InvalidPattern";
 ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat(invalidPattern, Locale.US, null);
 try {
 extendedMessageFormat.applyPattern(invalidPattern);
 } catch (ParseException e) {
 // Expected
 }
 }

}