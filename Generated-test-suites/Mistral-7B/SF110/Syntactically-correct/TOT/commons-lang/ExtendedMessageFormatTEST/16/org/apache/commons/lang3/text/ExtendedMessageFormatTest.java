package org.apache.commons.lang3.text;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;
import java.text.Format;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

 @Test
 public void testSimpleFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}");
 format.applyPattern(format.toPattern());
 String message = format.format(new Object[]{"John", 30});
 assertEquals("Hello John, your age is 30", message);
 }

 @Test
 public void testEmptyFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("");
 format.applyPattern("");
 String message = format.format(new Object[0]);
 assertEquals("", message);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, {1, {2}}");
 try {
 format.applyPattern(format.toPattern());
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormatRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, 'short'} {1, number, #, ##0.00}");
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", () -> new SimpleDateFormat("dd-MM-yyyy", Locale.US));
 registry.put("number", () -> new DecimalFormat("#,##0.00"));
 ExtendedMessageFormat formatWithRegistry = new ExtendedMessageFormat(format.toPattern(), null, registry);
 formatWithRegistry.applyPattern(formatWithRegistry.toPattern());
 String message = formatWithRegistry.format(new Object[]{new Date(), 123.45d});
 assertEquals("25-11-2021 123.45", message);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatDescription() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, 'invalid'} {1, date, 'short'}");
 try {
 format.applyPattern(format.toPattern());
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, 'missing'} {1, number, #, ##0.00}");
 Map<String, FormatFactory> registry = new HashMap<>();
 ExtendedMessageFormat formatWithRegistry = new ExtendedMessageFormat(format.toPattern(), null, registry);
 try {
 formatWithRegistry.applyPattern(formatWithRegistry.toPattern());
 } catch (IllegalArgumentException e) {
 String message = e.getMessage();
 assertTrue(message.contains("'missing'"));
 // expected
 }
 }

 @Test
 public void testCustomFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, customFormat, 'CustomFormat'} {1, number, #, ##0.00}");
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("CustomFormat", () -> new CustomFormat());
 ExtendedMessageFormat formatWithRegistry = new ExtendedMessageFormat(format.toPattern(), null, registry);
 formatWithRegistry.applyPattern(formatWithRegistry.toPattern());
 String message = formatWithRegistry.format(new Object[]{1, 123.45d});
 assertEquals("CustomFormat 1 123.45", message);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCustomFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, customFormat, 'InvalidFormat'} {1, number, #, ##0.00}");
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("InvalidFormat", () -> new InvalidFormat());
 ExtendedMessageFormat formatWithRegistry = new ExtendedMessageFormat(format.toPattern(), null, registry);
 try {
 formatWithRegistry.applyPattern(formatWithRegistry.toPattern());
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormatArgumentOutOfBounds() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {2}");
 try {
 format.applyPattern(format.toPattern());
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormatArgumentNull() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}") {
 @Override
 public void setFormat(int formatElementIndex, Format newFormat) {
 super.setFormat(formatElementIndex, new NullFormat());
 }
 };
 format.applyPattern(format.toPattern());
 String message = format.format(new Object[]{null});
 assertEquals("Hello null", message);
 }

 @Test(expected = ParseException.class)
 public void testFormatParseException() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1, date, 'long'}");
 format.applyPattern(format.toPattern());
 try {
 format.format(new Object[]{"John", new Date(0)});
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testFormatParseExceptionWithCustomMessage() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1, date, 'long'}");
 format.applyPattern(format.toPattern());
 format.setErrorMessage("Custom error message");
 try {
 format.format(new Object[]{"John", new Date(0)});
 } catch (ParseException e) {
 assertEquals("Custom error message", e.getMessage());
 }
 }
}