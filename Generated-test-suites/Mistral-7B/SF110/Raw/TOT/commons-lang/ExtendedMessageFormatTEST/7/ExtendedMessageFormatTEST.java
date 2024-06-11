package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormatMessageResolver;
import org.apache.commons.lang3.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

public class ExtendedMessageFormatTest {

 private static final String PATTERN_WITH_PLACEHOLDERS = "{0,date,short,MMMM dd, yyyy hh:mm a} {} {1,number,integer}";
 private static final String PATTERN_WITH_FORMAT_REGISTRY = "{0,format,custom.uppercaseFormat} {} {1,format,custom.lowercaseFormat}";

 @Test
 public void testSimpleUsage() {
 final Locale locale = Locale.ENGLISH;
 final Map<String, FormatFactory> formatRegistry = new HashMap<>();
 formatRegistry.put("custom.uppercaseFormat", new FormatFactory() {
 @Override
 public Format getFormat(String pattern, String formatName, Locale locale) {
 return new SimpleDateFormat(pattern.toUpperCase(), locale);
 }
 });
 formatRegistry.put("custom.lowercaseFormat", new FormatFactory() {
 @Override
 public Format getFormat(String pattern, String formatName, Locale locale) {
 return new SimpleDateFormat(pattern, locale);
 }
 });

 try {
 final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, locale, formatRegistry);
 final Object[] args = { new Date(System.currentTimeMillis()), "example" };
 final String formattedMessage = messageFormat.format(args);
 assertEquals("example 01 JANUARY 2023 03:30 PM 0", formattedMessage);
 } catch (IllegalArgumentException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testPlaceholderNull() {
 final Locale locale = Locale.ENGLISH;
 final Map<String, FormatFactory> formatRegistry = new HashMap<>();
 final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, locale, formatRegistry);
 final Object[] args = { null, "example" };
 try {
 messageFormat.format(args);
 fail("Expected IllegalArgumentException, but got no exception.");
 } catch (IllegalArgumentException e) {
 assertEquals("Argument 0 cannot be null", e.getMessage());
 }
 }

