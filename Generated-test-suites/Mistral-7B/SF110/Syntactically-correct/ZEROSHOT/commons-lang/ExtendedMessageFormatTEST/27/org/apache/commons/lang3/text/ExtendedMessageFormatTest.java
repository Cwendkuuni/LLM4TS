package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.junit.*;

public class ExtendedMessageFormatTest {

 private static class TestFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String name, String arguments, Locale locale) {
 throw new UnsupportedOperationException();
 }
 }

 @Test
 public void testExtendedMessageFormatConstruction() {
 // Test default constructor
 ExtendedMessageFormat format = new ExtendedMessageFormat("");
 assertTrue(format.getRegistry() == null);
 // Test constructor with pattern
 ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0}, {1}");
 assertNotNull(format1.getRegistry());
 assertEquals(2, format1.getFormats().length);
 // Test constructor with pattern and locale
 ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0}, {1}", Locale.US);
 assertEquals(Locale.US, format2.getLocale());
 // Test constructor with pattern, locale and registry
 ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0}, {1}", Locale.US, new HashMap<String, FormatFactory>() {{
 put("int", new TestFormatFactory());
 }});
 assertNotNull(format3.getRegistry());
 assertEquals(Locale.US, format3.getLocale());
 }

 @Test
 public void testToPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
 assertEquals("{0}, {1}", format.toPattern());
 }

 @Test
 public void testApplyPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
 format.applyPattern("{1}, {0}");
 assertEquals("{1}, {0}", format.toPattern());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyPatternInvalidFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
 format.applyPattern("{invalid}");
 }

 @Test
 public void testSetFormat() {
 try {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
 format.setFormat(0, new DecimalFormat("0.##"));
 fail();
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testSetFormatByArgumentIndex() {
 try {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
 format.setFormatByArgumentIndex(0, new DecimalFormat("0.##"));
 fail();
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", new HashMap<String, FormatFactory>() {{
 put("int", new TestFormatFactory());
 }});
 format.applyPattern("{int:0.##}, {int:0.##}");
 assertEquals("0.0, 0.0", format.format(new Object[]{1.0, 2.0}));
 }

 @Test
 public void testFormatWithCustomPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{customPattern:test, int:0.##}", new HashMap<String, FormatFactory>() {{
 put("int", new DecimalFormat("0.##"));
 put("customPattern", new TestCustomFormatFactory());
 }});
 format.applyPattern("{customPattern:test}, {int:0.##}");
 Object[] args = new Object[]{"test1", 1.0};
 assertEquals("test1, 1.0", format.format(args));
 }

 @Test
 public void testFormatWithCustomPatternAndArguments() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{customPattern:test, int:0.##}", new HashMap<String, FormatFactory>() {{
 put("int", new DecimalFormat("0.##"));
 put("customPattern", new TestCustomFormatFactory());
 }});
 format.applyPattern("{customPattern:test}, {int:0.##}");
 Object[] args = new Object[]{"test1", new Integer(1)};
 assertEquals("test1, 1.0", format.format(args));
 }

 @Test
 public void testHashCodeAndEquals() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0}, {1}");
 ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0}, {1}");
 assertTrue(format1.hashCode() == format2.hashCode());
 assertTrue(format1.equals(format2));
 // Test different pattern
 ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0}, {2}");
 assertFalse(format1.equals(format3));
 // Test different registry
 ExtendedMessageFormat format4 = new ExtendedMessageFormat("{0}, {1}", new HashMap<String, FormatFactory>() {{
 put("int", new TestFormatFactory());
 }});
 assertFalse(format1.equals(format4));
 // Test different locale
 ExtendedMessageFormat format5 = new ExtendedMessageFormat("{0}, {1}", Locale.US);
 assertFalse(format1.equals(format5));
 }

 private static class TestCustomFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String name, String arguments, Locale locale) {
 return new MessageFormat("customPattern", locale);
 }
 }
}