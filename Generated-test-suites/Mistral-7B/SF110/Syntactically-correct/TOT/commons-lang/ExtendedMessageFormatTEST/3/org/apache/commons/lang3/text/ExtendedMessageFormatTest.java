package org.apache.commons.lang3.text;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingFormatArgumentException;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

 @Test
 public void testBasicUsage() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}! Your age is {1}.");
 Validate.isTrue(format.toPattern().equals("Hello, {0}! Your age is {1}."));
 String[] args = {"John Doe", "25"};
 String result = format.format(args);
 assertEquals("Hello, John Doe! Your age is 25.", result);
 }

 @Test
 public void testSingleFormatRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}! Your age is {1,date,short}.");
 Validate.isTrue(format.toPattern().equals("Hello, {0}! Your age is {1,date,short}."));
 format.setLocale(Locale.US);
 String[] args = {"John Doe", "01-Jan-2022"};
 String result = format.format(args);
 assertEquals("Hello, John Doe! Your age is Jan 01, 2022.", result);
 }

 @Test
 public void testMultipleFormatRegistry() {
 Formatter formatter = new Formatter();
 HashMap<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new SimpleDateFormat("EEE, MMM dd, ''yyyy''"));
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}! Your birthday is {1,date}.", registry);
 Validate.isTrue(format.toPattern().equals("Hello, {0}! Your birthday is {1,date}."));
 String[] args = {"John Doe", "01-Jan-2022"};
 String result = format.format(args);
 assertEquals("Hello, John Doe! Your birthday is Mon, Jan 01, 2022.", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingFormatInRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}! Your height is {1,invalid}.", new HashMap<>());
 String[] args = {"John Doe", "5'9\""};
 try {
 format.format(args);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Unreadable format element at position 1", e.getMessage());
 }
 }

 @Test(expected = MissingFormatArgumentException.class)
 public void testMissingArgument() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}! Your height is {1}.");
 String[] args = {"John Doe"};
 try {
 format.format(args);
 fail();
 } catch (MissingFormatArgumentException e) {
 assertEquals("Format specifier '{1}' not found in message pattern", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatArgument() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}! Your height is {1,invalid}.", new HashMap<>());
 String[] args = {"John Doe", "invalid"};
 try {
 format.format(args);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid format argument at position 1: invalid", e.getMessage());
 }
 }
}