package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

import org.junit.Test;

public class NumericEntityUnescaperTest {

 private static final String TEST_INPUT = "&amp;#160;&#x20AC;&#032;&#x1F602;&#128337;asdf&amp;#1337;";
 private static final String EXPECTED_OUTPUT = "&nbsp;&#xe2;&#x20ac; 💩 asdf &amp;#1337;";

 @Test
 public void testTranslateMethodWithDefaultOptions() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
  StringWriter writer = new StringWriter();
  int translatedChars = unescaper.translate(new String(TEST_INPUT), 0, writer);
  assertEquals(EXPECTED_OUTPUT, writer.toString());
  assertEquals(TEST_INPUT.length(), translatedChars);
 }

 @Test
 public void testTranslateMethodWithSemiColonRequiredOption() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
  StringWriter writer = new StringWriter();
  int translatedChars = unescaper.translate(new String(TEST_INPUT), 0, writer);
  assertEquals(EXPECTED_OUTPUT + ";", writer.toString());
  assertEquals(TEST_INPUT.length() + 1, translatedChars);
 }

 @Test
 public void testTranslateMethodWithSemiColonOptionalOption() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
  StringWriter writer = new StringWriter();
  int translatedChars = unescaper.translate(new String(TEST_INPUT), 0, writer);
  assertEquals(EXPECTED_OUTPUT, writer.toString());
  assertEquals(TEST_INPUT.length(), translatedChars);
 }

 @Test
 public void testTranslateMethodWithErrorIfNoSemiColonOption() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
  StringWriter writer = new StringWriter();
  String testInput = TEST_INPUT + " ";
  try {
   int translatedChars = unescaper.translate(new String(testInput), 0, writer);
   fail("Expected IllegalArgumentException: Semi-colon required at end of numeric entity");
  } catch (IllegalArgumentException e) {
   // Expected
  }
 }

 @Test
 public void testTranslateMethodWithInvalidCharacterSequence() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
  StringWriter writer = new StringWriter();
  String testInput = "&amp;#160;&#x20AC;&#032;&#x1F602;&#128337;&%";
  try {
   int translatedChars = unescaper.translate(new String(testInput), 0, writer);
   fail("Expected 0 as the method should not translate the invalid sequence '%'");
  } catch (IllegalArgumentException e) {
   // Expected
  }
 }

 @Test
 public void testTranslateMethodWithInvalidHexCharacterSequence() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
  StringWriter writer = new StringWriter();
  String testInput = "&amp;#xZZZZ";
  try {
   int translatedChars = unescaper.translate(new String(testInput), 0, writer);
   fail("Expected 0 as the method should not translate the invalid hex sequence 'ZZZZ'");
  } catch (IllegalArgumentException e) {
   // Expected
  }
 }

 @Test
 public void testTranslateMethodWithInvalidLengthHexCharacterSequence() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
  StringWriter writer = new StringWriter();
  String testInput = "&amp;#xAB";
  try {
   int translatedChars = unescaper.translate(new String(testInput), 0, writer);
   fail("Expected 0 as the method should not translate a hex sequence of length 2");
  } catch (IllegalArgumentException e) {
   // Expected
  }
 }

 @Test
 public void testTranslateMethodWithZeroLengthHexCharacterSequence() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
  StringWriter writer = new StringWriter();
  String testInput = "&amp;#x";
  try {
   int translatedChars = unescaper.translate(new String(testInput), 0, writer);
   fail("Expected 0 as the method should not translate an empty hex sequence");
  } catch (IllegalArgumentException e) {
   // Expected
  }
 }

 @Test
 public void testTranslateMethodWithInvalidDecCharacterSequence() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
  StringWriter writer = new StringWriter();
  String testInput = "&amp;#abc";
  try {
   int translatedChars = unescaper.translate(new String(testInput), 0, writer);
   fail("Expected 0 as the method should not translate the invalid dec sequence 'abc'");
  } catch (IllegalArgumentException e) {
   // Expected
  }
 }

 @Test
 public void testTranslateMethodWithInvalidLengthDecCharacterSequence() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
  StringWriter writer = new StringWriter();
  String testInput = "&amp;#12345";
  try {
   int translatedChars = unescaper.translate(new String(testInput), 0, writer);
   fail("Expected 0 as the method should not translate a dec sequence of length > 5");
  } catch (IllegalArgumentException e) {
   // Expected
  }
 }

 @Test
 public void testTranslateMethodWithZeroLengthDecCharacterSequence() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
  StringWriter writer = new StringWriter();
  String testInput = "&amp;#";
  try {
   int translatedChars = unescaper.translate(new String(testInput), 0, writer);
   fail("Expected 0 as the method should not translate an empty dec sequence");
  } catch (IllegalArgumentException e) {
   // Expected
  }
 }

 @Test
 public void testTranslateMethodWithValidCharacterSequences() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
  StringWriter writer = new StringWriter();
  int translatedChars = 0;
  for (OPTION option : Arrays.asList(OPTION.values())) {
   String testInput = "&amp;" + Arrays.asList(OPTION.values())
        .stream()
        .filter(option1 -> !option.equals(option))
        .map(option1 -> option1.name().charAt(0) + Integer.toHexString(option1.ordinal()))
        .map(hex -> "&" + "#x" + hex)
        .collect(Collectors.joining());
    translatedChars += unescaper.translate(new String(testInput), 0, writer);
  }
  assertEquals(EXPECTED_OUTPUT.length(), translatedChars);
 }

}