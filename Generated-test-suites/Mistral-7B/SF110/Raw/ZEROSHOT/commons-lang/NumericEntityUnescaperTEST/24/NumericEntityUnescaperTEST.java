package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import org.junit.Test;

public class NumericEntityUnescaperTest {

 private static final String TEST_INPUT = "&amp;#160;&#x00A0;&#032;&#065536;&#X1F600;&#x1F601;abc&def&ghi&";
 private static final String TEST_OUTPUT = " \u00A0 \u0020 \uF0010 \uD835\uDDE8 \uD835\uDDE9 abc def ghi ";

 @Test
 public void testNumericEntityUnescaper() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
  StringWriter out = new StringWriter();
  int index = 0;

  for (char c : TEST_INPUT.toCharArray()) {
   int translatedLength = unescaper.translate(new String(TEST_INPUT), index, out);
   index += translatedLength;
  }

  String actualOutput = out.toString();
  assertEquals(TEST_OUTPUT, actualOutput);
 }

 @Test
 public void testNumericEntityUnescaperWithOptions() {
  EnumSet<NumericEntityUnescaper.OPTION> options = EnumSet.of(NumericEntityUnescaper.OPTION.semiColonRequired);
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper(options.toArray(new NumericEntityUnescaper.OPTION[0]));
  StringWriter out = new StringWriter();
  int index = 0;

  for (char c : TEST_INPUT.toCharArray()) {
   int translatedLength = unescaper.translate(new String(TEST_INPUT), index, out);
   index += translatedLength;
  }

  String actualOutput = out.toString();
  assertEquals(TEST_OUTPUT, actualOutput);

  String invalidInput = TEST_INPUT + " ";
  boolean exceptionThrown = false;

  try {
   for (char c : invalidInput.toCharArray()) {
    int translatedLength = unescaper.translate(new String(invalidInput), index, out);
    index += translatedLength;
   }
  } catch (IllegalArgumentException e) {
   exceptionThrown = true;
  }

  assertTrue(exceptionThrown);
 }

 @Test
 public void testNumericEntityUnescaperWithHexValues() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
  StringWriter out = new StringWriter();
  int index = 0;

  for (char c : ("&amp;#x1F600;&#x1F601;".toCharArray())) {
   int translatedLength = unescaper.translate(new String("&amp;#x1F600;&#x1F601;"), index, out);
   index += translatedLength;
  }

  String actualOutput = out.toString();
  assertEquals("\uD835\uDDE8 \uD835\uDDE9", actualOutput);
 }

 @Test
 public void testNumericEntityUnescaperWithInvalidHexValues() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
  StringWriter out = new StringWriter();
  int index = 0;

  for (char c : ("&amp;#xF0011;".toCharArray())) {
   int translatedLength = unescaper.translate(new String("&amp;#xF0011;"), index, out);
   index += translatedLength;
  }

  String actualOutput = out.toString();
  assertEquals("", actualOutput);
 }

 @Test
 public void testNumericEntityUnescaperWithLargeValue() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
  StringWriter out = new StringWriter();
  int index = 0;

  for (char c : ("&amp;#29999;".toCharArray())) {
   int translatedLength = unescaper.translate(new String("&amp;#29999;"), index, out);
   index += translatedLength;
  }

  String actualOutput = out.toString();
  byte[] expectedOutput = { 0xFF, 0xFF };
  assertArrayEquals(expectedOutput, Character.toByteArray(TEST_OUTPUT.substring(TEST_OUTPUT.length() - 2)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNumericEntityUnescaperWithMissingSemiColon() {
  NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
  StringWriter out = new StringWriter();
  int index = 0;

  for (char c : ("&amp;#160;".toCharArray())) {
   int translatedLength = unescaper.translate(new String("&amp;#160"), index, out);
   index += translatedLength;
  }

  String actualOutput = out.toString();
  assertEquals("", actualOutput);
 }
}