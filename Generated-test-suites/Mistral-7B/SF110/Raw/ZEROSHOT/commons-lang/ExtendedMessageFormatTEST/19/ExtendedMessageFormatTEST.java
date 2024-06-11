package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;

public class ExtendedMessageFormatTest {

 private static FormatFactory getFormatFactory() {
 return new FormatFactory() {
 @Override
 public Format getFormat(String name, String arguments, Locale locale) {
 throw new UnsupportedOperationException();
 }
 };
}

 @Test
 public void testEmptyConstructor() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("");
 assertNull(format.getFormat(0));
 }

 @Test
 public void testBasicConstructor() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, world!");
 assertEquals("Hello {0}, world!", format.toPattern());
 assertNull(format.getFormat(0));
 }

 @Test
 public void testLocaleConstructor() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, {1}!", Locale.US);
 assertEquals("Hello {0}, {1}!", format.toPattern());
 assertNull(format.getFormat(0));
 assertNull(format.getFormat(1));
 }

 @Test
 public void testMapConstructor() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", getFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("Now is {0}, and the date is {1}", registry);
 assertEquals("Now is {0}, and the date is {1}", format.toPattern());
 Format dateFormat = format.getFormat(0);
 assertNotNull(dateFormat);
 assertInstanceOf(DateFormat.class, dateFormat);
 Format dateFormat1 = format.getFormat(1);
 assertNotNull(dateFormat1);
 assertInstanceOf(DateFormat.class, dateFormat1);
 }

 @Test
 public void testApplyPattern() {
 String pattern = "Hello {0}, your age is {1}, and the date is {2}";
 ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", getFormatFactory());
 format.applyPattern(pattern, registry);
 assertEquals("Hello {0}, your age is {1}, and the date is {2}", format.toPattern());
 Format dateFormat = format.getFormat(2);
 assertNotNull(dateFormat);
 assertInstanceOf(DateFormat.class, dateFormat);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
 format.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
 format.setFormatByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
 Format[] formats = new Format[1];
 formats[0] = new SimpleDateFormat("yyyy-MM-dd");
 format.setFormats(formats);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
 Format[] formats = new Format[1];
 formats[0] = new SimpleDateFormat("yyyy-MM-dd");
 format.setFormatsByArgumentIndex(formats);
 }

 @Test
 public void testEquals() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}");
 ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}");
 assertTrue(format1.equals(format2));
 ExtendedMessageFormat format3 = new ExtendedMessageFormat("Hello {1}");
 assertFalse(format1.equals(format3));
 }

 @Test
 public void testHashCode() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}");
 ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}");
 assertEquals(format1.hashCode(), format2.hashCode());
 ExtendedMessageFormat format3 = new ExtendedMessageFormat("Hello {1}");
 assertNotEquals(format1.hashCode(), format3.hashCode());
 }

}