package org.apache.commons.lang3.text;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

 @Test
 public void testEmptyPattern() {
 ExtendedMessageFormat emptyFormat = new ExtendedMessageFormat("");
 assertEquals("", emptyFormat.toPattern());
 }

 @Test
 public void testDefaultLocalePattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
 assertEquals("Hello {0}, your age is {1}", format.toPattern());
 }

 @Test
 public void testCustomLocalePattern() {
 Locale frLocale = new Locale("fr", "FR");
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, votre âge est {1}.", frLocale);
 assertEquals("Hello {0}, votre âge est {1}", format.toPattern());
 }

 @Test
 public void testCustomFormatRegistry() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new SimpleDateFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("Today is {date, date, MMMM dd, ''yyyy''}.", null, registry);
 assertEquals("Today is January 01, ''yyyy''.", format.toPattern());
 }

 @Test
 public void testInvalidArgumentIndex() {
 try {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {999, number, integer}.", null);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid format argument index: 999", e.getMessage());
 }
 }

 @Test
 public void testNullFormatDescription() {
 try {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {}.", null);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid format description", e.getMessage());
 }
 }

 @Test
 public void testMissingFormatFactory() {
 Map<String, FormatFactory> registry = new HashMap<>();
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {date, date}.", null, registry);
 try {
 format.applyPattern("Hello {date, date, MMMM dd, ''yyyy''}.");
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("FormatFactory for 'date' not found", e.getMessage());
 }
 }

 @Test
 public void testFormatFactoryError() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new FormatFactory() {
 @Override
 public Format getFormat(String name, String argument, Locale locale) throws ParseException {
 throw new ParseException("Test error");
 }
 });
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {date, date}.", null, registry);
 format.applyPattern("Hello {date, date}.");
 }

 @Test
 public void testFormatApplication() throws ParseException {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.", null);
 Object[] args = {"John", 30};
 Object result = format.format(args);
 assertEquals("Hello John, your age is 30.", result);
 }

 @Test
 public void testFormatApplicationWithLocale() throws ParseException {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.", Locale.FRENCH);
 Object[] args = {"Jean", 30};
 Object result = format.format(args);
 assertEquals("Hello Jean, votre âge est 30.", result);
 }

 @Test
 public void testFormatApplicationWithRegistry() throws ParseException {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new SimpleDateFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("Today is {date, date, MMMM dd, ''yyyy''}.", null, registry);
 Object[] args = {new Date()};
 Object result = format.format(args);
 assertEquals("Today is January 01, ''yyyy''.", result);
 }

 @Test
 public void testFormatApplicationWithCustomPattern() throws ParseException {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Today is {date, date, MMMM dd, ''yyyy''}.", null);
 Object[] args = {new Date()};
 Object result = format.format(args);
 assertEquals("Today is January 01, ''yyyy''.", result);
 format.applyPattern("Today is {date, date, MMMM dd, yyyy}.");
 Object result2 = format.format(args);
 assertEquals("Today is January 01, 2023.", result2);
 }

 @Test
 public void testFormatApplicationWithEscapedQuotes() throws ParseException {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} says ''{1}''.", null);
 Object[] args = {"John", "Hello"};
 Object result = format.format(args);
 assertEquals("Hello John says ''Hello''.", result);
 }

 @Test
 public void testFormatApplicationWithComplexPattern() throws ParseException {
 ExtendedMessageFormat format = new ExtendedMessageFormat("There are {0} apples and {1} oranges.", null);
 Object[] args = {5, 3};
 Object result = format.format(args);
 assertEquals("There are 5 apples and 3 oranges.", result);
 }

 @Test
 public void testFormatApplicationWithError() throws ParseException {
 ExtendedMessageFormat format = new ExtendedMessageFormat("There are {0} apples and {1} oranges.", null);
 Object[] args = {5, 5};
 try {
 Object result = format.format(args);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid format argument index: 1", e.getMessage());
 }
 }

}