package org.apache.commons.lang3.text;

import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExtendedMessageFormatTest {

 @Test
 public void testEmptyPattern() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat(DUMMY_PATTERN);
 assertEquals("", fmt.toPattern());
 }

 @Test
 public void testBasicPattern() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}");
 assertEquals("{0} {1}", fmt.toPattern());
 }

 @Test
 public void testBasicFormatWithArgument() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}");
 fmt.applyPattern("{0} {1}");
 String result = fmt.format(new Object[]{"Hello", "World"});
 assertEquals("Hello World", result);
 }

 @Test
 public void testFormatWithCustomFormat() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,date,\"dd-MM-yyyy\"}");
 fmt.applyPattern("{0,date,\"dd-MM-yyyy\"}");
 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
 Date date = new Date();
 String result = fmt.format(new Object[]{date});
 assertEquals(dateFormat.format(date), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormat() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}");
 fmt.applyPattern("{0} {1}{2}");
 }

 @Test
 public void testCustomFormatWithRegistry() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", (FormatFactory)new SimpleDateFormat("dd-MM-yyyy"));
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,date,\"dd-MM-yyyy\"}", null, registry);
 fmt.applyPattern("{0,date,\"dd-MM-yyyy\"}");
 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
 Date date = new Date();
 String result = fmt.format(new Object[]{date});
 assertEquals(dateFormat.format(date), result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}");
 fmt.setFormat(0, new SimpleDateFormat("dd-MM-yyyy"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndex() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}");
 fmt.setFormatByArgumentIndex(0, new SimpleDateFormat("dd-MM-yyyy"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}");
 Format[] formats = new Format[]{new SimpleDateFormat("dd-MM-yyyy"), new SimpleDateFormat("dd-MM-yyyy")};
 fmt.setFormats(formats);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndex() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}");
 Format[] formats = new Format[]{new SimpleDateFormat("dd-MM-yyyy"), new SimpleDateFormat("dd-MM-yyyy")};
 fmt.setFormatsByArgumentIndex(formats);
 }

 @Test
 public void testEquals() {
 ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0} {1}");
 ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0} {1}");
 assertEquals(fmt1, fmt2);
 }

 @Test
 public void testHashCode() {
 ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0} {1}");
 ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0} {1}");
 int hashCode1 = fmt1.hashCode();
 int hashCode2 = fmt2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}");
 String toString = fmt.toString();
 assertTrue(toString.contains("ExtendedMessageFormat"));
 }

}