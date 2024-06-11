package org.apache.commons.lang.text;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

 // Test constructor with default locale
 @Test
 public void testDefaultConstructor() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1}.");
 assertEquals("Hello {0} {1}.", format.toPattern());
 }

 // Test constructor with custom locale
 @Test
 public void testCustomLocaleConstructor() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1}.", Locale.US);
 assertEquals("Hello {0} {1}.", format.toPattern());
 }

 // Test constructor with custom registry
 @Test
 public void testCustomRegistryConstructor() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("customFormat", new CustomFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {customFormat:FormatName}.", registry);
 assertEquals("Hello {customFormat:FormatName}.", format.toPattern());
 }

 // Test toPattern method
 @Test
 public void testToPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1}.", Locale.US);
 assertEquals("Hello {0} {1}.", format.toPattern());
 }

 // Test applyPattern method with simple pattern
 @Test
 public void testApplyPatternWithSimplePattern() {
 String pattern = "Hello {0} {1}.";
 ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
 format.applyPattern(pattern);
 assertEquals(pattern, format.toPattern());
 }

 // Test applyPattern method with custom format elements
 @Test
 public void testApplyPatternWithCustomFormatElements() {
 String pattern = "Hello {customFormat:FormatName}: {0} {1}.";
 ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US, null);
 FormatFactory registry = new HashMap<String, FormatFactory>() {{
 put("CustomFormat", new CustomFormatFactory());
 }};
 format.applyPattern(pattern);
 assertEquals(pattern, format.toPattern());
 }

 // Test getFormat method
 @Test
 public void testGetFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {customFormat:FormatName}.", Locale.US, null);
 FormatFactory registry = new HashMap<String, FormatFactory>() {{
 put("CustomFormat", new CustomFormatFactory());
 }};
 format.applyPattern("Hello {customFormat:FormatName}.", Locale.US, registry);
 Format customFormat = format.getFormat("CustomFormat");
 assertEquals("CustomFormat", customFormat.getClass().getName());
 }

 // Test setFormat method (UnsupportedOperationException)
 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1}.", Locale.US);
 format.setFormat(0, new StringFormat());
 }

 // Test setFormatByArgumentIndex method (UnsupportedOperationException)
 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1}.", Locale.US);
 format.setFormatByArgumentIndex(1, new StringFormat());
 }

 // Test setFormats method (UnsupportedOperationException)
 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1}.", Locale.US);
 Format[] formats = {new StringFormat(), new StringFormat()};
 format.setFormats(formats);
 }

 // Test setFormatsByArgumentIndex method (UnsupportedOperationException)
 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1}.", Locale.US);
 Format[] formats = {new StringFormat(), new StringFormat()};
 format.setFormatsByArgumentIndex(new Format[0], 0);
 }

 // Test format method
 @Test
 public void testFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1}.", Locale.US);
 String[] args = {"John", "Doe"};
 Object[] objs = format.parse(args);
 assertEquals("Hello John Doe.", objs[0].toString());
 }

 // Test format method with custom format elements
 @Test
 public void testFormatWithCustomFormatElements() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {customFormat:FormatName}: {0} {1}.", Locale.US, null);
 FormatFactory registry = new HashMap<String, FormatFactory>() {{
 put("CustomFormat", new CustomFormatFactory());
 }};
 format.applyPattern("Hello {customFormat:FormatName}: {0} {1}.", Locale.US, registry);
 String[] args = {"John", "Doe"};
 Object[] objs = format.parse(args);
 assertEquals("Hello CustomFormat: John Doe.", objs[0].toString());
 }

 // Test format method with custom format elements and locale
 @Test
 public void testFormatWithCustomFormatElementsAndLocale() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {customFormat:FormatName}: {0} {1}.", Locale.US, null);
 FormatFactory registry = new HashMap<String, FormatFactory>() {{
 put("CustomFormat", new CustomFormatFactory(Locale.FRANCE));
 }};
 format.applyPattern("Hello {customFormat:FormatName}: {0} {1}.", Locale.US, registry);
 String[] args = {"John", "Doe"};
 Object[] objs = format.parse(args);
 assertEquals("Hello CustomFormat: Doe John.", objs[0].toString(), "France locale should reverse arguments order");
 }

 // Test parse method
 @Test
 public void testParse() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1}.", Locale.US);
 String[] args = {"John", "Doe"};
 Object[] objs = format.parse(args);
 assertEquals("Hello John Doe.", objs[0].toString());
 }

 // Test parse method with custom format elements
 @Test
 public void testParseWithCustomFormatElements() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {customFormat:FormatName}: {0} {1}.", Locale.US, null);
 FormatFactory registry = new HashMap<String, FormatFactory>() {{
 put("CustomFormat", new CustomFormatFactory());
 }};
 format.applyPattern("Hello {customFormat:FormatName}: {0} {1}.", Locale.US, registry);
 String[] args = {"John", "Doe"};
 Object[] objs = format.parse(args);
 assertEquals("Hello CustomFormat: John Doe.", objs[0].toString());
 }

 // Test parse method with invalid format elements
 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidFormatElements() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {customFormat:FormatName}.", Locale.US, null);
 FormatFactory registry = new HashMap<String, FormatFactory>() {{
 put("CustomFormat", new CustomFormatFactory());
 }};
 String[] args = {"John", "Doe"};
 format.applyPattern("Hello {customFormat:FormatName}.", Locale.US, registry);
 Object[] objs = format.parse(args);
 // This test should throw an IllegalArgumentException due to the missing format element
 fail("An IllegalArgumentException should have been thrown.");
 }
}

class CustomFormatFactory implements FormatFactory {
 Format getFormat(String formatName, String formatStyle, Locale locale) {
 return new StringFormat(formatName);
 }
}

class StringFormat implements Format {
 @Override
 public StringBuffer format(Object object, StringBuffer toappend, FieldPosition pos) {
 toappend.append(object.toString());
 return toappend;
 }

 @Override
 public Object parseObject(String source, ParsePosition pos) {
 return source;
 }
}