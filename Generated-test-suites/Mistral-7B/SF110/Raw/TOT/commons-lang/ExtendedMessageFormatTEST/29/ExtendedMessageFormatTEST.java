package org.apache.commons.lang3.text;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.junit.Test;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

 private static final String TEST_PATTERN = "{0,date,yyyy-MM-dd HH:mm:ss.SSS} {1,number,#,##0.##} {2,choice,0#Hello|1#World}";
 private static final FormatFactory DATE_FORMAT_FACTORY = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
 private static final FormatFactory NUMBER_FORMAT_FACTORY = new DecimalFormat("#,##0.##");
 private static final FormatFactory CHOICE_FORMAT_FACTORY = new ChoiceFormat(new Object[]{"Hello", "World"}, new int[]{0, 1});

 @Test
 public void testSimpleFormatting() {
 final ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN);
 final Object[] args = new Object[]{new Date(), 123.456, 0};
 final String formattedMessage = format.format(args);
 final String expectedMessage = "2022-10-10 15:20:30.456 123.456 Hello";
 assertEquals(expectedMessage, formattedMessage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingFormatElement() {
 final ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1} {3}");
 final Object[] args = new Object[]{new Date(), 123.456};
 final String formattedMessage = format.format(args);
 fail("Expected IllegalArgumentException, but got: " + formattedMessage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatArgumentIndex() {
 final ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN);
 final Object[] args = new Object[]{new Date(), 123.456, 3};
 final String formattedMessage = format.format(args);
 fail("Expected IllegalArgumentException, but got: " + formattedMessage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnmatchedCloseFormatElement() {
 final ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1} {2} {3} {4} {5}}");
 final Object[] args = new Object[]{new Date(), 123.456, "Hello", 0};
 final String formattedMessage = format.format(args);
 fail("Expected IllegalArgumentException, but got: " + formattedMessage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatDescription() {
 final ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN);
 final Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("format.date", null);
 final ExtendedMessageFormat formattedMessage = new ExtendedMessageFormat(TEST_PATTERN, registry);
 formattedMessage.applyPattern(TEST_PATTERN);
 fail("Expected IllegalArgumentException, but got a formatted message");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingFormatFactory() {
 final ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN, new HashMap<>());
 format.applyPattern(TEST_PATTERN);
 final Object[] args = new Object[]{new Date(), 123.456, 0};
 try {
 format.format(args);
 fail("Expected MissingResourceException, but got: " + format.format(args));
 } catch (MissingResourceException e) {
 // Expected
 }
 }

 @Test
 public void testCustomFormatElement() {
 final ExtendedMessageFormat format = new ExtendedMessageFormat(TEST_PATTERN, new HashMap<>());
 final Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("format.date", DATE_FORMAT_FACTORY);
 registry.put("format.number", NUMBER_FORMAT_FACTORY);
 registry.put("format.choice", CHOICE_FORMAT_FACTORY);
 format.applyPattern(TEST_PATTERN, registry);
 final Object[] args = new Object[]{new Date(), 123.456, 1};
 final String formattedMessage = format.format(args);
 final String expectedMessage = "2022-10-10 15:20:30.456 123.456 World";
 assertEquals(expectedMessage, formattedMessage);
 }

}