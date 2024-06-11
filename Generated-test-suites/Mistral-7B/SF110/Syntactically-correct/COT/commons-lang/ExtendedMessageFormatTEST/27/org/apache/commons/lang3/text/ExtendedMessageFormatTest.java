package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.junit.Test;

public class ExtendedMessageFormatTest {

 private static final MessageFormat STANDARD_FORMAT = new MessageFormat("");
 private static final FormatFactory DUMMY_FORMAT_FACTORY = new FormatFactory() {
 @Override
 public Format getFormat(String name, String argument, Locale locale) {
 throw new UnsupportedOperationException();
 }
 };

 @Test
 public void testEmptyConstructor() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("");
 assertEquals("", formatter.toPattern());
 }

 @Test
 public void testStringConstructor() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}");
 assertEquals("{0}", formatter.toPattern());
 }

 @Test
 public void testLocaleConstructor() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}", Locale.US);
 assertEquals("{0}", formatter.toPattern());
 }

 @Test
 public void testMapConstructor() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}", null, Arrays.asList(new FormatFactory[] { DUMMY_FORMAT_FACTORY }));
 assertEquals("{0}", formatter.toPattern());
 }

 @Test
 public void testLocaleAndMapConstructor() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}", Locale.US, Arrays.asList(new FormatFactory[] { DUMMY_FORMAT_FACTORY }));
 assertEquals("{0}", formatter.toPattern());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}");
 formatter.setFormat(0, new Format() {});
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndex() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}");
 formatter.setFormatByArgumentIndex(0, new Format() {});
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}");
 formatter.setFormats(new Format[] { new Format() {} });
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndex() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}");
 formatter.setFormatsByArgumentIndex(new Format[] { new Format() {} });
 }

 @Test
 public void testApplyPatternWithValidPattern() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0} {1} {2}");
 formatter.applyPattern("{0} ''{1}'' {2}");
 assertEquals("{0} ''{1}'' {2}", formatter.toPattern());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyPatternWithInvalidFormatArgumentIndex() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0} {1} {2}");
 formatter.applyPattern("{4} {1} {2}");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyPatternWithInvalidFormatDescription() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0} {1} {2}");
 formatter.applyPattern("{0} {abc} {2}");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyPatternWithMissingFormat() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0} {1} {2}");
 formatter.applyPattern("{0} {format:'foo'} {2}");
 }

 @Test
 public void testGetFormat() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0} {1}{2}", null, Arrays.asList(new FormatFactory[] { new DummyFormatFactory("foo") }));
 formatter.applyPattern("{0} {1} {2}");
 Format format = formatter.getFormat("foo");
 assertNotNull(format);
 }

 @Test(expected = NullPointerException.class)
 public void testGetFormatWithNullFormatFactory() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0} {1}{2}", null, null);
 formatter.applyPattern("{0} {1} {2}");
 try {
 formatter.getFormat(null);
 } catch (NullPointerException e) {
 throw new AssertionError("Unexpected exception message", e);
 }
 assertEquals("null", e.getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFormatWithInvalidFormatName() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0} ", null, Arrays.asList(new FormatFactory[] { new DummyFormatFactory("foo") }));
 formatter.applyPattern("{0}");
 try {
 formatter.getFormat("bar");
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Unexpected exception message", e);
 }
 assertEquals("Unsupported format name: bar", e.getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFormatWithInvalidArguments() {
 ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0} {1}{2}", null, Arrays.asList(new FormatFactory[] { new DummyFormatFactory("foo") }));
 formatter.applyPattern("{0} {1} {2}");
 try {
 formatter.getFormat("foo", new Object[] { null });
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Unexpected exception message", e);
 }
 assertEquals("Unsupported format name: foo, arguments: null", e.getMessage());
 }
}

class DummyFormatFactory implements FormatFactory {
 private final String name;

 public DummyFormatFactory(String name) {
 this.name = name;
 }

 @Override
 public Format getFormat(String name, String argument, Locale locale) {
 throw new UnsupportedOperationException();
 }
}