package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

 private static class MyFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String name, String pattern, Locale locale) {
 throw new UnsupportedOperationException("Not implemented");
 }
 }

 @org.junit.Test
 public void testEmptyPattern() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("");
 assertEquals("", fmt.toPattern());
 }

 @org.junit.Test
 public void testSimplePattern() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}");
 assertEquals("{0} {1}", fmt.toPattern());
 }

 @org.junit.Test
 public void testCustomFormat() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,date,MM-dd-yyyy} {1,date,MMM dd, yyyy}");
 assertEquals("{0,date,MM-dd-yyyy} {1,date,MMM dd, yyyy}", fmt.toPattern());
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testInvalidFormat() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,invalid}");
 fail("Should throw IllegalArgumentException");
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testMissingFormat() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,format.MissingFormat}");
 fail("Should throw IllegalArgumentException");
 }

 @org.junit.Test
 public void testFormatWithFactory() {
 Map<String, FormatFactory> registry = new HashMap<String, FormatFactory>();
 registry.put("customFormat", new MyFormatFactory());
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,customFormat}", locale, registry);
 assertEquals("{0,customFormat}", fmt.toPattern());
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testMisspelledFormat() {
 Map<String, FormatFactory> registry = new HashMap<String, FormatFactory>();
 registry.put("customFormat", new MyFormatFactory());
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,customFormatt}", locale, registry);
 fail("Should throw IllegalArgumentException");
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testMissingArgument() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{1,date,MM-dd-yyyy}");
 Object[] args = new Object[0];
 try {
 fmt.format(args);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @org.junit.Test(expected = ParseException.class)
 public void testInvalidFormatArgument() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,format.InvalidFormat} {1,date,MM-dd-yyyy}");
 Object[] args = new Object[]{"InvalidFormat", Locale.getDefault()};
 try {
 fmt.format(args);
 fail("Should throw ParseException");
 } catch (ParseException e) {
 // expected
 }
 }
}