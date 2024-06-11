package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.SimpleDateFormat;

public class ExtendedMessageFormatTest {

 // Test constructor with default arguments
 @Test
 public void testDefaultConstructor() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("");
 assertEquals("", messageFormat.toPattern());
 }

 // Test constructor with pattern and default locale
 @Test
 public void testConstructorWithPatternAndDefaultLocale() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
 assertEquals("Hello {0}, your age is {1}", messageFormat.toPattern());
 }

 // Test constructor with pattern, default locale, and empty registry
 @Test
 public void testConstructorWithPatternAndDefaultLocaleAndEmptyRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>());
 assertEquals("Hello {0}, your age is {1}. ", messageFormat.toPattern());
 }

 // Test constructor with pattern, locale, and empty registry
 @Test
 public void testConstructorWithPatternLocaleAndEmptyRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new Locale("fr"), new HashMap<String, FormatFactory>());
 assertEquals("Hello {0}, your age is {1}. ", messageFormat.toPattern());
 }

 // Test constructor with pattern, locale, and registry
 @Test
 public void testConstructorWithPatternLocaleAndRegistry() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new DateFormatFactory());
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}{3}", new Locale("fr"), registry);
 assertEquals("Hello {0}, your age is {1}. {2}{3}", messageFormat.toPattern());
 }

 // Test toPattern method with default registry
 @Test
 public void testToPatternWithDefaultRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>());
 assertEquals("Hello {0}, your age is {1}. ", messageFormat.toPattern());
 }

 // Test toPattern method with custom registry
 @Test
 public void testToPatternWithCustomRegistry() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new DateFormatFactory());
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}{3}", new Locale("fr"), registry);
 messageFormat.applyPattern("Hello {0}, your age is {1}. {2}{0}");
 assertEquals("Hello {0}, your age is {1}. {2}{0}", messageFormat.toPattern());
 }

 // Test applyPattern method with default registry
 @Test
 public void testApplyPatternWithDefaultRegistry() throws ParseException {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>());
 messageFormat.applyPattern("Hello {0}, your age is {1}. {2} {3}");
 assertEquals("Hello {0}, your age is {1}. {2} {3}", messageFormat.toPattern());
 }

 // Test applyPattern method with custom registry
 @Test
 public void testApplyPatternWithCustomRegistry() throws ParseException {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new DateFormatFactory());
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}{3}", new Locale("fr"), registry);
 messageFormat.applyPattern("Hello {0}, your age is {1}. {2}{0} {4}");
 assertEquals("Hello {0}, your age is {1}. {2}{0} {4}", messageFormat.toPattern());
 }

 // Test equals method
 @Test
 public void testEquals() {
 ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>());
 ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>());
 ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new Locale("fr"), new HashMap<String, FormatFactory>());
 ExtendedMessageFormat messageFormat4 = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new Locale("fr"), messageFormat1.getFormats());
 assertTrue(messageFormat1.equals(messageFormat1));
 assertTrue(messageFormat1.equals(messageFormat2));
 assertFalse(messageFormat1.equals(messageFormat3));
 assertFalse(messageFormat1.equals(messageFormat4));
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>());
 ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>());
 ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new Locale("fr"), new HashMap<String, FormatFactory>());
 ExtendedMessageFormat messageFormat4 = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new Locale("fr"), messageFormat1.getFormats());
 assertEquals(messageFormat1.hashCode(), messageFormat1.hashCode());
 assertEquals(messageFormat1.hashCode(), messageFormat2.hashCode());
 assertNotEquals(messageFormat1.hashCode(), messageFormat3.hashCode());
 assertNotEquals(messageFormat1.hashCode(), messageFormat4.hashCode());
 }

 // Test getFormat method with default registry
 @Test(expected = UnsupportedOperationException.class)
 public void testGetFormatWithDefaultRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>());
 messageFormat.getFormat(0);
 }

 // Test getFormat method with custom registry
 @Test
 public void testGetFormatWithCustomRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>(), new Properties());
 messageFormat.applyPattern("Hello {0}, your age is {1}. {2} {3}");
 messageFormat.setLocale(Locale.US);
 messageFormat.setFormat("date", new SimpleDateFormat("yyyy-MM-dd"));
 Format dateFormat = messageFormat.getFormat("date");
 assertEquals(SimpleDateFormat.class, dateFormat.getClass());
 }

 // Test getFormatByArgumentIndex method with default registry
 @Test(expected = UnsupportedOperationException.class)
 public void testGetFormatByArgumentIndexWithDefaultRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>());
 messageFormat.getFormatByArgumentIndex(0);
 }

 // Test setFormat method with default registry
 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatWithDefaultRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>());
 messageFormat.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
 }

 // Test setFormatByArgumentIndex method with default registry
 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndexWithDefaultRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>());
 messageFormat.setFormatByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd"));
 }

 // Test setFormats method with default registry
 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsWithDefaultRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>());
 messageFormat.setFormats(new Object[] { new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("yyyy-MM-dd") });
 }

 // Test setFormatsByArgumentIndex method with default registry
 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndexWithDefaultRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>());
 messageFormat.setFormatsByArgumentIndex(new Object[] { new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("yyyy-MM-dd") });
 }

 // Test format method with default registry
 @Test
 public void testFormatWithDefaultRegistry() throws ParseException {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>());
 messageFormat.applyPattern("Hello {0}, your age is {1}. {2} {3}");
 Object[] args = { "John Doe", 25, new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01") };
 String result = messageFormat.format(args);
 assertEquals("Hello John Doe, your age is 25. 2022-01-01 Mon Jan 03 00:00:00 CET 2022", result);
 }

 // Test format method with custom registry
 @Test
 public void testFormatWithCustomRegistry() throws ParseException {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>(), new Properties());
 messageFormat.applyPattern("Hello {0}, your age is {1}. {2} {3}");
 messageFormat.setLocale(Locale.US);
 messageFormat.setFormat("date", new SimpleDateFormat("MM/dd/yyyy"));
 messageFormat.setFormat("time", new SimpleDateFormat("h:mm a"));
 Object[] args = { "John Doe", 25, new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01"), new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-02") };
 String result = messageFormat.format(args);
 assertEquals("Hello John Doe, your age is 25. 01/01/2022 Mon Jan 03 12:00 PM 01/02/2022 Tue Jan 04 11:00 AM", result);
 }

 // Test parse method with default registry
 @Test(expected = ParseException.class)
 public void testParseWithDefaultRegistry() throws ParseException {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>());
 Object[] args = { "Hello {0}, your age is {1}. {2} {3}", new Object[] { "John Doe", 25, new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01"), new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-02") } };
 messageFormat.parse(StringUtils.join(args, ", "));
 }

 // Test parse method with custom registry
 @Test
 public void testParseWithCustomRegistry() throws ParseException {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}. {2}", new HashMap<String, FormatFactory>(), new Properties());
 messageFormat.applyPattern("Hello {0}, your age is {1}. {2} {3}");
 messageFormat.setLocale(Locale.US);
 messageFormat.setFormat("date", new SimpleDateFormat("MM/dd/yyyy"));
 messageFormat.setFormat("time", new SimpleDateFormat("h:mm a"));
 Object[] args = { "Hello {0}, your age is {1}. {2} {3}", new Object[] { "John Doe", 25, new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01"), new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-02") } };
 messageFormat.parse(StringUtils.join(args, ", "));
 }
}