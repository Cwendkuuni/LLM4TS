package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.Format;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;

public class ExtendedMessageFormatTest {

 private static final String TEST_PATTERN = "{0,date,dd.MM.yyyy},{1},{2,number,number}";

 @SuppressWarnings("unchecked")
 private static Map<String, FormatFactory> getFormatFactoryMap() {
 return new HashMap<String, FormatFactory>() {{
 put("date", (FormatFactory) (String dateFormat) -> new SimpleDateFormat(dateFormat));
 put("number", (FormatFactory) (String numberFormat, Locale locale) -> new DecimalFormat(numberFormat, NumberFormat.getNumberInstance(locale)));
 }};
 }

 @org.junit.Test
 public void testConstructors() {
 // Test default constructor
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("");
 assertEquals("", fmt.toPattern());
 assertNull(fmt.registry);

 // Test constructor with pattern
 ExtendedMessageFormat fmtPattern = new ExtendedMessageFormat(TEST_PATTERN);
 assertEquals(TEST_PATTERN, fmtPattern.toPattern());
 assertNull(fmtPattern.registry);

 // Test constructor with pattern and locale
 ExtendedMessageFormat fmtPatternLocale = new ExtendedMessageFormat(TEST_PATTERN, Locale.ENGLISH);
 assertEquals(TEST_PATTERN, fmtPatternLocale.toPattern());
 assertNull(fmtPatternLocale.registry);

 // Test constructor with pattern, locale, and registry
 ExtendedMessageFormat fmtPatternLocaleRegistry = new ExtendedMessageFormat(TEST_PATTERN, Locale.ENGLISH, getFormatFactoryMap());
 assertEquals(TEST_PATTERN, fmtPatternLocaleRegistry.toPattern());
 assertNotNull(fmtPatternLocaleRegistry.registry);
 }

 @org.junit.Test
 public void testApplyPattern() {
 ExtendedMessageFormat fmtPatternLocaleRegistry = new ExtendedMessageFormat(TEST_PATTERN, Locale.ENGLISH, getFormatFactoryMap());
 String pattern = fmtPatternLocaleRegistry.toPattern();

 // Test empty pattern
 assertEquals("", new ExtendedMessageFormat("").toPattern());

 // Test simple pattern without format elements
 assertEquals(pattern, new ExtendedMessageFormat(pattern).toPattern());

 // Test pattern with format elements
 String testString = "Test {0}, {1}, {2}";
 ExtendedMessageFormat fmt = new ExtendedMessageFormat(testString, Locale.ENGLISH, getFormatFactoryMap());
 assertEquals(TEST_PATTERN, fmt.toPattern());

 // Test pattern with invalid format elements
 String invalidTestString = "Test {0}, {1, date}, {2, number, wrong_locale}";
 try {
 ExtendedMessageFormat invalidFmt = new ExtendedMessageFormat(invalidTestString, Locale.ENGLISH, getFormatFactoryMap());
 fail("Should fail with IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testGetFormat() {
 ExtendedMessageFormat fmtPatternLocaleRegistry = new ExtendedMessageFormat(TEST_PATTERN, Locale.ENGLISH, getFormatFactoryMap());

 // Test getFormat with valid format description
 String dateFormat = "dd.MM.yyyy";
 Format dateFormatObj = fmtPatternLocaleRegistry.getFormat("date");
 Format expectedDateFormatObj = new SimpleDateFormat(dateFormat);
 assertEquals(expectedDateFormatObj, fmtPatternLocaleRegistry.getFormat("date"));

 // Test getFormat with invalid format description
 String wrongFormatDescription = "wrongFormat";
 assertNull(fmtPatternLocaleRegistry.getFormat(wrongFormatDescription));
 }

 @org.junit.Test
 public void testEqualsAndHashCode() {
 ExtendedMessageFormat fmt1 = new ExtendedMessageFormat(TEST_PATTERN, Locale.ENGLISH, getFormatFactoryMap());
 ExtendedMessageFormat fmt2 = new ExtendedMessageFormat(TEST_PATTERN, Locale.ENGLISH, getFormatFactoryMap());
 ExtendedMessageFormat fmt3 = new ExtendedMessageFormat("{0},{1},{2}", Locale.ENGLISH, getFormatFactoryMap());
 ExtendedMessageFormat fmt4 = new ExtendedMessageFormat(TEST_PATTERN, Locale.ENGLISH, null);

 // Test equals and hashCode with same ExtendedMessageFormat
 assertTrue(fmt1.equals(fmt1));
 assertEquals(fmt1.hashCode(), fmt1.hashCode());

 // Test equals and hashCode with different ExtendedMessageFormat but same pattern and locale
 assertTrue(fmt1.equals(fmt2));
 assertEquals(fmt1.hashCode(), fmt2.hashCode());

 // Test equals and hashCode with different ExtendedMessageFormat but different pattern and locale
 assertFalse(fmt1.equals(fmt3));
 assertNotEquals(fmt1.hashCode(), fmt3.hashCode());

 // Test equals and hashCode with different ExtendedMessageFormat but same pattern and null registry
 assertFalse(fmt1.equals(fmt4));
 assertNotEquals(fmt1.hashCode(), fmt4.hashCode());
 }

}