 @Test
 public void testPlaceholderOutOfBounds() {
 final Locale locale = Locale.ENGLISH;
 final Map<String, FormatFactory> formatRegistry = new HashMap<>();
 final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, locale, formatRegistry);
 final Object[] args = { new Date(System.currentTimeMillis()), "example", "xyz" };
 try {
 messageFormat.format(args);
 fail("Expected IllegalArgumentException, but got no exception.");
 } catch (IllegalArgumentException e) {
 assertEquals("Index 2 out of bounds for length 2", e.getMessage());
 }
 }

 @Test
 public void testFormatRegistryMissingFormat() {
 final Locale locale = Locale.ENGLISH;
 final Map<String, FormatFactory> formatRegistry = new HashMap<>();
 final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_FORMAT_REGISTRY, locale, formatRegistry);
 try {
 messageFormat.applyPattern(PATTERN_WITH_FORMAT_REGISTRY);
 fail("Expected IllegalArgumentException, but got no exception.");
 } catch (IllegalArgumentException e) {
 assertEquals("Format 'custom.missingFormat' not found", e.getMessage());
 }
 }

 @Test
 public void testFormatRegistryMissingFormatDescription() {
 final Locale locale = Locale.ENGLISH;
 final Map<String, FormatFactory> formatRegistry = new HashMap<>();
 final FormatFactory formatFactory = new FormatFactory() {
 @Override
 public Format getFormat(String pattern, String formatName, Locale locale) {
 return new SimpleDateFormat(pattern + " missing description");
 }
 };
 formatRegistry.put("custom.formatFactory", formatFactory);
 final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_FORMAT_REGISTRY, locale, formatRegistry);
 try {
 messageFormat.applyPattern(PATTERN_WITH_FORMAT_REGISTRY);
 fail("Expected IllegalArgumentException, but got no exception.");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid format description 'custom.formatFactory missing description'", e.getMessage());
 }
 }

 @Test
 public void testFormatRegistryInvalidFormat() {
 final Locale locale = Locale.ENGLISH;
 final Map<String, FormatFactory> formatRegistry = new HashMap<>();
 final FormatFactory formatFactory = new FormatFactory() {
 @Override
 public Format getFormat(String pattern, String formatName, Locale locale) {
 throw new RuntimeException("Invalid format");
 }
 };
 formatRegistry.put("custom.invalidFormat", formatFactory);
 final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_FORMAT_REGISTRY, locale, formatRegistry);
 try {
 messageFormat.applyPattern(PATTERN_WITH_FORMAT_REGISTRY);
 fail("Expected IllegalArgumentException, but got no exception.");
 } catch (IllegalArgumentException e) {
 assertEquals("Format factory 'custom.invalidFormat' threw exception: Invalid format", e.getMessage());
 }
 }

 @Test
 public void testFormatRegistryNullFormat() {
 final Locale locale = Locale.ENGLISH;
 final Map<String, FormatFactory> formatRegistry = new HashMap<>();
 final FormatFactory formatFactory = null;
 formatRegistry.put("custom.nullFormat", formatFactory);
 final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_FORMAT_REGISTRY, locale, formatRegistry);
 try {
 messageFormat.applyPattern(PATTERN_WITH_FORMAT_REGISTRY);
 fail("Expected IllegalArgumentException, but got no exception.");
 } catch (IllegalArgumentException e) {
 assertEquals("Format factory 'custom.nullFormat' is null", e.getMessage());
 }
 }

 @Test
 public void testFormatRegistryInvalidFormatName() {
 final Locale locale = Locale.ENGLISH;
 final Map<String, FormatFactory> formatRegistry = new HashMap<>();
 final FormatFactory formatFactory = new FormatFactory() {
 @Override
 public Format getFormat(String pattern, String formatName, Locale locale) {
 return new SimpleDateFormat(pattern);
 }
 };
 formatRegistry.put("custom.invalidFormatName", formatFactory);
 final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_FORMAT_REGISTRY, locale, formatRegistry);
 try {
 messageFormat.applyPattern(PATTERN_WITH_FORMAT_REGISTRY);
 fail("Expected IllegalArgumentException, but got no exception.");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid format name 'custom.invalidFormatName'", e.getMessage());
 }
 }

 @Test
 public void testFormatRegistryMissingArgumentDescription() {
 final Locale locale = Locale.ENGLISH;
 final Map<String, FormatFactory> formatRegistry = new HashMap<>();
 final FormatFactory formatFactory = new FormatFactory() {
 @Override
 public Format getFormat(String pattern, String formatName, Locale locale) {
 return new SimpleDateFormat(pattern);
 }
 };
 final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_FORMAT_REGISTRY, locale, formatRegistry);
 try {
 messageFormat.applyPattern(PATTERN_WITH_FORMAT_REGISTRY.replace("{0,format,custom.lowercaseFormat}", "{0}"));
 fail("Expected IllegalArgumentException, but got no exception.");
 } catch (IllegalArgumentException e) {
 assertEquals("Missing format description for argument index 0 in format {0,format,custom.lowercaseFormat}", e.getMessage());
 }
 }

 @Test
 public void testFormatRegistryMissingFormatDescriptionWithFallback() {
 final Locale locale = Locale.ENGLISH;
 final Map<String, FormatFactory> formatRegistry = new HashMap<>();
 final FormatFactory formatFactory = new FormatFactory() {
 @Override
 public Format getFormat(String pattern, String formatName, Locale locale) {
 return new SimpleDateFormat(pattern);
 }
 };
 formatRegistry.put("custom.formatFactory", formatFactory);
 final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_FORMAT_REGISTRY, locale, formatRegistry);
 try {
 messageFormat.applyPattern(PATTERN_WITH_FORMAT_REGISTRY.replace("{0,format,custom.lowercaseFormat}", "{0}"));
 } catch (IllegalArgumentException e) {
 fail("Expected no exception, as there is a default format for the '{0}' placeholder.");
 }
 }

 @Test
 public void testCustomFormat() {
 final Locale locale = Locale.ENGLISH;
 final Map<String, FormatFactory> formatRegistry = new HashMap<>();
 final FormatFactory formatFactory = new FormatFactory() {
 @Override
 public Format getFormat(String pattern, String formatName, Locale locale) {
 return new MessageFormatMessageResolver(Pattern.compile(pattern), locale);
 }
 };
 formatRegistry.put("custom.messageFormatResolver", formatFactory);
 final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_FORMAT_REGISTRY, locale, formatRegistry);
 try {
 messageFormat.applyPattern(PATTERN_WITH_FORMAT_REGISTRY);
 final Object[] args = { "hello", 42 };
 final String formattedMessage = messageFormat.format(args);
 assertEquals("hello {1} {0}", formattedMessage);
 } catch (IllegalArgumentException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testCustomFormatWithMissingFormatDescription() {
 final Locale locale = Locale.ENGLISH;
 final Map<String, FormatFactory> formatRegistry = new HashMap<>();
 final FormatFactory formatFactory = new FormatFactory() {
 @Override
 public Format getFormat(String pattern, String formatName, Locale locale) {
 return new MessageFormatMessageResolver(Pattern.compile(pattern), locale);
 }
 };
 final ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(PATTERN_WITH_FORMAT_REGISTRY.replace("{1,format,custom.lowercaseFormat}", "{1}"), locale, formatRegistry);
 try {
 messageFormat.applyPattern(PATTERN_WITH_FORMAT_REGISTRY.replace("{1,format,custom.lowercaseFormat}", "{1}"));
 fail("Expected IllegalArgumentException, but got no exception.");
 } catch (IllegalArgumentException e) {
 assertEquals("Missing format description for argument index 1 in format {1,format,custom.lowercaseFormat}", e.getMessage());
 }
 }
}