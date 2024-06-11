package org.apache.commons.lang3.text;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

 private static final String TEST_PATTERN = "{0,date,yyyy-MM-dd HH:mm:ss.SSS} {1} {2,choice,0#No|1#Yes}.";

 @Test
 public void testEmptyConstructor() {
 ExtendedMessageFormat format = new ExtendedMessageFormat();
 assertNull(format.toPattern());
 }

 @Test
 public void testStringConstructor() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN);
 assertEquals(TEST_PATTERN, format.toPattern());
 }

 @Test
 public void testStringConstructorWithLocale() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN, Locale.US);
 assertEquals(TEST_PATTERN, format.toPattern());
 }

 @Test
 public void testStringConstructorWithRegistry() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new SimpleDateFormatFactory("yyyy-MM-dd HH:mm:ss.SSS"));
 ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN, null, registry);
 assertEquals(TEST_PATTERN, format.toPattern());
 }

 @Test
 public void testStringConstructorWithLocaleAndRegistry() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new SimpleDateFormatFactory("yyyy-MM-dd HH:mm:ss.SSS"));
 ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN, Locale.US, registry);
 assertEquals(TEST_PATTERN, format.toPattern());
 }

 @Test
 public void testInvalidPattern() {
 assertThrows(IllegalArgumentException.class, () -> new ExtendedMessageFormat("{0}"));
 }

 @Test
 public void testApplyPatternWithEmptyRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN, null);
 format.applyPattern(TEST_PATTERN);
 assertTrue(format.containsElements(format.getFormats()));
 }

 @Test
 public void testApplyPatternWithCustomFormats() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new DateFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN, null, registry);
 format.applyPattern(TEST_PATTERN);
 assertTrue(format.containsElements(format.getFormats()));
 }

 @Test
 public void testApplyPatternWithCustomFormatIndexOutOfBounds() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN, null, null);
 format.applyPattern(TEST_PATTERN);
 try {
 format.setFormat(1, new SimpleDateFormat("yyyy-MM-dd"));
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testApplyPatternWithCustomFormatNull() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN, null, null);
 format.applyPattern(TEST_PATTERN);
 try {
 format.setFormatByArgumentIndex(1, null);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testApplyPatternWithCustomFormatInvalidDescription() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN, null, null);
 format.applyPattern(TEST_PATTERN);
 assertThrows(IllegalArgumentException.class, () -> format.setFormatByArgumentIndex(1, "invalidFormat"));
 }

 @Test
 public void testFormatWithCustomFormat() throws ParseException {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new DateFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN, null, registry);
 format.applyPattern(TEST_PATTERN);
 Object[] args = { new Date(System.currentTimeMillis()), "yes", Boolean.TRUE };
 String result = format.format(args);
 assertEquals("{1-12-31 15:14:39.123} yes Yes", result);
 }

 @Test
 public void testFormatWithInvalidArgumentCount() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN, null);
 format.applyPattern(TEST_PATTERN);
 Object[] args = { new Date(System.currentTimeMillis()), "yes" };
 String result = format.format(args);
 assertNull(result);
 }

 @Test
 public void testFormatWithInvalidFormatIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN, null, null);
 format.applyPattern(TEST_PATTERN);
 Object[] args = { new Date(System.currentTimeMillis()), "yes", new Date(System.currentTimeMillis()), Boolean.TRUE };
 assertNull(format.format(args));
 }

 @Test
 public void testFormatWithInvalidFormatDescription() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN, null, null);
 format.applyPattern(TEST_PATTERN);
 Object[] args = { new Date(System.currentTimeMillis()), "yes", "invalidFormat" };
 assertNull(format.format(args));
 }

 @Test
 public void testFormatWithNullArguments() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN, null);
 format.applyPattern(TEST_PATTERN);
 Object[] args = { null, null, null };
 assertNull(format.format(args));
 }

 @Test
 public void testEquals() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat(TEST_PATTERN, null);
 ExtendedMessageFormat format2 = new ExtendedMessageFormat(TEST_PATTERN, null);
 ExtendedMessageFormat format3 = new ExtendedMessageFormat(TEST_PATTERN, Locale.US);
 ExtendedMessageFormat format4 = new ExtendedMessageFormat(TEST_PATTERN, null, null);
 ExtendedMessageFormat format5 = new ExtendedMessageFormat(TEST_PATTERN, null, null);
 assertTrue(format1.equals(format1));
 assertTrue(format1.equals(format2));
 assertFalse(format1.equals(format3));
 assertFalse(format1.equals(format4));
 assertFalse(format1.equals(format5));
 }

 @Test
 public void testHashCode() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat(TEST_PATTERN, null);
 ExtendedMessageFormat format2 = new ExtendedMessageFormat(TEST_PATTERN, null);
 ExtendedMessageFormat format3 = new ExtendedMessageFormat(TEST_PATTERN, Locale.US);
 ExtendedMessageFormat format4 = new ExtendedMessageFormat(TEST_PATTERN, null, null);
 ExtendedMessageFormat format5 = new ExtendedMessageFormat(TEST_PATTERN, null, null);
 int code1 = format1.hashCode();
 int code2 = format2.hashCode();
 int code3 = format3.hashCode();
 int code4 = format4.hashCode();
 int code5 = format5.hashCode();
 assertTrue(code1 == code2);
 assertFalse(code1 == code3);
 assertFalse(code1 == code4);
 assertFalse(code1 == code5);
 }

}