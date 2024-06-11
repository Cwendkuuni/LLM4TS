package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NumericEntityUnescaper
 */
public class NumericEntityUnescaperTest {

 @Test
 public void testTranslationOfNumericEntities() {
 String input = "&#x26;&#xA0;&#x3C3;&#x2260;&#x26;&#x26;&#xA0;&#x2260;&#x26;&#x26;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();

 int translationLength = unescaper.translate(CharSequence.wrap(input), 0, output);
 String expectedOutput = "≈&BLOCKS&≈";
 assertEquals(expectedOutput.length(), output.length());
 assertEquals(expectedOutput, output.toString());
 }

 @Test(expected = IOException.class)
 public void testTranslationOfEmptyString() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer output = new StringWriter();
 int translationLength = unescaper.translate(CharSequence.EMPTY, 0, output);
 fail("Expected IOException but no exception was thrown");
 }

 @Test(expected = IOException.class)
 public void testTranslationOfNullInput() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 Writer output = new StringWriter();
 int translationLength = unescaper.translate(null, 0, output);
 fail("Expected IOException but no exception was thrown");
 }

 @Test
 public void testTranslationOfInvalidEntity() {
 String input = "&%#x26;";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int translationLength = unescaper.translate(CharSequence.wrap(input), 0, output);
 assertEquals(0, translationLength);
 }

 @Test
 public void testTranslationOfInvalidHexEntity() {
 String input = "&#xzx";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int translationLength = unescaper.translate(CharSequence.wrap(input), 0, output);
 assertEquals(0, translationLength);
 }

 @Test
 public void testTranslationOfInvalidDecimalEntity() {
 String input = "&#1234567890123456";
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 StringBuilder output = new StringBuilder();
 int translationLength = unescaper.translate(CharSequence.wrap(input), 0, output);
 assertEquals(0, translationLength);
 }
}