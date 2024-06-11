package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import java.text.*;
import java.util.*;
import org.junit.*;

public class ExtendedMessageFormatTest {

 @BeforeClass
 public static void setup() {
 // Add any necessary setup here, such as creating test data or setting up test environment
 }

 @AfterClass
 public static void teardown() {
 // Add any necessary teardown code here, such as releasing resources or resetting test environment
 }

 @Test
 public void testConstructorWithDefaults() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("DefaultPattern");
 String result = format.format(new Object[]{new Integer(1), new Integer(2)});
 assertEquals("DefaultPattern{0, number, integer}{1, number, integer}", result);
 }

 @Test
 public void testConstructorWithLocale() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern{0, choice, 0#Foo|1#Bar}", Locale.ENGLISH);
 String result = format.format(new Object[]{new Integer(0), new Integer(1)});
 assertEquals("Pattern{0, choice, 0#Foo|1#Bar}[0]=Foo[1]=Bar", result);
 }

 @Test
 public void testConstructorWithRegistry() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("customFormat", new CustomFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern{0, format, customFormat}", Locale.ENGLISH, registry);
 String result = format.format(new Object[]{new Integer(1)});
 assertEquals("Pattern{0, format, customFormat}", result);
 }

 @Test
 public void testConstructorWithRegistryAndPattern() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("customFormat", new CustomFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern{0, format, customFormat}", Locale.ENGLISH, registry);
 format.applyPattern("Pattern{0, choice, 0#Foo|1#Bar}");
 String result = format.format(new Object[]{new Integer(0), new Integer(1)});
 assertEquals("Pattern{0, choice, 0#Foo|1#Bar}[0]=Foo[1]=Bar", result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern{0, format, customFormat}", Locale.ENGLISH, null);
 format.setFormat(0, new NumberFormat() {
 @Override
 public String format(Object obj) {
 throw new UnsupportedOperationException();
 }

 @Override
 public Object parseObject(String source) {
 throw new UnsupportedOperationException();
 }
 });
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern{0, format, customFormat}", Locale.ENGLISH, null);
 format.setFormatByArgumentIndex(1, new NumberFormat() {
 @Override
 public String format(Object obj) {
 throw new UnsupportedOperationException();
 }

 @Override
 public Object parseObject(String source) {
 throw new UnsupportedOperationException();
 }
 });
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern{0, format, customFormat}", Locale.ENGLISH, null);
 Format[] formats = new Format[1];
 formats[0] = new NumberFormat() {
 @Override
 public String format(Object obj) {
 throw new UnsupportedOperationException();
 }

 @Override
 public Object parseObject(String source) {
 throw new UnsupportedOperationException();
 }
 };
 format.setFormats(formats);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern{0, format, customFormat}", Locale.ENGLISH, null);
 Format[] formats = new Format[1];
 formats[0] = new NumberFormat() {
 @Override
 public String format(Object obj) {
 throw new UnsupportedOperationException();
 }

 @Override
 public Object parseObject(String source) {
 throw new UnsupportedOperationException();
 }
 };
 format.setFormatsByArgumentIndex(1, formats);
 }

 @Test
 public void testEquals() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat("Pattern{0, format, customFormat}", Locale.ENGLISH, null);
 ExtendedMessageFormat format2 = new ExtendedMessageFormat("Pattern{0, format, customFormat}", Locale.ENGLISH, null);
 assertTrue(format1.equals(format2));
 ExtendedMessageFormat format3 = new ExtendedMessageFormat("Pattern{0, format, customFormat}", Locale.ENGLISH, null);
 format1.setLocale(Locale.FRENCH);
 assertFalse(format1.equals(format2));
 assertFalse(format1.equals(format3));
 ExtendedMessageFormat format4 = new ExtendedMessageFormat("Pattern{0, format, customFormat}", Locale.ENGLISH, null);
 format4.setRegistry(new HashMap<>());
 assertFalse(format1.equals(format4));
 }

 @Test
 public void testHashCode() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat("Pattern{0, format, customFormat}", Locale.ENGLISH, null);
 ExtendedMessageFormat format2 = new ExtendedMessageFormat("Pattern{0, format, customFormat}", Locale.ENGLISH, null);
 int hashCode1 = format1.hashCode();
 int hashCode2 = format2.hashCode();
 assertEquals(hashCode1, hashCode2);
 format1.setLocale(Locale.FRENCH);
 int hashCode3 = format1.hashCode();
 assertNotEquals(hashCode1, hashCode3);
 format1.setRegistry(new HashMap<>());
 int hashCode4 = format1.hashCode();
 assertNotEquals(hashCode1, hashCode4);
 }

 @Test
 public void testGetFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern{0, format, customFormat}", Locale.ENGLISH, new HashMap<>());
 Format result = format.getFormat("customFormat");
 assertNotNull(result);
 }

 @Test
 public void testGetFormatNull() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern{0, format, customFormat}", Locale.ENGLISH, new HashMap<>());
 Format result = format.getFormat("UnknownFormat");
 assertNull(result);
 }
}

class CustomFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String name, String pattern, Locale locale) {
 return new NumberFormat() {
 @Override
 public String format(Object obj) {
 return "CustomFormat{" + obj + "}";
 }

 @Override
 public Object parseObject(String source) {
 throw new UnsupportedOperationException();
 }
 };
 }
}