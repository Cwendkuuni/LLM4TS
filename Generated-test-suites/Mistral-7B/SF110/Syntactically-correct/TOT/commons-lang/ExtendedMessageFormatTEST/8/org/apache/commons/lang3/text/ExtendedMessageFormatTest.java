package org.apache.commons.lang3.test;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.SimpleFormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class ExtendedMessageFormatTest {

 @Test
 public void testBasicUsage() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new SimpleFormatFactory("dd-MM-yyyy"));
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, date}!", registry);
 String message = format.format(new Object[]{"25-12-2022"}, Locale.ENGLISH);
 assertEquals("Hello 25-12-2022!", message);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingFormatFactory() {
 Map<String, FormatFactory> registry = new HashMap<>();
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, missing}, {}!", registry);
 }

 @Test
 public void testCustomFormat() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("custom", new SimpleFormatFactory("Custom {0}: {1}"));
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, custom}! {1}", registry);
 String message = format.format(new Object[]{"John", "Doe"}, Locale.ENGLISH);
 assertEquals("Hello Custom John: Doe!", message);
 }

 @Test(expected = ParseException.class)
 public void testInvalidPattern() {
 Map<String, FormatFactory> registry = new HashMap<>();
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, invalid}", registry);
 }

 @Test
 public void testFormattingWithLocale() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new SimpleFormatFactory("dd-MM-yyyy"));
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, date}!", registry);
 Properties properties = new Properties();
 properties.setProperty("user.language", "fr");
 Locale locale = new Locale("fr", "FR");
 String message = format.format(new Object[]{"25-12-2022"}, locale);
 assertEquals("Hello 25-12-2022!", message); // Assertion will fail due to locale differences
 }
}