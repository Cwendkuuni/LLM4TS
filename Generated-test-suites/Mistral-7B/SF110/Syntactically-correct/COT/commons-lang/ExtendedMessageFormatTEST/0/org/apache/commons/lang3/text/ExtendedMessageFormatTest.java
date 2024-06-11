package org.junit.test;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

 private static final String PATTERN_WITH_PLACEHOLDERS = "Hello {0}, your age is {1}. The format is {2}.";
 private static final String CUSTOM_FORMAT_DESCRIPTION = "customFormat[arg1,arg2]";

 @Test
 public void testDefaultConstructor() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat();
 assertNull(messageFormat.toPattern());
 assertNull(messageFormat.registry);
 }

 @Test
 public void testConstructorWithPattern() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 assertEquals(PATTERN_WITH_PLACEHOLDERS, messageFormat.toPattern());
 assertNotNull(messageFormat.registry);
 }

 @Test
 public void testConstructorWithPatternAndLocale() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.ENGLISH);
 assertEquals(PATTERN_WITH_PLACEHOLDERS, messageFormat.toPattern());
 assertNotNull(messageFormat.registry);
 }

 @Test
 public void testConstructorWithPatternAndRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, new HashMap<>());
 assertEquals(PATTERN_WITH_PLACEHOLDERS, messageFormat.toPattern());
 assertNotNull(messageFormat.registry);
 }

 @Test
 public void testConstructorWithPatternAndLocaleAndRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, Locale.ENGLISH, new HashMap<>());
 assertEquals(PATTERN_WITH_PLACEHOLDERS, messageFormat.toPattern());
 assertNotNull(messageFormat.registry);
 }

 @Test
 public void testToPattern() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 assertEquals(PATTERN_WITH_PLACEHOLDERS, messageFormat.toPattern());

 ExtendedMessageFormat messageFormatWithCustomPattern = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, new HashMap<>());
 messageFormatWithCustomPattern.applyPattern(PATTERN_WITH_PLACEHOLDERS + CUSTOM_FORMAT_DESCRIPTION);
 assertEquals(PATTERN_WITH_PLACEHOLDERS + CUSTOM_FORMAT_DESCRIPTION, messageFormatWithCustomPattern.toPattern());
 }

 @Test
 public void testApplyPattern() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 messageFormat.applyPattern(PATTERN_WITH_PLACEHOLDERS);
 assertEquals(PATTERN_WITH_PLACEHOLDERS, messageFormat.toPattern());

 ExtendedMessageFormat messageFormatWithCustomPattern = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, new HashMap<>());
 String customFormatDescription = "customFormat[arg1,arg2]";
 messageFormatWithCustomPattern.applyPattern(PATTERN_WITH_PLACEHOLDERS + customFormatDescription);
 assertEquals(PATTERN_WITH_PLACEHOLDERS + customFormatDescription, messageFormatWithCustomPattern.toPattern());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 messageFormat.setFormat(1, new MessageFormat(""));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndex() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 messageFormat.setFormatByArgumentIndex(1, new MessageFormat(""));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 messageFormat.setFormats(new MessageFormat[0]);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndex() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 messageFormat.setFormatsByArgumentIndex(new MessageFormat[0]);
 }

 @Test
 public void testEquals() {
 ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 assertTrue(messageFormat1.equals(messageFormat2));

 ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, new HashMap<>());
 assertFalse(messageFormat1.equals(messageFormat3));

 ExtendedMessageFormat messageFormat4 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, new HashMap<>());
 messageFormat4.applyPattern(PATTERN_WITH_PLACEHOLDERS + CUSTOM_FORMAT_DESCRIPTION);
 assertFalse(messageFormat1.equals(messageFormat4));
 }

 @Test
 public void testHashCode() {
 ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 assertEquals(messageFormat1.hashCode(), messageFormat2.hashCode());

 ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, new HashMap<>());
 assertNotEquals(messageFormat1.hashCode(), messageFormat3.hashCode());

 ExtendedMessageFormat messageFormat4 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, new HashMap<>());
 messageFormat4.applyPattern(PATTERN_WITH_PLACEHOLDERS + CUSTOM_FORMAT_DESCRIPTION);
 assertNotEquals(messageFormat1.hashCode(), messageFormat4.hashCode());
 }

 @Test
 public void testGetFormat() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, new HashMap<String, FormatFactory>() {{
 put("customFormat", new CustomFormatFactory());
 }});
 String customFormatDescription = "customFormat[arg1,arg2]";
 messageFormat.applyPattern(PATTERN_WITH_PLACEHOLDERS + customFormatDescription);
 Format customFormat = messageFormat.getFormat("customFormat");
 assertNotNull(customFormat);
 }

 @Test(expected = NullPointerException.class)
 public void testGetFormatWithNullDescription() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, new HashMap<String, FormatFactory>() {{
 put("customFormat", new CustomFormatFactory());
 }});
 messageFormat.getFormat(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadArgumentIndexWithInvalidArgumentIndex() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, new HashMap<>());
 messageFormat.applyPattern(PATTERN_WITH_PLACEHOLDERS + "{10}");
 try {
 messageFormat.readArgumentIndex(PATTERN_WITH_PLACEHOLDERS, new ParsePosition(0));
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testReadArgumentIndexWithValidArgumentIndex() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, new HashMap<>());
 messageFormat.applyPattern(PATTERN_WITH_PLACEHOLDERS + "{0}");
 int argumentIndex = messageFormat.readArgumentIndex(PATTERN_WITH_PLACEHOLDERS, new ParsePosition(0));
 assertEquals(0, argumentIndex);
 }

 @Test
 public void testParseFormatDescriptionWithValidFormatDescription() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, new HashMap<>());
 String formatDescription = "customFormat[arg1,arg2]";
 messageFormat.applyPattern(PATTERN_WITH_PLACEHOLDERS + formatDescription);
 String parsedFormatDescription = messageFormat.parseFormatDescription(PATTERN_WITH_PLACEHOLDERS, new ParsePosition(0));
 assertEquals(formatDescription, parsedFormatDescription);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFormatDescriptionWithInvalidFormatDescription() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, new HashMap<>());
 messageFormat.applyPattern(PATTERN_WITH_PLACEHOLDERS + "{0}");
 try {
 messageFormat.parseFormatDescription(PATTERN_WITH_PLACEHOLDERS, new ParsePosition(1));
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testInsertFormats() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, new HashMap<>());
 ArrayList<String> customPatterns = new ArrayList<>();
 customPatterns.add("customFormat");
 messageFormat.applyPattern(PATTERN_WITH_PLACEHOLDERS);
 messageFormat.insertFormats(PATTERN_WITH_PLACEHOLDERS, customPatterns);
 String result = messageFormat.format(new Object[]{"John Doe", 30, PATTERN_WITH_PLACEHOLDERS});
 assertEquals("Hello John Doe, your age is 30. The format is customFormat[arg1,arg2].", result);
 }
}

class CustomFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String description, Object arg, Locale locale) {
 return new MessageFormat("CustomFormat[{0}]");
 }
}