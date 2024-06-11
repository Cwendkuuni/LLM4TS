package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.junit.Test;

public class NumericEntityUnescaperTest {

 // Test constructor
 @Test
 public void testNumericEntityUnescaperConstructor() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 assertNotNull(unescaper);

 NumericEntityUnescaper unescaperWithOptions = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
 assertNotNull(unescaperWithOptions);
 }

 // Test isSet method
 @Test
 public void testIsSet() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 assertFalse(unescaper.isSet(OPTION.semiColonRequired));
 assertFalse(unescaper.isSet(OPTION.semiColonOptional));
 assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));

 NumericEntityUnescaper unescaperWithOptions = new NumericEntityUnescaper(OPTION.semiColonRequired);
 assertTrue(unescaperWithOptions.isSet(OPTION.semiColonRequired));
 assertFalse(unescaperWithOptions.isSet(OPTION.semiColonOptional));
 assertFalse(unescaperWithOptions.isSet(OPTION.errorIfNoSemiColon));

 NumericEntityUnescaper unescaperWithMultipleOptions = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
 assertTrue(unescaperWithMultipleOptions.isSet(OPTION.semiColonRequired));
 assertTrue(unescaperWithMultipleOptions.isSet(OPTION.errorIfNoSemiColon));
 assertFalse(unescaperWithMultipleOptions.isSet(OPTION.semiColonOptional));
 }

 // Test translate method with default options
 @Test
 public void testTranslateWithDefaultOptions() throws Exception {
 String input = "Hello &amp;#x1F601; World";
 String expectedOutput = "Hello 🌍 World";
 StringWriter output = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int translatedLength = unescaper.translate(new StringCharSequence(input), 0, output);
 assertEquals(expectedOutput, output.toString());
 assertEquals(input.length(), translatedLength);
 }

 // Test translate method with semiColonOptional option
 @Test
 public void testTranslateWithSemiColonOptional() throws Exception {
 String input = "Hello &amp;#x1F601; World";
 String expectedOutput = "Hello 🌍 World";
 StringWriter output = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 int translatedLength = unescaper.translate(new StringCharSequence(input), 0, output);
 assertEquals(expectedOutput, output.toString());
 assertEquals(input.length(), translatedLength);
 }

 // Test translate method with errorIfNoSemiColon option
 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithErrorIfNoSemiColon() throws Exception {
 String input = "Hello &amp;#x1F601";
 StringWriter output = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 int translatedLength = unescaper.translate(new StringCharSequence(input), 0, output);
 fail("Expected IllegalArgumentException to be thrown");
 }

 // Test translate method with invalid hex entity
 @Test
 public void testTranslateWithInvalidHexEntity() throws Exception {
 String input = "Hello &amp;#xZ1F601; World";
 StringWriter output = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int translatedLength = unescaper.translate(new StringCharSequence(input), 0, output);
 assertEquals(input, output.toString());
 assertEquals(input.length(), translatedLength);
 }

 // Test translate method with valid hex entity
 @Test
 public void testTranslateWithValidHexEntity() throws Exception {
 String input = "Hello &amp;#x1F601; World";
 String expectedOutput = "Hello 🌍 World";
 StringWriter output = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int translatedLength = unescaper.translate(new StringCharSequence(input), 0, output);
 assertEquals(expectedOutput, output.toString());
 assertEquals(input.length(), translatedLength);
 }

 // Test translate method with semiColonRequired option and missing semicolon
 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithMissingSemiColonAndSemiColonRequired() throws Exception {
 String input = "Hello &amp;#x1F601";
 StringWriter output = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 int translatedLength = unescaper.translate(new StringCharSequence(input), 0, output);
 fail("Expected IllegalArgumentException to be thrown");
 }

 // Test translate method with semiColonRequired and semiColonOptional options
 @Test
 public void testTranslateWithSemiColonRequiredAndOptional() throws Exception {
 String input = "Hello &amp;#x1F601; World &amp;#xA9;";
 String expectedOutput = "Hello 🌍 World ©";
 StringWriter output = new StringWriter();
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.semiColonOptional);
 int translatedLength = unescaper.translate(new StringCharSequence(input), 0, output);
 assertEquals(expectedOutput, output.toString());
 assertEquals(input.length(), translatedLength);
 }
}