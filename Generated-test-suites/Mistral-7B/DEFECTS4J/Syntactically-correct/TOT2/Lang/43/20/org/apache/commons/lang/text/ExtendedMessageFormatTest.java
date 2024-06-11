package org.apache.commons.lang.text;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

// Test case for constructor with pattern
@Test
public void testExtendedMessageFormat_constructor_pattern() {
 String pattern = "Hello {0}, your age is {1}, and today is {2}";
 ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
 assertEquals(pattern, format.toPattern());
}

// Test case for constructor with pattern and locale
@Test
public void testExtendedMessageFormat_constructor_pattern_locale() {
 String pattern = "Hello {0}, your age is {1}, and today is {2}";
 ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
 assertEquals(pattern, format.toPattern());
}

// Test case for constructor with pattern, locale, and registry
@Test
public void testExtendedMessageFormat_constructor_pattern_locale_registry() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new DateFormatFactory());
 String pattern = "Hello {0}, your age is {1}, and today is {date, date, MM-dd-yyyy}";
 ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US, registry);
 assertEquals(pattern, format.toPattern());
}

// Test case for toPattern method
@Test
public void testExtendedMessageFormat_toPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0}-{1}", Locale.US);
 assertEquals("{0}-{1}", format.toPattern());
}

// Test case for applyPattern method with valid pattern
@Test
public void testExtendedMessageFormat_applyPattern_valid_pattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}");
 format.applyPattern("Hello {0}, your age is {1}");
}

// Test case for applyPattern method with invalid pattern
@Test(expected = IllegalArgumentException.class)
public void testExtendedMessageFormat_applyPattern_invalid_pattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}");
 format.applyPattern("Hello {0}, your age is {1}, and today is {2}");
}

// Test case for getFormat method with valid format name
@Test
public void testExtendedMessageFormat_getFormat_valid_format_name() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new DateFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("", null, registry);
 format.applyPattern("Hello {0}, your age is {date, date, MM-dd-yyyy}");
 Format dateFormat = format.getFormat("date");
 assertNotNull(dateFormat);
}

// Test case for getFormat method with invalid format name
@Test(expected = IllegalStateException.class)
public void testExtendedMessageFormat_getFormat_invalid_format_name() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new DateFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("", null, registry);
 format.applyPattern("Hello {0}, your age is {invalidFormat, date, MM-dd-yyyy}");
 try {
 format.getFormat("invalidFormat");
 } catch (IllegalStateException e) {
 fail("Expected IllegalArgumentException, got " + e.getMessage());
 }
}

// Test case for format method with valid arguments
@Test
public void testExtendedMessageFormat_format_valid_arguments() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}");
 format.applyPattern("Hello {0}, your age is {1}");
 Object[] args = {"John Doe", 30};
 String result = format.format(args);
 assertEquals("Hello John Doe, your age is 30", result);
}

// Test case for format method with insufficient arguments
@Test(expected = IllegalArgumentException.class)
public void testExtendedMessageFormat_format_insufficient_arguments() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}");
 format.applyPattern("Hello {0}, your age is {1}");
 Object[] args = {"John Doe"};
 try {
 format.format(args);
 } catch (IllegalArgumentException e) {
 fail("Expected FormatException, got " + e.getMessage());
 }
}

// Test case for format method with too many arguments
@Test(expected = IllegalArgumentException.class)
public void testExtendedMessageFormat_format_too_many_arguments() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}");
 format.applyPattern("Hello {0}, your age is {1}");
 Object[] args = {"John Doe", 30, "Today is"};
 try {
 format.format(args);
 } catch (IllegalArgumentException e) {
 fail("Expected FormatException, got " + e.getMessage());
 }
}

// Test case for format method with custom format
@Test
public void testExtendedMessageFormat_format_custom_format() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {date, date, MM-dd-yyyy}");
 format.applyPattern("Hello {0}, your age is {date, date, MM-dd-yyyy}");
 Object[] args = {"John Doe", new Date()};
 String result = format.format(args);
 SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
 String expected = String.format("Hello John Doe, your age is %s",
 dateFormat.format(args[1]));
 assertEquals(expected, result);
}

// Test case for parse method with valid pattern
@Test
public void testExtendedMessageFormat_parse_valid_pattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}");
 format.applyPattern("Hello {0}, your age is {1}");
 String input = "Hello John Doe, your age is 30";
 Object[] args = format.parse(input);
 assertEquals(2, args.length);
 assertEquals("John Doe", args[0]);
 assertEquals(30, args[1]);
}

// Test case for parse method with invalid pattern
@Test(expected = ParseException.class)
public void testExtendedMessageFormat_parse_invalid_pattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}");
 format.applyPattern("Hello {0}, your age is {1}");
 String input = "Hello John Doe, your age is 30, and today is Monday";
 try {
 format.parse(input);
 } catch (ParseException e) {
 // Expected
 }
}

// Test case for parse method with insufficient arguments
@Test(expected = ParseException.class)
public void testExtendedMessageFormat_parse_insufficient_arguments() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}");
 format.applyPattern("Hello {0}, your age is {1}");
 String input = "Hello John Doe, your age is";
 try {
 format.parse(input);
 } catch (ParseException e) {
 fail("Expected no exception, got " + e.getMessage());
 }
}

// Test case for parse method with too many arguments
@Test(expected = ParseException.class)
public void testExtendedMessageFormat_parse_too_many_arguments() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}");
 format.applyPattern("Hello {0}, your age is {1}");
 String input = "Hello John Doe, your age is 30, and today is Monday";
 Object[] args = format.parse(input);
 try {
 format.parse(input, args);
 } catch (ParseException e) {
 fail("Expected no exception, got " + e.getMessage());
 }
}

// Test case for parse method with custom format
@Test
public void testExtendedMessageFormat_parse_custom_format() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {date, date, MM-dd-yyyy}");
 format.applyPattern("Hello {0}, your age is {date, date, MM-dd-yyyy}");
 String input = "Hello John Doe, your age is 12/31/2019";
 Object[] args = format.parse(input);
 SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
 Date expected = dateFormat.parse("12/31/2019");
 assertEquals(2, args.length);
 assertEquals("John Doe", args[0]);
 assertEquals(expected, args[1]);
}
}