package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;

public class ExtendedMessageFormatTest {

 private static class DummyFormatFactory implements FormatFactory {
  private final String name;

  public DummyFormatFactory(String name) {
    this.name = name;
  }

  @Override
  public Format getFormat(String name, String arguments, Locale locale) {
    return new DummyFormat();
  }
 }

 private static class DummyFormat implements Format {
  @Override
  public String format(Object arg0, Locale locale) throws IllegalArgumentException, NullPointerException, ParseException {
    return "DummyFormat: " + arg0;
  }

  @Override
  public String parameterName() {
    return "dummy";
  }
 }

 @org.junit.Test
 public void testExtendedMessageFormat_DefaultConstructor() {
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("");
   assertNull(formatter.getFormat(0));
   assertNull(formatter.getFormatByArgumentIndex(0));
   assertArrayEquals(new Format[0], formatter.getFormats());
   assertArrayEquals(new Format[0], formatter.getFormatsByArgumentIndex(0));
   assertEquals("", formatter.toPattern());
 }

 @org.junit.Test
 public void testExtendedMessageFormat_ConstructorWithPattern() {
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}");
   assertNull(formatter.getFormat(0));
   assertNull(formatter.getFormatByArgumentIndex(0));
   assertArrayEquals(new Format[0], formatter.getFormats());
   assertArrayEquals(new Format[0], formatter.getFormatsByArgumentIndex(0));
   assertEquals("{0}", formatter.toPattern());
 }

 @org.junit.Test
 public void testExtendedMessageFormat_ConstructorWithLocale() {
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}", Locale.US);
   assertNull(formatter.getFormat(0));
   assertNull(formatter.getFormatByArgumentIndex(0));
   assertArrayEquals(new Format[0], formatter.getFormats());
   assertArrayEquals(new Format[0], formatter.getFormatsByArgumentIndex(0));
   assertEquals("{0}", formatter.toPattern());
 }

 @org.junit.Test
 public void testExtendedMessageFormat_ConstructorWithRegistry() {
   Map<String, FormatFactory> registry = new HashMap<>();
   registry.put("dummy", new DummyFormatFactory("dummy"));
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("{dummy}", registry);
   Format format = formatter.getFormat(0);
   assertNotNull(format);
   assertEquals("dummy", format.parameterName());
   assertEquals("DummyFormat: null", format.format(null, Locale.getDefault()));
 }

 @org.junit.Test
 public void testExtendedMessageFormat_ConstructorWithPatternAndRegistry() {
   Map<String, FormatFactory> registry = new HashMap<>();
   registry.put("dummy", new DummyFormatFactory("dummy"));
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("{dummy}", Locale.US, registry);
   Format format = formatter.getFormat(0);
   assertNotNull(format);
   assertEquals("dummy", format.parameterName());
   assertEquals("DummyFormat: null", format.format(null, Locale.US));
 }

 @org.junit.Test
 public void testApplyPattern() throws ParseException {
   Map<String, FormatFactory> registry = new HashMap<>();
   registry.put("dummy", new DummyFormatFactory("dummy"));
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}", Locale.US, registry);
   String pattern = "{0,date,short,MMMM dd,yyyy} {1} {2,number,integer,#,0}";
   formatter.applyPattern(pattern);
   Format[] formats = formatter.getFormats();
   Format format0 = formats[0];
   Format format1 = formats[1];
   Format format2 = formats[2];
   assertEquals("dd.MM.yyyy", format0.parameterName());
   assertEquals("date", format1.parameterName());
   assertEquals("number", format2.parameterName());
   assertEquals("{0,date,short,MMMM dd,yyyy}", formatter.toPattern());
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
   Map<String, FormatFactory> registry = new HashMap<>();
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}", Locale.US, registry);
   formatter.setFormat(0, new StringFormat("%s"));
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndex() {
   Map<String, FormatFactory> registry = new HashMap<>();
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}", Locale.US, registry);
   formatter.setFormatByArgumentIndex(0, new StringFormat("%s"));
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
   Map<String, FormatFactory> registry = new HashMap<>();
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}", Locale.US, registry);
   Format[] formats = new Format[2];
   formats[0] = new StringFormat("%s");
   formats[1] = new StringFormat("%d");
   formatter.setFormats(formats);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndex() {
   Map<String, FormatFactory> registry = new HashMap<>();
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}", Locale.US, registry);
   Format[] formats = new Format[2];
   formats[0] = new StringFormat("%s");
   formats[1] = new StringFormat("%d");
   formatter.setFormatsByArgumentIndex(formats);
 }

 @org.junit.Test
 public void testEquals() {
   Map<String, FormatFactory> registry1 = new HashMap<>();
   Map<String, FormatFactory> registry2 = new HashMap<>();
   registry1.put("dummy1", new DummyFormatFactory("dummy1"));
   registry2.put("dummy2", new DummyFormatFactory("dummy2"));
   ExtendedMessageFormat formatter1 = new ExtendedMessageFormat("{0}", Locale.US, registry1);
   ExtendedMessageFormat formatter2 = new ExtendedMessageFormat("{0}", Locale.US, registry2);
   assertFalse(formatter1.equals(formatter2));
   ExtendedMessageFormat formatter3 = new ExtendedMessageFormat("{0}", Locale.US, registry1);
   assertTrue(formatter1.equals(formatter3));
 }

 @org.junit.Test
 public void testHashCode() {
   Map<String, FormatFactory> registry1 = new HashMap<>();
   Map<String, FormatFactory> registry2 = new HashMap<>();
   registry1.put("dummy1", new DummyFormatFactory("dummy1"));
   registry2.put("dummy2", new DummyFormatFactory("dummy2"));
   ExtendedMessageFormat formatter1 = new ExtendedMessageFormat("{0}", Locale.US, registry1);
   ExtendedMessageFormat formatter2 = new ExtendedMessageFormat("{0}", Locale.US, registry2);
   assertNotEquals(formatter1.hashCode(), formatter2.hashCode());
   ExtendedMessageFormat formatter3 = new ExtendedMessageFormat("{0}", Locale.US, registry1);
   assertEquals(formatter1.hashCode(), formatter3.hashCode());
 }

 @org.junit.Test
 public void testGetFormat() {
   Map<String, FormatFactory> registry = new HashMap<>();
   registry.put("dummy", new DummyFormatFactory("dummy"));
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("{dummy}", Locale.US, registry);
   Format format = formatter.getFormat(0);
   assertEquals("dummy", format.parameterName());
   assertEquals("DummyFormat: null", format.format(null, Locale.US));
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testGetFormat_InvalidFormatIndex() {
   Map<String, FormatFactory> registry = new HashMap<>();
   registry.put("dummy", new DummyFormatFactory("dummy"));
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("{dummy}", Locale.US, registry);
   formatter.getFormat(1);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testGetFormatByArgumentIndex_InvalidArgumentIndex() {
   Map<String, FormatFactory> registry = new HashMap<>();
   registry.put("dummy", new DummyFormatFactory("dummy"));
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("{dummy}", Locale.US, registry);
   formatter.getFormatByArgumentIndex(1);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testFormat() throws ParseException {
   Map<String, FormatFactory> registry = new HashMap<>();
   registry.put("dummy", new DummyFormatFactory("dummy"));
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("{dummy}", Locale.US, registry);
   Object[] arguments = new Object[1];
   arguments[0] = "test";
   String formatted = formatter.format(arguments);
   assertNotNull(formatted);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testFormat_NullArguments() throws ParseException {
   Map<String, FormatFactory> registry = new HashMap<>();
   registry.put("dummy", new DummyFormatFactory("dummy"));
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("{dummy}", Locale.US, registry);
   Object[] arguments = new Object[1];
   arguments[0] = null;
   formatter.format(arguments);
 }

 @org.junit.Test
 public void testFormat_MultipleArguments() throws ParseException {
   Map<String, FormatFactory> registry = new HashMap<>();
   registry.put("dummy1", new DummyFormatFactory("dummy1"));
   registry.put("dummy2", new DummyFormatFactory("dummy2"));
   ExtendedMessageFormat formatter = new ExtendedMessageFormat("{dummy1} {dummy2}", Locale.US, registry);
   Object[] arguments = new Object[2];
   arguments[0] = "test1";
   arguments[1] = "test2";
   String formatted = formatter.format(arguments);
   assertNotNull(formatted);
   assertTrue(formatted.startsWith("test1 test2"));
 }

}