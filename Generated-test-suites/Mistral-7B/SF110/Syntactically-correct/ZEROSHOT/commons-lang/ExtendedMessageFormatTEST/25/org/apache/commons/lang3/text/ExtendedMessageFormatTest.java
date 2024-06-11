package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;

public class ExtendedMessageFormatTest {

 private FormatFactory formatFactory;
 private ExtendedMessageFormat messageFormat;

 @Before
 public void setup() {
 final MessageFormatSampleFormatFactory factory = new MessageFormatSampleFormatFactory();
 formatFactory = factory;
 }

 @Test
 public void testExtendedMessageFormatConstructorWithPattern() {
 final String pattern = "Hello, {0}!";
 messageFormat = new ExtendedMessageFormat(pattern);
 assertEquals(pattern, messageFormat.toPattern());
 }

 @Test
 public void testExtendedMessageFormatConstructorWithPatternAndLocale() {
 final String pattern = "Hello, {0}!";
 messageFormat = new ExtendedMessageFormat(pattern, Locale.US);
 assertEquals(pattern, messageFormat.toPattern());
 }

 @Test
 public void testExtendedMessageFormatConstructorWithPatternAndRegistry() {
 final String pattern = "Hello, {format.intFormat, #, number, ##0}!";
 messageFormat = new ExtendedMessageFormat(pattern, null, Collections.singletonMap("format.intFormat", formatFactory));
 assertEquals(pattern, messageFormat.toPattern());
 }

 @Test
 public void testExtendedMessageFormatConstructorWithPatternLocaleAndRegistry() {
 final String pattern = "Hello, {format.intFormat, #, number, ##0}!";
 messageFormat = new ExtendedMessageFormat(pattern, Locale.US, Collections.singletonMap("format.intFormat", formatFactory));
 assertEquals(pattern, messageFormat.toPattern());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatUnsupported() {
 messageFormat.setFormat(0, new MessageFormat.Format("Integer", null, null, null));
 fail("UnsupportedOperationException not thrown");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndexUnsupported() {
 messageFormat.setFormatByArgumentIndex(0, new MessageFormat.Format("Integer", null, null, null));
 fail("UnsupportedOperationException not thrown");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsUnsupported() {
 final MessageFormat.Format[] formats = new MessageFormat.Format[2];
 formats[0] = new MessageFormat.Format("Integer", null, null, null);
 formats[1] = new MessageFormat.Format("Date", null, null, null);
 messageFormat.setFormats(formats);
 fail("UnsupportedOperationException not thrown");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndexUnsupported() {
 final MessageFormat.Format[] formats = new MessageFormat.Format[2];
 formats[0] = new MessageFormat.Format("Integer", null, null, null);
 formats[1] = new MessageFormat.Format("Date", null, null, null);
 messageFormat.setFormatsByArgumentIndex(formats);
 fail("UnsupportedOperationException not thrown");
 }

 @Test
 public void testEquals() {
 final ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello, {0}!", null, Collections.singletonMap("format.intFormat", formatFactory));
 final ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello, {0}!", null, Collections.singletonMap("format.intFormat", formatFactory));
 assertTrue(messageFormat1.equals(messageFormat2));
 }

 @Test
 public void testHashcode() {
 final ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello, {0}!", null, Collections.singletonMap("format.intFormat", formatFactory));
 final ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello, {1}!", null, Collections.singletonMap("format.intFormat", formatFactory));
 assertNotEquals(messageFormat1.hashCode(), messageFormat2.hashCode());
 }

 @Test
 public void testGetFormat() {
 final String formatDescription = "format.intFormat";
 final Format expectedFormat = new DecimalFormat("#,###");
 messageFormat.applyPattern("Hello, {format.intFormat, number, ##0}!");
 assertEquals(expectedFormat, messageFormat.getFormat(formatDescription));
 }

 @Test
 public void testGetFormatNull() {
 messageFormat.applyPattern("Hello, {nonExistentFormat, number, ##0}!");
 assertNull(messageFormat.getFormat("nonExistentFormat"));
 }

 @Test
 public void testApplyPattern() {
 final String pattern = "Hello, {0}! Your number is: {format.intFormat, number, ##0}";
 messageFormat.applyPattern(pattern);
 final Object[] arguments = new Object[]{"John", 123};
 final String expected = "Hello, John! Your number is: 123";
 assertEquals(expected, messageFormat.format(arguments));
 }

 @Test
 public void testApplyPatternWithRegistry() {
 final String pattern = "Hello, {format.intFormat, number, ##0}! Your number is: {format.dateFormat, date, medium}";
 messageFormat.applyPattern(pattern);
 final Object[] arguments = new Object[]{formatFactory, 123, new Date()};
 final String expected = "Hello, 123! Your number is: Mon Jan 30 10:15:33 EST 2023";
 assertEquals(expected, messageFormat.format(arguments));
 }

 @Test
 public void testApplyPatternWithRegistryNullArgument() {
 final String pattern = "Hello, {format.intFormat, number, ##0}! Your number is: {format.dateFormat, date, medium}";
 messageFormat.applyPattern(pattern);
 final Object[] arguments = new Object[]{null, 123, new Date()};
 final String expected = "Hello, 123! Your number is: Mon Jan 30 10:15:33 EST 2023";
 assertEquals(expected, messageFormat.format(arguments));
 }
}